package uk.ac.nottingham.ningboport.client.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uk.ac.nottingham.ningboport.server.db.DBManager;

public class GPSTest {
	//return a String including GPS infos
	public static ArrayList<GPSManager> getAllGPS() {
		setGPSList(new ArrayList<GPSManager>());
		
		DBManager m = new DBManager();
		ResultSet rs = m.getRs("SELECT * FROM Gps");
		try {
			while( rs.next() ){
				double longitude = rs.getDouble("longitude");
				double latitude = rs.getDouble("latitude");
				int sessionID =  rs.getInt("gps_sessionID");
				ResultSet rss = m.getRs("SELECT * FROM Session WHERE sessionID = " + sessionID);
				long time = Long.parseLong(rs.getString("gps_time"));
				if(rss.next()) {
					String driverName = rss.getString("session_driverName");
					String vehicleID = rss.getString("session_vehicleID");
					long startTime = Long.parseLong(rss.getString("startTime"));
					long expireTime = Long.parseLong(rss.getString("expireTime"));
					GPSManager gm = new GPSManager(sessionID, longitude, latitude, driverName, 
							vehicleID, startTime, expireTime, time);
					
					gpsList.add(gm);
				}

				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<GPSManager> gpss= new ArrayList<GPSManager>();
		for(int i = 0; i < gpsList.size(); i ++ ){
			gpss.add(new GPSManager(
					gpsList.get(i).getSessionID(),
					gpsList.get(i).getLongitude(), 
					gpsList.get(i).getLatitude(), 
					gpsList.get(i).getDriverName(), 
					gpsList.get(i).getVehicleID(), 
					gpsList.get(i).getStartTime(), 
					gpsList.get(i).getExpireTime(), 
					gpsList.get(i).getTime()));
		}

		return gpss;
	}
	
	//an ArrayList to save all the GPS
	private static ArrayList<GPSManager> gpsList = new ArrayList<GPSManager>();
	
	
	public static ArrayList<GPSManager> getGPSList() {
		return gpsList;
	}
	
	public static void setGPSList(ArrayList<GPSManager> gl){
		gpsList = gl;
	}	
	
	public static void addGPS(GPSManager gm) {
		gpsList.add(gm);
	}
	
}
