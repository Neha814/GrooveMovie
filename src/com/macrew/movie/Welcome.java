package com.macrew.movie;




import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.macrew.entity.userDetail;
import com.macrew.movie.Home.getQuesAnsTask;

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
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;

public class Welcome extends Activity {
	
	//private static final String AD_UNIT_ID = null;

	SharedPreferences sharedpreferences;
	
	String USERNAME , SCORE;
	TextView username;
	Button play,  topScorers , progress,bucks;
	LinearLayout adds;
	getUserIdTask getUserIdObj;
	CountDownTimer newIdFetcher;
	
	
	
	Boolean isConnected;
	
	private static final String AD_UNIT_ID =  "ca-app-pub-3056844563146692/9839558607" ;
	
	
	private AdView adView;
	
		
	public static ArrayList<HashMap<String, String>> TopScoreDetail ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		
		
		
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	        Window w = getWindow(); // in Activity's onCreate() for instance
	        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	    }*/
		
		setContentView(R.layout.welcome);
		
		username = (TextView) findViewById(R.id.username);
		
		play = (Button) findViewById(R.id.play);
		
		topScorers = (Button) findViewById(R.id.topScorers);
		progress = (Button) findViewById(R.id.progress);
		adds = (LinearLayout) findViewById(R.id.adds);
		bucks = (Button) findViewById(R.id.bucks);
		
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Welcome.this);
		Boolean getLaunchFlag = sharedpreferences.getBoolean("isAppLaunch", false);
		
		
		Editor editor1 = sharedpreferences.edit();
		
		
		editor1.putString("STEP", "1");
		
		
		editor1.commit();
		
		
	/*	AdView adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		adds.addView(adView);
		
		
		
		AdRequest adRequest = new AdRequest.Builder()
		    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		    .addTestDevice("D1A0260DC4E588FF3DCF5300F8B10795")
		    .build();

		
		adView.loadAd(adRequest); */
		
		//	setScore(10);
		
		
		
			
		
		
			 
			if(getLaunchFlag==null || !getLaunchFlag){
				Log.i("getScoreFromSp========","="+getLaunchFlag);
				Editor editor = sharedpreferences.edit();
				editor.putBoolean("isAppLaunch",true);
				
				editor.putInt("SCORE", 100);
				editor.putInt("STAGE", 0);
				editor.putString("username","Anonymous");
				editor.commit();
				getUserId();
				
			} else {
				Log.i("GOG TO JEL========","="+getLaunchFlag);
			}
			
			
		USERNAME = sharedpreferences.getString("username", "");
		Log.i("USERNAME++","*******"+USERNAME);
		
		
		
		username.setText(USERNAME);
		
		
		
		
		
		
		progress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Welcome.this , Progress.class);
				
				startActivity(i);
				
			}
		});
		
		
		
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(Welcome.this , Home.class);
				
				startActivity(i);
				
			}
		});
		
		topScorers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Welcome.this , TopScore.class);
				
				startActivity(i);
				
			}
		});
		
		bucks.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Welcome.this , appPurchase.class);
				
				startActivity(i);
				
			}
		});
		
	}

	private void getUserId1() {
		newIdFetcher = new CountDownTimer(2000, 2000) {
			
			

			public void onTick(long m) {
				long sec = m / 1000;
			
				if (NetConnection.checkInternetConnectionn(Welcome.this)) {
					
					Log.i("threads start","start");

					getUserId();
				}

			}

			public void onFinish() {

				System.out.println("************ Done *************");
				this.start();
			}
		};

		newIdFetcher.start();

		
	}
	
	public void getUserId(){
		
		getUserIdObj = new getUserIdTask();
		
		isConnected = NetConnection.checkInternetConnectionn(Welcome.this);
		if (isConnected == true) {

			getUserIdObj.execute();
		}

		else {

			new AlertUtils(Welcome.this, AlertUtils.NO_INTERNET_CONNECTION);

		}
		
	}
	
	public class getUserIdTask extends AsyncTask<Void, Integer, String> {
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebService.USER_ID_URL);

			String result = null;

			try {
				
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("GET user ID", "STATUS OK");

					result = out.toString();
					
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in getting user id", "......");
				Log.i("exception==","=="+e);
				getUserId();
			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			

			try {

				JSONObject jsonObj = new JSONObject(result);

				String USER_ID = jsonObj.getString("user_id");
				
				Editor editor = sharedpreferences.edit();
				editor.putString("user_id", USER_ID);
				editor.commit();
				
				userDetail.USER_ID = sharedpreferences.getString("user_id", "");
				
				Log.i("webServiceResult****", "=======" + userDetail.USER_ID);
				

			
			} catch (JSONException e) {
				e.printStackTrace();

				new AlertUtils(Welcome.this, AlertUtils.ERROR_OCCURED);
			}

			catch (Exception ae) {

			}

		}

	}

/**
 * getUserIdObj = new getUserIdTask();
		
		isConnected = NetConnection.checkInternetConnectionn(Welcome.this);
		if (isConnected == true) {

			getUserIdObj.execute();
		}

		else {

			new AlertUtils(Welcome.this, AlertUtils.NO_INTERNET_CONNECTION);

		}
 */
	
	
}
