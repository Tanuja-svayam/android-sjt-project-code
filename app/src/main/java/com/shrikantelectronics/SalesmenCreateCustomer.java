package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SalesmenCreateCustomer extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText consignor_mobilenoET ;
    EditText consignor_alternamemobilenoET ;
    TableLayout tablecustomeradvancereceipt;

    EditText consignor_nameET ;

    EditText consignor_address1ET ;
    EditText consignor_address2ET ;
    EditText consignor_address3ET ;
    EditText consignor_address4ET ;
    EditText consignor_pincodeET ;
    EditText consignor_gstnET;
    EditText consignor_gstnstateET ;

    EditText delivery_address1ET ;
    EditText delivery_address2ET ;
    EditText delivery_address3ET ;
    EditText delivery_address4ET ;
    EditText delivery_pincodeET ;
    EditText delivery_gstnET;
    EditText delivery_gstnstateET ;

    EditText consignor_emailET ;
    EditText consignor_inquiredproductET;
    EditText consignor_priceofferedET;
    EditText remarksET;

    EditText custtypenameET ;
    EditText custpriorityET ;
    EditText brandnameET;
    EditText topcategoryET;
    String syslocno="0";
    String sysaccledgerno="0";
    String walkin_custcd="0",customer_custcd="0",syscustactno="0", consignor_sysstateno="0", delivery_sysstateno = "0", custtype="", cust_priority_type="", sysbrandno = "0", sysproductcategoryno_top="0";

    Button pickbirthdate;
    Button pickAniversoryDate;

    TextView pickNextFollowupDate;
    EditText txtNextFollowupTime;

    int year, month, day;
    String sbirthdate;
    String sAniversoryDate;

    Button Button_state, Button_custtype, Button_custpriority, Button_state_delivery, Button_brandname, Button_topcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesmencreatecustomer);

        consignor_nameET = (EditText)findViewById(R.id.name);
        consignor_mobilenoET = (EditText)findViewById(R.id.mobileno);

        //consignor_mobilenoET.addTextChangedListener(new GenericTextWatcher_Mobile(consignor_mobilenoET));

        consignor_alternamemobilenoET = (EditText)findViewById(R.id.alternamemobileno);
        custtypenameET = (EditText)findViewById(R.id.custtypename);
        custpriorityET = (EditText)findViewById(R.id.custpriority);
        brandnameET = (EditText)findViewById(R.id.brandname);
        topcategoryET = (EditText)findViewById(R.id.topcategory);

        consignor_address1ET = (EditText)findViewById(R.id.address1);
        consignor_address2ET = (EditText)findViewById(R.id.address2);
        consignor_address3ET = (EditText)findViewById(R.id.address3);
        consignor_address4ET = (EditText)findViewById(R.id.address4);
        consignor_pincodeET = (EditText)findViewById(R.id.pincode);
        consignor_gstnET = (EditText)findViewById(R.id.consignor_gstn);
        consignor_gstnstateET = (EditText)findViewById(R.id.consignor_statename);

        delivery_address1ET = (EditText)findViewById(R.id.delivery_address1);
        delivery_address2ET = (EditText)findViewById(R.id.delivery_address2);
        delivery_address3ET = (EditText)findViewById(R.id.delivery_address3);
        delivery_address4ET = (EditText)findViewById(R.id.delivery_address4);
        delivery_pincodeET = (EditText)findViewById(R.id.delivery_pincode);
        delivery_gstnET = (EditText)findViewById(R.id.delivery_gstn);
        delivery_gstnstateET = (EditText)findViewById(R.id.delivery_statename);

        consignor_emailET = (EditText)findViewById(R.id.email);
        consignor_inquiredproductET = (EditText)findViewById(R.id.inquiredproduct);
        consignor_priceofferedET = (EditText)findViewById(R.id.priceoffered);
        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        txtNextFollowupTime = (EditText)findViewById(R.id.NextFollowupTime);
        remarksET = (EditText)findViewById(R.id.remarks);

        Intent i = getIntent();
        if( i.getStringExtra("walkin_custcd") != null)
        {
            walkin_custcd = i.getStringExtra("walkin_custcd");
        }
        else
        {
            walkin_custcd = "0";
        }

        if( i.getStringExtra("customer_custcd") != null)
        {
            customer_custcd = i.getStringExtra("customer_custcd");
        }
        else
        {
            customer_custcd = "0";
        }

        if( i.getStringExtra("syslocno") != null)
        {
            syslocno = i.getStringExtra("syslocno");
        }
        else
        {
            syslocno = "0";
        }

        if( i.getStringExtra("sysaccledgerno") != null)
        {
            sysaccledgerno = i.getStringExtra("sysaccledgerno");
        }
        else
        {
            sysaccledgerno = "0";
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

        consignor_sysstateno = "0";
        consignor_gstnstateET.setText(default_state);

        delivery_sysstateno = "0";
        delivery_gstnstateET.setText(default_state);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickbirthdate = (Button) findViewById(R.id.pickbirthdate);
        pickAniversoryDate = (Button) findViewById(R.id.pickAniversoryDate);

//        pickbirthdate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        pickbirthdate.setText("");
        pickAniversoryDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.Button_state:
                        navigatetoStateListActivity(1);
                        break;

                    case R.id.Button_state_delivery:
                        navigatetoStateListActivity(4);
                        break;

                    case R.id.Button_custtype:
                        navigatetoCustTypeListActivity();
                        break;

                    case R.id.Button_custpriority:
                        navigatetoCustPriorityListActivity();
                        break;

                    case R.id.Button_brandname:
                        navigatetoBrandListActivity();
                        break;

                    case R.id.Button_topcategory:
                        navigatetoTopCategoryListActivity();
                        break;
                }
            }
        };

        Button_state = (Button) findViewById(R.id.Button_state);
        Button_state.setOnClickListener(handler);

        Button_state_delivery = (Button) findViewById(R.id.Button_state_delivery);
        Button_state_delivery.setOnClickListener(handler);

        Button_custtype = (Button) findViewById(R.id.Button_custtype);
        Button_custtype.setOnClickListener(handler);

        Button_custpriority = (Button) findViewById(R.id.Button_custpriority);
        Button_custpriority.setOnClickListener(handler);

        Button_brandname = (Button) findViewById(R.id.Button_brandname);
        Button_brandname.setOnClickListener(handler);

        Button_topcategory = (Button) findViewById(R.id.Button_topcategory);
        Button_topcategory.setOnClickListener(handler);

        if(!walkin_custcd.equals("0")) {
            Invoke_CustomerDetailsSingle();
        }

        if(!customer_custcd.equals("0") && walkin_custcd.equals("0")) {
            Invoke_CustomerDetailsSingleByID();
        }

    }

    public void navigatetoStateListActivity(Integer requestcode){
        Intent homeIntent = new Intent(SalesmenCreateCustomer.this,StockVerificationState_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestcode);
    }

    public void navigatetoCustTypeListActivity(){
        Intent homeIntent = new Intent(SalesmenCreateCustomer.this,StockVerificationCustomerType_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,2);
    }

    public void navigatetoCustPriorityListActivity(){
        Intent homeIntent = new Intent(SalesmenCreateCustomer.this,StockVerificationCustomerPriority_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,3);
    }

    public void navigatetoBrandListActivity(){
        Intent homeIntent = new Intent(SalesmenCreateCustomer.this,StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }

    public void navigatetoTopCategoryListActivity(){
        Intent homeIntent = new Intent(SalesmenCreateCustomer.this,StockVerificationTopCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,6);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                consignor_sysstateno = bundle.getString("sysstateno");
                consignor_gstnstateET.setText(bundle.getString("statename"));
            }
        }

        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                delivery_sysstateno = bundle.getString("sysstateno");
                delivery_gstnstateET.setText(bundle.getString("statename"));
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                custtype = bundle.getString("gccode");
                custtypenameET.setText(bundle.getString("gcname"));
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                cust_priority_type = bundle.getString("gccode");
                custpriorityET.setText(bundle.getString("gcname"));
            }
        }

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysbrandno = bundle.getString("sysbrandno");
                brandnameET.setText(bundle.getString("description"));
            }
        }

        if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysproductcategoryno_top = bundle.getString("sysproductcategoryno");
                topcategoryET.setText(bundle.getString("description"));
            }
        }
    }

    public void Invoke_CustomerDetailsSingle() {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("walkin_custcd", "0" +  walkin_custcd);
            paramsMap.put("customer_custcd", "0" +  customer_custcd);
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
                        consignor_emailET.setText(jsonObject.getString("emailid"));
                        consignor_alternamemobilenoET.setText(jsonObject.getString("telephone"));

                        consignor_address1ET.setText(jsonObject.getString("address1"));
                        consignor_address2ET.setText(jsonObject.getString("address2"));
                        consignor_address3ET.setText(jsonObject.getString("address3"));
                        consignor_address4ET.setText(jsonObject.getString("address4"));
                        consignor_pincodeET.setText(jsonObject.getString("pincode"));
                        consignor_gstnET.setText(jsonObject.getString("consignor_gstn"));
                        consignor_gstnstateET.setText(jsonObject.getString("consignor_gstnstate"));

                        delivery_address1ET.setText(jsonObject.getString("delivery_address1"));
                        delivery_address2ET.setText(jsonObject.getString("delivery_address2"));
                        delivery_address3ET.setText(jsonObject.getString("delivery_address3"));
                        delivery_address4ET.setText(jsonObject.getString("delivery_address4"));
                        delivery_pincodeET.setText(jsonObject.getString("delivery_pincode"));
                        delivery_gstnET.setText(jsonObject.getString("delivery_gstn"));
                        delivery_gstnstateET.setText(jsonObject.getString("delivery_gstnstate"));

                        consignor_priceofferedET.setText(jsonObject.getString("productvalue"));
                        consignor_inquiredproductET.setText(jsonObject.getString("productenquiry"));
                        remarksET.setText(jsonObject.getString("lastfolupconversation"));

                       // walkin_custcd = jsonObject.getString("walkin_custcd");
                       //customer_custcd = jsonObject.getString("customer_custcd");
                      //  syscustactno = jsonObject.getString("syscustactno");

                        custtype = jsonObject.getString("custtype");
                        cust_priority_type = jsonObject.getString("cust_priority_type");
                        custtypenameET.setText(jsonObject.getString("custtypedesc"));
                        custpriorityET.setText(jsonObject.getString("cust_priority_typedesc"));
                        sysbrandno = jsonObject.getString("sysbrandno");
                        brandnameET.setText(jsonObject.getString("brandname"));

                        sysproductcategoryno_top = jsonObject.getString("sysproductcategoryno_top");
                        topcategoryET.setText(jsonObject.getString("topcategoryname"));

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

    public void Invoke_CustomerDetailsSingleByID() {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("custcd", "" +  customer_custcd );

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerDetailsSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        customer_custcd = jsonObject.getString("custcd");
                        consignor_nameET.setText(jsonObject.getString("name"));
                        consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));
                        consignor_emailET.setText(jsonObject.getString("emailid"));
                        consignor_alternamemobilenoET.setText(jsonObject.getString("telephone"));

                        consignor_address1ET.setText(jsonObject.getString("address1"));
                        consignor_address2ET.setText(jsonObject.getString("address2"));
                        consignor_address3ET.setText(jsonObject.getString("address3"));
                        consignor_address4ET.setText(jsonObject.getString("address4"));
                        consignor_pincodeET.setText(jsonObject.getString("pincode"));
                        consignor_gstnET.setText(jsonObject.getString("consignor_gstn"));
                        consignor_gstnstateET.setText(jsonObject.getString("consignor_gstnstate"));

                        delivery_address1ET.setText(jsonObject.getString("delivery_address1"));
                        delivery_address2ET.setText(jsonObject.getString("delivery_address2"));
                        delivery_address3ET.setText(jsonObject.getString("delivery_address3"));
                        delivery_address4ET.setText(jsonObject.getString("delivery_address4"));
                        delivery_pincodeET.setText(jsonObject.getString("delivery_pincode"));
                        delivery_gstnET.setText(jsonObject.getString("delivery_gstn"));
                        delivery_gstnstateET.setText(jsonObject.getString("delivery_gstnstate"));


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
        String consignor_address3 = consignor_address3ET.getText().toString();
        String consignor_address4 = consignor_address4ET.getText().toString();
        String consignor_pincode = consignor_pincodeET.getText().toString();
        String consignor_gstn = consignor_gstnET.getText().toString();
        String consignor_gstnstate = consignor_gstnstateET.getText().toString();

        String delivery_address1 = delivery_address1ET.getText().toString();
        String delivery_address2 = delivery_address2ET.getText().toString();
        String delivery_address3 = delivery_address3ET.getText().toString();
        String delivery_address4 = delivery_address4ET.getText().toString();
        String delivery_pincode = delivery_pincodeET.getText().toString();
        String delivery_gstn = delivery_gstnET.getText().toString();
        String delivery_gstnstate = delivery_gstnstateET.getText().toString();

        String consignor_email = consignor_emailET.getText().toString();
        String consignor_inquiredproduct = consignor_inquiredproductET.getText().toString();
        String consignor_priceoffered= consignor_priceofferedET.getText().toString();
        String remarks= remarksET.getText().toString();
        String followupdate = pickNextFollowupDate.getText().toString();
        String followuptime = txtNextFollowupTime.getText().toString();
        String consignor_alternamemobileno = consignor_alternamemobilenoET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String consignor_sysmrno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();
        //
        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();
        //



        //make birthdate cpmpulsory
//        if (pickbirthdate.getText().toString().trim().isEmpty()) {
//            Toast.makeText(this, "Please select Birth Date", Toast.LENGTH_SHORT).show();
//            return;
//        }


        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(consignor_mobileno) && Utility.isNotNull(consignor_name) && Utility.isNotNull(consignor_pincode)  && Utility.isNotNull(sysbrandno) && Utility.isNotNull(sysproductcategoryno_top)) {
            // When Email entered is Valid

                // Put Http parameter username with value of Email Edit View control
                paramsMap.put("customer_custcd", "0"+customer_custcd);
                paramsMap.put("walkin_custcd", "0"+walkin_custcd);
                paramsMap.put("syscustactno", "0"+syscustactno);
                paramsMap.put("sysmrno", "0"+consignor_sysmrno);
                paramsMap.put("companycd", "0"+consignor_companycd);
                paramsMap.put("custtype", ""+custtype);
                paramsMap.put("cust_priority_type", ""+cust_priority_type);
                paramsMap.put("sysbrandno", "0"+sysbrandno);
                paramsMap.put("sysproductcategoryno_top", "0"+sysproductcategoryno_top);

                paramsMap.put("consignor_name",""+ consignor_name);
                paramsMap.put("consignor_mobileno", ""+consignor_mobileno);
                paramsMap.put("alternamemobileno",""+ consignor_alternamemobileno);
                paramsMap.put("consignor_email", ""+consignor_email);

                paramsMap.put("consignor_address1", ""+consignor_address1);
                paramsMap.put("consignor_address2", ""+consignor_address2);
                paramsMap.put("consignor_address3", ""+consignor_address3);
                paramsMap.put("consignor_address4", ""+consignor_address4);
                paramsMap.put("consignor_pincode", ""+consignor_pincode);
                paramsMap.put("consignor_gstn", ""+consignor_gstn);
                paramsMap.put("consignor_gstnstate", ""+consignor_gstnstate);

                paramsMap.put("delivery_address1", ""+delivery_address1);
                paramsMap.put("delivery_address2", ""+delivery_address2);
                paramsMap.put("delivery_address3", ""+delivery_address3);
                paramsMap.put("delivery_address4", ""+delivery_address4);
                paramsMap.put("delivery_pincode", ""+delivery_pincode);
                paramsMap.put("delivery_gstn", ""+delivery_gstn);
                paramsMap.put("delivery_gstnstate", ""+delivery_gstnstate);

                paramsMap.put("inquiredproduct", ""+consignor_inquiredproduct);
                paramsMap.put("priceoffered", "0"+consignor_priceoffered);
                paramsMap.put("birthdate", ""+pickbirthdate.getText());
                paramsMap.put("aniversorydate",""+ pickAniversoryDate.getText());
                paramsMap.put("nextfollowupdate", "" + followupdate);
                paramsMap.put("nextfollowuptime", "0" + followuptime);
                paramsMap.put("remarks",""+ remarks);
                paramsMap.put("userid", ""+userid);
                paramsMap.put("giftcouponno", "");
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
        ApiHelper.post(URL + "Service1.asmx/SalemenAddWalkinCustomer", paramsMap, new ApiHelper.ApiCallback() {

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
                            customer_custcd = jsonObject.getString("customer_custcd");
                            syscustactno = jsonObject.getString("syscustactno");

                            custtype = jsonObject.getString("custtype");
                            cust_priority_type = jsonObject.getString("cust_priority_type");
                            sysbrandno = jsonObject.getString("walkin_sysbrandno");
                            sysproductcategoryno_top= jsonObject.getString("walkin_sysproductcategoryno_top");

                            consignor_nameET.setText(jsonObject.getString("custname"));
                            consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));

                            consignor_address1ET.setText(jsonObject.getString("address1"));
                            consignor_address2ET.setText(jsonObject.getString("address2"));
                            consignor_address3ET.setText(jsonObject.getString("address3"));
                            consignor_address4ET.setText(jsonObject.getString("address4"));
                            consignor_pincodeET.setText(jsonObject.getString("pincode"));
                            consignor_gstnET.setText(jsonObject.getString("gstn"));
                            consignor_gstnstateET.setText(jsonObject.getString("gstnstate"));

                            delivery_address1ET.setText(jsonObject.getString("delivery_address1"));
                            delivery_address2ET.setText(jsonObject.getString("delivery_address2"));
                            delivery_address3ET.setText(jsonObject.getString("delivery_address3"));
                            delivery_address4ET.setText(jsonObject.getString("delivery_address4"));
                            delivery_pincodeET.setText(jsonObject.getString("delivery_pincode"));
                            delivery_gstnET.setText(jsonObject.getString("delivery_gstn"));
                            delivery_gstnstateET.setText(jsonObject.getString("delivery_gstnstate"));

                            consignor_emailET.setText(jsonObject.getString("emailid"));
                            pickbirthdate.setText(jsonObject.getString("birthdayname"));
                            pickAniversoryDate.setText(jsonObject.getString("aniversarydatename"));
                            consignor_alternamemobilenoET.setText(jsonObject.getString("telephone"));
                            custtypenameET.setText(jsonObject.getString("custtypedesc"));
                            custpriorityET.setText(jsonObject.getString("cust_priority_typedesc"));
                            brandnameET.setText(jsonObject.getString("walkin_brandname"));
                            topcategoryET.setText(jsonObject.getString("walkin_topcategoryname"));


                            Toast.makeText(getApplicationContext(), "Walkin Added Successfully", Toast.LENGTH_LONG).show();

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

        consignor_emailET.setText("");
        consignor_inquiredproductET.setText("");
        consignor_priceofferedET.setText("0");
        consignor_alternamemobilenoET.setText("");

        consignor_address1ET.setText("");
        consignor_address2ET.setText("");
        consignor_address3ET.setText("");
        consignor_address4ET.setText("");
        consignor_pincodeET.setText("");
        consignor_gstnET.setText("");
        consignor_gstnstateET.setText("");

        delivery_address1ET.setText("");
        delivery_address2ET.setText("");
        delivery_address3ET.setText("");
        delivery_address4ET.setText("");
        delivery_pincodeET.setText("");
        delivery_gstnET.setText("");
        delivery_gstnstateET.setText("");

        custtypenameET.setText("");
        custpriorityET.setText("");
        brandnameET.setText("");
       topcategoryET.setText("");

    }

    public void navigatetoFollowupActivity(){
        Intent homeIntent = new Intent(SalesmenCreateCustomer.this,CrmActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("status","PENDING");
        finish();
        startActivity(homeIntent);
    }

    @SuppressWarnings("deprecation")
    public void setBirthDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @SuppressWarnings("deprecation")
    public void setAniversoryDate(View view) {
        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @SuppressWarnings("deprecation")
    public void setPickNextFollowupDate(View view) {
        showDialog(997);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    birthdateListener, year, month, day);
        }

        if (id == 998) {
            return new DatePickerDialog(this,
                    AniversoryDateListener, year, month, day);
        }

        if (id == 997) {
            return new DatePickerDialog(this,
                    NextfollowupDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener birthdateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showbirthdate(arg1, arg2, arg3);
                }
            };

    private void showbirthdate(int year, int month, int day) {
        pickbirthdate = (Button) findViewById(R.id.pickbirthdate);
        pickbirthdate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        //  invokeWS_Customer_Ledger(custcd);
    }

    private DatePickerDialog.OnDateSetListener AniversoryDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showAniversoryDate(arg1, arg2, arg3);
                }
            };

    private void showAniversoryDate(int year, int month, int day) {
        pickAniversoryDate = (Button) findViewById(R.id.pickAniversoryDate);
        pickAniversoryDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
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
        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }

    public void onClick_add_product(View view) {

        if (syscustactno== null || syscustactno.equals("0") ) {

            Submit_CreateWalkin();
        }

        if (Utility.isNotNull(syscustactno) && !syscustactno.equals("0") ) {

            String consignor_name = consignor_nameET.getText().toString();


            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String consignor_companycd  = globalVariable.getcompanycd();
            final String consignor_companyname  = globalVariable.getcompanyname();

            Intent homeIntent = new Intent(SalesmenCreateCustomer.this,Salesmen_SalesOrder.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            homeIntent.putExtra("sysinvorderno","0");
            homeIntent.putExtra("walkin_custcd",walkin_custcd);
            homeIntent.putExtra("customer_custcd",customer_custcd);
            homeIntent.putExtra("syscustactno","0" + syscustactno);

            homeIntent.putExtra("custname",consignor_name);
            homeIntent.putExtra("companycd",consignor_companycd);
            homeIntent.putExtra("companyname",consignor_companyname);

            finish();
            startActivity(homeIntent);
        }
    }



    public void onClick_create_quotation(View view) {

        if (syscustactno== null || syscustactno.equals("0") ) {

            Submit_CreateWalkin();
        }

        if (Utility.isNotNull(syscustactno) && !syscustactno.equals("0") ) {

            String consignor_name = consignor_nameET.getText().toString();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String consignor_companycd  = globalVariable.getcompanycd();
            final String consignor_companyname  = globalVariable.getcompanyname();

            Intent homeIntent = new Intent(SalesmenCreateCustomer.this,Salesmen_SalesQuotation.class);

            homeIntent.putExtra("sysinvorderno","0");
            homeIntent.putExtra("walkin_custcd",walkin_custcd);
            homeIntent.putExtra("customer_custcd",customer_custcd);
            homeIntent.putExtra("syscustactno","0" + syscustactno);

            homeIntent.putExtra("custname",consignor_name);
            homeIntent.putExtra("companycd",consignor_companycd);
            homeIntent.putExtra("companyname",consignor_companyname);

            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            finish();
            startActivity(homeIntent);
        }
    }


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SalesmenCreateCustomer.this);
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
        TextView textView = new TextView(SalesmenCreateCustomer.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesmenCreateCustomer.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}