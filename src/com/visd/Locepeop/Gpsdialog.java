package com.visd.Locepeop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

public class Gpsdialog extends Activity {
	AlertDialog.Builder builder;
	int flag = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("G22","Entered gps dialog");
		Bundle recdData = getIntent().getExtras();
		String ext = recdData.getString("ext");
		String wh = "";
		if (ext.equals("1"))
		{
			wh = "GIRAFFE : Your Network Location service seems to be disabled, do you want to enable it?";
		
		
			Log.d("G22","Entered builder 1");
		    builder = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
		    Log.d("G22","Entered builder 11");
		    builder.setMessage(wh)
		           .setCancelable(false)
		           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               @Override
					public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		            	   Log.d("G22","Entered builder 12");
		            	   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); 
		            	   flag = 1;
		            	   finish();
		               }
		           })
		           .setNegativeButton("No", new DialogInterface.OnClickListener() {
		               @Override
					public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		            	   Log.d("G22","Entered builder 13"); 
		            	   dialog.cancel();
		            	   finish();
		               }
		           });
		    Log.d("G22","Entered builder 14");
		    final AlertDialog alert = builder.create();
		    Log.d("G22","Entered builder 15");
		    alert.show();
		    Log.d("G22","Entered builder 16");
		}
		else
		{
			finish();
		}
		    
		
	}
	@Override
	protected void onPause() {
		Log.d("G22","GPS Dialog getting destroyed");
		if (flag == 0)
			{Toast.makeText(this, "Please Turn On GPS for good performance..", 10000).show();}
		finish();
		super.onPause();
	}
}
