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

	private PeriodicUpdater(ICheckableActivity activity) {
		this.activity = activity;
	}

	private void setActivity(ICheckableActivity activity) {
		this.activity = activity;
	}

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

	public void startPeriodicUpdate() {

		if (!isPeriodicUpdateRunning) {
			updateTimer.schedule(doAsynchronousTask, 0,
					Configuration.getDefaultUpdateInterval() * 1000);
			isPeriodicUpdateRunning = true;
		}
	}

	public void stopPeriodicUpdate() {

		updateTimer.cancel();
		isPeriodicUpdateRunning = false;
	}

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

	public void doUpdateWithTask(XMLTask task){

		if (LocalDataManager.getInstance().getSession().isSessionValid()) {

			XMLDataRequest dataRequest = new XMLDataRequest(
					LocalDataManager.getInstance().getSession(), new XMLGps(
							longitude, latitude));
			dataRequest.setTask(task);
			String[] postMsg = new String[1];
			postMsg[0] = XMLBuilder.buildDataRequest(dataRequest);
			
			UpdateTask updateTask = new UpdateTask(this, activity);
			updateTask.execute(postMsg);
		}

	}
	
	public void onPostUpdateProcess(String result) {

		if (result != null && result.compareTo("") != 0) {
			// Interpret response message
			XMLDataResponse response = XMLInterpreter
					.inteXmlDataResponse(result);
			// Validate session
			if (response.isSessionExists()) {
				// Update local data manager
				LocalDataManager.getInstance().update(response);
				if(activity instanceof TaskList){
					((TaskList)activity).updateList();
				}
			}
		}
	}

	public void setLongitude(double l) {
		longitude = l;
	}

	public void setLatitude(double l) {
		latitude = l;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	
}
