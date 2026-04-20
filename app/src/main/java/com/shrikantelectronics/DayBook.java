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

public class DayBook extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    Button pickFromDate;
    Button pickToDate;

    ProgressDialog prgDialog;

    String name;
    int year, month, day;

    TextView header_customername;
    TableLayout tablesalesregister;

    TableLayout tabledateos;
    TextView header_dateos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daybook_view);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        pickToDate = (Button) findViewById(R.id.pickToDate);
        pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        invokeWS_DayBook();
    }

    public void invokeWS_DayBook() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

            String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("companycd", "");
            paramsMap.put("sysvouchertypeno", "");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_DayBook", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_dateos= (TextView) findViewById(R.id.header_locationos);
                        header_dateos.setText("Day Book");

                        tabledateos  = (TableLayout) findViewById(R.id.tableLocationos);
                        tabledateos.removeAllViews();

                        tabledateos.setStretchAllColumns(true);
                        tabledateos.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(DayBook.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        TableRow.LayoutParams params = new TableRow.LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(DayBook.this);
                        TextView highsHeading_date = initPlainHeaderTextView();
                        highsHeading_date.setText("Date");
                        highsHeading_date.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_date.setGravity(Gravity.LEFT);

                        TextView highsHeading_particulars = initPlainHeaderTextView();
                        highsHeading_particulars.setText("Particular");
                        highsHeading_particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_particulars.setGravity(Gravity.RIGHT);

                        TextView highsheading_debit = initPlainHeaderTextView();
                        highsheading_debit.setText("Debit");
                        highsheading_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_debit.setGravity(Gravity.RIGHT);

                        TextView highsheading_credit = initPlainHeaderTextView();
                        highsheading_credit.setText("Credit");
                        highsheading_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_credit.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_date);
                        tblrowHeading.addView(highsHeading_particulars);
                        tblrowHeading.addView(highsheading_debit);
                        tblrowHeading.addView(highsheading_credit);

                        tabledateos.addView(tblrowHeading);

                        long fdebit;
                        long fcredit;

                        fdebit = 0;
                        fcredit = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("bi_daybook");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(DayBook.this);

                                final TextView highsLabel_date = initPlainTextView(i);
                                highsLabel_date.setText(jsonObject.getString("vtrndate"));
                                highsLabel_date.setTypeface(Typeface.DEFAULT);
                                highsLabel_date.setGravity(Gravity.CENTER);

                                TextView highsLabel_particulars = initPlainTextView(i);
                                highsLabel_particulars.setText(jsonObject.getString("particular"));
                                highsLabel_particulars.setTypeface(Typeface.DEFAULT);
                                highsLabel_particulars.setGravity(Gravity.LEFT);

                                TextView highslabel_debit = initPlainTextView(i);
                                highslabel_debit.setText(jsonObject.getString("dr_amount"));
                                highslabel_debit.setTypeface(Typeface.DEFAULT);
                                highslabel_debit.setGravity(Gravity.RIGHT);

                                TextView highslabel_credit = initPlainTextView(i);
                                highslabel_credit.setText(jsonObject.getString("cr_amount"));
                                highslabel_credit.setTypeface(Typeface.DEFAULT);
                                highslabel_credit.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_date);
                                tblrowLabels.addView(highsLabel_particulars);
                                tblrowLabels.addView(highslabel_debit);
                                tblrowLabels.addView(highslabel_credit);

                                tabledateos.addView(tblrowLabels);

                              //   fparticulars += Long.valueOf(jsonObject.getString("OPENING"));
                               //   fdebit += Long.valueOf(jsonObject.getString("DEBIT"));
                                 // fcredit += Long.valueOf(jsonObject.getString("CREDIT"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(DayBook.this);
                        TextView highsFooter_date = initPlainFooterTextView();
                        highsFooter_date.setText("Total");
                        highsFooter_date.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_date.setGravity(Gravity.LEFT);

                        TextView highsFooter_particulars = initPlainFooterTextView();
                        highsFooter_particulars.setText("");
                        highsFooter_particulars.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_particulars.setGravity(Gravity.RIGHT);

                        TextView highsFooter_debit = initPlainFooterTextView();
                        highsFooter_debit.setText(String.valueOf(fdebit));
                        highsFooter_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_debit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_credit = initPlainFooterTextView();
                        highsFooter_credit.setText(String.valueOf(fcredit));
                        highsFooter_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_credit.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_date);
                        tblrowFooter.addView(highsFooter_particulars);
                        tblrowFooter.addView(highsFooter_debit);
                        tblrowFooter.addView(highsFooter_credit);

                       // tabledateos.addView(tblrowFooter);

                    } catch (JSONException e) {
        // TODO Auto-generated catch block
        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
        e.printStackTrace();

        Toast.makeText(getApplicationContext(), "Status code :"+ e.toString() +"errmsg : "+e.getMessage(), Toast.LENGTH_LONG).show();
    }
}

    // When the response returned by REST has Http response code other than '200'
    @Override
    public void onFailure(String errorMessage) {
        // Hide Progress Dialog
        //     prgDialog.hide();
        // When Http response code is '404'
        Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

    }
});
        }catch (Exception e) {
        e.printStackTrace();
        //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(DayBook.this);
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
        TextView textView = new TextView(DayBook.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(DayBook.this);
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
        invokeWS_DayBook();
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
        invokeWS_DayBook();
    }

}


