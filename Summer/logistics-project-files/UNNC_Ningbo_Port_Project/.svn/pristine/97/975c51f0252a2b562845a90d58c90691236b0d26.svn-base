package uk.ac.nottingham.ningboport.server.datamgr;

import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.nottingham.ningboport.client.test.TaskUpdate;
import uk.ac.nottingham.ningboport.network.model.XMLLogin;
import uk.ac.nottingham.ningboport.network.model.XMLLoginComm;
import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.server.db.DBManager;

/**
 * This is the verification function that checking user log in and return session
 * @author Haoyue Zhu
 *
 */

public class Verification {
	/*
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
	*/
	public static XMLLoginComm validateLogin(  XMLLoginComm comm ) {
	    XMLLogin login = comm.getLogin();
	    
	    String driverName = login.getUsername();
	    String password = login.getPassword();
	    String vehicleID = login.getVehicleID();
		
		XMLSession session = new XMLSession();
	    if (checkPassword(driverName, password, vehicleID)){
	    	session = checkSession(driverName, vehicleID);
	    }
	    
	    comm.setSession(session);
	    //TODO: save session into Global
		return comm; 
	}
	


	public static boolean checkPassword(String driverName, String password, String vehicleID) {
		long login_time = System.currentTimeMillis();
		DBManager dbm = new DBManager();

		ResultSet rs = dbm.getRs("SELECT * FROM Driver WHERE driverName = '" + driverName + "'AND password = '" + password + "'");

	    try {
			if (rs.next()){
				rs.close();
				ResultSet rss = dbm.getRs("SELECT * FROM Vehicle WHERE vehicleID = '" + vehicleID + "'");
				if (rss.next()) {
					rss.close();
					dbm.updb("INSERT INTO Login(login_driverName,login_password,login_vehicleID, login_success, login_time) VALUES('" + driverName + "', '" + password + 
							"','" + vehicleID + "'," + true + "," + login_time + ");");
					dbm.destory();
					return true;
				}
				else {
					rss.close();
					dbm.updb("INSERT INTO Login(login_driverName,login_password,login_vehicleID, login_success, login_time) VALUES('" + driverName + "', '" + password + 
							"','" + vehicleID + "'," + false + "," + login_time + ");");
					dbm.destory();
					return false;
				}
			}
			
			else {
				rs.close();
				dbm.updb("INSERT INTO Login(login_driverName,login_password,login_vehicleID, login_success, login_time) VALUES('" + driverName + "', '" + password + 
						"','" + vehicleID + "'," + false + "," + login_time + ");");
				dbm.destory();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static void insertSession(XMLSession session) {
				
		DBManager dbm = new DBManager();
		String driverName = session.getDriverName();
		String vehicleID = session.getVehicleID();
		long startTime = session.getStartTime();
		long expireTime = session.getExpireTime();
		TaskUpdate a = new TaskUpdate();
		ResultSet rs =  dbm.getRs("SELECT * FROM Session WHERE session_driverName = '" + driverName  + "'AND session_vehicleID = '"
				+ vehicleID + "' AND startTime = " + startTime + " AND expireTime = " + expireTime +";");
		
		try {
			if (rs.next()){
				//TODO: if exist,return some message??
			}
			else {
				//if not exist, insert into DBS and assign Route
				dbm.updb("INSERT INTO Session(session_driverName, session_vehicleID, startTime, expireTime) values ('" +  driverName + "','" + vehicleID +"'," 
						+  startTime + "," + expireTime+ ");");	
				dbm = new DBManager();
				ResultSet rss =  dbm.getRs("SELECT sessionID FROM Session WHERE session_driverName = '" + driverName  + "'AND session_vehicleID = '"
						+ vehicleID + "' AND startTime = " + startTime + " AND expireTime = " + expireTime +";");
				if (rss.next()){
					a.assignRoutes(rss.getInt("sessionID"));
				}
				else {
					System.out.println("error!");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbm.destory();
		}
	}
	
	private static XMLSession checkSession(String driverName, String vehicleID) {
		DBManager dbm = new DBManager();
		long currentTime = System.currentTimeMillis();
		TaskUpdate a = new TaskUpdate();
		ResultSet rs =  dbm.getRs("SELECT * FROM Session WHERE session_driverName = '" + driverName  + "'AND session_vehicleID = '"
				+ vehicleID + "' ORDER BY sessionID DESC;");
		
		try {
			if (rs.next()){
				long startTime = Long.parseLong(rs.getString("startTime"));
				long expireTime = Long.parseLong(rs.getString("expireTime"));
				int sessionID = rs.getInt("sessionID");
				if (expireTime > currentTime) {
					// if the session already exist, return the session
					//TODO: return all the tasks of this session to Phone
					a.getTasks(sessionID);
					return new XMLSession(driverName,vehicleID,
							startTime , expireTime);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbm.destory();
		}
		//else, return new session
		//TODO: Assign routes that doesn't assigned and return them to Phone
		XMLSession session = new XMLSession(driverName,vehicleID);
		insertSession(session);
		return session;		
	}
}
