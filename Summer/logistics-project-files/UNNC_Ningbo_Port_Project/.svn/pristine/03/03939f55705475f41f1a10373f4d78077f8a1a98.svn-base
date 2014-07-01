package ac.uk.nottingham.ningboport.activity;

import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.conf.Configuration;
import ac.uk.nottingham.ningboport.controller.PeriodicUpdater;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

/**
 * Map activity, part of the code adopted from AMap demo.
 * Apart from the display of a map, this activity send GPS periodically like TaskList.
 * 
 *  @author Jiaqi LI
 */
public class MapRookie extends Activity implements LocationSource,
		AMapLocationListener, ICheckableActivity {

	private boolean running = false;
	private AMap aMap;
	private MapView aMapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Configuration.loadConf(this);

		setContentView(R.layout.map);
		aMapView = (MapView) findViewById(R.id.map);
		aMapView.onCreate(savedInstanceState);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		aMapView.onResume();
		running = true;

		if (aMap == null) {
			aMap = aMapView.getMap();

			if (aMap == null) {
				Toast.makeText(this,
						getResources().getString(R.string.msgMapNotReady),
						Toast.LENGTH_SHORT).show();
			} else {
				setUpMap();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, TaskList.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;

		case R.id.menu_settings:
			intent = new Intent(MapRookie.this, Settings.class);
			startActivity(intent);
			return true;

		case R.id.menu_about:
			intent = new Intent(MapRookie.this, About.class);
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
	protected void onPause() {
		super.onPause();
		aMapView.onPause();
		deactivate();
		running = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		aMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		aMapView.onLowMemory();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		aMapView.onSaveInstanceState(outState);
	}

	/**
	 * Initiate map.
	 */
	private void setUpMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));
		myLocationStyle.strokeColor(Color.BLACK);
		myLocationStyle.strokeWidth(5);
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);

		mAMapLocationManager = LocationManagerProxy.getInstance(MapRookie.this);

		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				PeriodicUpdater.getInstance(this).getLatitude(),
				PeriodicUpdater.getInstance(this).getLongitude()), 10));
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

	/**
	 * Call back function after location
	 */
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
			
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					PeriodicUpdater.getInstance(this).getLatitude(),
					PeriodicUpdater.getInstance(this).getLongitude()), aMap
					.getCameraPosition().zoom));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.general, menu);
		return true;
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
		}

		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 3000, 10, this);

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

	@Override
	public boolean isRunning() {
		return running;
	}
}
