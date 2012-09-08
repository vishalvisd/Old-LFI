package com.visd.Locepeop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class C2dmreceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("G27", "Entered C2dmreceiver");
		String action = intent.getAction();
		
		Log.w("G33", "cc called");
		if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) 
		{
			Log.w("G27", "Received message");
			final String payload = intent.getStringExtra("key1");
			int typeind= payload.indexOf("*type");
			String type = payload.substring(0, typeind);
			Log.d("G27","Type of C2DM messg : "+type);
			try
			{
				if (type.equals("AUc2dm"))
				{
					Log.d("G27", "Received AUc2dm type,complete message is : " + payload);
					
					String detailstring = payload.substring(payload.indexOf("*type*")+6);	
					detailstring = detailstring + "*";
					Log.d("G27","Details string = "+detailstring);
					int cindex = 0;
					int starindex = detailstring.indexOf("*");
					Log.d("G27","Starindedx = "+starindex);
					int cont = 0;
					String arr[] = new String[6];
					arr[0] = "AUc2dm";
					while (starindex != -1)
					{
						Log.d("G27","Entered while");
						cont++;
						Log.d("G27","Cont value = "+cont+" starindex = "+starindex);
						arr[cont] = detailstring.substring(cindex, starindex);
						Log.d("G27","arr[cont] manufactured, arr["+cont+"] = "+arr[cont]);
						cindex = starindex +1;
						starindex = detailstring.indexOf("*", cindex);				
					};
					
					
					Intent i = new Intent(context,Serviceforc2dm.class);
			        i.putExtra("DET", arr);	        
			        context.startService(i);
					
					Toast.makeText(context, "One Friend Request Received", 10000).show();
					
					
				}
				
				if (type.equals("EMc2dm"))
				{
					Log.d("G27", "Received EMc2dm type,complete message is : " + payload);
					String messs = payload.substring(payload.indexOf("*type*")+6);
					int ijk = messs.indexOf("*");
					String sendername = messs.substring(0, ijk);
				    String realmess = messs.substring(ijk+1);
				    Log.d("G27","Inside EMc2dm, realmessg : "+realmess);
				    Log.d("G27","Inside EMc2dm, senderregno : "+sendername);
				    String sjs[] = {realmess,sendername};
					
					Intent i2 = new Intent(context,Justts.class);	
					i2.putExtra("MSG", sjs);	
					i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        context.startActivity(i2);				
					
				}
				if (type.equals("CHKc2dm"))
				{
					
					Intent i2 = new Intent(context,Checkcatch.class);	
					context.startService(i2);
					
				}
				
				if (type.equals("LNAc2dm"))
				{
					Log.d("G27","entered in block for lnac2dm");
					if (Arrowhandler.aThis != null)
					{
						Log.d("G27","entered in block for lnac2dm athis null");
						TextView t = (TextView) Arrowhandler.aThis.t;
						Arrowhandler.aThis.globalstop = 1;
						t.setText("User is not Online.");
						
					}
					
				}
				
				
				if (type.equals("REMc2dm"))
				{
					//01-27 21:43:49.545: DEBUG/G22(3238): dmControl: message = REMc2dm*type*11
	
					Log.d("G27", "Received REMc2dm type,complete message is : " + payload);
					String toremregid = payload.substring(payload.indexOf("*type*")+6);
					String arr[] = {"REMc2dm",toremregid};				 
					Intent i = new Intent(context,Serviceforc2dm.class);
			        i.putExtra("DET", arr);	
			        context.startService(i);			
				}
				
				if (type.equals("UULc2dm") || type.equals("LUc2dm"))
				{
					//Received UULc2dm type,complete message is : UULc2dm*type*11*13.001506*74.7967
	
					Log.d("G27", "Received UULc2dm type,complete message is : " + payload);
					//Toast.makeText(context, payload, 10000).show();
					try
					{
						String regno = null;
						String lati = null;
						String longi = null;
						String accuracy = null;
						int i = payload.indexOf("*type*")+6;
						String str[] = new String[4];
						int temp;
						for (int j = 0;i!=0;j++)
						{
							Log.d("G27","uulc2dm Entered in for loop");
							temp = i;
							i = payload.indexOf("*",i);
							if (i==-1)
							{
								str[j] = payload.substring(temp);
								Log.d("G27","Inside uulc2dm : str["+j+"] = "+str[j]);
							}
							else
							{
								str[j] = payload.substring(temp, i);
								Log.d("G27","Inside uulc2dm1 : str["+j+"] = "+str[j]);
							}
							i++;
						}
						short islu = 0;
					
						 if (type.equals("LUc2dm"))
					        {
							 	Log.d("G30","C2dm receiver received a request from external party with regid = "+str[0]+", to have an updated location of this phone");
					        	islu = 1;				            	
					        }
						regno = str[0];
						lati = str[1];
						longi = str[2];
						try
						{
							accuracy = str[3];
						}
						catch (Exception e) {
							// TODO: handle exception
						}
						String messg[] = {"UULc2dm",regno,lati,longi,""+islu};
						Intent in = new Intent(context,Serviceforc2dm.class);
				        in.putExtra("DET", messg);	        
				        context.startService(in);					
					
				       
					
					
				        Log.d("G28","checkii 0 from c2dmreceiver");
						if (Arrowhandler.aThis != null && lati != null && longi != null && regno != null) 
						{
							Log.d("G28","checkii 1 from c2dmreceiver");
							if (Arrowhandler.aThis.reg.equals(str[0]));
							{
								Log.d("G28","checkii 2 from c2dmreceiver");	
								double angle = Math.atan2(Arrowhandler.aThis.mslongi - Float.parseFloat(longi), Arrowhandler.aThis.mslati - Float.parseFloat(lati));
					       	  	double degree = angle*57.295779;
					       	  	float dd = (float)degree;
					       	  	if (dd>0)
						 	    {
						 	     	dd = dd+180;
						 	    }
						 	    else if (dd<0)
						 	    {
						 	     	dd = 180 - (Math.abs(dd));
						 	    }  
							    Arrowhandler.aThis.d = dd;
							    float idisti = Arrowhandler.aThis.distFrom(Arrowhandler.aThis.mslati, Arrowhandler.aThis.mslongi, Float.parseFloat(lati), Float.parseFloat(longi));
							    idisti = (float) (idisti * 1.60934);
							    Arrowhandler.aThis.dist = idisti;
							    Button b = (Button) Arrowhandler.aThis.findViewById(R.id.button1);
							    String ac = " ?";
							    String added = "";
							    if (accuracy != null && accuracy.indexOf("null") == -1)
							    {
							    	try
							    	{
							    		int accuu = Integer.parseInt(accuracy);
							    		accuu = accuu / 2;
							    		if (accuu > 500)
							    		{
							    			added = "Large Variation imply this person and/or you are indoor or GPS sensor is off";
							    		}
							    		ac = accuu+" METERS";
							    	}
							    	catch (Exception e) {
										// TODO: handle exception
									}
							    }
							    
							    Log.d("G28","checkii 3 from c2dmreceiver");
							    b.setText("Got a new Current Location from the user : Distance Variation circle radius at max "+ac+", "+added+", TAP TO REFRESH");
							    b.setBackgroundResource(R.drawable.btn_grn);
							    Toast.makeText(context, "Updated Location Received", 10000).show();
							    
								final String thrlati = lati;
								final String thrlongi = longi;
								final Context thrcontext = context;
								try
								{
								Thread thr4 = new Thread (new Runnable() {
									
									@Override
									public void run() {
										//getAddress(Double.parseDouble(""+throslati), Double.parseDouble(""+throslongi));
										
										Log.d("G30","Testing for thread : after 11");
										String arr[] = {""+thrlati,""+thrlongi};
										Intent i = new Intent(thrcontext, Getaddress.class);
								        i.putExtra("Loci", arr);	        
								        thrcontext.startService(i);
								        Log.d("G30","Testing for thread : after 12");
								        try{
								        if (Arrowhandler.aThis != null)
								        {
								        	Arrowhandler.aThis.runOnUiThread(new Runnable() {    			
												public void run() {
													Toast.makeText(thrcontext, "Updating address", 10000).show();
												}});
								        	
								        }}
								        catch (Exception thre1)
								        {
								        	
								        }
									}
								});
				    			thr4.start();
								}
								catch (Exception e) {
									Toast.makeText(context, "Problem updating address", 10000).show();
								}
							    b.setBackgroundResource(R.drawable.btn_grn);
							    Toast.makeText(context, "Updated Location Received", 10000).show();
							}
						}
					}
					catch (NullPointerException e)
					{
						Toast.makeText(context, "UUL: Server Misbehaved", 10000);
					}
					catch (IndexOutOfBoundsException e2)
					{
						Toast.makeText(context, "UUL: Server Misbehaved", 10000);
					}
					catch (Exception e3)
					{
						Toast.makeText(context, "UUL: Server Misbehaved", 10000);
					}
				
				}
			}
			catch(Exception e)
			{
				Toast.makeText(context, "Something Went Wrong with Server Control1", 10000);
			}
				
			

			//final String payload2 = intent.getStringExtra("key1");
			Log.d("G27", "dmControl: message = " + payload );
		}
		
	

	}
	

}
