package com.visd.Locepeop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Sendc2dmreg extends Service {

	private PendingIntent pendingIntent;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		Log.d("G22","entered sendc2dmreg!");
		//Storing C2DM registration ID on Android Mobile Device Database
		if (Registration.mThis != null) 
    	{
			Log.d("G22","Temp please delete 1");
			  ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Saving..." );
    	}
		Log.d("G22","Temp please delete 2");
		String s = intent.getStringExtra("regId");		
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);  
		ContentValues values = new ContentValues();
		ContentValues values2 = new ContentValues();
		values.put("C2dm", s);		
		Log.d("G22","Temp please delete 3");
		Cursor c = db.rawQuery("SELECT * FROM User;", null); 
		Log.d("G22","Temp please delete 4");
		if (c.getCount() == 0)//hence user table has not been created, so the call is not from regsitration but from recover
		{
			Log.d("G22","Temp please delete 4.5");
			db.execSQL("INSERT INTO User VALUES ('','','','','','','','"+s+"','','','');" );	
			db.close();
			stopService(intent);
		}
		else{
		Log.d("G22","Temp please delete 5");
		c.moveToFirst();
		String un = c.getString(c.getColumnIndex("Name"));	
		db.update("User", values, "Name ='"+un+"'", null);		
        c.close();
        if (Registration.mThis != null) 
    	{
			  ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Saved, Waiting to send data to server !" );
    	}
        //storing complete
        Cursor userinfoc = db.rawQuery("SELECT * FROM User;", null); 
        userinfoc.moveToFirst();
        
        	String username = userinfoc.getString(userinfoc.getColumnIndex("Name"));
        	String usernum = userinfoc.getString(userinfoc.getColumnIndex("Pno"));
        	String userc2dm = userinfoc.getString(userinfoc.getColumnIndex("C2dm"));
        	String userpass = userinfoc.getString(userinfoc.getColumnIndex("Pass"));
        	float userlat = userinfoc.getFloat(userinfoc.getColumnIndex("Lat"));
        	float userlong = userinfoc.getFloat(userinfoc.getColumnIndex("Long"));
        	Log.d("G22","Retrieved Data : name : "+username+" num: "+usernum+" C2dm: " +userc2dm + " Password : "+userpass);
        
        userinfoc.close();
       
        Log.d("G22","appregid retreived after storing to database is : " + userc2dm);
        db.close();
        SQLiteDatabase db3 = openOrCreateDatabase("GiraffeDBv2", 0, null);  
        Cursor c3 = db3.rawQuery("SELECT * FROM User;", null); 
		c3.moveToFirst();
		String c2did = c3.getString(c3.getColumnIndex("C2dm"));	
		Log.d("G22","C2dm id retreived from visd :"+c2did);
		c3.close();
		db3.close();
        if (Registration.mThis != null) 
    	{
			  ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Ok with Registration Data " );
    	}
        if (cIC() == false)
		{
			Log.d("G22","Interne not there");
			Toast.makeText(this, "Internet is not there !!", 100000).show();
		}
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Firsthandle");    	
    	try {
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
    		// Get the deviceID    		
    		Log.d("G22","Usernumber which is sended to server: "+usernum);
    		nameValuePairs.add(new BasicNameValuePair("regname", username));
    		Log.d("G22","S   :"+username);
    		nameValuePairs.add(new BasicNameValuePair("regnum", usernum));
    		Log.d("G22","S   :"+usernum);
    		nameValuePairs.add(new BasicNameValuePair("regc2dm", userc2dm));
    		Log.d("G22","S   :"+userc2dm);
    		nameValuePairs.add(new BasicNameValuePair("reglat", ""+userlat));
    		Log.d("G22","S   :"+userlat);
    		nameValuePairs.add(new BasicNameValuePair("reglong", ""+userlong));
    		Log.d("G22","S   :"+userlong);
    		nameValuePairs.add(new BasicNameValuePair("pass", ""+userpass));
    		Log.d("G22","S   :"+userpass);
    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
    		HttpResponse response = client.execute(post);    		
    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    		String line = "";
    		Log.d("G22","Passedi3");
    		while ((line = rd.readLine()) != null) {
    			
    			Log.d("G22", "Reading: " + line);
    			int rcodeindex = line.indexOf("result");
    			Log.d("G22",""+rcodeindex);
    			if (rcodeindex != -1)
    			{
    				Log.d("G22","entered indont");
	    			String rcode =  line.substring(rcodeindex+6, rcodeindex+11);
	    			Log.d("G22","Rcode :" +rcode);
	    			if (rcode.equals("13911"))
	    			{
	    				 if (Registration.mThis != null) 
	    		    	  {
	    					    ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Number Unaccepted." );
	    		    	  }
	    			}
	    			else if (rcode.equals("13912") || rcode.equals("13913")|| rcode.equals("13914"))
	    			{
	    				Log.d("G22","Entered 13914 or 13912 or 13913");
	    				int regcodeindex = line.indexOf("REGID");
	    				String regcode = line.substring(regcodeindex+5);
	    				Log.d("G22","Regcode : "+regcode);
		    			SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);  
		    			values2.put("RegNo",regcode);
		    			db2.update("User", values2, "Name ='"+un+"'", null);	
		    			Cursor cc2 = db2.rawQuery("SELECT * FROM User", null);
		    			cc2.moveToFirst();    	   
		    			do
		    			{
		    				Log.d("G22","Details as retreived from Database after getting registration id from server (This is the present user table) :-");
		    				Log.d("G22", "Name :"+cc2.getString(cc2.getColumnIndex("Name")));
		    				Log.d("G22", "Phone Num :"+cc2.getString(cc2.getColumnIndex("Pno")));
		    				Log.d("G22", "C2dm id :"+cc2.getString(cc2.getColumnIndex("C2dm")));
		    				Log.d("G22", "Lat :"+cc2.getFloat(cc2.getColumnIndex("Lat")));
		    				Log.d("G22", "Long :"+cc2.getFloat(cc2.getColumnIndex("Long")));
		    				Log.d("G22","Reg Id :"+cc2.getString(cc2.getColumnIndex("RegNo")));
		    				Log.d("G22", "Tlu Value :"+cc2.getString(cc2.getColumnIndex("Tlu")));
		    				Log.d("G22","Mis1 :"+cc2.getString(cc2.getColumnIndex("Mis1")));
		    				Log.d("G22","Mis2 :"+cc2.getString(cc2.getColumnIndex("Mis2")));
		    				Log.d("G22","Misc value :"+cc2.getString(cc2.getColumnIndex("misc")));
		    				Log.d("G22","pass :"+cc2.getString(cc2.getColumnIndex("Pass")));
		    			} while (cc2.moveToNext());	
		    			cc2.close();
		    			Cursor cc3 = db2.rawQuery("SELECT * FROM Knowns", null);
		    			if(cc3.getCount() > 0)
		    			{
		    				cc3.moveToFirst();		    			
			    			Log.d("G22","Knowns table details :-");
			    			do
			    			{
			    				Log.d("G22","New Known Deatail:");
			    				Log.d("G22", "Regno :"+cc3.getString(cc3.getColumnIndex("RegNo")));
			    				Log.d("G22", "Name :"+cc3.getString(cc3.getColumnIndex("Name")));
			    				Log.d("G22", "Lat value :"+cc3.getString(cc3.getColumnIndex("Lat")));
			    				Log.d("G22", "Long value :"+cc3.getString(cc3.getColumnIndex("Long")));
			    				Log.d("G22", "Tlu value :"+cc3.getString(cc3.getColumnIndex("Tlu")));
			    				Log.d("G22", "Phone Number :"+cc3.getString(cc3.getColumnIndex("Pno")));
			    			}while (cc3.moveToNext());
		    			}
		    			cc3.close();
		    			db2.close();
		    			//PendingIntent pendingIntent;
		    			Intent myIntent = new Intent(Sendc2dmreg.this, Newservice.class);
		    	    	pendingIntent = PendingIntent.getService(Sendc2dmreg.this, 0, myIntent, 0);
		               AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		               Calendar calendar = Calendar.getInstance();
		               calendar.setTimeInMillis(System.currentTimeMillis());	               
		               calendar.add(Calendar.SECOND, 33);
		               alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
		               Log.d("G33","Alarm set from sendcreg");
		                  
		    			SQLiteDatabase dbb2 = openOrCreateDatabase("GiraffeDBv2", 0, null); 
		    			dbb2.execSQL("INSERT INTO States VALUES ('fix','','','"+ new Date() +"');" );
		    			dbb2.close();
		    			
		               Log.d("G22","Alarm set from sendcreg");
		               Toast.makeText(this, "Congrats and Welcome", 10000).show();
		    			if (Registration.mThis != null) 
		    	    	{
		    				  ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Your Device is Registed Successfully !!" );		    				  
		    	    	}
		    			try {
							Thread.sleep(3000);
							if (Registration.mThis!=null)
							{
								Registration.mThis.finish();
							}
							Intent MakegroupIntent = new Intent(this, main.class);
							MakegroupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			            	startActivity(MakegroupIntent);  
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			 
	    			}
    			}
    			
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    		Log.d("G22", "Entered Exception");	
    	}
    	SQLiteDatabase db4 = openOrCreateDatabase("GiraffeDBv2", 0, null);  
        Cursor c4 = db4.rawQuery("SELECT * FROM User;", null); 
		c4.moveToFirst();
		String c2didd = c4.getString(c4.getColumnIndex("C2dm"));	
		Log.d("G22","C2dm id retreived from visd 2last:"+c2didd);
		c4.close();
		db4.close();
		stopService(intent);
		}
        return super.onStartCommand(intent, flags, startId);
	}

@Override
public void onDestroy() {
	Log.d("G22","Sendc2dmreg service is destroyed");
	Log.d("G22","Temp please delete 6");
	super.onDestroy();
}
public boolean cIC() {
	Log.d("G22","Entered cic");
    final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
    if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
    	Log.d("G22","Returned true");  
    	return true;
    } else {              
          Log.d("G22","Returned false");  
        return false;
    }
}
}
