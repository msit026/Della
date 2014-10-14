package persistence;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import control.Controller;
import users.User;
import model.ActionItem;
import application.controller;
import application.db_connect;


/**
 * <p>
 * Title: DBManager
 * </p>
 *
 * <p>
 * Description: Read and write data to the Database, residing on the server
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2005
 * </p>
 *
 * @author Harry Sameshima
 * @version 1
 */
public class DBManager {
	static Connection conn = null;
	static Statement stmt = null;
	
	public DBManager() {
		
	}
	
	public static boolean login(String name, String pwd)
	{
		try
		{
			System.out.println("Login method called");
			
			Connection conn = db_connect.connect();
			PreparedStatement ps = conn.prepareStatement
				      ("select username,password from login where username=? and password=?");
	
			ps.setString(1, name);
			ps.setString(2, pwd);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				controller.user = new User(rs.getString("username"), rs.getString("password"));
				System.out.println("Username set: " + controller.user.getUname());
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * Read data related to the user and save it into the data structure
	 * This method is called as soon as the application is opened by the user
	 * 
	 * @return true if the application is connected to the server and data is properly retrieved 
	 */
	public static boolean readData() {
		
		ActionItem item;
		java.sql.Date created = null;
		java.sql.Date due = null;
		String name = null;
		try {
			conn = db_connect.connect();
			stmt = conn.createStatement(); 

			ResultSet rs = stmt.executeQuery("select * from actionitem where assignedMember = '" + controller.user.getUname() + "' or assignedTeam in (select team from associatedmt where member = '" + controller.user.getUname() + "');");
			
			while(rs.next())
			{
//				item.setActionItemName(rs.getString("actionItemName"));
//				item.setDescription(rs.getString("description"));
//				item.setResolution(rs.getString("resolution"));
//				item.setStatus(rs.getString("status"));
//				item.setCreatedDate(new Date());
				if((name = rs.getString("actionItemName")) != null)
				{
					item = new ActionItem(name, rs.getString("description"), rs.getString("resolution"), rs.getString("status"), rs.getString("assignedMember"), rs.getString("assignedTeam"));
					if((created = rs.getDate("creationDate")) != null)
						item.setCreatedDate(new Date(created.getTime()));
					if((due = rs.getDate("dueDate")) != null)
						item.setDueDate(new Date(due.getTime()));
					Controller.getInstance().getActionItemManager().ai_all.add(item);
				}
			}
			System.out.println("Out of read data");
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
	}

	/**
	 * Write out the action item to the database on server
	 * @param item Action Item to write to the DB
	 */
	public static boolean writeData(ActionItem item) {
		
		try {
			conn = db_connect.connect();
			stmt = conn.createStatement();
			String query="";
			
			System.out.println("--->> assigned Member: " + item.getAssignedMember() + " assigned Team: " + item.getAssignedTeam());
			
			ResultSet rs = stmt.executeQuery("select * from actionitem where actionItemName = '" + item.getActionItemName() + "';");
			
			if(rs.next())
			{
				stmt.executeUpdate("update actionitem set description = '" + item.getDescription() + "', resolution = '" + item.getResolution() +
						"', status = '" + item.getStatus() + "', creationDate='" + new java.sql.Date(item.getCreatedDate().getTime()).toString() +
						"', dueDate = '" + new java.sql.Date(item.getDueDate().getTime()).toString() + 
						"', assignedMember = '" + item.getAssignedMember() + "', assignedTeam = '" + item.getAssignedTeam() +
						"' where actionItemName = '"+ item.getActionItemName() +"';");
			}
			else
			{
				query = "insert into actionitem values('"+ item.getActionItemName() +"',";
				if(item.getDescription() == null)
					query += null + ", ";
				else
					query += "'" + item.getDescription() + "',";
				
				if(item.getResolution() == null)
					query += null + ", ";
				else
					query += "'" + item.getResolution() +	"', '" + item.getStatus() + "', '" + new java.sql.Date(item.getCreatedDate().getTime()).toString() + "', ";
				
				if(item.getAssignedMember() == null)
					query += null + ", ";
				else
					query += "'" + item.getAssignedMember() + "', ";
				
				if(item.getAssignedTeam() == null)
					query += null + ", ";
				else
					query += "'" + item.getAssignedTeam() + "', ";
				
				if(item.getDueDate() == null)
					query += null + ", ";
				else
					query += "'" + new java.sql.Date(item.getDueDate().getTime()).toString() + "');";
				//System.out.println("insert query: " + query);
				stmt.executeUpdate(query);
			}
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean checkDuplicate(String name) throws Exception{
		conn = db_connect.connect();
		stmt = conn.createStatement(); 
		
		ResultSet rs = stmt.executeQuery("select * from actionitem where actionItemName = '" + name + "';");
		
		if(rs.next())
		{
			return true;
		}
		return false;
	}

	public static ArrayList<String> getAllMembers() {
		
		ArrayList<String> members = new ArrayList<String>();
		try {
			conn = db_connect.connect();
			stmt = conn.createStatement(); 
			
			ResultSet rs = stmt.executeQuery("select * from members;");
			
			while(rs.next())
			{
				members.add(rs.getString("name"));
			}
			return members;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static HashMap<String, ArrayList<String>> getAllTeams() {
		
		HashMap<String, ArrayList<String>> teams = new HashMap<String, ArrayList<String>>();
		ArrayList<String> members;
		try {
			conn = db_connect.connect();
			stmt = conn.createStatement(); 
			Statement stmt1 = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from teams;");
			
			while(rs.next())
			{
				members = new ArrayList<String>();
				ResultSet rs1 = stmt1.executeQuery("select member from associatedmt where team = '" + rs.getString(1) + "';");
				while(rs1.next())
				{
					members.add(rs1.getString(1));
				}
				rs1.close();
				teams.put(rs.getString(1), members);
			}
			
			rs.close();
			return teams;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static List<String> getActionItemNames() {
		List<String> aiList = new ArrayList<String>();
		try {
			conn = db_connect.connect();
			stmt = conn.createStatement(); 
			String query = "select actionItemName from actionitem where assignedMember = '" + controller.user.getUname() + "' or assignedTeam in (select team from associatedmt where member = '" + controller.user.getUname() + "');";
			System.out.println("In DBManager : " + query);
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				aiList.add(rs.getString("actionItemName"));
			}
			return aiList;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
