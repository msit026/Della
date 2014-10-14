package persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import control.Controller;
import model.ActionItem;

/**
 * <p>
 * Title: DataManager
 * </p>
 *
 * <p>
 * Description: Read and write data to the persistent store, an xml file
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2005
 * </p>
 *
 * @author Harry Sameshima
 * @version 1
 */
public class DataManager {
	private static final String storageFile = "Della.data";

	public DataManager() {
		
	}

	/**
	 * Read our persistent store into an object
	 * @return Object
	 */
	public static boolean readData() {
		ActionItem item = new ActionItem();
		// Does the persistent store exist?
		File file = new File(storageFile);
		if (!file.exists()) { return false; }

		// Yes, so let's deserialize the object
		try {
			FileInputStream fileIn = new FileInputStream(file);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        while((item = (ActionItem)in.readObject()) != null)
	        {
	        	Controller.getInstance().getActionItemManager().ai_all.add(item);
	        }
	        in.close();
	        fileIn.close();
	        return true;
		}
		catch (IOException ex) {
			System.out.println("IO exception reading " + storageFile);
			System.exit(0);
		}
		catch (ClassNotFoundException ex) {
			System.out.println("Class not found exception while reading " + storageFile);
			System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Write out an object to the persistent store so we can save data
	 * @param o Object
	 */
	public static boolean writeData(ActionItem item) {

		try {
			FileOutputStream fileOut = new FileOutputStream(storageFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(item);
	        out.close();
	        fileOut.close();
	        return true;
		}
		catch (IOException ex) {
			System.out.println("IO Exception writing " + storageFile);
			return false;
			//System.exit(0);
		}
	}

	/**
	 * If the application cannot access the server, check the login credentials from the
	 * login file
	 * @param uname username of the user
	 * @param pwd password of the user
	 * @return
	 */
	public static boolean login(String uname, String pwd) {
		
		
		return true;
	}

	public static ArrayList<String> getAllMembers() {
		
		String member;
		ArrayList<String> list = new ArrayList<String>();
		File file = new File("members.data");
		if (!file.exists()) { return null; }
		try {
			
	        BufferedReader in = new BufferedReader(new FileReader(file));
	        while((member = in.readLine()) != null)
	        {
	        	list.add(member);
	        }
	        in.close();
	        return list;
		}
		catch (IOException ex) {
			System.out.println("IO exception reading members.data");
			System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

//	public static HashMap<String, ArrayList<String>> getAllTeams() {
//		
//		File file = new File("teams.data");
//		if (!file.exists()) { return null; }
//
//		// Yes, so let's deserialize the object
//		try {
//			FileInputStream fileIn = new FileInputStream(file);
//	        ObjectInputStream in = new ObjectInputStream(fileIn);
//	        while((team = (ActionItem)in.readObject()) != null)
//	        {
//	        	Controller.getInstance().getActionItemManager().ai_all.add(item);
//	        }
//	        in.close();
//	        fileIn.close();
//	        return true;
//		}
//		catch (IOException ex) {
//			System.out.println("IO exception reading " + storageFile);
//			System.exit(0);
//		}
//		catch (ClassNotFoundException ex) {
//			System.out.println("Class not found exception while reading " + storageFile);
//			System.exit(0);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return false;
//	}
}
