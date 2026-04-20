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

public class SalesOrderReceipt_Bank_Activity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText banknameET, chequenoET, chequeamtET;
    TextView chequedtET;

    int year, month, day;

    Button btnCreateBankReceipt;
    String companycd, sysinvorderno, syschequeno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorderreceipt_bank);

        btnCreateBankReceipt= (Button) findViewById(R.id.btnCreateBankReceipt);
  btnCreateBankReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Submit_CreateBank();
            }
        });


        syschequeno= "0";

        banknameET = (EditText)findViewById(R.id.bankname);
        chequenoET = (EditText)findViewById(R.id.chequeno);
        chequeamtET =  (EditText)findViewById(R.id.chequeamt);
        chequedtET = (TextView)findViewById(R.id.chequedt);

        Intent i = getIntent();
        if( i.getStringExtra("sysinvorderno") != null)
        {
                sysinvorderno = i.getStringExtra("sysinvorderno");
            Invoke_SalesOrderDetails_Bank(syschequeno, sysinvorderno);

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

        chequedtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

       prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

    }

    public void Invoke_SalesOrderDetails_Bank(String dsyschequeno, String dsysinvorderno) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("syschequeno", "0" +  dsyschequeno);
            paramsMap.put("sysinvorderno", "0" +  dsysinvorderno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSalesmen_SalesOrderDetails_Bank", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("salesorderbankdetails");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        syschequeno = jsonObject.getString("syschequeno");
                        sysinvorderno = jsonObject.getString("sysinvorderno");
                        companycd = jsonObject.getString("companycd");
                        banknameET.setText(jsonObject.getString("bankname"));
                        chequenoET.setText(jsonObject.getString("chequeno"));
                        chequeamtET.setText(jsonObject.getString("chequeamt"));
                        chequedtET.setText(jsonObject.getString("vchequedt"));

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

    public void Submit_CreateBank(){

        String bankname = banknameET.getText().toString();
        String chequeno = chequenoET.getText().toString();
        String chequeamt = chequeamtET.getText().toString();
        String chequedt = chequedtET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();
        final String userid  = globalVariable.getuserid();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvorderno) && Utility.isNotNull(chequeamt) ) {
            // When Email entered is Valid
            if (Utility.isNotNull(chequeno)) {
                // Put Http parameter username with value of Email Edit View control
                paramsMap.put("syschequeno", "0"+syschequeno);
                paramsMap.put("sysinvorderno", "0"+sysinvorderno);
                paramsMap.put("companycd", "0"+companycd);
                paramsMap.put("chequeno", ""+chequeno.toUpperCase());
                paramsMap.put("chequeamt", "0"+chequeamt);
                paramsMap.put("chequedt", ""+chequedt);
                paramsMap.put("bankname", ""+bankname);
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
        ApiHelper.post(URL + "Service1.asmx/SalesOrderReceipt_Add_Bank", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("salesorderbankdetails");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            //walkin_custcd = jsonObject.getString("walkin_custcd");

                            syschequeno = jsonObject.getString("syschequeno");
                            sysinvorderno = jsonObject.getString("sysinvorderno");
                            companycd = jsonObject.getString("companycd");
                            banknameET.setText(jsonObject.getString("bankname"));
                            chequenoET.setText(jsonObject.getString("chequeno"));
                            chequeamtET.setText(jsonObject.getString("chequeamt"));
                            chequedtET.setText(jsonObject.getString("vchequedt"));

                            Toast.makeText(getApplicationContext(), "Cheque Added Successfully", Toast.LENGTH_LONG).show();
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
        syschequeno = "0";
        sysinvorderno = "0";
        companycd = "0";
        banknameET.setText("");
        chequenoET.setText("");
        chequeamtET.setText("");
        chequedtET.setText("");
    }


    @SuppressWarnings("deprecation")
    public void setchequedt(View view) {
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
        chequedtET = (TextView) findViewById(R.id.chequedt);
        chequedtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SalesOrderReceipt_Bank_Activity.this);
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
        TextView textView = new TextView(SalesOrderReceipt_Bank_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesOrderReceipt_Bank_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}