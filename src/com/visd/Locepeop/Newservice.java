package com.visd.Locepeop;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Newservice extends Service {
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("G33","Enereed newservice");
		Intent ne = new Intent (this, Firstdisplay.class);
		ne.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
		startActivity(ne);
		
		stopService(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	

}
