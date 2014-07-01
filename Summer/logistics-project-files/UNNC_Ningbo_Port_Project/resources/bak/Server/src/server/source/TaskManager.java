package server.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import server.model.XMLSession;

/**
 * This class includes Task Manage functions
 * @author Haoyue Zhu
 *
 */

public class TaskManager {
	//this main method is just for testing!
	public static void main(String[] args) throws Exception {
		changeBoxAmount("1",1,2);
	}
	
	/*When driver finishes his task, updating the box amount in database*/
	public static void changeBoxAmount(String declareID, int big, int small) throws Exception{
		Class.forName("org.sqlite.JDBC");   
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:User.db");
	    Statement stat = conn.createStatement(); 
	    stat.executeUpdate("UPDATE Box SET bigBox = bigBox - " + big +  
	    		", smallBox = smallBox - " + small + " WHERE declareID = " + declareID + ";");  
	    
	}
}
