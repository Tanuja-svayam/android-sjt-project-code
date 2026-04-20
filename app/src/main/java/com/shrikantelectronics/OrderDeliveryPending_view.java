package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class OrderDeliveryPending_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] custname;
    String[] netinvoiceamt;
    String[] vinvoicedt;
    String[] invoiceno;
   // int[] invoiceimage;

    ListViewAdapter_OrderDeliveryPending adapter;

    ProgressDialog prgDialog;
    private ListView lv;
    EditText inputSearch;

    ArrayList<OrderDeliveryPending> arraylist = new ArrayList<OrderDeliveryPending>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        GetProductData();
    }

    public void GetProductData(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();
        final String approved_by_manager  = globalVariable.getsysemployeeno();
        final String sysemployeeno  = globalVariable.getsysemployeeno();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("companycd", companycd);
        paramsMap.put("sysemployeeno", "0");

        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetOrderPendingForDeliveryDetails", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("deliverypending");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            OrderDeliveryPending wp = new OrderDeliveryPending(jsonObject.getString("sysinvorderno"),jsonObject.getString("custname"), jsonObject.getString("netinvoiceamt"), jsonObject.getString("vinvoicedt"), jsonObject.getString("invoiceno"), jsonObject.getString("searchfield"), jsonObject.getString("dbd_paid_by_customer"), jsonObject.getString("dbd_amount"), jsonObject.getString("approved_by_manager"), jsonObject.getString("gross_slc_amount"), jsonObject.getString("remarks"));
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_OrderDeliveryPending(OrderDeliveryPending_view.this, arraylist);

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


