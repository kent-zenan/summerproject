package uk.ac.nottingham.ningboport.client.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import uk.ac.nottingham.ningboport.server.db.DBManager;

/**
 * 
 * @author Haoyue Zhu
 */
public class LoginTest {
	//return a String including Login infos
	public static ArrayList<AuthLog> getAllLogin() {
		setLoginList(new ArrayList<LoginManager>());
		
		DBManager m = new DBManager();
		ResultSet rs = m.getRs("SELECT * FROM Login;");
		try {
			while( rs.next() ){
				String driverName = rs.getString("login_driverName");
				String vehicleID = rs.getString("login_vehicleID");
				String password = rs.getString("login_password");
				boolean success = rs.getBoolean("login_success");
				long time = Long.parseLong(rs.getString("login_time"));
				
				LoginManager lm = new LoginManager(driverName,vehicleID,password,success,time);
				loginList.add(lm);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<AuthLog> logs = new ArrayList<AuthLog>();
		for(int i = 0; i < loginList.size(); i ++ ){
			logs.add(new AuthLog(loginList.get(i).getDriverName(), 
					loginList.get(i).getPassword(), 
					loginList.get(i).getVehicleID(), 
					loginList.get(i).isSuccess(), 
					new Date(loginList.get(i).getTime())));
		}

		return logs;
	}
	
	//an ArrayList to save all the Login info
	private static ArrayList<LoginManager> loginList = new ArrayList<LoginManager>();
	
	
	public static ArrayList<LoginManager> getLoginList() {
		return loginList;
	}
	
	public static void setLoginList(ArrayList<LoginManager> ll){
		loginList = ll;
	}	
	
	public static void addLogin(LoginManager lm) {
		loginList.add(lm);
	}
	
}
