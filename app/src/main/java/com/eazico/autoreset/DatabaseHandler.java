package com.eazico.autoreset;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "PROFILE_DB1";
    private static final String VOL = "VOL";
    private static final String WIFI = "WIFI";
    private static final String LAT = "LAT";
    private static final String LON = "LONG";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_TABLE = "CREATE TABLE " + "PROFILE1" + "("
                + LAT + " VARCHAR(20)," + LON + " VARCHAR(20) ," + VOL + " INTEGER,"
                + WIFI + " INTEGER," + " PRIMARY KEY("+LAT+","+LON+"));";
        db.execSQL(CREATE_TABLE);
        Log.i("table","new table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "PROFILE1");

        // Create tables again
        onCreate(db);
    }

    add_entry_status add_entry(double lon, double lat, int vol, int wifi) {

      //  String selectQuery = "SELECT  * FROM " + "PROFILE " + "WHERE LONG=" + lon + " AND LAT=" + lat;
      // Cursor cursor = db.rawQuery(selectQuery, null);
        add_entry_status stat;
        Data_class c=check(lat,lon);
        if(c!=null){
          //  cursor.close();
           // db.close();
           // Log.i("cursor count:" ,String.valueOf(cursor.getCount()));
            int er=updatetable(c,new Data_class(lat,lon,vol,wifi));
            stat=new add_entry_status(1,er);
            return stat;
        }else {
            Log.i("add","initial entry");
            ContentValues values = new ContentValues();
            values.put(LAT, String.valueOf(lat));
            values.put(LON, String.valueOf(lon));

            values.put(VOL, vol);
            values.put(WIFI, wifi);
            SQLiteDatabase db = this.getWritableDatabase();

            // Inserting Row
           int er=(int) db.insert("PROFILE1", null, values);
//cursor.close();
            db.close(); // Closing database connection
            stat=new add_entry_status(0,er);
            return stat;
        }
    }

    public List<Data_class> getsettings(double lon, double lat) {
      // SQLiteDatabase db = this.getWritableDatabase();
    // db.execSQL("DELETE FROM PROFILE;");

        //db.close();
        List<Data_class> contactList = new ArrayList<Data_class>();
        // Select All Query
      String selectQuery = "SELECT  * FROM " + "PROFILE1 ";

       SQLiteDatabase
                db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Data_class contact = new Data_class(Double.parseDouble(cursor.getString(0)), Double.parseDouble(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
Log.i("in the list",""+contact.lat+" "+contact.lon+" " +contact.vol+" "+contact.wifi);
                // Adding contact to list
                if(compare.ready(lat,lon,contact.lat,contact.lon)){
                    contactList.add(contact);
                    Log.i("add","added");
                }
                else
                    Log.i("add","not added");

            } while (cursor.moveToNext());
        }
      //  db.execSQL("DELETE FROM PROFILE;");

        cursor.close();
        db.close();


        return contactList;
    }
    public int updatetable(Data_class old,Data_class contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
values.put(LAT,String.valueOf(contact.lat));
        values.put(LON,String.valueOf(contact.lon));
        values.put(VOL, contact.vol);
        values.put(WIFI, contact.wifi);

        // updating row
        Log.i("update query old entry ",LAT + " = "+old.lat+" AND "+LON+ " = "+old.lon+" "+old.vol+" "+old.wifi);
        Log.i("update query new entry",""+contact.lat+" "+contact.lon+" "+contact.vol+" "+contact.wifi);
       // Cursor c=db.rawQuery("SELECT * FROM PROFILE WHERE  "+LAT+"="+old.lat+ " AND "+LON+" = "+old.lon+";",null);
       // if(c.getCount()>0)
       //     Log.i("nos","sucess");
       // else Log.i("nos","failure");



        int er= db.update("PROFILE1", values, LAT + " = "+String.valueOf(old.lat)+" AND "+LON+ " = "+String.valueOf(old.lon),    null);
        db.close();
        Log.i("error1", ""+ er);
        return er;
    }
    public void deletetable(Data_class contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PROFILE1", LAT+ " = ? AND"+LON+"=?",
                new String[] { String.valueOf(contact.lat),String.valueOf(contact.lon) });
        db.close();
    }
    public Data_class check(double lat,double lon){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + "PROFILE1 " ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Data_class contact = new Data_class(Double.parseDouble(cursor.getString(0)), Double.parseDouble(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));

                // Adding contact to list
                if(compare.ready(lat,lon,contact.lat,contact.lon)){
                    cursor.close();
                    db.close();
                    Log.i("already stored",""+contact.lat+" "+contact.lon);
                    return contact;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  null;
    }

}


