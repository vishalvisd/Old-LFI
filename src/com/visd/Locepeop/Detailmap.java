package com.visd.Locepeop;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Detailmap extends MapActivity {

	MapView mapView;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Log.d("G22","Started Detailmap");
		Bundle b = getIntent().getExtras();
	    int reg = b.getInt("Reg", 0);
	    String rg = ""+reg;
		
		//Toast.makeText(this, ""+reg, 10000).show();
		setContentView(R.layout.mapdetail);
		MapView view = (MapView) findViewById(R.id.themap);
		mapView = view;
		view.setBuiltInZoomControls(true);
		view.setStreetView(true);  
		MapController control = view.getController();
		control.setZoom(17);
		
		
		
		
		List<Overlay> mapOverlays =  view.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
	
		 SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);
		 
		 Cursor smsc = db.rawQuery("SELECT * FROM User", null);
		 if (smsc.getCount()>0)
		 {
			 smsc.moveToFirst();
			 float latiu = Float.parseFloat(smsc.getString(smsc.getColumnIndex("Lat")));
			float longiu =  Float.parseFloat(smsc.getString(smsc.getColumnIndex("Long")));	
			latiu = (float) (latiu+0.00001);
			Log.d("G41","user own lati litiu = "+latiu);
			Log.d("G41","user own longi longiu ="+longiu);
			String pnou = smsc.getString(smsc.getColumnIndex("Pno"));					
			GeoPoint pointu = new GeoPoint((int)(latiu * 1e6), (int)(longiu * 1e6));		
			control.animateTo(pointu);
			OverlayItem overlayitemu = new OverlayItem(pointu, "You are Here !" , "Your No. : "+pnou);
			itemizedoverlay.addOverlay(overlayitemu);
			mapOverlays.add(itemizedoverlay);
		 }
		smsc.close();
		
		
		Cursor msc = db.rawQuery("SELECT * FROM Knowns", null);
		if (msc.getCount()>0)
		{
			 msc.moveToFirst();
			if (!rg.equals("0") )
			{
				do
				{
					if (msc.getString(msc.getColumnIndex("RegNo")).equals(rg))
					{
						float lati = Float.parseFloat(msc.getString(msc.getColumnIndex("Lat")));
						float longi =  Float.parseFloat(msc.getString(msc.getColumnIndex("Long")));
						Log.d("G41","Known own lati liti = "+lati);
						Log.d("G41","Known own longi longi ="+longi);
						String name = msc.getString(msc.getColumnIndex("Name"));
						String pno = msc.getString(msc.getColumnIndex("Pno"));					
						GeoPoint point = new GeoPoint((int)(lati * 1e6), (int)(longi * 1e6));
						control.animateTo(point);
						OverlayItem overlayitem = new OverlayItem(point, name , pno);
						itemizedoverlay.addOverlay(overlayitem);
						mapOverlays.add(itemizedoverlay);
						
					}
				}while (msc.moveToNext());
			}
			else
			{
				control.setZoom(8);
				do
				{
						float lati = Float.parseFloat(msc.getString(msc.getColumnIndex("Lat")));
						float longi =  Float.parseFloat(msc.getString(msc.getColumnIndex("Long")));
						if ((lati > Math.abs(0.1)) || (longi > Math.abs(0.1)))
						{
							String name = msc.getString(msc.getColumnIndex("Name"));
							String pno = msc.getString(msc.getColumnIndex("Pno"));
							GeoPoint point = new GeoPoint((int)(lati * 1e6), (int)(longi * 1e6));
							OverlayItem overlayitem = new OverlayItem(point, name , pno);
							itemizedoverlay.addOverlay(overlayitem);
							mapOverlays.add(itemizedoverlay);
						}
						
				}while (msc.moveToNext());
			}
		}
			
		msc.close();
		db.close();
		
		/*
		GeoPoint point2 = new GeoPoint(35410000, 139460000);
		OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");
		itemizedoverlay.addOverlay(overlayitem2);
		mapOverlays.add(itemizedoverlay);
		*/
		
	}
	public boolean onCreateOptionsMenu(Menu menu){
	    super.onCreateOptionsMenu(menu);
	    MenuInflater oMenu = getMenuInflater();
	    oMenu.inflate(R.menu.menug, menu);
	    return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
	    switch(item.getItemId()){
	    case R.id.mapStreet:
	         mapView.setStreetView(true);
	         mapView.setSatellite(false);
	         mapView.invalidate();
	         return true;

	    case R.id.mapSat:
	         mapView.setSatellite(true);
	         mapView.setStreetView(false);
	         mapView.invalidate();
	         return true;
	    }
	    return false;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
