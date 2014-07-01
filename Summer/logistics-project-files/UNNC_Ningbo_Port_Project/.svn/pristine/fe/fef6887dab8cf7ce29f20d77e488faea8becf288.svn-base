package server.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import server.dbs.DBManager;
import server.model.XMLLogin;
import server.model.XMLLoginComm;
import server.model.XMLSession;

/**
 * This is the verification function that checking user log in and return session
 * @author Haoyue Zhu
 *
 */

public class Verification {
	
	public static void main(String[] args) throws Exception {
		XMLLogin log = new XMLLogin("Alex" , "politics", "9A773");
		XMLLoginComm ori = new XMLLoginComm( null, log);
		XMLLoginComm comm = validateLogin(ori);
		XMLSession session = comm.getSession();
		if (session == null) {
			System.out.print("not exist");
		}
		else {
			System.out.println("return Session success!!!");
			System.out.println("Name: " + session.getDriverName());
			System.out.println("VehicleID: " + session.getVehicleID());
			System.out.println("StartTime: " + session.getStartTime());
			System.out.println("ExpireTime: " + session.getExpireTime());
		}
	}
	
	public static XMLLoginComm validateLogin(  XMLLoginComm comm ) throws Exception {
	    XMLLogin login = comm.getLogin();
	    
	    String driverName = login.getUsername();
	    String password = login.getPassword();
	    String vehicleID = login.getVehicleID();
		
		XMLSession session = null;
	    if (checkPassword(driverName, password, vehicleID)){
	    	session = new XMLSession(driverName, vehicleID);
	    	insertSession(session);
	    }
	    
	    comm.setSession(session);
	    
		return comm; 
	}
	
	public static boolean checkPassword(String driverName, String password, String vehicleID) throws Exception {
		
		DBManager dbm = new DBManager();
		ResultSet rs = dbm.getRs("SELECT * FROM Driver WHERE driverName = '" + driverName + "'AND password = '" + password + "'");

	    if (rs.next()){
	    	rs.close();
	    	ResultSet rss = dbm.getRs("SELECT * FROM Vehicle WHERE vehicleID = '" + vehicleID + "'");
	    	if (rss.next()) {
	    		rss.close();
	    		dbm.destory();
	    		return true;
	    	}
	    	else {
	    		rss.close();
	    		dbm.destory();
	    		return false;
	    	}
	    }
	    
	    else {
	    	rs.close();
	    	dbm.destory();
	    	return false;
	    }
	}
	
	public static void insertSession(XMLSession session) {
				
		DBManager dbm = new DBManager();
		String driverName = session.getDriverName();
		String vehicleID = session.getVehicleID();
		long startTime = session.getStartTime();
		long expireTime = session.getExpireTime();
		
		ResultSet rs =  dbm.getRs("SELECT * FROM Session WHERE session_driverName = '" + driverName  + "'AND session_vehicleID = '"
				+ vehicleID + "'AND startTime = '" + startTime + "'AND expireTime = '" + expireTime +"'");
		
		try {
			if (rs.next()){
				//TODO: if exist,return some message??
			}
			else {
				//if not exist, insert into
				dbm.updb("insert into Session(session_driverName, session_vehicleID, startTime, expireTime) values ('" +  session.getDriverName() + "','" + session.getVehicleID() +"','" 
						+  startTime + "','" + expireTime+ "');");		
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbm.destory();
	}
}
