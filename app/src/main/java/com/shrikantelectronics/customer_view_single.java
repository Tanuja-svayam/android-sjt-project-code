package com.shrikantelectronics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;





import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;

import android.app.ListActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;

public class customer_view_single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TextView txtcustcd;
    TextView txtname;
    TextView txtmobile;
    TextView txttelephone;
    TextView txtemailid;
    TextView txtoutstanding;
    TextView txtaddress;
    TextView txtdeliveryaddress;

    String custcd1;
    String name1;
    String mobile1;
    String telephone;
    String emailid1;
    String outstanding1;
    String address1;
    String deliveryaddress;
    String vinvoicedt1;
    String netinvoiceamt1;

    ListViewAdapter_Customer_Product adapter;
    private ListView lv_customer_Product ;
    ArrayList<Customer_Product> arraylist = new ArrayList<Customer_Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_single);

        Intent i = getIntent();
        custcd1 = i.getStringExtra("custcd");
        name1= i.getStringExtra("name");
        // Locate the TextViews in singleitemview.xml
        txtname = (TextView) findViewById(R.id.name);
        txtmobile = (TextView) findViewById(R.id.mobile);
        txttelephone = (TextView) findViewById(R.id.telephone);
        txtemailid = (TextView) findViewById(R.id.emailid);
       // txtoutstanding = (TextView) findViewById(R.id.outstanding);
        txtaddress = (TextView) findViewById(R.id.address);
        txtdeliveryaddress = (TextView) findViewById(R.id.deliveryaddress);

        txtmobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mobile;
                mobile= txtmobile.getText().toString();
                CallDialer(mobile);
            }
        });

        txttelephone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mobile;
                mobile= txttelephone.getText().toString();
                CallDialer(mobile);
            }
        });

        invokeWS_Customer_Details(custcd1);
        GetCustomer_ProductData(custcd1);

    }

    public void CallDialer(String mobile) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));

        startActivity(intent);
    }

    public void CallSms() {

        invokeSendOutstandingSMWS();

        /*
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"+ mobile1));
        sendIntent.putExtra("sms_body", "" );
        startActivity(sendIntent);
*/

    }

    public void CustomerGiftCardLedger(){
        Intent customerIntent = new Intent(customer_view_single.this,Customer_Giftcard_Ledger_View.class);

        String name;
        name= txtname.getText().toString();

        customerIntent.putExtra("custcd",custcd1);
        customerIntent.putExtra("name",name);
        customerIntent.putExtra("fromdate","");
        customerIntent.putExtra("todate","");

        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void CustomerLedger(){
        Intent customerIntent = new Intent(customer_view_single.this,Customer_Ledger_View.class);

        String name;
        name= txtname.getText().toString();

        customerIntent.putExtra("custcd",custcd1);
        customerIntent.putExtra("name",name);
        customerIntent.putExtra("fromdate","");
        customerIntent.putExtra("todate","");

        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }


    public void CallEmail(View view) {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailid1});
        email.putExtra(Intent.EXTRA_SUBJECT, "Outstanding");
        email.putExtra(Intent.EXTRA_TEXT, "" );
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_custaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_wa) {
            CallSms();
        } else if (id == R.id.action_ledger) {
            CustomerLedger();
        } else if (id == R.id.action_giftcardledger) {
            CustomerGiftCardLedger();
        }
        return super.onOptionsItemSelected(item);
    }



    public void GetCustomer_ProductData(String custcd1){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("custcd", custcd1);

            invokeWS_Customer_Product(paramsMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeWS_Customer_Product(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerProductDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customer");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                Customer_Product wp = new Customer_Product(jsonObject.getString("sysinvno"),jsonObject.getString("custcd"), jsonObject.getString("invoiceno"), jsonObject.getString("invoicedt"), jsonObject.getString("brandname"), jsonObject.getString("modelname"),jsonObject.getString("topcategoryname"), jsonObject.getString("parentcategoryname"), jsonObject.getString("categoryname"), jsonObject.getString("serialno"), jsonObject.getString("quantity"), jsonObject.getString("netamt"), jsonObject.getString("manufacturer_warranty_tenure"), jsonObject.getString("main_manufacturer_warranty_tenure"), jsonObject.getString("vwarranty_start_date"), jsonObject.getString("vwarranty_end_date"), jsonObject.getString("sysorderdtlno"), jsonObject.getString("sysbrandno") , jsonObject.getString("sysproductcategoryno_top"), jsonObject.getString("sysmodelno"));

                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lv_customer_Product  = (ListView) findViewById(R.id.list_view );

                        adapter = new ListViewAdapter_Customer_Product(customer_view_single.this, arraylist);

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

    public void invokeWS_Customer_Details(String custcd){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("custcd", custcd);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerDetailsSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        txtname.setText(jsonObject.getString("name"));
                        txtmobile.setText(jsonObject.getString("contactpersonmobile"));
                        txttelephone.setText(jsonObject.getString("telephone"));
                        txtemailid.setText(jsonObject.getString("emailid"));
                      //  txtoutstanding.setText(jsonObject.getString("custbalance"));
                        txtaddress.setText(jsonObject.getString("address"));
                        txtdeliveryaddress.setText(jsonObject.getString("deliveryaddress"));
                        mobile1 =    jsonObject.getString("contactpersonmobile");

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

    public void invokeSendOutstandingSMWS(){
        // Show Progress Dialog
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("custcd", custcd1);

        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SendOutstandingSMSToCustomer", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {

                try {
                    JSONObject obj = response;

                    //
                    // When the JSON response has status boolean value assigned with true
                    //
                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                    }
                    // Else display error message
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
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

            }
        });
    }


}
