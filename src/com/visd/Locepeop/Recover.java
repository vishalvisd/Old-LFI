package com.visd.Locepeop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Recover extends Activity {
	
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
      	this.requestWindowFeature(Window.FEATURE_NO_TITLE);

  		//Remove notification bar
  		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  		setContentView(R.layout.recover);

  		 SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
  		Cursor c = db.rawQuery("SELECT * FROM User;", null);        
        final int icc = c.getCount();
        c.close();
        db.close();
        Button b = (Button) findViewById(R.id.recbut);
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (cIC() == true)
				{
					
					AlertDialog.Builder alertbox = new AlertDialog.Builder((new ContextThemeWrapper(Recover.this,R.style.AlertDialogCustom)));
					alertbox.setTitle("Support");
						if (icc == 0) {
							EditText et1 = (EditText) findViewById(R.id.editText1);
							EditText et2 = (EditText) findViewById(R.id.editText2);
							EditText et3 = (EditText) findViewById(R.id.editText3);
							String pno = et1.getText().toString();
							String pass = et2.getText().toString();
							String name = et3.getText().toString();
							if (pass.equals("")) {
								pass = "12345677";
							}
							HttpClient client = new DefaultHttpClient();
							HttpPost post = new HttpPost(
									"http://gifserb.appspot.com/Recoverserv");
							try {
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										3);
								// Get the deviceID
								nameValuePairs.add(new BasicNameValuePair(
										"pno", pno));
								nameValuePairs.add(new BasicNameValuePair(
										"pass", pass));
								nameValuePairs.add(new BasicNameValuePair(
										"name", name));

								post.setEntity(new UrlEncodedFormEntity(
										nameValuePairs));

								HttpResponse response = client.execute(post);
								BufferedReader rd = new BufferedReader(
										new InputStreamReader(response
												.getEntity().getContent()));
								String line = "";
								while ((line = rd.readLine()) != null) {

									Log.d("G25", "from Removeserv servlet : "
											+ line);
									
									if (line.equals("500")) {

										alertbox.setMessage("Congrats, the old number is removed, You may now register with that number, Thank you.");
										alertbox.setNeutralButton(
												"Ok",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int arg1) {
														dialog.dismiss();
														finish();
													}
												});
										alertbox.show();
									} else {
										alertbox.setMessage("Sorry, either Number/Name/Password is wrong or communication failure. You may try again.");
										alertbox.setNeutralButton(
												"Ok",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int arg1) {
														dialog.dismiss();

													}
												});
										alertbox.show();
									}

								}
							} catch (IOException e2) {
								e2.printStackTrace();
								Toast.makeText(Recover.this,
										"Connection Problem or Server Error!",
										5000).show();
								Log.d("G22", "Entered Luser Exception");
							}
						}
						else
						{
							Toast.makeText(Recover.this,"Ooops seems you are already registered !", 5000).show();
							alertbox.setMessage("oops seems you are already registered ! , You have to get unregisterd first, if it comes again and again uninstall and then reinstall or use different number.");
							alertbox.setNeutralButton(
									"Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int arg1) {
											dialog.dismiss();

										}
									});
							alertbox.show();
						}
						
						
					}
				else
				{
					Toast.makeText(Recover.this, "No Data connection !", 5000).show();
				}
			}
		});
        
    }
	 public boolean cIC() {
	    	Log.d("G22","Entered cic of registration");
	        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
	        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
	        	Log.d("G22","Returned true registration");  
	        	return true;
	        } else {              
	              Log.d("G22","Returned false registration");  
	            return false;
	        }
	     }

}
