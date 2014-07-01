package uk.ac.nottingham.ningboport.server.datamgr;

import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.nottingham.ningboport.network.model.XMLDataRequest;
import uk.ac.nottingham.ningboport.network.model.XMLDataResponse;
import uk.ac.nottingham.ningboport.network.model.XMLGps;
import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.server.db.DBManager;


public class XMLUpdate {
	
	/*
	public static void main(String[] args) throws Exception {
		XMLGps g = new XMLGps(123.77,44.22);
		XMLSession s = new XMLSession( "Alex", "9A773");
		s.setStartTime(Long.parseLong("1374751656232"));
		s.setExpireTime(Long.parseLong("1374794856232"));
		XMLDataRequest request = new XMLDataRequest(s, g);
		updateXMLData(request);
	}
	*/

	public static XMLDataResponse updateXMLData(XMLDataRequest request) {
		XMLSession session = request.getSession();
		XMLGps gps = request.getGps();
		
		//TODO: first check if session exists?
		String driverName = session.getDriverName();
		String vehicleID = session.getVehicleID();
		long startTime = session.getStartTime();
		long expireTime = session.getExpireTime();		
		
		DBManager dbm = new DBManager();
		ResultSet rs =  dbm.getRs("SELECT * FROM Session WHERE session_driverName = '" + driverName  + "'AND session_vehicleID = '"
				+ vehicleID + "' AND startTime = " + startTime + " AND expireTime = " + expireTime + ";");
		
		try {
			if (rs.next()){
				//Then save GPS info into DBS
				int sessionID = rs.getInt("sessionID");
				long time = System.currentTimeMillis();
				dbm.updb("INSERT INTO Gps(gps_sessionID, longitude, latitude, gps_time ) values (" +  sessionID + ",'" + gps.getLongitude() +"','" 
						+  gps.getLatitude() + "','" + time + "');");			

				dbm.destory();
			}
			else {
				//TODO: if not exist
				dbm.destory();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbm.destory();
		}
		
		return UpdateManager.update(request);
	}
}
