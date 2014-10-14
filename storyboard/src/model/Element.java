package model;

/**
 * <p>Title: Element</p>
 *
 * <p>Description: An entity to hold a name and a list of associated names</p>
 *
 * <p>Copyright: Copyright © 2007</p>
 *
 * @author Lynn Robert Carter
 * @version 1.07
 */
public class Element {
	private String name;
	private int numAssociatedNames;
	private String[] associatedName;
	private String selectedAssociatedName;
	private String selectedUnassociatedName;

	/**
	 * These are the constructors method for the AccessItem class.
	 *
	 */
	public Element() {
		System.out.println("Element Constructor called!!");
		name =  "";
		numAssociatedNames = 0;
		associatedName = new String[10];
		for (int i=0; i<10; i++) associatedName[i] = null;
	}

	public Element(String n) {
		name =  n;
		numAssociatedNames = 0;
		associatedName = new String[10];
		for (int i=0; i<10; i++) associatedName[i] = null;
	}

	/**
	 * Add a new associated name to this element; insert it where is belongs alphabetically
	 *
	 * Added for Della07
	 */
	public void addAssociatedName(String newName) {
		// Insert the new associated name into the list in sorted order
		if (numAssociatedNames < 10) {
			// increase the counts and start from the back of the list and shift names until the right hole is established
			int ndx = numAssociatedNames++;
			while (ndx > 0 && newName.compareToIgnoreCase(associatedName[ndx-1])<0) {
				associatedName[ndx] = associatedName[ndx-1];
				ndx--;
			}
			// add the new name into the generated hole
			associatedName[ndx] = newName;
		}
		// just ignore a name if there are 10 names already in the list
	}

	/**
	 * Remove an associated name from this element
	 *
	 * Added for Della07
	 */
	public void removeAssociatedName(String newName) {
		// Remove a name from the associated list by finding in and shift nodes left to fill the hole
		if (numAssociatedNames > 0 ) {
			// Search for the name
			int ndx = 0;
			while (ndx < numAssociatedNames && newName.compareToIgnoreCase(associatedName[ndx])!=0) {
				ndx++;
			}
			// Shift the names to fill up the hole
			while (ndx < numAssociatedNames - 1) {
				associatedName[ndx] = associatedName[ndx+1];
				ndx++;
			}
			// Null out the last name and update the count
			if (ndx < 10)
				associatedName[ndx] = null;
			numAssociatedNames--;
		}
	}

	/**
	 * Fetch the element's associated names and return an array that is sized just right
	 *
	 * Added for Della07
	 */
	public String[] getAssociatedNames() {
		// Find the end of the list and the number of names
		int numNames = 0;
		while (numNames < 10) {
			if (associatedName[numNames]==null) break;
			if (associatedName[numNames].compareTo("") == 0) break;
			numNames++;
		}

		// Create an appropriately sized array, move the names into it, and then return it
		String[] returnName = new String[numNames];
		for (int i=0; i<numNames; i++) returnName[i] = associatedName[i];
		return returnName; 
	}

	/**
	 * Define the element's associated names (no more than 10) and set the size so it is correct
	 *
	 * Added for Della07
	 */
	public void setAssociatedNames(String[] x) {
		// Bring in the set of associated names, up to a max of 10
		int listLength = x.length;
		if (listLength > 10) listLength = 10;
		for (int i=0; i<listLength; i++) associatedName[i] = x[i];
		// Determine how many of them are actully there
		numAssociatedNames = 0;
		while (numAssociatedNames < 10 && associatedName[numAssociatedNames] != null) numAssociatedNames++;
	}

	// Just the usual getters and setters
	public String getName() { return name; }

	public void setName(String x) { name = x; }
	
	public int getNumAssociatedNames() { return numAssociatedNames; }

	public String getSelectedAssociatedName() { return selectedAssociatedName; }

	public void setSelectedAssociatedName(String nameParam) { selectedAssociatedName = nameParam; }

	public String getSelectedUnassociatedName() { return selectedUnassociatedName; }

	public void setSelectedUnassociatedName(String nameParam) { selectedUnassociatedName = nameParam; }
}
