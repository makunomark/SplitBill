package com.example.splitbill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitbill.sqlite.ContactHistory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Author Konrad Gadzinowski
 * kgadzinowski@gmail.com
 */
public class MainActivity extends Fragment{
	
	final int REQUEST_CODE = 100;
	final int AMOUNT_REQUEST_CODE = 1;

	CardView card2;
	RecyclerView recyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	Button btnPickContact = null, editAmount = null, split = null;
	int divided_amount;
	protected List<Contact> contact_list = new ArrayList<Contact>();
	private ProgressDialog p_dialog;
	ContactHistory contactHistory;
	TextView amount_tv = null, number_friends_tv=null;
	int amount;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.main_activity, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		contactHistory = new ContactHistory(getActivity());

		amount_tv = (TextView)getView().findViewById(R.id._display_amount);
		amount_tv.setText("0");
		number_friends_tv= (TextView)getView().findViewById(R.id.textView_number_friends);
		number_friends_tv.setText("0");
		card2 = (CardView)getView().findViewById(R.id.card2);
		btnPickContact = (Button) getView().findViewById(R.id.btnPick);
		split = (Button)getView().findViewById(R.id.button_split_bill);
		editAmount = (Button)getView().findViewById(R.id.button_edit_amount);
		recyclerView = (RecyclerView)getView().findViewById(R.id.main_activity_recycler_view);
		mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		editAmount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent m = new Intent(getActivity(), EnterAmount.class);
				startActivityForResult(m, AMOUNT_REQUEST_CODE);
			}
		});

		setUi();
		split.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (amount == 0) {
					Toast.makeText(getActivity(), "Enter an amount to split", Toast.LENGTH_LONG).show();
				} else if (contact_list.size() == 0) {
					Toast.makeText(getActivity(), "Select friends", Toast.LENGTH_LONG).show();
				} else {
					splitBill();
				}
			}
		});
	}

	protected  void splitBill(){
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setMessage("Confirm to sending texts");
		alertDialog.setButton("Send Text", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new SendMessage().execute();
			}
		});
		alertDialog.show();
	}

	public void setUi() {

		btnPickContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent contactPicker = new Intent(getActivity(), ContactPickerActivity.class);
				//contactPicker.putExtra(ContactData.CHECK_ALL, cbCheckAll.isChecked());
				contactPicker.setPackage("com.reptilemobile.MultipleContactsPicker");
				startActivityForResult(contactPicker, REQUEST_CODE);
			}
		});

	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(contact_list.size()>0){
			contact_list.clear();
		}
		ArrayList<ContactData> contacts = new ArrayList<>();
		divided_amount = amount/(contact_list.size()+1);
		if(requestCode == REQUEST_CODE) {
			if(resultCode == Activity.RESULT_OK) {
				if(data.hasExtra(ContactData.CONTACTS_DATA)) {
					contacts = data.getParcelableArrayListExtra(ContactData.CONTACTS_DATA);
					divided_amount = amount/(contact_list.size()+1);
					if(contacts != null) {
						Iterator<ContactData> iterContacts = contacts.iterator();
						while(iterContacts.hasNext()) {
							ContactData contactData = iterContacts.next();
							Contact contact = new Contact();
							contact.setF_name(contactData.firstname);
							contact.setL_name(contactData.lastname);
							contact.setPhone_number(contactData.phoneNmb);
							contact.setAmount(divided_amount);
							contact_list.add(contact);
						}
						number_friends_tv.setText(String.valueOf(contact_list.size()));
					}
				}
			}
		}else if (requestCode == AMOUNT_REQUEST_CODE){
			if(resultCode == Activity.RESULT_OK){
				if(data.hasExtra("amount")){
					amount = data.getIntExtra("amount", 0);
					amount_tv.setText(String.valueOf(amount));
				}
			}
		}
		if(amount != 0 && contact_list.size() != 0 ){
			mAdapter = new MainActivityAdapter(contact_list, getActivity());
			recyclerView.setAdapter(mAdapter);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class SendMessage extends AsyncTask{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			p_dialog = new ProgressDialog(getActivity());
			p_dialog.setMessage("Sending message...");
			p_dialog.setCancelable(false);
			p_dialog.show();
		}

		@Override
		protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			p_dialog.dismiss();
		}

		@Override
		protected Object doInBackground(Object[] params) {
			for(int x = 0; x < contact_list.size(); x++){
				String message = contact_list.get(x).getF_name()+", you'll pay Ksh."+ contact_list.get(x).getAmount();
				try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(contact_list.get(x).getPhone_number(), null, message, null, null);

					contactHistory.add(contact_list.get(x).getF_name(), "" + contact_list.get(x).getAmount());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

}
