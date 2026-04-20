package com.shrikantelectronics;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class FA_LedgerMonthList extends AppCompatActivity {

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

    public double sngOpStock= 0, sngCStock= 0, sngDrtotal= 0, sngCrtotal= 0, dBal = 0;
    String sGroupName = "";
    String groupname = "";
    String MonthId = "";
    String MonthName = "";
    String yearid = "";
    String fromdate = "";
    String todate = "";
    String vouchertypename = "";
    String callsource= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_ledgermonthlist);

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

        Intent i = getIntent();
        callsource= i.getStringExtra("callsource");

        if (callsource.equals("MONTHSUMMARY"))
        {
            groupname = i.getStringExtra("groupname");
            MonthId = i.getStringExtra("MonthId");
            yearid = i.getStringExtra("yearid");

            if ((null==groupname) || ("".equals(groupname))) {
                groupname="0";
            }
            if ((null!=MonthId) || (!"".equals(MonthId)) || (!"".equals(yearid))|| (!"".equals(yearid))) {

                switch (MonthId)
                {
                    case "01":
                        MonthName="JAN";
                        break;
                    case "02":
                        MonthName="FEB";
                        break;
                    case "03":
                        MonthName="MAR";
                        break;
                    case "04":
                        MonthName="APR";
                        break;
                    case "05":
                        MonthName="MAY";
                        break;
                    case "06":
                        MonthName="JUN";
                        break;
                    case "07":
                        MonthName="JUL";
                        break;
                    case "08":
                        MonthName="AUG";
                        break;
                    case "09":
                        MonthName="SEP";
                        break;
                    case "10":
                        MonthName="OCT";
                        break;
                    case "11":
                        MonthName="NOV";
                        break;
                    case "12":
                        MonthName="DEC";
                        break;
                }

                final Calendar c1 = Calendar.getInstance();

                month = Integer.parseInt(MonthId);
                year   = Integer.parseInt(yearid);

                fromdate = "01-" + MonthName + "-" + yearid;

                try
                {
                   Date date1=new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
                    c1.setTime(date1);
                    int maxDd = c1.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String s = String.valueOf(maxDd);
                    todate =  s+ "-" + MonthName + "-" + yearid;
                }
                catch (Exception e)
                { e.printStackTrace();}

                pickFromDate.setText(fromdate);
                pickToDate.setText(todate);
            }
            else
            {
                pickFromDate.setText(globalVariable.getAccountingStartDate());
                pickToDate.setText(globalVariable.getAccountingEndDate());
            }
        }

        if (callsource.equals("VOUCHERSTATISTICS"))
        {
            vouchertypename = i.getStringExtra("vouchertypename");
            fromdate = i.getStringExtra("fromdate");
            todate = i.getStringExtra("todate");

            if ((null==vouchertypename) || ("".equals(vouchertypename)))
            {
                vouchertypename="";
            }

            if ((null!=fromdate) || (!"".equals(fromdate)) || (!"".equals(todate))|| (!"".equals(todate))) {
                pickFromDate.setText(fromdate);
                pickToDate.setText(todate);
            }
            else{
                pickFromDate.setText(globalVariable.getAccountingStartDate());
                pickToDate.setText(globalVariable.getAccountingEndDate());
            }
        }

        invokeWS_FA_Ledgerwise_Voucher_List();

    }

    public void invokeWS_FA_Ledgerwise_Voucher_List() {

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
            paramsMap.put("vouchertypename", vouchertypename);
            paramsMap.put("groupname",groupname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText("LEDGERWISE VOUCHERS");

            ApiHelper.post(URL + "Service1.asmx/FA_Ledgerwise_Voucher_List", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                        TableRow tblrowHeading = new TableRow(FA_LedgerMonthList.this);

                        TextView highsHeading_Date = initPlainHeaderTextView();
                        highsHeading_Date.setText("Date");
                        highsHeading_Date.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Date.setGravity(Gravity.CENTER);

                        TextView highsHeading_Particulars = initPlainHeaderTextView();
                        highsHeading_Particulars.setText("Particulars");
                        highsHeading_Particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Particulars.setGravity(Gravity.LEFT);

                        TextView highsHeading_Vch_Type = initPlainHeaderTextView();
                        highsHeading_Vch_Type.setText("Vch Type");
                        highsHeading_Vch_Type.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Vch_Type.setGravity(Gravity.LEFT);

                        TextView highsHeading_Vch_No = initPlainHeaderTextView();
                        highsHeading_Vch_No.setText("Vch No");
                        highsHeading_Vch_No.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Vch_No.setGravity(Gravity.LEFT);

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

                        tblrowHeading.addView(highsHeading_Date);
                        tblrowHeading.addView(highsHeading_Particulars);
                        tblrowHeading.addView(highsHeading_Vch_Type);
                        tblrowHeading.addView(highsHeading_Vch_No);
                        tblrowHeading.addView(highsHeading_Debit);
                        tblrowHeading.addView(highsheading_Credit);
                        tblrowHeading.addView(highsheading_Closing_Balance);

                        tabletoplocationstock.addView(tblrowHeading);

                        double drAmt= 0.00, crAmt= 0.00, f_Debit= 0.00, f_Credit= 0.00, balanceamount= 0.00, sngrunningBal= 0.00;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_ledgerwise_voucher_list");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {

                                JSONObject jsonObject = new_array.getJSONObject(i);

                                drAmt= jsonObject.getDouble("debit");
                                crAmt= jsonObject.getDouble("credit");
                                balanceamount= jsonObject.getDouble("balanceamount");

                                if (balanceamount < 0)
                                {sngrunningBal = balanceamount * (-1);}
                                else
                                {sngrunningBal = balanceamount;}

                                sngDrtotal = sngDrtotal + drAmt;
                                sngCrtotal = sngCrtotal + crAmt;

                                String accode = "";
                               // accode = jsonObject.getString("accode");

                                TableRow tblrowLabels = new TableRow(FA_LedgerMonthList.this);

                                final TextView highsLabel_voucherdt = initPlainTextView(i);
                                highsLabel_voucherdt.setText(jsonObject.getString("voucherdt"));
                                highsLabel_voucherdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_voucherdt.setGravity(Gravity.CENTER);

                                final TextView highsLabel_particulars = initPlainTextView(i);
                                highsLabel_particulars.setText(jsonObject.getString("particulars"));
                                highsLabel_particulars.setTypeface(Typeface.DEFAULT);
                                highsLabel_particulars.setGravity(Gravity.LEFT);

                                final TextView highsLabel_trndesc = initPlainTextView(i);
                                highsLabel_trndesc.setText(jsonObject.getString("trndesc"));
                                highsLabel_trndesc.setTypeface(Typeface.DEFAULT);
                                highsLabel_trndesc.setGravity(Gravity.LEFT);

                                final TextView highsLabel_refno = initPlainTextView(i);
                                highsLabel_refno.setText(jsonObject.getString("refno"));
                                highsLabel_refno.setTypeface(Typeface.DEFAULT);
                                highsLabel_refno.setGravity(Gravity.LEFT);

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

                                TextView highslabel_sngrunningBal = initPlainTextView(i);
                                highslabel_sngrunningBal.setText(String.format("%.2f",sngrunningBal) + " " + jsonObject.getString("drcr"));
                                highslabel_sngrunningBal.setTypeface(Typeface.DEFAULT);
                                highslabel_sngrunningBal.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_voucherdt);
                                tblrowLabels.addView(highsLabel_particulars);
                                tblrowLabels.addView(highsLabel_trndesc);
                                tblrowLabels.addView(highsLabel_refno);
                                tblrowLabels.addView(highsLabel_Debit);
                                tblrowLabels.addView(highslabel_Credit);
                                tblrowLabels.addView(highslabel_sngrunningBal);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        double f_Debit_total = 0, f_Credit_total = 0;

                        f_Debit_total = sngDrtotal;
                        f_Credit_total = sngCrtotal;

                        double f_tot_asset = 0;
                        double f_tot_liabilities = 0;

                        f_tot_asset = f_Debit_total;
                        f_tot_liabilities = f_Credit_total;

                        TableRow tblrowFooter = new TableRow(FA_LedgerMonthList.this);

                        TextView highsFooter_voucherdt = initPlainFooterTextView();
                        highsFooter_voucherdt.setText("");
                        highsFooter_voucherdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_voucherdt.setGravity(Gravity.LEFT);

                        TextView highsFooter_particulars = initPlainFooterTextView();
                        highsFooter_particulars.setText("GRAND TOTAL");
                        highsFooter_particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_particulars.setGravity(Gravity.LEFT);

                        TextView highsFooter_trndesc = initPlainFooterTextView();
                        highsFooter_trndesc.setText("");
                        highsFooter_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_trndesc.setGravity(Gravity.LEFT);

                        TextView highsFooter_refno = initPlainFooterTextView();
                        highsFooter_refno.setText("");
                        highsFooter_refno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_refno.setGravity(Gravity.LEFT);

                        TextView highsFooter_Debit = initPlainFooterTextView();
                        highsFooter_Debit.setText(String.format("%.2f",f_tot_asset));
                        highsFooter_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Debit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Credit = initPlainFooterTextView();
                        highsFooter_Credit.setText(String.format("%.2f",f_tot_liabilities));
                        highsFooter_Credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Credit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_sngrunningBal = initPlainFooterTextView();
                        highsFooter_sngrunningBal.setText(String.format("%.2f",sngrunningBal));
                        highsFooter_sngrunningBal.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_sngrunningBal.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_voucherdt);
                        tblrowFooter.addView(highsFooter_particulars);
                        tblrowFooter.addView(highsFooter_trndesc);
                        tblrowFooter.addView(highsFooter_refno);
                        tblrowFooter.addView(highsFooter_Debit);
                        tblrowFooter.addView(highsFooter_Credit);
                        tblrowFooter.addView(highsFooter_sngrunningBal);

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

        TextView textView = new TextView(FA_LedgerMonthList.this);
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
        TextView textView = new TextView(FA_LedgerMonthList.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_LedgerMonthList.this);
        textView.setPadding(10, 10, 10, 10);

        textView.setBackgroundColor(Color.parseColor("#142BAD"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(16);
        return textView;
    }

    @SuppressWarnings("deprecation")
    public void RefreshData(View view) {
        invokeWS_FA_Ledgerwise_Voucher_List();
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
        invokeWS_FA_Ledgerwise_Voucher_List();

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
        invokeWS_FA_Ledgerwise_Voucher_List();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_fa_ledgermonthlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ledger) {

        }
        else if (id == R.id.action_Download) {

        }
        return super.onOptionsItemSelected(item);
    }

}