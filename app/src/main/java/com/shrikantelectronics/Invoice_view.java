package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

public class Invoice_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] custname;
    String[] netinvoiceamt;
    String[] vinvoicedt;
    String[] invoiceno;
   // int[] invoiceimage;

    ListViewAdapter_Invoice adapter;

    ProgressDialog prgDialog;
    private ListView lv;
    EditText inputSearch;

    ArrayList<Invoice> arraylist = new ArrayList<Invoice>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_view);

        inputSearch = (EditText)findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new Invoice_view.GenericTextWatcher_ProductSerial(inputSearch));

        //GetProductData();

    }

    private class GenericTextWatcher_ProductSerial implements TextWatcher {

        private View view;
        private GenericTextWatcher_ProductSerial(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
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

        Editable editableValue1 = inputSearch.getText();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String sysuserno  = globalVariable.getSysuserno();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysuserno", sysuserno);
        paramsMap.put("search", "" +editableValue1);

        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetinvoiceDetails", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("products");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            Invoice wp = new Invoice( jsonObject.getString("sysinvno") ,jsonObject.getString("custname"), jsonObject.getString("netinvoiceamt"), jsonObject.getString("vinvoicedt"), jsonObject.getString("invoiceno"), jsonObject.getString("searchfield"), jsonObject.getString("sysinvorderno") );
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_Invoice(Invoice_view.this, arraylist);

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
                // prgDialog.hide();
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
