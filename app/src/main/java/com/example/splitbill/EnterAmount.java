package com.example.splitbill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by mark on 4/25/15.
 */
public class EnterAmount extends ActionBarActivity {
    EditText entered_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_amount);

        entered_amount = (EditText)findViewById(R.id.editText_enter_amount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.enter_amount_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_done_enter_amount:
                if(entered_amount.getText().toString().trim().equals("")){
                    Toast.makeText(EnterAmount.this, "Cannot be 0", Toast.LENGTH_LONG).show();
                }else{
                    int e_amount = Integer.parseInt(entered_amount.getText().toString().trim());
                    returnData(e_amount);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnData(int e_amount) {
        Intent result = new Intent();
        result.putExtra("amount", e_amount);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
