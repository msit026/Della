package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.*;
import persistence.*;

/**
 * <p>
 * Title: Controller
 * </p>
 *
 * <p>
 * Description: A singleton class to control instances of the assorted managers
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2005
 * </p>
 *
 * @author Harry Sameshima
 * @version 1
 */
public class Controller {
	//---------------------------------------------------------------------------------------------------------------------
	// Controller Attributes

	private static Controller theController = null;
	private ArrayList<String> members = null;
	private HashMap<String,ArrayList<String>> teams = null;
	private ActionItemManager actionItemManager = null;
	private static boolean dirtyFlag = false; // Has Della's state changed?
	//---------------------------------------------------------------------------------------------------------------------

	/*
      This constructor is private and synchronized because we are using the singleton design pattern and we want only one!
	 */
	private Controller() {
	}

	/**
	 * Return the singleton instance of the Controller class
	 * @return Controller
	 */
	public static synchronized Controller getInstance() {
		System.out.println("In Controller!");
//		// If theController is not initialized, we start by trying to read the intial state from the xml file
//		if (theController == null) {
//			theController = (Controller) DataManager.readData();
			
			// If theController is still null, we were not able to initialize from the xml file
			if (theController == null) {
				theController = new Controller();	// So we must initialize by calling the constructor and
				theController.setActionItemManager(new ActionItemManager());	// create an Action Item
			}
			theController.actionItemManager.setDateFormatChecker();
			
			theController.members = DBManager.getAllMembers();
			if(theController.members == null)
			{
				System.out.println("Members could not be retrieved from DB");
				theController.members = DataManager.getAllMembers();
			}
			if(theController.members == null)
				theController.members = new ArrayList<String>();
			
			theController.teams = DBManager.getAllTeams();
			if(theController.teams == null)
				theController.teams = new HashMap<String, ArrayList<String>>();
//		}
		System.out.println("Out of controller!");
		return theController;
	}

	/**
	 * Save the current controller and all of the data objects rooted here to the persistent store
	 */
	public void save() {
		dirtyFlag = false;
		//DataManager.writeData(this);
	}
	// The usual collection of getters and setters
	public ActionItemManager getActionItemManager() { return actionItemManager; }

	public boolean getDirtyFlag() { return dirtyFlag; }

	public void setActionItemManager(ActionItemManager x) { actionItemManager = x; }

	public void setDirtyFlag(boolean x) { dirtyFlag = x; }

	public List<String> getMemberList() {
		
		return members;
	}
	
	public List<String> getTeams()
	{
		ArrayList<String> teamNames = new ArrayList<String>(teams.keySet());
		return teamNames;
	}
}
