package com.visd.Locepeop;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLEncoder;


import javax.net.ssl.HttpsURLConnection;


import org.apache.http.protocol.HTTP;

import android.app.Activity;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Addpeople extends Activity implements Runnable {
	public static Addpeople addpeopleThis = null;
	String kpno = "";
	String urno = "";
	Button b;
	Thread t;
	boolean runned = false;
	
	@Override
	public void onBackPressed() {
		if (runned)
		{
			if (t.isAlive())
			{
				Toast.makeText(Addpeople.this, "Communicating with server please wait...", 10000).show();
			}
			else
			{
				super.onBackPressed();
			}
		}
		else
		{
			t = new Thread(this,"DD");
			super.onBackPressed();
		}
	}
	@Override
	protected void onStop() {
		
		try {
			t.join();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		catch (Exception e) {
			
		}
		super.onStop();
	}
	
	
	public void onCreate(Bundle savedInstanceState) 
    {
		
        super.onCreate(savedInstanceState);
        
       
  		setContentView(R.layout.addpeople);

  		SQLiteDatabase dbLast = openOrCreateDatabase("GiraffeDBv2", 0, null);
		Cursor knownlast = dbLast.rawQuery("SELECT * FROM Knowns;", null);
		if (knownlast.getCount() == 0)
		{
			TextView tv = (TextView) findViewById(R.id.textView2);
			String text = (String) tv.getText();
			text = text + " For getting a feel, you may add developer @ 7204336295 and remove at any time";
			tv.setText(text);
		}
		knownlast.close();
		dbLast.close();
		
       
        b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				b.setEnabled(false);
				EditText e = (EditText) findViewById(R.id.editText1);
				kpno = e.getText().toString().trim();
				//Log.d("G22","The phone num to add is : " + kpno);
				final SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
				Cursor allknowns = db.rawQuery("SELECT * FROM Knowns;", null);
				boolean acflag = true;
				Cursor cc = db.rawQuery("SELECT * FROM User;", null); 
				cc.moveToFirst();
				String phno = cc.getString(cc.getColumnIndex("Pno"));
				cc.close();
				if (kpno.equals(phno))
				{
					Toast.makeText(Addpeople.this, "This is your own number! ", 10000).show();
					b.setEnabled(true);
					acflag = false;
				}
				if (allknowns.getCount()>0)
				{
					allknowns.moveToFirst();
					do
					{
						Log.d("G22","Add people knownpno test, pno retreieve = "+ allknowns.getString(allknowns.getColumnIndex("Pno")).trim());
						String knownpn = allknowns.getString(allknowns.getColumnIndex("Pno")).trim();
						if (kpno.equals(knownpn))
						{
							
							
								Toast.makeText(Addpeople.this, "This Number is already added", 10000).show();
								b.setEnabled(true);
							
							acflag = false;
						}
					}while (allknowns.moveToNext());					
				}
				if (acflag)
				{
					Cursor c = db.rawQuery("SELECT * FROM User;", null); 
					c.moveToFirst();
					urno = c.getString(c.getColumnIndex("RegNo"));
					c.close();
					db.close();
					if (cIC() == false)
					{
						//Log.d("G22","Interne not there");
						Toast.makeText(Addpeople.this, "Internet is not there !!", 100000).show();
					}	
					else
					{
						b.setEnabled(false);
						Toast.makeText(Addpeople.this, "Processing... Please wait..", 10000).show();
					}
					runned = true;
					methd();
				   
					
					
					
					
			    }
				db.close();
			}
			
		});
        
        
    }
	 
	protected void methd() {
		 t = new Thread (Addpeople.this,"intnedcc");
			t.start();
		
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
	protected void onPause() {
		super.onPause();
		addpeopleThis = null;
		
		
	}
	@Override
	protected void onDestroy() {
		if (Luser.luserThis != null)
		{
			Luser.luserThis.stopSelf();
		}
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		addpeopleThis = this;
		//SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
		if (cIC() == false)
		{
			////Log.d("G22","Interne not there");
			Toast.makeText(this, "Trying to open Data Connection..", 100000).show();
			/*ConnectivityManager mgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    		Method dataMtd = null;
			try 
			{
				dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
			}
			catch (SecurityException e2)
			{
				
				////Log.d("G22", "Entered SecurityException");
				e2.printStackTrace();
			} 
			catch (NoSuchMethodException e2) 
			{
				////Log.d("G22", "Entered NoSuchMethodException");
				e2.printStackTrace();
			}
    		dataMtd.setAccessible(true);
    		String suc = null;
    		try 
    		{
				while (!cIC())
				{
					dataMtd.invoke(mgr, true);
					////Log.d("G24","main loopping");
				}
				suc = "1";
				
			} 
    		catch (IllegalArgumentException e1) 
    		{
				suc = "0";
				////Log.d("G22", "Enteredillegealargument Exception");
				e1.printStackTrace();
			} 
    		catch (IllegalAccessException e1) 
    		{
    			suc = "0";
				////Log.d("G22", "Entered illegalaccessException");
				e1.printStackTrace();
			} catch (InvocationTargetException e1) 
			{
				suc = "0";
				////Log.d("G22", "Entered invocationtargetException");
				e1.printStackTrace();
			} 
    		if (!suc.equals("0") && suc.equals("1"))
    		{
    			Cursor cur = db.rawQuery("SELECT * FROM States;", null); 
    			////Log.d("G22","Curcount = "+cur.getCount());
    			if (cur.getCount() == 0)
    			{
    				db.execSQL("INSERT INTO States VALUES ('','set','');" );	    				
    			}
    			cur.moveToFirst();
    			ContentValues values = new ContentValues();
				values.put("Misc1", "1");	
				db.update("States", values, "Misc2 ='set'", null);    	
				cur.close();
    		}
		}*/
	}

}
	@Override
	public void run() {
		 final AlertDialog.Builder alertbox = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom)));
		//HttpPost post = new HttpPost("http://gifserb.appspot.com/Addusers");    	
	    	
 		// Get the deviceID
 		//nameValuePairs.add(new BasicNameValuePair("regId", urno));
 		//nameValuePairs.add(new BasicNameValuePair("kPno", kpno ));
		 
		 
		 /*
		  Addpeople.this.runOnUiThread(new Runnable() {
    		public void run() {
       	 	}
		});

		  */
		 
		 
 		Log.d("G22","todel addpeople 1");
 	    HttpsURLConnection urlc = null;
 	    Log.d("G22","todel addpeople 2");
 	    //OutputStreamWriter out = null;
 	    DataOutputStream dataout = null;
 	    Log.d("G22","todel addpeople 3");
 	    BufferedReader in = null;
 	    Log.d("G22","todel addpeople 4");
 	    try {
 	    	Log.d("G22","todel addpeople 5");
 	        URL url = new URL("https://gifserb.appspot.com/Addusers");
 	        Log.d("G22","todel addpeople 6");
 	        urlc = (HttpsURLConnection) url.openConnection();
 	        Log.d("G22","todel addpeople 7");
 	        urlc.setRequestMethod("POST");
 	        Log.d("G22","todel addpeople 8");
 	        urlc.setDoOutput(true);
 	        urlc.setDoInput(true);
 	        urlc.setUseCaches(false);
 	        urlc.setConnectTimeout(30000);	
 	        urlc.setReadTimeout(28000);
 	       
 	        urlc.setAllowUserInteraction(false);
 	        Log.d("G22","todel addpeople 9");
 	       
 	        urlc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
 	        String output = "regId="+ URLEncoder.encode(urno, HTTP.UTF_8)
 	                +"&kPno="+ URLEncoder.encode(kpno, HTTP.UTF_8);
 	        Log.d("G22","todel addpeople 10");
 	        	        
 	        dataout = new DataOutputStream(urlc.getOutputStream());
 	        Log.d("G22","todel addpeople 11");
 	        // perform POST operation
 	        dataout.writeBytes(output);
 	        Log.d("G22","todel addpeople 12");
 	        // get response info
 	       
 	        // get required headers
 	        /*
 	         HttpResponse response = client.execute(post);    	
 	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
		Log.d("G25","Luser Sending to update loc complete");
		HttpResponse response = client.execute(post);    		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			
 	         */
 	        
 	       

 	        in = new BufferedReader(new InputStreamReader(urlc.getInputStream()),8096);
 	        Log.d("G22","todel addpeople 13");
 	        String line;
 	        
 	        // write html to System.out for debug
 	        while ((line = in.readLine()) != null) {

      
     

 		
 			Log.d("G22","Reply from adduser servlet : "+line);
 			Log.d("G22", line.substring(0, 3));
 			if (line.equals("404"))
 			{
 				Addpeople.this.runOnUiThread(new Runnable() {
 		    		public void run() {
 		       	 

 				alertbox.setTitle("Error");
 				alertbox.setMessage("Sorry, The Phone Number is not found on Server");
		            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
		            							{
		            								public void onClick(DialogInterface dialog, int arg1) 
		            								{
		            									dialog.dismiss();
		            									b.setEnabled(true);					            									
		            								}
		            							});
		            alertbox.show();
		            
 		    		}
 				});
 			}
 			
 			if (line.equals("401"))
 			{
 				Addpeople.this.runOnUiThread(new Runnable() {
 		    		public void run() {
 				alertbox.setTitle("Error");
 				alertbox.setMessage("Sorry, You cannot add this number right now");
		            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
		            							{
		            								public void onClick(DialogInterface dialog, int arg1) 
		            								{
		            									dialog.dismiss();
		            									b.setEnabled(true);					            									
		            								}
		            							});
		            alertbox.show();
 		    		}
 				});
 			}
 			if (line.substring(0, 3).equals("400"))
 			{
 				final String linee = line;
 				Addpeople.this.runOnUiThread(new Runnable() {
 		    		public void run() {
 				String knregid = linee.substring(linee.indexOf("reg..reg")+8, linee.indexOf("nam..nam"));
 				Log.d("G22", "knregid = "+knregid);
 				String knname = linee.substring(linee.indexOf("nam..nam")+8, linee.indexOf("pno..pno"));
 				Log.d("G22", "kname = "+knname);
 				String knpno = linee.substring(linee.indexOf("pno..pno")+8);
 				Log.d("G22", "knpno = "+knpno);
 				float knlat = (float) 0.0;
 				float knlong = (float) 0.0;
 				
 				//01-27 16:36:31.077: DEBUG/G22(31780): Reply from adduser servlet : 500reg..reg10nam..namYogepno..pno13910000

 				SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
 				db2.execSQL("INSERT INTO Knowns VALUES ('"+knregid+"','"+knname+"','"+knlat+"','"+knlong+"','','"+knpno+"');");
 				Log.d("G22","INSERT INTO Knowns VALUES ('"+knregid+"','"+knname+"','"+knlat+"','"+knlong+"','','"+knpno+"');");
 				db2.close();
 				Toast.makeText(Addpeople.this, "Request Already Pending", 10000);
 			}
				});
 						
 				
 			}
 			if (line.substring(0, 3).equals("500"))
 			{
 				final String linee = line;
 				Addpeople.this.runOnUiThread(new Runnable() {
 		    		public void run() {
 				String knregid = linee.substring(linee.indexOf("reg..reg")+8, linee.indexOf("nam..nam"));
 				Log.d("G22", "knregid = "+knregid);
 				String knname = linee.substring(linee.indexOf("nam..nam")+8, linee.indexOf("pno..pno"));
 				Log.d("G22", "kname = "+knname);
 				String knpno = linee.substring(linee.indexOf("pno..pno")+8);
 				Log.d("G22", "knpno = "+knpno);
 				float knlat = (float) 0.0;
 				float knlong = (float) 0.0;
 				
 				//01-27 16:36:31.077: DEBUG/G22(31780): Reply from adduser servlet : 500reg..reg10nam..namYogepno..pno13910000

 				SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
 				db2.execSQL("INSERT INTO Knowns VALUES ('"+knregid+"','"+knname+"','"+knlat+"','"+knlong+"','','"+knpno+"');");
 				Log.d("G22","INSERT INTO Knowns VALUES ('"+knregid+"','"+knname+"','"+knlat+"','"+knlong+"','','"+knpno+"');");
 				db2.close();
 				alertbox.setTitle("Success");
 				alertbox.setMessage("You are added, but you can't get location until user confirms");
		            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
		            							{
		            								public void onClick(DialogInterface dialog, int arg1) 
		            								{
		            									dialog.dismiss();
		            									b.setEnabled(true);	
		            									Intent inte = new Intent (Addpeople.this,main.class);
		            									startActivity(inte);
		            									finish();
		            								}
		            							});
		            alertbox.show();
 		    		}
				});
 						
 				
 			}
 			if (line.substring(0, 3).equals("505"))
 			{
 				final String linee = line;
 				Addpeople.this.runOnUiThread(new Runnable() {
 		    		public void run() {
 				String knregid = linee.substring(linee.indexOf("reg..reg")+8, linee.indexOf("nam..nam"));
 				String knname = linee.substring(linee.indexOf("nam..nam")+8, linee.indexOf("lat..lat"));
 				String knlat = linee.substring(linee.indexOf("lat..lat")+8, linee.indexOf("lon..lon"));
 				String knlong = linee.substring(linee.indexOf("lon..lon")+8, linee.indexOf("pno..pno"));
 				String knpno = linee.substring(linee.indexOf("pno..pno")+8);				    				
 				SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
 				db2.execSQL("INSERT INTO Knowns VALUES ('"+knregid+"','"+knname+"','"+knlat+"','"+knlong+"','','"+knpno+"');");
 				db2.close();
 				alertbox.setTitle("Success");
 				alertbox.setMessage("The User is added !!");
		            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
		            							{
		            								public void onClick(DialogInterface dialog, int arg1) 
		            								{
		            									dialog.dismiss();
		            									b.setEnabled(true);			
		            									Intent inte = new Intent (Addpeople.this,main.class);
		            									startActivity(inte);
		            									finish();
		            								}
		            							});
		            alertbox.show();
 		    		}
				});
 			}
 			
 		}//end of while
 }//end of try block
	
	catch(java.net.SocketTimeoutException e4)
	{
		Addpeople.this.runOnUiThread(new Runnable() {
	    		public void run() {
		Toast.makeText(Addpeople.this, "Your connection is stale", 10000).show();
		b.setEnabled(true);
	    		}});
	}
	catch (IOException e) 
	{
		final Exception e2 = e;
		Addpeople.this.runOnUiThread(new Runnable() {
    		public void run() {
		e2.printStackTrace();
		b.setEnabled(true);
		Toast.makeText(Addpeople.this, "I/O error, Your data connection may be stale", 10000).show();
		Log.d("G22", "Entered ap  I/O Exception"+e2.toString());	
    		}});
	}
	catch (Exception e) 
	{
		final Exception e2 = e;
		Addpeople.this.runOnUiThread(new Runnable() {
    		public void run() {
		Toast.makeText(Addpeople.this, "Your connection is stale..", 10000).show();
		String t = e2.toString();
		e2.printStackTrace();
		b.setEnabled(true);
		Log.d("G22", "Entered ap Exception"+t);
    		}});
	}
 	   Addpeople.this.runOnUiThread(new Runnable() {
   		public void run() {
   			b.setEnabled(true);
   		}});

		
	}
}
