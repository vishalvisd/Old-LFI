package com.visd.Locepeop;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.TextView;

public class C2dmregserv extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{	
		//Log.d("G22", "Entered C2dmregserv");
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
		Cursor c = db.rawQuery("SELECT * FROM User;", null); 
		c.moveToFirst();
		String c2dmid = c.getString(c.getColumnIndex("C2dm"));	
		//Log.d("G22","c2dmid from c2dmregserv :"+c2dmid+"p");
		if (c2dmid.length()<5 || c2dmid == null)
		{
			if (Registration.mThis!=null)
			{
				((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Wait. !" );
			}
			Intent rI = new Intent ("com.google.android.c2dm.intent.REGISTER");
			rI.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
			rI.putExtra("sender", "visd.locepeop@gmail.com"); 
			if (Registration.mThis!=null)
			{
				((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Wait... !" );
			}
			this.startService(rI);			
		}
		else
		{
			if (Registration.mThis!=null)
			{
				((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Pre-Registration Successful !" );
			}
			Intent i = new Intent(this,Sendc2dmreg.class);
	        i.putExtra("regId", c2dmid);	        
	        this.startService(i);
		}
		c.close();
		db.close();
		stopService(intent);		
		return super.onStartCommand(intent, flags, startId);
	}

}
