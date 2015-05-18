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
import com.example.splitbill.sqlite.Debt;
import com.example.splitbill.sqlite.Debts;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebtsList extends Fragment {
    RecyclerView recyclerView;
    Debts debts;
    String lender_no;
    String debtor_no;
    String amount;
    String date;
    int id;
    TextView emptyView;
    private ProgressDialog p_dialog;
    private List<Debt> debtors = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.split_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        emptyView = (TextView) view.findViewById(R.id.empty_view_2);
        recyclerView.setLayoutManager(mLayoutManager);
        debts = new Debts(getActivity());
        new RetrieveDebtsList().execute();
        super.onViewCreated(view, savedInstanceState);
    }

    public class RetrieveDebtsList extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p_dialog = new ProgressDialog(getActivity());
            p_dialog.setMessage("Getting debts list...");
            p_dialog.setCancelable(false);
            p_dialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            setAdapter();
            if (debtors.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("Pay for someone, to have them on the debts list");
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            p_dialog.dismiss();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Cursor c = debts.getDebts();
            if (c.moveToFirst()) {
                do {
                    Debt debt = new Debt();
                    id = c.getInt(c.getColumnIndex(Constants.KEY_ID));
                    debtor_no = c.getString(c.getColumnIndex(Constants.DEBTOR_NO));
                    lender_no = c.getString(c.getColumnIndex(Constants.LENDER_NO));
                    amount = c.getString(c.getColumnIndex(Constants.AMOUNT));
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    date = dateFormat.format(new Date(c.getLong(c.getColumnIndex(Constants.DATE))).getTime());
                    debt.setAmount(amount);
                    debt.setDebtor_no(debtor_no);
                    debt.setDate(date);
                    debt.setId(id);
                    debt.setLender_no(lender_no);
                    debtors.add(debt);
                } while (c.moveToNext());
            }
            return null;
        }

        public void setAdapter() {
            mAdapter = new DebtsListAdapter(debtors, getActivity());
            recyclerView.setAdapter(mAdapter);
        }

    }
}
