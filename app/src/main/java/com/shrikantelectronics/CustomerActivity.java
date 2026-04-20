package com.shrikantelectronics;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CustomerActivity extends AppCompatActivity {

    Button btnCustomerList;
    Button btnCustomerReceipt;
    Button btnCustomerOutstanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        btnCustomerList = (Button) findViewById(R.id.btnCustomerList);
        btnCustomerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatetoCustomerViewActivity();
            }
        });

               btnCustomerReceipt= (Button) findViewById(R.id.btnCustomerReceipt);
        btnCustomerReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatetoCustomerReceipt();
            }
        });

        btnCustomerOutstanding= (Button) findViewById(R.id.btnCustomerOutstanding);
        btnCustomerOutstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatetoCustomerOutstanding();
            }
        });
    }

    public void navigatetoCustomerViewActivity(){
        Intent customerIntent = new Intent(CustomerActivity.this,customer_view.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoCustomerReceipt(){
        Intent customerIntent = new Intent(CustomerActivity.this,ReceiptActivity.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoCustomerOutstanding(){
        Intent customerIntent = new Intent(CustomerActivity.this,OutstandingCustomerActivity.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }
}
