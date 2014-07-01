package ac.uk.nottingham.ningboport.activity;

import java.util.ArrayList;
import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.conf.Configuration;
import ac.uk.nottingham.ningboport.controller.SettingsArrayAdapter;
import ac.uk.nottingham.ningboport.model.AndroidSettingItem;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * The settings activity which display the current configurations and allows
 * user to change it.
 * 
 * @author Jiaqi LI
 * 
 */
public class Settings extends ListActivity {

	private SettingsArrayAdapter adapter;
	private ArrayList<AndroidSettingItem> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Configuration.loadConf(this);

		setContentView(R.layout.settings_list);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		items = new ArrayList<AndroidSettingItem>();

		Configuration.loadConf(this);

		items.add(new AndroidSettingItem(getResources().getString(
				R.string.hostAddr), Configuration.getgHost()));
		items.add(new AndroidSettingItem(getResources().getString(
				R.string.portNum), Configuration.getgPort()));
		items.add(new AndroidSettingItem(getResources().getString(
				R.string.loginAddr), Configuration.getgLoginAddr()));
		items.add(new AndroidSettingItem(getResources().getString(
				R.string.updateAddr), Configuration.getgUpdateAddr()));
		items.add(new AndroidSettingItem(getResources().getString(
				R.string.timeout),
				Integer.toString(Configuration.getgTimeout())));
		items.add(new AndroidSettingItem(getResources().getString(
				R.string.updateInterval), Integer.toString(Configuration
				.getgUpdateInterval())));
		items.add(new AndroidSettingItem(getResources().getString(
				R.string.resetAll), ""));

		adapter = new SettingsArrayAdapter(this, items);
		setListAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			return true;

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		final ListView listView = l;
		final int p = position;
		final EditText userInput = new EditText(this);

		switch (position) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			new AlertDialog.Builder(Settings.this)
					.setTitle(
							getResources().getString(
									R.string.menuSetUpdateContent))
					.setMessage(
							getResources().getString(
									R.string.menuSetUpdateContent))
					.setView(userInput)
					.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									if (updateConf(p, userInput.getText()
											.toString())) {
										((AndroidSettingItem) listView
												.getItemAtPosition(p))
												.setContent(userInput.getText()
														.toString());
									} else {
										Toast.makeText(
												Settings.this,
												getResources().getString(
														R.string.msgSetFailure),
												Toast.LENGTH_SHORT).show();
									}
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();

			adapter.notifyDataSetChanged();
			break;
		case 6:
			new AlertDialog.Builder(Settings.this)
					.setTitle(getResources().getString(R.string.menuResetTitle))
					.setMessage(getResources().getString(R.string.menuConfirm))
					.setPositiveButton(getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Configuration.resetAll(Settings.this);
									((AndroidSettingItem) listView
											.getItemAtPosition(0))
											.setContent(Configuration
													.getgHost());
									adapter.notifyDataSetChanged();
									((AndroidSettingItem) listView
											.getItemAtPosition(1))
											.setContent(Configuration
													.getgPort());
									adapter.notifyDataSetChanged();
									((AndroidSettingItem) listView
											.getItemAtPosition(2))
											.setContent(Configuration
													.getgLoginAddr());
									adapter.notifyDataSetChanged();
									((AndroidSettingItem) listView
											.getItemAtPosition(3))
											.setContent(Configuration
													.getgUpdateAddr());
									adapter.notifyDataSetChanged();
									((AndroidSettingItem) listView
											.getItemAtPosition(4))
											.setContent(Integer
													.toString(Configuration
															.getgTimeout()));
									adapter.notifyDataSetChanged();
									((AndroidSettingItem) listView
											.getItemAtPosition(5)).setContent(Integer
											.toString(Configuration
													.getgUpdateInterval()));
									adapter.notifyDataSetChanged();
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();

			break;
		default:
			break;
		}
	}

	/**
	 * Update configuration by index.
	 * 
	 * @param index
	 *            the index on the activity
	 * @param content
	 *            the new value to be updated
	 * @return true if update succeed, false otherwise
	 */
	private boolean updateConf(int index, String content) {

		switch (index) {
		case 0:
			return Configuration.setgHost(this, content);
		case 1:
			return Configuration.setgPort(this, content);
		case 2:
			return Configuration.setgLoginAddr(this, content);
		case 3:
			return Configuration.setgUpdateAddr(this, content);
		case 4:
			try {
				int iContent = Integer.parseInt(content);
				return Configuration.setgTimeout(this, iContent);
			} catch (Exception e) {
				return false;
			}
		case 5:
			try {
				int iContent = Integer.parseInt(content);
				return Configuration.setgUpdateInterval(this, iContent);
			} catch (Exception e) {
				return false;
			}
		default:
			return false;
		}
	}
}
