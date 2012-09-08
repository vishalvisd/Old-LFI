package com.visd.Locepeop;




import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;






import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;

import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;

import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Luser extends Service implements Runnable{

	public static Luser luserThis = null;
	LocationManager mlocManager;
	LocationListener mlocListener;
	private static final int ONE_MINUTES = 1000 * 60 * 1;
	Location lastnk = null;
	Location lastgk = null;
	Location takenlocation = null;
	boolean isgpson = true;
	int chkcount = 0;
	double lati,longi;
	float globallati = (float)0.0,globallongi=(float)0.0;
	int flagcount = 0;
	String externalregid = "";
	
	Intent inten;
	boolean flag1 = false;
	boolean flag2 = false;
	boolean flag3 = false;
	boolean flag4 = false;
	Date d1 = null, d2;
	long tluage = 100000;
	int ncount = 0;
	int gcount = 0;
	
	String eern, eelati, eelongi, eeaccura, eedat, eeexternalregid;
	
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	@Override
	public void onDestroy() {
		Log.d("G28","Luser entered on destroy");
		Log.d("G25","Luser entered on destroy");
		try
		{
			//TextView mvin = (TextView) Mainwindow.mainwindowThis.findViewById(R.id.mvindicator);
			//mvin.setVisibility(View.INVISIBLE);
			
		}
		catch(Exception e)
		{
			Log.d("G22D","Enter in exce luset tv3");
		}
		
		try{
			mlocManager.removeUpdates(mlocListener);
		}
		catch(Exception e)
		{
			Log.d("G28","Luser Entered in excetpi");
		}
	
		luserThis = null;
		super.onDestroy();
	}
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{	
		try {
			luserThis = this;
			inten = intent;
			Log.d("G28","ENTERED Luser service");
			Log.d("G28","ENTERED Luser service");
			
			mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			if (cIC() == false)
			{
				Log.d("G28","Internet not there");
				Toast.makeText(this, "Internet is not there !!", 100000).show();
			}
			SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);	
			externalregid = intent.getStringExtra("externalregid");		
			Log.d("G28","Externalregid ="+ externalregid);
			if (!externalregid.equals(""))
			{
				flag4 = true;
			
				Cursor cc2 = db.rawQuery("SELECT * FROM Knowns;", null); 
				if (cc2.getCount()>0)
				{
					cc2.moveToFirst();	
					do
					{
						if (cc2.getString(cc2.getColumnIndex("RegNo")).equals(externalregid))
						{
							flag3 = true;
						}
					}while (cc2.moveToNext());
				}
				if (flag3 == false)
				{
					externalregid = "";
				}
				cc2.close();
			}
			Date d = new Date();		
					
			Cursor c = db.rawQuery("SELECT * FROM User;", null); 		
			c.moveToFirst();		
			String tlu = c.getString(c.getColumnIndex("Tlu"));		
			String mis = c.getString(c.getColumnIndex("misc"));		
			c.close();
			db.close();
			
			DateFormat formatter ; 
			Date curdate = new Date();
			Date tludate = new Date();
			Date misdate = new Date();
			
			long misage = 1330000;
			boolean conerr1 = false;
			boolean conerr2 = false;
			formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zz yyyy");
			Log.d("G26","chk2");
			try {
				
				curdate = (Date)formatter.parse(""+d);
			} catch (ParseException e) {
				
				e.printStackTrace();
			} 
			try {
				
				tludate = (Date)formatter.parse(tlu);
			} catch (ParseException e) {
				conerr1 = true;
				e.printStackTrace();
			}
			try {
				
				misdate = (Date)formatter.parse(mis);
			} catch (ParseException e) {
				conerr2 = true;
				e.printStackTrace();
			} 
			Log.d("G26","chk3");
			if (conerr1 == false)
				{tluage = curdate.getTime() - tludate.getTime();}
			if (conerr2 == false)
				{misage = curdate.getTime() - misdate.getTime();}
			Log.d("G26","chk4");
			if ((externalregid.equals("")) && (flag4 == false))
			{
				//called by main
				//taking on tluage
				if (0 < tluage && tluage < 120000 )
				{
					flag1 = false;
					
				}
				if (tluage > 120000) ///*******100000********
				{
					flag1 = true;
					
				}
				//taking on misage
				if (misage < 1200000)
				{
					flag2 = false;
				}
				if (misage > 1200000) //1200000
				{
					if (cIC() )
					{
						flag1 = true;
						flag2 = true;
					}
				}
				
			}
			else if (flag3 == true)
			{
				//called by c2dmreceiver
				if (misage > 15000)
				{
					flag1 = true;
					flag2 = true;
				}
			}
			else
			{
				flag1 = true;
			}
			
			Log.d("G25","tluage = "+tluage);
			Log.d("G25","misage = "+misage);
			Log.d("G25","Flag1 ="+flag1);
			Log.d("G25","Flag2 ="+flag2);
			
			 
//		flag1 = true;
//		flag2 = true;
			
			
      
      
			
			if (flag1 == true)
			{
				
			    mlocListener = new MyLocationListener();
			    Log.d("G28","Luser value of isGpsenabled(opp) = "+ !mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER ));
			    if ( !mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER ) ) 
			    {
			    	Log.d("G28","Luser GPS check got that provides is disabled");  
			    	buildAlertMessageNoGps(1);
			    }
			    
				
			   
			    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
			    String locationProvider = LocationManager.NETWORK_PROVIDER;
   
			    String locationProviderg = LocationManager.GPS_PROVIDER;
			    lastnk = mlocManager.getLastKnownLocation(locationProvider);
			    lastgk = mlocManager.getLastKnownLocation(locationProviderg);
			  
			   if (lastnk != null)
			   {
				    if ((isBetterLocation(lastnk, lastgk)))
				    {
				    	
							lati = lastnk.getLatitude();
							longi = lastnk.getLongitude();
							Log.d("G28","DB updated 1A");
							if (tluage > (20*60*1000))
							{
								lastgk = null; 
							}
			
				    }
				    else
				    {
				    		if (lastgk != null)
				    		{
								lati = lastgk.getLatitude();
								longi = lastgk.getLongitude();
								if (tluage > (15*60*1000))
								{
									lastgk = null; 
								}
				    		}
					}
			   }
					
			       
			    
			  
			    try
				{
			    
			    Log.d("G28","Luser :  lastknown location -Network : "+lastnk.getLatitude()+";Lastgk ="+lastnk.getLongitude());
			    Log.d("G28","Luser :  lastknown location -Gps : "+lastgk.getLatitude()+";Lastgk ="+lastgk.getLongitude());
				}
			    catch (Exception e)
				{
					Log.d("G28","Luser has entered in exception");
					

				}
			    if (lastgk == null && isgpson == true)
			    {
			    	Log.d("G28","LUSER sleep1");
				    try {
				    	Log.d("G28","Luser entered in the 10 sec sleep");
						Thread.sleep(400);
					} catch (InterruptedException e) {
						
						
						e.printStackTrace();
					}
				    Log.d("G28","LUSER wake");
				    Log.d("G28","LUSER sleep2");
				    try {
				    	Log.d("G28","Luser entered in the 10 sec sleep");
						Thread.sleep(400);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				    Log.d("G28","LUSER wake");
				    Log.d("G28","LUSER sleep3");
				    try {
				    	Log.d("G28","Luser entered in the 10 sec sleep");
						Thread.sleep(400);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				    Log.d("G28","LUSER wake");
				    Log.d("G28","LUSER sleep4");
				    try {
				    	Log.d("G28","Luser entered in the 10 sec sleep");
						Thread.sleep(600);
					} catch (InterruptedException e) {
						
						
						e.printStackTrace();
					}
				    Log.d("G28","LUSER wake");
				    Log.d("G28","LUSER sleep5");
				    try {
				    	Log.d("G28","Luser entered in the 10 sec sleep");
						Thread.sleep(600);
					} catch (InterruptedException e) {
						
						
						e.printStackTrace();
					}
				    Log.d("G28","LUSER wake");
				    Log.d("G28","LUSER sleep5");
				    try {
				    	Log.d("G28","Luser entered in the 10 sec sleep");
						Thread.sleep(200);
					} catch (InterruptedException e) {
						
						
						e.printStackTrace();
					}
				    Log.d("G28","LUSER wake");
				   
				  
				    
			    }
			    Log.d("G28","visd checking to delete2");
			    mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, mlocListener);
			   // mlocManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, mlocListener);
			    Intent myIntent = new Intent(Luser.this, Stopmadluser.class);
				PendingIntent pendingIntent = PendingIntent.getService(Luser.this, 0, myIntent, 0);
			   AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			   Calendar calendar = Calendar.getInstance();
			   calendar.setTimeInMillis(System.currentTimeMillis());	               
			   calendar.add(Calendar.SECOND, 60);
			   alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
			   Log.d("G28","Alarm set from luser");
			    
			}
			else
			{
				Log.d("G28","Destroying service as flag1 is false, check flag1= "+flag1+", flag2 = "+flag2);
				stopSelf();
				stopService(intent);
			}
			
			
			
			return super.onStartCommand(intent, flags, startId);
		} catch (Exception e) {
			Toast.makeText(Luser.this, "Location service ERROR", 10000).show();
		}
		return startId;
	}
	
	
	
	private void buildAlertMessageNoGps(int i) {
		if (i == 1 ||  i == 3){
			isgpson = false;
			Log.d("G28","Luser starting gpsdialog");
			String mesg1 = "";
			if (externalregid.equals(""))
			{
				mesg1 = "1";//"GIRAFFE : Your Network Location service seems to be disabled, do you want to enable it?";
			}
			else
			{
				mesg1 = "2";// "GIRAFFE : Somebody wants to know your location and GPS satellite is off, do you want to enable it?";
			}
			Intent anotherActivityIntent = new Intent(this, Gpsdialog.class);
			anotherActivityIntent.putExtra("ext",mesg1);
			anotherActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(anotherActivityIntent);
			Log.d("G28","Luser starting gpsdialog");
			}
		else if (i == 13)
		{
			Log.d("G28","Entered builder 1");
			final AlertDialog.Builder builder = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
			String mesg1 = "";
			if (externalregid.equals(""))
			{
				mesg1 = "GIRAFFE : Your Network Location service seems to be disabled, do you want to enable it?";
			}
			else
			{
				mesg1 = "GIRAFFE : Somebody wants to know your location and GPS satellite is off, do you want to enable it?";
			}
				builder.setMessage(mesg1)
		           .setCancelable(false)
		           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               @Override
					public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		            	   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); 
		               }
		           })
		           .setNegativeButton("No", new DialogInterface.OnClickListener() {
		               @Override
					public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		                    dialog.cancel();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
		}
	}
	public class MyLocationListener implements LocationListener
	{
		@Override	
		public void onLocationChanged(Location loc)	
		{	
			double ltii = (double) loc.getLatitude();
			double lngii = (double) loc.getLongitude();
			
			
			Location locd = null;
			if (loc.getProvider().toString().indexOf("gps")!=-1 || loc.getProvider().toString().equalsIgnoreCase("gps"))
			{
				locd = loc;
			}
			if (loc.getProvider().toString().indexOf("network")!=-1 || loc.getProvider().toString().equalsIgnoreCase("network"))
			{
				ncount ++;
				if (ncount == 1)
				{
					d1 = new Date();
				}
				if (ncount == 2)
				{
					d2 = new Date();
					
					try {
						if ((d2.getTime()-d1.getTime() )>15000)
						{
							ncount = 3;
							Log.d("G28","date frod ghas incerem");
						}
					} catch (Exception e) {
						Log.d("G28","Entere in ddaet dxed");
						e.printStackTrace();
					}
				}
			}
			
			Log.d("G28","Entered on location changed, latest lati = "+ltii+" and longi = "+lngii+";Provider ="+loc.getProvider()+"; Accuracy ="+loc.getAccuracy());
			//Toast.makeText(Luser.this, ""+loc.getProvider()+"; accuracy = "+loc.getAccuracy()+"; ncount "+ncount+";gcount = "+gcount, 10000).show();
			try
			{
				TextView mvin = (TextView) Mainwindow.mainwindowThis.findViewById(R.id.mvindicator);
				mvin.setVisibility(View.VISIBLE);
				mvin.setBackgroundResource(R.drawable.btn_yellow_mw);
				Log.d("G22D","Enter in exce luset tv2 passed");
			}
			catch(Exception e)
			{
				Log.d("G22D","Enter in exce luset tv2");
			}
			Log.d("G28","Entered on location changed");
			Log.d("G28", " Luser : ENTERED ON LOCATION CHANGED");
			
			Log.d("G28","ncount ince1");
			if (loc.getProvider().toString().indexOf("gps") != -1 || loc.getProvider().toString().indexOf("network") == -1)
			{
				gcount ++;
			}
			
			if ((ncount < 3) && (loc.getProvider().toString().indexOf("network")!=-1 || loc.getProvider().toString().equalsIgnoreCase("network")))
			{
				try
				{
					if ((isBetterLocation(loc, lastnk) || isBetterLocation(loc, lastgk)) )
					{
						
					}
					else
					{
						
						Log.d("G28","ncount ince3");
					}
		
				    
				    Log.d("G28","received from network first location skipped");
				}
				catch (Exception e)
				{
					Log.d("G28","nocunt block exceoptio");
				}
			}
			else if ((gcount == 1) && (loc.getProvider().toString().indexOf("network") == -1) )
			{
				Log.d("G28","received from not network first location skipped");
			}
			else
			{
			//Log.d("G28","Result of first comparision :"+isBetterLocation(loc, lastnk) );
			//Log.d("G28","Result of Second comparision :"+isBetterLocation(loc, lastgk) );
			
				chkcount++;
				Log.d("G32","Luser chcount = "+chkcount);
				if (locd != null)
				{
					if (isBetterLocation(locd, loc))
					{
						loc = locd;
					}
				}
				if ((isBetterLocation(loc, lastnk) || isBetterLocation(loc, lastgk)) || chkcount > 2) //true
				{
					
					Log.d("G28","Entered in block, Removed location updatesdm, chkcount = "+chkcount);
					mlocManager.removeUpdates(mlocListener);
					/*
					if (isBetterLocation(loc, lastnk) && !isBetterLocation(loc, lastgk))
					{
						loc = lastgk;
						Log.d("G28","Luser :  location was better than last network but not better than last gps");
					}
					else if (!isBetterLocation(loc, lastnk) && isBetterLocation(loc, lastgk))
					{
						loc = lastnk;
						Log.d("G28","Luser :  location was not better than last network but better than last gps");
					}
					else if (!isBetterLocation(loc, lastnk) && !isBetterLocation(loc, lastgk))
					{
						Log.d("G28","Luser :  Neither location was better than network but nor better than last gps");
						if (isBetterLocation(lastnk,lastgk))
						{
							loc = lastnk;
							Log.d("G28","Luser :  loc = lastnk");
						}
						else
						{
							loc = lastgk;
							Log.d("G28","Luser :  loc = lastgk");
						}
					}
					*/
				
				
					
					Log.d("G28", "ENTERED ON Lc 3");
					Log.d("G28", "Final Location source"+loc.getProvider()+"  and accuracy : "+loc.getAccuracy());
					int accura = (int) loc.getAccuracy();
					//Toast.makeText(Luser.this, "Final Location source"+loc.getProvider()+"  and accuracy : "+loc.getAccuracy(), 10000).show();
					try
					{
						TextView mvin = (TextView) Mainwindow.mainwindowThis.findViewById(R.id.mvindicator);
						mvin.setVisibility(View.VISIBLE);
						mvin.setBackgroundResource(R.drawable.btn_greens);
						Log.d("G22D","entered in luser tv5 passed");
					}
					catch(Exception e)
					{
						Log.d("G22D","entered in luser tv 5 exe :"+e.toString());
					}
					lati = loc.getLatitude();
					longi = loc.getLongitude();
					Log.d("G28","Current Address value Latit : " + lati+ " Longit : " +longi);
					
					SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);			
					Cursor c = db.rawQuery("SELECT * FROM User;", null); 
					c.moveToFirst();
					String rn = c.getString(c.getColumnIndex("RegNo"));	
					Date dat = new Date();
					ContentValues values = new ContentValues();
					ContentValues values2 = new ContentValues();
					ContentValues values3 = new ContentValues();
					values.put("Lat", lati);
					values2.put("Long", longi);	
					values3.put("Tlu", ""+dat);
					
					db.update("User", values, "RegNo ='"+rn+"'", null);
					db.update("User", values2, "RegNo ='"+rn+"'", null);
					db.update("User", values3, "RegNo ='"+rn+"'", null);		
					Log.d("G28","DB updated 1");
			        c.close();
			        db.close();
			        Log.d("G25","User table updated with new loaction");
			        if (Mainwindow.mainwindowThis != null)
			        {
			        	Mainwindow.mainwindowThis.mslat = ""+lati;
			        	Mainwindow.mainwindowThis.mslong = ""+longi;
			        }
			        if (Arrowhandler.aThis != null)
			        {
			        	Arrowhandler.aThis.mslati = (float) lati;
			        	Arrowhandler.aThis.mslongi = (float) longi;
			        	double angle = Math.atan2(Arrowhandler.aThis.mslongi - Arrowhandler.aThis.oslongi, Arrowhandler.aThis.mslati - Arrowhandler.aThis.oslati);
			       	  	double degree = angle*57.295779;	
			       	  	float dd = (float) degree;
				       	if (dd>0)
				 	    {
				 	     	dd = dd+180;
				 	    }
				 	    else if (dd<0)
				 	    {
				 	     	dd = 180 - (Math.abs(dd));
				 	    }    
					    Arrowhandler.aThis.d = dd;
					    float idisti = Arrowhandler.aThis.distFrom(Arrowhandler.aThis.mslati, Arrowhandler.aThis.mslongi, Arrowhandler.aThis.oslati, Arrowhandler.aThis.oslongi);
					    idisti = (float) (idisti * 1.60934);
					    Arrowhandler.aThis.dist = idisti;				   
			        }
			        
			        
			        
					if (flag2 == true)
					{
						 eern = rn; eelati = ""+lati; eelongi = ""+longi; eeaccura = ""+accura; eedat = ""+dat; eeexternalregid = externalregid;
						Thread t = new Thread (Luser.this, "networkcomm");
						t.start();
						try {
							if (Mainwindow.mainwindowThis != null)
							{
								ProgressBar p = (ProgressBar) Mainwindow.mainwindowThis.findViewById(R.id.mwwait);
								p.setVisibility(View.VISIBLE);
							}
							t.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (Mainwindow.mainwindowThis != null)
						{
							ProgressBar p = (ProgressBar) Mainwindow.mainwindowThis.findViewById(R.id.mwwait);
							p.setVisibility(View.INVISIBLE);
						}
					}
					else
					{
						Log.d("G25","Luser DidNOT Sended to update loc");
					}
					Log.d("G28","Destroying from location changed..");
					mlocManager.removeUpdates(mlocListener);
					if (chkcount > 3)
					{
						SQLiteDatabase dbs = openOrCreateDatabase("GiraffeDBv2", 0, null);
						ContentValues cv = new ContentValues();
						String fix = "fix";
						cv.put("Misc2", "smlu");
						dbs.update("States", cv, "Misc1 = '"+fix+"'", null);
						dbs.close();
						int pid =  android.os.Process.myPid();
						int[] expid = {pid,chkcount};
						Log.d("G28","chkcount > 3");
						Context con = getApplicationContext();
						con.stopService (inten);
						if (!iSR())
						{
							
							Intent smuinte = new Intent (Luser.this, Stopmadluser.class);
							smuinte.putExtra("expid", expid);
							startService (smuinte);
						}
						
					}
					stopSelf();
			    	stopService(inten);	
			    	
				}
				else
				{
					mlocManager.removeUpdates(mlocListener);
					
					Log.d("G28","check for long since last update tulage = "+tluage);
					if (tluage > (10*60*1000))
					{
						 lati = loc.getLatitude();
						 longi = loc.getLongitude();
						 SQLiteDatabase dbaa = openOrCreateDatabase("GiraffeDBv2", 0, null);			
						 Cursor caa = dbaa.rawQuery("SELECT * FROM User;", null); 
						 if (caa.getCount() > 0)
						 {
									caa.moveToFirst();
								
									String rna = caa.getString(caa.getColumnIndex("RegNo"));	
									
									ContentValues valuesaa = new ContentValues();
									ContentValues values2aa = new ContentValues();
									
									valuesaa.put("Lat", ""+lati);
									values2aa.put("Long", ""+longi);	
									dbaa.update("User", valuesaa, "RegNo ='"+rna+"'", null);
									dbaa.update("User", values2aa, "RegNo ='"+rna+"'", null);
									Log.d("G28","DB updated 3");
								
						 }
						 dbaa.close();
					}
							
					Log.d("G25","Luser Sending to update loc, only if true is flag2 = "+flag2);
					Thread t = new Thread (Luser.this,"Inteco");
					if (flag2 == true)
					{
						int accuracc = (int) loc.getAccuracy();
						SQLiteDatabase db =openOrCreateDatabase("GiraffeDBv2", 0, null);			
						Cursor c = db.rawQuery("SELECT * FROM User;", null); 
						c.moveToFirst();
						String rn = c.getString(c.getColumnIndex("RegNo"));	
						Date dat = new Date();
						eeaccura = ""+accuracc;
						eelati = ""+lati;
						eelongi = ""+longi;
						eern = rn;
						eedat = ""+dat;
						eeexternalregid = externalregid;
						
						t.start();
						db.close();
						
						
					}
					SQLiteDatabase dba = openOrCreateDatabase("GiraffeDBv2", 0, null);			
					Cursor ca = dba.rawQuery("SELECT * FROM User;", null); 
				  
				    	
						
						if (ca.getCount() > 0)
						{
							ca.moveToFirst();
						
							String rna = ca.getString(ca.getColumnIndex("RegNo"));	
							
							ContentValues valuesa = new ContentValues();
							ContentValues values2a = new ContentValues();
							
							valuesa.put("Lat", ""+lati);
							values2a.put("Long", ""+longi);	
							dba.update("User", valuesa, "RegNo ='"+rna+"'", null);
							dba.update("User", values2a, "RegNo ='"+rna+"'", null);
							Log.d("G28","DB updated 4");
							
						}
					ca.close();
					dba.close();
					
						
					try {
						if (Mainwindow.mainwindowThis != null)
						{
							ProgressBar p = (ProgressBar) Mainwindow.mainwindowThis.findViewById(R.id.mwwait);
							p.setVisibility(View.VISIBLE);
						}
						t.join();
					} catch (InterruptedException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					if (Mainwindow.mainwindowThis != null)
					{
						ProgressBar p = (ProgressBar) Mainwindow.mainwindowThis.findViewById(R.id.mwwait);
						p.setVisibility(View.INVISIBLE);
					}	
					stopSelf();
			    	stopService(inten);	
				}
			}
			
		}
		
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
    	
    	
        try {
			if (currentBestLocation == null) {
				Log.d("G28","A new location is always better than no location");
				
			    return true;
			}
			else
			{
				Log.d("G28","inside isbetter location :provider = "+location.getProvider()+";"+currentBestLocation.getProvider()+";; location.gettime() = "+location.getTime()+"; currbest.getTime()="+currentBestLocation.getTime());
				Log.d("G28","currentBestLocation provider = " +currentBestLocation.getProvider().toString());
			}
			if (location.getProvider().toString().indexOf("gps") != -1)
			{
				Log.d("G28","founded location.getProvider gps");
				return true;
			}
			else
			{ 
				Log.d("G28","Network provided location was received"); 
				Location bigger, smaller;
				int flag = 0;
				Log.d("G28","location received from netwrok");
				if (currentBestLocation.getProvider().toString().indexOf("gps") != -1 || currentBestLocation.getProvider().toString().equalsIgnoreCase("gps"))
				{
					Log.d("G28","comparing with old Gps");
					Log.d("G28","comparing with old Gps");
					float dis = distFrom(location.getLatitude(), location.getLongitude(), currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
					Log.d("G28","Dis = "+dis);
					Log.d("G28","location.getAccuracy() = "+location.getAccuracy()+", currentbestlocation.getacuracy ="+ currentBestLocation.getAccuracy());
					if (location.getAccuracy() > currentBestLocation.getAccuracy())
					{
						bigger = location;
						smaller = currentBestLocation;
						flag = 1;
					}
					else
					{
						bigger = currentBestLocation;
						smaller = location;
					}
					if (dis < bigger.getAccuracy())
					{ 
						Log.d("G28","Entered is dis < bigger.getAccuracy ; flag = "+flag);
						if (dis < smaller.getAccuracy())
						{
							Log.d("G28","dis < smaller.getAccuracy ; flag = "+flag);
							if (flag == 1)
							{
								return true;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (flag == 1)
							{
								return false;
							}
							else
							{
								return true;
							}
						}
						
					}
					else
					{
						return true;
					}
				}
				else
				{
					Log.d("G28","comparing with old Network location");
					Log.d("G28","comparing with old Network location");
					if (distFrom(location.getLatitude(), location.getLongitude(), currentBestLocation.getLatitude(), currentBestLocation.getLongitude())>0)
					{
						Log.d("G28","comparing with old location returned true as distance difference = "+distFrom(location.getLatitude(), location.getLongitude(), currentBestLocation.getLatitude(), currentBestLocation.getLongitude()));
						return true;
						
					}
					else
					{
						Log.d("G28","comparing with old network location returned false");
						return false;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        // Check whether the new location fix is newer or older
        
        /*
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        Log.d("G28","Tiemdelta : "+timeDelta);
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }
       
        
        if (distFrom(location.getLatitude(), location.getLongitude(), currentBestLocation.getLatitude(), currentBestLocation.getLongitude()) > 200 )
        {
        	return true;
        }
        else
        {
        	int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        	 Log.d("G28","Accuracy delta : "+accuracyDelta);
             boolean isLessAccurate = accuracyDelta > 0;
             boolean isMoreAccurate = accuracyDelta < 0;
             boolean isSignificantlyLessAccurate = accuracyDelta > 50;
             if (isSignificantlyLessAccurate)
             {
            	 return false;
             }
             else
             {
            	 return true;
             }
        	
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        Log.d("G28","Accuracy delta : "+accuracyDelta);
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 100;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false; */
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    public boolean cIC() {
    	Log.d("G28","Entered cic");
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
        	Log.d("G28","Returned true");  
        	return true;
        } else {              
              Log.d("G28","Returned false");  
            return false;
        }
    }
    
    private boolean iSR() {

        String sClassName;
       
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) 
        {
            sClassName = service.service.getClassName();
         
            if (sClassName.contains("com.visd.Locepeop.Stopmadluser"))
            {
            	
                  return true;
                  
            }
        }
       
        return false;
    }
    
    public float distFrom(double d, double e, double f, double g) {
		//Log.d("SH","EF");
	    double er = 3958.75;
	    double dLat = Math.toRadians(f-d);
	    double dLng = Math.toRadians(g-e);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(d)) * Math.cos(Math.toRadians(f)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = er * c;
	    float dst = (float) dist;
	    dst = (float) (dst*1.60934*1000);
	    dist = Float.parseFloat(""+dist);
	    //Log.d("G28","Tr9");
	    //int meterConversion = 1609;

	    return dst;
	    }
	@Override
	public void run() {
		String arr[] = {eern,eelati, eelongi, eeaccura, eedat, eeexternalregid};
		Intent i = new Intent (Luser.this, Newicc.class);
		i.putExtra("DET", arr);
		startService(i);
		
	}
	
		
	}



