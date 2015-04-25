package com.example.splitbill.sqlite;

public class Debt {
    String lender_no;
    String debtor_no;
    String amount;
    String date;
    int id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLender_no() {
        return lender_no;
    }

    public void setLender_no(String lender_no) {
        this.lender_no = lender_no;
    }

    public String getDebtor_no() {
        return debtor_no;
    }

    public void setDebtor_no(String debtor_no) {
        this.debtor_no = debtor_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
