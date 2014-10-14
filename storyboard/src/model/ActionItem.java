package model;

import java.util.Date;

/**
 * <p>
 * Title: ActionItem
 * </p>
 *
 * <p>
 * Description: An entity to hold details about a particular action item
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2005, 2006
 * </p>
 *
 * @author Harry Sameshima; Lynn Robert Carter
 * @version 1.09
 */
public class ActionItem implements java.io.Serializable{

	//---------------------------------------------------------------------------------------------------------------------
	// Attributes

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String actionItemName;
	private String description;
	private String resolution;
	private String status;
	private Date dueDate;
	private Date createdDate;
	private String assignedMember;	// Added for Della05
	private String assignedTeam;	// Added for Della06
	//---------------------------------------------------------------------------------------------------------------------

	/**
	 * The ActionItem class constructors.
	 *
	 */
	public ActionItem() {
		actionItemName = description = resolution = status = assignedMember = assignedTeam = ""; // Updated for Della05 and Della09
		dueDate = createdDate = null;
	}

	public ActionItem(String ai, String desc, String res, String stat, String memb, String team) { // Updated for Della05 and Della09

		actionItemName = ai;
		description = desc;
		resolution = res;
		status = stat;
		createdDate = new Date();
		assignedMember = memb;	// Added for Della05
		assignedTeam = team;	// Added for Della09
	}

	// Just the usual getters and setters
	public String getActionItemName() { return actionItemName; }

	public String getDescription() { return description; }

	public String getResolution() { return resolution; }

	public String getStatus() { return status; }

	public Date getDueDate() { return dueDate; }

	public Date getCreatedDate() { return createdDate; }

	public void setActionItemName(String x) { actionItemName = x; }

	public void setDescription(String x) { description = x; }

	public void setResolution(String x) { resolution = x; }

	public void setStatus(String x) { status = x; }

	public void setDueDate(Date x) { dueDate = x; }

	public void setCreatedDate(Date x) { createdDate = x; }
	
	public String getAssignedMember() { return assignedMember; }	// Added for Della05

	public void setAssignedMember(String x) { assignedMember = x; } 	// Added for Della05  
	
	public String getAssignedTeam() { return assignedTeam; }	// Added for Della06

	public void setAssignedTeam(String x) { assignedTeam = x; } 	// Added for Della06 
	
	// override equals method so that when two action items are compared, 
	// this method is called instead of equals method of Object class
	@Override
	public boolean equals(Object item)
	{
		if(this.actionItemName.equalsIgnoreCase(((ActionItem)item).actionItemName))
			return true;
		return false;
	}
	
}
