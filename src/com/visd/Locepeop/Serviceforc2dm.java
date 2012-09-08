package com.visd.Locepeop;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class Serviceforc2dm extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int onStartCommand (Intent intent, int flags, int startId)
	{
		Log.d("G22","Entered in service for c2dm");
		String[] arrs = intent.getExtras().getStringArray("DET");	
		try
		{
		if (arrs[0].equals("AUc2dm"))
		{
			boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false;
			String kregid = arrs[1];
			String kname = arrs[2];
			String knum = arrs[3];
			String klat = arrs[4];
			String klong = arrs[5];
			Log.d("G22","kregid = "+kregid+";kname = "+kname+";knum="+knum+";klat="+klat+";klong = "+klong);
			SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
			Cursor c = db.rawQuery("SELECT * FROM Knowns", null);
			if (c.getCount()>0)
			{
				Log.d("G22","entere in knowns, c.getcount = "+c.getCount());
				c.moveToFirst();
				do
				{
					String knregid = c.getString(c.getColumnIndex("RegNo"));
					if (knregid.equals(kregid))
					{
						flag1 = true;
						ContentValues value1 = new ContentValues();
						ContentValues value2 = new ContentValues();		    			
						value1.put("Lat", klat);
						value2.put("Long", klong);
						db.update("Knowns", value1, "RegNo ='"+kregid+"'", null);
						db.update("Knowns", value2, "RegNo ='"+kregid+"'", null);
					}
				}while (c.moveToNext());
				
			}
			c.close();
			Cursor c2 = db.rawQuery("SELECT * FROM Waiting", null);
			if (c2.getCount()>0)
			{
				Log.d("G22","entere in waiting in service for c2dm, c2.getcount = "+c2.getCount());
				c2.moveToFirst();
				do
				{
					String knregid = c2.getString(c2.getColumnIndex("RegNo"));
					if (knregid.equals(kregid))
					{
						flag2 = true;
					}
				}while (c2.moveToNext());
			}
			db.close();
			Log.d("G22","flag values, Flag1 = "+flag1+"; Flag2 = "+flag2+"; Flag3 = "+flag3+"; Flag4 = "+flag4);
			if (flag1 == true)
			{
				SQLiteDatabase db3 = openOrCreateDatabase("GiraffeDBv2", 0, null);
				Cursor c3 = db3.rawQuery("SELECT * FROM User", null);
				c3.moveToFirst();
				String msregid = c3.getString(c3.getColumnIndex("RegNo"));
				c3.close();
				db3.close();
				HttpClient client = new DefaultHttpClient();				
		    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Addreply");    	
		    	Log.d("G22","Service for c2dm, sending to addreply complete");
		    	try {
		    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		    		// Get the deviceID
		    		nameValuePairs.add(new BasicNameValuePair("regid", msregid));
		    		nameValuePairs.add(new BasicNameValuePair("kregid", kregid));
		    		nameValuePairs.add(new BasicNameValuePair("code", "505"));
		    		
		    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
		    		
		    		HttpResponse response = client.execute(post);    		
		    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    		String line = "";
		    		while ((line = rd.readLine()) != null) {
		    			Log.d("G27","Processarr called by service for c2dm, and passing line = "+line);
		    			Intent proaarrinte = new Intent(this,Processarr.class);
		    			proaarrinte.putExtra("code", "505"+line);
		    			startService(proaarrinte);
		    			
		    		}
		    	} catch (IOException e2) {
		    		e2.printStackTrace();
		    		Log.d("G22", "Entered Exception");	
		    	}
			}
			else if (flag2 == false)
			{
				
				SQLiteDatabase db4 = openOrCreateDatabase("GiraffeDBv2", 0, null);
				db4.execSQL("INSERT INTO Waiting VALUES ('"+kregid+"','"+kname+"','"+klat+"','"+klong+"','','"+knum+"','','');" );	
				db4.close();	
				Toast.makeText(this, "One Add Request receiver, waiting for approval", 10000).show();
				Toast.makeText(this, "One Add Request receiver, waiting for approval", 10000).show();
				db4.close();
				
			}
			try {
				AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
				if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) 
				{
				       MediaPlayer mp = MediaPlayer.create(this, R.raw.snd);
				       mp.start();
				}
				Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
				long pattern[] = new long[] {0, 700, 150, 850};
				v.vibrate(pattern, -1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (arrs[0].equals("REMc2dm"))
		{
			String name = "";
			boolean flag = false;
			SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
			db.delete("Waiting", "RegNo = "+arrs[1], null);	
			Cursor c = db.rawQuery("SELECT * FROM Knowns", null);				
			if (c.getCount()>0)
			{
				c.moveToFirst();
				do
				{
					String reg = c.getString(c.getColumnIndex("RegNo"));
					if (reg.equals(arrs[1]))
					{
						name = c.getString(c.getColumnIndex("Name"));
						flag = true;
					}
				}while (c.moveToNext());
			}
			if (flag == true)
			{				
				db.delete("Knowns", "RegNo = "+arrs[1], null);				
				Toast.makeText(this, name+" Removed !", 10000).show();
			}
			db.close();
		}
		if (arrs[0].equals("UULc2dm"))
		{
			SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
			String regno = arrs[1];
			String lati = arrs[2];
			String longi = arrs[3];
			String islu = arrs[4];
			Log.d("G22","Insided servicefc2dm uulc2dm regno ="+regno+"  lati ="+lati+"  longi="+longi);
			ContentValues values = new ContentValues();
			ContentValues values2 = new ContentValues();
			values.put("Lat",lati);
			values2.put("Long",longi);
			db.update("Knowns", values, "RegNo ='"+regno+"'", null);
			db.update("Knowns", values2, "RegNo ='"+regno+"'", null);
			db.close();
			int isnotallowedflag = 0;
			int offline  = 0;
			if (islu.equals("1"))
			{
				if (!iSR())
				{
					Log.d("G30","Hence after checking all prerequisite condition, luser is started..");
					try 
					{
						 SQLiteDatabase dbtc = openOrCreateDatabase("GiraffeDBv2", 0, null);
						 Cursor wc = dbtc.rawQuery("SELECT * FROM SO", null);
						 
						 Cursor ofc = dbtc.rawQuery("SELECT * FROM Offline", null);
						 if (ofc.getCount() > 0)
						 {
							 ofc.moveToFirst();
							 String oval = ofc.getString(ofc.getColumnIndex("Value"));
							 if (oval.equals("1"))
							 {
								 offline = 1;
								 Log.d("G27","offline 1 =");
							 }
						 }
						 if(wc.getCount() > 0 || offline == 1 )
						 {
							 Log.d("G27","wc.et !!1=");
							 if (offline == 1)
							 {
								 Log.d("G27","wc.et !!01=");
								 isnotallowedflag = 1;
				            		Cursor c = dbtc.rawQuery("SELECT * FROM User;", null); 
									c.moveToFirst();
									String rn = c.getString(c.getColumnIndex("RegNo"));	
									Date dat = new Date();
									HttpClient client = new DefaultHttpClient();				
							    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Updatedloc");    	
							    	try 
							    	{
							    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
							    		// Get the deviceID
							    		nameValuePairs.add(new BasicNameValuePair("regId", rn));
							    		nameValuePairs.add(new BasicNameValuePair("lati", ""+lati));
							    		nameValuePairs.add(new BasicNameValuePair("longi", ""+longi));
							    		nameValuePairs.add(new BasicNameValuePair("accuracy", ""));
							    		nameValuePairs.add(new BasicNameValuePair("date", ""+dat));
							    		nameValuePairs.add(new BasicNameValuePair("ext", "xxxna"+regno));
							    		Log.d("G27","service for c2dm has sended xxxna");
							    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
							    		HttpResponse response = client.execute(post);    		
							    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							    		String line = "";
							    		while ((line = rd.readLine()) != null) 
							    		{
							    			
							    							    				
							    			
							    		}
							    	} 
							    	catch (Exception e) 
							    	{
							    		// 	TODO Auto-generated catch block
							    		e.printStackTrace();
							    	}
							    	c.close();
							 }
				            	
							 else
								{
								 wc.moveToFirst();
								 Log.d("G27","wc.et !!02=");
								
				            	do
				            	{
				            	if (wc.getString(wc.getColumnIndex("RegNo")).equals(regno))
				            	{
				            		Log.d("G27","wc.et !!03=");
				            		Log.d("G27","wc.et !!07=");
				            		//this user is in black list
				            		isnotallowedflag = 1;
				            		Log.d("G27","wc.et !!08=");
				            		Cursor c = dbtc.rawQuery("SELECT * FROM User;", null); 
				            		Log.d("G27","wc.et !!05=");
									c.moveToFirst();
									Log.d("G27","wc.et !!06=");
									String rn = c.getString(c.getColumnIndex("RegNo"));	
									Date dat = new Date();
									HttpClient client = new DefaultHttpClient();				
							    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Updatedloc");  
							    	Log.d("G27","wc.et !!04=");
							    	try 
							    	{
							    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
							    		// Get the deviceID
							    		nameValuePairs.add(new BasicNameValuePair("regId", rn));
							    		nameValuePairs.add(new BasicNameValuePair("lati", ""+lati));
							    		nameValuePairs.add(new BasicNameValuePair("longi", ""+longi));
							    		nameValuePairs.add(new BasicNameValuePair("accuracy", ""));
							    		nameValuePairs.add(new BasicNameValuePair("date", ""+dat));
							    		nameValuePairs.add(new BasicNameValuePair("ext", "xxxna"+regno));
							    		Log.d("G27","service for c2dm has sended xxxna");
							    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
							    		HttpResponse response = client.execute(post);    		
							    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							    		String line = "";
							    		while ((line = rd.readLine()) != null) 
							    		{
							    			
							    							    				
							    			
							    		}
							    	} 
							    	catch (Exception e) 
							    	{
							    		Log.d("G27","wc.et !!Exception=");
							    		e.printStackTrace();
							    	}
							    	c.close();
					
				            	}
				            	}while(wc.moveToNext());
						 	}
						 }
						 	if (isnotallowedflag == 0)
						 	{
						 		Intent inn = new Intent(this,Luser.class);		
						 		// there is to be a check that whether this row is offline value or not... 
				
						 		inn.putExtra("externalregid", regno);	
						 		startService(inn);
						 	}
						 	dbtc.close();
					}
					catch (Exception e) 
					{
							// TODO: handle exception
					}
				}
			}
		
		
			stopService(intent);
			getApplicationContext().stopService(new Intent(getApplicationContext(), Serviceforc2dm.class));
			return super.onStartCommand(intent, flags, startId);
		}
		}
		catch (Exception e) 
		{
			Toast.makeText(Serviceforc2dm.this, "SCERR", 10000).show();
		}
		stopService(intent);
		getApplicationContext().stopService(new Intent(getApplicationContext(), Serviceforc2dm.class));
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		//Log.d("G22","Serviceforcdmdestroyed");
		getApplicationContext().stopService(new Intent(getApplicationContext(), Serviceforc2dm.class));
		super.onDestroy();
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

}
