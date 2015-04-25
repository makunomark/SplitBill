package com.example.splitbill;

/**
 * Created by mark on 3/22/15.
 */
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    // Declare Variables

    private List<Contact> contact_list = null;

    public ListViewAdapter(List<Contact> contact_list) {
        this.contact_list = contact_list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected TextView amount;
        private TextView date;
        public ViewHolder(View v){
            super(v);
            this.name = (TextView)v.findViewById(R.id.textView_name);
            this.amount = (TextView)v.findViewById(R.id.textView_amount);
            this.date = (TextView)v.findViewById(R.id.textView_date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(contact_list.get(position).getF_name());
        holder.amount.setText(String.valueOf(contact_list.get(position).getAmount()));
        holder.date.setText(contact_list.get(position).getDate_time());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateResults(List<Contact> results) {
        this.contact_list = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }
}
