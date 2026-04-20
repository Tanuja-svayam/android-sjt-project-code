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

public class FinanceRecoveryScheduleActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    String name;

    TextView header_customername;
   // TableLayout tablesalesregister;

    TableLayout tablesalesregister;
    TextView header_salesregister;
    Button pickFromDate;
    String  FromDate;
    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;

    int year, month, day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_recovery_schedule_view);


        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        FromDate=Utility.ConvetToDDMMMYYYY(year,month,day);

        invokeWS_customerOs();
    }

    public void invokeWS_customerOs() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            String sysuserno = globalVariable.getSysuserno();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);


            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickFromDate.getText().toString());

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_FinanceRecovery_Schedule", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {



                        header_salesregister= (TextView) findViewById(R.id.header_salesregister);
                        header_salesregister.setText("customer Outstanding");

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        //tablesalesregister.setShrinkAllColumns(true);

                       // TableRow rowTitle = new TableRow(B2BOutstandingcustomerActivity.this);
                       // rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                       // TableRow.LayoutParams params = new TableRow.LayoutParams();
                       // params.span = 6;

                        TableRow tblrowHeading = new TableRow(FinanceRecoveryScheduleActivity.this);
                        TextView highsHeading_customer = initPlainHeaderTextView();
                        highsHeading_customer.setText("Customer");
                        highsHeading_customer.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_customer.setGravity(Gravity.LEFT);

                        TextView highsHeading_loanamt = initPlainHeaderTextView();
                        highsHeading_loanamt.setText("Loan Amount");
                        highsHeading_loanamt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_loanamt.setGravity(Gravity.RIGHT);

                        TextView highsHeading_overdue = initPlainHeaderTextView();
                        highsHeading_overdue.setText("Overdue");
                        highsHeading_overdue.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_overdue.setGravity(Gravity.RIGHT);
                        highsHeading_overdue.setBackgroundColor(getResources().getColor(R.color.col_30));

                        TextView highsHeading_balamt = initPlainHeaderTextView();
                        highsHeading_balamt.setText("Today' Due");
                        highsHeading_balamt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_balamt.setGravity(Gravity.RIGHT);
                        highsHeading_balamt.setBackgroundColor(getResources().getColor(R.color.col_45));

                        TextView highsHeading_total = initPlainHeaderTextView();
                        highsHeading_total.setText("Total Due");
                        highsHeading_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_total.setGravity(Gravity.RIGHT);
                        highsHeading_total.setBackgroundColor(getResources().getColor(R.color.col_60));

                        tblrowHeading.addView(highsHeading_customer);
                        tblrowHeading.addView(highsHeading_loanamt);
                        tblrowHeading.addView(highsHeading_overdue);
                        tblrowHeading.addView(highsHeading_balamt);
                        tblrowHeading.addView(highsHeading_total);

                        tablesalesregister.addView(tblrowHeading);

                        double floanamt;
                        double foverdue;
                        double fbalamt;
                        double ftotal;
                        double frowtotal;

                        floanamt=0;
                        foverdue= 0;
                        fbalamt= 0;
                        ftotal= 0;
                        frowtotal= 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("receivable_finance_emi_schedule");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(FinanceRecoveryScheduleActivity.this);

                                final String did;

                                final String sysloanno;
                                final String custcd;


                                sysloanno=jsonObject.getString("sysloanno");
                                custcd=jsonObject.getString("custcd");

                                floanamt+= Double.valueOf(jsonObject.getString("loanamt"));
                                foverdue+= Double.valueOf(jsonObject.getString("overdue"));
                                fbalamt+= Double.valueOf(jsonObject.getString("balamt"));
                                ftotal+= foverdue + fbalamt ;
                                frowtotal = foverdue + fbalamt ;

                                final TextView highsLabel_description = initPlainTextView(i);
                                highsLabel_description.setText(jsonObject.getString("custname"));
                                highsLabel_description.setTypeface(Typeface.DEFAULT);
                                highsLabel_description.setGravity(Gravity.LEFT);

                                TextView highslabel_loanamt = initPlainTextView(i);
                                highslabel_loanamt.setText(jsonObject.getString("loanamt"));
                                highslabel_loanamt.setTypeface(Typeface.DEFAULT);
                                highslabel_loanamt.setGravity(Gravity.RIGHT);

                                TextView highslabel_overdue = initPlainTextView(i);
                                highslabel_overdue.setText(jsonObject.getString("overdue"));
                                highslabel_overdue.setTypeface(Typeface.DEFAULT);
                                highslabel_overdue.setGravity(Gravity.RIGHT);
                                highslabel_overdue.setBackgroundColor(getResources().getColor(R.color.col_30));

                                TextView highslabel_balamt = initPlainTextView(i);
                                highslabel_balamt.setText(jsonObject.getString("balamt"));
                                highslabel_balamt.setTypeface(Typeface.DEFAULT);
                                highslabel_balamt.setGravity(Gravity.RIGHT);
                                highslabel_balamt.setBackgroundColor(getResources().getColor(R.color.col_45));

                                TextView highslabel_total = initPlainTextView(i);
                                highslabel_total.setText(Utility.DoubleToString(frowtotal));
                                highslabel_total.setTypeface(Typeface.DEFAULT);
                                highslabel_total.setGravity(Gravity.RIGHT);
                                highslabel_total.setBackgroundColor(getResources().getColor(R.color.col_60));

                                tblrowLabels.addView(highsLabel_description);
                                tblrowLabels.addView(highslabel_loanamt);
                                tblrowLabels.addView(highslabel_overdue);
                                tblrowLabels.addView(highslabel_balamt);
                                tblrowLabels.addView(highslabel_total);

                                tblrowLabels.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(FinanceRecoveryScheduleActivity.this, customer_view_single_for_loan.class);

                                        intent.putExtra("sysloanno", sysloanno);
                                        intent.putExtra("custcd", custcd);
                                        intent.putExtra("fromdate", FromDate);
                                        startActivity(intent);
                                    }
                                });

                                tablesalesregister.addView(tblrowLabels);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(FinanceRecoveryScheduleActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_loanamt = initPlainFooterTextView();
                        highsFooter_loanamt.setText(Utility.DoubleToString(floanamt));
                        highsFooter_loanamt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_loanamt.setGravity(Gravity.RIGHT);

                        TextView highsFooter_overdue = initPlainFooterTextView();
                        highsFooter_overdue.setText(Utility.DoubleToString(foverdue));
                        highsFooter_overdue.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_overdue.setGravity(Gravity.RIGHT);
                        highsFooter_overdue.setBackgroundColor(getResources().getColor(R.color.col_30));

                        TextView highsFooter_balamt = initPlainFooterTextView();
                        highsFooter_balamt.setText(Utility.DoubleToString(fbalamt));
                        highsFooter_balamt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_balamt.setGravity(Gravity.RIGHT);
                        highsFooter_balamt.setBackgroundColor(getResources().getColor(R.color.col_45));

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText(Utility.DoubleToString(ftotal));
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.RIGHT);
                        highsFooter_total.setBackgroundColor(getResources().getColor(R.color.col_60));

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_loanamt);
                        tblrowFooter.addView(highsFooter_overdue);
                        tblrowFooter.addView(highsFooter_balamt);
                        tblrowFooter.addView(highsFooter_total);
                        tablesalesregister.addView(tblrowFooter);

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

        TextView textView = new TextView(FinanceRecoveryScheduleActivity.this);
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
        TextView textView = new TextView(FinanceRecoveryScheduleActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FinanceRecoveryScheduleActivity.this);
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



    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    fromDateListener, year, month, day);
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
        FromDate=Utility.ConvetToDDMMMYYYY(year,month,day);
        invokeWS_customerOs();
    }


}


