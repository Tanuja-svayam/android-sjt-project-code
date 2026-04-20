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
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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


public class OrderDeliveryPending_view_single extends AppCompatActivity {

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

    TextView txtsysinvorderno;
    TextView txtcustname;
    TextView txtnetinvoiceamt;
    TextView txtvinvoicedt;
    TextView txtinvoiceno;

    TextView txtdbd_paid_by_customer;
    TextView txtdbd_amount;
    TextView txtapproved_by_manager;
    TextView txtgross_slc_amount;
    TextView txtremarks;

    String sysinvorderno;
    String custname;
    String netinvoiceamt;
    String vinvoicedt;
    String invoiceno;

    String dbd_paid_by_customer;
    String dbd_amount;
    String approved_by_manager;
    String gross_slc_amount;
    String remarks;

    private Button add_Approved;
    String invoicenofilename;

    private Button btnViewInvoice;
    private ProgressDialog pDialog;
    String FileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdeliverypending_view_single);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent i = getIntent();

        sysinvorderno = i.getStringExtra("sysinvorderno");
        custname = i.getStringExtra("custname");
        netinvoiceamt = i.getStringExtra("netinvoiceamt");
        vinvoicedt = i.getStringExtra("vinvoicedt");
        invoiceno = i.getStringExtra("invoiceno");

        dbd_paid_by_customer = i.getStringExtra("dbd_paid_by_customer");
        dbd_amount = i.getStringExtra("dbd_amount");
        approved_by_manager = i.getStringExtra("approved_by_manager");
        gross_slc_amount = i.getStringExtra("gross_slc_amount");
        remarks= i.getStringExtra("remarks");

        // Locate the TextViews in singleitemview.xml
        txtcustname = (TextView) findViewById(R.id.custname);
        txtnetinvoiceamt = (TextView) findViewById(R.id.netinvoiceamt);
        txtvinvoicedt = (TextView) findViewById(R.id.vinvoicedt);
        txtinvoiceno = (TextView) findViewById(R.id.invoiceno);

        txtdbd_paid_by_customer= (TextView) findViewById(R.id.dbd_paid_by_customer);
        txtdbd_amount= (TextView) findViewById(R.id.dbd_amount);
      //  txtapproved_by_manager= (TextView) findViewById(R.id.approved_by_manager);
        txtgross_slc_amount= (TextView) findViewById(R.id.gross_slc_amount);
        txtremarks= (TextView) findViewById(R.id.remarks);

        txtcustname.setText(custname);
        txtnetinvoiceamt.setText(netinvoiceamt);
        txtvinvoicedt.setText(vinvoicedt);
        txtinvoiceno.setText(invoiceno);

        txtdbd_paid_by_customer.setText(dbd_paid_by_customer);
        txtdbd_amount.setText(dbd_amount);
      //  txtapproved_by_manager.setText(approved_by_manager);
        txtgross_slc_amount.setText(gross_slc_amount);
        txtremarks.setText(remarks);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        invokeWS_Order_Model_List(sysinvorderno);
    }

    public void invokeApproveSalesOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Authorise_Sales_Order_Manager_Approval", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    String sysinvorderno_new = obj.getString("sysinvorderno");

                    if (Integer.valueOf(sysinvorderno_new) > 0)
                    {

                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }

                    finish();
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





    public void invokeWS_Order_Model_List(String dsysinvorderno) {

        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysinvorderno", dsysinvorderno);

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

                        TableRow tblrowHeading = new TableRow(OrderDeliveryPending_view_single.this);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Model");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsHeading_serialnno = initPlainHeaderTextView();
                        highsHeading_serialnno.setText("EDD");
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

                                TableRow tblrowLabels = new TableRow(OrderDeliveryPending_view_single.this);

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

                                TextView highsLabel_OMount = initPlainTextView(i);
                                highsLabel_OMount.setText(jsonObject.getString("netamt"));
                                highsLabel_OMount.setTypeface(Typeface.DEFAULT);
                                highsLabel_OMount.setGravity(Gravity.RIGHT);

                                TextView highsLabel_serialnno = initPlainTextView(i);
                                highsLabel_serialnno.setText(jsonObject.getString("vexpected_delivery_date"));
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

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(OrderDeliveryPending_view_single.this);
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
        TextView textView = new TextView(OrderDeliveryPending_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(OrderDeliveryPending_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


    public void download(View v)
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
            FileName = "Order" + timeStamp + ".pdf";

            paramsMap.put("sysinvorderno",sysinvorderno);
            paramsMap.put("FileName",FileName);

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
        ApiHelper.post(URL + "Service1.asmx/GenerateOrderPDF", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    try {
                        String fileUrl = Config.WEBSERVICE_URL + "MobileOrder/" + response.getString("error_msg");
                        Utility.downloadAndOpenPdf(OrderDeliveryPending_view_single.this, fileUrl,FileName);
                        finish();
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(OrderDeliveryPending_view_single.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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




}
