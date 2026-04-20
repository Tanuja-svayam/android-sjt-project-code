package com.shrikantelectronics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class customer_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;
    Context mContext;
    String[] custcd;
    String[] name;
    String[] address;
    String[] mobile;
    String[] emailid;
    String[] outstanding;
    String[] invoiceno;
    String[] vinvoicedt;
    String[] netinvoiceamt;

    String calledFrom;
    EditText inputSearch_customerET ;
    TableLayout tablesalesregister;

    ProgressDialog prgDialog;
    private ListView lv_customer ;
    EditText inputSearch_customer ;
    ArrayList<HashMap<String, String>> customerlist;
    ListViewAdapter_Customer adapter;
    ArrayList<FA_Ledger> arraylist = new ArrayList<FA_Ledger>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);
       // GetCustomerData();


        Intent i = getIntent();
        calledFrom = i.getStringExtra("calledfrom");


        inputSearch_customerET = (EditText)findViewById(R.id.inputSearch_customer);
        inputSearch_customerET.addTextChangedListener(new customer_view.GenericTextWatcher_Mobile(inputSearch_customerET));


    }


    private class GenericTextWatcher_Mobile implements TextWatcher {

        private View view;
        private GenericTextWatcher_Mobile(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = inputSearch_customerET.getText();
            if (editableValue1.length() >= 4) {
                GetCustomerData();
            }
        }
    }

    public void GetCustomerData(){
        try {

            Editable editableValue1 = inputSearch_customerET.getText();
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("custcd", "" +editableValue1);
            invokeWS_Customer(paramsMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeWS_Customer(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
                        tablesalesregister.removeAllViews();

                      //  tablesalesregister.setStretchAllColumns(true);
                      /// tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(customer_view.this);

                        TextView highsHeading_custname = initPlainHeaderTextView();
                        highsHeading_custname.setText("Customer");
                        highsHeading_custname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_custname.setGravity(Gravity.LEFT);

                        TextView highsHeading_contactpersonmobile = initPlainHeaderTextView();
                        highsHeading_contactpersonmobile.setText("Mobile");
                        highsHeading_contactpersonmobile.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_contactpersonmobile.setGravity(Gravity.CENTER);


                        tblrowHeading.addView(highsHeading_custname);
                        tblrowHeading.addView(highsHeading_contactpersonmobile);

                        tablesalesregister.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customer");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String customer_custcd;
                                final String walkin_custcd;
                                final String syscustactno;

                                final String custname;
                                final String companycd;
                                final String locationname;

                                customer_custcd=jsonObject.getString("custcd");
                                custname=jsonObject.getString("name");

                                TableRow tblrowLabels = new TableRow(customer_view.this);

                                TextView highsLabel_custname = initPlainTextView(i);
                                highsLabel_custname.setText(jsonObject.getString("name"));
                                highsLabel_custname.setTypeface(Typeface.DEFAULT);
                                highsLabel_custname.setGravity(Gravity.LEFT);

                                if(calledFrom.equals("LEDGER")) {
                                    highsLabel_custname.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(customer_view.this, customer_view_single.class);

                                            intent.putExtra("custcd", customer_custcd);
                                            intent.putExtra("name", custname);
                                            startActivity(intent);
                                        }
                                    });
                                }

                                if(calledFrom.equals("SALESORDER")) {

                                    highsLabel_custname.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(customer_view.this, Salesmen_SalesOrder.class);

                                            intent.putExtra("custcd", customer_custcd);
                                            intent.putExtra("name", custname);

                                            setResult(customer_view.RESULT_OK,intent);
                                            finish();

                                        }
                                    });
                                }

                                TextView highsLabel_contactpersonmobile = initPlainTextView(i);
                                highsLabel_contactpersonmobile.setText(jsonObject.getString("mobile"));
                                highsLabel_contactpersonmobile.setTypeface(Typeface.DEFAULT);
                                highsLabel_contactpersonmobile.setGravity(Gravity.CENTER);

                                tblrowLabels.addView(highsLabel_custname);
                                tblrowLabels.addView(highsLabel_contactpersonmobile);

                                // tblrowLabels.addView(highsLabel_Quantity);
                                //tblrowLabels.addView(highslabel_productvalue);

                                tablesalesregister.addView(tblrowLabels);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //    LinearLayout sv = new LinearLayout(MainActivity.this);

                        //        sv.addView(table);

                        //hsw.addView(sv);
                        //setContentView(hsw);

                        // setContentView(table);

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

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(customer_view.this);
        textView.setPadding(10, 10, 10, 10);


        if((n%2)==0)
        {
            textView.setBackgroundResource(R.drawable.cell_shape);
        }
        else
        {
            textView.setBackgroundResource(R.drawable.cell_shape_oddrow);
        }


        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    private TextView initPlainHeaderTextView() {
        TextView textView = new TextView(customer_view.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(customer_view.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}


