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

import ac.uk.nottingham.ningboport.activity.ICheckableActivity;
import ac.uk.nottingham.ningboport.conf.Configuration;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateTask extends AsyncTask<String, Integer, String> {

	private PeriodicUpdater updater;
	private ICheckableActivity activity;
	private ProgressDialog progressDialog;

	public UpdateTask(PeriodicUpdater updater, ICheckableActivity activity) {
		this.updater = updater;
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {

		if (activity.isRunning())
			progressDialog = ProgressDialog.show((Activity)activity, "Processing",
					"Updating tasks...", true, true,
					new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							UpdateTask.this.cancel(true);
						}
					});
	}

	@Override
	protected String doInBackground(String... params) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {

			HttpPost post = new HttpPost(Configuration.DEFAULT_HOST + ":"
					+ Configuration.DEFAULT_PORT
					+ Configuration.getgUpdateAddr());
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

		if(progressDialog != null)
			progressDialog.dismiss();
		updater.onPostUpdateProcess(result);
	}

	@Override
	protected void onCancelled() {
		if(progressDialog != null)
			progressDialog.dismiss();
		updater.onPostUpdateProcess("");
	}

}
