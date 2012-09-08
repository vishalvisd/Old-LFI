package com.visd.Locepeop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Arrowhandler extends Activity implements SensorEventListener, Runnable {
	//task defination of arrow handler: it will get the user location from the Sqlite user table and 
	//extracts the intent extras from the intent received and used the received regid to extract
	//the location values from the sqlite knowns table, at the same time it will also make a http 
	//request to the server's servlet that handles known location update request, the servlet is sended the 
	//known regno. along with the user reg no. , the servlet task is to make a c2dm request according the 
	//regno known.
	//One important aspect of this activity is that, it is to listen changes to sqlite. As when changes to 
	//sqlite occrs
	
	public static Arrowhandler aThis = null;
	Matrix mtx = new Matrix();
	ImageView img;
	Bitmap bmp;
	int w,h;
	public float d = 0;
	public float dist;
	public int globalstop = 0;
	public String reg;
	float oldval = 0,currentrot = 0, rotate = 0,senserotat;
	String nme;
	private SensorManager sensorManager = null;
	float mslati = (float) 0.0, mslongi=(float) 0.0, oslati = (float) 0.0, oslongi = (float) 0.0;
	String extreg = "";
	double angle, degree;
	TextView t,t2;
	Thread thr, thr2,thr3,thr4;
	Button b7 = null;
	boolean runned = false, runned2 = false, runned3 = false, runned4 = false, runned5=false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);		
		setContentView(R.layout.mmmain);
		
		try
		{
			Button b2 = (Button) findViewById(R.id.button1);
			b2.setBackgroundResource(R.drawable.btn_yelw);
			b2.setText("Will become GREEN in < 70 Sec. If not then there is problem in ur Friend's network. TAP/PRESS HERE TO FORCE REFRESH");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		TextView warning = (TextView) findViewById(R.id.speciali);
		warning.setSelected(true);
		t = (TextView) findViewById(R.id.textView1);		
		String[] arrs = getIntent().getExtras().getStringArray("ClickedRegNo");		
		reg = arrs[0];
		nme = arrs[1];
		Log.d("G22","arrowhandler received : "+reg+" ; and name = "+nme);
		//getting location value of both party
		Button mapb = (Button) findViewById(R.id.bmapdet);
		mapb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent mapi = new Intent(Arrowhandler.this,Detailmap.class);
				mapi.putExtra("Reg", Integer.parseInt(reg));
				startActivity(mapi);
				
			}
		});
		
		Button mapab = (Button) findViewById(R.id.ahsain);
		mapab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent mapi = new Intent(Arrowhandler.this,Detailmap.class);
				mapi.putExtra("Reg", "");
				startActivity(mapi);
				
			}
		});
		
		Button msg = (Button) findViewById(R.id.ahref);
		msg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intee = new Intent(Arrowhandler.this,Sendmsg.class);
				String tor[] = {reg,nme};
				intee.putExtra("toreg", tor);
				startActivity(intee);	
				
			}
		});
		
		
		
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
		Cursor c = db.rawQuery("SELECT * FROM User;", null); 
		c.moveToFirst();
		Log.d("G22","Tr1");
		extreg = c.getString(c.getColumnIndex("RegNo"));
		mslati = c.getFloat(c.getColumnIndex("Lat"));
		mslongi = c.getFloat(c.getColumnIndex("Long"));
		Log.d("G22","mslati : "+mslati);
		Log.d("G22","mslongi : "+mslongi);
		Log.d("G22","Tr2");
		c.close();
		
		Cursor c2 = db.rawQuery("SELECT * FROM Knowns;", null);
		if(c2.getCount() < 1)
		{
			c2.close();
			Toast.makeText(this, "Oops!", 10000);
			Intent inten = new Intent(this,main.class);
			startActivity(inten);
			finish();
		}
		else
		{
		c2.moveToFirst();
		do
		{
			
			String rgno = c2.getString(c2.getColumnIndex("RegNo"));
			if (rgno.equals(reg))
			{
				Log.d("G22","Tr3");
				oslati = c2.getFloat(c2.getColumnIndex("Lat"));
				oslongi = c2.getFloat(c2.getColumnIndex("Long"));
				Log.d("G22","Oslati : "+oslati);
				Log.d("G22","Oslongi : "+oslongi);
				//getAddress(Double.parseDouble(""+oslati), Double.parseDouble(""+oslongi));
				
				runned4 = true;
				final String throslati = ""+oslati;
    			final String throslongi = ""+oslongi;
    			Log.d("G30","Testing for thread : before 1");
    			
    			/*newpagal n = new newpagal(throslati, throslongi, Arrowhandler.this);
    			n.start();*/
    			thr4 = new Thread (new Runnable() {
					
					@Override
					public void run() {
						//getAddress(Double.parseDouble(""+throslati), Double.parseDouble(""+throslongi));
						
						Log.d("G30","Testing for thread : after 11");
						String arr[] = {""+throslati,""+throslongi};
						Intent i = new Intent(Arrowhandler.this, Getaddress.class);
				        i.putExtra("Loci", arr);	        
				        startService(i);
				        Log.d("G30","Testing for thread : after 12");
					}
				});
    			thr4.start();
				
				break;
			}
		}while (c2.moveToNext());
		c2.close();
		}
		//finished getting location value of both party
		Log.d("G22","Tr4");
		//finding distance and direction
		dist = distFrom(oslati,oslongi,mslati,mslongi);
   	  	dist = (float) (dist * 1.60934);
   	  	angle = Math.atan2(mslongi - oslongi, mslati - oslati);
   	  	degree = angle*57.295779;
   	  	Log.d("G22","Tr5");
   	  	d =  (float) degree;
	   	if (d>0)
	    {
	     	d = d+180;
	    }
	    else if (d<0)
	    {
	     	d = 180 - (Math.abs(d));
	    }   
	   	if (dist < 1)
	   	{
	   		float m = (float) 1000.0;
			float distt = (float) (dist*m);
			Log.d("G22D","dist = "+dist+"; and distt ="+distt);
	   		t.setText("Distance : "+distt+" meters (~), and direction follow the arrow below...");
	   	}
	   	else
	   	{
	   		t.setText("Distance : "+dist+" km (~), and direction follow the arrow below...");
	   	}
        Log.d("G22","Tr6");
	   	img=(ImageView)findViewById(R.id.imageView1);
    	bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bluearrow);
    	// Getting width & height of the given image.
    	w = bmp.getWidth();
    	h = bmp.getHeight();
    	
   	  	
    	Log.d("G22","Tr7");
		//requesting server to get update location of the knowns
    	if (cIC() == false)
		{
			Log.d("G22","Interne not there");
			Toast.makeText(this, "Internet is not there !!", 100000).show();
		}
    	
		runned2 = true;
		thr2 = new Thread(this, "ddd");
		thr2.run();
    	db.close();
		
	}
	public float distFrom(float lat1, float lng1, float lat2, float lng2) {
		Log.d("SH","EF");
	    double er = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = er * c;
	    Log.d("G22","Tr9");
	    //int meterConversion = 1609;

	    return new Float(dist).floatValue();
	    }
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		synchronized (this) {
			//Log.d("G22","Tr007");
			if ((oslati == 0.0 && oslongi == 0.0) || ((""+oslati).equals("") && (""+oslongi).equals("")))
			{
				t.setText("  You aren't yet Approved by the user, So you won't get location, if it comes again and again still after the user Approves you then please remove the user, then add again and retry. ");   
			}
			else
			{
				final Button b = (Button) findViewById(R.id.button1);
				b7 =  (Button) findViewById(R.id.button1);;
				b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							Toast.makeText(Arrowhandler.this, "WILL REFRESH in < 70 seconds", 10000).show();
							 b.setBackgroundResource(R.drawable.btn_yelw);
							 String btext = b.getText().toString();
							 if (btext.substring(0, 40).equals("Got a new Current Location from the user"))
							 {
								 b.setText(btext.substring(40));
							 }
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						b.setEnabled(false);
						if (!iSR())
		            	{
		            		Context con = Arrowhandler.this;
		            		Log.d("G29","Main about to start LUSER x1");
			            	Intent inte = new Intent(Arrowhandler.this,Luser.class);
			            	Log.d("G29","Main about to start LUSERx2");
			                inte.putExtra("externalregid", "xxx");	
			                Log.d("G29","Main about to start LUSER x3");
			                con.startService(inte);
			                
			                SQLiteDatabase dbs = openOrCreateDatabase("GiraffeDBv2", 0, null);
			    			ContentValues cv = new ContentValues();
			    			String fix = "fix";
			    			cv.put("Misc2", "ok");
			    			dbs.update("States", cv, "Misc1 = '"+fix+"'", null);
			    			dbs.close();
		            	}
						runned = true;
						thr = new Thread (Arrowhandler.this, "Arrowhandlercomm");
						thr.start();
						
				    						
					}
				});
				if (globalstop == 1)
				{
					t.setText("This user has switched his application mode to offline... so, you can't get his/her location");
				}
				else
				{
						
					if (dist < 1)
				   	{
						float m = (float) 1000.0;
						float distt = (float) (dist*m);
						Log.d("G22D","dist = "+dist+"; and distt ="+distt);
				   		t.setText("Distance : "+distt+" meters (~), and direction follow the arrow below...");
				   	}
				   	else
				   	{
				   		t.setText("Distance : "+dist+" km (~), and direction follow the arrow below...");
				   	}
				}
				//t.setText("Distance : "+dist+" km, and direction follow the arrow below...");
			}
		    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		   
		    }
		    
		    if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
		    	
		    	//Log.d("G22","Tr10");
		    	
		    	senserotat = d-sensorEvent.values[0];
		    	if (senserotat < 0)
		    	{
		    		senserotat = 360 - (Math.abs(senserotat));
		    	}
		    		rotate = (360-currentrot)+senserotat;
		    		if (rotate>360)
		    		{
		    			rotate = rotate-360;
		    		}
		    		mtx.postRotate(rotate);
		    		// Rotating Bitmap
		    		Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
		    		BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
		    		//Log.d("G22","Tr11");
		    		img.setImageDrawable(bmd);
		    		currentrot = senserotat;
		    		//Log.d("TTTT", Float.toString(sensorEvent.values[0]));
		    	
		  
		    }
		    }
		
	}
	 private boolean iSR() {

	        String sClassName;
	        Log.d("G29","isr entreed ");
	        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) 
	        {
	            sClassName = service.service.getClassName();
	            Log.d("G29","isr entreed 2");
	            if (sClassName.contains("com.visd.Locepeop.Luser"))
	            {
	            	Log.d("G29","isr found luser in mian ");
	                  return true;
	                  
	            }
	        }
	        Log.d("G29","isr not found luser in mian ");
	        return false;
	    }
	@Override
	protected void onPause() {
		super.onPause();
		aThis = null;
	}
	@Override
    protected void onResume() {
		 aThis = this;
    
    // Register this class as a listener for the accelerometer sensor
    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    // ...and the orientation sensor
    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    
    super.onResume();
    }
  
    @Override
    protected void onStop() {
    // Unregister the listener
    if (runned)
    {
    	try {
			thr.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    if (runned2)
    {
    	try {
			thr2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    if (runned3)
    {
    	try {
			thr3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    if (runned4)
    {
    	try {
			thr4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    sensorManager.unregisterListener(this);
    super.onStop();
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
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuc, menu);
		return true;
	}
    @Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		item.setEnabled(false);
		if(item.getItemId() == R.id.item6)
		{
			Intent intee;
			Log.d("G22","Menu Add user from owner is clicket");
			intee = new Intent(Arrowhandler.this,Sendmsg.class);
			String tor[] = {reg,nme};
			intee.putExtra("toreg", tor);
			startActivity(intee);	
			item.setEnabled(true);
			
		}
		return true;
	}
	@Override
	public void run() {
		
		
		String arr[] = {reg,extreg, ""+mslati, ""+mslongi};
		Intent i = new Intent (Arrowhandler.this, ServiceforArrowhandler.class);
		i.putExtra("DET", arr);
		startService(i);
		   	
		
	}


	public static Thread performOnBackgroundThread(final Runnable runnable) {
	    final Thread t = new Thread() {
	        @Override
	        public void run() {
	            try {
	                runnable.run();
	            } finally {

	            }
	        }
	    };
	    t.start();
	    return t;
	}
}




class newpagal extends Thread
{
	String throslati, throslongi;
	Context con;
	public newpagal(String a, String b, Context c) {
		throslati = a;
		throslongi = b;
		con = c;
	}
	@Override
	public void run() {
		Log.d("G30", "Afeter");
		String arr[] = {""+throslati,""+throslongi};
		Intent i = new Intent(con,Getaddress.class);
        i.putExtra("Loci", arr);	        
        con.startService(i);
	}
	
}