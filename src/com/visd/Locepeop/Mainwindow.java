package com.visd.Locepeop;

 

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.text.AndroidCharacter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Mainwindow extends ListActivity
{
	int i,i1,i2;
	String str[]= new String[500];
	String str2[]= new String[500];
	Intent intent;	
	int unregcnt = 0;
	int refcnt = 0;
	boolean userrem = false;
	public static Mainwindow mainwindowThis = null;
	String mslat = "0.0";
	String mslong = "0.0";
	long lastupdage = 0;
	
	
	@Override
	protected void onPause() {
	
		super.onPause();
		 unregcnt = 0;
		 refcnt = 0;
		mainwindowThis = null;
	}
	@Override
	protected void onResume() {
		checkonoffline();
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
        Cursor msc = db.rawQuery("SELECT * FROM User", null);
        msc.moveToFirst();
        mslat = msc.getString(msc.getColumnIndex("Lat"));
        mslong = msc.getString(msc.getColumnIndex("Long"));
        msc.close();
        db.close();
		mainwindowThis = this;
		 unregcnt = 0;
		 refcnt = 0;
		if (cIC() == false)
		{
			//Log.d("G22","Interne not there");
			Toast.makeText(this, "No Packet data connection !", 100000).show();
			/*
			ConnectivityManager mgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    		Method dataMtd = null;
			try 
			{
				dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
			}
			catch (SecurityException e2)
			{
				
				//Log.d("G22", "Entered SecurityException");
				e2.printStackTrace();
			} 
			catch (NoSuchMethodException e2) 
			{
				//Log.d("G22", "Entered NoSuchMethodException");
				e2.printStackTrace();
			}
    		dataMtd.setAccessible(true);
    		String suc = null;
    		try 
    		{
				while (!cIC())
				{
					dataMtd.invoke(mgr, true);
					//Log.d("G24","main loopping");
				}
				suc = "1";
				
			} 
    		catch (IllegalArgumentException e1) 
    		{
				suc = "0";
				//Log.d("G22", "Enteredillegealargument Exception");
				e1.printStackTrace();
			} 
    		catch (IllegalAccessException e1) 
    		{
    			suc = "0";
				//Log.d("G22", "Entered illegalaccessException");
				e1.printStackTrace();
			} catch (InvocationTargetException e1) 
			{
				suc = "0";
				//Log.d("G22", "Entered invocationtargetException");
				e1.printStackTrace();
			} 
    		if (!suc.equals("0") && suc.equals("1"))
    		{
    			Cursor cur = db.rawQuery("SELECT * FROM States;", null); 
    			//Log.d("G22","Curcount = "+cur.getCount());
    			if (cur.getCount() == 0)
    			{
    				db.execSQL("INSERT INTO States VALUES ('','set','');" );	    				
    			}
    			cur.moveToFirst();
    			ContentValues values = new ContentValues();
				values.put("Misc1", "1");	
				db.update("States", values, "Misc2 ='set'", null);    	
				cur.close();
    		}*/
		}
		super.onResume();
		
	}
	private void checkonoffline() {
		
		SQLiteDatabase dbtc = openOrCreateDatabase("GiraffeDBv2", 0, null);
		 Cursor ofc = dbtc.rawQuery("SELECT * FROM Offline", null);
		 if (ofc.getCount() > 0)
		 {
			 ofc.moveToFirst();
			 String oval = ofc.getString(ofc.getColumnIndex("Value"));
			 if (oval.equals("1"))
			 {
				
				 Button tvmwu = (Button) findViewById(R.id.mwunlike);
					tvmwu.setText("Offline");
					tvmwu.setTextColor(R.color.redv);
					tvmwu.setBackgroundResource(R.drawable.btn_red_basecurve);
					tvmwu.setVisibility(View.VISIBLE);
			 }
			 else
			 {
				 Button tvmwu = (Button) findViewById(R.id.mwunlike);
					tvmwu.setText("Online");
					tvmwu.setBackgroundResource(R.drawable.btn_green_basecurve);
					tvmwu.setTextColor(R.color.whitev);
					tvmwu.setVisibility(View.VISIBLE);
			 }
		 }
		 ofc.close();
		 dbtc.close();
		
	}
	@Override
	protected void onDestroy() {
		//Intent iminte = new Intent(this, Intemaint.class);
		//startService(iminte);
		
		Log.d("G22","Main window Entered on destroy, stopping luser if possible");
		
		
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		
	   
	       if (keyCode == KeyEvent.KEYCODE_MENU)
	       {
	    	 
	    	   return true;
	       }
	   
	    return super.onKeyDown(keyCode, event);
	}
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainwindow);
       
        final AlertDialog.Builder alertbx = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
        final AlertDialog.Builder alertbx1 = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
        //Log.d("G22","entered mainwindow!"); 
        Button mwunlike = (Button) findViewById(R.id.mwunlike);
        ProgressBar progr = (ProgressBar) findViewById(R.id.mwwait);	
		progr.setVisibility(View.INVISIBLE);
		
		try {
			Date d = new Date();
			Date now = new Date();
			Date oldltu = new Date();
			
			SQLiteDatabase db5 = openOrCreateDatabase("GiraffeDBv2", 0, null);
			Cursor cc3 = db5.rawQuery("SELECT * FROM States", null);
			cc3.moveToFirst();
			String ltu = cc3.getString(cc3.getColumnIndex("Misc4"));
			
			DateFormat formatter ; 
			formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zz yyyy");
			Log.d("G26","chk2");
			try {
				
				oldltu = (Date)formatter.parse(ltu);
			} catch (ParseException e) {
				
				e.printStackTrace();
			} 
			try {
				
				now = (Date)formatter.parse(""+d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			lastupdage = now.getTime() - oldltu.getTime();
			if (lastupdage > 500000000) //><><> should be 432000000
			{
				Toast.makeText(this, "You haven't have Refreshed from a long time, Please Refresh otherwise performance may deteriorate, Goto Menu - Refresh", 5000).show();
				
			}
			cc3.close();
			db5.close();
			
				
		}
		catch (Exception e5)
		{
			
			e5.printStackTrace();
		}
		
        mwunlike.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				try
				{
					SQLiteDatabase db4o2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
					Cursor c = db4o2.rawQuery("SELECT * FROM Offline", null);
					if (c.getCount() > 0)
					{
						c.moveToFirst();
						String val = c.getString(c.getColumnIndex("Value"));
						if (val.equals("1"))
						{
							db4o2.delete("Offline", null, null);
							db4o2.execSQL("INSERT INTO Offline VALUES ('0');");
							Toast.makeText(Mainwindow.this, "Your application is now online", 10000).show();
							Button tvmwu = (Button) findViewById(R.id.mwunlike);
							tvmwu.setVisibility(View.VISIBLE);
							tvmwu.setText("Online");
							tvmwu.setTextColor(R.color.whitev);
							tvmwu.setBackgroundResource(R.drawable.btn_green_basecurve);
						}
						else
						{
							db4o2.delete("Offline", null, null);
							db4o2.execSQL("INSERT INTO Offline VALUES ('1');");
							Toast.makeText(Mainwindow.this, "Your application is now offline", 10000).show();
							Button tvmwu = (Button) findViewById(R.id.mwunlike);
							tvmwu.setVisibility(View.VISIBLE);
							tvmwu.setText("Offline");
							tvmwu.setBackgroundResource(R.drawable.btn_red_basecurve);
							tvmwu.setTextColor(R.color.redv);
							
							
						}
					}
				}
				catch (Exception e) {
					Toast.makeText(Mainwindow.this, "Registration may be invalid!! you have to register again.", 10000).show();
				}				
				
			}
		});
        try {
			TextView tmvi = (TextView) findViewById(R.id.mvindicator);
			tmvi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent mapi = new Intent(Mainwindow.this,Detailmap.class);
					mapi.putExtra("Reg", "");
					startActivity(mapi);
					
				}
			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Cursor c,c2;        
        //finding if the regid/gid is empty
        //if regid is empty, it means either its not registered,i.e. not known to server, or the device has lost the id, though it may be known to server
        //However if any of the regid or gid is empty, the usertable will be resetted, and also the known table to prevent exceptions
        SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
        Cursor msc = db.rawQuery("SELECT * FROM User", null);
        msc.moveToFirst();
        mslat = msc.getString(msc.getColumnIndex("Lat"));
        mslong = msc.getString(msc.getColumnIndex("Long"));
        msc.close();
        
    	
    	TextView t = (TextView) findViewById(R.id.tvaddpeople);
    	t.setSelected(true);
    	t.setText("Click on Name for Details, Long Press on Name to send Messages (Ensure Proper Data connection)");
    		
    	//getting all knows with their name and regno.
    	c2 = db.rawQuery("SELECT * FROM Knowns;", null); 
    	if (c2.getCount() != 0)
    	{
    		
    		//l.setVisibility(View.VISIBLE);
    		c2.moveToFirst();
    		i = 0;
    		do
    		{
    			String oslat = c2.getString(c2.getColumnIndex("Lat"));
    			String oslong = c2.getString(c2.getColumnIndex("Long"));
    			Log.d("G22","oslat = "+oslat+"; oslong = "+oslong);
    			float oslati = Float.parseFloat(oslat);
    			float oslongi = Float.parseFloat(oslong);
    			float disti = (float) 0.0;
    			if (!(oslati < Math.abs(0.2) && oslongi < Math.abs(0.2)))
    			{
	    			disti = distFrom(oslati,oslongi,Float.parseFloat(mslat),Float.parseFloat(mslong));
	    	   	  	disti = (float) (disti * 1.60934);
    			}
    	   	  	
    	   	  	int dist = Math.round(disti);
    			str[i] =  c2.getString(c2.getColumnIndex("Name")) +" (~ "+dist+" Km.)"+ "**1391**"+ c2.getString(c2.getColumnIndex("RegNo"));
    			i++;
    			
    		}while(c2.moveToNext());
    		 Arrays.sort(str, 0, i);
    		for (i1 =0; i1<i;i1++)
    		{
    			i2 = str[i1].indexOf("**1391**");
    			str2[i1] = str[i1].substring(i2+8);
    			str[i1] = str[i1].substring(0, i2);
    			
    		}
    		String sstr[]= new String[i];
    		
    		int ii1;
    		for(ii1 = 0; ii1<i;ii1++)
    		{
    			sstr[ii1] = str[ii1];
    			
    		}
    		for (i1 = i; i1<500;i1++)
    		{
    			str[i1]="";
    			str2[i1]="";
    		}
    		
    		ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sstr);
    		
	        setListAdapter(arr);
    	}
    	else
    	{
    		t.setVisibility(View.VISIBLE);
    		t.setText("Click the menu Button to add people");
    	}
    	c2.close();
    	db.close();
    	
    	ListView list = getListView();
    	
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
	    	@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				/*Toast.makeText(Mainwindow.this,
						"Item in position " + position + " clicked",
						Toast.LENGTH_LONG).show();*/
	    		if (position < i)
	    		{
	    		alertbx1.setTitle("Send Message or Remove?");
	    		alertbx1.setMessage("What do you want to do?, Press on Message button to send Instant Message, Press on Remove button to Remove the user.");
	            alertbx1.setPositiveButton("Remove", new DialogInterface.OnClickListener() 
	            							{
	            								public void onClick(DialogInterface dialog, int arg1) 
	            								{
	    		
											    		alertbx.setTitle("Removing People");
											    		String remname = "";
											    		try
											    		{
											    			remname = str[position].substring(0, str[position].indexOf("(",2));
											    		}
											    		catch (Exception e)
											    		{
											    			Log.d("G22","exception while removing");
											    		}
											    		alertbx.setMessage("Are You Sure to remove "+remname+" from the list?");
											            alertbx.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
											            							{
											            								public void onClick(DialogInterface dialog, int arg1) 
											            								{
											            									
											            									SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
											            									Cursor c = db2.rawQuery("SELECT * FROM User", null);
											            									c.moveToFirst();
											            									String msregid = c.getString(c.getColumnIndex("RegNo"));
											            									c.close();
											            									db2.close();
											            									
											            									HttpParams httpParameters = new BasicHttpParams();
											            									int timeoutConnection = 15000;
											            									HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
											            									int timeoutSocket = 17000;
											            									HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);	    
											            									HttpClient client = new DefaultHttpClient(httpParameters);		            									        									
											            							    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Removeserv");    	
											            							    	try {
											            							    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
											            							    		// Get the deviceID
											            							    		nameValuePairs.add(new BasicNameValuePair("regid", msregid));
											            							    		nameValuePairs.add(new BasicNameValuePair("kregid", str2[position]));
											            							    		Log.d("G22","sended to removeserv from mainwindow remover service, ownregid ="+msregid+" , being removed regid = "+str2[position]);
											            							    		
											            							    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
											            							    		Log.d("G25","Removing Sending to removeserv servlet complete");
											            							    		HttpResponse response = client.execute(post);    		
											            							    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
											            							    		String line = "";
											            							    		while ((line = rd.readLine()) != null) {	            							    			
											            							    			Log.d("G25", "from remove servlet : "+line);
											            							    			String removed = "";
											            							    			try
											            							    			{
											            							    				removed = str[position].substring(0, str[position].indexOf("(",2));
											            							    			}
											            							    			catch (Exception e)
											            							    			{
											            							    				Log.d("G22","ent except while removing");
											            							    			}
											            							    			if (line.equals("505") || line.equals("404") || line.equals("500"))
											            							    			{
											            							    				SQLiteDatabase db3 = openOrCreateDatabase("GiraffeDBv2", 0, null);	            							    				
											            							    				db3.delete("Knowns", "RegNo = "+str2[position], null);
											            							    				db3.delete("Waiting", "RegNo = "+str2[position], null);
											            							    				db3.close();
											            							    				Toast.makeText(Mainwindow.this, removed + " has been successfully removed.", 10000).show();
											            							    				userrem = true;
											            							    				Intent inte = new Intent (Mainwindow.this,main.class);
														            									startActivity(inte);
														            									finish();
											            							    			
											            							    			}
											            							    			else
											            							    			{
											            							    				Toast.makeText(Mainwindow.this, "Problem occured in removing, Try later."+removed, 10000).show();
											            							    			}          							    					    			    			
											            							    			
											            							    		}
											            							    	} 
											            							    	catch(java.net.SocketTimeoutException e4)
											            							    	{
											            							    		Toast.makeText(Mainwindow.this, "Your connection is stale", 10000).show();
											            							    	}
											            							    	
											            							    	catch (IOException e2) {
											            							    		Toast.makeText(Mainwindow.this, "Sorry,Server Error", 10000).show();
											            							    		e2.printStackTrace();	            							    		
											            							    		Log.d("G22", "Entered Exception");	
											            							    	}
											            							    	catch (Exception e3) {
											            							    		Toast.makeText(Mainwindow.this, "Oops Something went wrong...", 10000).show();
											            							    		e3.printStackTrace();	            							    		
											            							    		Log.d("G22", "Entered Exception");	
											            							    	}
											            									dialog.dismiss();
											            									
											            								}
											            							});
											            alertbx.setNegativeButton("No", new DialogInterface.OnClickListener() 
														{
															public void onClick(DialogInterface dialog, int arg1) 
															{
																Toast.makeText(Mainwindow.this, "User Not removed", 10000).show();
																dialog.dismiss();
																
															}
														});
											            alertbx.show();
	            								}		
	            							});
	            alertbx1.setNeutralButton("Message", new DialogInterface.OnClickListener() 
	            {
	            public void onClick(DialogInterface dialog, int arg1) 
				{
	            	Intent intee;
	    			//Log.d("G22","Menu Add user from owner is clicket");
	    			intee = new Intent(Mainwindow.this,Sendmsg.class);
	    			String tor[] = {str2[position],str[position]};
	    			intee.putExtra("toreg", tor);
	    			startActivity(intee);	
						dialog.dismiss();
						
					}
				});
	            
	            
	            try {
					SQLiteDatabase db4o = openOrCreateDatabase("GiraffeDBv2", 0, null);
					Cursor c4o = db4o.rawQuery("SELECT * FROM SO", null);
         
					int otexflag = 0;
					if (c4o.getCount() > 0)
					{
						c4o.moveToFirst();
						do
						{
							if (c4o.getString(c4o.getColumnIndex("RegNo")).equals(str2[position]))
							{
								otexflag = 1;
								alertbx1.setNegativeButton("Go Online to this user", new DialogInterface.OnClickListener() 
					            {
					            public void onClick(DialogInterface dialog, int arg1) 
								{
					            	SQLiteDatabase db4o = openOrCreateDatabase("GiraffeDBv2", 0, null);
					            	db4o.delete("SO", "RegNo = "+str2[position], null);
					            	Toast.makeText(Mainwindow.this, "You are now online to this user.", 10000).show();
					            		
					            	
										dialog.dismiss();
										
									}
					            });
							}
						}while (c4o.moveToNext());
					}
					if (otexflag == 0)
					{
						alertbx1.setNegativeButton("Go Offline to this user", new DialogInterface.OnClickListener() 
					    {
					    public void onClick(DialogInterface dialog, int arg1) 
						{
					    	SQLiteDatabase db4o = openOrCreateDatabase("GiraffeDBv2", 0, null);
					    	db4o.execSQL("INSERT INTO SO VALUES ('"+str2[position]+"','');");
					    	Toast.makeText(Mainwindow.this, "You are now Offline to this user.", 10000).show();
					    		
					    	
								dialog.dismiss();
								
							}
					    });
					}
					
					
					c4o.close();
					
					db4o.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	            alertbx1.show();
	    		// Return true to consume the click event. In this case the
				// onListItemClick listener is not called anymore.
				
			}
	    		return true;
	    	}
	    
		});
	if (userrem == true)
	{
		Intent mainin = new Intent(this, main.class);
		startActivity(mainin);
		finish();
	}
	Button menub = (Button) findViewById(R.id.menubutton);
	menub.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			openOptionsMenu();
			
		}
	});
	
	
    }
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
						
			Toast.makeText(this, "Please Wait..", 15000).show();
			Toast.makeText(this, "Loading, Please Wait..", 15000).show();
			Intent anotherActivityIntent = new Intent(this, Arrowhandler.class);
			String mmm[] = {(String) str2[position],(String) str[position]};
			Log.d("G22","Sended to arrowhandler : "+mmm[0] +"  and: "+mmm[1]);
			anotherActivityIntent.putExtra("ClickedRegNo",mmm);		
			startActivity(anotherActivityIntent);
				
	}

	public boolean cIC() {
    	//Log.d("G22","Entered cic");
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
        	//Log.d("G22","Returned true");  
        	return true;
        } else {              
              //Log.d("G22","Returned false");  
            return false;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menua, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		AlertDialog.Builder alertbox = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
		item.setEnabled(false);
		if(item.getItemId() == R.id.item1)
		{
			//Log.d("G22","Menu Add user from owner is clicked");
			intent = new Intent(Mainwindow.this,Addpeople.class);
			startActivity(intent);
			item.setEnabled(true);
			finish();		
		}
		
		if(item.getItemId() == R.id.update)
		{
			Intent goToMarket = new Intent(Intent.ACTION_VIEW)
		    .setData(Uri.parse("market://details?id=com.visd.Locepeop"));
			startActivity(goToMarket);
			item.setEnabled(true);
		
		}
		
		
		
		if((item.getItemId() == R.id.item4))
		{
			unregcnt++;
			item.setEnabled(true);
			if (cIC() == false)
			{
				//Log.d("G22","Interne not there");
				Toast.makeText(this, "Internet is not there !!", 100000).show();
			}			
			else if (unregcnt == 1)
			{
				Toast.makeText(this, "Really Unregister? You will lose all Information!!", 100000).show();
			}
			else if (unregcnt == 2)
			{
				Toast.makeText(this, "Click once more to Unregister!!", 100000).show();
			}
			
			
			else{
			SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
			Cursor c = db2.rawQuery("SELECT * FROM User;", null); 
			c.moveToFirst();					
			String urno2 = c.getString(c.getColumnIndex("RegNo"));		
			c.close();
			db2.close();
			
			HttpClient client = new DefaultHttpClient();
	    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Unregserv");    	
	    	try {
		    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		    		// Get the deviceID
		    		nameValuePairs.add(new BasicNameValuePair("regid", urno2));
		    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
		    		HttpResponse response = client.execute(post);    		
		    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    		String line = "";
		    		while ((line = rd.readLine()) != null) 
		    		{
		    			//Log.d("G22", line);
		    			if (line.equals("500") || line.equals("404"))
		    			{
		    				SQLiteDatabase db3 = openOrCreateDatabase("GiraffeDBv2", 0, null);
		    				db3.delete("User", null, null);
		    				db3.delete("Knowns", null, null);
		    				db3.delete("Waiting", null, null);
		    				db3.delete("Messages", null, null);
		    				db3.close();
		    				if (line.equals("500"))
		    				{
			    				alertbox.setMessage("Successfully Unregistered !");
					            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
					            							{
					            								public void onClick(DialogInterface dialog, int arg1) 
					            								{
					            									dialog.dismiss();
					            									Intent mainin = new Intent(Mainwindow.this, main.class);
					            				    				startActivity(mainin);
					            									finish();
					            								}
					            							});
					            alertbox.show();	
		    				}
		    				if (line.equals("404"))
		    				{
		    					alertbox.setMessage("You were already Unregistered!");
					            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
					            							{
					            								public void onClick(DialogInterface dialog, int arg1) 
					            								{
					            									dialog.dismiss();
					            									Intent mainin = new Intent(Mainwindow.this, main.class);
					            				    				startActivity(mainin);
					            									finish();
					            								}
					            							});
					            alertbox.show();	
		    				}
		    			}
		    			else
		    			{
		    				Toast.makeText(this, "Error, Failed to Unregister !", 10000).show();
		    				item.setEnabled(true);
		    			}		    			
		    		}
	    	} 
	    	catch (IOException e2) 
	    	{
	    		e2.printStackTrace();
	    		Toast.makeText(this, "Error, Failed to Unregister !", 10000).show();
	    		item.setEnabled(true);
	    		//Log.d("G22", "Entered Exception");	
	    	}
			}
		}
		if((item.getItemId() == R.id.hwtu))
		{
			item.setEnabled(true);
			Intent hwtuinte = new Intent (this, Howtouse.class);
			startActivity(hwtuinte);
		}
		if((item.getItemId() == R.id.msgb))
		{
			item.setEnabled(true);
			Intent hwtuinte = new Intent (this, ListtryActivity.class);
			startActivity(hwtuinte);
		}
		if((item.getItemId() == R.id.gooff))
		{
			item.setEnabled(true);
			
			SQLiteDatabase db4o2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
			Cursor c = db4o2.rawQuery("SELECT * FROM Offline", null);
			if (c.getCount() > 0)
			{
				c.moveToFirst();
				String val = c.getString(c.getColumnIndex("Value"));
				if (val.equals("1")) //in offline state
				{
					db4o2.delete("Offline", null, null);
					db4o2.execSQL("INSERT INTO Offline VALUES ('0');");
					Toast.makeText(Mainwindow.this, "Your application is now online", 10000).show();
					Button tvmwu = (Button) findViewById(R.id.mwunlike);
					tvmwu.setVisibility(View.VISIBLE);
					tvmwu.setText("Online");
					tvmwu.setTextColor(R.color.whitev);
					tvmwu.setBackgroundResource(R.drawable.btn_green_basecurve);
					
					
				}
				else
				{
					db4o2.delete("Offline", null, null);
					db4o2.execSQL("INSERT INTO Offline VALUES ('1');");
					Toast.makeText(Mainwindow.this, "Your application is now offline", 10000).show();
					Button tvmwu = (Button) findViewById(R.id.mwunlike);
					tvmwu.setVisibility(View.VISIBLE);
					tvmwu.setText("Offline");
					tvmwu.setBackgroundResource(R.drawable.btn_red_basecurve);
					tvmwu.setTextColor(R.color.redv);
					
					
				}
					
			}
			c.close();
			db4o2.close();
		}
		
		if((item.getItemId() == R.id.dislike))
		{
			item.setEnabled(true);
			try
			{
				SQLiteDatabase dbmwu = openOrCreateDatabase("GiraffeDBv2", 0, null);
		        Cursor mscmwu = dbmwu.rawQuery("SELECT * FROM User", null);
		        mscmwu.moveToFirst();
		        String regid = mscmwu.getString(mscmwu.getColumnIndex("RegNo"));
				Intent intee;
    			intee = new Intent(Mainwindow.this,Sendmsg.class);
    			String tor[] = {"x"+regid,"Developers"};
    			intee.putExtra("toreg", tor);
    			startActivity(intee);
			}
			catch (Exception e) {
				Toast.makeText(Mainwindow.this, "Registration may be invalid!! you have to register again.", 10000).show();
			}	
		}

		if((item.getItemId() == R.id.mwref))
		{
			item.setEnabled(true);
			if (cIC() == false)
			{
				//Log.d("G22","Interne not there");
				Toast.makeText(this, "Internet is not there !!", 100000).show();
			}
			else
			{
				
				Log.d("G28","Lastupdage = "+lastupdage);
				if (lastupdage > 432000000) //><><> should be 432000000
				{
					refcnt++;
					if (refcnt < 2)
					{
						Toast.makeText(this, "Do you really want to Refresh, DO NOT REFRESH UNNECESSARILY", 100000).show();
					}
					else
					{
						Intent inrefreg = new Intent(Mainwindow.this,Refreshreg.class);
						startActivity(inrefreg);
					}
				}
				else
				{
					Long timeleft = 432000000 - lastupdage;
					Log.d("G28","Time left = "+timeleft);
					Long days = (timeleft/(86400000));
					int day = Math.round(days);
					if (day > 5 || day < 0)
					{
						Toast.makeText(this, "System Date/Time may need to be checked !", 100000).show();
						Intent inrefreg = new Intent(Mainwindow.this,Refreshreg.class);
						startActivity(inrefreg);
					}
					else
					{
						Toast.makeText(this, "You can refresh only once in 5 days !, Time left = "+day +" days", 100000).show();
					}
				}
			}
			
		}
		return super.onOptionsItemSelected(item);
	}
	public float distFrom(float lat1, float lng1, float lat2, float lng2) {
		//Log.d("SH","EF");
	    double er = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = er * c;
	    //Log.d("G22","Tr9");
	    //int meterConversion = 1609;

	    return new Float(dist).floatValue();
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
