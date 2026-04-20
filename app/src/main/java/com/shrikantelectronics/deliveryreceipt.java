package com.shrikantelectronics;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.HashMap;
//import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.FileProvider;
import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.ByteArrayBody;

public class deliveryreceipt extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int STORAGE_PERMISSION_CODE = 101;
    private String pendingFileUrl;
    private String pendingFileName;
    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    public static String invorderno ;
    public static String sysorderdtlno, sysinvorderno, sysinvno ;

    String FileName;

    TableLayout tabletoplocationstock;

    ProgressDialog prgDialog;
    TextView errorMsg;

    TextView custnameET;
    TextView serialnoET ;
    TextView modelnameET;
    TextView brandnameET;
    TextView topcategorynameET;
    TextView invordernoET;

    //String sysorderdtlno;
    String deliverycharges;
    String custname;
    String serialno ;
    String modelname;
    String brandname;
    String topcategoryname;
    String sysmodelno;
    String companycd;

    Toolbar toolbar;
    private Button scanBtn;
    private Button btnCreateInvoice, btnShowInvoice;

    private EditText  contentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deliveryreceipt);
        Intent i = getIntent();


        scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureSerialNumber(0);
            }
        });

        contentTxt = (EditText)findViewById(R.id.scan_content);
        contentTxt.addTextChangedListener(new GenericTextWatcher_ProductSerial(contentTxt));

        sysinvorderno = i.getStringExtra("sysinvorderno");
        sysorderdtlno = i.getStringExtra("sysorderdtlno");
        custname = i.getStringExtra("custname");
        deliverycharges = i.getStringExtra("deliverycharges");
        serialno = i.getStringExtra("serialno");
        modelname = i.getStringExtra("modelname");
        brandname = i.getStringExtra("brandname");
        topcategoryname = i.getStringExtra("topcategoryname");
        invorderno = i.getStringExtra("invorderno");
        sysmodelno = i.getStringExtra("sysmodelno");
        companycd = i.getStringExtra("companycd");

         custnameET = (TextView) findViewById(R.id.custname);
         serialnoET= (TextView) findViewById(R.id.serialno);
         modelnameET= (TextView) findViewById(R.id.modelname);
         brandnameET= (TextView) findViewById(R.id.brandname);
         topcategorynameET= (TextView) findViewById(R.id.topcategoryname);
         invordernoET= (TextView) findViewById(R.id.invorderno);

        custnameET.setText(custname);
        serialnoET.setText(serialno);
        modelnameET.setText(modelname);
        brandnameET.setText(brandname);
        topcategorynameET.setText(topcategoryname);
        invordernoET.setText(invorderno);
        errorMsg = (TextView) findViewById(R.id.login_error);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        btnShowInvoice = (Button) findViewById(R.id.btnShowInvoice);
        btnCreateInvoice = (Button) findViewById(R.id.btnCreateInvoice);
        //btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        btnCreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateInvoice();
            }
        });

        if(serialno != null && !serialno.equals("") ){
            // replace btnYourButton with your button’s ID reference
            btnCreateInvoice.setVisibility(View.VISIBLE);
        } else {
            btnCreateInvoice.setVisibility(View.GONE); // optional: hide if not 0
        }

        invokeWS_SerialStock();
    }

    private class GenericTextWatcher_ProductSerial implements TextWatcher {

        private View view;
        private GenericTextWatcher_ProductSerial(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = contentTxt.getText();
            if (editableValue1.length() >= 4) {

                invokeWS_SerialStock();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                contentTxt.setText(scanContent);
            }
        }
    }

    public void invokeWS_SerialStock() {
        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String user_companycd  = globalVariable.getcompanycd();

            String editableValue1 = "" + contentTxt.getText().toString();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("modelcode", sysmodelno);
            paramsMap.put("companycd", user_companycd);
            paramsMap.put("serialno", editableValue1.toString());
            paramsMap.put("barcodeno", "");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            //          header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetModelDeliveryDetails_SerialNos", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.removeAllViews();

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(deliveryreceipt.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Location");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Serial");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Age");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj =  response;
                        JSONArray new_array = obj.getJSONArray("products");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(deliveryreceipt.this);

                                final String sysproductno = jsonObject.getString("sysproductno");
                                final String sysmodelno = jsonObject.getString("sysmodelno");

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("companyname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                final TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("balancestock"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);
                                highsLabel_Quantity.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)

                                    {
                                        String serialno = highsLabel_Quantity.getText().toString();

                                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                        final String consignor_companycd  = globalVariable.getcompanycd();
                                        final String sysemployeeno  = globalVariable.getsysemployeeno();
                                        final String userid  = globalVariable.getuserid();

                                        Map<String, String> paramsMap = new HashMap<>();

                                        // When Email Edit View and Password Edit View have values other than Null
                                        if (Utility.isNotNull(sysproductno)) {
                                            // When Email entered is Valid

                                            paramsMap.put("sysproductno", sysproductno);
                                            paramsMap.put("sysorderdtlno", sysorderdtlno);
                                            paramsMap.put("sysmodelno", sysmodelno);
                                            paramsMap.put("serialno", serialno);
                                            paramsMap.put("userid", userid);
                                            paramsMap.put("deliveryinstruction", "");

                                            invokeUpdateProductInOrderWS(paramsMap);

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Please select the serial number, don't leave any field blank", Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });


                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("delivery_pending"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Status code :"+ e.toString() +"errmsg : "+e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }


    public void invokeUpdateProductInOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/UpdateProductInSalesOrder", paramsMap ,new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj =  response;

                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                   String noof_pending_serial =obj.getString("noof_pending_serial");

                    // Convert to integer and check
                    if ("0".equals(noof_pending_serial.trim())) {
                        // replace btnYourButton with your button’s ID reference
                        btnCreateInvoice.setVisibility(View.VISIBLE);
                    } else {
                        btnCreateInvoice.setVisibility(View.GONE); // optional: hide if not 0
                    }

                   // invokeSearchSalesmen_SalesOrder_DetailWS();
                   // Intent intent = new Intent(deliveryreceipt.this, pendingdelivery_view.class);
                   // deliveryreceipt.this.setResult(pendingdelivery_view.RESULT_OK,intent);

                    //finish();

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

                prgDialog.hide();
            }
        });
    }

    public void submit_click(View view) {
        // Get Email Edit View Value

        String contentserialnumber = contentTxt.getText().toString();

        // Instantiate Http Request Param Object
        Map<String, String> paramsMap = new HashMap<>();
        // When Email Edit View and Password Edit View have values other than Null

        paramsMap.put("sysorderdtlno", sysorderdtlno);
        paramsMap.put("receivermobileno", "");
        paramsMap.put("receivername", "");
        paramsMap.put("delivery_sign_file_name", "");
        paramsMap.put("serialnumber", contentserialnumber);

        invokeWS(paramsMap);

    }

    public void invokeWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddDeliveryReceipt", paramsMap ,new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {

                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj =  response;
                    // When the JSON response has status boolean value assigned with true

                    //Toast.makeText(getApplicationContext(), "Delivery Updated successfully !", Toast.LENGTH_LONG).show();

                    if(obj.getBoolean("status")){
                        // Set Default Values for Edit View controls
                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Delivery Updated successfully !", Toast.LENGTH_LONG).show();
                        Intent homeIntent = new Intent(getApplicationContext(),pendingdelivery_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
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
    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

                prgDialog.hide();






            }
        });
    }

    public void setDefaultValues(){

        sysorderdtlno = "";
        deliverycharges ="";
        custname="";
        serialno ="";
        modelname ="";
        brandname="";
        topcategoryname ="";
        invorderno ="";
    }

    private void captureSerialNumber(int requestCode) {
//        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //      scanIntegrator.setRequestCode(requestCode);
        //    scanIntegrator.initiateScan( );

        Intent homeIntent = new Intent(deliveryreceipt.this, com.shrikantelectronics.BarcodeScanner.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestCode);

    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(deliveryreceipt.this);
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
        TextView textView = new TextView(deliveryreceipt.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(deliveryreceipt.this);
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

            if(sysinvorderno != null && !sysinvorderno.equals("0") )
            {
                paramsMap.put("syshdrno", sysinvorderno);
                paramsMap.put("userid", userid);
                paramsMap.put("authid", "AUTHORISE");
                paramsMap.put("authstatus", "50");
                paramsMap.put("sysauthno", "0");

                invokeCreateInvoiceWS(paramsMap);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please Add Product and Then Create Invoice ", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeCreateInvoiceWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Authorise_Order_Direct_Invoice", paramsMap ,new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    String sysinvno_new = obj.getString("sysinvno");
                     sysinvno = obj.getString("sysinvno");
                    sysinvno = obj.getString("sysinvno");
                    if (Integer.valueOf(sysinvno_new) > 0)
                    {

                        btnShowInvoice.setVisibility(View.VISIBLE);

                        btnCreateInvoice.setVisibility(View.GONE);

                        Map<String, String> paramsMap = new HashMap<>();

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        FileName = "Invoice" + timeStamp + ".pdf";

                        paramsMap.put("sysinvno",sysinvno_new);
                        paramsMap.put("FileName",FileName);

                        invokeGenerateSalesInvoiceWS(paramsMap);
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
    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

                prgDialog.hide();

            }
        });
    }
    private void invokeGenerateSalesInvoiceWS(Map<String, String> paramsMap) {
        ProgressDialog prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Generating PDF, please wait...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        ApiHelper.post(URL + "Service1.asmx/GenerateInvoicePDF", paramsMap, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.dismiss();
                try {
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String fileName = "Invoice_" + timestamp + ".pdf";
                    String fileUrl = Config.WEBSERVICE_URL + "MobileInvoice/" + response.getString("error_msg");

                    Utility.downloadAndOpenPdf(deliveryreceipt.this,fileUrl, fileName);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Invalid server response", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void download_invoice(View v)
    {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvno) && !sysinvno.equals("0") ) {
            // When Email entered is Valid

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileName = "Invoice" + timeStamp + ".pdf";

            paramsMap.put("sysinvno",sysinvno);
            paramsMap.put("FileName",FileName);

            invokeGenerateSalesInvoiceWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }



}