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


import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class Newicc extends Service implements Runnable {

	String eern, eelati, eelongi, eeaccura, eedat, eeexternalregid;
	Intent i;
	@Override
	public void onDestroy() {
		Log.d("G25","Entered onDestroy of Newicc with i value : "+ i);
		stopService(i);
		stopSelf();
		super.onDestroy();
	}
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		i = intent;
		String[] arrs = intent.getExtras().getStringArray("DET");	
		eern = arrs[0];
		eelati =  arrs[1];
		eelongi =  arrs[2];
		eeaccura =  arrs[3];
		eedat =  arrs[4];
		eeexternalregid  =  arrs[5];
		Thread t = new Thread (this, "muskil");
		t.start();
		
		
		return startId;
	
	}
	@Override
	public void run() {
		HttpClient client = new DefaultHttpClient();				
    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Updatedloc");    	
    	try {
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
    		// Get the deviceID
    		nameValuePairs.add(new BasicNameValuePair("regId", eern));
    		nameValuePairs.add(new BasicNameValuePair("lati", ""+eelati));
    		nameValuePairs.add(new BasicNameValuePair("longi", ""+eelongi));
    		nameValuePairs.add(new BasicNameValuePair("accuracy", ""+eeaccura));
    		nameValuePairs.add(new BasicNameValuePair("date", ""+eedat));
    		nameValuePairs.add(new BasicNameValuePair("ext", eeexternalregid));
    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
    		Log.d("G25","Luser Sending to update loc complete accuracee "+eeaccura+"lati and longi = "+eelati+";"+eelongi);
    		Log.d("G30","Luser Sending to updateloc servlet on request of exterregid = "+eeexternalregid+", complete");
    		HttpResponse response = client.execute(post);    		
    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    		String line = "";
    		while ((line = rd.readLine()) != null) {
    			
    			Log.d("G25", "from Updateloc servlet : "+line);
    			Log.d("G30", "from Updateloc servlet : "+line);
    			if (line.equals("assuc") || line.equals("assucess"))
    			{
    				
    				try {
						int flagiv = 0;
						
						SQLiteDatabase dbb1 = openOrCreateDatabase("GiraffeDBv2", 0, null);	
						try 
						{
							if (eeexternalregid != "xxx" && eeexternalregid != "")
							{
								
								try {
									Vibrator v = (Vibrator) getSystemService(Newicc.VIBRATOR_SERVICE);
									long pattern[] = new long[] {0, 120};
									v.vibrate(pattern, -1);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								Cursor c11 = dbb1.rawQuery("SELECT * FROM Knowns;", null); 
								
									if (c11.getCount() != 0)
									{
										c11.moveToFirst();
										do
										{
											if (c11.getString(c11.getColumnIndex("RegNo")).equals(eeexternalregid.trim()))
											{
												try
												{
													//Toast.makeText(Luser.this, "Your location sended to "+c11.getString(c11.getColumnIndex("Name"))+"("+c11.getString(c11.getColumnIndex("Pno"))+")", 10000).show();
													//Toast.makeText(Luser.this, "Your location sended to "+c11.getString(c11.getColumnIndex("Name"))+"("+c11.getString(c11.getColumnIndex("Pno"))+")", 10000).show();
												}
												catch (Exception e) {
													Toast.makeText(Newicc.this, "Your location sended to someone",10000).show();
												}
												flagiv = 1;
											}
										}while (c11.moveToNext());
									
									if (flagiv == 0)
									{
										
									}
								}
								
							}
						}
						catch (Exception e) {
							// TODO: handle exception
						}
						ContentValues value4 = new ContentValues();
						Log.d("G25","new Date() =" +new Date());
						value4.put("misc", ""+new Date());
						dbb1.update("User", value4, "RegNo ='"+eern+"'", null);
						dbb1.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
    			}
    			else
    			{
    				Toast.makeText(Newicc.this, "Error in location updating to server", 10000).show();
    			}
    			
    		}
    	} catch (IOException e2) {
    		e2.printStackTrace();
    		Log.d("G28", "Entered Luser Exception");	
    	}
	onDestroy();

	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
