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
import ac.uk.nottingham.ningboport.model.AndroidTaskItem;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
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
		Configuration.loadConf(this);

		setContentView(R.layout.task_list);

		PeriodicUpdater.getInstance(this).doUpdate();
		PeriodicUpdater.getInstance(this).startPeriodicUpdate();

		adapter = new TaskArrayAdapter(this, LocalDataManager.getInstance()
				.getAndroidTasks());
		setListAdapter(adapter);

		mAMapLocationManager = LocationManagerProxy.getInstance(TaskList.this);
		activate(mListener);

		registerForContextMenu(getListView());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.general, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;
		switch (item.getItemId()) {

		case R.id.menu_settings:
			intent = new Intent(TaskList.this, Settings.class);
			startActivity(intent);
			return true;

		case R.id.menu_about:
			intent = new Intent(TaskList.this, About.class);
			startActivity(intent);
			return true;

		case R.id.menu_refresh:
			PeriodicUpdater.getInstance(this).doUpdate();
			return true;
			
		default:
			return true;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int index = info.position;
		XMLTask task;

		switch (item.getItemId()) {
		case 1:
			task = new XMLTask(adapter.getItem(index).getTaskID(),
					XMLTask.Action.start);
			PeriodicUpdater.getInstance(this).doUpdateWithTask(task);
			adapter.getItem(index).setStatus(AndroidTaskItem.Status.Started);
			break;
		case 2:
			task = new XMLTask(adapter.getItem(index).getTaskID(),
					XMLTask.Action.finish);
			PeriodicUpdater.getInstance(this).doUpdateWithTask(task);
			adapter.getItem(index).setStatus(AndroidTaskItem.Status.Finished);
			break;
		case 3:
			startActivity(new Intent(this, MapRookie.class));
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getResources().getString(R.string.menuActions));
		menu.add(0, Menu.FIRST, Menu.NONE,
				getResources().getString(R.string.menuActionStart));
		menu.add(0, Menu.FIRST + 1, Menu.NONE,
				getResources().getString(R.string.menuActionFinsih));
		menu.add(0, Menu.FIRST + 2, Menu.NONE,
				getResources().getString(R.string.menuGotoMap));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.menuExitTitle))
					.setMessage(getResources().getString(R.string.menuConfirm))
					.setPositiveButton(getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									//PeriodicUpdater.getInstance(TaskList.this).stopPeriodicUpdate();
									TaskList.this.finish();
									android.os.Process.killProcess(android.os.Process.myPid());
								}
							})
					.setNegativeButton(getResources().getString(R.string.no),
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
			Log.i("LOCA-Lat", Double.toString(location.getLatitude()));
			Log.i("LOCA-Lon", Double.toString(location.getLongitude()));

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

	/**
	 * update the whole task list.
	 */
	public void updateList() {
		adapter.clear();
		adapter.addAll(LocalDataManager.getInstance().getAndroidTasks());
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean isRunning() {
		return running;
	}
}