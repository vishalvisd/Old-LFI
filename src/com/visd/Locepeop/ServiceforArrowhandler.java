package com.visd.Locepeop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
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

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class ServiceforArrowhandler extends Service implements Runnable{

	String reg;
	String extreg;
	String mslati;
	String mslongi;
	Intent i;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	public void onDestroy() {
		try
		{
			getApplicationContext().stopService(new Intent(getApplicationContext(), ServiceforArrowhandler.class));
			stopService(i);
			stopSelf();
		}
		catch(Exception e)
		{
			Toast.makeText(ServiceforArrowhandler.this, "ERR", 10000).show();
		}
		super.onDestroy();
	}
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		
		try {
			i = intent;
			String[] arrs = intent.getExtras().getStringArray("DET");	
			reg = arrs[0];
			extreg =  arrs[1];
			mslati =  arrs[2];
			mslongi =  arrs[3];

			Thread t = new Thread (this, "muskil");
			t.start();
		} catch (Exception e) {
			Toast.makeText(ServiceforArrowhandler.this, "SFA1", 10000).show();
			e.printStackTrace();
		}
		
		return super.onStartCommand(intent, flags, startId);
		
	}

	@Override
	public void run() {
		HttpPost post = new HttpPost("http://gifserb.appspot.com/Generalserv");   
    	
    	BasicHttpParams httpParameters = new BasicHttpParams();    	
    	HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);   	
    	HttpConnectionParams.setSoTimeout(httpParameters, 7000);
    	
    	HttpClient client = new DefaultHttpClient(httpParameters);

    	try {
    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
    		Log.d("G22","Tr8");
    		nameValuePairs.add(new BasicNameValuePair("knownreg", reg));
    		nameValuePairs.add(new BasicNameValuePair("extreg", extreg));
    		nameValuePairs.add(new BasicNameValuePair("mslati", ""+mslati));
    		nameValuePairs.add(new BasicNameValuePair("mslongi", ""+mslongi));
    		Log.d("G22","Sended to Newlocationupdatereq knownreg : "+reg+" and extreg :" + extreg);
    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
    		HttpResponse response = client.execute(post);    		
    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    		String line = "";
    		
    		
    		while ((line = rd.readLine()) != null) 
    		{
    			
    			Log.d("G22", "Arrowhandler catchin response from gernerall serv = "+line);
    			final String linee = line;
    			if (Arrowhandler.aThis != null)
    			{
		    			Arrowhandler.aThis.runOnUiThread(new Runnable() {    			
							public void run() {
								Button b7 = Arrowhandler.aThis.b7;
								String remreg = "";		
		    			if (linee.substring(0, 3).equals("500") || linee.substring(0, 3).equals("505"))
		    			{ 	
		    				
		    				if (b7 != null)
		    			   b7.setEnabled(true);
			    			String oslati = linee.substring(linee.indexOf("oslati")+6,linee.indexOf("oslong"));
			    			String oslongi = linee.substring(linee.indexOf("oslong")+6);
			    			Log.d("G22","Final converted oslati "+ oslati+" oslongi :"+oslongi);
			    			if (!(oslati.equals(" ") || oslongi.equals(" ") || oslati.equals("") || oslongi.equals("")))
			    			{
				    			Arrowhandler.aThis.angle = Math.atan2(Float.parseFloat(mslongi) - Float.parseFloat(oslongi), Float.parseFloat(mslati) - Float.parseFloat(oslati));
				    			Arrowhandler.aThis.degree = Arrowhandler.aThis.angle*57.295779;		       	  	
				    			Arrowhandler.aThis.d = (float) Arrowhandler.aThis.degree;
							    if (Arrowhandler.aThis.d>0)
							    {
							    	Arrowhandler.aThis.d = Arrowhandler.aThis.d+180;
							    }
							    else if (Arrowhandler.aThis.d<0)
							    {
							    	Arrowhandler.aThis.d = 180 - (Math.abs(Arrowhandler.aThis.d));
							    }   
							    Arrowhandler.aThis.dist = Arrowhandler.aThis.distFrom(Float.parseFloat(mslati), Float.parseFloat(mslongi), Float.parseFloat(oslati), Float.parseFloat(oslongi));
							    Arrowhandler.aThis.dist = (float) (Arrowhandler.aThis.dist * 1.60934);
			    			}
			    			Arrowhandler.aThis = Arrowhandler.aThis;
			    			if (Arrowhandler.aThis == null)
			    			{
			    				Log.d("G22","Athis is null in arrowhandler ");
			    			}
			    			else
			    			{
			    				Log.d("G22","aThis is not nulll but = "+Arrowhandler.aThis);
			    			}
			    			final String throslati = oslati;
			    			final String throslongi = oslongi;
			    			//runned3 = true;
			    			Log.d("G30","Testing for thread : before 2");
			    			Thread thr3 = new Thread (new Runnable() {
								
								@Override
								public void run() {
									//getAddress(Double.parseDouble(""+throslati), Double.parseDouble(""+throslongi));
									Log.d("G30","Testing for thread : before 21");
									String arr[] = {""+throslati,""+throslongi};
									Intent i = new Intent(ServiceforArrowhandler.this,Getaddress.class);
							        i.putExtra("Loci", arr);	        
							        startService(i);
							        Log.d("G30","Testing for thread : before 22");
								}
							});
			    			thr3.start();
			    			
		    			}
		    			else if (linee.substring(0, 3).equals("404"))
		    			{
		    				Toast.makeText(ServiceforArrowhandler.this, "Sorry, this user has unregistred, Table will be updated as such", 10000).show();
		    				remreg = linee.substring(3);
		    				
		    			}
		    			else if (linee.substring(0, 3).equals("400"))
		    			{
		    				Toast.makeText(ServiceforArrowhandler.this, "Denied Location, Your add request is still not confirmed, so you won't get correct location", 10000).show();
		    			}
		    			else if (linee.substring(0, 3).equals("399"))
		    			{
		    				Toast.makeText(ServiceforArrowhandler.this, "Sorry, you have been removed by the user, Table will be updated", 10000).show();
		    				remreg = linee.substring(3);
		    			}
		    			else
		    			{
		    				if (b7 != null)
		    				b7.setEnabled(true);
		    				Toast.makeText(ServiceforArrowhandler.this, "Sorry, Server Bad Response", 10000).show();
		    			}
		    			
		    			if (linee.substring(0, 3).equals("404") || linee.substring(0, 3).equals("399"))
		    			{
		    				SQLiteDatabase database = openOrCreateDatabase("GiraffeDBv2", 0, null);
		    				database.delete("Waiting", "RegNo = "+remreg, null);
		    				database.delete("Knowns", "RegNo = "+remreg, null);
		    				database.close();
		    				Toast.makeText(ServiceforArrowhandler.this, "USER REMOVED AND LIST UPDATED", 10000).show();
		    				
		    			}
		    				}});
    			}
    			
    		}
    	} 
    	
    	catch (ConnectTimeoutException eto)
    	{
    		if (Arrowhandler.aThis != null)
    		{
	    		Arrowhandler.aThis.runOnUiThread(new Runnable() {    			
					public void run() {
	    		Toast.makeText(ServiceforArrowhandler.this, "Problem Communicating", 10000).show();
	    		Toast.makeText(ServiceforArrowhandler.this, "Please check data connection", 10000).show();
					}});
    		}
    		
    	}
    	catch (SocketTimeoutException se)
    	{
    		if (Arrowhandler.aThis != null)
    		{
	    		Arrowhandler.aThis.runOnUiThread(new Runnable() {    			
					public void run() {
						Toast.makeText(ServiceforArrowhandler.this, "Problem Communicating..", 10000).show();
					}});
    		}
    	}
    	catch (IOException ee) 
    	{
    		final IOException e2 = ee;
    		if (Arrowhandler.aThis != null)
    		{
	    		Arrowhandler.aThis.runOnUiThread(new Runnable() {    			
					public void run() {
	    		e2.printStackTrace();
	    		if (Arrowhandler.aThis.b7 != null)
	    		Arrowhandler.aThis.b7.setEnabled(true);
	    		Toast.makeText(ServiceforArrowhandler.this, "Problem with Network Communication V2", 10000).show();
	    		Log.d("G22", "Newlocationdateserv Entered Exception");
					}});
    		}
    	}
    	catch (Exception e)
    	{
    		Toast.makeText(ServiceforArrowhandler.this, "EE", 5000).show();
    	}
    	try
    	{
    		getApplicationContext().stopService(new Intent(getApplicationContext(), ServiceforArrowhandler.class));    		
    		onDestroy();
    	}
    	catch(Exception e)
    	{
    		try
    		{
    			onDestroy();
    		}
    		catch (Exception e2)
    		{
    			
    		}
    		Toast.makeText(ServiceforArrowhandler.this, "ERR.", 10000).show();
    	}
	}
}
