package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import android.content.Intent;


public class Event_Visitor extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    Button Button_custtype;
    EditText consignor_address1ET ;
    EditText consignor_address2ET ;
    EditText consignor_alternamemobilenoET ;
    EditText consignor_mobilenoET ;
    EditText consignor_nameET ;
    EditText consignor_pincodeET ;
    EditText custtypenameET ;
    ProgressDialog prgDialog;
    String walkin_custcd="0", syscustactno="0", custtype="";
    TextView errorMsg;
    TextView visitercountET;
    final String consignor_companycd  = "20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_visitor);

        consignor_nameET = (EditText)findViewById(R.id.name);
        consignor_mobilenoET = (EditText)findViewById(R.id.mobileno);
        consignor_alternamemobilenoET = (EditText)findViewById(R.id.alternamemobileno);
        custtypenameET = (EditText)findViewById(R.id.custtypename);
         consignor_address1ET = (EditText)findViewById(R.id.address1);
        consignor_address2ET = (EditText)findViewById(R.id.address2);
        consignor_pincodeET = (EditText)findViewById(R.id.pincode);
        visitercountET = (TextView) findViewById(R.id.visitercount);

        Intent i = getIntent();
        if( i.getStringExtra("walkin_custcd") != null)
        {
            walkin_custcd = i.getStringExtra("walkin_custcd");
        }
        else
        {
            walkin_custcd = "0";
        }

        if( i.getStringExtra("syscustactno") != null)
        {
            syscustactno = i.getStringExtra("syscustactno");
        }
        else
        {
            syscustactno = "0";
        }

        String mobileno;
        mobileno = i.getStringExtra("mobileno");
        consignor_mobilenoET.setText(mobileno);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String default_state  = globalVariable.getdefault_state();


        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {



                    case R.id.Button_custtype:
                        navigatetoCustTypeListActivity();
                        break;


                }
            }
        };

        Button_custtype = (Button) findViewById(R.id.Button_custtype);
        Button_custtype.setOnClickListener(handler);

        if(!walkin_custcd.equals("0")) {
            Invoke_CustomerDetailsSingle();
        }

        Invoke_VisiterCount();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_model, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_New) {
            setDefaultValues();
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigatetoCustTypeListActivity(){
        Intent homeIntent = new Intent(Event_Visitor.this,StockVerificationCustomerType_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                custtype = bundle.getString("gccode");
                custtypenameET.setText(bundle.getString("gcname"));
            }
        }
    }

    public void Invoke_CustomerDetailsSingle() {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("walkin_custcd", "0" +  walkin_custcd);
            paramsMap.put("customer_custcd", "0" );
            paramsMap.put("syscustactno", "0" +  syscustactno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/SalemenGetFollowupCustomerDetailsSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        consignor_nameET.setText(jsonObject.getString("custname"));
                        consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));
                        consignor_address1ET.setText(jsonObject.getString("address1"));
                        consignor_address2ET.setText(jsonObject.getString("address2"));
                        consignor_pincodeET.setText(jsonObject.getString("pincode"));
                        consignor_alternamemobilenoET.setText(jsonObject.getString("telephone"));
                        custtype = jsonObject.getString("custtype");
                        custtypenameET.setText(jsonObject.getString("custtypedesc"));

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

    public void Invoke_VisiterCount() {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("companycd", "0" +  consignor_companycd);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/EventManager_Get_VIsitorCount", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("crm_daily_walkin");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        visitercountET.setText("Visitors : " + jsonObject.getString("visitorcount"));

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

    public void onClick_submit(View view) {
        Submit_CreateWalkin();
    }

    public void Submit_CreateWalkin(){

        // Get Email Edit View Value

        String consignor_name = consignor_nameET.getText().toString();
        String consignor_mobileno = consignor_mobilenoET.getText().toString();
        String consignor_address1 = consignor_address1ET.getText().toString();
        String consignor_address2 = consignor_address2ET.getText().toString();
        String consignor_pincode = consignor_pincodeET.getText().toString();
         String consignor_alternamemobileno = consignor_alternamemobilenoET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        final String consignor_sysmrno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(consignor_mobileno) && Utility.isNotNull(consignor_name) ) {
            // When Email entered is Valid

                // Put Http parameter username with value of Email Edit View control
                paramsMap.put("customer_custcd", "0");
                paramsMap.put("walkin_custcd", "0"+walkin_custcd);
                paramsMap.put("syscustactno", "0"+syscustactno);
                paramsMap.put("consignor_mobileno", ""+consignor_mobileno);
                paramsMap.put("consignor_name",""+ consignor_name);
                paramsMap.put("consignor_address1", ""+consignor_address1);
                paramsMap.put("consignor_address2", ""+consignor_address2);
                paramsMap.put("consignor_pincode", ""+consignor_pincode);
                paramsMap.put("sysmrno", "0"+consignor_sysmrno);
                paramsMap.put("companycd", "0"+consignor_companycd);
                paramsMap.put("alternamemobileno",""+ consignor_alternamemobileno);
                paramsMap.put("userid", ""+userid);
                paramsMap.put("custtype", ""+custtype);

                invokeWS(paramsMap);
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
        ApiHelper.post(URL + "Service1.asmx/EventManager_Add_EventVIsitor", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("walkincustomerdetails");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            walkin_custcd = jsonObject.getString("walkin_custcd");
                            syscustactno = jsonObject.getString("syscustactno");
                            custtype = jsonObject.getString("custtype");
                            consignor_nameET.setText(jsonObject.getString("custname"));
                            consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));
                            consignor_address1ET.setText(jsonObject.getString("address1"));
                            consignor_address2ET.setText(jsonObject.getString("address2"));
                            consignor_pincodeET.setText(jsonObject.getString("pincode"));

                            consignor_alternamemobilenoET.setText(jsonObject.getString("telephone"));
                            custtypenameET.setText(jsonObject.getString("custtypedesc"));

                            Toast.makeText(getApplicationContext(), "Walkin Added Successfully", Toast.LENGTH_LONG).show();

                            setDefaultValues();
                            Invoke_VisiterCount();

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
        consignor_mobilenoET.setText("");
        consignor_nameET.setText("");
        consignor_address1ET.setText("");
        consignor_address2ET.setText("");
        consignor_pincodeET.setText("");
        consignor_alternamemobilenoET.setText("");
        custtypenameET.setText("");
        visitercountET.setText("");
        walkin_custcd="0";
        syscustactno="0";
        custtype="";
        errorMsg.setText("");
    }

}