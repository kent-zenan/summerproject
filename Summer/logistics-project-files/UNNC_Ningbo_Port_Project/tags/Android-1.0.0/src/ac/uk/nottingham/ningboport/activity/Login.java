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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Configuration.loadConf(this);

		final Button btnLogin = (Button) findViewById(R.id.buttonLogin);
		final EditText txtUn = (EditText) findViewById(R.id.editTextUn);
		final EditText txtPw = (EditText) findViewById(R.id.editTextPw);
		final EditText txtVidEditText = (EditText) findViewById(R.id.editTextVid);

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

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

	public void onPostLoginProcess(String result) {

		if (result == null) {
			Toast.makeText(this, "Error: network failure", Toast.LENGTH_SHORT)
					.show();
		} else if (result.compareTo("") == 0) {
			Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
		} else {
			// Interpret response message
			XMLLoginComm loginResponse = XMLInterpreter
					.inteXmlLoginComm(result);
			// Validate session
			if (loginResponse.isSessionExist()) {
				// Login
				LocalDataManager.getInstance().update(loginResponse);
				startActivity(new Intent(Login.this, TaskList.class));
			} else {
				Toast.makeText(this, "Error: username and password mismatch or vehicle not valid.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
