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

public class SalesOrderReceipt_Wallet_Activity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText documentnoET, digitalloanamtET, approvalcodeET, digitalcompanynameET;
    TextView digitalloandtET;

    int year, month, day;

    Button Button_digitalcompanyname, btnCreateWalletReceipt;
    String digitalcompanycd, companycd, sysinvorderno, sysdigitalloanno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorderreceipt_wallet);

        Button_digitalcompanyname = (Button) findViewById(R.id.Button_digitalcompanyname);
        btnCreateWalletReceipt= (Button) findViewById(R.id.btnCreateWalletReceipt);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Button_digitalcompanyname:
                        navigatetodigitalcompanynameListActivity();
                        break;
                }
            }
        };
        Button_digitalcompanyname.setOnClickListener(handler);

        btnCreateWalletReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Submit_CreateWallet();
            }
        });

        digitalcompanycd = "0";
        sysdigitalloanno= "0";

        documentnoET = (EditText)findViewById(R.id.documentno);
        digitalloandtET = (TextView)findViewById(R.id.digitalloandt);
        approvalcodeET =  (EditText)findViewById(R.id.approvalcode);
        digitalcompanynameET = (EditText)findViewById(R.id.digitalcompanyname);
        digitalloanamtET =  (EditText)findViewById(R.id.digitalloanamt);

        Intent i = getIntent();
        if( i.getStringExtra("sysinvorderno") != null)
        {
            sysinvorderno = i.getStringExtra("sysinvorderno");
            Invoke_SalesOrderDetails_Wallet(sysdigitalloanno, sysinvorderno);

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

        digitalloandtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

    }

    public void Invoke_SalesOrderDetails_Wallet(String dsysdigitalloanno, String dsysinvorderno) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysdigitalloanno", "0" +  dsysdigitalloanno);
            paramsMap.put("sysinvorderno", "0" +  dsysinvorderno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSalesmen_SalesOrderDetails_Wallet", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("salesordercarddetails");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sysdigitalloanno = jsonObject.getString("sysdigitalloanno");
                        sysinvorderno = jsonObject.getString("sysinvorderno");
                        digitalcompanycd = jsonObject.getString("digitalcompanycd");
                        companycd = jsonObject.getString("companycd");

                        documentnoET.setText(jsonObject.getString("documentno"));
                        digitalloandtET.setText(jsonObject.getString("vdigitalloandt"));
                        approvalcodeET.setText(jsonObject.getString("approvalcode"));
                        digitalcompanynameET.setText(jsonObject.getString("digitalcompanyname"));
                        digitalloanamtET.setText(jsonObject.getString("digitalloanamt"));

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
                digitalcompanycd = bundle.getString("digitalcompanycd");
                digitalcompanynameET.setText(bundle.getString("digitalcompanyname"));
            }
        }
    }

    public void navigatetodigitalcompanynameListActivity(){
        Intent homeIntent = new Intent(SalesOrderReceipt_Wallet_Activity.this,StockVerificationWallet_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }

    public void Submit_CreateWallet(){

        String digitalcompanyname = digitalcompanynameET.getText().toString();
        String documentno = documentnoET.getText().toString();
        String digitalloanamt = digitalloanamtET.getText().toString();
        String digitalloandt = digitalloandtET.getText().toString();
        String approvalcode = approvalcodeET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();
        final String userid  = globalVariable.getuserid();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvorderno) && Utility.isNotNull(digitalcompanycd) ) {
            // When Email entered is Valid
            if (Utility.isNotNull(digitalcompanyname)) {
                // Put Http parameter username with value of Email Edit View control
                paramsMap.put("sysdigitalloanno", "0"+sysdigitalloanno);
                paramsMap.put("sysinvorderno", "0"+sysinvorderno);
                paramsMap.put("digitalcompanycd", "0"+digitalcompanycd);
                paramsMap.put("companycd", "0"+companycd);
                paramsMap.put("documentno", ""+documentno.toUpperCase());
                paramsMap.put("digitalloandt", ""+digitalloandt);
                paramsMap.put("approvalcode", ""+approvalcode);
                paramsMap.put("digitalloanamt", "0"+digitalloanamt);
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
        ApiHelper.post(URL + "Service1.asmx/SalesOrderReceipt_Add_Wallet", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("salesordercarddetails");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            //walkin_custcd = jsonObject.getString("walkin_custcd");
                            sysdigitalloanno = jsonObject.getString("sysdigitalloanno");
                            sysinvorderno = jsonObject.getString("sysinvorderno");
                            digitalcompanycd = jsonObject.getString("digitalcompanycd");
                            companycd = jsonObject.getString("companycd");
                            documentnoET.setText(jsonObject.getString("documentno"));
                            digitalloandtET.setText(jsonObject.getString("vdigitalloandt"));
                            approvalcodeET.setText(jsonObject.getString("approvalcode"));
                            digitalcompanynameET.setText(jsonObject.getString("digitalcompanyname"));
                            digitalloanamtET.setText(jsonObject.getString("digitalloanamt"));

                            Toast.makeText(getApplicationContext(), "Wallet Added Successfully", Toast.LENGTH_LONG).show();
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
        sysdigitalloanno = "0";
        sysinvorderno = "0";
        digitalcompanycd = "0";
        companycd = "0";
        documentnoET.setText("");
        digitalloandtET.setText("");
        approvalcodeET.setText("");
        digitalcompanynameET.setText("");
        digitalloanamtET.setText("");
    }

    @SuppressWarnings("deprecation")
    public void setdigitalloandt(View view) {
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
                    DigitalLoanDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener DigitalLoanDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDigitalLoanDate(arg1, arg2, arg3);
                }
            };

    private void showDigitalLoanDate(int year, int month, int day) {
        digitalloandtET = (TextView) findViewById(R.id.digitalloandt);
        digitalloandtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SalesOrderReceipt_Wallet_Activity.this);
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
        TextView textView = new TextView(SalesOrderReceipt_Wallet_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesOrderReceipt_Wallet_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}