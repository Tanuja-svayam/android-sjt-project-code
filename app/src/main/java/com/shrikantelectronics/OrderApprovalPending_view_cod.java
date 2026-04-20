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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


public class OrderApprovalPending_view_cod extends AppCompatActivity {

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

    TextView txtapproved_by_manager;
    TextView txtgross_slc_amount;

    TextView txtremarks;

    String sysinvorderno;
    String sysreceiptno;
    String custname;
    String netinvoiceamt;
    String vinvoicedt;
    String invoiceno;

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
        setContentView(R.layout.activity_orderapprovalpending_view_cod);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent i = getIntent();

        sysreceiptno = i.getStringExtra("sysreceiptno");
        custname = i.getStringExtra("custname");
        netinvoiceamt = i.getStringExtra("netinvoiceamt");
        vinvoicedt = i.getStringExtra("vinvoicedt");
        invoiceno = i.getStringExtra("invoiceno");

        approved_by_manager = i.getStringExtra("approved_by_manager");
        gross_slc_amount = i.getStringExtra("gross_slc_amount");
        remarks= i.getStringExtra("remarks");
        sysinvorderno= i.getStringExtra("sysinvorderno");

        // Locate the TextViews in singleitemview.xml
        txtcustname = (TextView) findViewById(R.id.custname);
        txtnetinvoiceamt = (TextView) findViewById(R.id.netinvoiceamt);
        txtvinvoicedt = (TextView) findViewById(R.id.vinvoicedt);
        txtinvoiceno = (TextView) findViewById(R.id.invoiceno);
        txtgross_slc_amount = (TextView) findViewById(R.id.gross_slc_amount);


        txtremarks= (TextView) findViewById(R.id.remarks);

        txtcustname.setText(custname);
        txtnetinvoiceamt.setText(netinvoiceamt);
        txtvinvoicedt.setText(vinvoicedt);
        txtinvoiceno.setText(invoiceno);
        txtremarks.setText(remarks);

               prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        add_Approved = (Button) findViewById(R.id.add_Approved);
        add_Approved.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ApprovedSalesOrder();
            }
        });


    }

    public void ApprovedSalesOrder() {

        //String orderamount = orderamountET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(userid)) {
            // When Email entered is Valid

            paramsMap.put("syshdrno", sysreceiptno);
            paramsMap.put("userid", userid);

            invokeApproveSalesOrderWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeApproveSalesOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/mobile_Authorise_Credit", paramsMap, new ApiHelper.ApiCallback() {

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
                finish();
                Intent homeIntent = new Intent(OrderApprovalPending_view_cod.this, OrderApprovalPending_view.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
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
                                finish();
                Intent homeIntent = new Intent(OrderApprovalPending_view_cod.this, OrderApprovalPending_view.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(OrderApprovalPending_view_cod.this);
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
        TextView textView = new TextView(OrderApprovalPending_view_cod.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(OrderApprovalPending_view_cod.this);
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
                        Utility.downloadAndOpenPdf(OrderApprovalPending_view_cod.this, fileUrl,FileName);
                        finish();
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(OrderApprovalPending_view_cod.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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
