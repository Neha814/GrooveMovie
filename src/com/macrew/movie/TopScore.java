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

import com.macrew.utils.AlertUtils;
import com.macrew.utils.NetConnection;
import com.macrew.webServices.WebService;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopScore extends Activity {

	public static ArrayList<HashMap<String, String>> TopScoreDetail;
	topScorerTask topScorerObj;
	Boolean isConnected;
	SharedPreferences sharedpreferences;
	int SCORE;
	ListView topScore_Listview;
	LazyAdapter mAdapter;
	Button back;
	LinearLayout adds;
	private static final String AD_UNIT_ID = "ca-app-pub-3056844563146692/9839558607";
	private AdView adView;
	String inHomeScreen;
	
	

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

		setContentView(R.layout.topscore_listview);

		topScore_Listview = (ListView) findViewById(R.id.topScore_Listview);
		back = (Button) findViewById(R.id.back);
		adds = (LinearLayout) findViewById(R.id.adds);
		
		
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(TopScore.this);
		SCORE = sharedpreferences.getInt("SCORE", 0);

		/*AdView adView = new AdView(this);

		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		adds.addView(adView);

		
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("D1A0260DC4E588FF3DCF5300F8B10795").build();

		
		adView.loadAd(adRequest);*/

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(TopScore.this , Welcome.class);
				startActivity(i);
			}
		});

		getTopScorers();

	}

	protected void getTopScorers() {
		topScorerObj = new topScorerTask();

		isConnected = NetConnection.checkInternetConnectionn(TopScore.this);
		if (isConnected == true) {

			topScorerObj.execute();
		}

		else {

			// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
			new AlertUtils(TopScore.this, AlertUtils.NO_INTERNET_CONNECTION);

		}

	}

	public class topScorerTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(TopScore.this, "Processing...",
					"Please Wait", true, false);

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebService.TOP_SCORER);

			String result = null;

			try {

				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Log.i("url in welcome screen=", "=" + WebService.TOP_SCORER
						+ "?" + WebService.SCORE + "=" + SCORE + "&"
						+ WebService.USER_ID + "=" + userDetail.USER_ID);

				nameValuePairs.add(new BasicNameValuePair(WebService.SCORE,
						String.valueOf(SCORE)));
				nameValuePairs.add(new BasicNameValuePair(WebService.USER_ID,
						sharedpreferences.getString("user_id", "")));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("TOP SCORE STATUS", "STATUS OK");

					result = out.toString();
					Log.i("TOP SCORE result", "" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "......");
			}
			Log.i("result(TOP SCORE)", "=" + result);

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			dialog.dismiss();

			try {

				JSONObject jsonObj = new JSONObject(result);

				String status = jsonObj.getString(WebService.STATUS);

				Log.i("top socre status", "=" + status);

				if (status.equals("true")) {

					JSONArray jsonArray = jsonObj.getJSONArray("data");

					TopScoreDetail = new ArrayList<HashMap<String, String>>();
					;

					for (int i = 0; i < jsonArray.length(); i++) {

						Map<String, String> TopScoreList = new HashMap<String, String>();

						JSONObject c = jsonArray.getJSONObject(i);
						String point = c.getString("point");
						TopScoreList.put("point", point);

						String ADMINID = c.getString("ADMINID");
						TopScoreList.put("ADMINID", ADMINID);

						String username = c.getString("username");
						TopScoreList.put("username", username);

						TopScoreDetail.add((HashMap) TopScoreList);

					}
					Log.i("TopScoreDetail list==", "=" + TopScoreDetail);
					mAdapter = new LazyAdapter(TopScoreDetail, TopScore.this);

					topScore_Listview.setAdapter(mAdapter);

				}

				else {

				}

			} catch (JSONException e) {
				e.printStackTrace();

			}
			
			catch (Exception e) {
				e.printStackTrace();
				new AlertUtils(TopScore.this, "Error occured while processing the request.Please try again.");

			}

		}
	}

	/***************** end of async class *******************************************/

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;
		private Activity activity;

		public LazyAdapter(ArrayList<HashMap<String, String>> TopScoreDetail,
				TopScore context) {

			activity = context;
			mInflater = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {

			return TopScoreDetail.size();
		}

		@Override
		public Object getItem(int position) {

			return TopScoreDetail.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.topscore_listitem,
						null);

				holder.points = (TextView) convertView
						.findViewById(R.id.points);
				holder.number = (TextView) convertView
						.findViewById(R.id.number);
				holder.username = (TextView) convertView
						.findViewById(R.id.username);
				
				holder.RR1 =(RelativeLayout) convertView.findViewById(R.id.RR1);
				
				holder.points.setText(TopScoreDetail.get(position).get("point"));
				holder.username.setText(TopScoreDetail.get(position)
						.get("username"));
				holder.number.setText(""+(position+1));

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
				
				holder.points.setText(TopScoreDetail.get(position).get("point"));
				holder.username.setText(TopScoreDetail.get(position)
						.get("username"));
				holder.number.setText(""+(position+1));
			}

			
			
			if (TopScoreDetail.get(position).get("ADMINID").equals(sharedpreferences.getString("user_id", ""))){
				Log.i("ID (color)==","=="+sharedpreferences.getString("user_id", ""));
				
				holder.RR1.setBackgroundResource(R.drawable.highlight);
				
			}
				
				else {
					holder.RR1.setBackgroundResource(R.drawable.key_word_bg);
				}
			

			return convertView;
		}

	}

	class ViewHolder {
		TextView username, points, number;
		RelativeLayout RR1;

	}

}
