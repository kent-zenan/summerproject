package ac.uk.nottingham.ningboport.activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.LocationSource;

import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.conf.Configuration;
import ac.uk.nottingham.ningboport.controller.LocalDataManager;
import ac.uk.nottingham.ningboport.controller.PeriodicUpdater;
import ac.uk.nottingham.ningboport.controller.TaskArrayAdapter;
import ac.uk.nottingham.ningboport.model.XMLTask;
import android.location.Location;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * This file need refactoring.
 * 
 * The Task List activity (main screen) of the Android app.
 * 
 * @author Jiaqi LI
 * 
 */
public class TaskList extends ListActivity implements LocationSource,
		AMapLocationListener, ICheckableActivity {

	private boolean running = false;
	private TaskArrayAdapter adapter;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);

		Configuration.loadConf(this);

		PeriodicUpdater.getInstance(this).doUpdate();
		PeriodicUpdater.getInstance(this).startPeriodicUpdate();

		adapter = new TaskArrayAdapter(this, LocalDataManager.getInstance()
				.getAndroidTasks());
		setListAdapter(adapter);
		
		mAMapLocationManager = LocationManagerProxy
				.getInstance(TaskList.this);
		activate(mListener);
		
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = info.position;
		XMLTask task;
		
		switch (item.getItemId()) {
		case 1:
			task = new XMLTask(adapter.getItem(index).getTaskID(), XMLTask.Action.start);
			PeriodicUpdater.getInstance(this).doUpdateWithTask(task);
			break;
		case 2:
			task = new XMLTask(adapter.getItem(index).getTaskID(), XMLTask.Action.finish);
			PeriodicUpdater.getInstance(this).doUpdateWithTask(task);
			break;
		case 3:
			startActivity(new Intent(this, MapRookie.class));
			break;
		default:
			break;
		}
		return false;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Actions");
		menu.add(0, Menu.FIRST, Menu.NONE, "Start");
		menu.add(0, Menu.FIRST + 1, Menu.NONE, "Finish");
		menu.add(0, Menu.FIRST + 2, Menu.NONE, "Go to Map");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("Exit")
					.setMessage("Are sure to exit?")
					.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
								}
							})
					.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		running = false;
		deactivate();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		running = true;
	}
	
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		
		if (mListener != null) {
			mListener.onLocationChanged(location);
		}
		
		if (location != null) {
			PeriodicUpdater.getInstance(this).setLongitude(
					location.getLongitude());
			PeriodicUpdater.getInstance(this).setLatitude(
					location.getLatitude());
			
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {

		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
		}

		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 10000, 10, this);

	}

	@Override
	public void deactivate() {

		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
	
	public void updateList(){
		adapter.clear();
		adapter.addAll(LocalDataManager.getInstance().getAndroidTasks());
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean isRunning() {
		return running;
	}
}