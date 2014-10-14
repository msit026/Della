package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import persistence.DBManager;

/**
 * <p>
 * Title: ActionItemManager
 * </p>
 *
 * <p>
 * Description: A class to organize and manage all known action items
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2007
 * </p>
 *
 * @author Lynn Robert Carter
 * @version 1.09
  * Many thanks to Harry Sameshima for his original work.
*/
public class ActionItemManager {

	//---------------------------------------------------------------------------------------------------------------------
	// Constants

	private static final int noItemSelected = -1;	// Added for Della01

	// Added for Della02 (start)
	public static final int sortingFactorNone = 0;				// The constants for sorting factors
	public static final int sortingFactorCreationDate = 1;
	public static final int sortingFactorDueDate = 2;
	public static final String[] sortingFactorStrings = {"None", "Creation Date", "Due Date", 
		"Assigned Member", "Assigned Team"}; // Modified for Della05 and Della09

	public static final int sortingDirectionSmallToLarge = 0;	// The constants for sorting direction
	public static final int sortingDirectionLargeToSmall = 1;
	public static final String[] sortDirectionStrings = {"Small to Large", "Large to Small"};
	// Added for Della02 (end)

	// Added for Della10 (start)
	public static final int inclusionFactorAll = 0;				// The constants for inclusion factors
	public static final int inclusionFactorOpen = 1;
	public static final int inclusionFactorClosed = 2;
	public static final String[] inclusionFactorStrings = {"All Action Items", "Open Action Items", "Closed Action Items"};
	// Added for Della10 (end)

	private transient SimpleDateFormat dateFormat = null;

	public static final int statusOpen = 0;			// The constants for Open and Close
	public static final int statusClosed = 1;
	public static final String[] statusStrings = {"Open", "Closed"};

	private boolean editChangesPending;

	//---------------------------------------------------------------------------------------------------------------------
	// Attributes
	
	// Added for Della01
	public ArrayList <ActionItem> ai_all = null;
	

	private ActionItem currentActionItem = null;	// Currently displayed action item
	private ActionItem emptyActionItem = null;		// The standard empty action item
    
    // Added for Della01
    private AccessItemManager accessManager = null;
    private int sortDirection = 0;
	
	// Added for Della02 (start)
	private int sortFactor1 = 0;
	private int sortFactor2 = 0;
	// Added for Della02 (end)

	// Added for Della10 (start)
	private int inclusionFactor = 0;
	// Added for Della10 (end)

	// Added for Della04 (start)
	private ElementList memberList = null;
	// Added for Della04 (end)

	// Added for Della06 (start)
	private ElementList teamList = null;
	// Added for Della06 (end)

	//---------------------------------------------------------------------------------------------------------------------

	/**
	 * The ActionItemManager class constructor.
	 * 
	 */
	public ActionItemManager() {
		currentActionItem = new ActionItem();
		emptyActionItem = new ActionItem();
		emptyActionItem.setCreatedDate(null);
        
    	// Added for Della01
        
        accessManager = new AccessItemManager();

		// Added for Della04
		memberList = new ElementList();

		// Added for Della06
		teamList = new ElementList();
		if(ai_all == null)
			ai_all = new ArrayList<ActionItem>();
	}

	/**
	 * actionItemShouldBeIncluded is used to determine whether on not the inclustion factor would include a node.
	 * 
	 * Added for Della10
	 */
	private boolean actionItemShouldBeIncluded(ActionItem ai){
		if (inclusionFactor == 0) return true;
		if ((inclusionFactor == 1) && (ai.getStatus().compareToIgnoreCase("Open")==0)) return true;
		if ((inclusionFactor == 2) && (ai.getStatus().compareToIgnoreCase("Closed")==0)) return true;
		return false;
	}

    /**
     * reestablishActionItemAccessList is used to rebuild the access list any time some factor at the
     * heart of this list changes.  So far, these are the events that cause the list to be updated:
     * 		1. New action item added to the list of action items
     * 		2. An existing action item is updated
     * 
     * Added for Della01
     */
    private void reestablishActionItemAccessList(){
     	int resultListSize = 0;
     	int inputListSize = ai_all.size();
    	String[] names = new String[inputListSize];
    	int[] nameIndexes = new int[inputListSize];

    	for (int i = 0; i < inputListSize; i++){
    		// Updated for Della02
    		ActionItem ai = ai_all.get(i);
			if (actionItemShouldBeIncluded(ai)){	// Added for Della10
    			names[resultListSize] = getSortFactorText(ai, sortFactor1) + getSortFactorText(ai, sortFactor2) + ai.getActionItemName();
    			nameIndexes[resultListSize] = i;
    			resultListSize++;
			}
    	}
    	accessManager.establishSortedAccessList(names, nameIndexes, resultListSize, sortDirection);
    }

//    /**
//     * get an array of the processed action item names from the access list
//     * @return the array of names
//     * 
//     * Added for Della01
//     */
//    public String[] getActionItemNames(){
//		int listSize = accessManager.size();
//    	String[] names = new String[listSize];
//    	for (int i = 0; i < listSize; i++) {
//    		names[i] = accessManager.getAccessName(i);
//    	}
//    	
//    	return names;
//    }
    
    /**
     * Added for new della DB included
     */
    public List<String> getActionItemNames()
    {
    	return DBManager.getActionItemNames();
    }
    
	/**
	 * Create a new action item and add it to the list of action items
	 * @param name String
	 * @param description String
	 * @param resolution String
	 * @param status String
	 * @param dueDateStr String
	 * @return ActionItem
	 * 
	 * Added for Della01; Modified for Della05; Modified for Della09
	 */
	public ActionItem createActionItem(String name, String description,
			String resolution, String status,
			String dueDateStr, String member, String team) throws Exception {
		ActionItem ai = new ActionItem(name, description, resolution, status, member, team);
		validateActionItem(ai, name, dueDateStr, true, 0);
		setCurrentActionItem(ai);
		if(DBManager.writeData(ai))
		{
			ai_all.ensureCapacity((1 + ai_all.size()/10)*10);	// This expands the list, if necessary, in blocks of 10 names
			ai_all.add(ai);
			reestablishActionItemAccessList();
			return ai;
		}
		else
		{
			return null;
		}
	}
    
	/**
	 * Update an existing action item based on the parameters pass to the routine
	 * @param name String
	 * @param description String
	 * @param resolution String
	 * @param status String
	 * @param dueDateStr String
	 * @return ActionItem
	 */
	public ActionItem updateActionItem(String name, String description,
			String resolution, String status,
			String dueDateStr, 
			int itemIndex,	// Added for Della01
			String member,	// Added for Della05
			String team)	// Added for Della09
	throws Exception {
		if (itemIndex == noItemSelected) {	// Added for Della01
			throw new Exception("No action item has been selected to update!   ");
		}
		// Just allocate a new action item and save it.  The inner workings of
		// this method will change drastically in Task 01 so I don't care about
		// being wasteful now.
		ActionItem ai = new ActionItem(name, description, resolution, status, member, team); // Modified for Della05 and Della09
        
        // Added for Della01
        int actualItemIndex = accessManager.getAccessIndex(itemIndex);

		// Check if there are problems with the modifications.
		validateActionItem(ai, name, dueDateStr, false, actualItemIndex);	// Modified for Della01

        // We passed the tests so it's ok to set the new current action item
    	// Added for Della01
		ai_all.set(actualItemIndex, ai);
		reestablishActionItemAccessList();

		// We passed the tests so it's ok to set the new current action item
		setCurrentActionItem(ai);
		return ai;
	}

	/**
	 * Delete the current action item for the list of action items.
	 * 
	 * Added for Della10
	 */
	public void deleteCurrentActionItem(){
		int listSize = ai_all.size();
		for (int i = 0; i < listSize; i++){
			ActionItem ai = ai_all.get(i);
			if (ai.getActionItemName().equalsIgnoreCase(currentActionItem.getActionItemName())) {
				ai_all.remove(i);
				break;
			}
		}
		reestablishActionItemAccessList();
		setCurrentActionItem(emptyActionItem);
	}

	/**
	 * Check the parameters to see if the action item can be added to the list of action items.
	 * @param name String
	 * @param dueDateStr String
	 * @return boolean
	 * @throws an exception if there are any problems with the input.
	 */
    private void validateActionItem(ActionItem ai, String name, String dueDateStr, 
			boolean flag, int itemIndex) throws Exception {	// Modified for Della01
    	
    	String errorMsg = "";
    	
		if (name.trim().length() == 0) {
			errorMsg += "-> Please enter the name of ActionItem!\n\n";
		}

		Date dueDate = null;
		if (dueDateStr.length() != 0) {
			try {
				dueDate = dateFormat.parse(dueDateStr);
				if(dueDate.before(new Date()))
				{
					errorMsg += "Due date should not be before creation date!\n\n";
				}
			}
			catch (ParseException ex) {
				errorMsg += "-> Enter valid due date.\n\n"; // if due date is before creation date, set up error message
			}
		}
		ai.setDueDate(dueDate);
		
		// Added for Della01
		ActionItem aiTemp = new ActionItem();
		if(flag){
			// This checks for a create action... no duplications allowed
			try
			{
				// When connection cannot be established
				if(DBManager.checkDuplicate(name))
					errorMsg += "An actionitem with the given name already exists\n\n";
			}
			catch(Exception e)
			{
				for (int i=0;i<ai_all.size();i++){
					aiTemp = (ActionItem)ai_all.get(i);
					if(aiTemp.getActionItemName().equalsIgnoreCase(name))
						errorMsg += "An actionitem with the given name already exists\n\n";
				}
			}
		}
		else {
			// This checks for an update action... the only allowed duplication is for
			// the action item being updated... that is, the name has not changed!
			for (int i=0; i < ai_all.size(); i++){
				if (i != itemIndex)
				{
					aiTemp = (ActionItem)ai_all.get(i);
					if(aiTemp.getActionItemName().equalsIgnoreCase(name))
						throw new Exception("New Action Item name already exists!   ");
				}
			}
		}
		
		if(!errorMsg.equals(""))
			throw new Exception(errorMsg);
	}

	/**
	 * Get the action item index for the items whose name matches the provided parameter
	 * @param aiName	String	- This is the name of the action item to find
	 * @return	the item index of the found action item is returned
	 * 
	 * Added for Della02
	 */
	public int getActionItemIndex(String aiName){
		ActionItem temp = new ActionItem();
		int result = -1;
		for (int i = 0; i < accessManager.size(); i++){
			temp = (ActionItem)ai_all.get(accessManager.getAccessIndex(i));
			if(temp.getActionItemName().equals(aiName)) {
				result = i;
				break;
			}
		}
		return result;
	}

	/**
	 * getSortFactorText (private) is used to generate text for the sorting name to be
	 * generated based on the various action sorting factors
	 * 
	 * @param ai	- the action item whose fields are to be used
	 * @param sortingFactor	- the sorting factor requested
	 * @return a string based on the action item and the specified factor
	 * 
	 * Added for Della02, Modified for Della05
	 */
    private String getSortFactorText(ActionItem ai, int sortingFactor){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String tempFactor = new String("");
    	if (sortingFactor == 1) // The first choice is the creation date
    		if (ai.getCreatedDate() != null)
    			tempFactor = dateFormat.format(ai.getCreatedDate()) + ": ";
    		else
    			tempFactor = ": ";
    	else
    		if (sortingFactor == 2) // The second choice is the due date
    			if (ai.getDueDate() != null)
    				tempFactor = dateFormat.format(ai.getDueDate()) + ": ";
    			else
    				tempFactor = "- no due date -: ";
			else					// Added for Della05
				if (sortingFactor == 3) // The third choice is the assigned member
					if (ai.getAssignedMember().length() != 0)
						tempFactor = ai.getAssignedMember() + " : ";
					else
						tempFactor = "- No Member Assigned - : ";
				else				// Added for Della09
					if (sortingFactor == 4) // The fourth choice is the assigned team
						if (ai.getAssignedTeam().length() != 0)
							tempFactor = ai.getAssignedTeam() + " : ";
						else
							tempFactor = "- No Team Assigned - : ";
   	return tempFactor;
    }

	/**
	 * Remove a specific Assigned Member
	 * 
	 * @param name
	 * 
	 * Added for Della04; Modified for Della05
	 */
    public void removeAssignedMember(String name){
    	for (int ndx=0; ndx<ai_all.size(); ndx++)
    		if (ai_all.get(ndx) != null)
    			if (ai_all.get(ndx).getAssignedMember().equalsIgnoreCase(name))
    				ai_all.get(ndx).setAssignedMember("");
    	memberList.setUnaddedName(name);
    	memberList.findAndRemoveElement(name);
    	reestablishActionItemAccessList();
    }

	/**
	 * Remove a specific Assigned Team
	 * 
	 * @param name
	 * 
	 * Added for Della06 modified for Della09
	 */
	public void removeAssignedTeam(String name){
    	for (int ndx=0; ndx<ai_all.size(); ndx++)
    		if (ai_all.get(ndx) != null)
    			if (ai_all.get(ndx).getAssignedTeam().equalsIgnoreCase(name))
    				ai_all.get(ndx).setAssignedTeam("");
		teamList.setUnaddedName(name);
		teamList.findAndRemoveElement(name);
    	reestablishActionItemAccessList();
	}

	// The usual getters and setters

	/**
	 * Get the current action item 
	 * @return	- The current action item
	 */
	public ActionItem getCurrentActionItem() {
		if (currentActionItem == null) 
			return emptyActionItem;
		return currentActionItem;
	}

	public void setCurrentActionItem(ActionItem x) { currentActionItem = x; }

	public void clearCurrentActionItem() { currentActionItem = emptyActionItem; }

	public void setDateFormatChecker() { dateFormat = new SimpleDateFormat("yyyy-MM-dd"); }

	public void setEditChangesPending(boolean flag){ editChangesPending = flag; }

	public boolean getEditChangesPending(){ return editChangesPending; }
	
    // Added for Della01
    public ArrayList getActionItemList(){ return ai_all; }

	// Added for Della02
	public String getCurrentActionItemName() {
		if (currentActionItem==null) return "";
		return currentActionItem.getActionItemName();
	}

	public int getSortDirection() { return sortDirection; }

	public int getSortFactor1() { return sortFactor1; }

	public int getSortFactor2() { return sortFactor2; }

	public void setSortDirection(int sdir) {
		sortDirection = sdir;
		reestablishActionItemAccessList();
	}

	public void setSortFactor1(int sf1) {
		sortFactor1 = sf1;
		reestablishActionItemAccessList();
	}

	public void setSortFactor2(int sf2) {
		sortFactor2 = sf2;
		reestablishActionItemAccessList();
	}

	// Added for Della10
	public void setInclusionFactor(int inc) {
		inclusionFactor = inc;
		reestablishActionItemAccessList();
	}

	public int getInclusionFactor() { return inclusionFactor; }
    
    /**
     * Get the action item based on the access index
     * @param accessIndex	int	- This is the access index to find the actual index of the Action Item
     * @return	- The found action item is returned
     * 
     * Added for Della01
     */
	public ActionItem getActionItem(int accessIndex){
		if (accessIndex==noItemSelected) return emptyActionItem;
		return (ActionItem)ai_all.get(accessManager.getAccessIndex(accessIndex));
	}

	// Added for Della04
	public String getMember(int x) { return memberList.getName(x); }

	public ElementList getMemberList() { return memberList; }

	public int getMemberListSize() {
		int memberSize = 0;
		for (int i = 0; i <memberList.getListSize(); i++) {
			if (memberList.get(i) == null) break;
			if (memberList.getName(i).compareTo("") == 0) break;
			memberSize++;
		}
		return memberSize;
	}

	// Added for Della05
	public String getCurrentAssignedMember() { return currentActionItem.getAssignedMember(); }

	public int getCurrentAssignedMemberIndex() { return memberList.findElement(currentActionItem.getAssignedMember()); }

	// Added for Della09
	public String getCurrentAssignedTeam() { return currentActionItem.getAssignedTeam(); }

	public int getCurrentAssignedTeamIndex() { return teamList.findElement(currentActionItem.getAssignedTeam()); }

	// Added for Della06
	public String getTeam(int x) { return teamList.getName(x); }

	public ElementList getTeamList() { return teamList; }

	public int getTeamListSize() {
		int teamSize = 0;
		for (int i = 0; i <teamList.getListSize(); i++) {
			if (teamList.get(i) == null) break;
			if (teamList.getName(i).compareTo("") == 0) break;
			teamSize++;
		}
		return teamSize;
	}

	// Added for Della07
	private Element getElement(ElementList el, String name) {
		int ndx = el.findElement(name);
		if (ndx == -1)
			return null;
		else
			return el.get(ndx);
	}

	public int getNumTeamAffiliations(String memberName){
		Element member = getElement(memberList, memberName);
		return member.getNumAssociatedNames();
	}

	public void addTeamAffiliation(String memberName, String teamName){
		Element member = getElement(memberList, memberName);
		member.addAssociatedName(teamName);   	
	}

	public int getNumMemberAssociations(String teamName){
		Element team = getElement(teamList, teamName);
		return team.getNumAssociatedNames();
	}

	public void addMemberAssociation(String teamName, String memberName){
		Element team = getElement(teamList, teamName);
		team.addAssociatedName(memberName);   	
	}

	public String getSelectedUnaffiliatedTeam(String memberName) { return getElement(memberList, memberName).getSelectedUnassociatedName(); }

	public void setSelectedUnaffiliatedTeam(String memberName, String teamName) { getElement(memberList, memberName).setSelectedUnassociatedName(teamName); }

	public String getSelectedUnassociatedMember(String teamName) { return getElement(teamList, teamName).getSelectedUnassociatedName(); }

	public void setSelectedUnassociatedMember(String teamName, String memberName) { getElement(teamList, teamName).setSelectedUnassociatedName(memberName); }

	public String[] getAffiliatedTeams(String memberName) { return getElement(memberList, memberName).getAssociatedNames(); }

	public String[] getAssociatedMembers(String teamName) { return getElement(teamList, teamName).getAssociatedNames(); }

	public String getSelectedAffiliatedTeam(String memberName) { return getElement(memberList, memberName).getSelectedAssociatedName(); }

	public void setSelectedAffiliatedTeam(String memberName, String teamName) { getElement(memberList, memberName).setSelectedAssociatedName(teamName); }

	public String getSelectedAssociatedMember(String teamName) { return getElement(teamList, teamName).getSelectedAssociatedName(); }

	public void setSelectedAssociatedMember(String teamName, String memberName) { getElement(teamList, teamName).setSelectedAssociatedName(memberName); }

	public void removeTeamAffiliation(String memberName, String teamName) { getElement(memberList, memberName).removeAssociatedName(teamName); }

	public void removeMemberAssociation(String teamName, String memberName) { getElement(teamList, teamName).removeAssociatedName(memberName); }

	public String[] getUnaffiliatedTeams(String memberName) {
		// Determine the actual number of affiliated team names in the array
		String[] affiliatedTeam = getAffiliatedTeams(memberName);
		int numaffiliatedTeams = affiliatedTeam.length;
		while ((numaffiliatedTeams > 0) && (affiliatedTeam[numaffiliatedTeams-1] == null)) numaffiliatedTeams--;
		// The list of unaffiliated teams could be the same size as the number of teams in Della
		int numTeams = teamList.getListSize();
		String[] tempNames = new String[numTeams];
		// Work through the list of teams and only add the name to the list if it is not in the affiliated list
		int ndx = -1;
		for (int i=0; i<numTeams; i++) {
			int j=0;
			while ((j<numaffiliatedTeams) && (affiliatedTeam[j].compareToIgnoreCase(teamList.getName(i))!=0)) j++;
			if (j == numaffiliatedTeams) tempNames[++ndx] = teamList.getName(i);
		}
		// Now that the actual list of unaffiliated teams has been established, we can determine its size
		// so create a properly sized array and move the names into it to be returned to the caller
		ndx++;
		String[] returnNames = new String[ndx];
		for (int i=0; i<ndx; i++)
			returnNames[i] = tempNames[i];
		return returnNames;    	
	}
	
	// Added for Della08

	public String[] getUnassociatedMembers(String teamName) {
		// Determine the actual number of associated member names in the array
		String[] associatedMember = getAssociatedMembers(teamName);
		int numAssociatedMembers = associatedMember.length;
		while ((numAssociatedMembers > 0) && (associatedMember[numAssociatedMembers-1] == null)) numAssociatedMembers--;
		// The list of unassociated members could be the same size as the number of members in Della
		int numMembers = memberList.getListSize();
		String[] tempNames = new String[numMembers];
		// Work through the list of members and only add the name to the list if it is not in the associated list
		int ndx = -1;
		for (int i=0; i<numMembers; i++) {
			int j=0;
			while ((j<numAssociatedMembers) && (associatedMember[j].compareToIgnoreCase(memberList.getName(i))!=0)) j++;
			if (j == numAssociatedMembers) tempNames[++ndx] = memberList.getName(i);
		}
		// Now that the actual list of unassociated teams has been established, we can determine its size
		// so create a properly sized array and move the names into it to be returned to the caller
		ndx++;
		String[] returnNames = new String[ndx];
		for (int i=0; i<ndx; i++)
			returnNames[i] = tempNames[i];
		return returnNames;    	
	}

}
