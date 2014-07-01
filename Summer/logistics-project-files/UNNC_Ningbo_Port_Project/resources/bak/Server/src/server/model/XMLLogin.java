package server.model;

/**
 * Login XML Object
 * For details about XML format, please see spec.xml
 * 
 * @author Jiaqi LI
 *
 */
public class XMLLogin {

	private String username = null;
	private String password = null;
	private String vehicleID = null;
	
	public XMLLogin( String u, String p, String id){
		username = u;
		password = p;
		vehicleID = id;
	}

	public XMLLogin(){}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	
}
