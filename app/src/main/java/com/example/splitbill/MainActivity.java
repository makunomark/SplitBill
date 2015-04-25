package com.example.splitbill;

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
import com.example.splitbill.sqlite.Debt;
import com.example.splitbill.sqlite.Debts;

import java.util.ArrayList;
import java.util.List;


/**
 * Author Konrad Gadzinowski
 * kgadzinowski@gmail.com
 */
public class MainActivity extends Fragment{
	
	final int REQUEST_CODE = 100;
	final int AMOUNT_REQUEST_CODE = 1;
	protected List<Contact> contact_list = new ArrayList<>();
	ContactHistory contactHistory;
	TextView amount_tv = null, number_friends_tv = null;
	int amount;
	Globals globals = null;
	TextView emptyView;
	Debts debts;
	private CardView card2;
	private RecyclerView recyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private Button btnPickContact = null, editAmount = null, split = null;
	private ProgressDialog p_dialog;
	private List<Debt> debtors = new ArrayList<>();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.main_activity, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		contactHistory = new ContactHistory(getActivity());
		debts = new Debts(getActivity());
		amount_tv = (TextView) view.findViewById(R.id._display_amount);
		amount_tv.setText("0");
		number_friends_tv= (TextView)getView().findViewById(R.id.textView_number_friends);
		number_friends_tv.setText("0");
		emptyView = (TextView) view.findViewById(R.id.empty_view_1);
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
		globals = (Globals) getActivity().getApplication();
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
		if (contact_list.isEmpty()) {
			recyclerView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
			emptyView.setText("Select friends and add a bill");
		} else {
			recyclerView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		}
	}

	protected  void splitBill(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setMessage("Confirm to sending texts");
		alertDialog.setPositiveButton("Send Text", new DialogInterface.OnClickListener() {
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
		ArrayList<ContactData> contacts;
		Contact contact;
		if(requestCode == REQUEST_CODE) {
			if(resultCode == Activity.RESULT_OK) {
				if(data.hasExtra(ContactData.CONTACTS_DATA)) {
					contacts = data.getParcelableArrayListExtra(ContactData.CONTACTS_DATA);
					if(contacts != null) {
						contact = new Contact();
						contact.setF_name("Me");
						contact.setL_name("");
						contact.setPhone_number("");
						if (Integer.valueOf(amount_tv.getText().toString()) % (contacts.size() + 1) != 0) {
							contact.setAmount((Integer.valueOf(amount_tv.getText().toString()) / (contacts.size() + 1)) + (Integer.valueOf(amount_tv.getText().toString()) % (contacts.size() + 1)));
						} else {
							contact.setAmount(Integer.valueOf(amount_tv.getText().toString()) / (contacts.size() + 1));
						}
						contact_list.add(contact);
						for (ContactData contactData : contacts) {
							contact = new Contact();
							contact.setF_name(contactData.firstname);
							contact.setL_name(contactData.lastname);
							contact.setPhone_number(contactData.phoneNmb);
							contact.setAmount(Integer.valueOf(amount_tv.getText().toString()) / (contacts.size() + 1));
							contact_list.add(contact);
						}

						globals.setContact_list(contact_list);
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
			mAdapter = new MainActivityAdapter(contact_list, getActivity(), getActivity());
			recyclerView.setAdapter(mAdapter);

			if (contact_list.isEmpty()) {
				recyclerView.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText("Select friends and add a bill");
			} else {
				recyclerView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
			}
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
			p_dialog.dismiss();
		}

		@Override
		protected Object doInBackground(Object[] params) {
			contact_list = globals.getContact_list();
			debtors = globals.getDebtors();
			for (Contact contact : contact_list) {
				String message = contact.getF_name() + ", you'll pay Ksh." + contact.getAmount();
				try {
					SmsManager smsManager = SmsManager.getDefault();
					//smsManager.sendTextMessage(contact.getPhone_number(), null, message, null, null);
					contactHistory.add(contact.getF_name(), "" + contact.getAmount());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Debt debt : debtors) {
				debts.add(debt.getDebtor_no(), debt.getLender_no(), Integer.valueOf(debt.getAmount()));
			}
			return null;
		}
	}

}