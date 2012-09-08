package com.visd.Locepeop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class Checkcatch extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("G33","Entered checkcatch");
		String versionname = "null";
		int versioncode = 0;
		try {
			versionname = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			versioncode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("G33","Entered checkcatch1");
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
		Cursor c = db.rawQuery("SELECT * FROM User", null);
		String regid = "", latt = "", longit = "", code = "";
		if (c.getCount()>0)
		{
			c.moveToFirst();
			regid = c.getString(c.getColumnIndex("RegNo"));
			latt =  c.getString(c.getColumnIndex("Lat"));
			longit = c.getString(c.getColumnIndex("Long"));
		}
		else
		{
			code = "404";
		}
		c.close();
		Log.d("G33","Entered checkcatch2");
		String kregids = "";
		Cursor c2 = db.rawQuery("SELECT * FROM Knowns", null);
		if (c2.getCount()>0)
		{
			c2.moveToFirst();
			do
			{
				kregids = kregids+"i"+c2.getString(c2.getColumnIndex("RegNo"));
			}while (c2.moveToNext());
		}
		c2.close();
		Log.d("G33","Entered checkcatch3");
		String waitids = "";
		Cursor c3 = db.rawQuery("SELECT * FROM Waiting", null);
		if (c3.getCount()>0)
		{
			c3.moveToFirst();
			do
			{
				waitids = waitids+"j"+c3.getString(c3.getColumnIndex("RegNo"));
			}while (c3.moveToNext());
		}
		c3.close();
		Log.d("G33","Entered checkcatch4");
		String sts = "";
		db.execSQL("CREATE TABLE IF NOT EXISTS States " +"(Misc1 TEXT, Misc2 TEXT, Misc3 TEXT, Misc4 TEXT);");
		Cursor c4 = db.rawQuery("SELECT * FROM States", null);
		if (c4.getCount()>0)
		{
			c4.moveToFirst();
			do
			{
				sts = sts+"daga Misc1 = "+c4.getString(c4.getColumnIndex("Misc1"));
				sts = sts+";Misc2 = "+c4.getString(c4.getColumnIndex("Misc2"));
				sts = sts+";Misc3 = "+c4.getString(c4.getColumnIndex("Misc3"));
				sts = sts+";Misc4 = "+c4.getString(c4.getColumnIndex("Misc4"));
			}while (c4.moveToNext());
		}
		c4.close();
		Log.d("G33","Entered checkcatch5");
		HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Checkserv");   
    	String det =  "Lati  = "+latt+"; Longi = "+longit+";Version Name = "+versionname+"; Version code = "+versioncode+"; Knowns = "+kregids+"; Waitings = "+waitids+";States = "+sts;
    	try {
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
    		
    		nameValuePairs.add(new BasicNameValuePair("regid", regid));
    		nameValuePairs.add(new BasicNameValuePair("det",det));
    		
    		nameValuePairs.add(new BasicNameValuePair("code", ""+code));
    		Log.d("G33","Sended to checkserv regid = "+regid+"code = " +code+"details = " + det);
    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
    		HttpResponse response = client.execute(post);    		
    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    		String line = "";
    		
    		while ((line = rd.readLine()) != null) 
    		{
    			
    			
    			
    		}
    	} catch (IOException e2) {
    		e2.printStackTrace();
    		
    		//Log.d("G22", "Newlocationdateserv Entered Exception");	
    	}
    	
		
			
		db.close();
		
		stopService(intent);
		return super.onStartCommand(intent, flags, startId);
	}
	

}

