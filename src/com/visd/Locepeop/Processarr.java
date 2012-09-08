package com.visd.Locepeop;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Processarr extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public int onStartCommand (Intent intent, int flags, int startId)
	{
		boolean flag1 = false;
		try
		{
			String code = intent.getStringExtra("code").toString();
			Log.w("G22","Entered in processarr, code = "+code);
			String caller = code.substring(0,3);
			String rescode = code.substring(3, 6);
			if (rescode.equals("399") || rescode.equals("404"))
			{
				String kregid = code.substring(code.indexOf("bbbbbb")+6);
				SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
				db.delete("Waiting", "RegNo = "+kregid, null);    
				db.delete("Knowns", "RegNo = "+kregid, null);   
				db.close();
				if (Mainwindow.mainwindowThis != null)
				{
					Mainwindow.mainwindowThis.finish();
				}
				if (Arrowhandler.aThis !=null)
				{
					Arrowhandler.aThis.finish();
				}
				Toast.makeText(this, "User to add has Unregistered, List updated", 10000);
				Intent mainint = new Intent(this, main.class);
				startActivity(mainint);
			}
			else if (rescode.equals("500") || rescode.equals("501"))
			{
				String kregid = code.substring(code.indexOf("bbbbbb")+6, code.indexOf("qqqqqq"));
				String klati  = code.substring(code.indexOf("qqqqqq")+6, code.indexOf("zzzzzz"));
				String klongi = code.substring(code.indexOf("zzzzzz")+6);	
				String kname = "";
				String kpno = "";
				boolean flag = false;
				SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
				ContentValues value1 = new ContentValues();
				ContentValues value2 = new ContentValues();		    			
				value1.put("Lat", klati);
				value2.put("Long", klongi);
				
				if (caller.equals("505"))
				{
					db.update("Knowns", value1, "RegNo = "+kregid, null);
				}
				else if (caller.equals("500"))
				{
					Cursor qw = db.rawQuery("SELECT * FROM Waiting", null);
					qw.moveToFirst();
					do
					{
						if (qw.getString(qw.getColumnIndex("RegNo")).equals(kregid))
						{
							kname = qw.getString(qw.getColumnIndex("Name"));
							kpno  = qw.getString(qw.getColumnIndex("Pno"));
							db.delete("Waiting", "RegNo = "+kregid, null);  
							flag = true;
						}
					}while (qw.moveToNext());
					if (flag == false)
					{
						Toast.makeText(this, "Failed to Approve", 10000);
					}
					else
					{
						//RegNo TEXT, Name TEXT, Lat REAL, Long REAL, Tlu DATETIME,Pno TEXT
						db.execSQL("INSERT INTO Knowns VALUES ('"+kregid+"','"+kname+"','"+klati+"','"+klongi+"','','"+kpno+"');" );
					}
				}
			}
			else
			{
				Toast.makeText(this, "One Error occured ! (code = processar)", 10000);
				Log.e("G22","One Error occured ! (code = processar)");
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Sorry, Exception! (code = processar)", 10000);
			Log.e("G22","Sorry, Exception! (code = processar)");
		}
		//Log.d("G22","code.sub... = "+code.substring(0, 3));
		/*if (code.substring(0, 3).equals("404"))
		{			
			SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
			db.delete("Waiting", "RegNo = "+kregid, null);    
			db.delete("Knowns", "RegNo = "+kregid, null);   
			db.close();
			Toast.makeText(this, "User to remove has Unregistered, List updated", 10000);
			
		}
		if (code.substring(0, 3).equals("400"))
		{
			
			SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
			db2.delete("Waiting", "RegNo = "+kregid, null);    
			db2.delete("Knowns", "RegNo = "+kregid, null);   
			db2.close();
			Toast.makeText(this, "User has removed you, so cannot add", 10000);
			Toast.makeText(this, "List Updated", 10000);
			
		}
		if (code.substring(0, 3).equals("500"))
		{
			Log.d("G22","Entered in 500 processar");
			String lati = code.substring(3, code.indexOf("ppp"));
			String longi = code.substring(code.indexOf("ppp")+3, code.indexOf("qqq"));
			SQLiteDatabase db3 = openOrCreateDatabase("GiraffeDBv2", 0, null);
			Cursor c = db3.rawQuery("SELECT * FROM Knowns", null);
			if (c.getCount()>0)
			{
				c.moveToFirst();
				do
				{
					if (c.getString(c.getColumnIndex("RegNo")).equals(kregid));
					{
						ContentValues value1 = new ContentValues();
						ContentValues value2 = new ContentValues();		    			
						value1.put("Lat", lati);
						value2.put("Long", longi);
						db3.update("Knowns", value1, "RegNo ='"+kregid+"'", null);
						db3.update("Knowns", value2, "RegNo ='"+kregid+"'", null);
						Log.d("G22","Database update with new location for regno. : "+kregid+" and lati ="+lati+", longi = "+longi);
						flag1 = true;
					}
				}while(c.moveToNext());
			}
			c.close();
			Cursor c2 = db3.rawQuery("SELECT * FROM Waiting", null);
			if (c2.getCount()>0)
			{
				c2.moveToFirst();
				do
				{
					if (c2.getString(c2.getColumnIndex("RegNo")).equals(kregid));
					{
						if (flag1 == false)						
						{
							
							String knregid = c2.getString(c2.getColumnIndex("RegNo"));
							String knname = c2.getString(c2.getColumnIndex("Name"));
							String knlat = c2.getString(c2.getColumnIndex("Lat"));
							String knlong = c2.getString(c2.getColumnIndex("Long"));
							String knpno = c2.getString(c2.getColumnIndex("Pno"));
							db3.execSQL("INSERT INTO Knowns VALUES ('"+knregid+"','"+knname+"','"+knlat+"','"+knlong+"','','"+knpno+"');");
							
						}
						db3.delete("Waiting", "RegNo = "+kregid, null);
					}
					
				}while(c2.moveToNext());
			}
			Toast.makeText(this, "User Successfully Added", 10000);
		}*/
	    stopService(intent);
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		Log.d("G22","Procesar is destroyed");
		super.onDestroy();
	}
}
