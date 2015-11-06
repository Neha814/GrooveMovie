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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;







import com.macrew.entity.*;

import com.macrew.utils.AlertUtils;
import com.macrew.utils.NetConnection;
import com.macrew.webServices.WebService;


public class MainActivity extends Activity {

	EditText username, email, password;
	Button signup , back;
	
	String signUp_status;
	String USERNAME , EMAIL, PASSWORD;
	signUpTask signUpObj;
	Boolean isConnected;
	SharedPreferences sharedpreferences;
	
	/**
	 * SIGNUP ACTIVITY
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		username = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		signup = (Button) findViewById(R.id.signup);
		back = (Button) findViewById(R.id.back);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this , Login.class);
				startActivity(i);
			}
		});

		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				USERNAME = username.getText().toString();
				EMAIL = email.getText().toString();
				PASSWORD = password.getText().toString();
				
				
				if (USERNAME.equals("")) {

					new AlertUtils(MainActivity.this,
							AlertUtils.ENTER_EMPTY_USERNAME_WARNING);

					return;

				}

				else if (USERNAME.startsWith(" ")) {
					new AlertUtils(MainActivity.this,
							AlertUtils.USERNAME_STARTS_WITH_SPACE_WARNING);
					return;
				} else if (USERNAME.contains(" ")) {
					new AlertUtils(MainActivity.this,
							AlertUtils.NO_SPACE_ALLOWED);
					return;
				}

				else if (EMAIL.equals("")) {
					new AlertUtils(MainActivity.this,
							AlertUtils.ENTER_EMPTY_EMAIL_WARNING);

					return;

				} else if (PASSWORD.equals("")) {
					new AlertUtils(MainActivity.this,
							AlertUtils.ENTER_EMPTY_PASSWORD_WARNING);

					return;

				}

				else if (USERNAME.equals("")
						&&PASSWORD.equals("")
						&& EMAIL.equals("")) {
					new AlertUtils(MainActivity.this,
							AlertUtils.ENTER_EMPTY_FIELDS_WARNING);

					return;

				} else if (USERNAME.equals("")
						|| PASSWORD.equals("")
						|| EMAIL.equals("")) {
					new AlertUtils(MainActivity.this,
							AlertUtils.ENTER_MISSING_FIELDS_WARNING);

					return;

				} else {
					if (EMAIL
							.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
							&& EMAIL.length() > 0) {
						
						

						signUpObj = new signUpTask();
						/**
						 * Checking if network connection is there or not
						 */
						isConnected = NetConnection
								.checkInternetConnectionn(MainActivity.this);
						if (isConnected == true) {

							signUpObj.execute();
						}

						else {

							// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
							new AlertUtils(MainActivity.this,
									AlertUtils.NO_INTERNET_CONNECTION);

						}

					}

					else {
						new AlertUtils(MainActivity.this,
								AlertUtils.EMAIL_SYNTAX_ERROR_WARNING);

						return;
					}
				}

			}
		});
	}



	

	public class signUpTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(MainActivity.this, "Processing...",
					"Please Wait", true, false);

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebService.SIGNUP_URL);

			String result = null;

			try {

				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair(WebService.USERNAME,
						USERNAME));
				nameValuePairs.add(new BasicNameValuePair(WebService.PASSWORD,
						PASSWORD));
				nameValuePairs.add(new BasicNameValuePair(WebService.EMAIL,
						EMAIL));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("SIGN_UP STATUS", "STATUS OK");

					result = out.toString();
					Log.i("SIGN_UP STATUS result", "" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "......");
			}
			Log.i("result(error in signup)", "=" + result);

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			dialog.dismiss();

			try {

				JSONObject jsonObj = new JSONObject(result);

				signUp_status = jsonObj.getString(WebService.STATUS);

				Log.i("signUp_status", "=" + signUp_status);
				
				
				if (signUp_status.equals("TRUE")) {
					
					//sharedpreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
					
					sharedpreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
					sharedpreferences.edit().clear().commit();
					
					Editor editor = sharedpreferences.edit();

					editor.putString("username",USERNAME);
					editor.putString("email",EMAIL);
					editor.putString("password",PASSWORD);
					editor.commit();
					 
					userDetail.USERNAME = sharedpreferences.getString("username", "");
					userDetail.PASSWORD = sharedpreferences.getString("password", "");
					userDetail.EMAIL = sharedpreferences.getString("email", "");
					
					Log.i("userDetail.USERNAME=","="+userDetail.USERNAME);
					Log.i("userDetail.PASSWORD=","="+userDetail.PASSWORD);
					Log.i("userDetail.EMAIL=","="+userDetail.EMAIL);

					Intent i = new Intent(MainActivity.this, Welcome.class);

					dialog.dismiss();
					startActivity(i);

				}

				else {
					new AlertUtils(MainActivity.this,
							AlertUtils.USER_ALREADY_EXIST);

					return;
				}


			} 
			catch (JSONException e) 
			
			
			{
				new AlertUtils(MainActivity.this,
						AlertUtils.ERROR_ENCOUNTERED);
				e.printStackTrace();
			}

			
		}
	}
}
