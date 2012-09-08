package com.visd.Locepeop;

 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

public class Sendmsg extends Activity implements Runnable
{
	Thread tsendmsg;
	boolean runned = false;
	public static Sendmsg sendmsgThis = null;	
	@Override
	protected void onPause() {
		EditText mess = (EditText) findViewById(R.id.msgtosnd);
		SharedPreferences omsg = getSharedPreferences("MYPREF", 0);
		SharedPreferences.Editor editor = omsg.edit();
		editor.putString("mesg", mess.getText().toString());
		editor.commit();
		sendmsgThis = null;		
		super.onPause();
		finish();
	}
	@Override
	public void onBackPressed() {
		if (runned)
		{
			if (tsendmsg.isAlive())
			{
				Toast.makeText(Sendmsg.this, "Sending please wait, Connection may be slow", 10000).show();
			}
			else
			{
				super.onBackPressed();
			}
		}
		else
		{
			tsendmsg = new Thread(this,"DD");
			super.onBackPressed();
		}
	}
	@Override
	protected void onStop() {
		try {
			tsendmsg.join();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		catch (Exception ew)
		{
			ew.printStackTrace();
		}
		super.onStop();
	}
	@Override
	protected void onResume() {		
		EditText mess = (EditText) findViewById(R.id.msgtosnd);
		SharedPreferences omsg = getSharedPreferences("MYPREF", 0);
		mess.setText(omsg.getString("mesg", ""));
		
		sendmsgThis = this;
		
		if (cIC() == false)
		{
			
			Toast.makeText(Sendmsg.this, "No Internet connectivity..", 100000).show();
			
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
	Button thrb;
	String thrreg,thrmesgg,thrownname, thrmesgg2 ;
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmsg);
        String[] arrs = getIntent().getExtras().getStringArray("toreg");		
		thrreg = arrs[0];
		
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
        Cursor c = db.rawQuery("SELECT * FROM User;", null);         
    	c.moveToFirst();    	
    	thrownname = c.getString(c.getColumnIndex("Name"));
    	c.close();
    	db.close();
		String nme2 = arrs[1];
		String nme = "xxx";
		try
		{
			nme = nme2.substring(0, nme2.indexOf("(",2));
		}
		catch(Exception e)
		{
			Log.d("G22","Somethign");
		}
        TextView tvv = (TextView) findViewById(R.id.toreg);
        final TextView tvv2 = (TextView) findViewById(R.id.status);
        tvv2.setText(".");
        tvv.setText("Sending Messsage to "+nme);
        if (nme2.equals("Developers"))
        {
        	 tvv.setText("I didn't like the App. beacause:-");
        }
        final Button b = (Button) findViewById(R.id.sifm);
        thrb = b;
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				b.setEnabled(false);
				//b.setVisibility(View.INVISIBLE);
				tvv2.setText("Sending...");
				EditText mess = (EditText) findViewById(R.id.msgtosnd);
				String mesgg = mess.getText().toString();//.replace("'", "\'");
				if (mesgg.equals(""))
				{
					Toast.makeText(Sendmsg.this, "Message Empty !", 2000).show();
					b.setEnabled(true);
					b.setVisibility(View.VISIBLE);
				}
				else
				{
					
				
						try {
							mesgg = URLEncoder.encode(mesgg, HTTP.UTF_8);
							thrmesgg = mesgg;
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(Sendmsg.this, "Ooops", 1000).show();
						}
						
						String mesgg2 = mess.getText().toString().replace("'", "''");	
						thrmesgg2 = mesgg2;
						
						if (cIC() == false)
						{
							//Log.d("G22","Interne not there");
							Toast.makeText(Sendmsg.this, "Internet is not there !!", 10000).show();
						}
						else
						{
							b.setEnabled(false);
							Toast.makeText(Sendmsg.this, "Sending... Please wait..", 10000).show();
						}
						runned = true;
						methd2();
						
				}
			}

			
		});
        
        
    }
	private void methd2() {
		tsendmsg = new Thread(Sendmsg.this, "Send");
		tsendmsg.start();
		
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
	public void run() {
		final AlertDialog.Builder alertbox = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
		HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost("http://gifserb.appspot.com/Sndm");    	
    	try {
	    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	    		// Get the deviceID
	    		nameValuePairs.add(new BasicNameValuePair("regId", thrreg));
	    		nameValuePairs.add(new BasicNameValuePair("ms", thrmesgg));
	    		nameValuePairs.add(new BasicNameValuePair("ownname", thrownname));
	    		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    		
	    		HttpResponse response = client.execute(post);    		
	    		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    		String line = "";
	    		while ((line = rd.readLine()) != null) 
	    		{
	    			//Log.d("G22", line);
	    			final String linee = line;
	    			Sendmsg.this.runOnUiThread(new Runnable() {
	               		public void run() {
	    			if (linee.equals("Sended"))
	    			{
	    				alertbox.setMessage("Message Sended !!");
			            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
			            							{
			            								public void onClick(DialogInterface dialog, int arg1) 
			            								{
			            									dialog.dismiss();	
			            									
			            								}
			            							});
			            alertbox.show();
			            Date d = new Date();
			            SQLiteDatabase dbv2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
			            dbv2.execSQL("CREATE TABLE IF NOT EXISTS Messages " +"(Party TEXT, Tim TEXT, Body TEXT, Flag TEXT, Misc4 TEXT);");
			            Cursor cv2 =  dbv2.rawQuery("SELECT * FROM Messages;", null);        
			            //finding the sended person name..
			            String sendname = "Developers";
			            Cursor ccv2 = dbv2.rawQuery("SELECT * FROM Knowns", null);
			            if (ccv2.getCount()>0)
			            {
			            	ccv2.moveToFirst();
			            	do
			            	{					            	
				            	if (ccv2.getString(ccv2.getColumnIndex("RegNo")).equals(thrreg))
				            	{
				            		sendname =  ccv2.getString(ccv2.getColumnIndex("Name"));
				            	}
			            	}while(ccv2.moveToNext());
			            	ccv2.close();
			            }
			            //finding the sended person name complete
			            int icc = cv2.getCount();
			            if (icc > 499)
			            {
			            	cv2.moveToFirst();
			            	Log.d("G31","entere snds to delete");
			            	String tlu = cv2.getString(cv2.getColumnIndex("Tim"));
			            	dbv2.delete("Messages", "Tim = "+"'"+tlu+"'", null);
			            }
			            cv2.close();
			            dbv2.execSQL("INSERT INTO Messages VALUES ('"+"YOU -> "+sendname+" ','"+d+"','"+thrmesgg2+"','TRUE','');" );
			    		dbv2.close();
	    			}
	               		}});	
	    		}
    		}
    	catch (IOException e2) 
    	{
    		e2.printStackTrace();
    		Sendmsg.this.runOnUiThread(new Runnable() {
           		public void run() {
           			thrb.setVisibility(View.VISIBLE);
            		thrb.setEnabled(true);
           		}});
    		
    		//Log.d("G22", "Entered Exception");	
    	}	
    	Sendmsg.this.runOnUiThread(new Runnable() {
       		public void run() {
       			thrb.setVisibility(View.VISIBLE);
       			thrb.setEnabled(true);
       		}});
    	
		
	}

}
