package com.shrikantelectronics;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class supplier_view_single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TextView txtsysvendorno;
    TextView txtname;
    TextView txtmobile;
    TextView txtemailid;
    TextView txtoutstanding;
    TextView txtaddress;
    TextView txtdeliveryaddress;

    String sysvendorno1;
    String name1;
    String mobile1;
    String emailid1;
    String outstanding1;
    String address1;
    String deliveryaddress;
    String vinvoicedt1;
    String netinvoiceamt1;

    ListViewAdapter_Supplier_Product adapter;
    private ListView lv_customer_Product ;
    ArrayList<Supplier_Product> arraylist = new ArrayList<Supplier_Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_view_single);

        Intent i = getIntent();
        sysvendorno1 = i.getStringExtra("sysvendorno");
        name1= i.getStringExtra("name");
        // Locate the TextViews in singleitemview.xml
        txtname = (TextView) findViewById(R.id.name);
        txtmobile = (TextView) findViewById(R.id.mobile);
        txtemailid = (TextView) findViewById(R.id.emailid);
        txtoutstanding = (TextView) findViewById(R.id.outstanding);
        txtaddress = (TextView) findViewById(R.id.address);
        txtdeliveryaddress = (TextView) findViewById(R.id.deliveryaddress);

        //GetSupplier_ProductData(sysvendorno1);
        invokeWS_Supplier_Details(sysvendorno1);
    }

    public void CallDialer(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile1));

        startActivity(intent);
    }

    public void CallSms(View view) {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"+ mobile1));
        sendIntent.putExtra("sms_body", "Outstanding of Rs. " +  outstanding1 + " is due from " + vinvoicedt1 + ". Kindly make the payment at earliest. Please ignore if already paid." );
        startActivity(sendIntent);
    }


    public void CallEmail(View view) {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailid1});
        email.putExtra(Intent.EXTRA_SUBJECT, "Outstanding");
        email.putExtra(Intent.EXTRA_TEXT, "Outstanding of Rs. " +  outstanding1 + " is due from " + vinvoicedt1 + ". Kindly make the payment at earliest. Please ignore if already paid." );
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }


    public void SupplierLedger(View view){
        Intent customerIntent = new Intent(supplier_view_single.this,Supplier_Ledger_View.class);

        customerIntent.putExtra("sysvendorno",sysvendorno1);
        customerIntent.putExtra("name",name1);
        customerIntent.putExtra("fromdate","");
        customerIntent.putExtra("todate","");


        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void GetSupplier_ProductData(String sysvendorno1){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysvendorno", sysvendorno1);

            invokeWS_Supplier_Product(paramsMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeWS_Supplier_Product(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSupplierProductDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("supplier");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                Supplier_Product wp = new Supplier_Product(jsonObject.getString("sysvendorno"), jsonObject.getString("invoiceno"), jsonObject.getString("invoicedt"), jsonObject.getString("brandname"), jsonObject.getString("modelname"),jsonObject.getString("parentcategoryname"), jsonObject.getString("categoryname"), jsonObject.getString("serialno"), jsonObject.getString("quantity"), jsonObject.getString("netamt"), jsonObject.getString("manufacturer_warranty_tenure"), jsonObject.getString("main_manufacturer_warranty_tenure"), jsonObject.getString("vwarranty_start_date"), jsonObject.getString("vwarranty_end_date"));

                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lv_customer_Product  = (ListView) findViewById(R.id.list_view );

                        adapter = new ListViewAdapter_Supplier_Product(supplier_view_single.this, arraylist);

                        lv_customer_Product.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

                                    }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void invokeWS_Supplier_Details(String sysvendorno){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysvendorno", sysvendorno);


            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSupplierDetailsSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("suppliersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        txtname.setText(jsonObject.getString("name"));
                        txtmobile.setText(jsonObject.getString("contactpersonmobile"));
                        txtemailid.setText(jsonObject.getString("emailid"));
                        txtoutstanding.setText(jsonObject.getString("supplierbalance"));
                        txtaddress.setText(jsonObject.getString("address"));
                       // txtdeliveryaddress.setText(jsonObject.getString("deliveryaddress"));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

                                    }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
