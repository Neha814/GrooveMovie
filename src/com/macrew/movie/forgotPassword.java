package com.macrew.movie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;



import com.macrew.movie.Home.getQuesAnsTask;
import com.macrew.utils.AlertUtils;
import com.macrew.utils.NetConnection;
import com.macrew.webServices.WebService;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class forgotPassword extends Activity {

	Button submit , back;
	EditText email;
	String emailAddress;
	forgotPasswordTask forgotPasswordObj;
	Boolean isConnected;
	String webServiceResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.forgot_password);

		submit = (Button) findViewById(R.id.submit);
		email = (EditText) findViewById(R.id.email);
		back = (Button) findViewById(R.id.back);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(forgotPassword.this , Login.class);
				startActivity(i);
				
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				emailAddress = email.getText().toString();
				
				
				if (emailAddress.equals("")) {
					new AlertUtils(forgotPassword.this,
							AlertUtils.ENTER_EMPTY_EMAIL_WARNING);

					return;

				}
				else {
					
					if (emailAddress
							.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
							&& emailAddress.length() > 0) {
						
				forgotPasswordObj = new forgotPasswordTask();
				/**
				 * Checking if network connection is there or not
				 */
				isConnected = NetConnection
						.checkInternetConnectionn(forgotPassword.this);
				if (isConnected == true) {

					forgotPasswordObj.execute();
				}

				else {

					// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
					new AlertUtils(forgotPassword.this,
							AlertUtils.NO_INTERNET_CONNECTION);
				}
					}
					
					else {
						new AlertUtils(forgotPassword.this,
								AlertUtils.EMAIL_SYNTAX_ERROR_WARNING);

						return;
					}
				}

			}
		});

	}
	
/**************** async class for getting question and answer *************************/
	
	public class forgotPasswordTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(forgotPassword.this, "Processing...",
					"Please Wait", true, false);
			
			
			/*dialog = new Dialog(forgotPassword.this);
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog);
			dialog.show();*/

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebService.FORGOT_PASSWORD);

			String result = null;

			try {

				

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("FORGOT PASSWORD STATUS", "STATUS OK");

					result = out.toString();
					Log.i("FORGOT PASSWORD STATUS result", "" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in forgot password section", "......");
			}
			Log.i("result of forgot password section", "=" + result);

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			dialog.dismiss();

			try {

				JSONObject jsonObj = new JSONObject(result);

				webServiceResult = jsonObj.getString(WebService.STATUS);

				Log.i("webServiceResult", "=" + webServiceResult);

				if (webServiceResult.equals("true")) {
					
					new AlertUtils(forgotPassword.this,
							AlertUtils.CHECK_YOUR_INBOX);

					return;
				}

				else {
					new AlertUtils(forgotPassword.this,
							AlertUtils.PLEASE_TRY_AGAIN);

					return;
				}

			}
			catch (JSONException e) {
				e.printStackTrace();

				new AlertUtils(forgotPassword.this,
						AlertUtils.PLEASE_TRY_AGAIN);
			}

		}
}

	
	/********************** ending of async class *********************************************/
	

}
