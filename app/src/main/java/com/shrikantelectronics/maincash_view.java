package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class maincash_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String legdertype;
    String[] bankcd;
    String[] name;
    String[] address;
    String[] mobile;
    String[] emailid;
    String[] outstanding;
    String[] invoiceno;
    String[] vinvoicedt;
    String[] netinvoiceamt;

    ProgressDialog prgDialog;
    private ListView lv_maincash ;
    EditText inputSearch_maincash ;
    ArrayList<HashMap<String, String>> maincashlist;
    ListViewAdapter_Maincash adapter;
    ArrayList<Maincash> arraylist = new ArrayList<Maincash>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincash_view);

        Intent i = getIntent();
        legdertype = i.getStringExtra("legdertype");

       GetMaincashData();
    }

    public void GetMaincashData(){
        try {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bankcd", "0");
        paramsMap.put("legdertype", legdertype);

       invokeWS_Maincash(paramsMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeWS_Maincash(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetMainCashDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("maincash");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                Maincash wp = new Maincash(jsonObject.getString("name"), jsonObject.getString("outstanding"), jsonObject.getString("bankcd"),legdertype);
                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lv_maincash  = (ListView) findViewById(R.id.list_view );
                        inputSearch_maincash  = (EditText) findViewById(R.id.inputSearch_maincash);

                        adapter = new ListViewAdapter_Maincash(maincash_view.this, arraylist);

                        lv_maincash.setAdapter(adapter);

                        inputSearch_maincash.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                          int arg3) {

                            }

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                 String text = inputSearch_maincash.getText().toString().toLowerCase(Locale.getDefault());
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


