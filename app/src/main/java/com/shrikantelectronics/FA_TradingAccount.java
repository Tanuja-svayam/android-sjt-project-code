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

public class FA_TradingAccount extends AppCompatActivity {

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
        setContentView(R.layout.activity_fa_trading_account);

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

       ReportType="Detailed";
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

            ApiHelper.post(URL + "Service1.asmx/FA_StockSummary", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_stocksummary");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sngOpStock = jsonObject.getDouble("op_valueprice");
                        sngCStock = jsonObject.getDouble("cl_valueprice");

                        invokeWS_FA_Trading_Account();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void invokeWS_FA_Trading_Account() {

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

            ApiHelper.post(URL + "Service1.asmx/FA_Trading_Account", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                      // tabletoplocationstock.setStretchAllColumns(true);
                       // tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(FA_TradingAccount.this);
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

                        TableRow tblrowLabels_opening_stock = new TableRow(FA_TradingAccount.this);

                        final TextView highsLabel_Particulars_opening_stock = initPlainTextView(0);
                        highsLabel_Particulars_opening_stock.setText("Opening Stock");
                        highsLabel_Particulars_opening_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Particulars_opening_stock.setGravity(Gravity.LEFT);
                        highsLabel_Particulars_opening_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Particulars_opening_stock.setTextSize(16);

                        TextView highsLabel_Debit_opening_stock = initPlainTextView(0);
                        if (sngOpStock!=0)
                        {highsLabel_Debit_opening_stock.setText(String.format("%.2f",sngOpStock));}
                        else {highsLabel_Debit_opening_stock.setText("");}
                        highsLabel_Debit_opening_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Debit_opening_stock.setGravity(Gravity.RIGHT);
                        highsLabel_Debit_opening_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Debit_opening_stock.setTextSize(16);

                        TextView highslabel_Credit_opening_stock = initPlainTextView(0);
                        highslabel_Credit_opening_stock.setText("");
                        highslabel_Credit_opening_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highslabel_Credit_opening_stock.setGravity(Gravity.RIGHT);
                        highslabel_Credit_opening_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highslabel_Credit_opening_stock.setTextSize(16);

                        tblrowLabels_opening_stock.addView(highsLabel_Particulars_opening_stock);
                        tblrowLabels_opening_stock.addView(highsLabel_Debit_opening_stock);
                        tblrowLabels_opening_stock.addView(highslabel_Credit_opening_stock);

                        tblrowLabels_opening_stock.setClickable(false);

                        tabletoplocationstock.addView(tblrowLabels_opening_stock);

                        String strColName = "";
                        double drAmt= 0.00, crAmt= 0.00, gdrAmt= 0.00, gcrAmt = 0.00;


                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_tradingstatement");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {

                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels_group = new TableRow(FA_TradingAccount.this);

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
                                            Intent intent = new Intent(FA_TradingAccount.this, FA_GroupSummary.class);
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
                                    TableRow tblrowLabels = new TableRow(FA_TradingAccount.this);

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

                                                Intent intent = new Intent(FA_TradingAccount.this, FA_GroupSummary.class);
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
                                                Intent intent = new Intent(FA_TradingAccount.this, FA_LedgerMonthSummary.class);
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

                        TableRow tblrowLabels_closing_stock = new TableRow(FA_TradingAccount.this);

                        final TextView highsLabel_Particulars_closing_stock = initPlainTextView(0);
                        highsLabel_Particulars_closing_stock.setText("Closing Stock");
                        highsLabel_Particulars_closing_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Particulars_closing_stock.setGravity(Gravity.LEFT);
                        highsLabel_Particulars_closing_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Particulars_closing_stock.setTextSize(16);

                        TextView highsLabel_Debit_closing_stock = initPlainTextView(0);
                        highsLabel_Debit_closing_stock.setText("");
                        highsLabel_Debit_closing_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Debit_closing_stock.setGravity(Gravity.RIGHT);
                        highsLabel_Debit_closing_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Debit_closing_stock.setTextSize(16);

                        TextView highslabel_Credit_closing_stock = initPlainTextView(0);
                        if (sngCStock!=0)
                        highslabel_Credit_closing_stock.setText(String.format("%.2f",sngCStock));
                        highslabel_Credit_closing_stock.setTypeface(Typeface.DEFAULT_BOLD);
                        highslabel_Credit_closing_stock.setGravity(Gravity.RIGHT);
                        highslabel_Credit_closing_stock.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highslabel_Credit_closing_stock.setTextSize(16);

                        tblrowLabels_closing_stock.addView(highsLabel_Particulars_closing_stock);
                        tblrowLabels_closing_stock.addView(highsLabel_Debit_closing_stock);
                        tblrowLabels_closing_stock.addView(highslabel_Credit_closing_stock);

                        tblrowLabels_closing_stock.setClickable(false);

                        tabletoplocationstock.addView(tblrowLabels_closing_stock);

                        double f_Debit_total = 0, f_Credit_total = 0;

                        f_Debit_total = sngDrtotal ;
                        f_Credit_total = sngCrtotal;

                        double f_net_Loss = 0, f_net_Profit = 0, f_Bal= 0;
                        String f_net_Profit_Loss_Dis = "";

                        f_Bal = f_Debit_total - f_Credit_total;

                        if ((f_Bal + sngOpStock - sngCStock) < 0){
                            f_net_Profit = (f_Bal + sngOpStock - sngCStock) * -1;
                        }
                         else{f_net_Profit = 0;}

                        if ((f_Bal + sngOpStock - sngCStock) >= 0) {
                            f_net_Loss = (f_Bal + sngOpStock - sngCStock);
                        }
                        else{f_net_Loss = 0;}

                        if (f_net_Profit > 0) {
                            f_net_Profit_Loss_Dis = "Gross Profit C/o";
                        }
                        else{f_net_Profit_Loss_Dis = "Gross Loss C/o";
                        }

                        TableRow tblrowLabels_Profit_Loss = new TableRow(FA_TradingAccount.this);

                        final TextView highsLabel_Particulars_Profit_Loss = initPlainTextView(0);
                        highsLabel_Particulars_Profit_Loss.setText(f_net_Profit_Loss_Dis);
                        highsLabel_Particulars_Profit_Loss.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Particulars_Profit_Loss.setGravity(Gravity.LEFT);
                        highsLabel_Particulars_Profit_Loss.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Particulars_Profit_Loss.setTextSize(16);

                        TextView highsLabel_Debit_Profit_Loss = initPlainTextView(0);
                        if (f_net_Profit!=0)
                        {highsLabel_Debit_Profit_Loss.setText(String.format("%.2f",f_net_Profit));}
                        else {highsLabel_Debit_Profit_Loss.setText("");}
                        highsLabel_Debit_Profit_Loss.setTypeface(Typeface.DEFAULT_BOLD);
                        highsLabel_Debit_Profit_Loss.setGravity(Gravity.RIGHT);
                        highsLabel_Debit_Profit_Loss.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highsLabel_Debit_Profit_Loss.setTextSize(16);

                        TextView highslabel_Credit_Profit_Loss = initPlainTextView(0);
                        if (f_net_Profit!=0)
                        {highslabel_Credit_Profit_Loss.setText(String.format("%.2f",f_net_Loss));}
                        else {highslabel_Credit_Profit_Loss.setText("");}
                        highslabel_Credit_Profit_Loss.setTypeface(Typeface.DEFAULT_BOLD);
                        highslabel_Credit_Profit_Loss.setGravity(Gravity.RIGHT);
                        highslabel_Credit_Profit_Loss.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                        highslabel_Credit_Profit_Loss.setTextSize(16);

                        tblrowLabels_Profit_Loss.addView(highsLabel_Particulars_Profit_Loss);
                        tblrowLabels_Profit_Loss.addView(highsLabel_Debit_Profit_Loss);
                        tblrowLabels_Profit_Loss.addView(highslabel_Credit_Profit_Loss);

                        tblrowLabels_Profit_Loss.setClickable(false);

                        tabletoplocationstock.addView(tblrowLabels_Profit_Loss);

                        double f_tot_Debit = 0, f_tot_Credit  = 0;

                        f_tot_Debit = f_Debit_total + f_net_Profit + sngOpStock;
                        f_tot_Credit = f_Credit_total + f_net_Loss + sngCStock;

                        TableRow tblrowFooter = new TableRow(FA_TradingAccount.this);
                        TextView highsFooter_Particulars = initPlainFooterTextView();
                        highsFooter_Particulars.setText("GRAND TOTAL");
                        highsFooter_Particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Particulars.setGravity(Gravity.LEFT);

                        TextView highsFooter_Debit = initPlainFooterTextView();
                        highsFooter_Debit.setText(String.format("%.2f",f_tot_Debit));
                        highsFooter_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Debit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Credit = initPlainFooterTextView();
                        highsFooter_Credit.setText(String.format("%.2f",f_tot_Credit));
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

        TextView textView = new TextView(FA_TradingAccount.this);
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
        TextView textView = new TextView(FA_TradingAccount.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_TradingAccount.this);
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
            Intent intent = new Intent(FA_TradingAccount.this, FA_LedgerwiseBalance.class);
            intent.putExtra("groupname","");
            intent.putExtra("statementtype","TA");
            intent.putExtra("fromdate",pickFromDate.getText().toString());
            intent.putExtra("todate",pickToDate.getText().toString());
            startActivity(intent);
        }else if (id == R.id.action_Download) {

        }
        return super.onOptionsItemSelected(item);
    }
}