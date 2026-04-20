package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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

public class StockVerificationPaymentMode_view extends Activity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] paymodename;

    ProgressDialog prgDialog;
    ListViewAdapter_StockVerification_PaymentMode adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> PaymentMode;

    String under;

    ArrayList<PaymentMode> arraylist = new ArrayList<PaymentMode>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmode_view);

        invokeWS_PaymentMode();
    }

    public void invokeWS_PaymentMode(){
        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("bankcd", "");
            paramsMap.put("sysuserno", sysuserno);

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/CashDetails", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("CashAccounts");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            PaymentMode wp = new PaymentMode(jsonObject.getString("bankcd"),jsonObject.getString("paymodename"));
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_StockVerification_PaymentMode(StockVerificationPaymentMode_view.this, arraylist);

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


