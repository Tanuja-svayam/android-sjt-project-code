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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Salesmen_SalesQuotation extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;


    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;
    int year, month, day;
    Button pickPurchaseDate;
    private Button btnSaveInvoice;
    Button btnCreateSalesOrder;
    CheckBox chkwithlowside, chkwithinstallation;
    EditText remarksET;

    EditText locationET ;
    EditText custnameET;
    EditText invordernoET ;
    EditText brandnameET;
    EditText modelnameET ;
    EditText quantityET;
    EditText unitrateET;
    EditText exchangedetailsET;

    String FileName;

    Button search_invorderno;

    ListViewAdapter adapter;
    EditText inputSearch;

    String companycd = "0", walkin_custcd = "0", customer_custcd = "0", sysbrandno = "0", sysmodelno = "0", sysinvorderno = "0", sysorderdtlno = "0", sysstateno = "0" ;
    String companyname, custname, invorderno, vinvorderdt, brandname, modelname, quantity, serialnnocount, device_token_id;

    Button Button_custname, Button_modelname,  Button_brandname;
    Button Button_location,  Button_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesmen_salesquotation);


        Intent i = getIntent();
        sysinvorderno = i.getStringExtra("sysinvorderno");
        invorderno = i.getStringExtra("invorderno");

        walkin_custcd = i.getStringExtra("walkin_custcd");
        customer_custcd = i.getStringExtra("customer_custcd");
        custname = i.getStringExtra("custname");

        companycd=i.getStringExtra("companycd");
        companyname=i.getStringExtra("companyname");

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd_emp  = globalVariable.getcompanycd();
        final String companyname_emp  = globalVariable.getcompanyname();
        final String reportingto  = globalVariable.getreportingto();
        final String reporting_fullname  = globalVariable.getreporting_fullname();

        locationET = (EditText)findViewById(R.id.location);
        custnameET = (EditText)findViewById(R.id.custname);
        invordernoET = (EditText)findViewById(R.id.invorderno);
      //  barcodeET = (EditText)findViewById(R.id.barcode);
        brandnameET = (EditText)findViewById(R.id.brandname);
        modelnameET = (EditText)findViewById(R.id.modelname);
        quantityET = (EditText)findViewById(R.id.quantity);
        unitrateET = (EditText)findViewById(R.id.unitrate);
        exchangedetailsET= (EditText)findViewById(R.id.exchangedetails);
        remarksET = (EditText)findViewById(R.id.remarks);
        chkwithlowside = (CheckBox) findViewById(R.id.withlowside);
        chkwithinstallation = (CheckBox) findViewById(R.id.withinstallation);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        search_invorderno= (Button) findViewById(R.id.search_invorderno);
        search_invorderno.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sysinvorderno="0";
                invokeSearchSalesmen_QuotationWS();
            }
        });
        //
        // button on click listener
//
        OnClickListener handler = new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.Button_location:
                        navigatetoLocationListActivity();
                        break;

                    case R.id.Button_brandname:
                        navigatetoBrandListActivity();
                        break;

                    case R.id.Button_modelname:
                        navigatetoModelListActivity();
                        break;

                    case R.id.Button_custname:
                        navigatetoCustomerListActivity();
                        break;
                }
            }
        };

        btnSaveInvoice = (Button) findViewById(R.id.btnSaveInvoice);
        //btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        btnSaveInvoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateQuotation();
            }
        });

        btnCreateSalesOrder = (Button) findViewById(R.id.btnCreateSalesOrder);
        btnCreateSalesOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CreateSalesOrder();
            }
        });

        // our button
        Button_location = (Button) findViewById(R.id.Button_location);
        Button_location.setOnClickListener(handler);

        Button_custname = (Button) findViewById(R.id.Button_custname);
        Button_custname.setOnClickListener(handler);

        Button_brandname = (Button) findViewById(R.id.Button_brandname);
        Button_brandname.setOnClickListener(handler);

        Button_modelname = (Button) findViewById(R.id.Button_modelname);
        Button_modelname.setOnClickListener(handler);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickPurchaseDate = (Button) findViewById(R.id.pickPurchaseDate);
        pickPurchaseDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));


        companycd = companycd_emp;
        locationET.setText(companyname_emp);

        if(custname != null && !custname.equals("") )
        {
            custnameET.setText(custname);
        }

        if(custname == null || custname.equals(""))

            {

                customer_custcd = globalVariable.getcashsales_custcd();
                custname = globalVariable.getcashsales_customername();
                custnameET.setText(custname);
        }

        invordernoET.setText(invorderno);
        brandnameET.findFocus();


        if(sysinvorderno != null && !sysinvorderno.equals("0") )
        {
            //  Toast.makeText(getApplicationContext(), sysinvorderno, Toast.LENGTH_LONG).show();
            invokeSearchSalesmen_QuotationWS();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                companycd = bundle.getString("companycd");
                locationET.setText(bundle.getString("companyname"));
            }
        }


        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysbrandno = bundle.getString("sysbrandno");
                brandnameET.setText(bundle.getString("description"));
            }
        }

        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysmodelno = bundle.getString("sysmodelno");
                modelnameET.setText(bundle.getString("modelcode"));
            }
        }

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                customer_custcd = bundle.getString("custcd");
                custnameET.setText(bundle.getString("name"));
            }
        }


        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysbrandno = bundle.getString("sysbrandno");
                brandnameET.setText(bundle.getString("brandname"));
                sysmodelno = bundle.getString("sysmodelno");
                modelnameET.setText(bundle.getString("modelcode"));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_add_quotation) {
            navigatetoCreateQuotationActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    public void CreateSalesOrder() {

        //String orderamount = orderamountET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(userid)) {
            // When Email entered is Valid

            if(sysinvorderno != null && !sysinvorderno.equals("0") )
            {

                paramsMap.put("sysinvorderno", sysinvorderno);
                paramsMap.put("sysorderdtlno", "0");
                paramsMap.put("walkin_custcd", "0" + walkin_custcd);
                paramsMap.put("customer_custcd", "0" +customer_custcd);
                paramsMap.put("userid", userid);
                 paramsMap.put("authid", "AUTHORISE");
                paramsMap.put("authstatus", "10");
                paramsMap.put("sysauthno", "0");

                invokeCreateSalesOrderWS(paramsMap);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please Add Product and Then Create Invoice ", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeCreateSalesOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Authorise_Quotation", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    String sysinvorderno_order ="0";
                    sysinvorderno_order = obj.getString("sysinvorderno");
                    if (Double.valueOf(sysinvorderno_order) > 0)
                    {
                        Map<String, String> paramsMap = new HashMap<>();

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        FileName = "SaesOrder_" + timeStamp + ".pdf";

                        paramsMap.put("sysinvorderno",sysinvorderno_order);
                        paramsMap.put("FileName",FileName);

                        invokeGenerateSalesOrderWS(paramsMap);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }

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

    public void invokeGenerateSalesOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GenerateOrderPDF", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    try {
                        String fileUrl = Config.WEBSERVICE_URL + "MobileQuotation/" + response.getString("error_msg");
                        Utility.downloadAndOpenPdf(Salesmen_SalesQuotation.this, fileUrl,FileName);
                        finish();
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(Salesmen_SalesQuotation.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }

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

    public void navigatetoCreateQuotationActivity(){
        Intent homeIntent = new Intent(Salesmen_SalesQuotation.this,Salesmen_SalesQuotation.class);

        homeIntent.putExtra("sysinvorderno","0");
        homeIntent.putExtra("walkin_custcd","0");
        homeIntent.putExtra("customer_custcd","0");
        homeIntent.putExtra("syscustactno","0" + "0");

        homeIntent.putExtra("custname","");
        homeIntent.putExtra("companycd","0");
        homeIntent.putExtra("companyname","");


        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoLocationListActivity(){
        Intent homeIntent = new Intent(Salesmen_SalesQuotation.this,StockVerificationLocation_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("trftype","TO");
        startActivityForResult(homeIntent,1);
   }

    public void navigatetoBrandListActivity(){
        Intent homeIntent = new Intent(Salesmen_SalesQuotation.this,StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,3);
    }


    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(Salesmen_SalesQuotation.this,StockVerificationModel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysbrandno",sysbrandno);
        startActivityForResult(homeIntent,4);
    }

    public void navigatetoCustomerListActivity(){
        Intent homeIntent = new Intent(Salesmen_SalesQuotation.this,customer_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("calledfrom","QUOTATION");
        startActivityForResult(homeIntent,5);
    }

    @SuppressWarnings("deprecation")
    public void setPurchaseDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    PurchaseDateListener, year, month, day);
        }


        return null;
    }

    private DatePickerDialog.OnDateSetListener PurchaseDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showPurchaseDate(arg1, arg2, arg3);
                }
            };

    private void showPurchaseDate(int year, int month, int day) {
        pickPurchaseDate = (Button) findViewById(R.id.pickPurchaseDate);
        pickPurchaseDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        //invokeWS_Customer_Ledger(custcd);
    }



    public void invokeSearchSalesmen_QuotationWS(){
        // Show Progress Dialog
      //  prgDialog.show();

        invordernoET = (EditText)findViewById(R.id.invorderno);
        final String xinvorderno = invordernoET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("sysinvorderno", "0" +sysinvorderno);
        paramsMap.put("invorderno", "" + xinvorderno);

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SearchSalesmen_Quotation", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    JSONObject header_obj_header = response;
                    JSONArray header_array = header_obj_header.getJSONArray("quotationdetail");
                    JSONObject header_jsonObject = header_array.getJSONObject(0);
                    sysinvorderno=header_jsonObject.getString("sysinvorderno");
                    companycd=header_jsonObject.getString("companycd");
                    customer_custcd=header_jsonObject.getString("customer_custcd");
                    walkin_custcd=header_jsonObject.getString("walkin_custcd");
                    companyname=header_jsonObject.getString("companyname");
                    custname=header_jsonObject.getString("custname");
                    invorderno=header_jsonObject.getString("invorderno");
                    vinvorderdt=header_jsonObject.getString("vinvorderdt");



                    if (header_jsonObject.getString("withlowside").equals("Y"))
                    {
                        chkwithlowside.setChecked(true);
                    }
                    else
                    {
                        chkwithlowside.setChecked(false);
                    }

                    if (header_jsonObject.getString("withinstallation").equals("Y"))
                    {
                        chkwithinstallation.setChecked(true);
                    }
                    else
                    {
                        chkwithinstallation.setChecked(false);
                    }
                    remarksET.setText(header_jsonObject.getString("remarks"));

                    device_token_id=header_jsonObject.getString("device_token_id");
                    invordernoET.setText(invorderno);
                    locationET.setText(companyname);
                    custnameET.setText(custname);
                    invordernoET.setText(vinvorderdt);

                    tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                    tabletoplocationstock.removeAllViews();
                    tabletoplocationstock.setStretchAllColumns(true);
                    tabletoplocationstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(Salesmen_SalesQuotation.this);

                    TextView highsHeading_id= initPlainHeaderTextView();
                    highsHeading_id.setText("ID");
                    highsHeading_id.setWidth(20);
                    highsHeading_id.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_id.setGravity(Gravity.CENTER);

                    TextView highsHeading_Model= initPlainHeaderTextView();
                    highsHeading_Model.setText("Model");
                    highsHeading_Model.setWidth(300);
                    highsHeading_Model.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Model.setGravity(Gravity.CENTER);

                    TextView highsHeading_Quantity = initPlainHeaderTextView();
                    highsHeading_Quantity.setText("Qty");
                    highsHeading_Quantity.setWidth(20);
                    highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Quantity.setGravity(Gravity.CENTER);

                    TextView highsHeading_Price = initPlainHeaderTextView();
                    highsHeading_Price.setText("Price");
                    highsHeading_Price.setWidth(20);
                    highsHeading_Price.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Price.setGravity(Gravity.CENTER);

                    tblrowHeading.addView(highsHeading_id);
                    tblrowHeading.addView(highsHeading_Model);
                    tblrowHeading.addView(highsHeading_Quantity);
                    tblrowHeading.addView(highsHeading_Price);

                    tabletoplocationstock.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("quotationdetail");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            sysinvorderno=jsonObject.getString("sysinvorderno");
                            sysorderdtlno=jsonObject.getString("sysorderdtlno");
                            companycd=jsonObject.getString("companycd");
                            customer_custcd=jsonObject.getString("customer_custcd");
                            walkin_custcd=jsonObject.getString("walkin_custcd");
                            sysbrandno=jsonObject.getString("sysbrandno");
                            sysmodelno=jsonObject.getString("sysmodelno");
                            companyname=jsonObject.getString("companyname");
                            custname=jsonObject.getString("custname");
                            invorderno=jsonObject.getString("invorderno");
                            vinvorderdt=jsonObject.getString("vinvorderdt");
                            brandname=jsonObject.getString("brandname");
                            modelname=jsonObject.getString("modelname");
                            quantity=jsonObject.getString("quantity");

                            invordernoET.setText(invorderno);
                            locationET.setText(companyname);

                            TableRow tblrowLabels = new TableRow(Salesmen_SalesQuotation.this);

                            final TextView highsLabel_id = initPlainTextView(i);
                            final TextView highsLabel_sysinvorderno = initPlainTextView(i);
                            final TextView highsLabel_sysorderdtlno = initPlainTextView(i);
                            final TextView highsLabel_sysmodelno = initPlainTextView(i);
                            final TextView highsLabel_modelname = initPlainTextView(i);
                            final TextView highsLabel_sysbrandno = initPlainTextView(i);
                            final TextView highsLabel_brandname = initPlainTextView(i);
                            final TextView highsLabel_quantity = initPlainTextView(i);
                            final TextView highsLabel_unitprice = initPlainTextView(i);

                            final TextView highsLabel_sysproductno = initPlainTextView(i);
                            final TextView highsLabel_stickernumber = initPlainTextView(i);
                            final TextView highsLabel_serialno = initPlainTextView(i);

                            highsLabel_sysinvorderno.setText(jsonObject.getString("sysinvorderno"));
                            highsLabel_sysorderdtlno.setText(jsonObject.getString("sysorderdtlno"));
                            highsLabel_sysmodelno.setText(jsonObject.getString("sysmodelno"));
                            highsLabel_sysbrandno.setText(jsonObject.getString("sysbrandno"));
                            highsLabel_brandname.setText(jsonObject.getString("brandname"));
                            highsLabel_unitprice.setText(jsonObject.getString("unitprice_include_tax"));
                            highsLabel_sysproductno.setText(jsonObject.getString("sysproductno"));
                            highsLabel_stickernumber.setText(jsonObject.getString("stickernumber"));
                            highsLabel_serialno.setText(jsonObject.getString("serialno"));

                            highsLabel_id.setText(jsonObject.getString("id"));
                            highsLabel_id.setWidth(20);
                            highsLabel_id.setTypeface(Typeface.DEFAULT);
                            highsLabel_id.setGravity(Gravity.CENTER);

                            highsLabel_modelname.setText(jsonObject.getString("modelname"));
                            highsLabel_modelname.setWidth(300);
                            highsLabel_modelname.setTypeface(Typeface.DEFAULT);
                            highsLabel_modelname.setGravity(Gravity.LEFT);
                            highsLabel_modelname.setOnClickListener(new OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent = new Intent(Salesmen_SalesQuotation.this, Salesmen_SalesQuotationModel.class);
                                    intent.putExtra("sysinvorderno",highsLabel_sysinvorderno.getText());
                                    intent.putExtra("sysorderdtlno",highsLabel_sysorderdtlno.getText());
                                    intent.putExtra("sysbrandno",highsLabel_sysbrandno.getText());
                                    intent.putExtra("companycd",companycd);
                                    intent.putExtra("sysmodelno",highsLabel_sysmodelno.getText());
                                    intent.putExtra("customer_custcd",customer_custcd);
                                    intent.putExtra("walkin_custcd",walkin_custcd);
                                    intent.putExtra("companyname",companyname);
                                    intent.putExtra("custname",custname);
                                    intent.putExtra("invorderno",invorderno);
                                    intent.putExtra("vinvorderdt",vinvorderdt);
                                    intent.putExtra("brandname",highsLabel_brandname.getText());
                                    intent.putExtra("modelname",highsLabel_modelname.getText());
                                    intent.putExtra("quantity",highsLabel_quantity.getText());
                                    intent.putExtra("unitprice",highsLabel_unitprice.getText());
                                    intent.putExtra("sysproductno",highsLabel_sysproductno.getText());
                                    intent.putExtra("stickernumber",highsLabel_stickernumber.getText());
                                    intent.putExtra("serialno",highsLabel_serialno.getText());

                                    startActivity(intent);
                                }
                            });

                            highsLabel_quantity.setText(jsonObject.getString("quantity"));
                            highsLabel_quantity.setWidth(20);
                            highsLabel_quantity.setTypeface(Typeface.DEFAULT);
                            highsLabel_quantity.setGravity(Gravity.CENTER);

                            TextView highsLabel_unitprice_include_tax = initPlainTextView(i);
                            highsLabel_unitprice_include_tax.setText(jsonObject.getString("unitprice_include_tax"));
                            highsLabel_unitprice_include_tax.setWidth(20);
                            highsLabel_unitprice_include_tax.setTypeface(Typeface.DEFAULT);
                            highsLabel_unitprice_include_tax.setGravity(Gravity.CENTER);

                            tblrowLabels.addView(highsLabel_id);
                            tblrowLabels.addView(highsLabel_modelname);
                            tblrowLabels.addView(highsLabel_quantity);
                            tblrowLabels.addView(highsLabel_unitprice_include_tax);

                            tblrowLabels.setClickable(true);

                            tabletoplocationstock.addView(tblrowLabels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    quantityET.setText("");
                    unitrateET.setText("");
                    exchangedetailsET.setText("");
                    modelnameET.setText("");
                    brandnameET.setText("");
                    sysbrandno="0";
                    sysmodelno="0";

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
           //     prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    public void onClick_CreateSalesmen_Quotation(View view) {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();
        final String sysemployeeno = globalVariable.getsysemployeeno();
        String withlowside, withinstallation;
        invordernoET = (EditText)findViewById(R.id.invorderno);
        quantityET = (EditText)findViewById(R.id.quantity);
        unitrateET = (EditText)findViewById(R.id.unitrate);
        exchangedetailsET = (EditText)findViewById(R.id.exchangedetails);
        String invorderno = invordernoET.getText().toString();
        String quantity= quantityET.getText().toString();
        String unitrate= unitrateET.getText().toString();
        String exchangedetails= exchangedetailsET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        if (sysmodelno.equals("0"))
        {

            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
        else {


        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(companycd) || Utility.isNotNull(walkin_custcd) || Utility.isNotNull(invorderno) || Utility.isNotNull(quantity) || Utility.isNotNull(unitrate) || Utility.isNotNull(sysmodelno) || Utility.isNotNull(sysbrandno) ) {
            // When Email entered is Valid

            pickPurchaseDate = (Button) findViewById(R.id.pickPurchaseDate);
            String invorderdt = pickPurchaseDate.getText().toString();


            String remarks = remarksET.getText().toString();
            if(sysinvorderno == null)
            {
                sysinvorderno="0";
            }

            if(walkin_custcd == null)
            {
                walkin_custcd="0";
            }



            if(chkwithlowside.isChecked())
            {
                withlowside = "Y";
            }
            else
            {
                withlowside = "N";
            };

            if(chkwithinstallation.isChecked())
            {
                withinstallation = "Y";
            }
            else
            {
                withinstallation = "N";
            };




            paramsMap.put("sysinvorderno", sysinvorderno);
            paramsMap.put("sysorderdtlno", "0");
            paramsMap.put("walkin_custcd", "0" + walkin_custcd);
            paramsMap.put("customer_custcd", "0" +customer_custcd);
            paramsMap.put("invorderno", "" + invorderno);
            paramsMap.put("invorderdt", "" + invorderdt);
            paramsMap.put("sysproductno", "0");
            paramsMap.put("sysmodelno", "0" +sysmodelno);
            paramsMap.put("quantity", "0" +quantity);
            paramsMap.put("unitprice_include_tax", "0" +unitrate);
            paramsMap.put("companycd", "0" +companycd);
            paramsMap.put("sysemployeeno", "0" +sysemployeeno);
            paramsMap.put("userid", userid);
            paramsMap.put("exchangedetails", ""+exchangedetails);
            paramsMap.put("fincompanycd", "0" );
            paramsMap.put("approved_by_manager", "0"+sysemployeeno);
            paramsMap.put("remarks", "" + remarks);
            paramsMap.put("withlowside", "" + withlowside);
            paramsMap.put("withinstallation", "" + withinstallation);

            invokeCreateSalesmen_QuotationWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
        }
    }

    public void invokeCreateSalesmen_QuotationWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/CreateSalesmen_Quotation", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    JSONObject header_obj_header = response;
                    JSONArray header_array = header_obj_header.getJSONArray("quotationdetail");
                    JSONObject header_jsonObject = header_array.getJSONObject(0);
                    sysinvorderno=header_jsonObject.getString("sysinvorderno");
                    companycd=header_jsonObject.getString("companycd");
                    customer_custcd=header_jsonObject.getString("customer_custcd");
                    walkin_custcd=header_jsonObject.getString("walkin_custcd");
                    companyname=header_jsonObject.getString("companyname");
                    custname=header_jsonObject.getString("custname");
                    invorderno=header_jsonObject.getString("invorderno");
                    vinvorderdt=header_jsonObject.getString("vinvorderdt");
                    device_token_id=header_jsonObject.getString("device_token_id");
                    remarksET.setText(header_jsonObject.getString("remarks"));
                    if (header_jsonObject.getString("withlowside").equals("Y"))
                    {
                        chkwithlowside.setChecked(true);
                    }
                    else
                    {
                        chkwithlowside.setChecked(false);
                    }

                    if (header_jsonObject.getString("withinstallation").equals("Y"))
                    {
                        chkwithinstallation.setChecked(true);
                    }
                    else
                    {
                        chkwithinstallation.setChecked(false);
                    }


                    invordernoET.setText(invorderno);
                    locationET.setText(companyname);
                    custnameET.setText(custname);
                    invordernoET.setText(vinvorderdt);

                    tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                    tabletoplocationstock.removeAllViews();
                    tabletoplocationstock.setStretchAllColumns(true);
                    tabletoplocationstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(Salesmen_SalesQuotation.this);

                    TextView highsHeading_id= initPlainHeaderTextView();
                    highsHeading_id.setText("ID");
                    highsHeading_id.setWidth(20);
                    highsHeading_id.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_id.setGravity(Gravity.CENTER);

                    TextView highsHeading_Model= initPlainHeaderTextView();
                    highsHeading_Model.setText("Model");
                    highsHeading_Model.setWidth(300);
                    highsHeading_Model.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Model.setGravity(Gravity.CENTER);

                    TextView highsHeading_Quantity = initPlainHeaderTextView();
                    highsHeading_Quantity.setText("Quantity");
                    highsHeading_Quantity.setWidth(20);
                    highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Quantity.setGravity(Gravity.CENTER);

                    TextView highsHeading_Price = initPlainHeaderTextView();
                    highsHeading_Price.setText("Price");
                    highsHeading_Price.setWidth(20);
                    highsHeading_Price.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Price.setGravity(Gravity.CENTER);

                    tblrowHeading.addView(highsHeading_id);
                    tblrowHeading.addView(highsHeading_Model);
                    tblrowHeading.addView(highsHeading_Quantity);
                    tblrowHeading.addView(highsHeading_Price);

                    tabletoplocationstock.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("quotationdetail");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            sysinvorderno=jsonObject.getString("sysinvorderno");
                            sysorderdtlno=jsonObject.getString("sysorderdtlno");
                            companycd=jsonObject.getString("companycd");
                            customer_custcd=header_jsonObject.getString("customer_custcd");
                            walkin_custcd=header_jsonObject.getString("walkin_custcd");
                            sysbrandno=jsonObject.getString("sysbrandno");
                            sysmodelno=jsonObject.getString("sysmodelno");
                            companyname=jsonObject.getString("companyname");
                            custname=jsonObject.getString("custname");
                            invorderno=jsonObject.getString("invorderno");
                            vinvorderdt=jsonObject.getString("vinvorderdt");
                            brandname=jsonObject.getString("brandname");
                            modelname=jsonObject.getString("modelname");
                            quantity=jsonObject.getString("quantity");

                            invordernoET.setText(invorderno);

                            TableRow tblrowLabels = new TableRow(Salesmen_SalesQuotation.this);

                            final TextView highsLabel_id = initPlainTextView(i);
                            final TextView highsLabel_sysinvorderno = initPlainTextView(i);
                            final TextView highsLabel_sysorderdtlno = initPlainTextView(i);
                            final TextView highsLabel_sysmodelno = initPlainTextView(i);
                            final TextView highsLabel_modelname = initPlainTextView(i);
                            final TextView highsLabel_sysbrandno = initPlainTextView(i);
                            final TextView highsLabel_brandname = initPlainTextView(i);
                            final TextView highsLabel_quantity = initPlainTextView(i);
                            final TextView highsLabel_unitprice = initPlainTextView(i);

                            highsLabel_sysinvorderno.setText(jsonObject.getString("sysinvorderno"));
                            highsLabel_sysorderdtlno.setText(jsonObject.getString("sysorderdtlno"));
                            highsLabel_sysmodelno.setText(jsonObject.getString("sysmodelno"));
                            highsLabel_sysbrandno.setText(jsonObject.getString("sysbrandno"));
                            highsLabel_brandname.setText(jsonObject.getString("brandname"));
                            highsLabel_unitprice.setText(jsonObject.getString("unitprice_include_tax"));

                            highsLabel_id.setText(jsonObject.getString("id"));
                            highsLabel_id.setWidth(20);
                            highsLabel_id.setTypeface(Typeface.DEFAULT);
                            highsLabel_id.setGravity(Gravity.CENTER);

                            highsLabel_modelname.setText(jsonObject.getString("modelname"));
                            highsLabel_modelname.setWidth(300);
                            highsLabel_modelname.setTypeface(Typeface.DEFAULT);
                            highsLabel_modelname.setGravity(Gravity.LEFT);
                            highsLabel_modelname.setOnClickListener(new OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent = new Intent(Salesmen_SalesQuotation.this, Salesmen_SalesQuotationModel.class);
                                    intent.putExtra("sysinvorderno",highsLabel_sysinvorderno.getText());
                                    intent.putExtra("sysorderdtlno",highsLabel_sysorderdtlno.getText());
                                    intent.putExtra("sysbrandno",highsLabel_sysbrandno.getText());
                                    intent.putExtra("companycd",companycd);
                                    intent.putExtra("sysmodelno",highsLabel_sysmodelno.getText());
                                    intent.putExtra("customer_custcd",customer_custcd);
                                    intent.putExtra("walkin_custcd",walkin_custcd);
                                    intent.putExtra("companyname",companyname);
                                    intent.putExtra("custname",custname);
                                    intent.putExtra("invorderno",invorderno);
                                    intent.putExtra("vinvorderdt",vinvorderdt);
                                    intent.putExtra("brandname",highsLabel_brandname.getText());
                                    intent.putExtra("modelname",highsLabel_modelname.getText());
                                    intent.putExtra("quantity",highsLabel_quantity.getText());
                                    intent.putExtra("unitprice",highsLabel_unitprice.getText());

                                    startActivity(intent);
                                }
                            });

                            highsLabel_quantity.setText(jsonObject.getString("quantity"));
                            highsLabel_quantity.setWidth(20);
                            highsLabel_quantity.setTypeface(Typeface.DEFAULT);
                            highsLabel_quantity.setGravity(Gravity.CENTER);

                            TextView highsLabel_unitprice_include_tax = initPlainTextView(i);
                            highsLabel_unitprice_include_tax.setText(jsonObject.getString("unitprice_include_tax"));
                            highsLabel_unitprice_include_tax.setWidth(20);
                            highsLabel_unitprice_include_tax.setTypeface(Typeface.DEFAULT);
                            highsLabel_unitprice_include_tax.setGravity(Gravity.CENTER);

                            tblrowLabels.addView(highsLabel_id);
                            tblrowLabels.addView(highsLabel_modelname);
                            tblrowLabels.addView(highsLabel_quantity);
                            tblrowLabels.addView(highsLabel_unitprice_include_tax);

                            tblrowLabels.setClickable(true);

                            tabletoplocationstock.addView(tblrowLabels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    quantityET.setText("");
                    unitrateET.setText("");
                    modelnameET.setText("");
                    brandnameET.setText("");
                    sysbrandno="0";
                    sysmodelno="0";

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
                prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Salesmen_SalesQuotation.this);
        textView.setPadding(10, 30, 10, 30);


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
        TextView textView = new TextView(Salesmen_SalesQuotation.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Salesmen_SalesQuotation.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    public void download_General_Quotation(View v)
    {

        if(sysinvorderno != null && !sysinvorderno.equals("0") )
        {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String consignor_companycd  = globalVariable.getcompanycd();
            final String sysemployeeno  = globalVariable.getsysemployeeno();
            final String userid  = globalVariable.getuserid();

            Map<String, String> paramsMap = new HashMap<>();

            // When Email Edit View and Password Edit View have values other than Null
            if (Utility.isNotNull(sysinvorderno)) {
                // When Email entered is Valid

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                FileName = "Quotation_" + timeStamp + ".pdf";

                paramsMap.put("sysinvorderno",sysinvorderno);
                paramsMap.put("FileName",FileName);

                invoke_Generate_Quotation(paramsMap);

            }
            else {
                Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
            }
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Order Not Saved", Toast.LENGTH_LONG).show();
        }

    }

    public void invoke_Generate_Quotation(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GenerateQuotationPDF_General", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;
 
                    try {
                        String fileUrl = Config.WEBSERVICE_URL + "MobileQuotation/" + response.getString("error_msg");
                        Utility.downloadAndOpenPdf(Salesmen_SalesQuotation.this, fileUrl,FileName);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(Salesmen_SalesQuotation.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }

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

    public void invoke_Generate_Annexure(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GenerateQuotationPDF_Annexure", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    try {
                        String fileUrl = Config.WEBSERVICE_URL + "MobileQuotation/" + response.getString("error_msg");
                        Utility.downloadAndOpenPdf(Salesmen_SalesQuotation.this, fileUrl,FileName);
                     //   finish();
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(Salesmen_SalesQuotation.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }

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

    public void UpdateQuotation() {

        //String orderamount = orderamountET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();
        final  String remarks = remarksET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(userid)) {
            // When Email entered is Valid


            if(sysinvorderno != null && !sysinvorderno.equals("0") )
            {
                paramsMap.put("sysinvorderno", sysinvorderno);

                paramsMap.put("remarks",remarks );
                paramsMap.put("userid", userid);

                invokeUpdateQuotationWS(paramsMap);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please Add Product and Then Create Invoice ", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeUpdateQuotationWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Update_Quotation_Remarks", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

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

}

