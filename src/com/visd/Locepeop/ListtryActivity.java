package com.visd.Locepeop;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListtryActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	 static final ArrayList<HashMap<String,String>> list =    
     		new ArrayList<HashMap<String,String>>();
	 int index = 0;
	@Override
	protected void onDestroy() {
		list.clear();
		super.onDestroy();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_view);

        SimpleAdapter adapter = new SimpleAdapter(
        this,
        list,
        R.layout.custom_row_view,
        new String[] {"pen","price","color"},
        new int[] {R.id.text1,R.id.text2, R.id.text3}

        );

        populateList();


        setListAdapter(adapter);
        
       /* final AlertDialog.Builder alertbx1 = new AlertDialog.Builder(this); 
        ListView list = getListView();
    	
    	list.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
    		public boolean onItemLongClick(AdapterView<?> parent, View view,
    				final int position, long id) {
    			Toast.makeText(ListtryActivity.this,
    					"Item in position " + position + " clicked",
    					Toast.LENGTH_LONG).show();
        	Log.d("G31","position = "+position+"index = "+index);
        		if (position < index)
        		{
        			
    	            SQLiteDatabase db2 = openOrCreateDatabase("GiraffeDBv2", 0, null);
       			 db2.execSQL("DELETE FROM Messages WHERE rowid = '"+position+"';");
       			 Toast.makeText(ListtryActivity.this, "Deleted !", 1000).show();
       			 db2.close();
        		}
				return true;
        	}
    	});*/
    		
        
      
    }
    
    private void populateList() {
    	
    	 SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);    
    	 Cursor c =  db.rawQuery("SELECT * FROM Messages;", null);        
         int icc = c.getCount();
         String strr [] = new String[500];
         String strr2[] = new String[500];
         String strr3[] = new String[500];
         //int index = 0;
         Log.d("G31","icc = "+icc);
         int j;
         if (icc > 0)
         {
        	 Log.d("G31","chk 1");
        	 c.moveToFirst();
        	 do{
		         
		         Log.d("G31","chk 2");
		    	String name = c.getString(c.getColumnIndex("Party"));
		    	String message = c.getString(c.getColumnIndex("Body"));
		    	String tim = c.getString(c.getColumnIndex("Tim"));
		    	String flag = c.getString(c.getColumnIndex("Flag"));
		    	 Log.d("G31","chk 3");
		    	if (flag.equals("TRUE"))
		    	{
		    		 Log.d("G31","chk 4");
		    		message = ">>>> "+message +" <<<<";
		    		name = name +"  >>>>";
		    	}
		    	else
		    	{
		    		name = "<<<<  "+name;
		    		message = "    "+message;
		    	}
		    	 Log.d("G31","chk 5");
		    	strr[index] = name;
		    	strr2[index] = message;
		    	strr3[index] = tim;
		    	try
		    	{
		    		strr3[index] = strr3[index].substring(0, strr3[index].indexOf("GMT"));
		    	}
		    	catch (Exception e)
		    	{
		    		Log.d("G31","chk 5");
		    	}
		    	 Log.d("G31","chk 6");
		    	index++;
		    	
			   
        	 }while(c.moveToNext());
        	 if (index != 0)
        	 {
	        	 String strr4 [] = new String[index];
	             String strr5[] = new String[index];
	             String strr6[] = new String[index];
	             for (j = 0; j<index; j++)
	             {
	            	strr4[j] = strr[j];
	            	strr5[j] = strr2[j];
	            	strr6[j] = strr3[j];
	            	
	             }
	            String arr1[] = reverse(strr4);
	            String arr2[] = reverse(strr5);
	            String arr3[] = reverse(strr6);

            
        	 
        	 Log.d("G31","chk 7");
        	 
        	 for (j=0; j<icc;j++)
        	 {
        		 Log.d("G31","chk 8");
	        	 	HashMap<String,String> temp = new HashMap<String,String>();
				    temp.put("pen",arr1[j]);
				    temp.put("price", arr2[j]);
				    temp.put("color", arr3[j]);
				    list.add(temp);
				    Log.d("G31","chk 9");
        	 }
        	 }
        	 else
        	 {
        		 Toast.makeText(ListtryActivity.this, "No Messages !", 10000).show();
        		 finish();
        	 }
         }
         else
         {
        	 Toast.makeText(ListtryActivity.this, "No Messages !", 10000).show();
        	 finish();
         }
        
    	
    }
    public static String[] reverse(String[] data) {
        for (int left = 0, right = data.length - 1; left < right; left++, right--) {
            // swap the values at the left and right indices
            String temp = data[left];
            data[left]  = data[right];
            data[right] = temp;
        }
		return data;
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menue, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this); 
		item.setEnabled(false);
		if(item.getItemId() == R.id.remmsg)
		{
			final SQLiteDatabase db = openOrCreateDatabase("GiraffeDBv2", 0, null);    
			Cursor d =  db.rawQuery("SELECT * FROM Messages;", null);
			final int dele = d.getCount();
			d.close();
			alertbox.setTitle("Permanently Deleting message  ...");
			alertbox.setMessage("You are about to delete "+dele+" messages permanently !");
            alertbox.setPositiveButton("Confirm", new DialogInterface.OnClickListener() 
            							{
            								public void onClick(DialogInterface dialog, int arg1) 
            								{
            									db.delete("Messages", null, null);
            									Toast.makeText(ListtryActivity.this, "Delete "+dele+" messages!", 10000).show();
            									db.close();
            									finish();
            								}
            							});
            alertbox.setNegativeButton("Back",  new DialogInterface.OnClickListener() 
            							{
            								public void onClick(DialogInterface dialog, int arg1) 
            								{
            									item.setEnabled(true);
            									dialog.dismiss();
            								}
            							});
            alertbox.show();
			
		}
		return false;
	}
	
}