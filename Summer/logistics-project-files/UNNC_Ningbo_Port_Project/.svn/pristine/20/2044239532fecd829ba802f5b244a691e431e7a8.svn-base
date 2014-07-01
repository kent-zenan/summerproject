package ac.uk.nottingham.ningboport.controller;

import java.util.Timer;
import java.util.TimerTask;

import ac.uk.nottingham.ningboport.activity.ICheckableActivity;
import ac.uk.nottingham.ningboport.activity.TaskList;
import ac.uk.nottingham.ningboport.conf.Configuration;
import ac.uk.nottingham.ningboport.model.XMLDataRequest;
import ac.uk.nottingham.ningboport.model.XMLDataResponse;
import ac.uk.nottingham.ningboport.model.XMLGps;
import ac.uk.nottingham.ningboport.model.XMLTask;
import ac.uk.nottingham.ningboport.util.XMLBuilder;
import ac.uk.nottingham.ningboport.util.XMLInterpreter;
import android.os.Handler;

/**
 * This class is sort of network manager. Provides methods to send data to
 * server and handle response from server. UnLike LocalDataManager, this class
 * handle the network communication rather than local data. Like
 * LocalDataManager, there will only be one instance of it.
 * 
 * @author Jiaqi LI
 * 
 */
public class PeriodicUpdater {

	private static volatile PeriodicUpdater instance = null;
	private ICheckableActivity activity = null;
	private double longitude = 0;
	private double latitude = 0;

	private boolean isPeriodicUpdateRunning = false;
	private final Handler handler = new Handler();
	private Timer updateTimer = new Timer();
	private TimerTask doAsynchronousTask = new TimerTask() {
		@Override
		public void run() {
			handler.post(new Runnable() {
				public void run() {
					doUpdate();
				}
			});
		}
	};

	/**
	 * Deliberately set to private to force singleton.
	 * 
	 * @param activity
	 *            the activity that implements ICheckableActivity interface.
	 */
	private PeriodicUpdater(ICheckableActivity activity) {
		this.activity = activity;
	}

	/**
	 * Set the associated activity.
	 * 
	 * @param activity
	 *            the activity that associated with the update thread.
	 */
	private void setActivity(ICheckableActivity activity) {
		this.activity = activity;
	}

	/**
	 * Get the instance of updater. If instance is exist, then it will be
	 * returned, otherwise a new instance will be created and returned.
	 * 
	 * @param activity
	 *            the activity which implements ICheckableActivity interface
	 *            that require the updater.
	 * @return the instance of updater.
	 */
	public static PeriodicUpdater getInstance(ICheckableActivity activity) {

		synchronized (PeriodicUpdater.class) {
			if (instance == null) {
				instance = new PeriodicUpdater(activity);
			} else {
				instance.setActivity(activity);
			}
		}
		return instance;
	}

	/**
	 * Start periodic update.
	 */
	public void startPeriodicUpdate() {

		if (!isPeriodicUpdateRunning) {
			updateTimer.schedule(doAsynchronousTask, 0,
					Configuration.getgUpdateInterval() * 1000);
			isPeriodicUpdateRunning = true;
		}
	}

	/**
	 * Stop periodic update. Not used currently, expected to be called when
	 * application exit.
	 */
	public void stopPeriodicUpdate() {

		updateTimer.cancel();
		isPeriodicUpdateRunning = false;
	}

	/**
	 * Do update operation. This will form a XMLDataRequest object which is a
	 * session and a set of GPS location, and call UpdateTask to send it to
	 * server. Read XML specification document in model package for details on
	 * communication protocol.
	 */
	public void doUpdate() {

		if (LocalDataManager.getInstance().getSession().isSessionValid()) {

			String[] postMsg = new String[1];
			postMsg[0] = XMLBuilder.buildDataRequest(new XMLDataRequest(
					LocalDataManager.getInstance().getSession(), new XMLGps(
							longitude, latitude)));

			UpdateTask updateTask = new UpdateTask(this, activity);
			updateTask.execute(postMsg);
		}
	}

	/**
	 * Do update operation. This will form a XMLDataRequest object which is a
	 * session, a set of GPS location and a set of tasks that need to start or
	 * finish, and call UpdateTask to send it to server. Read XML specification
	 * document in model package for details on communication protocol.
	 */
	public void doUpdateWithTask(XMLTask task) {

		if (LocalDataManager.getInstance().getSession().isSessionValid()) {

			XMLDataRequest dataRequest = new XMLDataRequest(LocalDataManager
					.getInstance().getSession(),
					new XMLGps(longitude, latitude));
			dataRequest.setTask(task);
			String[] postMsg = new String[1];
			postMsg[0] = XMLBuilder.buildDataRequest(dataRequest);

			UpdateTask updateTask = new UpdateTask(this, activity);
			updateTask.execute(postMsg);
		}

	}

	/**
	 * Callback function to handle response from server.
	 * 
	 * @param result
	 *            response from the server.
	 */
	public void onPostUpdateProcess(String result) {

		if (result != null && result.compareTo("") != 0) {
			// Interpret response message
			XMLDataResponse response = XMLInterpreter
					.inteXmlDataResponse(result);
			// Validate session
			if (response.isSessionExists()) {
				// Update local data manager
				LocalDataManager.getInstance().update(response);
				if (activity instanceof TaskList) {
					((TaskList) activity).updateList();
				}
			}
		}
	}

	/**
	 * Update longitude.
	 * 
	 * @param l
	 *            new longitude value.
	 */
	public void setLongitude(double l) {
		longitude = l;
	}

	/**
	 * Update latitude.
	 * 
	 * @param l
	 *            new latitude value.
	 */
	public void setLatitude(double l) {
		latitude = l;
	}

	/**
	 * Get longitude value.
	 * 
	 * @return longitude value.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Get latitude value.
	 * 
	 * @return latitude value.
	 */
	public double getLatitude() {
		return latitude;
	}
}
