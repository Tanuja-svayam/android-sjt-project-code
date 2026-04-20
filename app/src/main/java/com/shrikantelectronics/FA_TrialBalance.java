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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;

public class FA_TrialBalance extends AppCompatActivity {

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

    public String ReportType="";

    public double sngOpStock= 0, sngCStock= 0, sngDrtotal= 0, sngCrtotal= 0, dBal = 0;

    String sGroupName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_trial_balance);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

       pickFromDate = (Button) findViewById(R.id.pickFromDate);
       pickToDate = (Button) findViewById(R.id.pickToDate);

       final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
       pickFromDate.setText(globalVariable.getAccountingStartDate());
       pickToDate.setText(globalVariable.getAccountingEndDate());

        ReportType="Condensed";
       invokeWS_FA_Stock_Summary();

    }

    public void invokeWS_FA_Stock_Summary() {

        try {



            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            //sngOpStock = 0;
           // sngCStock = 0;

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/FA_StockSummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_stocksummary");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sngOpStock = jsonObject.getDouble("op_valueprice");
                        sngCStock = jsonObject.getDouble("cl_valueprice");

                        invokeWS_FA_Trial_Balance();

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
            	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS_FA_Trial_Balance() {

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

            //paramsMap.put("fromdate", "01-APR-2021");
           // paramsMap.put("todate", "31-MAR-2022");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

           // header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
         //   header_toplocationstock.setText("TRIAL BALANCE");

            ApiHelper.post(URL + "Service1.asmx/FA_Trial_Balance", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                      // tabletoplocationstock.setStretchAllColumns(true);
                       // tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(FA_TrialBalance.this);
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

                        tblrowHeading.addView(highsHeading_Particulars);
                        tblrowHeading.addView(highsHeading_Debit);
                        tblrowHeading.addView(highsheading_Credit);

                        tabletoplocationstock.addView(tblrowHeading);

                        String strColName = "";
                        double drAmt= 0.00, crAmt= 0.00, gdrAmt= 0.00, gcrAmt = 0.00;


                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_trialbalance");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {

                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels_group = new TableRow(FA_TrialBalance.this);

                                drAmt= jsonObject.getDouble("cl_dr_amount");
                                crAmt= jsonObject.getDouble("cl_cr_amount");

                                if (!sGroupName.equals(jsonObject.getString("gname")))
                                {
                                    String top_id = "";
                                    top_id=jsonObject.getString("top_id");

                                    gdrAmt= jsonObject.getDouble ("gr_dr_amount");
                                    gcrAmt= jsonObject.getDouble("gr_cr_amount");

                                    final TextView highsLabel_Particulars = initPlainTextView(i);
                                    highsLabel_Particulars.setText(jsonObject.getString("gname"));
                                    highsLabel_Particulars.setTypeface(Typeface.DEFAULT_BOLD);
                                    highsLabel_Particulars.setGravity(Gravity.LEFT);
                                    highsLabel_Particulars.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                    highsLabel_Particulars.setTextSize(16);

                                    TextView highsLabel_Debit = initPlainTextView(i);
                                    if (gdrAmt>0)
                                    {highsLabel_Debit.setText(String.format("%.2f",gdrAmt));}
                                    else {highsLabel_Debit.setText("");}
                                    highsLabel_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                                    highsLabel_Debit.setGravity(Gravity.RIGHT);
                                    highsLabel_Debit.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                    highsLabel_Debit.setTextSize(16);

                                    TextView highslabel_Credit = initPlainTextView(i);
                                    if (gcrAmt>0)
                                    {highslabel_Credit.setText(String.format("%.2f",gcrAmt));}
                                    else {highslabel_Credit.setText("");}
                                    highslabel_Credit.setTypeface(Typeface.DEFAULT_BOLD);
                                    highslabel_Credit.setGravity(Gravity.RIGHT);
                                    highslabel_Credit.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                    highslabel_Credit.setTextSize(16);

                                    tblrowLabels_group.addView(highsLabel_Particulars);
                                    tblrowLabels_group.addView(highsLabel_Debit);
                                    tblrowLabels_group.addView(highslabel_Credit);

                                    tblrowLabels_group.setClickable(true);

                                    final String finalTop_id = top_id;
                                    tblrowLabels_group.setOnClickListener(new OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent intent = new Intent(FA_TrialBalance.this, FA_GroupSummary.class);
                                            intent.putExtra("groupname", finalTop_id);
                                            intent.putExtra("fromdate",pickFromDate.getText().toString());
                                            intent.putExtra("todate",pickToDate.getText().toString());
                                            startActivity(intent);
                                        }
                                    });

                                    tabletoplocationstock.addView(tblrowLabels_group);
                                }

                                sngDrtotal = sngDrtotal + drAmt;
                                sngCrtotal = sngCrtotal + crAmt;

                                String accode = "";
                                accode = jsonObject.getString("accode");

                                if (ReportType.equals("Detailed")) {
                                    TableRow tblrowLabels = new TableRow(FA_TrialBalance.this);

                                    final TextView highsLabel_Particulars = initPlainTextView(i);
                                    highsLabel_Particulars.setText("          "+jsonObject.getString("name"));
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

                                    tblrowLabels.addView(highsLabel_Particulars);
                                    tblrowLabels.addView(highsLabel_Debit);
                                    tblrowLabels.addView(highslabel_Credit);

                                    tblrowLabels.setClickable(true);


                                    if (jsonObject.getString("subledger").equals("N")) {
                                        final String finalaccode = accode;
                                        tblrowLabels.setOnClickListener(new OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {

                                                Intent intent = new Intent(FA_TrialBalance.this, FA_GroupSummary.class);
                                                intent.putExtra("groupname",finalaccode);
                                                intent.putExtra("fromdate",pickFromDate.getText().toString());
                                                intent.putExtra("todate",pickToDate.getText().toString());
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    if (jsonObject.getString("subledger").equals("Y")) {
                                        final String finalaccode = accode;

                                        tblrowLabels.setOnClickListener(new OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {
                                                Intent intent = new Intent(FA_TrialBalance.this, FA_LedgerMonthSummary.class);
                                                intent.putExtra("groupname",finalaccode);
                                                intent.putExtra("fromdate",pickFromDate.getText().toString());
                                                intent.putExtra("todate",pickToDate.getText().toString());
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    tabletoplocationstock.addView(tblrowLabels);
                                }
                                sGroupName = jsonObject.getString("gname");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowLabels_stock = new TableRow(FA_TrialBalance.this);

                        final TextView highsLabel_Particulars_stock = initPlainTextView(0);
                        highsLabel_Particulars_stock.setText("Opening Stock");
                        highsLabel_Particulars_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Particulars_stock.setGravity(Gravity.LEFT);
                        highsLabel_Particulars_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Particulars_stock.setTextSize(16);

                        TextView highsLabel_Debit_stock = initPlainTextView(0);
                        if (sngOpStock!=0)
                        {highsLabel_Debit_stock.setText(String.format("%.2f",sngOpStock));}
                        else {highsLabel_Debit_stock.setText("");}
                        highsLabel_Debit_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Debit_stock.setGravity(Gravity.RIGHT);
                        highsLabel_Debit_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Debit_stock.setTextSize(16);

                        TextView highslabel_Credit_stock = initPlainTextView(0);
                        highslabel_Credit_stock.setText("");
                        highslabel_Credit_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highslabel_Credit_stock.setGravity(Gravity.RIGHT);
                        highslabel_Credit_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highslabel_Credit_stock.setTextSize(16);


                        tblrowLabels_stock.addView(highsLabel_Particulars_stock);
                        tblrowLabels_stock.addView(highsLabel_Debit_stock);
                        tblrowLabels_stock.addView(highslabel_Credit_stock);

                        tblrowLabels_stock.setClickable(false);

                        tabletoplocationstock.addView(tblrowLabels_stock);


                        double f_Debit_total = 0, f_Credit_total = 0;

                        f_Debit_total = sngDrtotal + sngOpStock;
                        f_Credit_total = sngCrtotal;

                        double f_diff_opbal = 0, f_diff_op_amt_dis_dr = 0, f_diff_op_amt_dis_cr = 0;
                        String f_display_diff_in_op = "";

                        if (f_Credit_total != f_Debit_total){
                            if (f_Credit_total >f_Debit_total){
                                f_diff_opbal = f_Credit_total - f_Debit_total;}
                            else {
                                f_diff_opbal = f_Debit_total - f_Credit_total;
                            }
                        }
                        else{
                            f_diff_opbal = 0;
                        }

                        if (f_Credit_total != f_Debit_total)
                        {
                            if (f_Debit_total < f_Credit_total) {
                                f_diff_op_amt_dis_dr = f_diff_opbal;
                            }
                            else {
                                f_diff_op_amt_dis_dr = 0;
                            }
                            if (f_Credit_total < f_Debit_total) {
                                f_diff_op_amt_dis_cr = f_diff_opbal;
                            }
                            else{
                                f_diff_op_amt_dis_cr = 0;
                            }
                        }

                        if (f_Credit_total != f_Debit_total) {
                            f_display_diff_in_op = "Diff. in opening balance";
                        }
                        else {
                            f_display_diff_in_op = "";
                        }

                        TableRow tblrowLabels_diff = new TableRow(FA_TrialBalance.this);

                        final TextView highsLabel_Particulars_diff = initPlainTextView(0);
                        highsLabel_Particulars_diff.setText(f_display_diff_in_op);
                        highsLabel_Particulars_diff.setTypeface(Typeface.DEFAULT);
                        highsLabel_Particulars_diff.setGravity(Gravity.LEFT);

                        TextView highsLabel_Debit_diff = initPlainTextView(0);
                        if (f_diff_op_amt_dis_dr != 0)
                        {highsLabel_Debit_diff.setText(String.format("%.2f",f_diff_op_amt_dis_dr));}
                        else {highsLabel_Debit_diff.setText("");}
                        highsLabel_Debit_diff.setTypeface(Typeface.DEFAULT);
                        highsLabel_Debit_diff.setGravity(Gravity.RIGHT);

                        TextView highslabel_Credit_diff = initPlainTextView(0);
                        if (f_diff_op_amt_dis_cr != 0)
                        {highslabel_Credit_diff.setText(String.format("%.2f",f_diff_op_amt_dis_cr));}
                        else {highslabel_Credit_diff.setText("");}
                        highslabel_Credit_diff.setTypeface(Typeface.DEFAULT);
                        highslabel_Credit_diff.setGravity(Gravity.RIGHT);

                        tblrowLabels_diff.addView(highsLabel_Particulars_diff);
                        tblrowLabels_diff.addView(highsLabel_Debit_diff);
                        tblrowLabels_diff.addView(highslabel_Credit_diff);

                        tblrowLabels_diff.setClickable(false);

                        tabletoplocationstock.addView(tblrowLabels_diff);


                        double f_tot_asset = 0;
                        double f_tot_liabilities = 0;
                        f_tot_asset = f_Debit_total + f_diff_op_amt_dis_dr;
                        f_tot_liabilities = f_Credit_total + f_diff_op_amt_dis_cr;

                        TableRow tblrowFooter = new TableRow(FA_TrialBalance.this);
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

                        tblrowFooter.addView(highsFooter_Particulars);
                        tblrowFooter.addView(highsFooter_Debit);
                        tblrowFooter.addView(highsFooter_Credit);

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

        TextView textView = new TextView(FA_TrialBalance.this);
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
        TextView textView = new TextView(FA_TrialBalance.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_TrialBalance.this);
        textView.setPadding(10, 10, 10, 10);

        textView.setBackgroundColor(Color.parseColor("#142BAD"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(16);
        return textView;
    }

    @SuppressWarnings("deprecation")
    public void RefreshData(View view) {
        invokeWS_FA_Stock_Summary();
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
        invokeWS_FA_Stock_Summary();
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
        invokeWS_FA_Stock_Summary();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_fa_trialbalance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_Condensed) {
            ReportType="Condensed";
            invokeWS_FA_Stock_Summary();
        } else if (id == R.id.action_detailed) {
            ReportType="Detailed";
            invokeWS_FA_Stock_Summary();
        } else if (id == R.id.action_ledgerwise) {
            Intent intent = new Intent(FA_TrialBalance.this, FA_LedgerwiseBalance.class);
            intent.putExtra("groupname","");
            intent.putExtra("statementtype","");
            intent.putExtra("fromdate",pickFromDate.getText().toString());
            intent.putExtra("todate",pickToDate.getText().toString());
            startActivity(intent);
        }else if (id == R.id.action_Download) {

        }
        return super.onOptionsItemSelected(item);
    }
}