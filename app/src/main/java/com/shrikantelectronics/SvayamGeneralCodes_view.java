package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class SvayamGeneralCodes_view extends Activity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] GeneralCodesname;

    ProgressDialog prgDialog;
    ListViewAdapter_GeneralCodes adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> GeneralCodes;


    String under, gctype = "";

    ArrayList<GeneralCodes> arraylist = new ArrayList<GeneralCodes>();

    @Override
    public void onCreate(Bundle savedInstanceGeneralCodes) {
        super.onCreate(savedInstanceGeneralCodes);
        setContentView(R.layout.activity_customer_type_view);
        Intent i = getIntent();

        gctype = i.getStringExtra("gctype");

        invokeWS_CustomerType();
    }

    public void invokeWS_CustomerType(){
        try {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

            final String userid  = globalVariable.getuserid();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("gctype", gctype);
            paramsMap.put("userid", userid);
            paramsMap.put("svayam_clientid", GlobalClass.SVAYAM_CLIENTID);
            paramsMap.put("svayam_subscriptionid", GlobalClass.SVAYAM_SUBSCRIPTIONID);

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetGeneralCodeList_ForSvayam", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("generalcodes");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            GeneralCodes wp = new GeneralCodes(jsonObject.getString("DropDownValue"),jsonObject.getString("DropDownText"));
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_GeneralCodes(SvayamGeneralCodes_view.this, arraylist);

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


