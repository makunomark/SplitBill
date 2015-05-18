package com.example.splitbill.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Debts {
    MyDB dba;
    Context context;

    public Debts(Context c) {
        this.context = c;
        dba = new MyDB(context);
    }

    public void add(String debtor_no, String lender_no, int amount) {
        dba.open();
        dba.insertDebtsRecord(debtor_no, lender_no, amount);
        Log.i("Insert to db:(DEBTS)", "::SUCCESS::");
    }

    public Cursor getDebts() {
        dba.open();
        return dba.getDebtsRecords();
    }

    public void deleteRecord(int id){
        dba.open();
        dba.deleteDebtRecord(id);
    }
}