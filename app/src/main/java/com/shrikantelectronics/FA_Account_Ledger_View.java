package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FA_Account_Ledger_View extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    Button pickFromDate;
    Button pickToDate;

    ProgressDialog prgDialog;
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> customerlist;

    ArrayList<Account_Ledger> arraylist = new ArrayList<Account_Ledger>();

    String sysaccledgerno;
    String name;
    int year, month, day;
    String fromdate;
    String todate;

    TextView header_accountledgername;
    TableLayout tableaccountledger;

    String FileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_account_ledger_view);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();

        sysaccledgerno = i.getStringExtra("sysaccledgerno");
        name = i.getStringExtra("accountledgername");

        fromdate = i.getStringExtra("fromdate");
        todate = i.getStringExtra("todate");

        header_accountledgername = (TextView) findViewById(R.id.header_accountledgername);
        header_accountledgername.setText(name);

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
        invokeWS_Account_Ledger(sysaccledgerno);
    }

    public void invokeWS_Account_Ledger(String dsysaccledgerno){
        try {

            Map<String, String> paramsMap = new HashMap<>();
            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("sysaccledgerno", dsysaccledgerno);
            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetAccountLedger", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tableaccountledger  = (TableLayout) findViewById(R.id.tableaccountledger);
                        tableaccountledger.removeAllViews();

                        tableaccountledger.setStretchAllColumns(true);
                      //  tableaccountledger.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(FA_Account_Ledger_View.this);
                        TextView highsHeading_voucherdt = initPlainHeaderTextView();
                        highsHeading_voucherdt.setText("Date");
                        highsHeading_voucherdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_voucherdt.setGravity(Gravity.CENTER);

                        TextView highsHeading_particulars = initPlainHeaderTextView();
                        highsHeading_particulars.setText("Particulars");
                        highsHeading_particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_particulars.setGravity(Gravity.CENTER);

                        TextView highsHeading_trndesc = initPlainHeaderTextView();
                        highsHeading_trndesc.setText("Vch Type");
                        highsHeading_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_trndesc.setGravity(Gravity.LEFT);

                        TextView highsHeading_refno = initPlainHeaderTextView();
                        highsHeading_refno.setText("Vch No.");
                        highsHeading_refno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_refno.setGravity(Gravity.LEFT);

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
                        tblrowHeading.addView(highsHeading_particulars);
                        tblrowHeading.addView(highsHeading_trndesc);
                        tblrowHeading.addView(highsHeading_refno);
                        tblrowHeading.addView(highsHeading_debit);
                        tblrowHeading.addView(highsHeading_credit);
                        tblrowHeading.addView(highsHeading_balance);

                        tableaccountledger.addView(tblrowHeading);

                        double drAmt= 0.00, crAmt= 0.00, fCredit_total= 0, fDebit_total = 0, fbal=0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("accountledger");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                drAmt = jsonObject.getDouble("debit");
                                crAmt=jsonObject.getDouble("credit");
                                fbal=jsonObject.getDouble("balanceamount");
                                final String sysaccledgerno;

                                sysaccledgerno=jsonObject.getString("sysaccledgerno");

                                TableRow tblrowLabels = new TableRow(FA_Account_Ledger_View.this);

                                TextView highsLabel_voucherdt = initPlainTextView(i);
                                highsLabel_voucherdt.setText(jsonObject.getString("voucherdt"));
                                highsLabel_voucherdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_voucherdt.setGravity(Gravity.CENTER);

                                TextView highsLabel_particulars = initPlainTextView(i);
                                highsLabel_particulars.setText(jsonObject.getString("particulars"));
                                highsLabel_particulars.setTypeface(Typeface.DEFAULT);
                                highsLabel_particulars.setGravity(Gravity.LEFT);

                                TextView highsLabel_trndesc = initPlainTextView(i);
                                highsLabel_trndesc.setText(jsonObject.getString("trndesc"));
                                highsLabel_trndesc.setTypeface(Typeface.DEFAULT);
                                highsLabel_trndesc.setGravity(Gravity.LEFT);

                                TextView highsLabel_refno = initPlainTextView(i);
                                highsLabel_refno.setText(jsonObject.getString("refno"));
                                highsLabel_refno.setTypeface(Typeface.DEFAULT);
                                highsLabel_refno.setGravity(Gravity.LEFT);

                                TextView highsLabel_debit= initPlainTextView(i);
                                highsLabel_debit.setText(jsonObject.getString("debit"));
                                highsLabel_debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_debit.setGravity(Gravity.RIGHT);

                                TextView highsLabel_credit = initPlainTextView(i);
                                highsLabel_credit.setText(jsonObject.getString("credit"));
                                highsLabel_credit.setTypeface(Typeface.DEFAULT);
                                highsLabel_credit.setGravity(Gravity.RIGHT);

                                TextView highsLabel_balance = initPlainTextView(i);
                                highsLabel_balance.setText(jsonObject.getString("balanceamount"));
                                highsLabel_balance.setTypeface(Typeface.DEFAULT);
                                highsLabel_balance.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_voucherdt);
                                tblrowLabels.addView(highsLabel_particulars);
                                tblrowLabels.addView(highsLabel_trndesc);
                                tblrowLabels.addView(highsLabel_refno);
                                tblrowLabels.addView(highsLabel_debit);
                                tblrowLabels.addView(highsLabel_credit);
                                tblrowLabels.addView(highsLabel_balance);

                                tableaccountledger.addView(tblrowLabels);

                                fDebit_total = fDebit_total + drAmt;
                                fCredit_total = fCredit_total + crAmt;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(FA_Account_Ledger_View.this);
                        TextView highsFooter_voucherdt = initPlainFooterTextView();
                        highsFooter_voucherdt.setText("");
                        highsFooter_voucherdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_voucherdt.setGravity(Gravity.LEFT);

                        TextView highsFooter_particulars = initPlainFooterTextView();
                        highsFooter_particulars.setText("TOTAL");
                        highsFooter_particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_particulars.setGravity(Gravity.CENTER);

                        TextView highsFooter_trndesc = initPlainFooterTextView();
                        highsFooter_trndesc.setText("");
                        highsFooter_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_trndesc.setGravity(Gravity.CENTER);

                        TextView highsFooter_refno = initPlainFooterTextView();
                        highsFooter_refno.setText("");
                        highsFooter_refno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_refno.setGravity(Gravity.CENTER);

                        TextView highsFooter_debit = initPlainFooterTextView();
                        highsFooter_debit.setText(String.format("%.2f",fDebit_total));
                        highsFooter_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_debit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_credit = initPlainFooterTextView();
                        highsFooter_credit.setText(String.format("%.2f",fCredit_total));
                        highsFooter_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_credit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_balance = initPlainFooterTextView();
                        highsFooter_balance.setText(String.format("%.2f",fbal));
                        highsFooter_balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_balance.setGravity(Gravity.CENTER);

                        tblrowFooter.addView(highsFooter_voucherdt);
                        tblrowFooter.addView(highsFooter_particulars);
                        tblrowFooter.addView(highsFooter_trndesc);
                        tblrowFooter.addView(highsFooter_refno);
                        tblrowFooter.addView(highsFooter_debit);
                        tblrowFooter.addView(highsFooter_credit);
                        tblrowFooter.addView(highsFooter_balance);

                        tableaccountledger.addView(tblrowFooter);

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

        TextView textView = new TextView(FA_Account_Ledger_View.this);
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
        TextView textView = new TextView(FA_Account_Ledger_View.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_Account_Ledger_View.this);
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
        invokeWS_Account_Ledger(sysaccledgerno);
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
        invokeWS_Account_Ledger(sysaccledgerno);
    }


    public void downloadledger(View v)
    {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysaccledgerno)) {
            // When Email entered is Valid


            //    sysaccledgerno = i.getStringExtra("sysaccledgerno");
            ///  name = i.getStringExtra("name");

            String fromdate = "";
            String todate = "";

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileName = "Ledger" + timeStamp + ".pdf";

            paramsMap.put("sysaccledgerno",sysaccledgerno);
            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("FileName",FileName);

            invokeCreateCustomerLedgerWS(paramsMap);

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeCreateCustomerLedgerWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GenerateCustomerLedgerPDF", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;



                    try {
                        String fileUrl = Config.WEBSERVICE_URL + "MobileCustomerLedger/" + response.getString("error_msg");
                        Utility.downloadAndOpenPdf(FA_Account_Ledger_View.this, fileUrl,FileName);
                        finish();
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(FA_Account_Ledger_View.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                    }

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
                prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }



}


