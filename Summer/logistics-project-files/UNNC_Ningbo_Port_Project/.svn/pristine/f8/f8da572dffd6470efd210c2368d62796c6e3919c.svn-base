package uk.ac.nottingham.ningboport.client.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.ac.nottingham.ningboport.server.db.DBManager;
/**
 * Contains method that processing GPS
 * @author Haoyue Zhu
 *
 */

//TODO:CHANGE the Log Data in Report.jsp to XML transger

public class GPSTest {
	
//	public static void main (String args[] ) {
//        GPSTest gpsTest = new GPSTest("Alex", "", 0 , 0);
//        System.out.println(StringToDate("08/05/2013"));
//        System.out.println(StringToDate("06/08/2013"));
//        System.out.println(System.currentTimeMillis());
//        System.out.println(StringToDate("08/07/2013"));
//
//	}
	
	
	//an ArrayList to save all the GPS
	private ArrayList<GPSManager> gpsList = new ArrayList<GPSManager>();
	//an ArrayList to save the last GPS
	private ArrayList<GPSManager> lastGPSList = new ArrayList<GPSManager>();
	
	//Variables for searching
	private String search_driverName = "";
	private String search_vehicleID = "";
	private long startDate = 0;
	private long endDate = 0;
	
	public GPSTest() {
		
	}
	
	public GPSTest(String driverName, String vehicleID, long startDate, long endDate) {
		this.search_driverName = driverName;
		this.search_vehicleID = vehicleID;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	//return a String including ALL GPS infos
	public ArrayList<GPSManager> getAllGPS() {
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
					
					boolean isDriver = false;
					boolean isVehicle = false;
					boolean isTime = false;
					
					
					if (search_driverName != "") {
						isDriver = (search_driverName == driverName);
					}
					else {
						isDriver = true;
					}

					if (search_vehicleID != "") {
						isVehicle = (search_vehicleID == vehicleID);
					}
					else {
						isVehicle = true;
					}
					
					if (startDate == 0 && endDate == 0){
						isTime = true;
					}
					else {
						if (startDate == 0) {
							if ( time < endDate ) {
								isTime = true;
							}
						}
						else if (endDate == 0) {
							if ( time > startDate ) {
								isTime = true;
							}							
						}
						else {
							if ( time > startDate && time < endDate) {
								isTime = true;
							}							
						}
					}
					
					if (isDriver == true && isVehicle == true && isTime == true){
						GPSManager gm = new GPSManager(sessionID, longitude, latitude, driverName, 
								vehicleID, startTime, expireTime, time);			
						
						gpsList.add(gm);
					}
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
		m.destory();
		return gpss;
	}

	//return a String including Searched GPS infos
	public String getSearchedGPS() {
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
					
					boolean isDriver = false;
					boolean isVehicle = false;
					boolean isTime = false;
					
					
					if (search_driverName != "") {
						isDriver = (search_driverName.equals(driverName));
					}
					else {
						isDriver = true;
					}

					if (search_vehicleID != "") {
						isVehicle = (search_vehicleID.equals(vehicleID));
					}
					else {
						isVehicle = true;
					}
					
					if (startDate == 0 && endDate == 0){
						isTime = true;
					}
					else {
						if (startDate == 0) {
							if ( time < endDate ) {
								isTime = true;
							}
						}
						else if (endDate == 0) {
							if ( time > startDate ) {
								isTime = true;
							}							
						}
						else {
							if ( time > startDate && time < endDate) {
								isTime = true;
							}							
						}
					}
					
					if (isDriver == true && isVehicle == true && isTime == true){
						GPSManager gm = new GPSManager(sessionID, longitude, latitude, driverName, 
								vehicleID, startTime, expireTime, time);			
						
						gpsList.add(gm);
					}
				}

				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		int l = gpsList.size();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<nbp>");
		for(int i = 0; i < l; i++){
			;
			sb.append("<gps>");
			sb.append("<sessionID>" + gpsList.get(i).getSessionID() + "</sessionID>");
			sb.append("<longitude>" + gpsList.get(i).getLongitude() + "</longitude>");
			sb.append("<latitude>" + gpsList.get(i).getLatitude() + "</latitude>");
			sb.append("<driverName>" + gpsList.get(i).getDriverName() + "</driverName>");
			sb.append("<vehicleID>" + gpsList.get(i).getVehicleID() + "</vehicleID>");
			sb.append("<startTime>" + new Date(gpsList.get(i).getStartTime()).toString() + "</startTime>");
			sb.append("<expireTime>" + new Date(gpsList.get(i).getExpireTime()).toString() + "</expireTime>");
			sb.append("<Time>" + new Date(gpsList.get(i).getTime()).toString() + "</Time>");
			sb.append("</gps>");
			
		}
		sb.append("</nbp>");
		m.destory();
		return sb.toString();
	}

		
	//return a String including Last GPS infos
	public String getLastGPS() {
		setLastGPSList(new ArrayList<GPSManager>());
		long currentTime = System.currentTimeMillis();
		DBManager m = new DBManager();
		ResultSet rs = m.getRs("SELECT sessionID,gpsID,longitude,latitude,session_driverName,session_vehicleID,startTime,expireTime,gps_time FROM (SELECT MAX(gpsID) AS last FROM Gps GROUP BY gps_sessionID) AS t, Session, Gps WHERE (sessionID = gps_sessionID AND gpsID = last);");
		try {
			while( rs.next() ){
				long expireTime = Long.parseLong(rs.getString("expireTime"));
				if (expireTime > currentTime) {
					long time = Long.parseLong(rs.getString("gps_time"));
					
					double longitude = rs.getDouble("longitude");
					double latitude = rs.getDouble("latitude");
					int sessionID =  rs.getInt("sessionID");
					String driverName = rs.getString("session_driverName");
					String vehicleID = rs.getString("session_vehicleID");
					long startTime = Long.parseLong(rs.getString("startTime"));
					GPSManager gm = new GPSManager(sessionID, longitude, latitude, driverName, 
								vehicleID, startTime, expireTime, time);
						
					lastGPSList.add(gm);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		int l = lastGPSList.size();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<nbp>");
		for(int i = 0; i < l; i++){
			
			sb.append("<gps>");
			sb.append("<sessionID>" + lastGPSList.get(i).getSessionID() + "</sessionID>");
			sb.append("<longitude>" + lastGPSList.get(i).getLongitude() + "</longitude>");
			sb.append("<latitude>" + lastGPSList.get(i).getLatitude() + "</latitude>");
			sb.append("<driverName>" + lastGPSList.get(i).getDriverName() + "</driverName>");
			sb.append("<vehicleID>" + lastGPSList.get(i).getVehicleID() + "</vehicleID>");
			sb.append("<startTime>" + new Date(lastGPSList.get(i).getStartTime()).toString() + "</startTime>");
			sb.append("<expireTime>" + new Date(lastGPSList.get(i).getExpireTime()).toString() + "</expireTime>");
			sb.append("<Time>" + new Date(lastGPSList.get(i).getTime()).toString() + "</Time>");
			sb.append("</gps>");
			
		}
		sb.append("</nbp>");
		m.destory();
		return sb.toString();
	}	
	
	public ArrayList<GPSManager> getLastGPSList() {
		return lastGPSList;
	}
	
	public void setLastGPSList(ArrayList<GPSManager> gl){
		lastGPSList = gl;
	}	
	
	public void addLastGPSList(GPSManager gm) {
		lastGPSList.add(gm);
	}	
	
	
	
	public ArrayList<GPSManager> getGPSList() {
		return gpsList;
	}
	
	public void setGPSList(ArrayList<GPSManager> gl){
		gpsList = gl;
	}	
	
	public void addGPS(GPSManager gm) {
		gpsList.add(gm);
	}
	
}
