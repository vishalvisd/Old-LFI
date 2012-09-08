package com.visd.Locepeop;


import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextThemeWrapper;

public class Justts extends Activity {
	public void onCreate(Bundle savedInstanceState) 
    {
		Log.d("G33","Entered jutts0");
        super.onCreate(savedInstanceState);       
        //setContentView(R.layout.blank);
        String[] arrs = getIntent().getExtras().getStringArray("MSG");	
        Log.d("G33","Entered jutts1");
        Date d = new Date();
        SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Messages " +"(Party TEXT, Tim TEXT, Body TEXT, Flag TEXT, Misc4 TEXT);");
        Cursor c =  db.rawQuery("SELECT * FROM Messages;", null);        
        int icc = c.getCount();
        if (icc > 499)
        {
        	Log.d("G31","entere jutss to delete");
        	c.moveToFirst();
        	String tlu = c.getString(c.getColumnIndex("Tim"));
        	db.delete("Messages", "Tim = "+"'"+tlu+"'", null);
        }
        c.close();
    
      //  String s = [[arrs[0] stringWithFormat:@"CREATE Table '%@' ('Name' 'char(50)','ID' 'integer')",theString]UTF8String];
        String arrs0 = arrs[0].toString().replace("'","''");
       
        db.execSQL("INSERT INTO Messages VALUES ('"+arrs[1]+"','"+d+"','"+arrs0+"','FALSE','');" );
		db.close();
        
        try {
			AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
			if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) 
			{
			       MediaPlayer mp = MediaPlayer.create(this, R.raw.snd);
			       mp.start();
			}
			Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			long pattern[] = new long[] {0, 700, 150, 850};
			v.vibrate(pattern, -1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
     

      
    
    
       

      	    
        AlertDialog.Builder alertbox = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
        Log.d("G22","Mss to show : "+arrs[0]);
        alertbox.setTitle(arrs[1]  + " said : ");
        alertbox.setMessage(arrs[0]);       
        alertbox.setCancelable(true);
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
        							{
        								public void onClick(DialogInterface dialog, int arg1) 
        								{
        									dialog.dismiss();		
        									finish();
        								}
        							});
        alertbox.show();
       
       
        
    }
        

}
