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

public class OutstandingLocationActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    Button pickFromDate;
    Button pickToDate;

    ProgressDialog prgDialog;

    String name;
    int year, month, day;

    TextView header_customername;
    TableLayout tablesalesregister;

    TableLayout tableLocationos;
    TextView header_locationos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_outstanding_view);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,1));

        pickToDate = (Button) findViewById(R.id.pickToDate);
        pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));



        invokeWS_LocationOs();
    }

     public void invokeWS_LocationOs() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

            String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("sysuserno",sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_LocationCustomerOsSummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_locationos= (TextView) findViewById(R.id.header_locationos);
                        header_locationos.setText("Location Outstanding");

                        tableLocationos  = (TableLayout) findViewById(R.id.tableLocationos);
                        tableLocationos.removeAllViews();

                        tableLocationos.setStretchAllColumns(true);
                        tableLocationos.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(OutstandingLocationActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                        TableRow.LayoutParams params = new TableRow.LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(OutstandingLocationActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_cash = initPlainHeaderTextView();
                        highsHeading_cash.setText("Opening");
                        highsHeading_cash.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_cash.setGravity(Gravity.RIGHT);

                        TextView highsheading_cheque = initPlainHeaderTextView();
                        highsheading_cheque.setText("Debit");
                        highsheading_cheque.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_cheque.setGravity(Gravity.RIGHT);

                        TextView highsheading_creditcard = initPlainHeaderTextView();
                        highsheading_creditcard.setText("Credit");
                        highsheading_creditcard.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_creditcard.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_cash);
                        tblrowHeading.addView(highsheading_cheque);
                        tblrowHeading.addView(highsheading_creditcard);

                        tableLocationos.addView(tblrowHeading);

                        long fCASH;
                        long fCHEQUE;
                        long fCREDITCARD;
                        long fFINNANCE;

                        fCASH = 0;
                        fCHEQUE = 0;
                        fCREDITCARD = 0;
                        fFINNANCE = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("location_customer_outstanding");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(OutstandingLocationActivity.this);

                                final TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("LOCATION"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);
                                highsLabel_location.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                          Intent intent = new Intent(OutstandingLocationActivity.this, OutstandingCustomerActivity.class);
                                         intent.putExtra("locationname",highsLabel_location.getText());
                                         intent.putExtra("fromdate",pickFromDate.getText().toString());
                                         intent.putExtra("todate",pickToDate.getText().toString());
                                          startActivity(intent);
                                    }
                                });

                                TextView highsLabel_cash = initPlainTextView(i);
                                highsLabel_cash.setText(jsonObject.getString("OPENING"));
                                highsLabel_cash.setTypeface(Typeface.DEFAULT);
                                highsLabel_cash.setGravity(Gravity.RIGHT);

                                TextView highslabel_cheque = initPlainTextView(i);
                                highslabel_cheque.setText(jsonObject.getString("DEBIT"));
                                highslabel_cheque.setTypeface(Typeface.DEFAULT);
                                highslabel_cheque.setGravity(Gravity.RIGHT);

                                TextView highslabel_creditcard = initPlainTextView(i);
                                highslabel_creditcard.setText(jsonObject.getString("CREDIT"));
                                highslabel_creditcard.setTypeface(Typeface.DEFAULT);
                                highslabel_creditcard.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_cash);
                                tblrowLabels.addView(highslabel_cheque);
                                tblrowLabels.addView(highslabel_creditcard);

                                tableLocationos.addView(tblrowLabels);

                              //   fCASH += Long.valueOf(jsonObject.getString("OPENING"));
                                //  fCHEQUE += Long.valueOf(jsonObject.getString("DEBIT"));
                               //   fCREDITCARD += Long.valueOf(jsonObject.getString("CREDIT"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(OutstandingLocationActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fCASH));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(fCHEQUE));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra1 = initPlainFooterTextView();
                        highsFooter_ra1.setText(String.valueOf(fCREDITCARD));
                        highsFooter_ra1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra1.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);
                        tblrowFooter.addView(highsFooter_ra1);

                       // tableLocationos.addView(tblrowFooter);

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
                    // When Http response code is '500'


                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(OutstandingLocationActivity.this);
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
        TextView textView = new TextView(OutstandingLocationActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(OutstandingLocationActivity.this);
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
        invokeWS_LocationOs();
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
        invokeWS_LocationOs();
    }

}


