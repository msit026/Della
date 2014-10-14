package model;

/**
 * <p>
 * Title: AccessItem
 * </p>
 *
 * <p>
 * Description: An entity to hold the access information for action items
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2007
 * </p>
 *
 * @author Lynn Robert Carter
 * @version 1.01
 */

public class AccessItem {

	//---------------------------------------------------------------------------------------------------------------------
	// Attributes

	private String name;
	private int nameIndex;
	//---------------------------------------------------------------------------------------------------------------------

	/**
	 * The AccessItem class constructors.
	 *
	 */
	public AccessItem() {
		name =  "";
		nameIndex = -1;
	}

	public AccessItem(String n, int i) {
		name =  n;
		nameIndex = i;
	}

	// The usual getters and setters
	public String getName() { return name; }

	public int getNameIndex() { return nameIndex; }

	public void setName(String x) { name = x; }

	public void setNameIndex(int x) { nameIndex = x; }

}
