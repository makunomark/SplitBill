package com.example.splitbill.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
public class MyDB {
    private final Context context;
    private final MyDBhelper dbhelper;
    private SQLiteDatabase db;
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

    public long insertRecord(String name, String amount) {
        //general records.
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

    public long insertDebtsRecord(String debtor_no, String lender_no, int amount) {
        try {
            ContentValues newTaskValue = new ContentValues();
            newTaskValue.put(Constants.DEBTOR_NO, debtor_no);
            newTaskValue.put(Constants.LENDER_NO, lender_no);
            newTaskValue.put(Constants.AMOUNT, String.valueOf(amount));
            newTaskValue.put(Constants.DATE, java.lang.System.currentTimeMillis());
            return db.insert(Constants.TABLE_NAME_DEBTS, null, newTaskValue);
        } catch (SQLiteException ex) {
            Log.e("Debts: insert records:", ex.getMessage());
            return -1;
        }
    }

    public Cursor getDebtsRecords() {
        return db.query(Constants.TABLE_NAME_DEBTS, null, null, null, null, null, null);
    }

    public Cursor getRecords() {
        return db.query(Constants.TABLE_NAME, null, null, null, null, null, null);
    }
}