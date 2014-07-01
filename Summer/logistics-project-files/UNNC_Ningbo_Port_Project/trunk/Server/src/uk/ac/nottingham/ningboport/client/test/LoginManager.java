package uk.ac.nottingham.ningboport.client.test;
/**
 * An Object to save all the Login info
 * @author Haoyue Zhu
 *
 */
public class LoginManager {
	private String driverName;
	private String vehicleID;
	private String password;
	private boolean success;
	private long time;
	
	public LoginManager(String driverName, String vehicleID, String password, boolean success, long time){
		this.driverName = driverName;
		this.vehicleID = vehicleID;
		this.password = password;
		this.success = success;
		this.time = time;
	}
	
	public String getDriverName() {
		return driverName;
	}
	
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	public String getVehicleID() {
		return vehicleID;
	}
	
	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
