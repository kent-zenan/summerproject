package ac.uk.nottingham.ningboport.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import ac.uk.nottingham.ningboport.R;
import ac.uk.nottingham.ningboport.activity.Login;
import ac.uk.nottingham.ningboport.conf.Configuration;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Send login request and waiting for response asynchronously. The initiator
 * activity should provided to perform callback and UI actions. <br />
 * Please be aware that this LoginTask can be called safely only if the activity
 * is running. Read Android activity life cycle for details.<br />
 * As this task will be executed only once during the login operation for now,
 * therefore it's safe to be called from the Login activity. <br />
 * However <strong>if further development requires a delayed call or
 * periodically calls, you are strongly advised to perform status check on
 * activity and NULL pointer check on ProgressDialog</strong>. UpdateTask is an
 * example for this.
 * 
 * @author Jiaqi LI
 * 
 */
public class LoginTask extends AsyncTask<String, Integer, String> {

	private Login activity;
	private ProgressDialog progressDialog;

	/**
	 * Construct the LoginTask with a activity
	 * 
	 * @param activity
	 *            the activity that used to display Progress Dialog and handle
	 *            results.
	 */
	public LoginTask(Login activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {

		progressDialog = ProgressDialog.show(activity, activity.getResources()
				.getString(R.string.dialogProc), activity.getResources()
				.getString(R.string.dialogLogin), true, true,
				new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						LoginTask.this.cancel(true);
					}
				});
	}

	@Override
	protected String doInBackground(String... params) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {

			HttpPost post = new HttpPost(Configuration.getgHost() + ":"
					+ Configuration.getgPort() + Configuration.getgLoginAddr());
			post.setEntity(new StringEntity(params[0]));
			Log.i("POST", params[0]);
			response = httpclient.execute(post);

			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {

		if (result != null)
			Log.i("RETURN", result);
		progressDialog.dismiss();
		activity.onPostLoginProcess(result);
	}

	@Override
	protected void onCancelled() {

		progressDialog.dismiss();
		activity.onPostLoginProcess("");
	}
}
