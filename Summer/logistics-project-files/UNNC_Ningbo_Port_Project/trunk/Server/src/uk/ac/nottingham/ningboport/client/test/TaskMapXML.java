package uk.ac.nottingham.ningboport.client.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.nottingham.ningboport.server.db.DBManager;
/**
 * Contains methods that show the predicted route of tasks in the map
 * @author Haoyue Zhu
 */
public class TaskMapXML {
	private String search_driverName = "";
	private String search_vehicleID = "";
	private long startDate = 0;
	private long endDate = 0;
	
	public TaskMapXML() {
		
	}
	
	public TaskMapXML(String driverName, String vehicleID, long startDate, long endDate) {
		this.search_driverName = driverName;
		this.search_vehicleID = vehicleID;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public static void main (String args[] ) {
		TaskMapXML a = new TaskMapXML("","",0,0);
		System.out.print(a.taskToXML());
	}
	
	public String taskToXML(){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<nbp>");
		DBManager m = new DBManager();
		ResultSet rs = m.getRs("SELECT s.sessionID,session_driverName,session_vehicleID,r.shift_normal_start_time,r.route_num,t.planned_start,t.planned_finish,"
				+ "sequence_num,t.small,t.large,commodity_id,source,destination,p1.gps_l as src_l,p2.gps_l as des_l,p1.gps_a as src_a,p2.gps_a as des_a "
				+ "FROM Commodity c, Task t, Session s,Ports p1,Ports p2,Route r "
				+ "WHERE r.sessionID = s.sessionID "
				+ "AND t.commodity_id = c.id  AND t.route_id = r.id "
				+ "AND p1.name = c.source AND p2.name = c.destination ORDER BY s.sessionID ASC,sequence_num ASC;");
		try {
			while(rs.next()){
				int sessionID = rs.getInt("sessionID");
				String driverName = rs.getString("session_driverName");
				String vehicleID = rs.getString("session_vehicleID");
				String shiftTime = rs.getString("shift_normal_start_time");
				long time = StringToLong(shiftTime);
				int routeID = rs.getInt("route_num");
				int sequenceNo = rs.getInt("sequence_num");
				int small = rs.getInt("small");
				int large = rs.getInt("large");	
				String commodityID = rs.getString("commodity_id");
				String src_name = rs.getString("source");
				String des_name = rs.getString("destination");
				double src_longitude = rs.getDouble("src_l");
				double src_latitude = rs.getDouble("src_a");
				double des_longitude = rs.getDouble("des_l");
				double des_latitude = rs.getDouble("des_a");

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
					sb.append("<task>");
					sb.append("<sessionID>" + sessionID + "</sessionID>");
					sb.append("<driverName>" + driverName + "</driverName>");
					sb.append("<vehicleID>" + vehicleID + "</vehicleID>");
					sb.append("<shiftTime>" + shiftTime + "</shiftTime>");
					sb.append("<routeID>" + routeID + "</routeID>");
					sb.append("<sequenceNo>" + sequenceNo + "</sequenceNo>");
					sb.append("<small>" + small + "</small>");
					sb.append("<large>" + large + "</large>");
					sb.append("<commodityID>" + commodityID + "</commodityID>");
					sb.append("<source>" + src_name + "</source>");
					sb.append("<srcLongitude>" + src_longitude + "</srcLongitude>");
					sb.append("<srcLatitude>" + src_latitude + "</srcLatitude>");
					sb.append("<destination>" + des_name + "</destination>");
					sb.append("<desLongitude>" + des_longitude + "</desLongitude>");
					sb.append("<desLatitude>" + des_latitude + "</desLatitude>");
					sb.append("</task>");
				}
				

				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("</nbp>");
		m.destory();
		return sb.toString();
	}
	
	
	private static long StringToLong(String timeString){
		
		if (timeString == "") {
			return 0;
		}
		else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = dateFormat.parse(timeString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return date.getTime();
		}
	}
}
