package com.visd.Locepeop;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Stopmadluser extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("G25","Entere ind stopmaduser");
		try
		{
			int[] expid = intent.getIntArrayExtra("expid");
			int pid =  expid[0];
			int chkcount = expid[1];
			Log.d("G25","Entere ind stopmaduser with pid = "+pid+"; and chkcount = "+chkcount);
			SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
			ContentValues cv = new ContentValues();
			String fix = "fix";
			cv.put("Misc2", "smlu");
			db.update("States", cv, "Misc1 = '"+fix+"'", null);
			db.close();
			
			int flag = 0;
			if (chkcount > 3)
			{
				Toast.makeText(this, "..", 10000).show();
				if (chkcount > 4 )
				{
					Toast.makeText(this, "Locate People : Unable to Stop GPS sensor, please STOP manually", 10000).show();
					if (iSR())
					{
						Toast.makeText(this, "Locate People : Unable to Stop GPS sensor, please STOP manually", 10000).show();
						Toast.makeText(this, "Locate People : Unable to Stop GPS sensor, please STOP manually", 10000).show();
						Toast.makeText(this, "Locate People : Unable to Stop GPS sensor, please STOP manually", 10000).show();
					}
				}
				flag = 1;
				
			}
	
			Log.d("G25","Stopmadluse is being stopped pre");
			getApplicationContext().stopService(new Intent(getApplicationContext(), Luser.class));
			android.os.Process.killProcess(pid);
			Log.d("G25","Stopmadluse is being stopped");
			getApplicationContext().stopService(new Intent(getApplicationContext(), Stopmadluser.class));
			stopService(intent);
			stopSelf();
			
			return super.onStartCommand(intent, flags, startId);
		}
		catch (Exception e)
		{
			if (iSR())
			{
				Toast.makeText(this, "Locate People : Couldn't get location. Location may not be available.", 3000).show();
				Toast.makeText(this, "Locate People : Couldn't get location. Location may not be available.", 3000).show();
				Toast.makeText(this, "Sensor stopped to save battery,", 3000).show();
				Toast.makeText(this, "Make Sure your settings for use Wireless Network and/or GPS are on.", 3000).show();
				Toast.makeText(this, "Make Sure your settings for use Wireless Network and/or GPS are on.", 3000).show();
				Toast.makeText(this, "Move you device to outdoor to get GPS location.", 10000).show();
				Toast.makeText(this, "Move you device to outdoor to get GPS location.", 3000).show();
			}
			
			
			getApplicationContext().stopService(new Intent(getApplicationContext(), Luser.class));
			getApplicationContext().stopService(new Intent(getApplicationContext(), Stopmadluser.class));
			stopService(intent);
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
			
		}
	}
	 private boolean iSR() {

	        String sClassName;
	        Log.d("G25","isr entreed ");
	        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) 
	        {
	            sClassName = service.service.getClassName();
	            Log.d("G25","isr entreed 2");
	            if (sClassName.contains("com.visd.Locepeop.Luser"))
	            {
	            	Log.d("G25","isr found luser in mian ");
	                  return true;
	                  
	            }
	        }
	        Log.d("G25","isr not found luser in mian called from stopmadluser ");
	        return false;
	    }
	@Override
	public void onDestroy() {
		Log.d("G25","Stop mad user stopped");
		
		super.onDestroy();
	}
}
