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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Procurement extends AppCompatActivity {

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
    private Button btnCreateInvoice;

    EditText locationET ;
    EditText supplierET;
    EditText refdocumentnoET ;
    EditText brandnameET;
    EditText modelnameET ;
    EditText quantityET;
    EditText unitrateET;

    Button search_refdocumentno, Button_modelname_new;

    ListViewAdapter adapter;
    EditText inputSearch;

    String companycd  = "0", sysvendorno  = "0", sysbrandno  = "0", sysmodelno  = "0", sysprocurmentno  = "0", sysprocdtlno  = "0";
    String companyname, vendorname, srefdocumentno, vprocurementdt, brandname, modelname, quantity, serialnnocount;

    Button Button_supplier, Button_modelname,  Button_brandname;
    Button Button_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement);

        Intent i = getIntent();
        sysprocurmentno = i.getStringExtra("sysprocurmentno");

        locationET = (EditText)findViewById(R.id.location);
        supplierET = (EditText)findViewById(R.id.supplier);
        refdocumentnoET = (EditText)findViewById(R.id.refdocumentno);
        brandnameET = (EditText)findViewById(R.id.brandname);
        modelnameET = (EditText)findViewById(R.id.modelname);
        quantityET = (EditText)findViewById(R.id.quantity);
        unitrateET = (EditText)findViewById(R.id.unitrate);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        btnCreateInvoice = (Button) findViewById(R.id.btnCreateInvoice);
        //btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        btnCreateInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CreateInvoice();
            }
        });

        Button_modelname_new= (Button) findViewById(R.id.Button_modelname_new);
        Button_modelname_new.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                navigatetoModelCreateActivity();
            }
        });

        search_refdocumentno= (Button) findViewById(R.id.search_refdocumentno);
        search_refdocumentno.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sysprocurmentno="0";
                invokeSearchProcurementWS();
            }
        });

        // button on click listener

        OnClickListener handler = new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.Button_location:
                        navigatetoLocationListActivity();
                        break;

                        case R.id.Button_supplier:
                        navigatetoSupplierListActivity();
                        break;

                    case R.id.Button_brandname:
                        navigatetoBrandListActivity();
                        break;

                        case R.id.Button_modelname:
                        navigatetoModelListActivity();
                        break;
                }
            }
        };

        // our button
        Button_location = (Button) findViewById(R.id.Button_location);
        Button_location.setOnClickListener(handler);

        Button_supplier = (Button) findViewById(R.id.Button_supplier);
        Button_supplier.setOnClickListener(handler);

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

        sysprocurmentno=i.getStringExtra("sysprocurmentno");
        companycd=i.getStringExtra("companycd");
        sysvendorno=i.getStringExtra("sysvendorno");

        companyname=i.getStringExtra("companyname");
        vendorname=i.getStringExtra("vendorname");
        srefdocumentno=i.getStringExtra("refdocumentno");

        vprocurementdt=i.getStringExtra("vprocurementdt");

       // locationET.setText(companyname);
        //supplierET.setText(vendorname);
        //refdocumentnoET.setText(srefdocumentno);
        //pickPurchaseDate.setText(vprocurementdt);


        if(sysprocurmentno != null)
        {
         //  Toast.makeText(getApplicationContext(), sysprocurmentno, Toast.LENGTH_LONG).show();
            invokeSearchProcurementWS();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                companycd = bundle.getString("companycd");
                locationET.setText(bundle.getString("companyname"));
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysvendorno = bundle.getString("sysvendorno");
                supplierET.setText(bundle.getString("description"));
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

    public void navigatetoModelCreateActivity(){
        Intent homeIntent = new Intent(Procurement.this,Procurement_Model_Create.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,99);
    }

    public void navigatetoLocationListActivity(){
        Intent homeIntent = new Intent(Procurement.this,StockVerificationLocation_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("trftype","TO");
        startActivityForResult(homeIntent,1);
    }

    public void navigatetoSupplierListActivity(){
        Intent homeIntent = new Intent(Procurement.this,ProcurementSupplier_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,2);
    }

    public void navigatetoBrandListActivity(){
        Intent homeIntent = new Intent(Procurement.this,StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,3);
    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(Procurement.this,StockVerificationModel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysbrandno",sysbrandno);
        startActivityForResult(homeIntent,4);
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
        //invokeWS_Customer_Ledger(sysvendorno);
    }

    public void invokeSearchProcurementWS(){
        // Show Progress Dialog
      //  prgDialog.show();

        refdocumentnoET = (EditText)findViewById(R.id.refdocumentno);
        final String xrefdocumentno = refdocumentnoET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("sysprocurmentno", "0" +sysprocurmentno);
        paramsMap.put("refbillno", "" + xrefdocumentno);

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SearchProcurement", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
            //    prgDialog.hide();
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("procurementdtl");

                    JSONObject jsonObject_one = new_array.getJSONObject(0);

                    sysprocurmentno=jsonObject_one.getString("sysprocurmentno");
                    companycd=jsonObject_one.getString("companycd");
                    sysvendorno=jsonObject_one.getString("sysvendorno");

                    companyname=jsonObject_one.getString("companyname");
                    vendorname=jsonObject_one.getString("vendorname");
                    srefdocumentno=jsonObject_one.getString("refdocumentno");
                    vprocurementdt=jsonObject_one.getString("vprocurementdt");

                    locationET = (EditText)findViewById(R.id.location);
                    supplierET = (EditText)findViewById(R.id.supplier);
                    refdocumentnoET = (EditText)findViewById(R.id.refdocumentno);
                    pickPurchaseDate = (Button) findViewById(R.id.pickPurchaseDate);

                    locationET.setText(jsonObject_one.getString("companyname"));
                    supplierET.setText(jsonObject_one.getString("vendorname"));
                    refdocumentnoET.setText(jsonObject_one.getString("refdocumentno"));
                    pickPurchaseDate.setText(jsonObject_one.getString("vprocurementdt"));

                    tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                    tabletoplocationstock.removeAllViews();
                    tabletoplocationstock.setStretchAllColumns(true);
                    tabletoplocationstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(Procurement.this);

                    TextView highsHeading_Serial= initPlainHeaderTextView();
                    highsHeading_Serial.setText("Model");
                    highsHeading_Serial.setWidth(300);
                    highsHeading_Serial.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Serial.setGravity(Gravity.LEFT);

                    TextView highsHeading_Sticker = initPlainHeaderTextView();
                    highsHeading_Sticker.setText("Quantity");
                    highsHeading_Sticker.setWidth(20);
                    highsHeading_Sticker.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Sticker.setGravity(Gravity.LEFT);

                    TextView highsHeading_serialnnocount = initPlainHeaderTextView();
                    highsHeading_serialnnocount.setText("Pending");
                    highsHeading_serialnnocount.setWidth(20);
                    highsHeading_serialnnocount.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_serialnnocount.setGravity(Gravity.LEFT);

                    tblrowHeading.addView(highsHeading_Serial);
                    tblrowHeading.addView(highsHeading_Sticker);
                    tblrowHeading.addView(highsHeading_serialnnocount);

                    tabletoplocationstock.addView(tblrowHeading);


                    JSONObject obj1 = response;
                    JSONArray new_array1 = obj1.getJSONArray("procurementdtl");

                    for (int i = 0, count = new_array1.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array1.getJSONObject(i);

                            quantity=jsonObject.getString("quantity");
                            serialnnocount=jsonObject.getString("serialnnocount");

                            TableRow tblrowLabels = new TableRow(Procurement.this);

                            final TextView highsLabel_sysprocurmentno = initPlainTextView(i);
                            final TextView highsLabel_sysprocdtlno = initPlainTextView(i);
                            final TextView highsLabel_sysmodelno = initPlainTextView(i);
                            final TextView highsLabel_modelname = initPlainTextView(i);
                            final TextView highsLabel_sysbrandno = initPlainTextView(i);
                            final TextView highsLabel_brandname = initPlainTextView(i);
                            final TextView highsLabel_quantity = initPlainTextView(i);

                            highsLabel_sysprocurmentno.setText(jsonObject.getString("sysprocurmentno"));
                            highsLabel_sysprocdtlno.setText(jsonObject.getString("sysprocdtlno"));
                            highsLabel_sysmodelno.setText(jsonObject.getString("sysmodelno"));
                            highsLabel_modelname.setText(jsonObject.getString("modelname"));
                            highsLabel_sysbrandno.setText(jsonObject.getString("sysbrandno"));
                            highsLabel_brandname.setText(jsonObject.getString("brandname"));
                            highsLabel_quantity.setText(jsonObject.getString("quantity"));

                            TextView highsLabel_serialno = initPlainTextView(i);
                            highsLabel_serialno.setText(jsonObject.getString("modelname"));
                            highsLabel_serialno.setWidth(300);
                            highsLabel_serialno.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialno.setGravity(Gravity.LEFT);
                            highsLabel_serialno.setOnClickListener(new OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent = new Intent(Procurement.this, ProcurementModelSerial.class);
                                    intent.putExtra("sysprocurmentno",highsLabel_sysprocurmentno.getText());
                                    intent.putExtra("sysprocdtlno",highsLabel_sysprocdtlno.getText());
                                    intent.putExtra("sysbrandno",highsLabel_sysbrandno.getText());
                                    intent.putExtra("companycd",companycd);
                                    intent.putExtra("sysmodelno",highsLabel_sysmodelno.getText());
                                    intent.putExtra("sysvendorno",sysvendorno);
                                    intent.putExtra("companyname",companyname);
                                    intent.putExtra("vendorname",vendorname);
                                    intent.putExtra("refdocumentno",srefdocumentno);
                                    intent.putExtra("vprocurementdt",vprocurementdt);
                                    intent.putExtra("brandname",highsLabel_brandname.getText());
                                    intent.putExtra("modelname",highsLabel_modelname.getText());
                                    intent.putExtra("quantity",highsLabel_quantity.getText());
                                    intent.putExtra("serialnnocount",serialnnocount);

                                    startActivity(intent);
                                }
                            });

                            TextView highsLabel_barcodeno = initPlainTextView(i);
                            highsLabel_barcodeno.setText(jsonObject.getString("quantity"));
                            highsLabel_barcodeno.setWidth(20);
                            highsLabel_barcodeno.setTypeface(Typeface.DEFAULT);
                            highsLabel_barcodeno.setGravity(Gravity.CENTER);

                            TextView highsLabel_serialnnocount = initPlainTextView(i);
                            highsLabel_serialnnocount.setText(jsonObject.getString("serialnnocount"));
                            highsLabel_serialnnocount.setWidth(20);
                            highsLabel_serialnnocount.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialnnocount.setGravity(Gravity.CENTER);

                            tblrowLabels.addView(highsLabel_serialno);
                            tblrowLabels.addView(highsLabel_barcodeno);
                            tblrowLabels.addView(highsLabel_serialnnocount);

                            tblrowLabels.setClickable(true);

                            tabletoplocationstock.addView(tblrowLabels);



                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
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
           //     prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }


    public void onClick_CreateProcurement(View view) {

        // String orderamount = orderamountET.getText().toString();
        // String srefbillno = refbillnoET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        refdocumentnoET = (EditText)findViewById(R.id.refdocumentno);
        quantityET = (EditText)findViewById(R.id.quantity);
        unitrateET = (EditText)findViewById(R.id.unitrate);

        String refdocumentno = refdocumentnoET.getText().toString();
        String quantity= quantityET.getText().toString();
        String unitrate= unitrateET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(companycd) || Utility.isNotNull(sysvendorno) || Utility.isNotNull(refdocumentno) || Utility.isNotNull(quantity) || Utility.isNotNull(unitrate) || Utility.isNotNull(sysmodelno) || Utility.isNotNull(sysbrandno) ) {
            // When Email entered is Valid

            pickPurchaseDate = (Button) findViewById(R.id.pickPurchaseDate);
            String PurchaseDate = pickPurchaseDate.getText().toString();

            if(sysprocurmentno == null)
            {
                sysprocurmentno="0";
            }
            paramsMap.put("sysprocurmentno", sysprocurmentno);
            paramsMap.put("sysprocdtlno", "0");
            paramsMap.put("sysvendorno", "0" +sysvendorno);
            paramsMap.put("refbillno", "" + refdocumentno);
            paramsMap.put("procurementdt", "" + PurchaseDate);
            paramsMap.put("sysmodelno", "0" +sysmodelno);
            paramsMap.put("quantity", "0" +quantity);
            paramsMap.put("unitrate", "0" +unitrate);
            paramsMap.put("companycd", "0" +companycd);
            paramsMap.put("userid", userid);

            invokeCreateProcurementWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeCreateProcurementWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/CreateProcurement", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                    tabletoplocationstock.removeAllViews();
                    tabletoplocationstock.setStretchAllColumns(true);
                    tabletoplocationstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(Procurement.this);

                    TextView highsHeading_Serial= initPlainHeaderTextView();
                    highsHeading_Serial.setText("Model");
                    highsHeading_Serial.setWidth(300);
                    highsHeading_Serial.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Serial.setGravity(Gravity.CENTER);

                    TextView highsHeading_Sticker = initPlainHeaderTextView();
                    highsHeading_Sticker.setText("Quantity");
                    highsHeading_Sticker.setWidth(20);
                    highsHeading_Sticker.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Sticker.setGravity(Gravity.CENTER);

                    TextView highsHeading_serialnnocount = initPlainHeaderTextView();
                    highsHeading_serialnnocount.setText("Pending");
                    highsHeading_serialnnocount.setWidth(20);
                    highsHeading_serialnnocount.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_serialnnocount.setGravity(Gravity.CENTER);

                    tblrowHeading.addView(highsHeading_Serial);
                    tblrowHeading.addView(highsHeading_Sticker);
                    tblrowHeading.addView(highsHeading_serialnnocount);

                    tabletoplocationstock.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("procurementdtl");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            sysprocurmentno=jsonObject.getString("sysprocurmentno");
                            sysprocdtlno=jsonObject.getString("sysprocdtlno");
                            companycd=jsonObject.getString("companycd");
                            sysvendorno=jsonObject.getString("sysvendorno");
                            sysbrandno=jsonObject.getString("sysbrandno");
                            sysmodelno=jsonObject.getString("sysmodelno");
                            companyname=jsonObject.getString("companyname");
                            vendorname=jsonObject.getString("vendorname");
                            srefdocumentno=jsonObject.getString("refdocumentno");
                            vprocurementdt=jsonObject.getString("vprocurementdt");
                            brandname=jsonObject.getString("brandname");
                            modelname=jsonObject.getString("modelname");
                            quantity=jsonObject.getString("quantity");
                            serialnnocount=jsonObject.getString("serialnnocount");

                            TableRow tblrowLabels = new TableRow(Procurement.this);

                            final TextView highsLabel_sysprocurmentno = initPlainTextView(i);
                            final TextView highsLabel_sysprocdtlno = initPlainTextView(i);
                            final TextView highsLabel_sysmodelno = initPlainTextView(i);
                            final TextView highsLabel_modelname = initPlainTextView(i);
                            final TextView highsLabel_sysbrandno = initPlainTextView(i);
                            final TextView highsLabel_brandname = initPlainTextView(i);
                            final TextView highsLabel_quantity = initPlainTextView(i);

                            highsLabel_sysprocurmentno.setText(jsonObject.getString("sysprocurmentno"));
                            highsLabel_sysprocdtlno.setText(jsonObject.getString("sysprocdtlno"));
                            highsLabel_sysmodelno.setText(jsonObject.getString("sysmodelno"));
                            highsLabel_modelname.setText(jsonObject.getString("modelname"));
                            highsLabel_sysbrandno.setText(jsonObject.getString("sysbrandno"));
                            highsLabel_brandname.setText(jsonObject.getString("brandname"));
                            highsLabel_quantity.setText(jsonObject.getString("quantity"));

                            TextView highsLabel_serialno = initPlainTextView(i);
                            highsLabel_serialno.setText(jsonObject.getString("modelname"));
                            highsLabel_serialno.setWidth(300);
                            highsLabel_serialno.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialno.setGravity(Gravity.LEFT);
                            highsLabel_serialno.setOnClickListener(new OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent = new Intent(Procurement.this, ProcurementModelSerial.class);
                                    intent.putExtra("sysprocurmentno",highsLabel_sysprocurmentno.getText());
                                    intent.putExtra("sysprocdtlno",highsLabel_sysprocdtlno.getText());
                                    intent.putExtra("sysbrandno",highsLabel_sysbrandno.getText());
                                    intent.putExtra("companycd",companycd);
                                    intent.putExtra("sysmodelno",highsLabel_sysmodelno.getText());
                                    intent.putExtra("sysvendorno",sysvendorno);
                                    intent.putExtra("companyname",companyname);
                                    intent.putExtra("vendorname",vendorname);
                                    intent.putExtra("refdocumentno",srefdocumentno);
                                    intent.putExtra("vprocurementdt",vprocurementdt);
                                    intent.putExtra("brandname",highsLabel_brandname.getText());
                                    intent.putExtra("modelname",highsLabel_modelname.getText());
                                    intent.putExtra("quantity",highsLabel_quantity.getText());
                                    intent.putExtra("serialnnocount",serialnnocount);

                                    startActivity(intent);
                                }
                            });


                            TextView highsLabel_barcodeno = initPlainTextView(i);
                            highsLabel_barcodeno.setText(jsonObject.getString("quantity"));
                            highsLabel_barcodeno.setWidth(20);
                            highsLabel_barcodeno.setTypeface(Typeface.DEFAULT);
                            highsLabel_barcodeno.setGravity(Gravity.CENTER);

                            TextView highsLabel_serialnnocount = initPlainTextView(i);
                            highsLabel_serialnnocount.setText(jsonObject.getString("serialnnocount"));
                            highsLabel_serialnnocount.setWidth(20);
                            highsLabel_serialnnocount.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialnnocount.setGravity(Gravity.CENTER);

                            tblrowLabels.addView(highsLabel_serialno);
                            tblrowLabels.addView(highsLabel_barcodeno);
                            tblrowLabels.addView(highsLabel_serialnnocount);

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

    public void onClick_DeleteProcurement(View view) {
        // Get Email Edit View Value

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysprocdtlno)) {
            // Put Http parameter usermodelname with value of Email Edit View control
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            paramsMap.put("sysprocurmentno", "0"+sysprocurmentno);
            paramsMap.put("userid", "0"+userid);

            invokeDeleteProcurementWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeDeleteProcurementWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/DeleteProcurement", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                //  prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    // When the JSON response has status boolean value assigned with true

                    //Toast.makeText(getApplicationContext(), "Customer is successfully added!", Toast.LENGTH_LONG).show();

                    if(obj.getBoolean("status")){
                        // Set Default Values for Edit View controls
                        //setResetValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Purchase Deleted Successfully...!", Toast.LENGTH_LONG).show();
                        //navigatetoFollowupActivity();
                        finish();
                    }
                    // Else display error message
                    else{
                        errorMsg.setText(obj.getString("error_msg"));
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
                //  prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Procurement.this);
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
        TextView textView = new TextView(Procurement.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Procurement.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }



    public void CreateInvoice() {

        //String orderamount = orderamountET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(userid)) {
            // When Email entered is Valid

            paramsMap.put("syshdrno", sysprocurmentno);
            paramsMap.put("userid", userid);
            paramsMap.put("authid", "AUTHORISE");
            paramsMap.put("authstatus", "10");
            paramsMap.put("sysauthno", "0");

            invokeAuthoriseInvoiceWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeAuthoriseInvoiceWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Authorise_Purchase_Invoice", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    String sysinvno_new = obj.getString("sysinvno");
                   // sysinvno = obj.getString("sysinvno");
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

