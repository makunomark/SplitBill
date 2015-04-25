package com.example.splitbill.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by mark on 4/23/15.
 */
public class ContactHistory {
    MyDB dba;
    Context context;
    /*Cart(String product_no, String desc, int unit_price, int quantity){
        this.product_no = product_no;
        this.desc = desc;
        this.unit_price = unit_price;
        this.quantity = quantity;
    }
    */
    public ContactHistory(Context c){
        this.context =c;
        dba = new MyDB(context);
    }

    public void add(String name, String amount){
        //TODO compute for quantity
        dba.open();
        dba.insertRecord(name, amount);
        Log.i("Insert to db:(CART)", "::SUCCESS::");
    }

    public Cursor getHist(){
        dba.open();
        Cursor c = dba.getRecords();
        return c;
    }
}
