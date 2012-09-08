package com.visd.Locepeop;

import java.io.BufferedReader;
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
import android.util.Log;
import android.widget.TextView;

public class Refregserv extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		String s = intent.getStringExtra("regId");
		
		SQLiteDatabase db3 = openOrCreateDatabase("GiraffeDBv2", 0, null);  
        Cursor c3 = db3.rawQuery("SELECT * FROM User;", null); 
		c3.moveToFirst();
		String regno = c3.getString(c3.getColumnIndex("RegNo"));
		String oldc2dm = c3.getString(c3.getColumnIndex("C2dm"));
		Log.d("G33","Old c2dm = "+oldc2dm);
		Log.d("G33","New c2dm = "+s);
		c3.close();
		db3.close();
		
		
		HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Refregserv");    	
    	try {
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    		nameValuePairs.add(new BasicNameValuePair("regid", regno));
    		nameValuePairs.add(new BasicNameValuePair("c2dm", s));
    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
    		HttpResponse response = client.execute(post);    		
    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    		String line = "";
    		Log.d("G33","Passedi3");
    		while ((line = rd.readLine()) != null) {
    			if (line.equals("500"))
    			{
    				Log.d("G33","Response from server = "+line);
    				   					
    					SQLiteDatabase db4 = openOrCreateDatabase("GiraffeDBv2", 0, null);  
    					ContentValues value = new ContentValues();
    					ContentValues value2 = new ContentValues();
						value.put("C2dm", s);
						value2.put("Misc4", ""+new Date());
						String fix = "fix";
						db4.update("User", value, "RegNo = "+regno, null);
						db4.update("States", value2, "Misc1 = '"+fix+"'", null);
						db4.close();	
						Log.d("G33","Process completed");
					if (Refreshreg.refregThis != null)
	    			{
						((TextView)Refreshreg.refregThis.findViewById(R.id.regregtv1)).setText("Your Device is Refreshed, you may now leave this window, Thankyou." );
    				}
    			}
    			else
    			{
    				if (Refreshreg.refregThis != null)
    				{
    					((TextView)Refreshreg.refregThis.findViewById(R.id.regregtv1)).setText("Problem occured, Sorry device is not refreshed." );
    				}
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		((TextView)Refreshreg.refregThis.findViewById(R.id.regregtv1)).setText("Server/Connectivity Problem. Sorry, device is not refreshed." );
    		Log.d("G33","Error : "+e);
    	}
    
    			
		
		
		
		stopService(intent);
		return super.onStartCommand(intent, flags, startId);
	}

}
