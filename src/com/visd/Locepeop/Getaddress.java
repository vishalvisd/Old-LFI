package com.visd.Locepeop;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;


public class Getaddress extends Service implements Runnable{

	Intent inten;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	String[] arrs;
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		inten = intent;
		try {
			Log.d("G22","entered in Getaddress Service");
			
			Log.d("G22","getaddress waked from 5 sec sleep");
			if (Arrowhandler.aThis != null)
			{	           
			    Log.d("G22","Entered in getaddress where to chagne text 1");
				((TextView)Arrowhandler.aThis.findViewById(R.id.textView2)).setText("Finding Address...");	           
			}
			arrs = intent.getExtras().getStringArray("Loci");	
			Thread th = new Thread(this,"DDDC");
			th.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //stopSelf();
		stopService(intent);
		return super.onStartCommand(intent, flags, startId);
	}
	private String func(String a)
    {
    	if (a == null)
    	{
    		return "";
    	}
    	else
    	{
    		return ","+a;
    	}
    }
	@Override
	public void onDestroy() {
		Log.d("G22","Getaddres entered on destroy");
		super.onDestroy();
	}

	@Override
	public void run() {
		String lat = arrs[0];
		String lng = arrs[1];
    	final Geocoder geocoder = new Geocoder(Getaddress.this, Locale.getDefault());
    	//Log.d("G22","entered in getaddress2");
        try {
        	
        	
		    List<Address> addresses = geocoder.getFromLocation(Float.parseFloat(lat), Float.parseFloat(lng), 1);
					
		
          
            //Log.d("G22","entered in getaddress4");
            if (addresses.size()>0)
            {           
            Address obj = addresses.get(0);
            String add = "Locality : ";
            add = add + obj.getAddressLine(0);
          
           
            add = add + func(obj.getAdminArea());
            add = add + func(obj.getPostalCode());           
            add = add + func(obj.getLocality());
            add = add + func(obj.getSubThoroughfare());
            add = add + func(obj.getSubAdminArea());
            add = add + func(obj.getCountryName());
            add = add + func(obj.getCountryCode());
            
            if (Arrowhandler.aThis == null)
            {
            	Log.d("G22","Athis null");
            	stopSelf();
            	stopService(inten);
            }
            if (Arrowhandler.aThis != null)
            {	   final String adde = add;
            	Log.d("G22","Entered in getaddress where to chagne text 2");
            	Arrowhandler.aThis.runOnUiThread(new Runnable() {
		    		public void run() {
	            ((TextView)Arrowhandler.aThis.findViewById(R.id.textView2)).setText(adde);
		    		}});
            }
            //Log.d("G23","add1 : "+add);
            ////Log.d("G23","add2 : "+finaladd);
            }
            else
            {
            	if (Arrowhandler.aThis != null)
                {	           
                    Log.d("G22","Entered in getaddress where to chagne text 1");
                    Arrowhandler.aThis.runOnUiThread(new Runnable() {
    		    		public void run() {
        			((TextView)Arrowhandler.aThis.findViewById(R.id.textView2)).setText("Unable to Get Address..");	 
    		    		}});
                }
            }
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            final IOException ew = e;
            if (Arrowhandler.aThis != null)
            {	           
                Log.d("G22","Entered in getaddress where to chagne text 1");
                Arrowhandler.aThis.runOnUiThread(new Runnable() {
		    		public void run() {
    			((TextView)Arrowhandler.aThis.findViewById(R.id.textView2)).setText("Unable to Get Address..");	 
		    		}});
            }
            if (Arrowhandler.aThis != null)
            {
            Arrowhandler.aThis.runOnUiThread(new Runnable() {
	    		public void run() {
            Toast.makeText(Arrowhandler.aThis, ew.getMessage(), Toast.LENGTH_SHORT).show();
	    		}});
            }
            
        }
        catch (Exception ex1)
        {
        	try
        	{
        		Toast.makeText(Getaddress.this, "EGA", 10000).show();
        	}
        	catch (Exception ex2) {
				// TODO: handle exception
			}
        }
		
	}

}

/*GUIStatics.currentAddress = obj.getSubAdminArea() + ","
+ obj.getAdminArea();
GUIStatics.latitude = obj.getLatitude();
GUIStatics.longitude = obj.getLongitude();
GUIStatics.currentCity= obj.getSubAdminArea();
GUIStatics.currentState= obj.getAdminArea();*/
/*String a[] = {obj.getAddressLine(0),obj.getCountryName(),obj.getCountryCode(),obj.getAdminArea(),obj.getPostalCode(),obj.getSubAdminArea(), obj.getLocality(),obj.getSubThoroughfare()};
if (a[0].equals(null))
{
a[0] = "";
}
if (a[1].equals(null))
{
a[1] = "";
}
if (a[2].equals(null))
{
a[2] = "";
}
if (a[3].equals(null))
{
a[3] = "";
}
if (a[4].equals(null))
{
a[4] = "";
}
if (a[5].equals(null))
{
a[5] = "";
}
if (a[6].equals(null))
{
a[6] = "";
}
if (a[7].equals(null))
{
a[7] = "";
}
String finaladd = a[0]+a[1]+a[2]+a[3]+a[4]+a[5]+a[6]+a[7];*/