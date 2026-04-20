package com.shrikantelectronics;

import android.app.Activity;
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
import java.util.HashMap;
import java.util.Locale;

public class supplier_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] sysvendorno;
    String[] name;
    String[] address;
    String[] mobile;
    String[] emailid;
    String[] outstanding;
    String[] invoiceno;
    String[] vinvoicedt;
    String[] netinvoiceamt;

    ProgressDialog prgDialog;
    private ListView lv_Supplier ;
    EditText inputSearch_Supplier ;
    ArrayList<HashMap<String, String>> Supplierlist;
    ListViewAdapter_Supplier adapter;
    ArrayList<Supplier> arraylist = new ArrayList<Supplier>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_view);
        GetSupplierData();
    }

    public void GetSupplierData(){
        try {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysvendorno", "0");

       invokeWS_Supplier(paramsMap);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void invokeWS_Supplier(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSupplierDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("Supplier");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                Supplier wp = new Supplier(jsonObject.getString("name"), jsonObject.getString("address"), jsonObject.getString("mobile"), jsonObject.getString("emailid") , jsonObject.getString("outstanding"), jsonObject.getString("invoiceno"), jsonObject.getString("vinvoicedt"), jsonObject.getString("netinvoiceamt"),jsonObject.getString("sysvendorno"));
                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lv_Supplier  = (ListView) findViewById(R.id.list_view );
                        inputSearch_Supplier  = (EditText) findViewById(R.id.inputSearch_Supplier);

                        adapter = new ListViewAdapter_Supplier(supplier_view.this, arraylist);

                        lv_Supplier.setAdapter(adapter);

                        inputSearch_Supplier.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                          int arg3) {

                            }

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                 String text = inputSearch_Supplier.getText().toString().toLowerCase(Locale.getDefault());
                                 adapter.filter(text);
                            }
                        });

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


