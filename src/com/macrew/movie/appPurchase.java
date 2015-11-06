package com.macrew.movie;

import java.util.Arrays;
import java.util.List;

import com.android.vending.billing.IInAppBillingService;

import com.macrew.inappbilling.util.IabHelper;
import com.macrew.inappbilling.util.IabResult;
import com.macrew.inappbilling.util.Inventory;
import com.macrew.inappbilling.util.Purchase;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.macrew.inappbilling.util.*;


public class appPurchase extends Activity {
	
	IInAppBillingService mService;
	IabHelper mHelper;
	
	Button back , buy1 , buy2 , buy3;
	
	private static final String TAG = "com.macrew.inappbilling";
	static final String ITEM_SKU1 = "com.groovimoviequotes.groovimoviequotes.mbucks250";
	static final String ITEM_SKU2 = "com.groovimoviequotes.groovimoviequotes.mbucks1250";
	static final String ITEM_SKU3 = "com.groovimoviequotes.groovimoviequotes.mbucks5000";
	//static final String ITEM_SKU3="android.test.purchased";
	
	
	String inHomeScreen;
	
	SharedPreferences sharedpreferences;

	
	
	/*String[] items = new String[] {"com.groovimoviequotes.groovimoviequotes.mbucks250","com.groovimoviequotes.groovimoviequotes.mbucks1250",
	"com.groovimoviequotes.groovimoviequotes.mbucks5000"};*/
	
	String[] items = new String[] {"com.groovimoviequotes.groovimoviequotes.mbucks250","com.groovimoviequotes.groovimoviequotes.mbucks1250",
	"com.groovimoviequotes.groovimoviequotes.mbucks5000"};
	



	List<String> item_list =(List) Arrays.asList(items);
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {    
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		
		
	    setContentView(R.layout.purchase);   
	    sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

	   
	    back = (Button) findViewById(R.id.back);
	    buy1 = (Button) findViewById(R.id.buy1);
		buy2 = (Button) findViewById(R.id.buy2);
		buy3 = (Button) findViewById(R.id.buy3);
		
		sharedpreferences = PreferenceManager.getDefaultSharedPreferences(appPurchase.this);
		
		inHomeScreen = sharedpreferences.getString("inHomeScreen", "");
	    
	    back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(appPurchase.this , Welcome.class);
				startActivity(i);
			}
		});
	    
	    String base64EncodedPublicKey = 
	            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoKhYC8VYtK1ecnI" +
	            "w9UUrM5CYuHoTyzM+piAeXLaBR4O6gdL4TdbWAyrZLe675AFSvK2Wbj7qymZ0" +
	            "Ao6W3jYkHYuUwAzGFZQTPxKcW64KSlChEZQLpdK68nNw+QfG0hSh+UQBl5YQcyj" +
	            "HHqwxMaTCqycM6cmCSNwNORI7r4m0O2nU00GNQFq+urJml/QB1NO0PknTw4/L" +
	            "BAiSL8y9ZfGAVRFgNcRvvVMNCkkhWw7H/JOKvVJKAAlNOBkNpWt3fAsMeAPfsNOr08" +
	            "iWgvJRzPs9yJQZnp5Apgs8WFgHtYUH9DkGXsrt6uZe/+RpVBldVzBMiC9J2q1kEOKuEmsHxmHUzwIDAQAB";
		
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		
		mHelper.startSetup(new 
				IabHelper.OnIabSetupFinishedListener() {
	        	   	 public void onIabSetupFinished(IabResult result) 
				 {
	        	           if (!result.isSuccess()) {
	        	             Log.d(TAG,"======"+ "In-app Billing setup failed: " + 
						result);
	        	           } else {             
	        	      	     Log.d(TAG,"======"+  "In-app Billing is set up OK");
	        	      	     
	        	      	   mHelper.queryInventoryAsync(true, item_list,
	       						mGotInventoryListenerGetNextList);
			           }
	        	         }
	        	});
		
		buy1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				buyClick(v);
			}
		});
		
		buy2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buyClick2(v);
				
			}
		});

		buy3.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				buyClick3(v);
		
			}
		});
	   
	}
	
	
	
	
	protected void buyClick(View view) {
		 mHelper.launchPurchaseFlow(this, ITEM_SKU1, 10001,   
   			   mPurchaseFinishedListener, "mypurchasetoken");
		
	}
	protected void buyClick2(View view) {
		 mHelper.launchPurchaseFlow(this, ITEM_SKU2, 10002,   
  			   mPurchaseFinishedListener, "mypurchasetoken");
		
	}
	protected void buyClick3(View view) {
		 mHelper.launchPurchaseFlow(this, ITEM_SKU3, 10003,   
  			   mPurchaseFinishedListener, "mypurchasetoken");
		
	}
	
	// Callback for when a purchase is finished
		IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
				Log.d(TAG, "Purchase finished: " + result + ", purchase: "
						+ purchase);
				 Editor editor = sharedpreferences.edit();
				    int SCORE = sharedpreferences.getInt("SCORE", 0);
				    int NEW_SCORE = 0;
				if (result.isFailure()) {
					Log.i("purchase","failure");
					
					return;
				} 
				 else if (purchase.getSku().equals(ITEM_SKU1)) {
					 
					 //consumeItem();
					 Log.i("purchase==","ITEM_SKU1=="+purchase.getSku());
					 
					  NEW_SCORE = SCORE+250;
						
				
				}
				 else if (purchase.getSku().equals(ITEM_SKU2)) {
					 
					 Log.i("purchase==","ITEM_SKU2=="+purchase.getSku());
					  NEW_SCORE = SCORE+1250;
					   
						
					}
				 else if (purchase.getSku().equals(ITEM_SKU3)) {
					 Log.i("purchase==","ITEM_SKU3=="+purchase.getSku());
					   
					 NEW_SCORE = SCORE+5000;
					}
				else {

				}
				editor.putInt("SCORE", NEW_SCORE);
				editor.commit();
				Log.d(TAG, "Purchase successful.");

			}
		};

		@Override
		protected void onActivityResult(int requestCode, int resultCode, 
		     Intent data) 
		{
		      if (!mHelper.handleActivityResult(requestCode, 
		              resultCode, data)) {     
		    	super.onActivityResult(requestCode, resultCode, data);
		    	Log.i("0n Activity result==","requestCode=="+requestCode+"resultCode=="+
		    			resultCode+"data=="+data);
		      }
		      
		      else {
		          Log.d(TAG, "onActivityResult handled by IABUtil.");
		      }
		}
		
		IabHelper.QueryInventoryFinishedListener mGotInventoryListenerGetNextList = new IabHelper.QueryInventoryFinishedListener() {
			public void onQueryInventoryFinished(IabResult result,
					Inventory inventory) {

				Log.d(TAG, "Query inventory finished. 1");

				if (result.isFailure()) {
					Log.d(TAG, "Query inventory finished. 1=faied");
					return;
				}
				
				Log.i("inventory.getAllProducts()==","=="+inventory.getSkuDetails(ITEM_SKU1));
				
				
				

			}
				
		};
}