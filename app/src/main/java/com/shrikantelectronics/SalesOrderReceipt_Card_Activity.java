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


public class SalesOrderReceipt_Card_Activity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText tiddescriptionET, documentnoET, trnamtET, cashbackET, intervationET, batchnoET, approvalcodeET;
    TextView swipedtET;

    int year, month, day;

    Button Button_tiddescription, btnCreateCardReceipt;
    String systidno, companycd, sysinvorderno, systidtrnno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorderreceipt_card);

        Button_tiddescription = (Button) findViewById(R.id.Button_tiddescription);
        btnCreateCardReceipt= (Button) findViewById(R.id.btnCreateCardReceipt);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Button_tiddescription:
                        navigatetotiddescriptionListActivity();
                        break;
                }
            }
        };
        Button_tiddescription.setOnClickListener(handler);

        btnCreateCardReceipt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Submit_CreateCash();
            }
        });

        systidno = "0";
        systidtrnno= "0";

        tiddescriptionET = (EditText)findViewById(R.id.tiddescription);
        documentnoET = (EditText)findViewById(R.id.documentno);
        trnamtET =  (EditText)findViewById(R.id.trnamt);
        cashbackET =  (EditText)findViewById(R.id.cashback);
        intervationET=  (EditText)findViewById(R.id.intervation);
        swipedtET = (TextView)findViewById(R.id.swipedt);
        batchnoET =  (EditText)findViewById(R.id.batchno);
        approvalcodeET =  (EditText)findViewById(R.id.approvalcode);

        Intent i = getIntent();
        if( i.getStringExtra("sysinvorderno") != null)
        {
            sysinvorderno = i.getStringExtra("sysinvorderno");
            Invoke_SalesOrderDetails_Card(systidtrnno, sysinvorderno);
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

        swipedtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

       prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

    }

    public void Invoke_SalesOrderDetails_Card(String dsystidtrnno, String dsysinvorderno) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("systidtrnno", "0" +  dsystidtrnno);
            paramsMap.put("sysinvorderno", "0" +  dsysinvorderno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSalesmen_SalesOrderDetails_Card", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("salesordercarddetails");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        systidtrnno = jsonObject.getString("systidtrnno");
                        sysinvorderno = jsonObject.getString("sysinvorderno");
                        systidno = jsonObject.getString("systidno");
                        companycd = jsonObject.getString("companycd");
                        tiddescriptionET.setText(jsonObject.getString("tiddescription"));
                        documentnoET.setText(jsonObject.getString("documentno"));
                        trnamtET.setText(jsonObject.getString("trnamt"));
                        cashbackET.setText(jsonObject.getString("cashback"));
                        intervationET.setText(jsonObject.getString("interest_intervention"));
                        swipedtET.setText(jsonObject.getString("vswipedt"));
                        batchnoET.setText(jsonObject.getString("batchno"));
                        approvalcodeET.setText(jsonObject.getString("approvalcode"));

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
                systidno = bundle.getString("systidno");
                tiddescriptionET.setText(bundle.getString("tiddescription"));
            }
        }
    }

    public void navigatetotiddescriptionListActivity(){
        Intent homeIntent = new Intent(SalesOrderReceipt_Card_Activity.this,StockVerificationTidMaster_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }

    public void Submit_CreateCash(){

        String tiddescription = tiddescriptionET.getText().toString();
        String documentno = documentnoET.getText().toString();
        String trnamt = trnamtET.getText().toString();
        String cashback = cashbackET.getText().toString();
        String intervation = intervationET.getText().toString();
        String swipedt = swipedtET.getText().toString();
        String batchno = batchnoET.getText().toString();
        String approvalcode = approvalcodeET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();
        final String userid  = globalVariable.getuserid();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvorderno) && Utility.isNotNull(systidno) ) {
            // When Email entered is Valid
            if (Utility.isNotNull(tiddescription)) {
                // Put Http parameter username with value of Email Edit View control
                paramsMap.put("systidtrnno", "0"+systidtrnno);
                paramsMap.put("sysinvorderno", "0"+sysinvorderno);
                paramsMap.put("systidno", "0"+systidno);
                paramsMap.put("companycd", "0"+companycd);
                paramsMap.put("documentno", ""+documentno.toUpperCase());
                paramsMap.put("trnamt", "0"+trnamt);
                paramsMap.put("cashback", "0"+cashback);
                paramsMap.put("intervation", "0"+intervation);
                paramsMap.put("swipedt", ""+swipedt);
                paramsMap.put("batchno", ""+batchno);
                paramsMap.put("approvalcode", ""+approvalcode);
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
        ApiHelper.post(URL + "Service1.asmx/SalesOrderReceipt_Add_Card", paramsMap, new ApiHelper.ApiCallback() {

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

                            systidtrnno = jsonObject.getString("systidtrnno");
                            sysinvorderno = jsonObject.getString("sysinvorderno");
                            systidno = jsonObject.getString("systidno");
                            companycd = jsonObject.getString("companycd");
                            tiddescriptionET.setText(jsonObject.getString("tiddescription"));
                            documentnoET.setText(jsonObject.getString("documentno"));
                            trnamtET.setText(jsonObject.getString("trnamt"));
                            cashbackET.setText(jsonObject.getString("cashback"));
                            intervationET.setText(jsonObject.getString("interest_intervention"));
                            swipedtET.setText(jsonObject.getString("vswipedt"));
                            batchnoET.setText(jsonObject.getString("batchno"));
                            approvalcodeET.setText(jsonObject.getString("approvalcode"));

                            Toast.makeText(getApplicationContext(), "Card Added Successfully", Toast.LENGTH_LONG).show();

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
        systidtrnno = "0";
        sysinvorderno = "0";
        systidno = "0";
        companycd = "0";
        tiddescriptionET.setText("");
        documentnoET.setText("");
        trnamtET.setText("");
        cashbackET.setText("");
        intervationET.setText("");
        swipedtET.setText("");
        batchnoET.setText("");
        approvalcodeET.setText("");
    }

    @SuppressWarnings("deprecation")
    public void setswipedt(View view) {
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
        swipedtET = (TextView) findViewById(R.id.swipedt);
        swipedtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SalesOrderReceipt_Card_Activity.this);
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
        TextView textView = new TextView(SalesOrderReceipt_Card_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesOrderReceipt_Card_Activity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}