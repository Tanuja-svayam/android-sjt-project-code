package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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

public class Quotation_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] custname;
    String[] netinvoiceamt;
    String[] vinvorderdt;
    String[] invorderno;
   // int[] invoiceimage;

    ListViewAdapter_Quotation adapter;

    ProgressDialog prgDialog;
    private ListView lv;
    EditText inputSearch;

    ArrayList<Quotation> arraylist = new ArrayList<Quotation>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_view);

        GetProductData();
    }

    public void GetProductData(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String sysuserno  = globalVariable.getSysuserno();
        final String groupcode = globalVariable.getgroupcode();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysuserno", sysuserno);

        if (groupcode.equals("SAG")) {
            paramsMap.put("sysemployeeno", "0");
        }
        else
        {
            paramsMap.put("sysemployeeno", "0"+sysemployeeno);
        }
        invokeWS_Product(paramsMap);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_quotation, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_add_quotation) {
            navigatetoCreateQuotationActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    public void navigatetoCreateQuotationActivity(){
        Intent homeIntent = new Intent(Quotation_view.this,Salesmen_SalesQuotation.class);

        homeIntent.putExtra("sysinvorderno","0");
        homeIntent.putExtra("walkin_custcd","0");
        homeIntent.putExtra("customer_custcd","0");
        homeIntent.putExtra("syscustactno","0" + "0");

        homeIntent.putExtra("custname","");
        homeIntent.putExtra("companycd","0");
        homeIntent.putExtra("companyname","");

        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSalesmen_QuotationDetails", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("quotation");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            Quotation wp = new Quotation(jsonObject.getString("sysinvorderno"),jsonObject.getString("custname"), jsonObject.getString("netinvoiceamt"), jsonObject.getString("vinvorderdt"), jsonObject.getString("invorderno"), jsonObject.getString("searchfield"));
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_Quotation(Quotation_view.this, arraylist);

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


