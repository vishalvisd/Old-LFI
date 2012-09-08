package com.visd.Locepeop;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;

public class Firstdisplay extends Activity {

	
		public void onCreate(Bundle savedInstanceState) 
	    {
			Log.d("G33","Entered Firstdisplay");
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.blank);
		String sender = "TEAM LOCATE FRIENDS INSTANTLY";
		Date d = new Date();
		String mes = "Thank you for installing Locate Friends Instantly." +
				" Imagine an application, which when you open, " +
				"you get to know about where are all your friends, relatives, etc, " +
				"and the information is so accurate that you can feel that you are seeing everyone. " +
				"You can plan your visit or can know " +
				"which friend is nearest to you. You can track you lost mobile and much much more. You have to use it to beleive it." +
				"Its superfast and you never get any stale information. And its the only application which achieve this by " +
				"making peer to peer direct connection with the other user's phone so you always get the fastest and most reliable information for free. " +
				" Also you can always send unlimited messages which are delivered at lightning speed." +
					"All you need is to just ask your friend to get the Applcation too so that you can add him/her " +
					"(Market search text : “Locate Friends Instnatly com.visd”). Now you have the Power never ever before. " +
					"Thank you, Team Locate Frineds Instantly";       
		 SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
	        db.execSQL("CREATE TABLE IF NOT EXISTS Messages " +"(Party TEXT, Tim TEXT, Body TEXT, Flag TEXT, Misc4 TEXT);");
	        Cursor c =  db.rawQuery("SELECT * FROM Messages;", null);        
	        int icc = c.getCount();
	        if (icc > 99)
	        {
	        	Log.d("G31","entere jutss to delete from firstdispaly !");
	        	c.moveToFirst();
	        	String tlu = c.getString(c.getColumnIndex("Tim"));
	        	db.delete("Messages", "Tim = "+"'"+tlu+"'", null);
	        }
	        c.close();
	    
	      //  String s = [[arrs[0] stringWithFormat:@"CREATE Table '%@' ('Name' 'char(50)','ID' 'integer')",theString]UTF8String];
	        String arrs0 = mes.toString().replace("'","''");
	       
	        db.execSQL("INSERT INTO Messages VALUES ('"+sender+"','"+d+"','"+arrs0+"','FALSE','');" );
			db.close();
		
		
		
		 AlertDialog.Builder alertbox = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom))); 
	        
	        alertbox.setTitle(sender);
	        alertbox.setMessage(mes);
	        alertbox.setCancelable(false);
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

