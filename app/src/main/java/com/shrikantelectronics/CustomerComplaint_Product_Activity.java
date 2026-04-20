package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import android.widget.ListView;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class CustomerComplaint_Product_Activity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    String syscustactno;
    String sysorderdtlno;
    String custcd ;

    EditText inputSearch_customerET ;

    ArrayList<Customer_Product> arraylist = new ArrayList<Customer_Product>();

    ListViewAdapter_Customer_Product_For_Complaint adapter;
    private ListView lv_customer_Product ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customercomplain_product);


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        custcd = i.getStringExtra("custcd");


       prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("custcd", "0"+custcd);

        invokeWS_Customer_Product(paramsMap);

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
                                Customer_Product wp = new Customer_Product(jsonObject.getString("sysinvno"), jsonObject.getString("custcd"), jsonObject.getString("invoiceno"), jsonObject.getString("invoicedt"), jsonObject.getString("brandname"), jsonObject.getString("modelname"),jsonObject.getString("topcategoryname"), jsonObject.getString("parentcategoryname"), jsonObject.getString("categoryname"), jsonObject.getString("serialno"), jsonObject.getString("quantity"), jsonObject.getString("netamt"), jsonObject.getString("manufacturer_warranty_tenure"), jsonObject.getString("main_manufacturer_warranty_tenure"), jsonObject.getString("vwarranty_start_date"), jsonObject.getString("vwarranty_end_date"), jsonObject.getString("sysorderdtlno"), jsonObject.getString("sysbrandno") , jsonObject.getString("sysproductcategoryno_top"), jsonObject.getString("sysmodelno"));

                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        lv_customer_Product  = (ListView) findViewById(R.id.list_view );

                        adapter = new ListViewAdapter_Customer_Product_For_Complaint(CustomerComplaint_Product_Activity.this, arraylist);

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
}