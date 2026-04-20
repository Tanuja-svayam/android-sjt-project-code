package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SalesOrderReceipt_Cash_Activity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText paymodenameET, voucherrefnoET, amountET;
    TextView voucherdtET;

    int year, month, day;

    Button Button_paymodename, btnCreateCashReceipt;
    String syspaymodeno, companycd, sysinvorderno, sysreceiptno,dbdoncustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorderreceipt_cash);

        Button_paymodename = (Button) findViewById(R.id.Button_paymodename);
        btnCreateCashReceipt= (Button) findViewById(R.id.btnCreateCashReceipt);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Button_paymodename:
                        navigatetopaymodenameListActivity();
                        break;
                }
            }
        };
        Button_paymodename.setOnClickListener(handler);

        btnCreateCashReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Submit_CreateCash();
            }
        });

        syspaymodeno = "0";
        sysreceiptno= "0";

        paymodenameET = (EditText)findViewById(R.id.paymodename);
        voucherrefnoET = (EditText)findViewById(R.id.voucherrefno);
        amountET =  (EditText)findViewById(R.id.amount);
        voucherdtET = (TextView)findViewById(R.id.voucherdt);

        Intent i = getIntent();
        if( i.getStringExtra("sysinvorderno") != null)
        {
                sysinvorderno = i.getStringExtra("sysinvorderno");
            Invoke_SalesOrderDetails_Cash(sysreceiptno, sysinvorderno);

        }
        else
        {
            sysinvorderno = "0";
        }

        // Find Error Msg Text View control by ID

        //errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        voucherdtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

       prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


    }

    public void Invoke_SalesOrderDetails_Cash(String dsysreceiptno, String dsysinvorderno) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysreceiptno", "0" +  dsysreceiptno);
            paramsMap.put("sysinvorderno", "0" +  dsysinvorderno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSalesmen_SalesOrderDetails_Cash", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("salesordercashdetails");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sysreceiptno = jsonObject.getString("sysreceiptno");
                        sysinvorderno = jsonObject.getString("sysinvorderno");
                        syspaymodeno = jsonObject.getString("syspaymodeno");
                        companycd = jsonObject.getString("companycd");
                        paymodenameET.setText(jsonObject.getString("paymodename"));
                        voucherrefnoET.setText(jsonObject.getString("voucherrefno"));
                        amountET.setText(jsonObject.getString("amount"));
                        voucherdtET.setText(jsonObject.getString("vvoucherdt"));

                    } catch (JSONException e) {
                        // Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

                                    }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                syspaymodeno = bundle.getString("bankcd");
                paymodenameET.setText(bundle.getString("paymodename"));
            }
        }
    }

    public void navigatetopaymodenameListActivity(){
        Intent homeIntent = new Intent(SalesOrderReceipt_Cash_Activity.this,StockVerificationPaymentMode_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }

    public void Submit_CreateCash(){

        String paymodename = paymodenameET.getText().toString();
        String voucherrefno = voucherrefnoET.getText().toString();
        String amount = amountET.getText().toString();
        String voucherdt = voucherdtET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();
        final String userid  = globalVariable.getuserid();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvorderno) && Utility.isNotNull(syspaymodeno) ) {
            // When Email entered is Valid
            if (Utility.isNotNull(paymodename)) {
                // Put Http parameter username with value of Email Edit View control
                paramsMap.put("sysreceiptno", "0"+sysreceiptno);
                paramsMap.put("sysinvorderno", "0"+sysinvorderno);
                paramsMap.put("syspaymodeno", "0"+syspaymodeno);
                paramsMap.put("companycd", "0"+companycd);
                paramsMap.put("voucherrefno", ""+voucherrefno.toUpperCase());
                paramsMap.put("amount", "0"+amount);
                paramsMap.put("voucherdt", ""+voucherdt);
                paramsMap.put("userid", ""+userid);

                invokeWS(paramsMap);

            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter required product", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SalesOrderReceipt_Add_Cash", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("salesordercashdetails");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            //walkin_custcd = jsonObject.getString("walkin_custcd");

                            sysreceiptno = jsonObject.getString("sysreceiptno");
                            sysinvorderno = jsonObject.getString("sysinvorderno");
                            syspaymodeno = jsonObject.getString("syspaymodeno");
                            companycd = jsonObject.getString("companycd");
                            paymodenameET.setText(jsonObject.getString("paymodename"));
                            voucherrefnoET.setText(jsonObject.getString("voucherrefno"));
                            amountET.setText(jsonObject.getString("amount"));
                            voucherdtET.setText(jsonObject.getString("vvoucherdt"));

                            Toast.makeText(getApplicationContext(), "Cash Added Successfully", Toast.LENGTH_LONG).show();
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                   // NavigateToCustoemerOrder();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(String errorMessage) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    public void setDefaultValues(){
        sysreceiptno = "0";
        sysinvorderno = "0";
        syspaymodeno = "0";
        companycd = "0";
        paymodenameET.setText("");
        voucherrefnoET.setText("");
        amountET.setText("");
        voucherdtET.setText("");
    }

    public void navigatetoFollowupActivity(){
        Intent homeIntent = new Intent(SalesOrderReceipt_Cash_Activity.this,CrmActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("status","PENDING");
        finish();
        startActivity(homeIntent);
    }

    @SuppressWarnings("deprecation")
    public void setvoucherdt(View view) {
        showDialog(997);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub


        if (id == 997) {
            return new DatePickerDialog(this,
                    NextfollowupDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener NextfollowupDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showNextfollowupDate(arg1, arg2, arg3);
                }
            };

    private void showNextfollowupDate(int year, int month, int day) {
        voucherdtET = (TextView) findViewById(R.id.voucherdt);
        voucherdtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SalesOrderReceipt_Cash_Activity.this);
        textView.setPadding(10, 10, 10, 10);

        if((n%2)==0)
        {
            textView.setBackgroundResource(R.drawable.cell_shape);
        }
        else
        {
            textView.setBackgroundResource(R.drawable.cell_shape_oddrow);
        }

        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    private TextView initPlainHeaderTextView() {
        TextView textView = new TextView(SalesOrderReceipt_Cash_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesOrderReceipt_Cash_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}