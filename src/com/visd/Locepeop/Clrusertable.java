package com.visd.Locepeop;


import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

public class Clrusertable extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.d("G22","Started Clrusertable service");
		//String op = intent.getExtras().getString("op");
		SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
		/*if (op.equals("a") || op.equals("c"))
		{
			db.delete("User", null, null);
		}
		if (op.equals("b") || op.equals("c"))
		{
			db.delete("Knowns", null, null);
		}*/
		db.delete("User", null, null);
		db.close();
		
		stopService(intent);
		return super.onStartCommand(intent, flags, startId);
	}

}
