package server.model;
import java.util.Date;

/**
 * XML Session object.
 * For details about XML format, please see spec.xml
 * 
 * @author Haoyue Zhu
 *
 */

public class XMLSession {
	
	private String driverName = null;
	private String vehicleID = null;
	private long startTime = 0;
	private long expireTime = 0;
	
	public XMLSession(){}
	
	public XMLSession(String driverName, String vehicleID){
		this.driverName = driverName;
		this.vehicleID = vehicleID;
		this.startTime = System.currentTimeMillis();
		this.expireTime = System.currentTimeMillis() + 12 * 60 * 60 * 1000;
	}
	
	public XMLSession(String driverName, String vehicleID, 
			long startTime, long expireTime){
		this.startTime = startTime;
		this.expireTime = expireTime;
		this.driverName = driverName;
		this.vehicleID = vehicleID;
	}
	
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	
	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public String getDriverName() {
		return this.driverName;
	}
	
	
	public String getVehicleID() {
		return this.vehicleID;
	}
	
	public long getStartTime() {
		return this.startTime;
	}	
	
	public long getExpireTime() {
		return this.expireTime;
	}	
	
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public boolean equals(Object s) {
		XMLSession session = (XMLSession)s;
		if ( this.driverName == session.getDriverName() && 
				this.vehicleID == session.getVehicleID() && 
				this.getStartTime() == session.getStartTime()) {
			
			return true;
		}
		
		else
			return false;
		
	}
}
