package com.visd.Locepeop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class C2dmregrecv extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String a = intent.getAction();
		Log.d("G22","c2dm  1 registraionid received!");
	    if (a.equals("com.google.android.c2dm.intent.REGISTRATION")) 
	    {
	      Log.d("G33","c2dm registraionid received!");
	      Log.d("G22","c2dm registraionid received!");
	      String regId = intent.getStringExtra("registration_id");
	      String error = intent.getStringExtra("error");
	      String unregistered = intent.getStringExtra("unregistered");
	      if (error != null) 
	      {
	    	  Log.d("G22","Entered error: "+error);
	    	  Log.d("G33","Entered error: "+error); 
	    	  /* recover is not implemented yet
	    	  if (Recover.recoverThis != null)
	    	  {
	    		 ((TextView) Recover.recoverThis.findViewById(R.id.rectvstatus)).setText("Sorr,Communication Error (is your data connection ok?)");
	    	  }
	    	  */
	    	  if (error.equals("ACCOUNT_MISSING"))
	    	  {
	    		  error = "Phone lack google account, Market not initialised properly";
	    	  }
	    	  if (error.equals("AUTHENTICATION_FAILED"))
	    	  {
	    		  error = "Your Android Market is not working! A proper Gmail id. is necessarry to register you phone to google.";
	    	  }
	    	  if (error.equals("AUTHENTICATION_FAILED"))
	    	  {
	    		  error = "Your Android Market is not working! A proper Gmail id. is necessarry to register you phone to google.";
	    	  }
	    	  if (error.equals("TOO_MANY_REGISTRATIONS"))
	    	  {
	    		  error = "User has too many other applications registered.";
	    	  }
	    	  if (error.equals("INVALID_SENDER"))
	    	  {
	    		  error = "Sender account is not recognized.";
	    	  }
	    	  if (error.equals("PHONE_REGISTRATION_ERROR"))
	    	  {
	    		  error = "Incorrect phone registration with Google. This phone doesn't currently support C2DM.";
	    	  }
	    	  if (error.equals("SERVICE_NOT_AVAILABLE"))
	    	  {
	    		  error = "Can't read the response (Please check your Connection), Use exponential back off and retry.";
	    	  }
	    	  if (Refreshreg.refregThis != null) 
	    	  {
					  ((TextView)Refreshreg.refregThis.findViewById(R.id.regregtv1)).setText("Pre-Regestration Error ! Error :" +error);
					 
					  
	    	  }
	    	  else
	    	  {
		    	  if (Registration.mThis != null) 
		    	  {
					    ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("C2DM Registration Error: " +error+"   (is your data connection ok?)" );
		    	  }
		    	  Intent iii = new Intent(context,Clrusertable.class);
		    	  context.startService(iii);
	    	  }
	    	  
	      }
	      else if (regId != null) 
	      {
	    	Log.d("G22","c2dm id received!" + regId);
	    	Log.d("G33","c2dm id received!" + regId); 
	    	/*  
	    	if (Recover.recoverThis != null)
	    	  {
	    		 ((TextView) Recover.recoverThis.findViewById(R.id.rectvstatus)).setText("Ready to Recover");
	    	  }
	    	 */
	    	if (Registration.mThis != null) 
	    	{
				  ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Pre - Registration Successful !" );

			        Intent i = new Intent(context,Sendc2dmreg.class);
			        i.putExtra("regId", regId);	        
			        context.startService(i);
	    	}
	    	 
	    	if (Refreshreg.refregThis != null) 
	    	{
	    		Log.d("G33","Starting regregserv "); 
				  ((TextView)Refreshreg.refregThis.findViewById(R.id.regregtv1)).setText("Pre-Regid Received, Wait little more... !" );
				  Intent ifn = new Intent(context,Refregserv.class);
			      ifn.putExtra("regId", regId);	        
			      context.startService(ifn);
				  
	    	}
	    	Log.d("G33","oveer andout"); 
	        
	      } 
	      else if (unregistered != null) 
	      { 
	    	  Log.d("G22","Entered Unregisted " + unregistered);
	    	  if (Registration.mThis != null) 
	    	  {
				    ((TextView)Registration.mThis.findViewById(R.id.textView5)).setText("Problem :The C2DM server Unregistered you. Please try once again " );
	    	  }
	      }

		}

	}
	

}
