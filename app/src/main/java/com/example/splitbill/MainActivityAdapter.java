package com.example.splitbill;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitbill.sqlite.ContactHistory;

import java.util.List;

/**
 * Created by mark on 4/25/15.
 */
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder>{
    // Declare Variables

    public List<Contact> contact_list = null;
    private Context context;
    public MainActivityAdapter(List<Contact> contact_list, Context context) {
        this.contact_list = contact_list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected TextView amount;
        protected Button payfor;
        public ViewHolder(View v){
            super(v);
            this.name = (TextView)v.findViewById(R.id.textView_name);
            this.amount = (TextView)v.findViewById(R.id.textView_amount);
            this.payfor = (Button)v.findViewById(R.id.button_pay_for);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_list_item, null);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(contact_list.get(position).getF_name());
        holder.amount.setText(String.valueOf(contact_list.get(position).getAmount()));
        holder.payfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int item_id = (int)getItemId(position);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                //AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Select who to pay for");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setTitle("Select One Name");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        context,
                        android.R.layout.select_dialog_singlechoice);
                for(int x = 0; x<contact_list.size();x++){
                    if(!contact_list.get(item_id).getF_name().equals(contact_list.get(x).getF_name())) {
                        arrayAdapter.add(contact_list.get(x).getF_name());
                    }
                }
                alertDialog.setAdapter(arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {

                                // get prompts.xml view
                                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                                    View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                    alertDialogBuilder.setView(promptView);

                                    final EditText editText = (EditText) promptView.findViewById(R.id.amount);
                                    // setup a dialog window
                                    alertDialogBuilder.setCancelable(true)
                                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    int amount = Integer.parseInt(editText.getText().toString().trim());
                                                    Toast.makeText(context, contact_list.get(item_id).getF_name()+" shall pay "+amount+" for "+ arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                                    int old_paying_amount = contact_list.get(item_id).getAmount();
                                                    contact_list.get(item_id).setAmount(old_paying_amount + amount);

                                                    for(int m = 0; m < contact_list.size(); m++){
                                                        if(contact_list.get(m).getF_name().equals(arrayAdapter.getItem(which))){
                                                            contact_list.get(m).setAmount(contact_list.get(m).getAmount() - amount);
                                                        }
                                                    }
                                                    updateResults(contact_list);
                                                }
                                            });
                                    AlertDialog alert = alertDialogBuilder.create();
                                    alert.show();
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

    public void updateResults(List<Contact> results) {
        this.contact_list = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }
}