package ac.uk.nottingham.ningboport.conf;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Configuration is a set of Android Preference Data for the entire application.
 * If further extensions are needed, they should be put here, rather than using
 * a new class or database like SQLite. There are six elements for now
 * <ul>
 * <li>Host Address - the URI or IP address of the server. e.g.,
 * http://112.124.50.122</li>
 * <li>Port Number - the port that accept client's HTTP requests. e.g., 8080</li>
 * <li>Login Address - the relative address of Servlet where server handle login
 * request. e.g., /NBPSimplServ/Login</li>
 * <li>Update Address - the relative address of Servlet where server handle
 * update request. e.g., /NBPSimplServ/Report</li>
 * <li>Timeout Value - the maximum time in seconds that a connection can holds.
 * e.g., 5. <strong>Please note this parameter has not been used for now, it's
 * here for further extension. </strong></li>
 * <li>Update Interval - the frequency of update in seconds. e.g., 30</li>
 * </ul>
 * <p>
 * Callers to any get methods are expected to call loadConf() first in order to
 * load latest data.
 * </p>
 * <p>
 * It is callers responsibilities to set the correct new configuration. All set
 * methods will return true (success) as long as the data accepted by the
 * Android system.
 * </p>
 * 
 * @author Jiaqi LI
 * 
 */
public class Configuration {

	public final static String CONF_FILE = "ac.uk.nottingham.ningboport";
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

	/**
	 * Load configuration form system.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 */
	public static void loadConf(Activity activity) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		gHost = sharedPreferences.getString("host", DEFAULT_HOST);
		gPort = sharedPreferences.getString("port", DEFAULT_PORT);
		gTimeout = sharedPreferences.getInt("timeout", DEFAULT_TIME_OUT);
		gUpdateInterval = sharedPreferences.getInt("updateInterval",
				DEFAULT_UPDATE_INTERVAL);
		gLoginAddr = sharedPreferences.getString("loginAddr",
				DEFAULT_LOGIN_ADDR);
		gUpdateAddr = sharedPreferences.getString("updateAddr",
				DEFAULT_UPDATE_ADDR);
	}

	/**
	 * Reset all configuration to the default values.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 */
	public static void resetAll(Activity activity) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("host", DEFAULT_HOST).commit();
		sharedPreferences.edit().putString("port", DEFAULT_PORT).commit();
		sharedPreferences.edit().putInt("timeout", DEFAULT_TIME_OUT).commit();
		sharedPreferences.edit()
				.putInt("updateInterval", DEFAULT_UPDATE_INTERVAL).commit();
		sharedPreferences.edit().putString("loginAddr", DEFAULT_LOGIN_ADDR)
				.commit();
		sharedPreferences.edit().putString("updateAddr", DEFAULT_UPDATE_ADDR)
				.commit();
		Configuration.loadConf(activity);
	}

	/**
	 * Get server address
	 * 
	 * @return host address
	 */
	public static String getgHost() {
		return gHost;
	}

	/**
	 * Get port number
	 * 
	 * @return port number
	 */
	public static String getgPort() {
		return gPort;
	}

	/**
	 * Get timeout value
	 * 
	 * @return timeout value
	 */
	public static int getgTimeout() {
		return gTimeout;
	}

	/**
	 * Get update interval values.
	 * 
	 * @return update interval
	 */
	public static int getgUpdateInterval() {
		return gUpdateInterval;
	}

	/**
	 * Get login address
	 * 
	 * @return login address
	 */
	public static String getgLoginAddr() {
		return gLoginAddr;
	}

	/**
	 * Get update address
	 * 
	 * @return update address
	 */
	public static String getgUpdateAddr() {
		return gUpdateAddr;
	}

	/**
	 * Set server address a new value. This can be either an URI or an IP
	 * address.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 * @param gHost
	 *            the format looks like "http://112.124.50.122"
	 * @return true if update operation succeed, false otherwise
	 */
	public static boolean setgHost(Activity activity, String gHost) {
		Configuration.gHost = gHost;
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("host", gHost).commit();
	}

	/**
	 * Set port number a new value. Tomcat is 8080 by default.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 * @param gPort
	 *            new port number
	 * @return true if update operation succeed, false otherwise
	 */
	public static boolean setgPort(Activity activity, String gPort) {
		Configuration.gPort = gPort;
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("port", gPort).commit();
	}

	/**
	 * Set timeout a new value.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 * @param gTimeout
	 *            new timeout value.
	 * @return true if update operation succeed, false otherwise.
	 */
	public static boolean setgTimeout(Activity activity, int gTimeout) {
		Configuration.gTimeout = gTimeout;
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		return sharedPreferences.edit().putInt("timeout", gTimeout).commit();
	}

	/**
	 * Set update interval a new value.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 * @param gUpdateInterval
	 *            new value of update interval.
	 * @return true if update operation succeed, false otherwise.
	 */
	public static boolean setgUpdateInterval(Activity activity,
			int gUpdateInterval) {
		Configuration.gUpdateInterval = gUpdateInterval;
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		return sharedPreferences.edit()
				.putInt("updateInterval", gUpdateInterval).commit();
	}

	/**
	 * Set login address a new value.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 * @param gLoginAddr
	 *            the new login address. The format should look like
	 *            "/NBPSimplServ/Login"
	 * @return true if update operation succeed, false otherwise.
	 */
	public static boolean setgLoginAddr(Activity activity, String gLoginAddr) {
		Configuration.gLoginAddr = gLoginAddr;
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("loginAddr", gLoginAddr)
				.commit();
	}

	/**
	 * Set update address a new value.
	 * 
	 * @param activity
	 *            the activity that used to get Shared Preference.
	 * @param gUpdateAddr
	 *            the new update address. The format should look like
	 *            "/NBPSimplServ/Report"
	 * @return true if update operation succeed, false otherwise.
	 */
	public static boolean setgUpdateAddr(Activity activity, String gUpdateAddr) {
		Configuration.gUpdateAddr = gUpdateAddr;
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				CONF_FILE, android.content.Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("updateAddr", gUpdateAddr)
				.commit();
	}

}
