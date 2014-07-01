package server.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import planner.Route;
import planner.Task;
import server.model.XMLLogin;
import server.model.XMLLoginComm;
import server.model.XMLSession;
import server.model.XMLTask.Action;

public class VehicleManager {
	
	private Vector<Vector<Route>> routes;
	
	/** This Method is used to validate Login */
	public static synchronized XMLLoginComm validateLogin(XMLLoginComm loginComm) throws Exception {
		XMLSession session = null;
		XMLLogin login = loginComm.getLogin();
		String driverName = login.getUsername();
		String password = login.getPassword();
		String vehicleID = login.getVehicleID();
		
		if  (checkPassword(driverName, password, vehicleID)){
	    	session = new XMLSession(driverName, vehicleID);
	    	insertSession(session);
	    	loginComm.setSession(session);
	    }
		
		return loginComm;
	}
	
	public static boolean checkPassword(String driverName, String password, String vehicleID) throws Exception {
		Class.forName("org.sqlite.JDBC");   
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:User.db");
	    Statement stat = conn.createStatement(); 
	    ResultSet rs = stat.executeQuery("SELECT * FROM Driver WHERE driverName = '" + driverName + "'AND password = '" + password + "'"); 
	    
	    
	    if (rs.next()){
	    	rs.close();
	    	ResultSet rss = stat.executeQuery("SELECT * FROM Vehicle WHERE vehicleID = '" + vehicleID + "'");
	    	if (rss.next()) {
	    		rss.close();
	    		conn.close();
	    		return true;
	    	}
	    	else {
	    		rss.close();
	    		conn.close();
	    		return false;
	    	}
	    }
	    
	    else {
	    	rs.close();
	    	conn.close();
	    	return false;
	    }
	}
	
	public static void insertSession(XMLSession session) throws Exception {
				
        Connection conn = DriverManager.getConnection("jdbc:sqlite:User.db");   
        conn.setAutoCommit(false);   
           
        Statement stat = conn.createStatement();   		
		stat.executeUpdate("insert into Session values ('" +  session.getDriverName() + "','" + session.getVehicleID() +"','" 
				+ session.getStartTime() + "','" + session.getExpireTime() +"');");
		conn.commit();   
		conn.close();
	}
	
	public static void addTask(Task t, Route r, int sequenceNo, ArrayList<UpdateManager> m) {

		XMLSession session = r.getSession();
		int l = m.size();
		boolean exist = false;
		for(int i = 0; i < l; i ++){
			if(m.get(i).getSession().equals(session)){
				m.get(i).addSingleTask(new SingleUpdateManager(Action.add, t, sequenceNo));
				exist = true;
				break;
			}
		}
		if (exist == false) {
			m.add(new UpdateManager(session));
			int index = m.size() - 1;
			m.get(index).addSingleTask(new SingleUpdateManager(Action.add, t, sequenceNo));
		}
	}
	

	public static void removeTask(Route r, int sequenceNo, ArrayList<UpdateManager> m) {
		XMLSession session = r.getSession();
		int l = m.size();
		boolean exist = false;
		for(int i = 0; i < l; i ++){
			if(m.get(i).getSession().equals(session)){
				m.get(i).addSingleTask(new SingleUpdateManager(Action.delete, sequenceNo));
				exist = true;
				break;
			}
		}
		if (exist == false) {
			m.add(new UpdateManager(session));
			int index = m.size() - 1;
			m.get(index).addSingleTask(new SingleUpdateManager(Action.delete, sequenceNo));
		}		
	}
	
	public static void moveTask(Route r1, int index1, Route r2, int index2, ArrayList<UpdateManager> m) {
		Task temp1 = r1.taskSet.get(index1);
		Task temp2 = r2.taskSet.get(index2);
		
		
		XMLSession session1 = r1.getSession();
		XMLSession session2 = r2.getSession();
		int l1 = m.size();
		boolean exist = false;
		for(int i = 0; i < l1; i ++){
			if(m.get(i).getSession().equals(session1)){
				m.get(i).addSingleTask(new SingleUpdateManager(Action.delete, index1));
				m.get(i).addSingleTask(new SingleUpdateManager(Action.add, temp2, index1));
				exist = true;
				break;
			}
		}
		
		if (exist == false) {
			m.add(new UpdateManager(session1));
			int index = m.size() - 1;
			m.get(index).addSingleTask(new SingleUpdateManager(Action.delete, index1));
			m.get(index).addSingleTask(new SingleUpdateManager(Action.add, temp2, index1));
		}		
		
		int l2 = m.size();
		exist = false;
		for(int i = 0; i < l2; i ++){
			if(m.get(i).getSession().equals(session2)){
				m.get(i).addSingleTask(new SingleUpdateManager(Action.delete, index2));
				m.get(i).addSingleTask(new SingleUpdateManager(Action.add, temp1, index2));
				exist = true;
				break;
			}
		}
		
		if (exist == false) {
			m.add(new UpdateManager(session2));
			int index = m.size() - 1;
			m.get(index).addSingleTask(new SingleUpdateManager(Action.delete, index2));
			m.get(index).addSingleTask(new SingleUpdateManager(Action.add, temp1, index2));
		}			

	}
	
	
	
	
}
