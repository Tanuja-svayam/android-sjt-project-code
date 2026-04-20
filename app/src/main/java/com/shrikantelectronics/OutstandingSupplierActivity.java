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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class OutstandingSupplierActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    Button pickFromDate;
    Button pickToDate;

    ProgressDialog prgDialog;

    String vendorname,fromdate,todate;
    int year, month, day;

    TextView header_customername;
    TableLayout tablesalesregister;
String type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ledger_view);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        header_customername= (TextView) findViewById(R.id.header_customername);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,1));

        pickToDate = (Button) findViewById(R.id.pickToDate);
        pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        Intent i = getIntent();
        type = i.getStringExtra("type");

        if (type.equals("GROUP")){
            invokeWS_SupplierGroupOutstanding();
        }
        else {
            vendorname = i.getStringExtra("vendorname");
            fromdate = i.getStringExtra("fromdate");
            todate = i.getStringExtra("todate");
            invokeWS_SupplierOutstanding();
        }

    }


    public void navigatetoCustomerOutstanding(){
        Intent homeIntent = new Intent(OutstandingSupplierActivity.this,OutstandingCustomerActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoSupplierOutstanding(){
        Intent homeIntent = new Intent(OutstandingSupplierActivity.this,OutstandingSupplierActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void invokeWS_SupplierGroupOutstanding(){
        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();
            final String sysuserno = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();
            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("locationname", "");

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSupplierGroupOutstanding", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablecustomerledger);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(OutstandingSupplierActivity.this);

                        TextView highsHeading_vendorname = initPlainHeaderTextView();
                        highsHeading_vendorname.setText("Supplier");
                        highsHeading_vendorname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_vendorname.setGravity(Gravity.LEFT);

                        TextView highsHeading_debit = initPlainHeaderTextView();
                        highsHeading_debit.setText("Debit");
                        highsHeading_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_debit.setGravity(Gravity.RIGHT);

                        TextView highsHeading_credit = initPlainHeaderTextView();
                        highsHeading_credit.setText("Credit");
                        highsHeading_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_credit.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_vendorname);
                        tblrowHeading.addView(highsHeading_debit);
                        tblrowHeading.addView(highsHeading_credit);

                        tablesalesregister.addView(tblrowHeading);

                        double fCredit;
                        double fDebit;

                        fCredit = 0;
                        fDebit = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("supplieroutstanding");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String dCustcd;
                                dCustcd = jsonObject.getString("sysvendorno");

                                TableRow tblrowLabels = new TableRow(OutstandingSupplierActivity.this);

                                final TextView highsLabel_vendorname = initPlainTextView(i);
                                highsLabel_vendorname.setText(jsonObject.getString("vendorname"));
                                highsLabel_vendorname.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(OutstandingSupplierActivity.this, OutstandingSupplierActivity.class);
                                        intent.putExtra("type","VENDOR");
                                        intent.putExtra("fromdate",pickFromDate.getText().toString());
                                        intent.putExtra("todate",pickToDate.getText().toString());
                                        intent.putExtra("vendorname",highsLabel_vendorname.getText());
                                        startActivity(intent);
                                    }
                                });

                                highsLabel_vendorname.setTypeface(Typeface.DEFAULT);
                                highsLabel_vendorname.setGravity(Gravity.LEFT);

                                TextView highsLabel_debit= initPlainTextView(i);
                                highsLabel_debit.setText(jsonObject.getString("DEBIT"));
                                highsLabel_debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_debit.setGravity(Gravity.RIGHT);

                                TextView highsLabel_credit = initPlainTextView(i);
                                highsLabel_credit.setText(jsonObject.getString("CREDIT"));
                                highsLabel_credit.setTypeface(Typeface.DEFAULT);
                                highsLabel_credit.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_vendorname);
                                tblrowLabels.addView(highsLabel_debit);
                                tblrowLabels.addView(highsLabel_credit);

                                tablesalesregister.addView(tblrowLabels);

                                fCredit += Double.valueOf(jsonObject.getString("CREDIT"));
                                fDebit += Double.valueOf(jsonObject.getString("DEBIT"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(OutstandingSupplierActivity.this);
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

                       // tblrowFooter.addView(highsFooter_category);
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

    public void invokeWS_SupplierOutstanding(){
        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();
            final String sysuserno = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();
            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("locationname", vendorname);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSupplierOutstanding", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablecustomerledger);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        header_customername.setText(vendorname);

                        TableRow tblrowHeading = new TableRow(OutstandingSupplierActivity.this);

                        TextView highsHeading_vendorname = initPlainHeaderTextView();
                        highsHeading_vendorname.setText("Supplier");
                        highsHeading_vendorname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_vendorname.setGravity(Gravity.LEFT);

                        TextView highsHeading_debit = initPlainHeaderTextView();
                        highsHeading_debit.setText("Debit");
                        highsHeading_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_debit.setGravity(Gravity.RIGHT);

                        TextView highsHeading_credit = initPlainHeaderTextView();
                        highsHeading_credit.setText("Credit");
                        highsHeading_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_credit.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_vendorname);
                        tblrowHeading.addView(highsHeading_debit);
                        tblrowHeading.addView(highsHeading_credit);

                        tablesalesregister.addView(tblrowHeading);

                        double fCredit;
                        double fDebit;

                        fCredit = 0;
                        fDebit = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("supplieroutstanding");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String dCustcd;
                                dCustcd = jsonObject.getString("sysvendorno");

                                TableRow tblrowLabels = new TableRow(OutstandingSupplierActivity.this);

                                final TextView highsLabel_vendorname = initPlainTextView(i);
                                highsLabel_vendorname.setText(jsonObject.getString("vendorname"));
                                highsLabel_vendorname.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(OutstandingSupplierActivity.this, Supplier_Ledger_View.class);
                                        intent.putExtra("sysvendorno",dCustcd);
                                        intent.putExtra("name",highsLabel_vendorname.getText());
                                        intent.putExtra("fromdate",pickFromDate.getText().toString());
                                        intent.putExtra("todate",pickToDate.getText().toString());
                                        startActivity(intent);
                                    }
                                });

                                highsLabel_vendorname.setTypeface(Typeface.DEFAULT);
                                highsLabel_vendorname.setGravity(Gravity.LEFT);

                                TextView highsLabel_debit= initPlainTextView(i);
                                highsLabel_debit.setText(jsonObject.getString("DEBIT"));
                                highsLabel_debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_debit.setGravity(Gravity.RIGHT);

                                TextView highsLabel_credit = initPlainTextView(i);
                                highsLabel_credit.setText(jsonObject.getString("CREDIT"));
                                highsLabel_credit.setTypeface(Typeface.DEFAULT);
                                highsLabel_credit.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_vendorname);
                                tblrowLabels.addView(highsLabel_debit);
                                tblrowLabels.addView(highsLabel_credit);

                                tablesalesregister.addView(tblrowLabels);

                                fCredit += Double.valueOf(jsonObject.getString("CREDIT"));
                                fDebit += Double.valueOf(jsonObject.getString("DEBIT"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(OutstandingSupplierActivity.this);
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

                        // tblrowFooter.addView(highsFooter_category);
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

        TextView textView = new TextView(OutstandingSupplierActivity.this);
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
        TextView textView = new TextView(OutstandingSupplierActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(OutstandingSupplierActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    @SuppressWarnings("deprecation")
    public void setFromDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @SuppressWarnings("deprecation")
    public void setToDate(View view) {
        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    fromDateListener, year, month, day);
        }

        if (id == 998) {
            return new DatePickerDialog(this,
                    toDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener fromDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showFromDate(arg1, arg2, arg3);
                }
            };

    private void showFromDate(int year, int month, int day) {
        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        invokeWS_SupplierOutstanding();
    }

    private DatePickerDialog.OnDateSetListener toDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showToDate(arg1, arg2, arg3);
                }
            };

    private void showToDate(int year, int month, int day) {
        pickToDate = (Button) findViewById(R.id.pickToDate);
        pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        invokeWS_SupplierOutstanding();
    }

}


