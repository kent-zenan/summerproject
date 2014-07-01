package ac.uk.nottingham.ningboport.activity;

import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.conf.Configuration;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * This is the credit activity showing the description of the project and name
 * of team members.
 * 
 * @author Jiaqi LI
 * 
 */
public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Configuration.loadConf(this);
		setContentView(R.layout.about);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
}
