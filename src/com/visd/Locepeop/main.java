package com.visd.Locepeop;



import java.util.Date;





import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class main extends Activity 
{
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
        Date d = new Date();
       
        db.execSQL("CREATE TABLE IF NOT EXISTS Knowns " +"(RegNo TEXT, Name TEXT, Lat REAL, Long REAL, Tlu DATETIME,Pno TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS States " +"(Misc1 TEXT, Misc2 TEXT, Misc3 TEXT, Misc4 TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS States2 " +"(Misc1 TEXT, Misc2 TEXT, Misc3 TEXT, Misc4 TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Messages " +"(Party TEXT, Tim TEXT, Body TEXT, Flag TEXT, Misc4 TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Waiting " +"(RegNo TEXT, Name TEXT, Lat REAL, Long REAL, Tlu DATETIME,Pno TEXT,Status1 TEXT,Status2 TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS User" +"(RegNo TEXT,Name TEXT,Lat REAL,Long REAL,Tlu DATETIME,Mis1 TEXT,Pno NUMERIC,C2dm VARCHAR,misc TEXT,Mis2 Text,Pass TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS SO " +"(RegNo TEXT, Misc1 TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Offline " +"(Value TEXT);");
        ContentValues valu = new ContentValues();
        valu.put("Name", "LocatePeo");
        db.update("User", valu, "RegNo ='"+23+"'", null);		
        
        SQLiteDatabase db4o2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
		Cursor cdb4o2 = db4o2.rawQuery("SELECT * FROM Offline", null);
		if (cdb4o2.getCount() == 0)
		{
			db4o2.execSQL("INSERT INTO Offline VALUES ('0');");
		}
		cdb4o2.close();
		db4o2.close();
		
       /* db.delete("Knowns", null, null);
        db.execSQL("INSERT INTO Knowns VALUES ('123','jose','35.155596','-80.931848','','890221223');");
        db.execSQL("INSERT INTO Knowns VALUES ('124','sweety','26.55596','75.631848','','890221225');");
        db.execSQL("INSERT INTO Knowns VALUES ('125','jay','12.90882566','74.680681151','','890221229');");
        db.execSQL("INSERT INTO Knowns VALUES ('126','vijay','13.00596','74.7751848','','890221227');"); */
        //db.execSQL("INSERT INTO Waiting VALUES ('124','Suresh','17.22','13.22','','832323243','','');");
        //db.execSQL("INSERT INTO Knowns VALUES ('125','Ganesh','28.36','77.13','','434234224');");
        
        
        //Checking if the user is registered to server       
        Cursor c = db.rawQuery("SELECT * FROM User;", null);        
        int icc = c.getCount();
        Log.d("G22","icc value = "+icc);
        Intent logtable = new Intent(this,Logtable.class);
        startService(logtable);
        if (cIC() == false)
		{
			Log.d("G22","Interne not there");
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
				
				Log.d("G22", "Entered SecurityException");
				e2.printStackTrace();
			} 
			catch (NoSuchMethodException e2) 
			{
				Log.d("G22", "Entered NoSuchMethodException");
				e2.printStackTrace();
			}
    		dataMtd.setAccessible(true);
    		String suc = null;
    		try 
    		{
				while (!cIC())
				{
					dataMtd.invoke(mgr, true);
					Log.d("G24","main loopping");
				}
				suc = "1";
				
			} 
    		catch (IllegalArgumentException e1) 
    		{
				suc = "0";
				Log.d("G22", "Enteredillegealargument Exception");
				e1.printStackTrace();
			} 
    		catch (IllegalAccessException e1) 
    		{
    			suc = "0";
				Log.d("G22", "Entered illegalaccessException");
				e1.printStackTrace();
			} catch (InvocationTargetException e1) 
			{
				suc = "0";
				Log.d("G22", "Entered invocationtargetException");
				e1.printStackTrace();
			} 
    		if (!suc.equals("0") && suc.equals("1"))
    		{
    			Cursor cur = db.rawQuery("SELECT * FROM States;", null); 
    			Log.d("G22","Curcount = "+cur.getCount());
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
        
        if (icc==0)
        {
        	//Not Registered,i.e first launch
        	//Register to C2DM servers 
        	//send the registration no. to the servers
        	//Strat Registration activity        	
        	//and finish*
        	
	    	   	
        	
        	Log.d("G22","Entered in getcount 0");
        	c.close();
        	Intent RegistrationIntent = new Intent(this,Registration.class);
        	startActivity(RegistrationIntent);        	
        	db.close();
        	
        }
        else
        {
        	Log.d("G22","Entered innn");
        	c.moveToFirst();
            String RegNo = c.getString(c.getColumnIndex("RegNo"));
            Log.d("G28","C2dmid right now  = "+c.getString(c.getColumnIndex("C2dm")));
            
            SQLiteDatabase dbtc = openOrCreateDatabase("GiraffeDBv2", 0, null);
            Cursor wc = dbtc.rawQuery("SELECT * FROM Waiting", null);
            Cursor kc = dbtc.rawQuery("SELECT * FROM Knowns", null);
            if(wc.getCount() > 0 && kc.getCount() > 0)
            {
            	wc.moveToFirst();
            	do
            	{
            		kc.moveToFirst();
            		do
            		{
            			if (kc.getString(kc.getColumnIndex("RegNo")).equals(wc.getString(wc.getColumnIndex("RegNo"))))
            			{
            				dbtc.delete("Knowns", "RegNo = "+ kc.getString(kc.getColumnIndex("RegNo")), null);
            			}
            		}
            		while (kc.moveToNext());
            	}while (wc.moveToNext());
            }
            kc.close();
            wc.close();
            dbtc.close();
           
            Log.d("G22", "Regno :" +RegNo);
               	
            c.close();
            if (RegNo.equals("") || RegNo.equals("xxx"))
            {
            	Intent RegistrationIntent = new Intent(this,Registration.class);
            	startActivity(RegistrationIntent);        	
            	db.close();            	
            }
            
            else 
       	 	{
	        	//User is registered 
	        	//Make a list of all users in the group by just considering the Knowns database.
	        	//in the option show what are the available options
            	Intent Waitinglist = new Intent(this, Waitingr.class);
	        	startActivity(Waitinglist);
	        	Log.d("G22","sleep main");
			    try {
			    	Log.d("G22","main sleep");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					
					e.printStackTrace();
				}
            	Log.d("G29","all clear from main, about to continue with mainwin");
            	if (!iSR())
            	{
            		Context con = this;
            		Log.d("G29","Main about to start LUSER x1");
	            	Intent inte = new Intent(this,Luser.class);
	            	Log.d("G29","Main about to start LUSERx2");
	                inte.putExtra("externalregid", "");	
	                Log.d("G29","Main about to start LUSER x3");
	                con.startService(inte);
	                
	                SQLiteDatabase dbs = openOrCreateDatabase("GiraffeDBv2", 0, null);
	    			ContentValues cv = new ContentValues();
	    			String fix = "fix";
	    			cv.put("Misc2", "ok");
	    			dbs.update("States", cv, "Misc1 = '"+fix+"'", null);
	    			dbs.close();
            	}
            	else
            	{
            		SQLiteDatabase dbss = openOrCreateDatabase("GiraffeDBv2", 0, null);
        			Cursor dbssc = dbss.rawQuery("SELECT * FROM States;", null); 
        			if (dbssc.getCount() != 0)
        			{
        				dbssc.moveToFirst();
        				String s = dbssc.getString(dbssc.getColumnIndex("Misc2"));
        				if (s.equals("smlu"))
        				{
        					Toast.makeText(this, "Unable to stop GPS sensor, Please stop from Setting->Running Services or Restart the device", 10000).show();
        				}
        			}
        			dbssc.close();
        			dbss.close();
            	}
            	
	        	
       	 	}  
            db.close();
        }
        
        
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
    /*
    private boolean iSR() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.visd.Locepeop.Luser".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/
    public boolean cIC() {
    	Log.d("G24","Entered cic");
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
        	Log.d("G22","Returned true");  
        	return true;
        } else {              
              Log.d("G24","Returned false");  
            return false;
        }
    }

    @Override
    protected void onPause() {
    	finish();
    	super.onPause();
    }
   
}