package uk.ac.nottingham.ningboport.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection
 * 
 * @author Haoyue ZHU
 *
 */
public class DBAccess {
	private static Connection connect;
	
	public final static Connection getConnction(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/nbp", "nbp", "cretofnbp");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connect;
	}
	
	public void closeConnection() throws Exception{
		connect.close();
	}
}
