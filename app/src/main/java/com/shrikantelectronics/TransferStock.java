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

import java.util.Calendar;

public class TransferStock extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    TableLayout tabletransferstock;
    TextView header_topsource_companynamestock;
    int year, month, day;
    Button pickTransferDate;

    EditText source_companynameET ;
    EditText destination_companynameET;
    EditText brandnameET;
    EditText transporternameET;
    EditText serialnoET ;
    EditText barcodeET;
    EditText remarksET;

    EditText scanserialno;

    ListViewAdapter adapter;
    EditText inputSearch;

    String source_companycd, destination_companycd, sysbrandno, sysproductno, systrfno, systrfdtlno, transportercd;
    String source_companyname, destination_companyname, vtransferdt, brandname, serialno, barcodeno, serialnnocount, transportername;

    Button Button_location_to, Button_serialno,  Button_brandname, Button_transportername;
    Button Button_location_from, Button_scanserialno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferstock);

        Intent i = getIntent();
        systrfno = i.getStringExtra("systrfno");

        sysbrandno = "0";
        source_companynameET = (EditText)findViewById(R.id.source_companyname);
        destination_companynameET = (EditText)findViewById(R.id.destination_companyname);
        transporternameET = (EditText)findViewById(R.id.transportername);
        brandnameET = (EditText)findViewById(R.id.brandname);
        serialnoET = (EditText)findViewById(R.id.serialno);
        barcodeET= (EditText)findViewById(R.id.barcode);
        remarksET= (EditText)findViewById(R.id.remarks);
        scanserialno = (EditText)findViewById(R.id.scanserialno);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        // button on click listener

        OnClickListener handler = new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.Button_location_from:

                        navigatetoLocationFromListActivity();
                        break;

                        case R.id.Button_location_to:
                            navigatetoLocationToListActivity();
                        break;

                    case R.id.Button_brandname:
                        navigatetoBrandListActivity();
                        break;


                    case R.id.Button_transportername:
                        navigatetoTransporterListActivity();
                        break;

                        case R.id.Button_serialno:
                        navigatetoModelListActivity();
                        break;
                }
            }
        };

        // our button
        Button_location_from = (Button) findViewById(R.id.Button_location_from);
        Button_location_from.setOnClickListener(handler);

        Button_location_to = (Button) findViewById(R.id.Button_location_to);
        Button_location_to.setOnClickListener(handler);

        Button_brandname = (Button) findViewById(R.id.Button_brandname);
        Button_brandname.setOnClickListener(handler);

        Button_transportername = (Button) findViewById(R.id.Button_transportername);
        Button_transportername.setOnClickListener(handler);

        Button_serialno = (Button) findViewById(R.id.Button_serialno);
        Button_serialno.setOnClickListener(handler);

        Button_scanserialno = (Button)findViewById(R.id.Button_scanserialno);
        Button_scanserialno.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                captureSerialNumber(0);
            }
        });

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickTransferDate = (Button) findViewById(R.id.pickTransferDate);
        pickTransferDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        systrfno=i.getStringExtra("systrfno");
        source_companycd=i.getStringExtra("source_companycd");
        destination_companycd=i.getStringExtra("destination_companycd");

        source_companyname=i.getStringExtra("source_companyname");
        destination_companyname=i.getStringExtra("destination_companyname");

        vtransferdt=i.getStringExtra("vtransferdt");
    }

    private void captureSerialNumber(int requestCode) {

        Intent homeIntent = new Intent(TransferStock.this, com.shrikantelectronics.BarcodeScanner.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                scanserialno.setText(scanContent);
                onClick_search_serialno();
            }
        }

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                source_companycd = bundle.getString("companycd");
                source_companynameET.setText(bundle.getString("companyname"));
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                destination_companycd = bundle.getString("companycd");
                destination_companynameET.setText(bundle.getString("companyname"));
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
                sysproductno = bundle.getString("sysproductno");
                serialnoET.setText(bundle.getString("serialno"));
                barcodeET.setText(bundle.getString("barcodeno"));
            }
        }

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                transportercd = bundle.getString("transportercd");
                transporternameET.setText(bundle.getString("transportername"));
            }
        }

    }

    public void navigatetoLocationFromListActivity(){
        Intent homeIntent = new Intent(TransferStock.this,StockVerificationLocation_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("trftype","FROM");
        startActivityForResult(homeIntent,1);
    }

    public void navigatetoLocationToListActivity(){
        Intent homeIntent = new Intent(TransferStock.this,StockVerificationLocation_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("trftype","TO");
        startActivityForResult(homeIntent,2);
    }

    public void navigatetoBrandListActivity(){
        Intent homeIntent = new Intent(TransferStock.this,StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,3);
    }

    public void navigatetoTransporterListActivity(){
        Intent homeIntent = new Intent(TransferStock.this,StockVerificationTransporter_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }

    public void navigatetoModelListActivity(){

        Intent homeIntent = new Intent(TransferStock.this,StockVerificationSerial_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysbrandno",sysbrandno);
        homeIntent.putExtra("source_companycd",source_companycd);
        startActivityForResult(homeIntent,4);
    }

    @SuppressWarnings("deprecation")
    public void setTransferDate(View view) {
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
                    TransferDateListener, year, month, day);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener TransferDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showTransferDate(arg1, arg2, arg3);
                }
            };

    private void showTransferDate(int year, int month, int day) {
        pickTransferDate = (Button) findViewById(R.id.pickTransferDate);
        pickTransferDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        //invokeWS_Customer_Ledger(destination_companycd);
    }

    public void onClick_search_serialno() {

        Map<String, String> paramsMap = new HashMap<>();
        serialnoET = (EditText) findViewById(R.id.serialno);
        String serialno;
        serialno = serialnoET.getText().toString();

        if (Utility.isNotNull(serialno) ) {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysemployeeno  = globalVariable.getsysemployeeno();

            paramsMap.put("serialnumber", "" +serialno);
            paramsMap.put("sysemployeeno", "0" +sysemployeeno);
           // invokeWS_Product_BySerial(paramsMap);

        }
    }

    public void invokeWS_Product_BySerial(Map<String, String> paramsMap){
        try {

            //  imageLoader = new ImageLoader(serial_view_single.this);

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSerialDetails_Employee", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;

                        JSONArray new_array = obj.getJSONArray("productsserial");

                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sysproductno=jsonObject.getString("sysproductno");
                        sysbrandno=jsonObject.getString("sysbrandno");
                        //sysmodelno=jsonObject.getString("sysmodelno");

                        serialnoET.setText(jsonObject.getString("serialno"));
                        //txtbarcodeno.setText(jsonObject.getString("barcodeno"));
                        brandnameET.setText(jsonObject.getString("brandname"));
                        //modelnameET.setText(jsonObject.getString("modelname"));
                        //unitrateET.setText(jsonObject.getString("vpp"));

                       // quantityET.setText("1");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onClick_CreateStockTransfer(View view) {

        // String orderamount = orderamountET.getText().toString();
        // String srefbillno = refbillnoET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        String transferno = "";

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(source_companycd) || Utility.isNotNull(destination_companycd) || Utility.isNotNull(sysproductno) || Utility.isNotNull(transportercd) ) {
            // When Email entered is Valid

            pickTransferDate = (Button) findViewById(R.id.pickTransferDate);
            String TransferDate = pickTransferDate.getText().toString();
            String remarks   = remarksET.getText().toString();

            if(systrfno == null)
            {
                systrfno="0";
            }
            paramsMap.put("systrfno","0" + systrfno);
            paramsMap.put("systrfdtlno", "0");
            paramsMap.put("destination_companycd", "0" +destination_companycd);
            paramsMap.put("transferno", "" + transferno);
            paramsMap.put("transferdt", "" + TransferDate);
            paramsMap.put("sysproductno", "0" +sysproductno);
            paramsMap.put("quantity", "1" );
            paramsMap.put("source_companycd", "0" +source_companycd);
            paramsMap.put("transportercd", "0" +transportercd);
            paramsMap.put("remarks", remarks);
            paramsMap.put("userid", userid);

            invokeCreateStockTransferWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeCreateStockTransferWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/CreateStockTransfer", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    tabletransferstock  = (TableLayout) findViewById(R.id.tabletransferstock);
                    tabletransferstock.removeAllViews();
                    tabletransferstock.setStretchAllColumns(true);
                    tabletransferstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(TransferStock.this);

                    TextView highsHeading_Serial= initPlainHeaderTextView();
                    highsHeading_Serial.setText("Serial No");
                    highsHeading_Serial.setWidth(300);
                    highsHeading_Serial.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Serial.setGravity(Gravity.CENTER);

                    TextView highsHeading_Sticker = initPlainHeaderTextView();
                    highsHeading_Sticker.setText("Sticker");
                    highsHeading_Sticker.setWidth(20);
                    highsHeading_Sticker.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Sticker.setGravity(Gravity.CENTER);

                    tblrowHeading.addView(highsHeading_Serial);
                    //tblrowHeading.addView(highsHeading_Sticker);

                    tabletransferstock.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("transferdtl");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            systrfno=jsonObject.getString("systrfno");
                            systrfdtlno=jsonObject.getString("systrfdtlno");
                            source_companycd=jsonObject.getString("source_companycd");
                            destination_companycd=jsonObject.getString("destination_companycd");
                            sysbrandno=jsonObject.getString("sysbrandno");
                            sysproductno=jsonObject.getString("sysproductno");
                            source_companyname=jsonObject.getString("source_companyname");
                            destination_companyname=jsonObject.getString("destination_companyname");

                            vtransferdt=jsonObject.getString("vtransferdt");
                            brandname=jsonObject.getString("brandname");
                            serialno=jsonObject.getString("serialno");
                            barcodeno=jsonObject.getString("barcodeno");

                            transportercd=jsonObject.getString("transportercd");
                            transportername=jsonObject.getString("transportername");

                            TableRow tblrowLabels = new TableRow(TransferStock.this);

                            final TextView highsLabel_systrfno = initPlainTextView(i);
                            final TextView highsLabel_systrfdtlno = initPlainTextView(i);
                            final TextView highsLabel_sysproductno = initPlainTextView(i);
                            final TextView highsLabel_serialno = initPlainTextView(i);
                            final TextView highsLabel_sysbrandno = initPlainTextView(i);
                            final TextView highsLabel_brandname = initPlainTextView(i);
                            final TextView highsLabel_barcodeno = initPlainTextView(i);

                            highsLabel_systrfno.setText(jsonObject.getString("systrfno"));
                            highsLabel_systrfdtlno.setText(jsonObject.getString("systrfdtlno"));
                            highsLabel_sysproductno.setText(jsonObject.getString("sysproductno"));
                            highsLabel_serialno.setText(jsonObject.getString("serialno"));
                            highsLabel_sysbrandno.setText(jsonObject.getString("sysbrandno"));
                            highsLabel_brandname.setText(jsonObject.getString("brandname"));
                            highsLabel_barcodeno.setText(jsonObject.getString("barcodeno"));

                            highsLabel_serialno.setText(jsonObject.getString("serialno"));
                            highsLabel_serialno.setWidth(300);
                            highsLabel_serialno.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialno.setGravity(Gravity.LEFT);

                            highsLabel_barcodeno.setText(jsonObject.getString("barcodeno"));
                            highsLabel_barcodeno.setWidth(20);
                            highsLabel_barcodeno.setTypeface(Typeface.DEFAULT);
                            highsLabel_barcodeno.setGravity(Gravity.CENTER);

                            tblrowLabels.addView(highsLabel_serialno);
                           // tblrowLabels.addView(highsLabel_barcodeno);

                            tblrowLabels.setClickable(true);

                            tabletransferstock.addView(tblrowLabels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    serialnoET.setText("");
                    barcodeET.setText("");
                    sysproductno="0";


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

    public void onClick_DeleteStockTransfer(View view) {
        // Get Email Edit View Value

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(systrfdtlno)) {
            // Put Http parameter userserialno with value of Email Edit View control
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            paramsMap.put("systrfno", "0"+systrfno);
            paramsMap.put("userid", "0"+userid);

            invokeDeleteStockTransferWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeDeleteStockTransferWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/DeleteStockTransfer", paramsMap, new ApiHelper.ApiCallback() {

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
                        Toast.makeText(getApplicationContext(), "Transfer Deleted Successfully...!", Toast.LENGTH_LONG).show();
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

        TextView textView = new TextView(TransferStock.this);
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
        TextView textView = new TextView(TransferStock.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(TransferStock.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}

