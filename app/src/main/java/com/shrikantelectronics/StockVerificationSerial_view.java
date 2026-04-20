package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class StockVerificationSerial_view extends Activity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] modelcode;
    String[] mrp;
    String[] dp;
    String[] stock;
   // int[] modelimage;

    ProgressDialog prgDialog;
    ListViewAdapter_StockVerification_Serial adapter;
    private ListView lv;
    EditText inputSearch;
   // ArrayList<HashMap<String, String>> productList;

    String sysbrandno,source_companycd;

    ArrayList<Serial_Stock_Verification> arraylist = new ArrayList<Serial_Stock_Verification>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_transfer_view);

        Intent i = getIntent();

        sysbrandno = i.getStringExtra("sysbrandno");
        source_companycd= i.getStringExtra("source_companycd");

        inputSearch = findViewById(R.id.inputSearch);
        lv = findViewById(R.id.list_view);
        adapter = new ListViewAdapter_StockVerification_Serial(StockVerificationSerial_view.this, arraylist);
        lv.setAdapter(adapter); // Set adapter initially


        if (sysbrandno.equals("0")) {
            inputSearch.addTextChangedListener(new GenericTextWatcher_ProductSerial(inputSearch));
        } else {
            GetProductData();
        }

        // Add filter TextWatcher only once
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text); // Custom filter inside your adapter
            }
        });



    }

    private class GenericTextWatcher_ProductSerial implements TextWatcher {



        private View view;
        private GenericTextWatcher_ProductSerial(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = inputSearch.getText();
            if (editableValue1.length() >= 3) {
                GetProductData();
            }
        }
    }

    public void GetProductData(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();

        Editable editableValue1 = inputSearch.getText();


        if (arraylist != null) {
            arraylist.clear();
        }

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("serialno", "" +editableValue1.toString());
        paramsMap.put("source_companycd", source_companycd);
        paramsMap.put("sysbrandno", "0" + sysbrandno);

        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

           //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSerialDetails_Brandwise", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("products");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            Serial_Stock_Verification wp = new Serial_Stock_Verification(jsonObject.getString("sysproductno"),jsonObject.getString("serialno"),jsonObject.getString("barcodeno"),jsonObject.getString("modelname"),jsonObject.getString("brandname"),jsonObject.getString("stocklocation"),jsonObject.getString("sysmodelno"),jsonObject.getString("sysbrandno"),jsonObject.getString("searchserial"));
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_StockVerification_Serial(StockVerificationSerial_view.this, arraylist);

                    // Binds the Adapter to the ListView
                    lv.setAdapter(adapter);



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
           //     prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }

        });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}


