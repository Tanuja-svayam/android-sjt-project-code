package com.shrikantelectronics;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;

import android.app.ListActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class model_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;
    ListViewAdapter adapter;
    private ListView lv;
    EditText inputSearch;

    ArrayList<Models> arraylist = new ArrayList<>();

    protected static final int RESULT_SPEECH = 1;
    public ArrayList<String> text;

    public static TextToSpeech toSpeech;
    public int result;

    String lastSearch = "";   // 🔑 Track last query

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_view);

        inputSearch = (EditText)findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new GenericTextWatcher_Product());
    }

    private class GenericTextWatcher_Product implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString().trim();

            // If less than 3 chars → reset
            if (text.length() < 3) {
                if (adapter != null) {
                    arraylist.clear();
                    adapter.notifyDataSetChanged();
                }
                lastSearch = ""; // reset so new API can trigger later
                return;
            }

            // Update adapter's search query for highlighting
            if (adapter != null) {
                adapter.setSearchQuery(text);
            }

            // If different search term (first 3+ chars changed) → call API
            if (!text.equalsIgnoreCase(lastSearch)) {
                lastSearch = text;
                GetProductData();
            }
            // Else filter locally
            else if (adapter != null) {
                adapter.filter(text.toLowerCase(Locale.getDefault()));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_model, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnQrScanner) {
            try {
                captureSerialNumber(0);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support QR Scanner",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        if (id == R.id.action_New) {
            navigatetoModelCreateActivity();
        }

        if (id == R.id.btnSpeak) {
            try {
                startActivityForResult(intent, RESULT_SPEECH);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void captureSerialNumber(int requestCode) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setRequestCode(requestCode);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                inputSearch.setText(scanContent);
                GetProductData();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && data != null) {
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getApplicationContext(), "Feature not supported in your device", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("voice", text.get(0));
                    Toast.makeText(model_view.this,text.get(0), Toast.LENGTH_LONG).show();
                    inputSearch.setText(text.get(0).replace(" ",""));
                }
            }
            else {
                toSpeech.speak("Sorry I could not hear you",TextToSpeech.QUEUE_FLUSH,null);
            }
        }
    }

    public void navigatetoModelCreateActivity(){
        Intent homeIntent = new Intent(model_view.this,Procurement_Model_Create.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,99);
    }

    public void GetProductData(){
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();

        Editable editableValue1 = inputSearch.getText();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("modelcode", "" +editableValue1);
        paramsMap.put("sysemployeeno", sysemployeeno);

        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {
            ApiHelper.post(URL + "Service1.asmx/GetModelDetails_Employee", paramsMap, new ApiHelper.ApiCallback()   {

                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");

                        arraylist.clear(); // clear old results

                        for (int i = 0; i < new_array.length(); i++) {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            String imageurlpath = IMAGEURL + "ProductImage/" + jsonObject.getString("imageurl");

                            Models wp = new Models(
                                    jsonObject.getString("sysmodelno"),
                                    jsonObject.getString("modelcode"),
                                    jsonObject.getString("sysproductcategoryno_top"),
                                    jsonObject.getString("sysproductcategoryno_parent"),
                                    jsonObject.getString("sysproductcategoryno"),
                                    jsonObject.getString("topcategoryname"),
                                    jsonObject.getString("parentcategoryname"),
                                    jsonObject.getString("categoryname"),
                                    imageurlpath,
                                    jsonObject.getString("searchfield"),
                                    jsonObject.getString("stock_stk"),
                                    jsonObject.getString("stock_bkg"),
                                    jsonObject.getString("stock_avl"),
                                    jsonObject.getString("mrp"),
                                    jsonObject.getString("dp"),
                                    jsonObject.getString("slc")
                            );
                            arraylist.add(wp);
                        }

                        lv = (ListView) findViewById(R.id.list_view);

                        if (adapter == null) {
                            adapter = new ListViewAdapter(model_view.this, arraylist);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),
                                "Error Occurred [Invalid JSON]!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
