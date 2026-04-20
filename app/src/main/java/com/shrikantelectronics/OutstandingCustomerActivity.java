package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;

public class OutstandingCustomerActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    Button pickFromDate;
    Button pickToDate;

    ProgressDialog prgDialog;

    String name;
    int year, month, day;

    String locationname;
    String fromdate;
    String todate;

    TextView header_customername;
    TableLayout tablesalesregister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_outstanding_view);

        Intent i = getIntent();

        locationname = i.getStringExtra("locationname");
        fromdate = i.getStringExtra("fromdate");
        todate = i.getStringExtra("todate");

        invokeWS_CustomerOutstanding(locationname);
    }

    public void invokeWS_CustomerOutstanding(String locationname){
        try {

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("fromdate", fromdate);
            paramsMap.put("todate", todate);
            paramsMap.put("locationname", locationname);

            header_customername= (TextView) findViewById(R.id.header_customername);
            header_customername.setText(locationname + " Customer List");

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerOutstanding", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablecustomerledger);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(OutstandingCustomerActivity.this);

                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Age");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_custname = initPlainHeaderTextView();
                        highsHeading_custname.setText("Customer");
                        highsHeading_custname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_custname.setGravity(Gravity.LEFT);

                        TextView highsHeading_debit = initPlainHeaderTextView();
                        highsHeading_debit.setText("Debit");
                        highsHeading_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_debit.setGravity(Gravity.RIGHT);

                        TextView highsHeading_credit = initPlainHeaderTextView();
                        highsHeading_credit.setText("Credit");
                        highsHeading_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_credit.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_custname);
                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_debit);
                        tblrowHeading.addView(highsHeading_credit);

                        tablesalesregister.addView(tblrowHeading);

                        double fCredit;
                        double fDebit;

                        fCredit = 0;
                        fDebit = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customeroutstanding");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String dCustcd;
                                dCustcd = jsonObject.getString("custcd");

                                TableRow tblrowLabels = new TableRow(OutstandingCustomerActivity.this);

                                final TextView highsLabel_custname = initPlainTextView(i);
                                highsLabel_custname.setText(jsonObject.getString("custname"));
                                highsLabel_custname.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(OutstandingCustomerActivity.this, customer_view_single.class);
                                        intent.putExtra("custcd",dCustcd);
                                        startActivity(intent);


                                     //   Intent intent = new Intent(OutstandingCustomerActivity.this, Customer_Ledger_View.class);
                                     //   intent.putExtra("custcd",dCustcd);
                                      //  intent.putExtra("name",highsLabel_custname.getText());
                                      //  intent.putExtra("fromdate",fromdate);
                                      //  intent.putExtra("todate",todate);

                                        startActivity(intent);
                                    }
                                });
                                highsLabel_custname.setTypeface(Typeface.DEFAULT);
                                highsLabel_custname.setGravity(Gravity.LEFT);

                                TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("ageing"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.RIGHT);

                                TextView highsLabel_debit= initPlainTextView(i);
                                highsLabel_debit.setText(jsonObject.getString("DEBIT"));
                                highsLabel_debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_debit.setGravity(Gravity.RIGHT);

                                TextView highsLabel_credit = initPlainTextView(i);
                                highsLabel_credit.setText(jsonObject.getString("CREDIT"));
                                highsLabel_credit.setTypeface(Typeface.DEFAULT);
                                highsLabel_credit.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_custname);
                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_debit);
                                tblrowLabels.addView(highsLabel_credit);

                                tablesalesregister.addView(tblrowLabels);

                                fCredit += Double.valueOf(jsonObject.getString("CREDIT"));
                                fDebit += Double.valueOf(jsonObject.getString("DEBIT"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(OutstandingCustomerActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText("TOTAL");
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_debit = initPlainFooterTextView();
                        highsFooter_debit.setText(String.format("%.2f", fDebit));
                        highsFooter_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_debit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_credit = initPlainFooterTextView();
                        highsFooter_credit.setText(String.format("%.2f", fCredit));
                        highsFooter_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_credit.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_debit);
                        tblrowFooter.addView(highsFooter_credit);

                        tablesalesregister.addView(tblrowFooter);

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

        TextView textView = new TextView(OutstandingCustomerActivity.this);
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
        TextView textView = new TextView(OutstandingCustomerActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(OutstandingCustomerActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}


