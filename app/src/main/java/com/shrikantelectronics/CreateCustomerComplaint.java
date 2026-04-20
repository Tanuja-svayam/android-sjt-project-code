package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateCustomerComplaint extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText consignor_mobilenoET;
    EditText consignor_nameET;
    EditText consignor_address1ET;
    EditText consignor_address2ET;
    EditText consignor_pincodeET;
    EditText consignor_emailET;
    EditText consignor_inquiredproductET;
    EditText consignor_priceofferedET;
    EditText referencecodeET;

    TextView invoicenoET;
    TextView invoicedtET;
    TextView brandname1ET;
    TextView modelnameET;
    TextView parentcategorynameET;
    TextView categorynameET;
    TextView serialnoET;

    TextView netamtET;
    TextView manufacturer_warranty_tenureET;
    TextView main_manufacturer_warranty_tenureET;
    TextView warranty_start_dateET;
    TextView warranty_end_dateET;
    EditText brandnameET;
    EditText topcategoryET;

    Button add_Customer_model;

    TableLayout tablefollowup_history;
    TextView header_followuphistory;

    String syscustactno = "0";
    String sysorderdtlno = "0";
    String custcd = "0";
    EditText inquiredproductET;
    EditText chargesET;
    String sysbrandno= "0", sysmodelno= "0", sysproductcategoryno_top= "0", sysproductcategoryno_parent= "0", sysproductcategoryno = "0";
    Button Button_topcategory, Button_parentcategory, Button_productcategory, Button_brandname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcustomercomplaint);

        consignor_nameET = (EditText) findViewById(R.id.name);
        syscustactno = "0";

        consignor_address1ET = (EditText) findViewById(R.id.address1);
        consignor_address2ET = (EditText) findViewById(R.id.address2);
        consignor_pincodeET = (EditText) findViewById(R.id.pincode);
        consignor_emailET = (EditText) findViewById(R.id.email);
        consignor_inquiredproductET = (EditText) findViewById(R.id.inquiredproduct);
        consignor_priceofferedET = (EditText) findViewById(R.id.priceoffered);
        referencecodeET = (EditText) findViewById(R.id.referencecode);

        brandnameET = (EditText) findViewById(R.id.brandname);
        topcategoryET = (EditText) findViewById(R.id.topcategory);

        //consignor_address1ET = (EditText)findViewById(R.id.address1);

        invoicenoET = (TextView) findViewById(R.id.invoiceno);
        invoicedtET = (TextView) findViewById(R.id.invoicedt);
        brandname1ET = (TextView) findViewById(R.id.brandname1);
        modelnameET = (TextView) findViewById(R.id.modelname);
        parentcategorynameET = (TextView) findViewById(R.id.parentcategoryname);
        categorynameET = (TextView) findViewById(R.id.categoryname);
        serialnoET = (TextView) findViewById(R.id.serialno);
        netamtET = (TextView) findViewById(R.id.netamt);
        manufacturer_warranty_tenureET = (TextView) findViewById(R.id.manufacturer_warranty_tenure);
        warranty_start_dateET = (TextView) findViewById(R.id.warranty_start_date);
        warranty_end_dateET = (TextView) findViewById(R.id.warranty_end_date);

        consignor_mobilenoET = (EditText) findViewById(R.id.mobileno);
        consignor_mobilenoET.addTextChangedListener(new GenericTextWatcher_Mobile(consignor_mobilenoET));

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        OnClickListener handler = new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.Button_topcategory:
                        navigatetoTopCategoryListActivity();
                        break;

                    case R.id.Button_brandname:
                        navigatetoBrandListActivity();
                        break;
                }
            }
        };

        Button_topcategory = (Button) findViewById(R.id.Button_topcategory);
        Button_topcategory.setOnClickListener(handler);

        Button_brandname = (Button) findViewById(R.id.Button_brandname);
        Button_brandname.setOnClickListener(handler);

        add_Customer_model = (Button) findViewById(R.id.add_Customer_model);
        add_Customer_model.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                navigateto_Add_Customer_Product_Activity();
            }
        });

        Intent i = getIntent();
        custcd = i.getStringExtra("custcd");
        syscustactno = i.getStringExtra("syscustactno");

        if (syscustactno != null && !syscustactno.equals("0")) {
            Invoke_CustomerComplaintDetails();
        } else {
            custcd = "0";
            syscustactno = "0";
        }
    }

    public void Invoke_CustomerDetails() {
        try {

            Editable editableValue1 = consignor_mobilenoET.getText();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("mobileno1", "" + editableValue1);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerDetailsByMobileNumber", paramsMap, new ApiHelper.ApiCallback() {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);
                        custcd = jsonObject.getString("custcd");
                        consignor_nameET.setText(jsonObject.getString("name"));
                        //consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));
                        consignor_address1ET.setText(jsonObject.getString("address1"));
                        consignor_address2ET.setText(jsonObject.getString("address2"));
                        consignor_pincodeET.setText(jsonObject.getString("pincode"));
                        consignor_emailET.setText(jsonObject.getString("emailid"));

                        invoicenoET.setText(jsonObject.getString("invoiceno"));
                        invoicedtET.setText(jsonObject.getString("invoicedt"));
                        brandnameET.setText(jsonObject.getString("brandname"));
                        modelnameET.setText(jsonObject.getString("modelname"));
                        parentcategorynameET.setText(jsonObject.getString("parentcategoryname"));
                        categorynameET.setText(jsonObject.getString("categoryname"));
                        serialnoET.setText(jsonObject.getString("serialno"));
                        netamtET.setText(jsonObject.getString("netamt"));
                        manufacturer_warranty_tenureET.setText(jsonObject.getString("manufacturer_warranty_tenure"));
                        //warranty_start_dateET.setText(jsonObject.getString("warranty_start_date"));
                        //warranty_end_dateET.setText(jsonObject.getString("warranty_end_date"));

                        // invokeWS_Customer_Products(custcd);
                        //7046404438

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigateto_Add_Customer_Product_Activity() {

        if (custcd != null && !custcd.equals("0")) {
            Intent homeIntent = new Intent(CreateCustomerComplaint.this, CustomerComplaint_Product_Activity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.putExtra("custcd", custcd);
            startActivityForResult(homeIntent, 1);
        } else {
            Toast.makeText(getApplicationContext(), "Order Not Saved", Toast.LENGTH_LONG).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysorderdtlno = bundle.getString("sysorderdtlno");

                invoicenoET.setText(bundle.getString("invoiceno"));
                invoicedtET.setText(bundle.getString("invoicedt"));
                brandname1ET.setText(bundle.getString("brandname"));
                parentcategorynameET.setText(bundle.getString("parentcategoryname"));
                categorynameET.setText(bundle.getString("categoryname"));
                serialnoET.setText(bundle.getString("serialno"));
                netamtET.setText(bundle.getString("netamt"));
                manufacturer_warranty_tenureET.setText(bundle.getString("manufacturer_warranty_tenure"));
                warranty_start_dateET.setText(bundle.getString("warranty_start_date"));
                warranty_end_dateET.setText(bundle.getString("warranty_end_date"));

                sysproductcategoryno_top = bundle.getString("sysproductcategoryno_top");
                topcategoryET.setText(bundle.getString("topcategoryname"));

                sysbrandno = bundle.getString("sysbrandno");
                brandnameET.setText(bundle.getString("brandname"));

                sysmodelno = bundle.getString("sysmodelno");
                modelnameET.setText(bundle.getString("modelname"));

            }
        }


        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysproductcategoryno_top = bundle.getString("sysproductcategoryno");
                topcategoryET.setText(bundle.getString("description"));
                sysmodelno = "0";
            }
        }

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysbrandno = bundle.getString("sysbrandno");
                brandnameET.setText(bundle.getString("description"));
                sysmodelno = "0";
            }
        }

    }

    public void navigatetoBrandListActivity() {
        Intent homeIntent = new Intent(CreateCustomerComplaint.this, StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent, 5);
    }

    public void navigatetoTopCategoryListActivity() {
        Intent homeIntent = new Intent(CreateCustomerComplaint.this, StockVerificationTopCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent, 2);
    }

    public void onClick_submit(View view) {
        // Get Email Edit View Value

        String consignor_name = consignor_nameET.getText().toString();
        String consignor_mobileno = consignor_mobilenoET.getText().toString();
        String consignor_address1 = consignor_address1ET.getText().toString();
        String consignor_address2 = consignor_address2ET.getText().toString();
        String consignor_pincode = consignor_pincodeET.getText().toString();
        String consignor_email = consignor_emailET.getText().toString();
        String consignor_inquiredproduct = consignor_inquiredproductET.getText().toString();
        String consignor_priceoffered = consignor_priceofferedET.getText().toString();
        String referencecode = referencecodeET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd = globalVariable.getcompanycd();
        final String consignor_sysmrno = globalVariable.getsysemployeeno();

        //final String sysemployeeno  = globalVariable.getsysemployeeno();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(consignor_address1) && Utility.isNotNull(consignor_mobileno) && Utility.isNotNull(consignor_name) && Utility.isNotNull(consignor_pincode)) {
            // When Email entered is Valid
            // Put Http parameter username with value of Email Edit View control

            paramsMap.put("syscustactno", "0" + syscustactno);
            paramsMap.put("custcd", "0" + custcd);
            paramsMap.put("consignor_mobileno", "" + consignor_mobileno);
            paramsMap.put("consignor_name", "" + consignor_name);
            paramsMap.put("consignor_address1", "" + consignor_address1);
            paramsMap.put("consignor_address2", "" + consignor_address2);
            paramsMap.put("consignor_pincode", "" + consignor_pincode);
            paramsMap.put("consignor_email", "" + consignor_email);
            paramsMap.put("sysmrno", "0" + consignor_sysmrno);
            paramsMap.put("companycd", "0" + consignor_companycd);
            paramsMap.put("inquiredproduct", "" + consignor_inquiredproduct);
            paramsMap.put("priceoffered", "0" + consignor_priceoffered);
            paramsMap.put("sysorderdtlno", "0" + sysorderdtlno);
            paramsMap.put("referencecode", "" + referencecode);
            paramsMap.put("sysbrandno", "0" + sysbrandno);
            paramsMap.put("sysproductcategoryno_top", "0" + sysproductcategoryno_top);
            paramsMap.put("sysmodelno", "0" + sysmodelno);

            invokeWS(paramsMap);

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS(Map<String, String> paramsMap) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddWalkinCustomerComplaints", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("customersingle");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                    syscustactno = jsonObject.getString("syscustactno");
                    custcd = jsonObject.getString("custcd");
                    sysorderdtlno = jsonObject.getString("sysorderdtlno");
                    sysbrandno = jsonObject.getString("sysbrandno");
                    sysproductcategoryno_top = jsonObject.getString("sysproductcategoryno_top");
                    sysmodelno = jsonObject.getString("sysmodelno");

                    consignor_nameET.setText(jsonObject.getString("custname"));
                    consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));
                    consignor_address1ET.setText(jsonObject.getString("address1"));
                    consignor_address2ET.setText(jsonObject.getString("address2"));
                    consignor_pincodeET.setText(jsonObject.getString("pincode"));
                    consignor_emailET.setText(jsonObject.getString("emailid"));
                    consignor_priceofferedET.setText(jsonObject.getString("productvalue"));
                    consignor_inquiredproductET.setText(jsonObject.getString("productenquiry"));
                    referencecodeET.setText(jsonObject.getString("referencecode"));

                    invoicenoET.setText(jsonObject.getString("invoiceno"));
                    invoicedtET.setText(jsonObject.getString("invoicedt"));
                    brandnameET.setText(jsonObject.getString("brandname"));
                    modelnameET.setText(jsonObject.getString("modelname"));
                    topcategoryET.setText(jsonObject.getString("topcategoryname"));
                    parentcategorynameET.setText(jsonObject.getString("parentcategoryname"));
                    categorynameET.setText(jsonObject.getString("categoryname"));
                    serialnoET.setText(jsonObject.getString("serialno"));
                    netamtET.setText(jsonObject.getString("netamt"));
                  //  manufacturer_warranty_tenureET.setText(jsonObject.getString("manufacturer_warranty_tenure"));
                    // warranty_start_dateET.setText(jsonObject.getString("warranty_start_date"));
                    // warranty_end_dateET.setText(jsonObject.getString("warranty_end_date"));

                    Toast.makeText(getApplicationContext(), jsonObject.getString("status"), Toast.LENGTH_LONG).show();

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
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void Invoke_CustomerComplaintDetails() {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("syscustactno", "0" + syscustactno);
        paramsMap.put("custcd", "0" + custcd);

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GetWalkinCustomerComplaints", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("customersingle");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                    syscustactno = jsonObject.getString("syscustactno");
                    custcd = jsonObject.getString("custcd");
                    sysorderdtlno = jsonObject.getString("sysorderdtlno");

                    consignor_nameET.setText(jsonObject.getString("custname"));
                    consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));
                    consignor_address1ET.setText(jsonObject.getString("address1"));
                    consignor_address2ET.setText(jsonObject.getString("address2"));
                    consignor_pincodeET.setText(jsonObject.getString("pincode"));
                    consignor_emailET.setText(jsonObject.getString("emailid"));
                    consignor_priceofferedET.setText(jsonObject.getString("productvalue"));
                    consignor_inquiredproductET.setText(jsonObject.getString("productenquiry"));
                    referencecodeET.setText(jsonObject.getString("referencecode"));

                    invoicenoET.setText(jsonObject.getString("invoiceno"));
                    invoicedtET.setText(jsonObject.getString("invoicedt"));
                    brandnameET.setText(jsonObject.getString("brandname"));
                    modelnameET.setText(jsonObject.getString("modelname"));
                    parentcategorynameET.setText(jsonObject.getString("parentcategoryname"));
                    categorynameET.setText(jsonObject.getString("categoryname"));
                    serialnoET.setText(jsonObject.getString("serialno"));
                    netamtET.setText(jsonObject.getString("netamt"));
                    manufacturer_warranty_tenureET.setText(jsonObject.getString("manufacturer_warranty_tenure"));
                    // warranty_start_dateET.setText(jsonObject.getString("warranty_start_date"));
                    // warranty_end_dateET.setText(jsonObject.getString("warranty_end_date"));

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
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setDefaultValues() {
        consignor_mobilenoET.setText("");
        consignor_nameET.setText("");
        consignor_address1ET.setText("");
        consignor_address2ET.setText("");
        consignor_pincodeET.setText("");
        consignor_emailET.setText("");
        consignor_inquiredproductET.setText("");
        consignor_priceofferedET.setText("");
        referencecodeET.setText("");
    }

    private class GenericTextWatcher_Mobile implements TextWatcher {

        private View view;

        private GenericTextWatcher_Mobile(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = consignor_mobilenoET.getText();
            if (editableValue1.length() == 10) {
                Invoke_CustomerDetails();
            }
        }
    }


}