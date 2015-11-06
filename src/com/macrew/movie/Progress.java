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


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.macrew.entity.userDetail;
import com.macrew.movie.Home.getQuesAnsTask;

import com.macrew.utils.NetConnection;
import com.macrew.webServices.WebService;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Progress extends Activity {

	TextView username, stage, bucks,time;
	SharedPreferences sharedpreferences;
	String USERNAME;
	int STAGE, TIME;
	String status;
	Button back, ok;
	String inHomeScreen;
	int SCORE;
	RelativeLayout RR5;
	EditText user;
	String getUsername;
	CountDownTimer newMessageFetcher;
	updateUsernameTask updateUsernameObj;
	LinearLayout adds;
	String isUsernameSet;
	private static final String AD_UNIT_ID = "ca-app-pub-3056844563146692/9839558607";
	private AdView adView;
	Long timeInMilliseconds = 0L,startTime=0L,updatedTime=0L, timeSwapBuff=0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.progress);

		username = (TextView) findViewById(R.id.username);
		stage = (TextView) findViewById(R.id.stage);
		ok = (Button) findViewById(R.id.ok);
		bucks = (TextView) findViewById(R.id.bucks);
		back = (Button) findViewById(R.id.back);
		adds = (LinearLayout) findViewById(R.id.adds);
		user = (EditText) findViewById(R.id.user);
		RR5 = (RelativeLayout) findViewById(R.id.RR5);
		time =(TextView) findViewById(R.id.time);

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Progress.this);
		
		
		//username.setVisibility(View.INVISIBLE);
		
		isUsernameSet = sharedpreferences.getString("isUsernameSet", "");
		
		if(isUsernameSet.equals("yes")){
			RR5.setVisibility(View.INVISIBLE);
			String USERNAME = sharedpreferences.getString("username", "");
			username.setText(USERNAME);
			
		}
		
		else {
			RR5.setVisibility(View.VISIBLE);
			username.setText("Anonymous");
		}
		 
/*
		AdView adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		adds.addView(adView);

	
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("D1A0260DC4E588FF3DCF5300F8B10795").build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);*/
		
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (NetConnection.checkInternetConnectionn(Progress.this)) {

					updateUsernameObj = new updateUsernameTask();
					updateUsernameObj.execute();
				}
				
			}
		});
		
		
		
		
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Progress.this , Welcome.class);
				startActivity(i);
			}
		});

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Progress.this);
		
		STAGE = sharedpreferences.getInt("STAGE", 0);
		Log.i("STAGE+++++++++=", "=" + String.valueOf(STAGE));
		
		stage.setText(String.valueOf(STAGE));

		

		SCORE = sharedpreferences.getInt("SCORE", 0);
		Log.i("SCORE+++++++++++++++=", "=" + SCORE);
		bucks.setText(String.valueOf(SCORE));
		
		startTime = SystemClock.uptimeMillis();
		timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
		timeSwapBuff = sharedpreferences.getLong("timeSwapBuff", 0);
		updatedTime = timeSwapBuff + timeInMilliseconds;
		
		int secs = (int) (updatedTime / 1000);
		int mins = secs / 60;
		int hr = mins / 60;
		secs = secs % 60;
		//int milliseconds = (int) (updatedTime % 1000);

		time.setText("" + hr + ":" + String.format("%02d", mins) + ":"
				+ String.format("%02d", secs));
		
		
		
		
		
	}
/**
 * sharedpreferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					Editor editor = sharedpreferences.edit();
					editor.putString("username", USERNAME);
 */
	
	
	
	
	
	/*********** Update username web service *****************************/
	public class updateUsernameTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			/*dialog = ProgressDialog.show(getActivity(),
                    "Loading...", "Please Wait", true, false);
			dialog.show();*/
		}
		
		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					WebService.USERNAME_URL);

			String result = null;

			try {

				getUsername = user.getText().toString();

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				
				
				nameValuePairs.add(new BasicNameValuePair("username",
						getUsername	));
				
				Log.i("id to send===","==="+sharedpreferences.getString("user_id", ""));
				
				nameValuePairs.add(new BasicNameValuePair("ADMINID",
						sharedpreferences.getString("user_id", "")	));
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("Progress Screen Status==", "STATUS OK");

					result = out.toString();
					Log.i("RESULT", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in progress screen", "......" + e);
			}
			
			return result;

			
		}
		
		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//dialog.dismiss();
			
			try {
				JSONObject jsonObj = new JSONObject(result);
				status = jsonObj.getString("status");
				if(status.equals("True") ){
					
					username.setText(getUsername);
					RR5.setVisibility(View.INVISIBLE);	
					
					Editor editor = sharedpreferences.edit();
					editor.putString("username", getUsername);
					editor.putString("isUsernameSet","yes");
					editor.commit();
					
					
				
				}
				else  {
					
					username.setText("Anonymous");
				}
			}
			catch(Exception e){
				
			}
		}

		
	}
	/*********************************************************************/
}


