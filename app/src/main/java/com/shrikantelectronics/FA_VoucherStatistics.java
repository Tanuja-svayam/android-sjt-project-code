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

public class FA_VoucherStatistics extends AppCompatActivity {

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

    public double dBal = 0;

    String sGroupName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_voucherstatistics);

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

       invokeWS_FA_VoucherStatistics();

    }

      public void invokeWS_FA_VoucherStatistics() {

        try {

            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            dBal = 0;

            sGroupName = "";

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/FA_VoucherStatistics", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                        TableRow tblrowHeading = new TableRow(FA_VoucherStatistics.this);
                        TextView highsHeading_Particulars = initPlainHeaderTextView();
                        highsHeading_Particulars.setText("Types Of Vouchers");
                        highsHeading_Particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Particulars.setGravity(Gravity.LEFT);

                        TextView highsHeading_No_Of_Vouchers = initPlainHeaderTextView();
                        highsHeading_No_Of_Vouchers.setText("No Of Vouchers");
                        highsHeading_No_Of_Vouchers.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_No_Of_Vouchers.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_Particulars);
                        tblrowHeading.addView(highsHeading_No_Of_Vouchers);

                        tabletoplocationstock.addView(tblrowHeading);

                        double no_of_vouchers = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("fa_voucherstatistics");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {

                                JSONObject jsonObject = new_array.getJSONObject(i);
                                no_of_vouchers= jsonObject.getDouble("no_of_vouchers");
                                dBal = dBal +no_of_vouchers;

                                TableRow tblrowLabels = new TableRow(FA_VoucherStatistics.this);

                                final TextView highsLabel_Particulars = initPlainTextView(i);
                                highsLabel_Particulars.setText(jsonObject.getString("vouchertypename"));
                                highsLabel_Particulars.setTypeface(Typeface.DEFAULT);
                                highsLabel_Particulars.setGravity(Gravity.LEFT);

                                TextView highsLabel_Debit = initPlainTextView(i);
                                if (no_of_vouchers!=0)
                                {highsLabel_Debit.setText(String.format("%.0f",no_of_vouchers));}
                                else {highsLabel_Debit.setText("");}
                                highsLabel_Debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_Debit.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_Particulars);
                                tblrowLabels.addView(highsLabel_Debit);

                                tblrowLabels.setClickable(true);
                                tblrowLabels.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(FA_VoucherStatistics.this, FA_LedgerMonthList.class);

                                        intent.putExtra("callsource","VOUCHERSTATISTICS");
                                        intent.putExtra("vouchertypename",highsLabel_Particulars.getText());
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

                        TableRow tblrowFooter = new TableRow(FA_VoucherStatistics.this);
                        TextView highsFooter_Particulars = initPlainFooterTextView();
                        highsFooter_Particulars.setText("TOTAL");
                        highsFooter_Particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Particulars.setGravity(Gravity.LEFT);

                        TextView highsFooter_Debit = initPlainFooterTextView();
                        highsFooter_Debit.setText(String.format("%.2f",dBal));
                        highsFooter_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Debit.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_Particulars);
                        tblrowFooter.addView(highsFooter_Debit);

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

        TextView textView = new TextView(FA_VoucherStatistics.this);
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
        TextView textView = new TextView(FA_VoucherStatistics.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_VoucherStatistics.this);
        textView.setPadding(10, 10, 10, 10);

        textView.setBackgroundColor(Color.parseColor("#142BAD"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(16);
        return textView;
    }

    @SuppressWarnings("deprecation")
    public void RefreshData(View view) {
        invokeWS_FA_VoucherStatistics();
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
        invokeWS_FA_VoucherStatistics();
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
        invokeWS_FA_VoucherStatistics();
    }

}