package com.example.splitbill;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.splitbill.sqlite.Debt;
import com.example.splitbill.sqlite.Debts;

import org.w3c.dom.Text;

import java.util.List;

public class DebtsListAdapter extends RecyclerView.Adapter<DebtsListAdapter.ViewHolder> {
    // Declare Variables

    String lender, debtor;
    private List<Debt> contact_list = null;
    Debts debts;
    Context context;
    int id = 0;
    public DebtsListAdapter(List<Debt> contact_list, Context context) {
        this.contact_list = contact_list;
        this.context = context;
        debts = new Debts(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.debts_list_item, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (contact_list.get(position).getLender_no().equals("")) {
            lender = "Me";
        } else {
            lender = contact_list.get(position).getLender_no();
        }

        if (contact_list.get(position).getDebtor_no().equals("")) {
            debtor = "Me";
        } else {
            debtor = contact_list.get(position).getDebtor_no();
        }


        //holder.text.setText(lender + " id " + contact_list.get(position).getId() + " Ksh." + contact_list.get(position).getAmount() + " on " + contact_list.get(position).getDate());
        holder.date.setText("Date: " + contact_list.get(position).getDate());
        holder.amount.setText("Amount: " + contact_list.get(position).getAmount());
        holder.debtor.setText("Debtor: " + debtor);
        holder.lender.setText("Lender: " + lender);
        holder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = contact_list.get(position).getId();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Record will be deleted");
                final AlertDialog.Builder ok = alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        debts.deleteRecord(id);
                        for (int i = 0; contact_list.size() > i; i++) {
                            if (contact_list.get(i).getId() == id) {
                                contact_list.remove(i);
                                updateResults(contact_list);
                            }
                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateResults(List<Debt> contact_list) {
        this.contact_list = contact_list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView debtor, lender, date, amount;
        protected Button clear;

        public ViewHolder(View v) {
            super(v);
            this.debtor = (TextView) v.findViewById(R.id.debts_debtor);
            this.lender = (TextView) v.findViewById(R.id.debts_lender);
            this.amount = (TextView) v.findViewById(R.id.debts_amount);
            this.date = (TextView) v.findViewById(R.id.debts_date);
            this.clear = (Button) v.findViewById(R.id.debts_clear_button);
        }
    }
}
