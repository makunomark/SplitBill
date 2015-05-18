package com.example.splitbill;

import android.app.Application;

import com.example.splitbill.sqlite.Debt;

import java.util.ArrayList;
import java.util.List;

public class Globals extends Application {
    private List<Debt> debtors = new ArrayList<>();
    private List<Contact> contact_list = new ArrayList<>();

    public List<Contact> getContact_list() {
        return contact_list;
    }

    public void setContact_list(List<Contact> contact_list) {
        this.contact_list = contact_list;
    }

    public List<Debt> getDebtors() {
        return debtors;
    }

    public void setDebtors(List<Debt> debtors) {
        this.debtors = debtors;
    }
}
