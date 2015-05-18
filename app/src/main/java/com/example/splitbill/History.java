package com.example.splitbill;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.splitbill.sqlite.Constants;
import com.example.splitbill.sqlite.ContactHistory;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends Fragment{
    RecyclerView recyclerView;
    ContactHistory contactHistory;
    TextView emptyView;
    List<Contact> contactlist = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog p_dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.split_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyView = (TextView) view.findViewById(R.id.empty_view_2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        contactHistory = new ContactHistory(getActivity());
        new LoadItems().execute();


    }

    public class LoadItems extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Cursor c = contactHistory.getHist();
            String name, amount, date;
            if(c.moveToFirst()){
                do{
                    Contact contact = new Contact();
                    name = c.getString(c.getColumnIndex(Constants.PERSON_NAME));
                    amount = c.getString(c.getColumnIndex(Constants.AMOUNT));
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    date = dateFormat.format(new Date(c.getLong(c.getColumnIndex(Constants.DATE))).getTime());
                    contact.setF_name(name);
                    contact.setAmount(Integer.parseInt(amount));
                    contact.setDate_time(String.valueOf(date));
                    contactlist.add(contact);
                } while(c.moveToNext());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            p_dialog = new ProgressDialog(getActivity());
            p_dialog.setMessage("Loading history items");
            p_dialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setAdapter();

            if (contactlist.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Seems like you haven't split bills yet");
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }

            p_dialog.dismiss();
        }

        public void setAdapter() {
            mAdapter = new ListViewAdapter(contactlist);
            recyclerView.setAdapter(mAdapter);
        }
    }


}