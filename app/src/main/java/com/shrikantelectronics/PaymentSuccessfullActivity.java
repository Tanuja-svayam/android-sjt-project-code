package com.shrikantelectronics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccessfullActivity extends AppCompatActivity  {
    private static final String TAG = PaymentSuccessfullActivity.class.getSimpleName();
    private AlertDialog.Builder alertDialogBuilder;

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String sysinvno, closing, emailid, mobile, PaymentReferenceNumber;
    TextView  tvsvayam_paymentreceipt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paymentsuccessfull);


        Intent i = getIntent();

        sysinvno = i.getStringExtra("sysinvno");
        closing = i.getStringExtra("closing");
        PaymentReferenceNumber = i.getStringExtra("PaymentReferenceNumber");
        emailid = i.getStringExtra("emailid");
        mobile = i.getStringExtra("mobile");


        Button buttonskip = (Button) findViewById(R.id.btn_skip);


        alertDialogBuilder = new AlertDialog.Builder(PaymentSuccessfullActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");


        TextView tvsvayam_paymentreceipt = (TextView) findViewById(R.id.svayam_paymentreceipt);
        tvsvayam_paymentreceipt.setGravity(Gravity.CENTER_HORIZONTAL);
        tvsvayam_paymentreceipt.setText("Svayam received of rs. "  + closing + " sucessfully \n\nReceipt Number : " + PaymentReferenceNumber + "\n\nCheck registered e-mail and whatsapp for Invoice \nApplication wiil restart now..." + "\n Thank You.");


        buttonskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipPayment();
            }
        });

    }


    public void skipPayment() {

        final Activity activity = this;

       try {
            Intent homeIntent = new Intent(getApplicationContext(),login.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

}
