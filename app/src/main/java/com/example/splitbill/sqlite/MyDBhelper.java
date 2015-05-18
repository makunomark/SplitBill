package com.example.splitbill.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class MyDBhelper extends SQLiteOpenHelper{
    //create split history table
    private static final String CREATE_TABLE="create table "+
            Constants.TABLE_NAME+" ("+
            Constants.KEY_ID+" integer primary key autoincrement, "+
            Constants.PERSON_NAME+" text not null, "+
            Constants.AMOUNT+" text not null, "+
            Constants.DATE+" long);";
    //create debts table
    private static final String CREATE_DEBTS_TABLE = "create table " +
            Constants.TABLE_NAME_DEBTS + " (" +
            Constants.KEY_ID + " integer primary key autoincrement, " +
            Constants.DEBTOR_NO + " text not null, " +
            Constants.LENDER_NO + " text not null, " +
            Constants.AMOUNT + " text not null, " +
            Constants.DATE + " long);";

    public MyDBhelper(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("MyDBhelper onCreate","Creating all the tables");
        try {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_DEBTS_TABLE);
        } catch(SQLiteException ex) {
            Log.v("Create table exception", ex.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version "+oldVersion
                +" to "+newVersion
                +", which will destroy all old data");
        db.execSQL("drop table if exists "+Constants.TABLE_NAME);
        onCreate(db);
    }
}