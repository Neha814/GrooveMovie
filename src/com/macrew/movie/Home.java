package com.macrew.movie;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.net.DatagramSocket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;

import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;

import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import com.facebook.android.Facebook;

import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.macrew.entity.userDetail;

import com.macrew.utils.AlertUtils;
import com.macrew.utils.NetConnection;
import com.macrew.webServices.WebService;

import android.R.color;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;

import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Browser.BookmarkColumns;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import android.util.Log;
import android.view.Display;

import android.view.View.OnClickListener;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import android.widget.TextView;

public class Home extends Activity implements AnimationListener {

	private static final String AD_UNIT_ID = "ca-app-pub-3056844563146692/9839558607";
	private AdView adView;
	LinearLayout adds;

	TextView question, score, timer, stage1, stage2, stage3;
	String questionText, answer_text, getAnswer, alreadyAskedQuestion;
	int Stage = 0;
	char firstChar, randomChar;
	int no_of_words;
	RelativeLayout AnswerLayout, RR1, answers;
	int s4;
	float screenDensity;
	
	ImageView iv, imageView1;
	int width;
	int screen_width, screen_height;
	TextView et;
	TextView bt;
	TextView tv, inner_timer;
	java.util.Date noteTS;
	Boolean getLaunchFlag;
	Boolean isHintClicked = false;
	Boolean getScoreFromSp;
	int getScore;
	int lives;
	int index;
	Boolean isConnected, isSolveClicked = false;
	String webServiceResult;
	getQuesAnsTask getQuesAnsObj;
	SharedPreferences sharedpreferences;
	String appName = "twitter";
	int layoutWidth;
	int right = 0;
	int hintClicked;
	int ansNo = 0;
	String ANS, ANS2;
	int selectedPosition = -1;
	int tag1 = 0;
	int position1;
	int space_to_left;
	CountDownTimer newMessageFetcher;
	/****** Timer Variables *********/

	private long startTime = 0L;
	Boolean isSkippedClicked = false;
	private Handler customHandler = new Handler();

	long timeInMilliseconds = 0L;

	long timeSwapBuff = 0L;

	long updatedTime = 0L;
	
	Boolean isTimeShownFirstTime = true;
	
	int cursor = 1;

	/******************************/

	Button hint, skip, home, twitter, fb, solve;

	ArrayList<Integer> hintList = new ArrayList<Integer>();

	List<String> names = new ArrayList<String>();
	String sp_words;

	List<TextView> allEds;
	List<TextView> allEds1;
	Boolean isSolvedPress = false;

	private static final String APP_ID = "632462720206521";

	Facebook facebook = new Facebook(APP_ID);
	FacebookDialog shareDialog;
	private UiLifecycleHelper uiHelper;

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	boolean isResuming;
	// Animation
	Animation animZoomIn;
	Animation animZoomOut;
	
	CounterClass inner_timer_counter;

	/** ---------------------------------- */

	/***
	 * for hiding soft keyboard on touching the screen
	 */
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) {
	 * InputMethodManager imm = (InputMethodManager)
	 * getSystemService(Context.INPUT_METHOD_SERVICE);
	 * imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	 * return true; }
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Home.this);
		
		
		

		
		getLaunchFlag = sharedpreferences
				.getBoolean("isAppLaunchinHome", false);

		Editor editor = sharedpreferences.edit();
		editor.putBoolean("isAppLaunchinHome", true);
		editor.commit();

		startTime = SystemClock.uptimeMillis();

		customHandler.postDelayed(updateTimerThread, 0);

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		Editor editor1 = sharedpreferences.edit();
		editor1.putString("inHomeScreen", "YES");
		editor1.commit();

		if (getCurrentFocus() != null) {

			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}

		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(
					"com.facebook.android", 0);
			uiHelper = new UiLifecycleHelper(this, null);
			uiHelper.onCreate(savedInstanceState);
			shareDialog = new FacebookDialog.ShareDialogBuilder(this).build();
		} catch (PackageManager.NameNotFoundException e) {

		}

		/**
		 * to get screen width and height
		 */

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screen_width = size.x;
		screen_height = size.y;
	

		screenDensity = getResources().getDisplayMetrics().density;
		

		setContentView(R.layout.new_home2);

		hint = (Button) findViewById(R.id.hint);
		skip = (Button) findViewById(R.id.skip);

		home = (Button) findViewById(R.id.home);
		question = (TextView) findViewById(R.id.question);
		score = (TextView) findViewById(R.id.score);
		AnswerLayout = (RelativeLayout) findViewById(R.id.AnswerLayout);
		stage1 = (TextView) findViewById(R.id.stage1);
		stage2 = (TextView) findViewById(R.id.stage2);
		stage3 = (TextView) findViewById(R.id.stage3);
		twitter = (Button) findViewById(R.id.twitter);
		fb = (Button) findViewById(R.id.fb);
		timer = (TextView) findViewById(R.id.timer);
		answers = (RelativeLayout) findViewById(R.id.answers);
		solve = (Button) findViewById(R.id.solve);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		inner_timer = (TextView) findViewById(R.id.inner_timer);

		adds = (LinearLayout) findViewById(R.id.adds);

//		fb.bringToFront();
//		twitter.bringToFront();
//		hint.bringToFront();
//		skip.bringToFront();
//		solve.bringToFront();

		/*
		 * AdView adView = new AdView(this);
		 * 
		 * adView.setAdSize(AdSize.SMART_BANNER);
		 * adView.setAdUnitId(AD_UNIT_ID); adds.addView(adView);
		 * 
		 * 
		 * AdRequest adRequest = new AdRequest.Builder()
		 * .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		 * .addTestDevice("D1A0260DC4E588FF3DCF5300F8B10795").build();
		 * 
		 * 
		 * adView.loadAd(adRequest);
		 */

		// lookUpForNewMessage();
		// load the animation
		animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_in);

		animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom_out);
		
		animZoomOut.setAnimationListener(this);

		/********************* facebook twitter sharing ***************************/

		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {

					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_TEXT, "It's a Tweet!"
							+ "#MyApp");
					intent.setType("text/plain");
					final PackageManager pm = getPackageManager();

					final List<?> activityList = pm.queryIntentActivities(
							intent, 0);
					int len = activityList.size();
					for (int i = 0; i < len; i++) {

						final ResolveInfo app = (ResolveInfo) activityList
								.get(i);
						
						
						if ((app.activityInfo.name.contains(appName))) {
							Log.i("twitter==<>", "" + app.activityInfo.name);

							final ActivityInfo activity = app.activityInfo;
							final ComponentName x = new ComponentName(
									activity.applicationInfo.packageName,
									activity.name);

							intent = new Intent(Intent.ACTION_SEND);
							intent.addCategory(Intent.CATEGORY_LAUNCHER);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							intent.setComponent(x);
							
							intent.putExtra(
									Intent.EXTRA_TEXT,
									"Score for Groove Movie is : "
											+ sharedpreferences.getInt("SCORE",
													0));
							intent.setType("application/twitter");

							startActivity(intent);

							break;

						}
					}
				} catch (final ActivityNotFoundException e) {
					Log.i("Twitter intent", "no twitter native", e);
				}

				/*
				 * Toast.makeText(Home.this.getApplicationContext(),
				 * "Your Post has been shared", Toast.LENGTH_LONG) .show();
				 */
			}
		});

		fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				goFacebookLogin();
				
			}

		});

		/*************************************************************************/

		ScrollView scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		scrollView1.setVerticalScrollBarEnabled(true);

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		getScoreFromSp = sharedpreferences.getBoolean("isAppLaunch", false);

		

		if (getScoreFromSp == null) {

			Editor editor2 = sharedpreferences.edit();

			editor2.commit();
			setScore(100);
		} else {
			setScoreText();

		}
		
		
		

		/**
		 * get current time of system.
		 */
		
		
		
		Calendar cal = Calendar.getInstance();
		
	
		Date date = cal.getTime();
	    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    String strDate = df.format(date);
	   
	    
	    /**
		 * differnce betweeen stored time and current time
		 */
	    	Date current = null;
	    	Date stored = null;
	    	
	    	try{
	    	current = df.parse(strDate);
	    	stored = df.parse((sharedpreferences.getString("timeStored", "")));
	    	
	    	if(stored.equals("")){
	    		stored = df.parse("00/00/00 00:00:00");
	    	}
	    	
	    	//Log.e("date format==",""+strDate);
	    //	Log.e("stored format==",""+sharedpreferences.getString("timeStored", ""));
	    	
	    	long diff = current.getTime() - stored.getTime();
	    	
	    	long diffDays = diff / (24 * 60 * 60 * 1000);
	    	
	    	//Log.e("diffDays==",""+diffDays);
	    	
	    	long diffSeconds = diff / 1000 % 60;
	    	
	    	//Log.e("diffSeconds==",""+diffSeconds);
	    	
	    	if(diffDays>=1){
	    		
	    		int bucsk_to_add = (int)(diffDays*5);
	    		
	    		setScore(getScore+bucsk_to_add);
	    		
	    	
	    		
	    		
	    	}
	    	}
	    	
	    	catch(Exception e){
	    		Log.e("Exception(diffDays)==",""+e);
	    	}
	    	
	    Editor ed = sharedpreferences.edit();
	    ed.putString("timeStored", strDate);
	    ed.commit();
		
		

		/***
		 * CALL FOR ASYNC CLASS FOR GETTING QUESTIONS AND ANSWERS
		 **/

		getQuesAnsObj = new getQuesAnsTask();
		/**
		 * Checking if network connection is there or not
		 */
		

		solve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/****** show dialog ********/
				final Dialog dialog;
				
				
				onPause();
				dialog = new Dialog(Home.this);
				dialog.setCancelable(false);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.inner_timer_dialog);

				
				dialog.show();

				new CountDownTimer(5000, 1000) {

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

						dialog.dismiss();
						
						 inner_timer_counter = new CounterClass(31000,1000);  
							onPause(); // to stop timer
							isSolvedPress = true;
							
							inner_timer.setVisibility(View.VISIBLE);
							inner_timer_counter.start();  
							solve.setEnabled(false);
						//onResume();
					}
				}.start();
				
				/***************************/
			
			}

		});

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				putQuesMidInSp();

				Intent i = new Intent(Home.this, Welcome.class);
				onPause();
				startActivity(i);

				

			}
		});

		skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isSkippedClicked = true;
				
				if (getScore < 5) {
					final AlertDialog alert = new AlertDialog.Builder(Home.this)
							.create();
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.alert_for_skip,
							(ViewGroup) findViewById(R.id.root));

					Button cancel = (Button) view.findViewById(R.id.cancel);
					Button purchase = (Button) view.findViewById(R.id.purchase);

					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							alert.dismiss();

						}
					});

					purchase.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = new Intent(Home.this, appPurchase.class);

							alert.dismiss();
							startActivity(i);

						}
					});
					alert.setView(view);
					alert.show();

					return;
				}
				
				else {
				setScore(getScore - 5);
				removeQuestionFromList();
				ansNo = 0;
				}
				

				showQuestion();

			}
		});

		hint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isHintClicked = true;

				if (getScore < 5) {
					final AlertDialog alert = new AlertDialog.Builder(Home.this)
							.create();
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.my_alert_dialog,
							(ViewGroup) findViewById(R.id.root));

					Button cancel = (Button) view.findViewById(R.id.cancel);
					Button purchase = (Button) view.findViewById(R.id.purchase);

					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							alert.dismiss();

						}
					});

					purchase.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = new Intent(Home.this, appPurchase.class);

							alert.dismiss();
							startActivity(i);

						}
					});
					alert.setView(view);
					alert.show();

					return;
				}
				if (hintList.size() >= answer_text.length()
						|| !validateAnswerFields()) {
					new AlertUtils(Home.this, AlertUtils.NO_MORE_HINTS);
					return;
				}

				

				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(Home.this);

				hintClicked = sharedpreferences.getInt(
						"NO_OF_TIMES_HINT_CLICKED", 0);
				hintClicked = hintClicked + 1;

				Editor editor = sharedpreferences.edit();
				editor.putInt("NO_OF_TIMES_HINT_CLICKED", hintClicked);
				editor.commit();

				lives = 2;

				int tag1 = validHint();
			
				hintList.add(tag1);

				setHintText();

				setScore(getScore - 5);

			}
		});
		
		
		isConnected = NetConnection.checkInternetConnectionn(Home.this);
		if (isConnected == true) {
			if (userDetail.QuestionsAnswerList.size() <= 0
					|| userDetail.QuestionsAnswerList.equals(null)) {

				getQuesAnsObj.execute();
			} else {
				showQuestion();
				isResuming = true; // resuming activity so do not genreate random question
			}
		}

		else {

			new AlertUtils(Home.this, AlertUtils.NO_INTERNET_CONNECTION);

		}

	}

	Runnable updateTimerThread = new Runnable() {

		public void run() {

			if (getLaunchFlag == null || !getLaunchFlag) {

				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
				updatedTime = timeSwapBuff + timeInMilliseconds;

				Editor editor = sharedpreferences.edit();

				editor.putLong("timeSwapBuff", timeSwapBuff);
				editor.putLong("timeInMilliseconds", timeInMilliseconds);
				editor.putBoolean("isAppLaunchinHome", true);
				editor.commit();
			} else {

				timeSwapBuff = sharedpreferences.getLong("timeSwapBuff", 0);
				timeInMilliseconds = sharedpreferences.getLong(
						"timeInMilliseconds", 0);

				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
				updatedTime = timeSwapBuff + timeInMilliseconds;

				Editor editor = sharedpreferences.edit();

				editor.putLong("timeSwapBuff", timeSwapBuff);
				editor.putLong("timeInMilliseconds", timeInMilliseconds);

				editor.commit();
			}

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			int hr = mins / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);

			timer.setText("" + hr + ":" + String.format("%02d", mins) + ":"
					+ String.format("%02d", secs));
			customHandler.postDelayed(this, 0);
		}

	};

	protected void matchAnswer() {
		int stage = 0;

		
		Stage = 1;

		if (isSolveClicked == false) {

			sharedpreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			int STAGE = sharedpreferences.getInt("STAGE", 0);
			stage = STAGE + Stage;
			Editor editor1 = sharedpreferences.edit();
			editor1.putInt("STAGE", stage);
			editor1.commit();
		}

		else {
			isSolveClicked = false;
		}

		String ANSWER_TEXT = answer_text.toUpperCase();

		int correctMatchFound = 0;
		for (int i = 0; i < ANSWER_TEXT.length(); i++) {

			

			char CharToMatch = ANSWER_TEXT.charAt(i);

			TextView etAns = allEds.get(i);
			if (etAns.getText().length() <= 0) {

				break;
			}
			char etANS = etAns.getText().charAt(0);

			if (CharToMatch != etANS) {

				break;
			}

			correctMatchFound++;

		}
		if (correctMatchFound == answer_text.length()) {

			putQuesMidInSp();
			
			try{
			inner_timer_counter.cancel();
			inner_timer.setText("00:00:00");
			inner_timer.setVisibility(View.INVISIBLE);
			}
			
			catch(Exception e){
				Log.e("inner timer exception",""+e);
			}

			Editor editor = sharedpreferences.edit();

			editor.commit();

			final Dialog dialog;
			TextView textView2;
			onPause();
			dialog = new Dialog(Home.this);
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog);

			textView2 = (TextView) dialog.findViewById(R.id.textView2);

		

			if (ansNo == 0) {
				textView2.setText("On To Stage 2!!!");
			}

			if (ansNo == 1) {
				textView2.setText("On To Stage 3!!!");
			}

			if (ansNo == 2) {
				textView2.setText("On To next Question!!!");
			}
			dialog.show();

			new CountDownTimer(2000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

					dialog.dismiss();
					onResume();
				}
			}.start();

			/********************* SET SCORE *********************************************************/
			int hint_number = sharedpreferences.getInt(
					"NO_OF_TIMES_HINT_CLICKED", 0);
			if (hint_number < 1) {

				setScore(getScore + 2);
			}

			else if (hint_number == 1) {

				setScore(getScore + 1);
			}

			else {

				setScore(getScore);
			}

			ansNo++;
			showQuestion();

		} else {
			putQuesMidInSp();

			

		}

	}

	private void putQuesMidInSp() {

		Editor editor = sharedpreferences.edit();
		String intialMID = sharedpreferences.getString("MID", "");

		if (intialMID.length() > 0) {
			if (alreadyAskedQuestion != null) {
				intialMID += "," + alreadyAskedQuestion;
				
			} else {
				
			}
		} else {
			intialMID = alreadyAskedQuestion;
		
		}
		

		editor.putString("MID", intialMID);
		editor.commit();

	}

	private void setScoreText() {

		getScore = sharedpreferences.getInt("SCORE", 0);

		score.setText("" + getScore);

	}

	void setScore(int Score) {

		Editor editor = sharedpreferences.edit();
		editor.putInt("SCORE", Score);

		editor.commit();
		setScoreText();
	}

	/**
	 * 
	 * to get random number
	 */

	private int getRandomNumber(int Size) {

		Random r = new Random();
	

		int index = r.nextInt(Size) + 1;

		

		return index;

	}

	/**
	 * 
	 * to get hint
	 */

	private int validHint() {
		int randNo = (int) (Math.random() * answer_text.length());

		char CHAR;
		CHAR = answer_text.charAt(randNo);
		TextView edt = allEds.get(randNo);

		/********* ADDITONAL THINGS ADDED ********************/
		String abc = edt.getText().toString();

		if (hintList.contains(randNo) || String.valueOf(CHAR).equals(" ")
				|| abc.length() == 1) {

			return validHint();
		}

		return randNo;
	}

	/**
	 * 
	 * to get hint
	 */

	private boolean validateAnswerFields() {
		boolean spaceFound = false;
		for (int i = 0; i < allEds.size(); i++) {

			TextView et = allEds.get(i);
			if (et.getText().length() <= 0) {
				spaceFound = true;
				break;
			}

		}

		return spaceFound;
	}

	/**
	 * to show question and answer
	 */
	private void showQuestion() {

		tag1 = 0;
		hint.setEnabled(true);
		if (isSolvedPress) {
			onResume();
			inner_timer_counter.cancel();
			  inner_timer.setVisibility(View.INVISIBLE);
			  removeQuestionFromList();
			isSolvedPress = false;
		}
		allEds = new ArrayList<TextView>();

		hintList.clear();

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Home.this);

		Editor editor = sharedpreferences.edit();
		editor.putInt("NO_OF_TIMES_HINT_CLICKED", 0);
		editor.commit();

		lives = 3;

		if (ansNo == 3) {
			removeQuestionFromList();
			ansNo = 0;

		}

		if (userDetail.QuestionsAnswerList.size() <= 0
				|| userDetail.QuestionsAnswerList.equals(null)) {

			if (isConnected == true) {

				getQuesAnsObj = new getQuesAnsTask();
				getQuesAnsObj.execute();

			}

			else {

				new AlertUtils(Home.this,
						AlertUtils.YOU_HAVE_PLAYED_ALL_THE_QUESTIONS);

			}

			return;

		}

		
		if (ansNo == 0 && isResuming) {

			/**
			 * stage 1
			 */

			userDetail.randQuesNo = getRandomNumber(userDetail.QuestionsAnswerList
					.size());
		

		} 

		else {
			isResuming = false;
		}

		if (userDetail.randQuesNo >= userDetail.QuestionsAnswerList.size())
			userDetail.randQuesNo--;

		question.setText(userDetail.QuestionsAnswerList.get(
				userDetail.randQuesNo).get(userDetail.QUESTIONS));
		
		

		

		Log.i("question=", "==" + question.getText().toString());

		
		/**
		 * ACTOR NAME
		 */

		if (ansNo == 1) {
			stage2.setBackgroundResource(R.drawable.stage2_green);
			stage1.setBackgroundResource(R.drawable.stage1);
			stage3.setBackgroundResource(R.drawable.stage3);
			answer_text = userDetail.QuestionsAnswerList.get(
					userDetail.randQuesNo).get(userDetail.ANSWERS2);
			Log.i("when ansNo==1", "==" + answer_text);
			setKeyboard();
			
			hint.setEnabled(false);
			skip.setEnabled(false);
			solve.setEnabled(false);
			fb.setEnabled(false);
			twitter.setEnabled(false);
			// set animation listener
			animZoomIn.setAnimationListener(this);
			
			animZoomIn.setFillAfter(true);
			animZoomIn.setFillEnabled(true);
			
			
			
			new Handler().postDelayed(new Runnable() {
				public void run() {
					imageView1.bringToFront();
					onPause();
					imageView1.setBackgroundResource(R.drawable.actors_name);
					imageView1.setVisibility(View.VISIBLE);
					
					imageView1.startAnimation(animZoomIn);
				}
			}, 2000L);
			}

		

		/**
		 * CHARACTER NAME
		 */
		if (ansNo == 2) {
			stage3.setBackgroundResource(R.drawable.stage3_green);
			stage1.setBackgroundResource(R.drawable.stage1);
			stage2.setBackgroundResource(R.drawable.stage2);
			answer_text = userDetail.QuestionsAnswerList.get(
					userDetail.randQuesNo).get(userDetail.ANSWERS3);
			Log.i("when ansNo==2", "==" + answer_text);
			setKeyboard();
			hint.setEnabled(false);
			skip.setEnabled(false);
			solve.setEnabled(false);
			fb.setEnabled(false);
			twitter.setEnabled(false);

			animZoomIn.setAnimationListener(this);
			animZoomIn.setFillAfter(true);
			animZoomIn.setFillEnabled(true);

			new Handler().postDelayed(new Runnable() {
				public void run() {
					imageView1.bringToFront();
					onPause();
					imageView1
							.setBackgroundResource(R.drawable.actors_character);

					imageView1.setVisibility(View.VISIBLE);
					
					imageView1.startAnimation(animZoomIn);
				}
			}, 2000L);

		}

		/* case 0 or 3 ********* */

		/**
		 * MOVIE QUOTE
		 */
		else if (ansNo == 0) {

			stage1.setBackgroundResource(R.drawable.stage1_green);
			stage2.setBackgroundResource(R.drawable.stage2);
			stage3.setBackgroundResource(R.drawable.stage3);

			answer_text = userDetail.QuestionsAnswerList.get(
					userDetail.randQuesNo).get(userDetail.ANSWERS1);
			Log.i("when ansNo==0", "==" + answer_text);
			setKeyboard();
			
			
			hint.setEnabled(false);
			skip.setEnabled(false);
			solve.setEnabled(false);
			fb.setEnabled(false);
			twitter.setEnabled(false);
			animZoomIn.setAnimationListener(this);
			animZoomIn.setFillAfter(true);
			animZoomIn.setFillEnabled(true);

			if(isSkippedClicked){
				imageView1.bringToFront();
				onPause();
				imageView1.setBackgroundResource(R.drawable.guess_movie_quote);
				imageView1.setVisibility(View.VISIBLE);
				imageView1.startAnimation(animZoomIn);
				isSkippedClicked = false;
			}
			else {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					imageView1.bringToFront();
					onPause();
					imageView1.setBackgroundResource(R.drawable.guess_movie_quote);
				
					imageView1.setVisibility(View.VISIBLE);
					imageView1.startAnimation(animZoomIn);
				}
			}, 2000L);
			}

		}

		alreadyAskedQuestion = userDetail.QuestionsAnswerList.get(
				userDetail.randQuesNo).get("mid");
	
		putQuesMidInSp();

		/**
		 * removing item from array list
		 */

		firstChar = answer_text.charAt(0);

		width = answer_text.length();
		int hintArray[] = new int[answer_text.length()];

		/**
		 * putting hints in hint array List
		 * 
		 * hint at 0th position
		 */
		hintArray[0] = 0;
		hintList.add(hintArray[0]);
		if (answer_text.contains(" ")) {
			String parts[] = answer_text.split(" ");

			/**
			 * putting hints in hint array
			 * 
			 * hint at 1st position
			 */
			if (parts.length >= 2) {
				hintList.add(parts[0].length());
			}
		}

		if (answer_text.length() >= 2) {
			if (hintList.size() <= 1) {
				int h2 = getRandomNumber(answer_text.length());
				if (h2 <= 1)
					h2++;
				else
					h2--;
				hintList.add(h2);
			}
		}

		else {

		}

		/**************************** FOR SETTING TextView IN LAYOUT DYNAMICALLY *************************/

		AnswerLayout.removeAllViews();

		int xCords = 0;
		int yCords = 0;
		int width = 30;
		int height = 40;
		int widthInDensity;
		int heightInDensity = (int) (height * screenDensity + 0.5f);
		int leftMargin = (int) (43 * screenDensity + 0.5f);
		int rightMargin = (int) (43 * screenDensity + 0.5f);
		userDetail.h = question.getHeight();
		int rows = 0, cols = 0;
		right = (int) ((int) 2 * screenDensity + 0.5f);
		int top = (int) ((int) 5 * screenDensity + 0.5f);

		names = Arrays.asList(answer_text.split(" "));
		Log.i("**********names list==", "*******" + names);

		no_of_words = names.size();
	

		for (int i = 0; i < no_of_words; i++) {
			sp_words = names.get(i);
		

		}

		RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(
				(screen_width), RelativeLayout.LayoutParams.WRAP_CONTENT);
		layout_description.leftMargin = (int) (35 * screenDensity + 0.5f);
		layout_description.rightMargin = (int) (35 * screenDensity + 0.5f);
		layout_description.topMargin = ((int) (userDetail.h + 20 * screenDensity + 0.5f));

		AnswerLayout.setLayoutParams(layout_description);

	

		int tag = 0;
		
		for (int j = 0; j < names.size(); j++) {

			sp_words = names.get(j);
			cols = 0;

			tag = 0;

			for (int i = 0; i < sp_words.length(); i++) {

				int width_requried = screen_width - (leftMargin + rightMargin);
				int width_1_box = width_requried / sp_words.length();
				

				if (screenDensity < 3) {
					if (width_1_box >= 91) {
					
						widthInDensity = 65;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);

					}

					else if (width_1_box < 91 && width_1_box >= 78) {
					
						widthInDensity = 57;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);

					}

					else if (width_1_box < 78 && width_1_box >= 68) {
						widthInDensity = 50;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);
					

					}

					else if (width_1_box < 68 && width_1_box >= 60) {
						widthInDensity = 43;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);
						

					} else {
						widthInDensity = 40;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);
						

					}

				}// end of if

				/*************************** ELSE ******************************/
				else {
					if (width_1_box >= 205) {
					
						widthInDensity = 80;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);

					}

					else if (width_1_box < 205 && width_1_box >= 137) {
						
						widthInDensity = 78;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);

					}

					else if (width_1_box < 188 && width_1_box >= 164) {
						widthInDensity = 70;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);
						

					}

					else if (width_1_box < 164 && width_1_box >= 137) {
						widthInDensity = 76;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);
					

					}

					else if (width_1_box < 137 && width_1_box >= 117) {
						widthInDensity = 73;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);
						

					} else {
						widthInDensity = 54;
						s4 = (int) (xCords + widthInDensity * (2 - 1)
								+ ((2 - 1) * right) * screenDensity + 0.5f);
						

					}

				} //

				et = new TextView(this);
				cols++;
				int totalAcomdatedWidth = (int) (widthInDensity
						* sp_words.length() + leftMargin + rightMargin
						+ (20 * screenDensity + 0.5f) + ((((sp_words.length() - 1) * right)
						* screenDensity + 0.5f)));
				int leftSpaceInsideBox = screen_width - totalAcomdatedWidth;
				int marginToLeft = (int) (Math.floor(leftSpaceInsideBox / 2) - Math
						.floor(widthInDensity / 2));
			
				et.setX((marginToLeft + widthInDensity * (cols - 1)
						+ ((cols - 1) * right) * screenDensity + 0.5f));

				et.setY((yCords + rows * heightInDensity + (rows * top)
						* screenDensity + 0.5f));

			

				et.setSingleLine();
				et.setId(tag);

			
				if (tag == 0) {
					et.setBackgroundResource(R.drawable.rounded_edittext);
				} else {
					et.setBackgroundResource(R.drawable.rounded_edittext1);
				}

				et.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				et.setWidth(widthInDensity);

				et.setHeight(heightInDensity);

				InputFilter[] filters = { new InputFilter.LengthFilter(1),
						new InputFilter.AllCaps() };
				et.setFilters(filters);

				et.setGravity(Gravity.CENTER);

				et.setTypeface(Typeface.DEFAULT_BOLD);

				et.setFocusable(false);

				allEds.add(et);

				tag++;
				AnswerLayout.addView(et);

				AnswerLayout.bringToFront();

			}

			rows++;

		} // ending of for loop
		
		RelativeLayout.LayoutParams layout_description1 = new RelativeLayout.LayoutParams(
				screen_width, (int) ((int) (((rows + 1) * heightInDensity))
						+ (20 * screenDensity + 0.5f) + ((rows + 1) * top)));
		layout_description1.leftMargin = leftMargin;
		layout_description1.rightMargin = rightMargin;
		
		layout_description1.topMargin = ((int) (174 + 20 * screenDensity + 0.5f));
		
	

		AnswerLayout.setLayoutParams(layout_description1);
		AnswerLayout.setPadding((int) (20 * screenDensity + 0.5f),
				(int) (20 * screenDensity + 0.5f),
				(int) (10 * screenDensity + 0.5f), 5);

		answer_text = answer_text.replace(" ", "");

		setHintText();
		
		

	}

	/******************************* FOR DISPLAYING CHAR IN TextView **********************************/
	private void setHintText() {
		for (int i = 0; i < hintList.size(); i++) {

			TextView eht = allEds.get(hintList.get(i));

			String ans = answer_text.substring(hintList.get(i));
			eht.setText(ans);
			eht.setTextColor(Color.parseColor("#c22026"));

		}
		matchAnswer();
		isHintClicked = false;

	}

	private void removeQuestionFromList() {

		userDetail.QuestionsAnswerList.remove(userDetail.randQuesNo);
		

	}

	/**************** async class for getting question and answer *************************/

	public class getQuesAnsTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(Home.this, "Loading...",
					"Please Wait", true, false);

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebService.HOME_URL);

			String result = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair("mid",
						sharedpreferences.getString("MID", "")));

//				Log.e("URL==", "==" + WebService.HOME_URL + "mid" + "="
//						+ sharedpreferences.getString("MID", ""));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
				

					result = out.toString();
					
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in getting ques", "......" + e);

			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			dialog.dismiss();
			userDetail.QuestionsAnswerList.clear();

//			Log.i("RESULT==", "==" + result);

			String RESULT = result;

			if (RESULT == null) {

				final Dialog dialog1;
				dialog1 = new Dialog(Home.this);
				dialog1.setCancelable(false);
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.setContentView(R.layout.dilaog_ques);

				Button ok;
				ok = (Button) dialog1.findViewById(R.id.ok);
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(Home.this, Welcome.class);

						startActivity(i);

					}
				});
				dialog1.show();

			}

			try {

				JSONObject jsonObj = new JSONObject(result);

				webServiceResult = jsonObj.getString(WebService.STATUS);

				

				if (webServiceResult.equals("true")) {

					JSONArray dataArray = jsonObj.getJSONArray("data");

					for (int i = 0; i < dataArray.length(); i++) {

						Map<String, String> QuestionAnswerDetails = new HashMap<String, String>();

						JSONObject c = dataArray.getJSONObject(i);
						String question = c.getString("question");
						String answer1 = c.getString("answer1");
						String answer2 = c.getString("answer3");
						String answer3 = c.getString("answer2");
						String mid = c.getString("mid");
						QuestionAnswerDetails.put(userDetail.QUESTIONS,
								question);
						QuestionAnswerDetails.put(userDetail.ANSWERS1, answer1);
						QuestionAnswerDetails.put(userDetail.ANSWERS2, answer2);
						QuestionAnswerDetails.put(userDetail.ANSWERS3, answer3);

						QuestionAnswerDetails.put("mid", mid);

						userDetail.QuestionsAnswerList
								.add((HashMap) QuestionAnswerDetails);

					}

					showQuestion();

				}

				else {
					new AlertUtils(Home.this, AlertUtils.ERROR_OCCURED);

					return;
				}

			} catch (JSONException e) {
				e.printStackTrace();

				new AlertUtils(Home.this, AlertUtils.ERROR_OCCURED);
			}

			catch (Exception ae) {

			}

		}

	}

	private void publishStory() {

		final Dialog dialog;
		dialog = ProgressDialog.show(Home.this, "Updating status...",
				"Please Wait", true, false);
		dialog.show();
		Session session = Session.getActiveSession();

		

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);

				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Bundle postParams = new Bundle();
			postParams.putString("name", "Groovie Movie");
			postParams.putString("description", "Score for Groovie Movie is : "
					+ sharedpreferences.getInt("SCORE", 0));

			postParams.putString("link",
					"https://developers.facebook.com/android");
			postParams
					.putString("picture",
							"https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {

					try {

						JSONObject graphResponse = response.getGraphObject()
								.getInnerJSONObject();

					
						String postId = null;

						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.i("", "JSON error " + e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(Home.this.getApplicationContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					} else {
						dialog.dismiss();
						Toast.makeText(Home.this.getApplicationContext(),
								"Your Post has been shared", Toast.LENGTH_LONG)
								.show();

					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		try {
			uiHelper.onActivityResult(requestCode, resultCode, data,
					new FacebookDialog.Callback() {
						@Override
						public void onError(
								FacebookDialog.PendingCall pendingCall,
								Exception error, Bundle data) {
							Log.e("Activity", String.format("Error: %s",
									error.toString()));
						}

						@Override
						public void onComplete(
								FacebookDialog.PendingCall pendingCall,
								Bundle data) {
							Log.i("Activity", "Success!");
						}
					});
		} catch (Exception e) {

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		putQuesMidInSp();
		// removeQuestionFromList();
	
		try {
			uiHelper.onResume();
		} catch (Exception e) {

		}

		startTime = SystemClock.uptimeMillis();

		customHandler.postDelayed(updateTimerThread, 0);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try {
			uiHelper.onSaveInstanceState(outState);
		} catch (Exception e) {

		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// putQuesMidInSp();
		// removeQuestionFromList();
		
		try {
			uiHelper.onPause();
		} catch (Exception e) {

		}

		timeSwapBuff = sharedpreferences.getLong("timeSwapBuff", 0);
		timeInMilliseconds = sharedpreferences.getLong("timeInMilliseconds", 0);
		timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
		timeSwapBuff += timeInMilliseconds;
		Editor editor = sharedpreferences.edit();
		editor.putLong("timeSwapBuff", timeSwapBuff);
		editor.commit();

		customHandler.removeCallbacks(updateTimerThread);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// putQuesMidInSp();
		// removeQuestionFromList();
		try {
			uiHelper.onDestroy();
		} catch (Exception e) {

		}
	
		timeSwapBuff = sharedpreferences.getLong("timeSwapBuff", 0);
		timeInMilliseconds = sharedpreferences.getLong("timeInMilliseconds", 0);

		timeSwapBuff += timeInMilliseconds;
		Editor editor = sharedpreferences.edit();
		editor.putLong("timeSwapBuff", timeSwapBuff);
		editor.commit();

		customHandler.removeCallbacks(updateTimerThread);

	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	private void goFacebookLogin() {
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					
					publishStory();
				} else {

					

				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		putQuesMidInSp();
		// removeQuestionFromList();
		finish();
		return;
	}

	@Override
	public void onStop() {
		// putQuesMidInSp();
		// removeQuestionFromList();
		super.onStop();

	}

	/********** <><<><<><><><><><<><><SET KEYBOARD <><><><><><><><><><><><><><><> ******************/
	public void setKeyboard() {

		allEds1 = new ArrayList<TextView>();
		answers.removeAllViews();

		ANS = answer_text.replace(" ", "");
		ANS2 = answer_text.replace(" ", "");

		int mod = (ANS.length() % 5);
		int y = 5 - mod;
		int new_length = ANS.length() + y;

		for (int i = 0; i < y; i++) {
			String ANS1 = generateRandomChar();
			ANS = ANS + ANS1;

		}

		char[] ans = new char[ANS.length()];

		for (int j = 0; j < ANS.length(); j++) {
			ans[j] = ANS.charAt(j);
		}

		ShuffleArray(ans);

		int xCords = 0;
		int yCords = 0;
		int width = 40;
		int height = 40;
		int widthInDensity = (int) (width * screenDensity + 0.5f);
		int heightInDensity = (int) (height * screenDensity + 0.5f);

		int params_width = (int) (screen_width - (85 * screenDensity + 0.5f));

		int screenWIdth = (int) (screen_width * screenDensity + 0.5f);

		int totalColsInOneRow = 5;

		int layoutWidth = answers.getWidth();

		int totalRows = (int) Math.floor(ANS.length() / totalColsInOneRow) + 2;

		

		int rows = 0, cols = 0;
		int top = (int) (5 * screenDensity + 0.5f);

		if (screenDensity <= 2) {
			right = (int) ((3 * screenDensity + 0.5f));

		}

		else {
			right = (int) ((2 * screenDensity + 0.5f));

		}

		LayoutParams params = answers.getLayoutParams();

		params.height = (int) ((heightInDensity * totalRows + 2 * 26));

		int layoutHeight = answers.getHeight();

		int tag = 0;

		/**
		 * generating characters
		 */

		for (int i = 0; i < new_length; i++) {

			bt = new TextView(this);

			if (i % totalColsInOneRow == 0) {
				rows++;
				cols = 0;
			} else {

				cols++;
			}

			if (i == 0)
				rows = 0;

			bt.setX((xCords + width * cols + (cols * right)) * screenDensity
					+ 0.5f);
			bt.setY((yCords + (rows * height) + rows * top) * screenDensity
					+ 0.5f);

			bt.setId(tag);

			tag++;
			bt.setBackgroundResource(R.drawable.rounded_edittext);

			bt.setWidth(widthInDensity);

			bt.setHeight(heightInDensity);

			InputFilter[] filters = { new InputFilter.AllCaps() };
			bt.setFilters(filters);

			bt.setGravity(Gravity.CENTER);

			bt.setTypeface(Typeface.DEFAULT_BOLD);

			bt.setFocusable(false);

			bt.setText("" + ans[i]);
			bt.setTextColor(Color.parseColor("#000000"));

			allEds1.add(bt);

			answers.addView(bt);
			

			bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					selectedPosition = (Integer) v.getId();
					TextView bt1 = allEds1.get(selectedPosition);
					String n = bt1.getText().toString();

					char c = n.charAt(0);

					tag1 = searchForValidView(tag1);

					if (tag1 >= ANS2.length()) {

						return;
					}

					TextView et1 = allEds.get(tag1);

					if (c != Character.toUpperCase(answer_text.charAt(tag1))) {
						if (isSolvedPress) {
							showQuestion();
							return;
						}
						lives--;
						et1.setText("");

						if (lives == 0 && !isHintClicked) {

							new AlertUtils(Home.this, AlertUtils.NO_MORE_LIVES);

						}

						if (lives < 0) {

							String ans1 = et1.getText().toString();

							et1.setText("");

							new AlertUtils(Home.this, AlertUtils.NO_MORE_LIVES);

							return;
						}

						iv = new ImageView(Home.this);
						int w = et1.getWidth();
						int h = et1.getHeight();
						if (screenDensity >= 3) {
							iv.setX(et1.getX() - (w / 2));
						
						}

						else {
							iv.setX(et1.getX() - (10 * screenDensity + 0.5f));
						
						}
						iv.setY(et1.getY() - (10 * screenDensity + 0.5f));

						et1.getHeight();
					

						iv.setImageResource(R.drawable.cross1);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								w / 2, h / 2);
						iv.setLayoutParams(layoutParams);
						iv.setScaleType(ImageView.ScaleType.FIT_XY);
						AnswerLayout.addView(iv);

						ObjectAnimator fadeOut = ObjectAnimator.ofFloat(iv,
								"alpha", 1f, 0f);
						fadeOut.setDuration(1000);

						ObjectAnimator fadeIn = ObjectAnimator.ofFloat(iv,
								"alpha", 0f, 1f);
						fadeIn.setDuration(1000);

						final AnimatorSet mAnimationSet = new AnimatorSet();

						mAnimationSet.play(fadeOut).after(fadeIn);

						mAnimationSet
								.addListener(new AnimatorListenerAdapter() {
									@Override
									public void onAnimationEnd(
											Animator animation) {
										super.onAnimationEnd(animation);
										AnswerLayout.removeView(iv);
									}
								});
						mAnimationSet.start();

					} // if closed

					else if (c == Character.toUpperCase(answer_text
							.charAt(tag1)) && lives <= 0 && !isHintClicked) {

						et1.setText("");
						new AlertUtils(Home.this, AlertUtils.NO_MORE_LIVES);

					} else {
						et1.setText(n);
						et1.setTextColor(Color.parseColor("#000000"));
						AnswerLayout.removeView(iv);
						bt1.setVisibility(View.INVISIBLE);
						tag1++;
						cursor++;
						cursor = searchForValidView(cursor);
						
						if(cursor< allEds.size()){
							blinkingAnimation(cursor);
						}
						
					}
					matchAnswer();
					
				}
			});

		}
		
		


	}

	

	/***********************************************blinking animation **********************************************************************/
		protected void blinkingAnimation(int tag1) {
			// TODO Auto-generated method stub
			
		
		iv = new ImageView(Home.this);
		
		TextView et1 = allEds.get(tag1);
		int w = et1.getWidth();
		int h = et1.getHeight();
		if (screenDensity >= 3) {
			iv.setX(et1.getX() - (w / 2));
		
		}

		else {
			iv.setX(et1.getX() - (10 * screenDensity + 0.5f));
		
		}
		iv.setY(et1.getY() - (10 * screenDensity + 0.5f));

		et1.getHeight();
	

		iv.setImageResource(R.drawable.line);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				w / 2, h / 2);
		iv.setLayoutParams(layoutParams);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		AnswerLayout.addView(iv);

		final ObjectAnimator fadeOut = ObjectAnimator.ofFloat(iv,
				"alpha", 1f, 0f);
		fadeOut.setDuration(500);
		

		final ObjectAnimator fadeIn = ObjectAnimator.ofFloat(iv,
				"alpha", 0f, 1f);
		fadeIn.setDuration(500);
		

		final AnimatorSet mAnimationSet = new AnimatorSet();

		mAnimationSet.play(fadeOut).after(fadeIn);
		
		
		

		mAnimationSet
				.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(
							Animator animation) {
						super.onAnimationEnd(animation);
						try {
							mAnimationSet.removeAllListeners();
						mAnimationSet.start();
						}
						
						catch(Exception e){
							Log.e("exception==",""+e);
						}
					}
				});
		mAnimationSet.start();
		
		}
		
/***************************************************************************************************************************************/

	private int searchForValidView(int index) {

		int validV = index;

		if (hintList.contains(validV)) {
			validV++;

			return searchForValidView(validV);
		}

		return validV;
	}

	private void ShuffleArray(char[] ans) {
		int index;
		char temp;
		Random random = new Random();
		for (int i = ans.length - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			temp = ans[index];
			ans[index] = ans[i];
			ans[i] = temp;
		}

	}

	private String generateRandomChar() {
		
		String[] chars = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z" };
		String randomChar = (chars[(int) (Math.random() * 10)]);
		return randomChar;
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub\
		hint.setEnabled(true);
		skip.setEnabled(true);
		solve.setEnabled(true);
		fb.setEnabled(true);
		twitter.setEnabled(true);
		if (arg0 == animZoomIn) {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					imageView1.bringToFront();
					
					imageView1.startAnimation(animZoomOut);
				}
			}, 1000L);

			
			imageView1.setVisibility(View.INVISIBLE);
			
		}
		else 
			if (arg0 == animZoomOut) {
				onResume();
				cursor = 0;
				cursor = searchForValidView(cursor);
				
				blinkingAnimation(cursor);
			//	cursor++;
				
				

		}

	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

		

	}
	
	public class CounterClass extends CountDownTimer {  
        public CounterClass(long millisInFuture, long countDownInterval) {  
             super(millisInFuture, countDownInterval);  
        }  
        @Override  
       public void onFinish() {  
        	onResume();
         inner_timer.setText("00:00:00");  
         solve.setEnabled(true);
         inner_timer.setVisibility(View.INVISIBLE);
         
         removeQuestionFromList();
			ansNo = 0;
			
			showQuestion();
       }  
       
        @Override  
        public void onTick(long millisUntilFinished) {  
              long millis = millisUntilFinished;  
               String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),  
                   TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),  
                   TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));  
               System.out.println(hms);  
               inner_timer.setText(hms);  
        }  
   }  
}  





