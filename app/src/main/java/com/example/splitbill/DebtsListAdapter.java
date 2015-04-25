package com.example.splitbill;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.splitbill.sqlite.Debt;

import java.util.List;

public class DebtsListAdapter extends RecyclerView.Adapter<DebtsListAdapter.ViewHolder> {
    // Declare Variables

    String lender, debtor;
    private List<Debt> contact_list = null;

    public DebtsListAdapter(List<Debt> contact_list) {
        this.contact_list = contact_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.debts_list_item, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (contact_list.get(position).getLender_no().equals("")) {
            lender = "I";
        } else {
            lender = contact_list.get(position).getLender_no();
        }

        if (contact_list.get(position).getDebtor_no().equals("")) {
            debtor = "me";
        } else {
            debtor = contact_list.get(position).getDebtor_no();
        }


        holder.text.setText(lender + " lended " + debtor + " Ksh." + contact_list.get(position).getAmount() + " on " + contact_list.get(position).getDate());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateResults(List<Debt> results) {
        this.contact_list = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView text;

        public ViewHolder(View v) {
            super(v);
            this.text = (TextView) v.findViewById(R.id.debts_hist_text);
        }
    }
}
