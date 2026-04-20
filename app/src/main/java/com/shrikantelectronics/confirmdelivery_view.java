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
import java.util.HashMap;
import java.util.Locale;

public class confirmdelivery_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] modelcode;
    String[] mrp;
    String[] dp;
    String[] stock;

    ListViewAdapter_ConfirmDelivery adapter;

    ProgressDialog prgDialog;
    private ListView lv;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;

    ArrayList<PendingDelivery> arraylist = new ArrayList<PendingDelivery>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingdelivery_view);

       GetProductData();
    }

    public void GetProductData(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("companycd", companycd);
        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetDeliveryConfirmPendingList", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("pendingdelivery");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            PendingDelivery wp = new PendingDelivery(
                                    jsonObject.getString("sysorderdtlno"),
                                    jsonObject.getString("vinvorderdt"),
                                    jsonObject.getString("invorderno"),
                                    jsonObject.getString("custname"),
                                    jsonObject.getString("custaddress"),
                                    jsonObject.getString("custdeliveryaddress"),
                                    jsonObject.getString("customermobileno"),
                                    jsonObject.getString("custtelephone"),
                                    jsonObject.getString("custemailid"),
                                    jsonObject.getString("topcategoryname"),
                                    jsonObject.getString("parentcategoryname"),
                                    jsonObject.getString("categoryname"),
                                    jsonObject.getString("brandname"),
                                    jsonObject.getString("modelname"),
                                    jsonObject.getString("serialno"),
                                    jsonObject.getString("picklocationname"),
                                    jsonObject.getString("vexpected_delivery_date"),
                                    jsonObject.getString("delorderno"),
                                    jsonObject.getString("transportcharges"),
                                    jsonObject.getString("deliveryinstruction"),
                                    jsonObject.getString("delivery_status"),
                                    jsonObject.getString("seachfield"),
                                    jsonObject.getString("sysmodelno"),
                                    jsonObject.getString("sysinvorderno"),
                                    jsonObject.getString("companycd")

                            );
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_ConfirmDelivery(confirmdelivery_view.this, arraylist);

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
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }

        });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

