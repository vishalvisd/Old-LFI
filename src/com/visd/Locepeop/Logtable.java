package com.visd.Locepeop;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class Logtable extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);  		
		Cursor cc2 = db2.rawQuery("SELECT * FROM User", null);
		if(cc2.getCount() > 0)
		{
			Log.d("G22","Details as retreived from Database (This is the present user table) :-");
			cc2.moveToFirst();    	   
			do
			{
				Log.i("G22", "Name :"+cc2.getString(cc2.getColumnIndex("Name")));
				Log.i("G22", "Phone Num :"+cc2.getString(cc2.getColumnIndex("Pno")));
				Log.i("G22", "C2dm id :"+cc2.getString(cc2.getColumnIndex("C2dm")));
				Log.i("G22", "Lat :"+cc2.getFloat(cc2.getColumnIndex("Lat")));
				Log.i("G22", "Long :"+cc2.getFloat(cc2.getColumnIndex("Long")));
				Log.i("G22","Reg Id :"+cc2.getString(cc2.getColumnIndex("RegNo")));
				Log.i("G22", "Tlu Value :"+cc2.getString(cc2.getColumnIndex("Tlu")));
				Log.i("G22","Mis1 :"+cc2.getString(cc2.getColumnIndex("Mis1")));
				Log.i("G22","Mis2 value :"+cc2.getString(cc2.getColumnIndex("Mis2")));
				Log.i("G22","Misc value :"+cc2.getString(cc2.getColumnIndex("misc")));
			} while (cc2.moveToNext());	
		}
		cc2.close();
		Log.d("G22","Logtable : pass1");
		Cursor cc3 = db2.rawQuery("SELECT * FROM Knowns", null);
		if(cc3.getCount() > 0)
		{
			cc3.moveToFirst();		    			
			Log.d("G22","Knowns table details :-");
			do
			{
				Log.i("G22","New Known Deatail:");
				Log.i("G22", "Regno :"+cc3.getString(cc3.getColumnIndex("RegNo")));
				Log.i("G22", "Name :"+cc3.getString(cc3.getColumnIndex("Name")));
				Log.i("G22", "Lat value :"+cc3.getString(cc3.getColumnIndex("Lat")));
				Log.i("G22", "Long value :"+cc3.getString(cc3.getColumnIndex("Long")));
				Log.i("G22", "Tlu value :"+cc3.getString(cc3.getColumnIndex("Tlu")));
				Log.i("G22", "Phone Number :"+cc3.getString(cc3.getColumnIndex("Pno")));
			}while (cc3.moveToNext());
		}
		cc3.close();
		Log.d("G22","Logtable : pass2");
		Cursor cc4 = db2.rawQuery("SELECT * FROM Waiting", null);
		if(cc4.getCount() > 0)
		{
			cc4.moveToFirst();		    			
			Log.d("G22","Waiting table details :-");
			do
			{
				Log.i("G22","New Waiting Deatail:");
				Log.i("G22", "Regno :"+cc4.getString(cc4.getColumnIndex("RegNo")));
				Log.i("G22", "Name :"+cc4.getString(cc4.getColumnIndex("Name")));
				Log.i("G22", "Lat value :"+cc4.getString(cc4.getColumnIndex("Lat")));
				Log.i("G22", "Long value :"+cc4.getString(cc4.getColumnIndex("Long")));
				Log.i("G22", "Tlu value :"+cc4.getString(cc4.getColumnIndex("Tlu")));
				Log.i("G22", "Phone Number :"+cc4.getString(cc4.getColumnIndex("Pno")));
			}while (cc4.moveToNext());
		}
		cc4.close();
		db2.close();
		Log.d("G22","Logtable : pass3");
		stopService(intent);
		return super.onStartCommand(intent, flags, startId);
	}

}
