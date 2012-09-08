package com.visd.Locepeop;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Howtouse extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.howtouse);
		int caller = getIntent().getIntExtra("caller", 0);
		String regs = "";
		String general ="&nbsp&nbsp&nbsp&nbsp&nbsp With this app. you can always know in an Instant exactly where are your friends,etc " +
						"currently in the world and also send unlimited instant messages for free." +
						" So, Feel the Never before amazing power just by asking your friends to download the App. " +
						" and discover a new convenient and easier life !" +
						" <br><br> How to Use : <br><br>";
		if (caller == 1)
		{
			regs = "Registration Help,<br><br> To register, you have to give any numeric number  in the phone number field. It will be your ID," +
					" so that other can add you using the ID. <br>Password :Please give a complex password.<br><br>If you had used the app before and forget to " +
					"Unregister during previous un-installation, and now want to use the same number to register again than you have to select from the menu –“" +
					"Any Problem” and then fill the form to unregister you old number, so that you may again use the same number.<br><br>Dur" +
					"ing registration you will be shown the present status at bottom of the screen. Have little patience, the registration process will take approx" +
					". 15 sec (or more depending on your data connection). After registration you will be automatically be taken to the Main Sc" +
					"reen.<br><br>";
		}
		TextView tv = (TextView) findViewById(R.id.hwttv1);
		String summary = "<html><font color=\"#FFFFFF\"" + "<p align="+ "\"" +"left" + "\""+ ">" +  regs+general+"0)	For getting a feel, you may always add the number - 7204336295 (the developer), and remove at any time when you wish. <br><br>1)	The application provide you " +
				"peer to peer on demand connection where your phone directly fetch current location of your friend or relative " +
				"and thus is very realiable. <br><br>2)	Adding Friends, or any other known people :-<br>-	The  other person has to have the application " +
				"installed.<br>-	You must be knowing his phone number / the No. with which he/she had registered.<br>-	Just click on ‘Add User’ menu , " +
				"enter the number and you are done.<br><br>3)	To Send messages, Long Press / click the friend name shown in the list, after that you will" +
				" be taken to new window, there on hitting the menu key you can choose to send message.<br><br>4)    Distance and Direction system works best" +
				" when the two people are distant from each other (at least 150meters) although this constraint is not there when users are outdoor or distant." +
				"  Little variation occur when users are indoor/very near to each other.<br><br> 5)	To Remove User, long press on any name.<br><br>6)	" +
				"To Unregister click three consecutive time the unregister menu item from the menu.<br>Warning : Once unregistered, you will loose all " +
				"information about your friends and same thing for them too.<br><br>7)	You might feel that you are not able to receive messages or user " +
				"location is not getting updated, at this time you may choose to 'Refresh'. <br>Warning : Please never use Refresh unnecessarily. It might " +
				"make you application stale and then you have reinstall.<br><br>8)	 Indicators : Yellow means no new better location received, Green" +
				" means a better approximate of your location has been received.<br><br>9)	Support :" +
				" visd@in.com, we will revert to you within couple of hours.<br><br>IMPORTANT : THE APP. IS MADE IN SUCH A WAY THAT IT SHOULD USE GPS FOR A VERY" +
				" SHORT PERIOD OF TIME (~10sec), SO, NO NEED TO WORRY ABOUT BATTERY OR POSSIBLE SLOWDOWN OF DEVICE. ALSO IF YOU DON'T LIKE TO KEEP GPS " +
				"ENABLED, YOU MAY OPT SO AND THE APP. WILL WORK JUST FINE. <br> THE ONLY REQUIREMENT IS DATA CONNECTION. THOUGH THE APP. CONSUMES VERY MINUTE" +
				" DATA (~0.1 KB) BUT IT REQUIRES DATA CONNECTION TO COMMUNICATE LOCATION AND GET THEIR LOCATION. SO, KEEP PACKET DATA ENABLED<br><br><br>       Thank you, Happy Geo-Locating<BR><BR>" 
		+"</p>"+  "</font></html>";
		tv.setText(Html.fromHtml(summary));

		
	}

}
