package com.visd.Locepeop;


import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends Activity 
{
    
    public static Registration mThis = null;
    LocationManager mlocManager;
	LocationListener mlocListener;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	float lati,longi;
	int flagcount = 0;
	int bigcount = 0;
	int chkcount = 0;
	float globallati = (float)0.0,globallongi=(float)0.0;
	long pn;
	String un,ps;	
	boolean isclicked = false;
	Button regclick;
	Button rgmenu;
	int flag2 = 1;
	TextView t; 
	Location lastnk = null;
	Location lastgk = null;
	PowerManager powerManager;
	private PowerManager.WakeLock wakeLock;
	String ttexx = "";
	SQLiteDatabase db;//= openOrCreateDatabase("GiraffeDBv2", 0, null);
	//public static Object mThis;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Log.d("G22","entered registration!");        
           
       
  		setContentView(R.layout.registration); 

        db = openOrCreateDatabase("GiraffeDBv2", 0, null);
        db.delete("User", null, null);
        db.delete("Knowns",null,null);
        db.delete("Waiting",null,null);
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "dim Wake Lock");
        final AlertDialog.Builder alertbox = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom)));   
        regclick = (Button) findViewById(R.id.button1);
        rgmenu = (Button) findViewById(R.id.regmenu);
        t = (TextView) findViewById(R.id.textView5);
        TextView sc = (TextView) findViewById(R.id.textView3);
        sc.setSelected(true);
        final EditText pstv = (EditText) findViewById(R.id.regpas);
		//pstv.setText("123456778");
        boolean b = cIC();
        
        if (b!=true)
        {       	
        	regclick.setEnabled(false);
        	t.setText("No Packet Data connection (No Internet connection)");       	
        	
        }
        Button menub = (Button) findViewById(R.id.regmenu);
    	menub.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			openOptionsMenu();
    			
    		}
    	});
        t.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String textt = t.getText().toString();
				if (textt.equals("Number Unaccepted."))
				{
					t.setVisibility(View.INVISIBLE);
					ttexx = "Number unaccepted hence";
					regclick.setVisibility(View.VISIBLE);	
					regclick.setEnabled(true);
					rgmenu.setVisibility(View.VISIBLE);	
					rgmenu.setEnabled(true);
					
					alertbox.setMessage("This Phone number already exists on server, please use different num");
		            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
		            							{
		            								public void onClick(DialogInterface dialog, int arg1) 
		            								{
		            									dialog.dismiss();
		            									t.setVisibility(View.VISIBLE);		            									
		            								}
		            							});
		            alertbox.show();
				}
				if (textt.equals("Your Device is Registed Successfully !!"))
				{
					Log.d("G22","REGISTRATION says : Device successfully registered to server, Registration completed");	            	
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
        final AlertDialog.Builder alertb = new AlertDialog.Builder((new ContextThemeWrapper(this, R.style.AlertDialogCustom)));
       
        Log.d("G26","entered registration! 1");        
        regclick.setOnClickListener(new OnClickListener() 
        							{
			
										@Override
										public void onClick(View arg0) 
										{
											t.setVisibility(View.VISIBLE);	
											isclicked = true;
											EditText pntv = (EditText) findViewById(R.id.editText1);
											EditText untv = (EditText) findViewById(R.id.editText2);
											//EditText pstv = (EditText) findViewById(R.id.regpas);
											//pstv.setText("123456778");
											un = untv.getText().toString().trim();	
											ps = pstv.getText().toString().trim();
											if (pntv.getText().toString().trim().equals(""))
											{
												pntv.setText("0");
												pntv.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
											}
											else if (!isNumeric(pntv.getText().toString()))
											{
												pntv.setText("0");												
												pntv.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
											}
											else if (pntv.getText().toString().trim().length()<8)
											{
												pntv.setText("0");	
												pntv.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
											}
											else
											{
												pntv.setBackgroundColor(getResources().getColor(android.R.color.white));
											}
											if (un.equals(""))
											{
												untv.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
											}
											else
											{
												untv.setBackgroundColor(getResources().getColor(android.R.color.white));
											}
											if (ps.equals("") ||  ps.length() <8)
											{
												pstv.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
											}
											else
											{
												pstv.setBackgroundColor(getResources().getColor(android.R.color.white));
											}
											String spn = pntv.getText().toString().trim();	
											pn = Long.parseLong(spn);
											if (ps.length() <8)
											{
												alertbox.setMessage("Password Min. length 8");
									            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
									            							{
									            								public void onClick(DialogInterface dialog, int arg1) 
									            								{
									            									dialog.dismiss();
									            								}
									            							});
									            alertbox.show();
											}
											else if (spn.equals("0") || un.equals("") || ps.equals(""))
											{
												Log.d("G22","Registration entered empty field");
									            alertbox.setMessage("Entry seems invalid, you have to fill both Nickname Phone Number field, Number is to atleast 3 digit");
									            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
									            							{
									            								public void onClick(DialogInterface dialog, int arg1) 
									            								{
									            									dialog.dismiss();
									            								}
									            							});
									            alertbox.show();
											}
											else if (un.length()>10)
											{
												alertbox.setMessage("Nick name should be less than 10 characters");
									            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
									            							{
									            								public void onClick(DialogInterface dialog, int arg1) 
									            								{
									            									dialog.dismiss();
									            								}
									            							});
									            alertbox.show();
											}
											else
											{
												
												
												regclick.setEnabled(false);
												regclick.setVisibility(View.INVISIBLE);	
												rgmenu.setEnabled(false);
												rgmenu.setVisibility(View.INVISIBLE);
												db = openOrCreateDatabase("GiraffeDBv2", 0, null);
												Cursor c = db.rawQuery("SELECT * FROM User;", null);    											
										        int icc = c.getCount();
										        String r = "";
										        if (icc != 0)
										        {
										        	c.moveToFirst();
										        	r = c.getString(c.getColumnIndex("RegNo"));										        	
										        }
										        c.close();
										        Log.d("G22","Registration icc :"+icc);
										        if (icc != 0 && !(ttexx.equals("Number unaccepted hence")) && !(r.equals("")))
										        {
										        	alertbox.setMessage("Oopsi.. Already registerd!!! or Request forwared, if comes again and again please check network connection");
										            alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
										            							{
										            								public void onClick(DialogInterface dialog, int arg1) 
										            								{
										            									finish();
										            								}
										            							});
										            alertbox.show();										           
										        }
										        else if (!(ttexx.equals("Number unaccepted hence")))
										        {
										        	alertb.setMessage("Registration is bit complex task so it may take some time. Its likely you might fail to register in one attempt, in that case please retry. During Registration You should keep the GPS sensor ON and please ENSURE PROPER DATA CONNECTION, Ensure Proper Registration for proper performance");
										            alertb.setTitle("Starting registration, Please be patient in the process");
										            alertb.setNeutralButton("Ok", new DialogInterface.OnClickListener() 
										            							{
										            								public void onClick(DialogInterface dialog, int arg1) 
										            								{
										            								
										            									dialog.dismiss();
										            									
																			            try {
																							Thread.sleep(700);
																						} catch (InterruptedException e1) {
																							// TODO Auto-generated catch block
																							e1.printStackTrace();
																						}
																			            
																			            
										            									
										            									
										            									try {
																							Thread.sleep(200);
																						} catch (InterruptedException e) {
																							// TODO Auto-generated catch block
																							e.printStackTrace();
																						}
										            									t = (TextView) findViewById(R.id.textView5);	
																			        	t.setText("Finding your Location");										        	
																						mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
																					    mlocListener = new MyLocationListener();
																					    if (!mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) 
																					    {
																				        	  boolean xyz = buildAlertMessageNoGps(1);
																				        }
																				        if(!mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
																				        {
																				        	 boolean xy =buildAlertMessageNoGps(3);
																				        }									    
																					    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
																					    String locationProvider = LocationManager.NETWORK_PROVIDER;
																					    String locationProviderg = LocationManager.GPS_PROVIDER;
																					    lastnk = mlocManager.getLastKnownLocation(locationProvider);
																					    lastgk = mlocManager.getLastKnownLocation(locationProviderg);
																					    if (lastgk == null)
																					    {
																						    try {
																								Thread.sleep(10000);
																							} catch (InterruptedException e) {
																								// TODO Auto-generated catch block
																								e.printStackTrace();
																							}
																					    }
																					    mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);												    
																					    Log.d("G22","Last Known Network Location :" +lastnk);
																					    Log.d("G22","Last known gps location : "+lastgk);
																					    
																					    flag2 = 2;
										            								}
										            							});
										            alertb.show();	
										        	
										        }
										        else if (ttexx.equals("Number unaccepted hence"))
										        {
										        	ttexx = "flagoff";
										            ContentValues values = new ContentValues();
										    		ContentValues values2 = new ContentValues();
										    		ContentValues values3 = new ContentValues();
										    		values.put("Name", un);
										    		values2.put("Pno", pn);
										    		values3.put("Pass", ps);
										    		Cursor c6 = db.rawQuery("SELECT * FROM User;", null); 
										    		Log.d("G22","REGISTRATION: Entered in nuh and after c6");
										    		c6.moveToFirst();
										    		String on = c6.getString(c6.getColumnIndex("Mis2"));	
										    		db.update("User", values, "Mis2 ='"+on+"'", null);
										    		db.update("User", values2, "Mis2 ='"+on+"'", null);		
										    		db.update("User", values3, "Mis2 ='"+on+"'", null);		
										            c6.close();
										            Log.d("G22","REGISTRATION nuh: About to start c2dmregserv");
										            Intent sIn = new Intent(getApplicationContext(), C2dmregserv.class);
										            startService(sIn);     
										        }
										        db.close();
											}
											
								            
					
										}
									}
        );
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		
	   
	       if (keyCode == KeyEvent.KEYCODE_MENU)
	       {
	    	 
	    	   return true;
	       }
	   
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();		
		Log.d("G22","REGISTRATION entered in on pause");
		
		mThis = null;
		if (flag2 == 2)
		{	
			mlocManager.removeUpdates(mlocListener);
		}
		wakeLock.release();

	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("G22","REGISTRATION entered in on Resume");
		mThis = this;
		regclick.setVisibility(View.VISIBLE);
		rgmenu.setVisibility(View.VISIBLE);
		flag2 =1;
		wakeLock.acquire();
		boolean b = cIC();
        if (b!=true)
        {       	
        	regclick.setEnabled(false);
        	t.setText("No Packet Data connection (No Internet connection)");       	
        	
        }
        else
        {
        	regclick.setEnabled(true);
        	rgmenu.setEnabled(true);
        	if (t.getText().toString().equals("No Packet Data connection (No Internet connection)"))
        	{
        		t.setText("");
        	}
        }
	}
        
    @Override
    protected void onDestroy() {
    	Log.d("G22","REGISTRATION entered in on destroy");
    	if (flag2 == 2)
		{
    		mlocManager.removeUpdates(mlocListener);
		}
    	db.close();    	
    	super.onDestroy();
    }   
    /* There is no need for option menu till recover is implemented*/
    @Override
   	public boolean onCreateOptionsMenu(Menu menu) 
   	{
   		MenuInflater inflater = getMenuInflater();
   		inflater.inflate(R.menu.menuf, menu);
   		return true;
   	}
   
    /* There is no need for option menu in registration till recover is implemented*/
    @Override
   	public boolean onOptionsItemSelected(MenuItem item)
   	{
       	
       	item.setEnabled(false);
   		if(item.getItemId() == R.id.support)
   		{
   				if (cIC() == false)
   				{
   					Log.d("G22","Interne not there");
   					Toast.makeText(this, "Internet is not there !!", 100000).show();
   				}
   				
   					Intent recoverint = new Intent(this,Recover.class);
	   				startActivity(recoverint);

   				
   		}
   		if(item.getItemId() == R.id.howtouse)
   		{
   				
   				
   					Intent recoverint = new Intent(this,Howtouse.class);
   					recoverint.putExtra("caller", 1);
	   				startActivity(recoverint);

   				
   		}
   		item.setEnabled(true);
		return true;
   	}
   
    
    public class MyLocationListener implements LocationListener
	{
		@Override	
		public void onLocationChanged(Location loc)	
		{			
			Log.d("G22", "ENTERED ON LOCATION CHANGED, Location source"+loc.getProvider()+"  and accuracy : "+loc.getAccuracy());			
			lati = (float) loc.getLatitude();
			longi = (float) loc.getLongitude();	 
			
			
			/*if ((Math.abs(lati-globallati)>.01 || Math.abs(longi-globallongi)>.01)&&bigcount <3)
			{
				Log.d("G22", "ENTERED ON Lc 1");
				globallati = lati;
				globallongi = longi;
				flagcount = 0;
				bigcount++;
			}
			else if (flagcount < 0)
			{
				Log.d("G22", "ENTERED ON Lc 2");
				flagcount++;				
			}
			else
			{*/ 
			Log.d("G22","Result of first comparision :"+isBetterLocation(loc, lastnk) );
			Log.d("G22","Result of Second comparision :"+isBetterLocation(loc, lastgk) );		
			chkcount++;
			if ((isBetterLocation(loc, lastnk) || isBetterLocation(loc, lastgk)) || chkcount >2)
			{
				
				mlocManager.removeUpdates(mlocListener);
				if (isBetterLocation(loc, lastnk) && !isBetterLocation(loc, lastgk))
				{
					loc = lastgk;
				}
				else if (!isBetterLocation(loc, lastnk) && isBetterLocation(loc, lastgk))
				{
					loc = lastnk;
				}
				Log.d("G22", "Final Location source"+loc.getProvider()+"  and accuracy : "+loc.getAccuracy());	
				double lattti = loc.getLatitude();
				double longggi = loc.getLongitude();
				Log.d("G22","Current Address value Latit : " + lattti+ " Longit : " +longggi);
				Log.d("G22","ent before getaddress");
				//getAddress(lattti, longggi);
				Log.d("G22","ent after getaddress");
				Log.d("G22", "ENTERED ON Lc 3");				
				t = (TextView) findViewById(R.id.textView5);	
				t.setText("Location finding process completed");
				Date d = new Date();		
				db = openOrCreateDatabase("GiraffeDBv2", 0, null);
				db.delete("User", null, null);
				db.execSQL("INSERT INTO User VALUES ('','"+un+"','"+lati+"','"+longi+"','"+d+"','','"+pn+"','','','0','"+ps+"');" );				
				Log.d("G22","about to start service of getting registration");				
				Intent serviceIntent = new Intent(getApplicationContext(), C2dmregserv.class);
	            startService(serviceIntent);            
			}
			
		}
	
		@Override
		public void onProviderDisabled(String arg0) {
			
			Toast.makeText( getApplicationContext(),"Giraffe22 : Gps is Disabled!",Toast.LENGTH_SHORT ).show();
		}
	
		@Override
		public void onProviderEnabled(String arg0) {
			
			Toast.makeText( getApplicationContext(),"Giraffe22 : Gps Enabled!", Toast.LENGTH_SHORT).show();
		}
	
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	}
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        Log.d("G22","Tiemdelta : "+timeDelta);
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        Log.d("G22","Accuracy delta : "+accuracyDelta);
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    private boolean buildAlertMessageNoGps(int i) {
		if (i == 1 || i == 3){
			
		    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    
		    builder.setMessage("GIRAFFE: Your GPS seems to be disabled, do you want to enable it?")
		           .setCancelable(false)
		           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               @Override
					public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		            	   
		            	   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); 
		               }
		           })
		           .setNegativeButton("No", new DialogInterface.OnClickListener() {
		               @Override
					public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		            	   
		                    dialog.cancel();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
			}
		return true;
		
		
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
    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }


}
