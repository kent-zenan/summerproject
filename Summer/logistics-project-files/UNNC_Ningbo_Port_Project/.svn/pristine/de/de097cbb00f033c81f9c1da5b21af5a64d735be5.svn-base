package ac.uk.nottingham.ningboport.conf;

import android.app.Activity;
import android.content.SharedPreferences;

public class Configuration {

	public final static String CONF_FILE = "nbp_conf";
	public final static String DEFAULT_HOST = "http://112.124.50.122";
	public final static String DEFAULT_PORT = "8080";
	public final static String DEFAULT_LOGIN_ADDR = "/NBPSimplServ/Login";
	public final static String DEFAULT_UPDATE_ADDR = "/NBPSimplServ/Report";
	public final static int DEFAULT_TIME_OUT = 5;
	public final static int DEFAULT_UPDATE_INTERVAL = 30;

	private static String gHost = DEFAULT_HOST;
	private static String gPort = DEFAULT_PORT;
	private static int gTimeout = DEFAULT_TIME_OUT;
	private static int gUpdateInterval = DEFAULT_UPDATE_INTERVAL;
	private static String gLoginAddr = DEFAULT_LOGIN_ADDR;
	private static String gUpdateAddr = DEFAULT_UPDATE_ADDR;
	
	public static void loadConf(Activity activity){
		SharedPreferences sharedPreferences = activity.getSharedPreferences(CONF_FILE, android.content.Context.MODE_PRIVATE); 
		gHost = sharedPreferences.getString("host", DEFAULT_HOST);
		gPort = sharedPreferences.getString("port", DEFAULT_PORT);
		gTimeout = sharedPreferences.getInt("timeout", DEFAULT_TIME_OUT);
		gUpdateInterval = sharedPreferences.getInt("updateInterval", DEFAULT_UPDATE_INTERVAL);
		gLoginAddr = sharedPreferences.getString("loginAddr", DEFAULT_LOGIN_ADDR);
		gUpdateAddr = sharedPreferences.getString("updateAddr", DEFAULT_UPDATE_ADDR);
	}

	public static String getgHost() {
		return gHost;
	}

	public static String getgPort() {
		return gPort;
	}

	public static int getgTimeout() {
		return gTimeout;
	}

	public static int getDefaultUpdateInterval() {
		return gUpdateInterval;
	}

	public static String getgLoginAddr() {
		return gLoginAddr;
	}

	public static String getgUpdateAddr() {
		return gUpdateAddr;
	}
	
	
}
