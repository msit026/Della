package model;

import java.util.ArrayList;

/**
* <p>
 * Title: ElementManager
 * </p>
 *
 * <p>
 * Description: A class to organize and manage the list of members/teams
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2007
 * </p>
 *
 * @author Lynn Robert Carter
 * @version 1.07
 */
public class ElementList {
	
	private ArrayList<Element> theList = null;
	private String unaddedName = "";
	private int currentSelectedElementIndex = -1;

	

    /**
     * These is the constructor method for the ElementManager class.
     *
     */
    public ElementList() { theList = new ArrayList<Element>(); }
    
    /**
     * addElement is used to establish a sorted list of Elements (a name and a list of associated named, 
     *      returning the index where it was placed in the list.  The routine throws an exception if the 
     *      name duplicates a name already found in the list (ignoring case).
     * @param name - A name string 
     * @return index - the index where the name was inserted into the list (used for managing select lists)
     * @exception - This name duplicates a name already found in the list.
     */
    public int addElement(String name) throws Exception {

		if (name.compareTo("") == 0)
			throw new Exception("The new name must not be empty!   ");
		int size = theList.size();
		if (size > 999)
			throw new Exception("Della limits you to no more than 1000 names!   ");
		if (size > 0) {
			// See if the name is in the list and if not, where it belongs in sorted order
			int small = size-1;
			while (small>=0) {
				int result = theList.get(small).getName().compareToIgnoreCase(name);
				if (result < 0)
					break;
				else if (result == 0)
					throw new Exception("New name duplicates an existing name!   ");
				small--;
			}

			// The fact it gets here means it is not in the list and small is just before where it belongs
			// Make room for the new item by sliding element to the right one slot
			theList.add(theList.get(size-1));
			for (int i = size-1; i-1>small; i--)
				theList.set(i, theList.get(i-1));

			// Create a new member and insert it where it belong into the list
			Element mem = new Element(name);
			theList.set(small+1, mem);
			return small+1;
		}
		else {
			// Getting here means that the list was empty, so we just add it to the list
			Element mem = new Element(name);
			theList.add(mem);
			return 0;
		}
	}
    
	/**
	 * Removes an element from the list.
	 * @param elemIndex - The index of the elemnent to be removed
	 */
    public void removeElement(int elemIndex) { theList.remove(elemIndex); }

	/**
	 * Find an element in the list.
	 * @param name - The string of the name to be found
	 * @return - the index where it was found in the list
	 * 
	 * Added for Della04
	 */
	public int findElement(String name) {
		if (name.length()==0) return -1;
		int size = theList.size();
		int ndx = 0;
		while (ndx < size)
			if (theList.get(ndx).getName().equalsIgnoreCase(name))
				break;
			else
				ndx++;
		if (ndx == size) ndx--;
		return ndx;
	}

	/**
	 * Find an element in the list and remove it
	 * @param name - The string of the name to be found and removed
	 * 
	 * Added for Della04
	 */
	public void findAndRemoveElement(String name){ removeElement(findElement(name)); }

	/**
	 * When an associated name is removed, it must be removed from all the elements in the list
	 * 
	 * Added for Della07
	 */
	public void removeAssociatedName(String name) {
		int listSize = theList.size();
		for (int ndx=0; ndx < listSize; ndx++) {
			theList.get(ndx).removeAssociatedName(name);
		}
	}

	// getters that access the name and index of the ith element of the access list    
	/**
	 * Get the name of the ith element in the list
	 */
	public String getName(int elemIndex){
		if (elemIndex >= theList.size()) return "";
		return theList.get(elemIndex).getName();
	}

	/**
	 * Get the number of elements in the list
	 */
	public int getListSize(){ return theList.size(); }

	/**
	 * Set the current edit value for the list of elements so it can be restored
	 */
	public void setUnaddedName(String x){ unaddedName = x; }

	/**
	 * Get the current edit value for the list of elements when the display restored for editing
	 */
	public String getUnaddedName() { return unaddedName; }

	/**
	 * Remember the index of the currently selected element
	 */
	public void setCurrentSelectedElementIndex(int x){ currentSelectedElementIndex = x; }

	/**
	 * Provide the index of the currently selected element
	 */
	public int getCurrentSelectedElementIndex() { return currentSelectedElementIndex; }
    
	/**
	 * Get the ith element from the list
	 */
	public Element get(int elemIndex){ return theList.get(elemIndex); }

}
