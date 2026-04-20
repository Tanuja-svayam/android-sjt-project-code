package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import java.util.ArrayList;
import java.util.Locale;

public class TransferSerial_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] modelcode;
    String[] mrp;
    String[] dp;
    String[] stock;
   // int[] modelimage;


    ProgressDialog prgDialog;
    ListViewAdapter_StockTransferReceipt adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;
    EditText inputSearch_serialET ;

    ArrayList<Serial_Stock_Transfer> arraylist = new ArrayList<Serial_Stock_Transfer>();

   EditText inputsearch_serial;
    // ArrayList<HashMap<String, String>> productList;

    String sysbrandno,source_companycd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockverificationserial_view);

        inputSearch_serialET = (EditText)findViewById(R.id.inputsearch_serial);

        GetProductData();

    }

    public void GetProductData(){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();

        Editable editableValue1 = inputSearch_serialET.getText();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("serialnumber", "" +editableValue1);
        paramsMap.put("companycd", "0" +companycd);

        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetPendingTransferSerialDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                Serial_Stock_Transfer wp = new Serial_Stock_Transfer(jsonObject.getString("sysproductno"),jsonObject.getString("serialno"),jsonObject.getString("barcodeno"),jsonObject.getString("modelname"),jsonObject.getString("brandname"),jsonObject.getString("stocklocation"),jsonObject.getString("sysmodelno"),jsonObject.getString("sysbrandno"),jsonObject.getString("searchserial"),jsonObject.getString("systrfdtlno"),jsonObject.getString("systrfno"));
                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lv = (ListView) findViewById(R.id.list_view);
                        inputsearch_serial = (EditText) findViewById(R.id.inputsearch_serial);

                        adapter = new ListViewAdapter_StockTransferReceipt(TransferSerial_view.this, arraylist);

                        // Binds the Adapter to the ListView
                        lv.setAdapter(adapter);

                        inputsearch_serial.addTextChangedListener(new TextWatcher() {

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
                                String text = inputsearch_serial.getText().toString().toLowerCase(Locale.getDefault());
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


