package com.shrikantelectronics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import androidx.appcompat.app.AppCompatActivity;
import com.shrikantelectronics.R;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private AlertDialog.Builder alertDialogBuilder;

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String closing, emailid, mobile, allowed_concerent_mobile_users, closing_for_gateway;
    TextView tvamount, tvproductname, tvsvayam_paymentreceipt;

    int sysinvno = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);

        Intent i = getIntent();
        closing = i.getStringExtra("closing");
        emailid = i.getStringExtra("emailid");
        mobile = i.getStringExtra("mobile");
        allowed_concerent_mobile_users = i.getStringExtra("allowed_concerent_mobile_users");
        closing_for_gateway= i.getStringExtra("closing_for_gateway");


        //gateway_closing = (Double.valueOf(closing)* 100);

        /*
          To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
        */

        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout
        Button button = (Button) findViewById(R.id.btn_pay);
        Button buttonskip = (Button) findViewById(R.id.btn_skip);

        TextView tvamount = (TextView) findViewById(R.id.amount);
        TextView tvproductname = (TextView) findViewById(R.id.productname);
        TextView tvsvayam_paymentreceipt = (TextView) findViewById(R.id.svayam_paymentreceipt);


        alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");

        tvamount.setText(closing);
        tvproductname.setText("");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

        buttonskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipPayment();
            }
        });

    }

    public void startPayment() {
        /*
         You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

            try {
                JSONObject options = new JSONObject();
                options.put("name", "Shreya Software Services Pvt. Ltd.");
                options.put("description", "AMC");
                options.put("send_sms_hash",true);
                options.put("allow_rotation", true);
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "http://184.168.121.181/SvayamImages/svayamlogo.png");
                options.put("currency", "INR");
                options.put("amount", closing_for_gateway);

                JSONObject preFill = new JSONObject();
                preFill.put("email", emailid);
                preFill.put("contact", mobile);

                options.put("prefill", preFill);

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }
        }


    public void skipPayment() {
        /*
         You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            activity.finish();
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

     /*
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */

    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try{

            Create_GatewayReceiptInSvayamMgmt(s, paymentData);

//           alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: "+s+"\nPayment Data: "+paymentData.getData());
  //         alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try{

            finish();
            Intent intent = new Intent(PaymentActivity.this, PaymentSuccessfullActivity.class);
            intent.putExtra("sysinvno", sysinvno);
            intent.putExtra("closing", closing);
            intent.putExtra("PaymentReferenceNumber", "Payment Failed");
            intent.putExtra("emailid", emailid);
            intent.putExtra("mobile", mobile);

            startActivity(intent);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void Create_GatewayReceiptInSvayamMgmt(String s, PaymentData paymentData) {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("sysinvno", "0" +  sysinvno);
            paramsMap.put("razorpayrefno",s);
            paramsMap.put("receiptamount", "0" + closing);
            paramsMap.put("clientid", GlobalClass.CLIENTID);
            paramsMap.put("subscriptionid", GlobalClass.SUBSCRIPTIONID);
            paramsMap.put("userid", "" + userid);
            paramsMap.put("apiresponse", s);

        invokeCreateGatewayReceiptInSvayamMgmtWS(paramsMap);

    }

    public void invokeCreateGatewayReceiptInSvayamMgmtWS(Map<String, String> paramsMap){
        // Show Progress Dialog
       // prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Create_receipt_for_PaymentGateway", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
             //   prgDialog.hide();
                try {

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("svayam_paymentgatewayreceipt");
                    JSONObject jsonObject = new_array.getJSONObject(0);
                    //
                    // When the JSON response has status boolean value assigned with true
                    //
                    if(new_array.length()>0) {
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                        TextView tvsvayam_paymentreceipt = (TextView) findViewById(R.id.svayam_paymentreceipt);
                        tvsvayam_paymentreceipt.setGravity(Gravity.CENTER_HORIZONTAL);

                        tvsvayam_paymentreceipt.setText("Svayam Receipt Created Sucessfully \nReceipt Number : " + jsonObject.getString("PaymentReferenceNumber").toString() + "\nInvoice will be delivered on registered email and mobile \nThank You." );

                        String PaymentReferenceNumber = jsonObject.getString("PaymentReferenceNumber").toString();

                        finish();
                        Intent intent = new Intent(PaymentActivity.this, PaymentSuccessfullActivity.class);
                        intent.putExtra("sysinvno", sysinvno);
                        intent.putExtra("closing", closing);
                        intent.putExtra("PaymentReferenceNumber", PaymentReferenceNumber);
                        intent.putExtra("emailid", emailid);
                        intent.putExtra("mobile", mobile);

                        startActivity(intent);

                    }


                    } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200',
            @Override
            public void onFailure(String errorMessage) {
                // Hide Progress Dialog
             //   prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

}
