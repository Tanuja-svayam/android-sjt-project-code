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

public class FA_LedgerwiseBalance extends AppCompatActivity {

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
    String fromdate = "";
    String todate = "";
    String statementtype= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_ledgerwisebalance);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        groupname = i.getStringExtra("groupname");
        statementtype = i.getStringExtra("statementtype");
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
            groupname="0";
        }
        if ((null==statementtype) || ("".equals(statementtype))) {
            statementtype="";
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
        invokeWS_FA_Ledgerwise_Summary();

    }

    public void invokeWS_FA_Ledgerwise_Summary() {

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
            paramsMap.put("statementtype", statementtype);
            paramsMap.put("sysaccledgerno",groupname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText("LEDGERWISE BALANCE");

            ApiHelper.post(URL + "Service1.asmx/FA_Ledgerwise_Summary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                      // tabletoplocationstock.setStretchAllColumns(true);
                       // tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(FA_LedgerwiseBalance.this);

                        TextView highsHeading_Primary_Group = initPlainHeaderTextView();
                        highsHeading_Primary_Group.setText("Primary Group");
                        highsHeading_Primary_Group.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Primary_Group.setGravity(Gravity.LEFT);

                        TextView highsHeading_Parent_Group = initPlainHeaderTextView();
                        highsHeading_Parent_Group.setText("Parent Group");
                        highsHeading_Parent_Group.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Parent_Group.setGravity(Gravity.LEFT);

                        TextView highsHeading_Ledger = initPlainHeaderTextView();
                        highsHeading_Ledger.setText("Ledger");
                        highsHeading_Ledger.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Ledger.setGravity(Gravity.LEFT);

                        TextView highsHeading_Debit = initPlainHeaderTextView();
                        highsHeading_Debit.setText("Debit");
                        highsHeading_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Debit.setGravity(Gravity.RIGHT);

                        TextView highsheading_Credit = initPlainHeaderTextView();
                        highsheading_Credit.setText("Credit");
                        highsheading_Credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Credit.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_Primary_Group);
                        tblrowHeading.addView(highsHeading_Parent_Group);
                        tblrowHeading.addView(highsHeading_Ledger);
                        tblrowHeading.addView(highsHeading_Debit);
                        tblrowHeading.addView(highsheading_Credit);

                        tabletoplocationstock.addView(tblrowHeading);

                        double drAmt= 0.00, crAmt= 0.00, f_Debit= 0.00, f_Credit= 0.00, opbal= 0.00;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_ledgerbalance");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {

                                JSONObject jsonObject = new_array.getJSONObject(i);

                                drAmt= jsonObject.getDouble("cl_dr_amount");
                                crAmt= jsonObject.getDouble("cl_cr_amount");
                                opbal= jsonObject.getDouble("opbal");

                                if (opbal > 0)
                                {f_Debit = opbal;}
                                else
                                {f_Debit = 0;}

                                if (opbal < 0)
                                {f_Credit = opbal * (-1);}
                                else
                                {f_Credit = 0;}


                                sngDrtotal = sngDrtotal + drAmt;
                                sngCrtotal = sngCrtotal + crAmt;

                                String accode = "";
                                accode = jsonObject.getString("accode");

                                TableRow tblrowLabels = new TableRow(FA_LedgerwiseBalance.this);

                                final TextView highsLabel_Primary_Group = initPlainTextView(i);
                                highsLabel_Primary_Group.setText(jsonObject.getString("gname"));
                                highsLabel_Primary_Group.setTypeface(Typeface.DEFAULT);
                                highsLabel_Primary_Group.setGravity(Gravity.LEFT);

                                final TextView highsLabel_Parent_Group = initPlainTextView(i);
                                highsLabel_Parent_Group.setText(jsonObject.getString("gname_sub"));
                                highsLabel_Parent_Group.setTypeface(Typeface.DEFAULT);
                                highsLabel_Parent_Group.setGravity(Gravity.LEFT);

                                final TextView highsLabel_Ledger = initPlainTextView(i);
                                highsLabel_Ledger.setText(jsonObject.getString("name"));
                                highsLabel_Ledger.setTypeface(Typeface.DEFAULT);
                                highsLabel_Ledger.setGravity(Gravity.LEFT);

                                TextView highsLabel_Debit = initPlainTextView(i);
                                if (f_Debit>0)
                                {highsLabel_Debit.setText(String.format("%.2f",f_Debit));}
                                else {highsLabel_Debit.setText("");}
                                highsLabel_Debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_Debit.setGravity(Gravity.RIGHT);

                                TextView highslabel_Credit = initPlainTextView(i);
                                if (f_Credit>0)
                                {highslabel_Credit.setText(String.format("%.2f",f_Credit));}
                                else {highslabel_Credit.setText("");}
                                highslabel_Credit.setTypeface(Typeface.DEFAULT);
                                highslabel_Credit.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_Primary_Group);
                                tblrowLabels.addView(highsLabel_Parent_Group);
                                tblrowLabels.addView(highsLabel_Ledger);
                                tblrowLabels.addView(highsLabel_Debit);
                                tblrowLabels.addView(highslabel_Credit);

                                tblrowLabels.setClickable(true);

                                final String finalaccode = accode;

                                tblrowLabels.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(FA_LedgerwiseBalance.this, FA_LedgerMonthSummary.class);
                                        intent.putExtra("groupname",finalaccode);
                                        intent.putExtra("fromdate",pickFromDate.getText().toString());
                                        intent.putExtra("todate",pickToDate.getText().toString());
                                        startActivity(intent);
                                    }
                                });

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

                        TableRow tblrowFooter = new TableRow(FA_LedgerwiseBalance.this);

                        TextView highsFooter_Primary_Group = initPlainFooterTextView();
                        highsFooter_Primary_Group.setText("GRAND TOTAL");
                        highsFooter_Primary_Group.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Primary_Group.setGravity(Gravity.LEFT);

                        TextView highsFooter_Parent_Group = initPlainFooterTextView();
                        highsFooter_Parent_Group.setText("");
                        highsFooter_Parent_Group.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Parent_Group.setGravity(Gravity.LEFT);

                        TextView highsFooter_Ledger = initPlainFooterTextView();
                        highsFooter_Ledger.setText("");
                        highsFooter_Ledger.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Ledger.setGravity(Gravity.LEFT);

                        TextView highsFooter_Debit = initPlainFooterTextView();
                        highsFooter_Debit.setText(String.format("%.2f",f_tot_asset));
                        highsFooter_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Debit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Credit = initPlainFooterTextView();
                        highsFooter_Credit.setText(String.format("%.2f",f_tot_liabilities));
                        highsFooter_Credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Credit.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_Primary_Group);
                        tblrowFooter.addView(highsFooter_Parent_Group);
                        tblrowFooter.addView(highsFooter_Ledger);
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

        TextView textView = new TextView(FA_LedgerwiseBalance.this);
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
        TextView textView = new TextView(FA_LedgerwiseBalance.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_LedgerwiseBalance.this);
        textView.setPadding(10, 10, 10, 10);

        textView.setBackgroundColor(Color.parseColor("#142BAD"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(16);
        return textView;
    }

    @SuppressWarnings("deprecation")
    public void RefreshData(View view) {
        invokeWS_FA_Ledgerwise_Summary();
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
        invokeWS_FA_Ledgerwise_Summary();

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
        invokeWS_FA_Ledgerwise_Summary();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_fa_groupsummary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_group) {

        }  else if (id == R.id.action_ledgerwise) {
            Intent intent = new Intent(FA_LedgerwiseBalance.this, FA_LedgerwiseBalance.class);
            intent.putExtra("groupname",groupname);
            intent.putExtra("statementtype","");
            intent.putExtra("fromdate",pickFromDate.getText().toString());
            intent.putExtra("todate",pickToDate.getText().toString());
            startActivity(intent);
        }
        else if (id == R.id.action_Download) {

        }
        return super.onOptionsItemSelected(item);
    }

}