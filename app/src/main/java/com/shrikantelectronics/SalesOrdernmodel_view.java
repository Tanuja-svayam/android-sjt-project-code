package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SalesOrdernmodel_view extends Activity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] modelcode;
    String[] mrp;
    String[] dp;
    String[] stock;
   // int[] modelimage;

    String sysinvorderno ="0";
    String sysorderdtlno ="0";
    String custcd ="0";
    String custname ="0";

    String vinvoicedt ="0";
    String netinvoiceamt ="0";
    String invoiceno ="0";


    TextView consignor_custnameET ;


    ListViewAdapter_SalesOrder adapter;

    ProgressDialog prgDialog;
    private ListView lv;
    EditText inputSearch;

    ArrayList<SalesOrder_Models> arraylist = new ArrayList<SalesOrder_Models>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grnmodel_view);

        consignor_custnameET = (TextView)findViewById(R.id.name);
        //orderamountET = (EditText)findViewById(R.id.orderamount);

        Intent i = getIntent();

        sysorderdtlno ="0";
        sysinvorderno = i.getStringExtra("sysinvorderno");
        custcd = i.getStringExtra("custcd");
        custname= i.getStringExtra("custname");
        consignor_custnameET.setText(custname);
        vinvoicedt = i.getStringExtra("vinvoicedt");
        netinvoiceamt = i.getStringExtra("netinvoiceamt");
        invoiceno= i.getStringExtra("invoiceno");

        GetProductData();

    }

    public void GetProductData(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("modelcode", "");
        paramsMap.put("sysemployeeno", sysemployeeno);

        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetModelDetails", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("products");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            String imageurlpath = IMAGEURL + "ProductImage/" + jsonObject.getString("imageurl");
                            SalesOrder_Models wp = new SalesOrder_Models(sysinvorderno, sysorderdtlno, invoiceno, custcd, custname, vinvoicedt, netinvoiceamt, jsonObject.getString("sysmodelno"),jsonObject.getString("modelcode"), jsonObject.getString("mrp"), jsonObject.getString("dp"), jsonObject.getString("stock"), imageurlpath, jsonObject.getString("searchfield"));
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_SalesOrder(SalesOrdernmodel_view.this, arraylist);

                    // Binds the Adapter to the ListView
                    lv.setAdapter(adapter);

                    inputSearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            // When user changed the Text
                         //   home.this.adapter.getFilter().filter(cs);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                      int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                            adapter.filter(text);
                        }
                    });

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


