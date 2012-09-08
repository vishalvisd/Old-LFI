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



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Waitingr extends Activity {

	String msregid = "";
	String userlati = "";
	String userlongi = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.waitingr);
		

		
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
		Log.d("G22","Entered waitingr");
		
		final Cursor waitinglist = db.rawQuery("SELECT * FROM Waiting;", null);   
		int wcount = waitinglist.getCount();
		if (wcount != 0)
		{
			Cursor userdet = db.rawQuery("SELECT * FROM User;", null); 
			if (userdet.getCount() == 0)
			{
				Toast.makeText(this, "FATAL ERROR, Register Again", 10000).show();
				db.delete("Knowns", null, null);
				db.delete("Waiting", null, null);
				db.close();
				finish();
			}
			userdet.moveToFirst();
			msregid = userdet.getString(userdet.getColumnIndex("RegNo"));
			userlati = userdet.getString(userdet.getColumnIndex("Lat"));
			userlongi = userdet.getString(userdet.getColumnIndex("Long"));
			userdet.close();
			db.close();
			
			
			waitinglist.moveToFirst();
			Log.d("G22","Entered waitingr *1");
				
				
				
				final TextView tv1 = (TextView) findViewById(R.id.prtext);
				tv1.setText("Add Request Received:- \n\n"+"From : "+waitinglist.getString(waitinglist.getColumnIndex("Name"))+" ("+waitinglist.getString(waitinglist.getColumnIndex("Pno"))+"), \n\nWAITING FOR APPROVAL..");
				final Button b1 = (Button) findViewById(R.id.prb1);
				final Button b2 = (Button) findViewById(R.id.prb2);
				b1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Log.d("G22","Entered waitingr *2");
						b2.setEnabled(false);
						b1.setEnabled(false);
						
						
						if (!cIC())
						{
							Toast.makeText(Waitingr.this, "Can't Approve, No Packet Data Connection, Please Enable it ", 10000).show();
							b2.setEnabled(true);
							
							
						}
						else
						{
							HttpClient client = new DefaultHttpClient();
					    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Addreply");    	
					    	try {
					    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
					    		// Get the deviceID
					    		nameValuePairs.add(new BasicNameValuePair("regid", msregid));
					    		nameValuePairs.add(new BasicNameValuePair("kregid", waitinglist.getString(waitinglist.getColumnIndex("RegNo"))));
					    		nameValuePairs.add(new BasicNameValuePair("mslati", userlati));
					    		nameValuePairs.add(new BasicNameValuePair("mslongi", userlongi));
					    		nameValuePairs.add(new BasicNameValuePair("code", "500"));
					    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
					    		HttpResponse response = client.execute(post);    		
					    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					    		String line = "";
					    		while ((line = rd.readLine()) != null) {
					    			Log.d("G27","Processarr called by waitingr, and passing line = "+line);
					    			Intent proaarrinte = new Intent(Waitingr.this,Processarr.class);
					    			proaarrinte.putExtra("code", "500"+line);
					    			startService(proaarrinte);
					    			
					    		}
					    	} catch (IOException e2) {
					    		e2.printStackTrace();
					    		Toast.makeText(Waitingr.this, "Sorry, Server Misbehaved", 10000).show();
					    		
					    		//Log.d("G22", "Entered Exception");	
					    	}
					    	if (waitinglist.moveToNext())
					    	{
					    		
								tv1.setText("Add Request Received from "+waitinglist.getString(waitinglist.getColumnIndex("Name"))+" ("+waitinglist.getString(waitinglist.getColumnIndex("Pno"))+"), Waiting for Approval..");
								b1.setEnabled(true);
								b2.setEnabled(true);
								
					    	}
					    	else
					    	{
					    		Intent RegistrationIntent = new Intent(Waitingr.this,Mainwindow.class);
					        	startActivity(RegistrationIntent);
					        	finish();
					    	}
						}
					
						
				
					}
				});
				b2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.d("G22","Entered on Reject clike");
						b1.setEnabled(false);
						b2.setEnabled(false);
						Log.d("G22","Entered on Reject 1");
						SQLiteDatabase db3 = openOrCreateDatabase("GiraffeDBv2", 0, null);
						String regno = waitinglist.getString(waitinglist.getColumnIndex("RegNo"));
						Log.d("G22","Entered on Reject 2");
						db3.delete("Waiting", "RegNo = "+regno, null);    		
	    				Toast.makeText(Waitingr.this, "Request Removed", 10000).show();
	    				if (waitinglist.moveToNext())
				    	{
	    					Log.d("G22","Entered on Reject 4, positon = "+waitinglist.getPosition());
				    		String requestfrom = waitinglist.getString(waitinglist.getColumnIndex("Name"));
							String pno = waitinglist.getString(waitinglist.getColumnIndex("Pno"));
							tv1.setText("Add Request Received from "+requestfrom+" ("+pno+"), Waiting for Approval..");
							b1.setEnabled(true);
							b2.setEnabled(true);
							
				    	}
				    	else
				    	{
				    		Intent RegistrationIntent = new Intent(Waitingr.this,Mainwindow.class);
				        	startActivity(RegistrationIntent);
				        	waitinglist.close();
				        	finish();
				    	}
	    				
						
					}
				});
	  
			
		}
		else
		{
			Log.d("G22","Entered waitingr *3");
			Intent RegistrationIntent = new Intent(this,Mainwindow.class);
        	startActivity(RegistrationIntent);  
        	waitinglist.close();
        	db.close();
        	finish();
        	     
		}
		
    
    	
	}
	 public boolean cIC() {
	    	Log.d("G22","Entered cic of waitingr");
	        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
	        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
	        	//Log.d("G22","Returned true");  
	        	return true;
	        } else {              
	              //Log.d("G24","Returned false");  
	            return false;
	        }
	    }
}
