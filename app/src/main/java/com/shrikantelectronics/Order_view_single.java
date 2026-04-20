package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Order_view_single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    public ImageLoader imageLoader;

    private ListView lv_model_location;

    ArrayList<HashMap<String, String>> customerlist;
    ListViewAdapter_Model_Location adapter;
    ArrayList<Model_Location> arraylist = new ArrayList<Model_Location>();

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;

    TextView txtsysinvno;
    TextView txtcustname;
    TextView txtnetinvoiceamt;
    TextView txtvinvoicedt;
    TextView txtinvoiceno;

    String sysinvno;
    String custname;
    String netinvoiceamt;
    String vinvoicedt;
    String invoiceno;

    private Button btnCreateInvoice;
    String invoicenofilename;

    private Button btnViewInvoice;
    private ProgressDialog pDialog;
    String FileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view_single);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent i = getIntent();

        sysinvno = i.getStringExtra("sysinvno");
        custname = i.getStringExtra("custname");
        netinvoiceamt = i.getStringExtra("netinvoiceamt");
        vinvoicedt = i.getStringExtra("vinvoicedt");
        invoiceno = i.getStringExtra("invoiceno");

        // Locate the TextViews in singleitemview.xml
        txtcustname = (TextView) findViewById(R.id.custname);
        txtnetinvoiceamt = (TextView) findViewById(R.id.netinvoiceamt);
        txtvinvoicedt = (TextView) findViewById(R.id.vinvoicedt);
        txtinvoiceno = (TextView) findViewById(R.id.invoiceno);

        txtcustname.setText(custname);
        txtnetinvoiceamt.setText(netinvoiceamt);
        txtvinvoicedt.setText(vinvoicedt);
        txtinvoiceno.setText(invoiceno);

        btnCreateInvoice = (Button) findViewById(R.id.btnCreateInvoice);
        //btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        btnCreateInvoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CreateInvoice();
            }
        });

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        invokeWS_Order_Model_List(sysinvno);

    }

    public void onClick_add_product(View view) {
        Intent homeIntent = new Intent(Order_view_single.this,SalesOrdernmodel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //refbillnoET= (EditText)findViewById(R.id.refbillno);

        //refbillno = refbillnoET.getText().toString();

        homeIntent.putExtra("sysinvorderno",sysinvno);
        homeIntent.putExtra("custcd","0");
        homeIntent.putExtra("custname",custname);
        homeIntent.putExtra("vinvoicedt",vinvoicedt);
        homeIntent.putExtra("netinvoiceamt",netinvoiceamt);
        homeIntent.putExtra("invoiceno",invoiceno);

        startActivity(homeIntent);
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

            paramsMap.put("syshdrno", sysinvno);
            paramsMap.put("userid", userid);
            paramsMap.put("authid", "AUTHORISE");
            paramsMap.put("authstatus", "50");
            paramsMap.put("sysauthno", "0");

            invokeCreateInvoiceWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeCreateInvoiceWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Authorise_Order_Direct_Invoice", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    String sysinvno_new = obj.getString("sysinvno");

                    if (Integer.valueOf(sysinvno_new) > 0)
                    {
                        Map<String, String> paramsMap = new HashMap<>();


                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        FileName = "Invoice" + timeStamp + ".pdf";

                        paramsMap.put("sysinvno",sysinvno_new);
                        paramsMap.put("FileName",FileName);

                        invokeCreateSalesInvoiceWS(paramsMap);
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




    public void invokeCreateSalesInvoiceWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GenerateInvoicePDF", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;



                    try{
                        String fileUrl = Config.WEBSERVICE_URL + "MobileInvoice/" + response.getString("error_msg");
                        Utility.downloadAndOpenPdf(Order_view_single.this, fileUrl,FileName);

                    }catch(ActivityNotFoundException e){
                        Toast.makeText(Order_view_single.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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


    public void invokeWS_Order_Model_List(String sysinvorderno) {

        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysinvorderno", sysinvorderno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            //          header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetModelOrderDetails_Models", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();
                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Order_view_single.this);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Model");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsHeading_serialnno = initPlainHeaderTextView();
                        highsHeading_serialnno.setText("Serial No");
                        highsHeading_serialnno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_serialnno.setGravity(Gravity.CENTER);

                        TextView highsHeading_OMount = initPlainHeaderTextView();
                        highsHeading_OMount.setText("Amount");
                        highsHeading_OMount.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_OMount.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsHeading_serialnno);
                        tblrowHeading.addView(highsHeading_OMount);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Order_view_single.this);

                                final  String  sysinvorderno_temp;
                                final  String  sysorderdtlno_temp;
                                final  String  sysmodelno_temp;
                                final  String  modelcode_temp;
                                final  String  refbillno_temp;
                                final  String  custcd_temp;

                                sysinvorderno_temp = jsonObject.getString("sysinvorderno");
                                sysorderdtlno_temp = jsonObject.getString("sysorderdtlno");
                                sysmodelno_temp = jsonObject.getString("sysmodelno");
                                modelcode_temp = jsonObject.getString("modelname");

                                refbillno_temp = jsonObject.getString("refbillno");
                                custcd_temp = jsonObject.getString("custcd");

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("modelname"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.LEFT);
                                highsLabel_Quantity.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(Order_view_single.this, SalesOrder_add_model.class);

                                        intent.putExtra("sysinvorderno",sysinvorderno_temp);
                                        intent.putExtra("sysorderdtlno",sysorderdtlno_temp);
                                        intent.putExtra("refbillno",refbillno_temp);
                                        intent.putExtra("custcd",custcd_temp);
                                        intent.putExtra("custname",custname);
                                        intent.putExtra("sysmodelno",sysmodelno_temp);
                                        intent.putExtra("modelcode",modelcode_temp);
                                        intent.putExtra("vinvoicedt",vinvoicedt);
                                        intent.putExtra("netinvoiceamt",netinvoiceamt);

                                        startActivity(intent);

                                       }
                                });

                                TextView highsLabel_OMount = initPlainTextView(i);
                                highsLabel_OMount.setText(jsonObject.getString("netamt"));
                                highsLabel_OMount.setTypeface(Typeface.DEFAULT);
                                highsLabel_OMount.setGravity(Gravity.RIGHT);

                                TextView highsLabel_serialnno = initPlainTextView(i);
                                highsLabel_serialnno.setText(jsonObject.getString("serialno"));
                                highsLabel_serialnno.setTypeface(Typeface.DEFAULT);
                                highsLabel_serialnno.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highsLabel_serialnno);
                                tblrowLabels.addView(highsLabel_OMount);

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

    public void download(View v)
    {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvno)) {
            // When Email entered is Valid

            paramsMap.put("sysinvorderno",sysinvno);

            invokeCreateSalesOrderWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    public void invokeCreateSalesOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GenerateInvoicePDF", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;


                    try{
                        String fileUrl = Config.WEBSERVICE_URL + "MobileInvoice/" + response.getString("error_msg");
                        Utility.downloadAndOpenPdf(Order_view_single.this, fileUrl,FileName);

                    }catch(ActivityNotFoundException e){
                        Toast.makeText(Order_view_single.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Order_view_single.this);
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
        TextView textView = new TextView(Order_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Order_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


}
