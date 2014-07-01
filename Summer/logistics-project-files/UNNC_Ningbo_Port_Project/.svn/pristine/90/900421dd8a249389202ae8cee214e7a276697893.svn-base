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

import ac.uk.nottingham.ningboport.activity.Login;
import ac.uk.nottingham.ningboport.conf.Configuration;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<String, Integer, String> {

	private Login activity;
	private ProgressDialog progressDialog;
	
	public LoginTask(Login activity){
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		
		progressDialog = ProgressDialog.show(activity, "Processing",
				"Loging in...", true, true,
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
        	
        	HttpPost post = new HttpPost(Configuration.DEFAULT_HOST + ":" +
        			Configuration.DEFAULT_PORT + Configuration.getgLoginAddr());
        	post.setEntity(new StringEntity(params[0]));
        	Log.i("POST", params[0]);
            response = httpclient.execute(post);
            
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {}
        
        return responseString;
	}

	@Override
	protected void onPostExecute(String result) {

		progressDialog.dismiss();
		activity.onPostLoginProcess(result);
	}

	@Override
	protected void onCancelled() {

		progressDialog.dismiss();
		activity.onPostLoginProcess("");
	}
}
