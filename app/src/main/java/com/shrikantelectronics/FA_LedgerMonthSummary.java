package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

public class FA_LedgerMonthSummary extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationstock;
    Button pickFromDate;
    Button pickToDate;
    TextView header_toplocationstock;

    ProgressDialog prgDialog;
    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;

    int year, month, day;

    public double sngDrtotal= 0, sngCrtotal= 0, dBal = 0;

    String sGroupName = "";
    String groupname = "";
    String fromdate = "";
    String todate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_ledgermonthsummary);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        groupname = i.getStringExtra("groupname");
        fromdate = i.getStringExtra("fromdate");
        todate= i.getStringExtra("todate");

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

       pickFromDate = (Button) findViewById(R.id.pickFromDate);
       pickToDate = (Button) findViewById(R.id.pickToDate);

       final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        if ((null==groupname) || ("".equals(groupname))) {
            //Toast.makeText(getApplicationContext(), "Please provide the Server url Settings->Server URL->Save", Toast.LENGTH_SHORT).show();
            groupname="0";
        }
        if ((null==fromdate) || ("".equals(fromdate)))
        {
            pickFromDate.setText(globalVariable.getAccountingStartDate());
        }
        else
        {
            pickFromDate.setText(fromdate);
        }

        if ((null==todate) || ("".equals(todate)))
        {
            pickToDate.setText(globalVariable.getAccountingEndDate());
        }
        else
        {
            pickToDate.setText(todate);
        }

       invokeWS_FA_Month_Summary();

    }

    public void invokeWS_FA_Month_Summary() {

        try {

            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            sngDrtotal = 0;
            sngCrtotal = 0;
            dBal = 0;

            sGroupName = "";

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("sysaccledgerno", groupname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText("MONTHWISE SUMMARY");

            ApiHelper.post(URL + "Service1.asmx/FA_Ledger_Month_Summary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                      // tabletoplocationstock.setStretchAllColumns(true);
                       // tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(FA_LedgerMonthSummary.this);
                        TextView highsHeading_Particulars = initPlainHeaderTextView();
                        highsHeading_Particulars.setText("Particulars");
                        highsHeading_Particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Particulars.setGravity(Gravity.LEFT);

                        TextView highsHeading_Debit = initPlainHeaderTextView();
                        highsHeading_Debit.setText("Debit");
                        highsHeading_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Debit.setGravity(Gravity.RIGHT);

                        TextView highsheading_Credit = initPlainHeaderTextView();
                        highsheading_Credit.setText("Credit");
                        highsheading_Credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Credit.setGravity(Gravity.RIGHT);

                        TextView highsheading_Closing_Balance = initPlainHeaderTextView();
                        highsheading_Closing_Balance.setText("Balance");
                        highsheading_Closing_Balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Closing_Balance.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_Particulars);
                        tblrowHeading.addView(highsHeading_Debit);
                        tblrowHeading.addView(highsheading_Credit);
                        tblrowHeading.addView(highsheading_Closing_Balance);

                        tabletoplocationstock.addView(tblrowHeading);

                        String strColName = "";
                        double drAmt= 0.00, crAmt= 0.00, gdrAmt= 0.00, gcrAmt = 0.00, sngrunningBal = 0, runningBal = 0;
                       // int I,X  = 0;
                       // I = 6;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_ledgermonthsummary");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {

                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels_group = new TableRow(FA_LedgerMonthSummary.this);

                                drAmt= jsonObject.getDouble("cl_dr_amount");
                                crAmt= jsonObject.getDouble("cl_cr_amount");
                                runningBal= jsonObject.getDouble("runningBal");

                                sngDrtotal = sngDrtotal + drAmt;
                                sngCrtotal = sngCrtotal + crAmt;

                                String MonthId = "";
                                MonthId = jsonObject.getString("MonthId");

                                String yearid = "";
                                yearid = jsonObject.getString("yearid");

                                if (runningBal < 0)
                                {
                                    sngrunningBal = runningBal * (-1);
                                }
                                else {
                                    sngrunningBal = runningBal;
                                }

                                TableRow tblrowLabels = new TableRow(FA_LedgerMonthSummary.this);

                                final TextView highsLabel_Particulars = initPlainTextView(i);
                                highsLabel_Particulars.setText(jsonObject.getString("name"));
                                highsLabel_Particulars.setTypeface(Typeface.DEFAULT);
                                highsLabel_Particulars.setGravity(Gravity.LEFT);

                                TextView highsLabel_Debit = initPlainTextView(i);
                                if (drAmt>0)
                                {highsLabel_Debit.setText(String.format("%.2f",drAmt));}
                                else {highsLabel_Debit.setText("");}
                                highsLabel_Debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_Debit.setGravity(Gravity.RIGHT);

                                TextView highslabel_Credit = initPlainTextView(i);
                                if (crAmt>0)
                                {highslabel_Credit.setText(String.format("%.2f",crAmt));}
                                else {highslabel_Credit.setText("");}
                                highslabel_Credit.setTypeface(Typeface.DEFAULT);
                                highslabel_Credit.setGravity(Gravity.RIGHT);

                                TextView highslabel_Closing_Balance = initPlainTextView(i);
                                highslabel_Closing_Balance.setText(String.format("%.2f",sngrunningBal) + " " +jsonObject.getString("drcr"));
                                highslabel_Closing_Balance.setTypeface(Typeface.DEFAULT);
                                highslabel_Closing_Balance.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_Particulars);
                                tblrowLabels.addView(highsLabel_Debit);
                                tblrowLabels.addView(highslabel_Credit);
                                tblrowLabels.addView(highslabel_Closing_Balance);

                                tblrowLabels.setClickable(true);

                                final String fMonthId = MonthId;
                                final String fyearid = yearid;

                                tblrowLabels.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(FA_LedgerMonthSummary.this, FA_LedgerMonthList.class);
                                        intent.putExtra("groupname",groupname);
                                        intent.putExtra("MonthId",fMonthId);
                                        intent.putExtra("yearid",fyearid);
                                        intent.putExtra("callsource","MONTHSUMMARY");
                                        startActivity(intent);
                                    }
                                });
                                tabletoplocationstock.addView(tblrowLabels);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        double f_tot_asset = 0;
                        double f_tot_liabilities = 0;
                        f_tot_asset = sngDrtotal;
                        f_tot_liabilities = sngCrtotal;

                        TableRow tblrowFooter = new TableRow(FA_LedgerMonthSummary.this);
                        TextView highsFooter_Particulars = initPlainFooterTextView();
                        highsFooter_Particulars.setText("GRAND TOTAL");
                        highsFooter_Particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Particulars.setGravity(Gravity.LEFT);

                        TextView highsFooter_Debit = initPlainFooterTextView();
                        highsFooter_Debit.setText(String.format("%.2f",f_tot_asset));
                        highsFooter_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Debit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Credit = initPlainFooterTextView();
                        highsFooter_Credit.setText(String.format("%.2f",f_tot_liabilities));
                        highsFooter_Credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Credit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Closing_Balance = initPlainFooterTextView();
                        highsFooter_Closing_Balance.setText(String.format("%.2f",sngrunningBal));
                        highsFooter_Closing_Balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Closing_Balance.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_Particulars);
                        tblrowFooter.addView(highsFooter_Debit);
                        tblrowFooter.addView(highsFooter_Credit);
                        tblrowFooter.addView(highsFooter_Closing_Balance);

                        tabletoplocationstock.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Status code :"+ e.toString() +"errmsg : "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(FA_LedgerMonthSummary.this);
        textView.setPadding(10, 10, 10, 10);
/*
        if((n%2)==0)
        {
            textView.setBackgroundResource(R.drawable.cell_shape);
        }
        else
        {
            textView.setBackgroundResource(R.drawable.cell_shape_oddrow);
        }
*/
        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    private TextView initPlainHeaderTextView() {
        TextView textView = new TextView(FA_LedgerMonthSummary.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_LedgerMonthSummary.this);
        textView.setPadding(10, 10, 10, 10);

        textView.setBackgroundColor(Color.parseColor("#142BAD"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(16);
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
        invokeWS_FA_Month_Summary();

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
        invokeWS_FA_Month_Summary();

    }
}