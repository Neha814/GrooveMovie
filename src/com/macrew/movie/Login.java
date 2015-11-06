package com.macrew.movie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;




import com.google.android.gms.ads.AdView;
import com.macrew.entity.userDetail;

import com.macrew.utils.AlertUtils;
import com.macrew.utils.NetConnection;
import com.macrew.webServices.WebService;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

public class Login extends Activity {

	EditText username, password;
	Button signIn;
	TextView noAccount, forgot_password;
	Boolean isConnected;
	signInTask signInObj;
	String USERNAME, PASSWORD;
	String signIn_status;
	SharedPreferences sharedpreferences;
	AdView adView;

	/**
	 * SIGNIN ACTIVITY
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.login);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		signIn = (Button) findViewById(R.id.signIn);
		noAccount = (TextView) findViewById(R.id.noAccount);
		forgot_password = (TextView) findViewById(R.id.forgot_password);

		forgot_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(Login.this, forgotPassword.class);

				startActivity(i);
			}
		});

		noAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(Login.this, MainActivity.class);

				startActivity(i);

			}
		});

		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				USERNAME = username.getText().toString();
				PASSWORD = password.getText().toString();

				if (USERNAME.equalsIgnoreCase("")) {
					new AlertUtils(Login.this,
							AlertUtils.ENTER_EMPTY_USERNAME_WARNING);

					return;

				} else if (PASSWORD.equalsIgnoreCase("")) {
					new AlertUtils(Login.this,
							AlertUtils.ENTER_EMPTY_PASSWORD_WARNING);

					return;
				}

				else if (USERNAME.startsWith(" ")) {
					new AlertUtils(Login.this,
							AlertUtils.USERNAME_STARTS_WITH_SPACE_WARNING);
					return;
				} else if (USERNAME.contains(" ")) {
					new AlertUtils(Login.this, AlertUtils.NO_SPACE_ALLOWED);
					return;
				}

				else if (PASSWORD.equals("") && USERNAME.equals("")) {
					new AlertUtils(Login.this,
							AlertUtils.ENTER_EMPTY_FIELDS_WARNING);

					return;

				}

				else if (PASSWORD.equals(" ")
						|| USERNAME.equals(" ")) {
					new AlertUtils(Login.this,
							AlertUtils.ENTER_EMPTY_FIELDS_WARNING);

					return;

				}

				else {

					signInObj = new signInTask();
					/**
					 * Checking if network connection is there or not
					 */
					isConnected = NetConnection
							.checkInternetConnectionn(Login.this);
					if (isConnected == true) {

						signInObj.execute();
					}

					else {

						new AlertUtils(Login.this,
								AlertUtils.NO_INTERNET_CONNECTION);

					}

				}

			}
		});

	}

	public class signInTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(Login.this, "Processing...",
					"Please Wait", true, false);

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebService.LOGIN_URL);

			String result = null;

			try {

				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair(WebService.PASSWORD,
						PASSWORD));
				nameValuePairs.add(new BasicNameValuePair(WebService.USERNAME,
						USERNAME));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("SIGN_IN STATUS", "STATUS OK");

					result = out.toString();
					Log.i("SIGN_IN STATUS result", "" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "......");
			}
			Log.i("result(error in signIn)", "=" + result);

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			dialog.dismiss();

			try {

				JSONObject jsonObj = new JSONObject(result);

				signIn_status = jsonObj.getString(WebService.STATUS);

				Log.i("signIn_status", "=" + signIn_status);

				if (signIn_status.equals("TRUE")) {

					userDetail.USER_ID = jsonObj.getString("user_id");
					Log.i("userDetail.USER_ID=", "USER_ID=="
							+ userDetail.USER_ID);
					sharedpreferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					Editor editor = sharedpreferences.edit();
					editor.putString("username", USERNAME);
					Log.i("USERNAME in login screen==", "=" + USERNAME);
					editor.commit();
					Intent i = new Intent(Login.this, Welcome.class);
					
					dialog.dismiss();
					startActivity(i);

				}

				else {
					new AlertUtils(Login.this,
							AlertUtils.PASSWORD_OR_USERNAME_INCORRECT);

					return;
				}

			} catch (JSONException e) {
				e.printStackTrace();

				new AlertUtils(Login.this,
						AlertUtils.ERROR_OCCURED_WHILE_SIGNIN);
			}

		}
	}

}
