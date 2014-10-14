package model;

import java.util.ArrayList;

/**
 * <p>
 * Title: AccessItemManager
 * </p>
 *
 * <p>
 * Description: A class to organize and manage the access to action items
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2007
 * </p>
 *
 * @author Lynn Robert Carter
 * @version 1.01
 */
public class AccessItemManager {

	//---------------------------------------------------------------------------------------------------------------------
	// Attributes

	private ArrayList<AccessItem> accessList = null;
	//---------------------------------------------------------------------------------------------------------------------


	/**
	 * These is the constructor method for the AccessItem class.
	 *
	 */
	public AccessItemManager() {
		accessList = new ArrayList<AccessItem>();
	}

	/**
	 * createAccessItem is used to establish an access item and add it to the list of access items
	 * @param name - A name string (formatted by the ActionItemManager when needed)
	 * @param access - An index to get back to the original action item for this name
	 * @return the created action item
	 */
	public AccessItem createAccessItem(String name, int access) {
		AccessItem ai = new AccessItem(name, access);
		accessList.add(ai);
		return ai;
	}

	/**
	 * Create the current state of the access list based on the list of names and a corresponding
	 * list of accessing indexes.  The sort direction indicates if the sorting should be done low
	 * to high or high to low
	 * 
	 * @param names	- This is an array of processed action item names
	 * @param accesses - This is an array of accessing indexes one for each processed action item name
	 * @param sortDirection - 1 means Large to Small, otherwise, Small to Large
	 */
	public void establishSortedAccessList(String[] names, int[] accesses, int listSize, int sortDirection){
		// Start with an empty list
		accessList.clear();

		// Add in each of the items into the list
		for (int i = 0; i < listSize; i++)
			createAccessItem(names[i], accesses[i]);

		// A bubble sort based with a direction flag that determines whether this is a sort up or down
		int pass = 1;
		boolean exchanges = false;
		do {
			exchanges = false;
			for (int i = 0; i < listSize - pass; i++){
				if (sortDirection == 1){
					if (accessList.get(i).getName().compareToIgnoreCase(accessList.get(i+1).getName()) < 0) {
						AccessItem temp = accessList.get(i);
						accessList.set(i, accessList.get(i+1));
						accessList.set(i+1, temp);
						exchanges = true;
					}
				}
				else {
					if (accessList.get(i).getName().compareToIgnoreCase(accessList.get(i+1).getName()) > 0) {
						AccessItem temp = accessList.get(i);
						accessList.set(i, accessList.get(i+1));
						accessList.set(i+1, temp);
						exchanges = true;
					}
				}
			}
			pass++;
		} while (exchanges);
	}

//	getters that access the name and index of the ith element of the access list    
	public String getAccessName(int aiIndex){
		return accessList.get(aiIndex).getName();
	}

	public int getAccessIndex(int aiIndex){
		if (aiIndex==-1) return -1;
		return accessList.get(aiIndex).getNameIndex();
	}

	public int size(){
		return accessList.size();
	}

}
