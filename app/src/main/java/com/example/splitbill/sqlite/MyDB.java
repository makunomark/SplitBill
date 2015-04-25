package com.example.splitbill.sqlite;

/**
 * Created by mark on 4/23/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
public class MyDB {
    private SQLiteDatabase db;
    private final Context context;
    private final MyDBhelper dbhelper;
    public MyDB(Context c){
        context = c;
        dbhelper = new MyDBhelper(context, Constants.DATABASE_NAME, null,
                Constants.DATABASE_VERSION);
    }
    public void close()
    {
        db.close();
    }
    public void open() throws SQLiteException
    {
        try {
            db = dbhelper.getWritableDatabase();
        } catch(SQLiteException ex) {
            Log.v("Open database:", ex.getMessage());
            db = dbhelper.getReadableDatabase();
        }
    }
    public long insertRecord(String name, String amount)
    {
        try{
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put(Constants.PERSON_NAME, name);
            newTaskValue.put(Constants.AMOUNT, amount);
            newTaskValue.put(Constants.DATE, java.lang.System.currentTimeMillis());
            return db.insert(Constants.TABLE_NAME, null, newTaskValue);
        } catch(SQLiteException ex) {
            Log.v("Insert into database:", ex.getMessage());
            return -1;
        }
    }
    public Cursor getRecords()
    {
        Cursor c = db.query(Constants.TABLE_NAME, null, null,
                null, null, null, null);
        return c;
    }
}