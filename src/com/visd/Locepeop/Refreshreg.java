package com.visd.Locepeop;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Refreshreg extends Activity {
	public static Refreshreg refregThis = null;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Log.d("G33","entered Refresreg!");        
        setContentView(R.layout.refreshreg); 
        
        Intent rI = new Intent ("com.google.android.c2dm.intent.REGISTER");
		rI.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		rI.putExtra("sender", "visd.locepeop@gmail.com"); 
		this.startService(rI);		
    }
	@Override
	protected void onDestroy() {
		refregThis = null;
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		refregThis = this;
		super.onResume();
	}

}
