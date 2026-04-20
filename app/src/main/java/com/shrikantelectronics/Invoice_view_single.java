package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;





import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Invoice_view_single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    public ImageLoader imageLoader;

    private ListView lv_model_location;

    ArrayList<HashMap<String, String>> customerlist;
    ListViewAdapter_Model_Location adapter;
    ArrayList<Model_Location> arraylist = new ArrayList<Model_Location>();

    TextView txtsysinvno;
    TextView txtcustname;
    TextView txtnetinvoiceamt;
    TextView txtvinvoicedt;
    TextView txtinvoiceno;

    String sysinvno;
    String sysinvorderno;
    String custname;
    String netinvoiceamt;
    String vinvoicedt;
    String invoiceno;

    String invoicenofilename;
    String purpose;
    Button btnReopen;

    private Button btnViewInvoice;
    private ProgressDialog pDialog;
    String FileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_view_single);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent i = getIntent();

        sysinvno = i.getStringExtra("sysinvno");
        sysinvorderno= i.getStringExtra("sysinvorderno");
        custname = i.getStringExtra("custname");
        netinvoiceamt = i.getStringExtra("netinvoiceamt");
        vinvoicedt = i.getStringExtra("vinvoicedt");
        invoiceno = i.getStringExtra("invoiceno");
        purpose= i.getStringExtra("PURPOSE");

                // Locate the TextViews in singleitemview.xml
        txtcustname = (TextView) findViewById(R.id.custname);
        txtnetinvoiceamt = (TextView) findViewById(R.id.netinvoiceamt);
        txtvinvoicedt = (TextView) findViewById(R.id.vinvoicedt);
        txtinvoiceno = (TextView) findViewById(R.id.invoiceno);

        btnReopen= (Button) findViewById(R.id.btnReopen);
        btnReopen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ReopenInvoice();
            }
        });

        txtcustname.setText(custname);
        txtnetinvoiceamt.setText(netinvoiceamt);
        txtvinvoicedt.setText(vinvoicedt);
        txtinvoiceno.setText(invoiceno);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        if (purpose.equals("DOWNLOAD"))
        {
            download_from_ledger();
        }

    }

    public void download_from_ledger()
    {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvno)) {
            // When Email entered is Valid


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileName = "Invoice" + timeStamp + ".pdf";

            paramsMap.put("sysinvno",sysinvno);
            paramsMap.put("FileName",FileName);

            invokeCreateSalesInvoiceWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
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


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileName = "Invoice" + timeStamp + ".pdf";

            paramsMap.put("sysinvno",sysinvno);
            paramsMap.put("FileName",FileName);

            invokeCreateSalesInvoiceWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
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

                    try {
                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        String fileName = "Invoice_" + timestamp + ".pdf";
                        String fileUrl = Config.WEBSERVICE_URL + "MobileInvoice/" + response.getString("error_msg");

                        Utility.downloadAndOpenPdf(Invoice_view_single.this, fileUrl,FileName);
                        finish();
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(Invoice_view_single.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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


    public void ReopenInvoice() {

        //String orderamount = orderamountET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(userid)) {
            // When Email entered is Valid , String userid

            paramsMap.put("sysinvorderno", sysinvorderno);
            paramsMap.put("userid", userid);
            invokeReopenSalesInvoiceWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeReopenSalesInvoiceWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Reopen_Invoice", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    sysinvno = obj.getString("sysinvno");
                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    finish();
                    Intent homeIntent = new Intent(Invoice_view_single.this, Invoice_view.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                                finish();
                Intent homeIntent = new Intent(Invoice_view_single.this, Invoice_view.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
    }

}
