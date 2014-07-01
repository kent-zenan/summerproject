package ac.uk.nottingham.ningboport.activity;

import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.conf.Configuration;
import ac.uk.nottingham.ningboport.controller.LocalDataManager;
import ac.uk.nottingham.ningboport.controller.LoginTask;
import ac.uk.nottingham.ningboport.model.XMLLogin;
import ac.uk.nottingham.ningboport.model.XMLLoginComm;
import ac.uk.nottingham.ningboport.model.XMLSession;
import ac.uk.nottingham.ningboport.util.XMLBuilder;
import ac.uk.nottingham.ningboport.util.XMLInterpreter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Login activity.
 * 
 * @author Jiaqi LI
 *
 */
public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Configuration.loadConf(this);

		setContentView(R.layout.login);

		final Button btnLogin = (Button) findViewById(R.id.buttonLogin);
		final EditText txtUn = (EditText) findViewById(R.id.editTextUn);
		final EditText txtPw = (EditText) findViewById(R.id.editTextPw);
		final EditText txtVidEditText = (EditText) findViewById(R.id.editTextVid);

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (txtUn.getText().toString().compareTo("") == 0
						|| txtPw.getText().toString().compareTo("") == 0
						|| txtVidEditText.getText().toString().compareTo("") == 0) {
					Toast.makeText(Login.this,
							getResources().getString(R.string.msgEmptyTable),
							Toast.LENGTH_SHORT).show();
					return;
				}
				// Build post message
				String[] postMsg = new String[1];
				postMsg[0] = XMLBuilder.buildLoginRequest(new XMLLoginComm(
						new XMLSession(), new XMLLogin(txtUn.getText()
								.toString(), txtPw.getText().toString(),
								txtVidEditText.getText().toString())));
				// Execute remote login request
				LoginTask loginTask = new LoginTask(Login.this);
				loginTask.execute(postMsg);
			}
		});

	}

	/**
	 * Called by LoginTask after the login finished or canceled.
	 * This method handles the result of an login attempt.
	 * 
	 * @param login result returned from LoginTask
	 */
	public void onPostLoginProcess(String result) {

		if (result == null) {
			Toast.makeText(this,
					getResources().getString(R.string.msgNetworkFailure),
					Toast.LENGTH_SHORT).show();
		} else if (result.compareTo("") == 0) {
			Toast.makeText(this,
					getResources().getString(R.string.msgCanceled),
					Toast.LENGTH_SHORT).show();
		} else {
			// Interpret response message
			XMLLoginComm loginResponse = XMLInterpreter
					.inteXmlLoginComm(result);
			// Validate session
			if (loginResponse.isSessionExist()) {
				// Login
				LocalDataManager.getInstance().update(loginResponse);
				Intent intent = new Intent(Login.this, TaskList.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else {
				Toast.makeText(this,
						getResources().getString(R.string.msgLoginFailure),
						Toast.LENGTH_LONG).show();
			}
		}
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
			intent = new Intent(Login.this, Settings.class);
			startActivity(intent);
			return true;

		case R.id.menu_about:
			intent = new Intent(Login.this, About.class);
			startActivity(intent);
			return true;
		case R.id.menu_refresh:
			Toast.makeText(this, getResources().getString(R.string.loginRequired), Toast.LENGTH_LONG);
			return true;
		default:
			return true;
		}
	}
}
