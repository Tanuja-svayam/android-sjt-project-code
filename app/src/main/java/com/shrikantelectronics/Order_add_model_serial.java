package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class Order_add_model_serial extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    public ImageLoader imageLoader;

    private ListView lv_model_location ;

    ArrayList<HashMap<String, String>> customerlist;
    ListViewAdapter_Model_Location adapter;
    ArrayList<Model_Location> arraylist = new ArrayList<Model_Location>();

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;
    TextView txtmodelcode;

    String sysinvorderno ="0";
    String sysorderdtlno ="0";
    String custcd ="0";
    String refbillno ="";
    String sysproduct_procurmentno ="0";
    String custname = "";
    String netinvoiceamt;
    String invoicedt;
    String invoiceno;

    String sysmodelno1 ="0";
    String modelcode1 ="";

    EditText scan_content;
    ProgressDialog prgDialog;

    private Button scanBtn;
    private EditText contentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn_model_serial_view);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();

        sysinvorderno = i.getStringExtra("sysinvorderno");
        sysorderdtlno = i.getStringExtra("sysorderdtlno");
        custcd = i.getStringExtra("custcd");
        custname= i.getStringExtra("custname");
        refbillno = i.getStringExtra("refbillno");
        sysmodelno1 = i.getStringExtra("sysmodelno");
        modelcode1 = i.getStringExtra("modelcode");
        invoicedt = i.getStringExtra("invoicedt");
        netinvoiceamt = i.getStringExtra("netinvoiceamt");

        // Locate the TextViews in singleitemview.xml
        txtmodelcode = (TextView) findViewById(R.id.modelcode1);
        txtmodelcode.setText(modelcode1);

        scanBtn = (Button)findViewById(R.id.scan_button);
        contentTxt = (EditText)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureSerialNumber();
            }
        });

      //  invokeWS_GRN_Model_SerialStock(sysinvorderno, sysorderdtlno, sysmodelno1,sysproduct_procurmentno);
        invokeWS_SerialStock(sysmodelno1);
    }

    public void invokeWS_SerialStock(String sysmodelno) {

        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String companycd  = globalVariable.getcompanycd();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("modelcode", sysmodelno);
            paramsMap.put("companycd", companycd);


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

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Order_add_model_serial.this);
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

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Order_add_model_serial.this);

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
                    // Hide Progress Dialog
                    //     prgDialog.hide();
                    // When Http response code is '404'
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    // When Http response code is '500'


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
        ApiHelper.post(URL + "Service1.asmx/UpdateProductInSalesOrder", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                    Intent homeIntent = new Intent(Order_add_model_serial.this,Order_view_single.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    homeIntent.putExtra("sysinvno",sysinvorderno);
                    homeIntent.putExtra("custname",custname);
                    homeIntent.putExtra("vinvoicedt",invoicedt);
                    homeIntent.putExtra("netinvoiceamt",netinvoiceamt);
                    homeIntent.putExtra("invoiceno",refbillno);
                    finish();
                    startActivity(homeIntent);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image


        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            //formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText(scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void captureSerialNumber() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }


    public void onClick_add_product(View view) {
        Intent homeIntent = new Intent(Order_add_model_serial.this,SalesOrdernmodel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        homeIntent.putExtra("sysinvorderno",sysinvorderno);
        homeIntent.putExtra("custcd",custcd);
        homeIntent.putExtra("name",custname);
        homeIntent.putExtra("refbillno",refbillno);

        startActivity(homeIntent);
    }

    public void onClick_Back(View view) {
        Intent homeIntent = new Intent(Order_add_model_serial.this,SalesmenCreateCustomer.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        homeIntent.putExtra("sysinvorderno",sysinvorderno);
        homeIntent.putExtra("custcd",custcd);
        homeIntent.putExtra("name",custname);
        homeIntent.putExtra("invoiceno",refbillno);

        startActivity(homeIntent);
    }

    public void onClick_submit(View view) {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();
        String sserialno;

        scan_content = (EditText) findViewById(R.id.scan_content);

        sserialno = scan_content.getText().toString();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sserialno)) {
            // When Email entered is Valid

            paramsMap.put("sysinvorderno", "" + sysinvorderno);
            paramsMap.put("sysorderdtlno", "" + sysorderdtlno);
            paramsMap.put("custcd", "" + custcd);
            paramsMap.put("refbillno", "" + refbillno);
            paramsMap.put("sysmodelno1", "" + sysmodelno1);
            paramsMap.put("serialno", "" + sserialno);
            paramsMap.put("companycd", "" + consignor_companycd);
            paramsMap.put("userid","" +  userid);
            paramsMap.put("sysproduct_procurmentno", "" +sysproduct_procurmentno);

            invokeCreatePurchaseOrderWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeCreatePurchaseOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/CreatePurchaseOrder", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    JSONArray new_array = obj.getJSONArray("procurementdtl");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                    sysinvorderno = jsonObject.getString("sysinvorderno");
                    sysorderdtlno= jsonObject.getString("sysorderdtlno");
                    custcd= jsonObject.getString("custcd");
                    refbillno= jsonObject.getString("refbillno");
                    sysmodelno1= jsonObject.getString("sysmodelno");
                    sysproduct_procurmentno= jsonObject.getString("sysproduct_procurmentno");
                    setDefaultValues();
                    invokeWS_GRN_Model_SerialStock(sysinvorderno, sysorderdtlno, sysmodelno1, sysproduct_procurmentno);

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
        scan_content.setText("");
        sysproduct_procurmentno = "0";

    }

    public void invokeWS_GRN_Model_SerialStock(String sysinvorderno, String sysorderdtlno, String sysmodelno, String sysproduct_procurmentno) {

        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysinvorderno", sysinvorderno);
            paramsMap.put("sysorderdtlno", sysorderdtlno);
            paramsMap.put("sysmodelno", sysmodelno);
            paramsMap.put("sysproduct_procurmentno", sysproduct_procurmentno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
  //          header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetModelGRNDetails_SerialNos", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();
                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Order_add_model_serial.this);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Serial");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_Quantity);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Order_add_model_serial.this);

                                final TextView highsLabel_category = initPlainTextView(i);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("serialno"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_Quantity);

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
                    // Hide Progress Dialog
                    //     prgDialog.hide();
                    // When Http response code is '404'
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    // When Http response code is '500'


                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }


    }



    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Order_add_model_serial.this);
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
        TextView textView = new TextView(Order_add_model_serial.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Order_add_model_serial.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


}
