package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
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

public class Ledger_Finance_View extends Activity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ListViewAdapter_Customer_Ledger adapter;

    Button pickFromDate;
    Button pickToDate;

    ProgressDialog prgDialog;
    private ListView lv_customer ;
    EditText inputSearch_customer ;
    ArrayList<HashMap<String, String>> customerlist;

    ArrayList<Customer_Ledger> arraylist = new ArrayList<Customer_Ledger>();

    String fincompanycd;
    String name;
    int year, month, day;
    String fromdate;
    String todate;

    TextView header_customername;
    TableLayout tablesalesregister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ledger_view);

        Intent i = getIntent();

        fincompanycd = i.getStringExtra("fincompanycd");
        name = i.getStringExtra("name");

        fromdate = i.getStringExtra("fromdate");
        todate = i.getStringExtra("todate");

        header_customername = (TextView) findViewById(R.id.header_customername);
        header_customername.setText(name);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickToDate = (Button) findViewById(R.id.pickToDate);

        if ((fromdate.length()>0) && (todate.length()>0))
        {
            pickFromDate.setText(fromdate);
            pickToDate.setText(todate);
        }
        else
        {
            pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,1));
            pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        }
        invokeWS_Finance_Ledger(fincompanycd);
    }

    public void invokeWS_Finance_Ledger(String fincompanycd){
        try {

            Map<String, String> paramsMap = new HashMap<>();
            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("dfincompanycd", fincompanycd);
            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetFinanceLedger", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablecustomerledger);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Ledger_Finance_View.this);
                        TextView highsHeading_voucherdt = initPlainHeaderTextView();
                        highsHeading_voucherdt.setText("Date");
                        highsHeading_voucherdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_voucherdt.setGravity(Gravity.CENTER);

                        TextView highsHeading_trndesc = initPlainHeaderTextView();
                        highsHeading_trndesc.setText("Particular");
                        highsHeading_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_trndesc.setGravity(Gravity.LEFT);

                        TextView highsHeading_debit = initPlainHeaderTextView();
                        highsHeading_debit.setText("Debit");
                        highsHeading_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_debit.setGravity(Gravity.RIGHT);

                        TextView highsHeading_credit = initPlainHeaderTextView();
                        highsHeading_credit.setText("Credit");
                        highsHeading_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_credit.setGravity(Gravity.RIGHT);

                        TextView highsHeading_balance = initPlainHeaderTextView();
                        highsHeading_balance.setText("Balance");
                        highsHeading_balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_balance.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_voucherdt);
                        tblrowHeading.addView(highsHeading_trndesc);
                        tblrowHeading.addView(highsHeading_debit);
                        tblrowHeading.addView(highsHeading_credit);
                        tblrowHeading.addView(highsHeading_balance);

                        tablesalesregister.addView(tblrowHeading);

                        double fCredit;
                        double fDebit;

                        fCredit = 0;
                        fDebit = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("financeledger");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);


                                TableRow tblrowLabels = new TableRow(Ledger_Finance_View.this);

                                TextView highsLabel_voucherdt = initPlainTextView(i);
                                highsLabel_voucherdt.setText(jsonObject.getString("Voucher_Date"));
                                highsLabel_voucherdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_voucherdt.setGravity(Gravity.CENTER);

                                TextView highsLabel_trndesc = initPlainTextView(i);
                                highsLabel_trndesc.setText(jsonObject.getString("Customer"));
                                highsLabel_trndesc.setTypeface(Typeface.DEFAULT);
                                highsLabel_trndesc.setGravity(Gravity.LEFT);

                                TextView highsLabel_debit= initPlainTextView(i);
                                highsLabel_debit.setText(jsonObject.getString("Debit"));
                                highsLabel_debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_debit.setGravity(Gravity.RIGHT);

                                TextView highsLabel_credit = initPlainTextView(i);
                                highsLabel_credit.setText(jsonObject.getString("Credit"));
                                highsLabel_credit.setTypeface(Typeface.DEFAULT);
                                highsLabel_credit.setGravity(Gravity.RIGHT);

                                TextView highsLabel_balance = initPlainTextView(i);
                                highsLabel_balance.setText(jsonObject.getString("Balance"));
                                highsLabel_balance.setTypeface(Typeface.DEFAULT);
                                highsLabel_balance.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_voucherdt);
                                tblrowLabels.addView(highsLabel_trndesc);
                                tblrowLabels.addView(highsLabel_debit);
                                tblrowLabels.addView(highsLabel_credit);
                                tblrowLabels.addView(highsLabel_balance);

                                tablesalesregister.addView(tblrowLabels);

                                //fCredit += Integer.valueOf(0+jsonObject.getString("credit"));
                                //fDebit += Integer.valueOf(0+jsonObject.getString("debit"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(Ledger_Finance_View.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText("TOTAL");
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_debit = initPlainFooterTextView();
                        highsFooter_debit.setText(String.valueOf(fDebit));
                        highsFooter_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_debit.setGravity(Gravity.RIGHT);


                        TextView highsFooter_credit = initPlainFooterTextView();
                        highsFooter_credit.setText(String.valueOf(fCredit));
                        highsFooter_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_credit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_balance = initPlainFooterTextView();
                        highsFooter_balance.setText("");
                        highsFooter_balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_balance.setGravity(Gravity.CENTER);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_debit);
                        tblrowFooter.addView(highsFooter_credit);
                        tblrowFooter.addView(highsFooter_balance);

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

        TextView textView = new TextView(Ledger_Finance_View.this);
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
        TextView textView = new TextView(Ledger_Finance_View.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Ledger_Finance_View.this);
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
        invokeWS_Finance_Ledger(fincompanycd);
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
        invokeWS_Finance_Ledger(fincompanycd);
    }

}


