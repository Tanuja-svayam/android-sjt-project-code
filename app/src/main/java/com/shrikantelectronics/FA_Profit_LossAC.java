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

import java.io.StringWriter;
import java.util.Calendar;

public class FA_Profit_LossAC extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tableexpense;
    TableLayout tableincome;
    Button pickFromDate;
    Button pickToDate;


    ProgressDialog prgDialog;
    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;

    int year, month, day;

    public String ReportType="";

    public double sngOpStock= 0, sngCStock= 0, sngDrtotal= 0, sngCrtotal= 0, dBal = 0, gross_profit_loss_value= 0;

    String sGroupName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_profit_lossac);

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
        invokeWS_Profit_Loss_Account();

    }

    public void invokeWS_Profit_Loss_Account() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);


            ApiHelper.post(URL + "Service1.asmx/FA_ProfitLoss_Account", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tableexpense  = (TableLayout) findViewById(R.id.tableexpense);

                        tableexpense.setStretchAllColumns(true);
                        tableexpense.setColumnShrinkable(0,true);

                        TableRow tblrowHeading_expense = new TableRow(FA_Profit_LossAC.this);
                        TextView highsHeading_expense_description = initPlainHeaderTextView();
                        highsHeading_expense_description.setText("Expense");
                        highsHeading_expense_description.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_expense_description.setGravity(Gravity.LEFT);

                        TextView highsHeading_expense_closingbal = initPlainHeaderTextView();
                        highsHeading_expense_closingbal.setText("Amount");
                        highsHeading_expense_closingbal.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_expense_closingbal.setGravity(Gravity.CENTER);

                        tblrowHeading_expense.addView(highsHeading_expense_description);
                        tblrowHeading_expense.addView(highsHeading_expense_closingbal);

                        tableexpense.addView(tblrowHeading_expense);

                        Double fclosingbal_expense;
                        fclosingbal_expense = 0.00;


                        JSONObject obj = response;
                        JSONArray new_array_expense = obj.getJSONArray("fa_profitloss_account");

                        for (int i = 0, count = new_array_expense.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array_expense.getJSONObject(i);

                                String s =jsonObject.getString("groupname");
                                if(s.equals("Expenses"))
                                {
                                TableRow tblrowLabels_expense = new TableRow(FA_Profit_LossAC.this);

                                    String description =jsonObject.getString("description");
                                    String closingbal =jsonObject.getString("closingbal");
                                    String subledger =jsonObject.getString("subledger");

                                    StringWriter sw = new StringWriter();
                                    StringWriter swamount = new StringWriter();

                                    int n = Integer.valueOf(jsonObject.getString("lvl"));
                                    n = (n+1)*5;
                                    String sspace = String.format("%1$"+n+"s", "");

                                    sw.append(sspace);
                                    sw.append(description);

                                    swamount.append(closingbal);
                                    //swamount.append(sspace);

                                    if(subledger.equals("Y") && !description.equals("Gross Profit")) {
                                        fclosingbal_expense += Double.valueOf(jsonObject.getString("closingbal"));
                                    };


                                final TextView highsLabel_expense_description = initPlainTextView(i);
                                highsLabel_expense_description.setText(sw.toString());
                                highsLabel_expense_description.setTypeface(Typeface.DEFAULT);
                                highsLabel_expense_description.setGravity(Gravity.LEFT);


                                /*
                                highsLabel_expense_description.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(FA_Profit_LossAC_New.this, FA_Profit_LossAC_New_Category.class);
                                        intent.putExtra("description",highsLabel_expense_description.getText());
                                        startActivity(intent);
                                    }
                                });
*/
                                TextView highsLabel_expense_closingbal = initPlainTextView(i);
                                highsLabel_expense_closingbal.setText(swamount.toString());
                                highsLabel_expense_closingbal.setTypeface(Typeface.DEFAULT);
                                highsLabel_expense_closingbal.setGravity(Gravity.RIGHT);

                                    if(jsonObject.getString("lvl").equals("0")) {
                                        highsLabel_expense_description.setTypeface(highsLabel_expense_description.getTypeface(), Typeface.BOLD_ITALIC);

                                        highsLabel_expense_closingbal.setTypeface(highsLabel_expense_description.getTypeface(), Typeface.BOLD_ITALIC);
                                        highsLabel_expense_closingbal.setPaintFlags(highsLabel_expense_closingbal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    }

                                tblrowLabels_expense.addView(highsLabel_expense_description);
                                tblrowLabels_expense.addView(highsLabel_expense_closingbal);

                                tblrowLabels_expense.setClickable(true);

                                tableexpense.addView(tblrowLabels_expense);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter_expense = new TableRow(FA_Profit_LossAC.this);
                        TextView highsFooter_expense_description = initPlainFooterTextView();
                        highsFooter_expense_description.setText("Total");
                        highsFooter_expense_description.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_expense_description.setGravity(Gravity.LEFT);

                        TextView highsFooter_expense_closingbal = initPlainFooterTextView();
                        highsFooter_expense_closingbal.setText(String.format("%.2f",fclosingbal_expense));
                        highsFooter_expense_closingbal.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_expense_closingbal.setGravity(Gravity.RIGHT);

                        tblrowFooter_expense.addView(highsFooter_expense_description);
                        tblrowFooter_expense.addView(highsFooter_expense_closingbal);

                        tableexpense.addView(tblrowFooter_expense);



//--------------------------------------------------



                        tableincome  = (TableLayout) findViewById(R.id.tableincome);

                        tableincome.setStretchAllColumns(true);
                        tableincome.setColumnShrinkable(0,true);

                        TableRow tblrowHeading_income = new TableRow(FA_Profit_LossAC.this);
                        TextView highsHeading_income_description = initPlainHeaderTextView();
                        highsHeading_income_description.setText("Income");
                        highsHeading_income_description.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_income_description.setGravity(Gravity.LEFT);

                        TextView highsHeading_income_closingbal = initPlainHeaderTextView();
                        highsHeading_income_closingbal.setText("Amount");
                        highsHeading_income_closingbal.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_income_closingbal.setGravity(Gravity.CENTER);

                        tblrowHeading_income.addView(highsHeading_income_description);
                        tblrowHeading_income.addView(highsHeading_income_closingbal);

                        tableincome.addView(tblrowHeading_income);

                        Double fclosingbal_income;
                        fclosingbal_income = 0.00;

                        JSONArray new_array_income = obj.getJSONArray("fa_profitloss_account");

                        for (int i = 0, count = new_array_income.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array_income.getJSONObject(i);
                                String s =jsonObject.getString("groupname");
                                if(s.equals("Income")) {
                                    TableRow tblrowLabels_income = new TableRow(FA_Profit_LossAC.this);

                                    String description =jsonObject.getString("description");
                                    String closingbal =jsonObject.getString("closingbal");
                                    String subledger =jsonObject.getString("subledger");

                                    StringWriter sw = new StringWriter();
                                    StringWriter swamount = new StringWriter();

                                    int n = Integer.valueOf(jsonObject.getString("lvl"));
                                    n = (n+1)*5;
                                    String sspace = String.format("%1$"+n+"s", "");

                                    sw.append(sspace);
                                    sw.append(description);

                                    swamount.append(closingbal);
                                  //  swamount.append(sspace);

                                    if(subledger.equals("Y") && !description.equals("Gross Profit")) {
                                        fclosingbal_income += Double.valueOf(jsonObject.getString("closingbal"));
                                    };

                                    final TextView highsLabel_income_description = initPlainTextView(i);
                                    highsLabel_income_description.setText(sw.toString());
                                    highsLabel_income_description.setTypeface(Typeface.DEFAULT);
                                    highsLabel_income_description.setGravity(Gravity.LEFT);
                                /*
                                highsLabel_income_description.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(FA_Profit_LossAC_New.this, FA_Profit_LossAC_New_Category.class);
                                        intent.putExtra("description",highsLabel_income_description.getText());
                                        startActivity(intent);
                                    }
                                });
*/
                                    TextView highsLabel_income_closingbal = initPlainTextView(i);
                                    highsLabel_income_closingbal.setText(swamount.toString());
                                    highsLabel_income_closingbal.setTypeface(Typeface.DEFAULT);
                                    highsLabel_income_closingbal.setGravity(Gravity.RIGHT);

                                    if(jsonObject.getString("lvl").equals("0")) {
                                        highsLabel_income_description.setTypeface(highsLabel_income_description.getTypeface(), Typeface.BOLD_ITALIC);

                                        highsLabel_income_closingbal.setTypeface(highsLabel_income_description.getTypeface(), Typeface.BOLD_ITALIC);
                                        highsLabel_income_closingbal.setPaintFlags(highsLabel_income_closingbal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    }

                                    tblrowLabels_income.addView(highsLabel_income_description);
                                    tblrowLabels_income.addView(highsLabel_income_closingbal);

                                    tblrowLabels_income.setClickable(true);

                                    tableincome.addView(tblrowLabels_income);



                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter_income = new TableRow(FA_Profit_LossAC.this);
                        TextView highsFooter_income_description = initPlainFooterTextView();
                        highsFooter_income_description.setText("Total");
                        highsFooter_income_description.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_income_description.setGravity(Gravity.LEFT);

                        TextView highsFooter_income_closingbal = initPlainFooterTextView();
                        highsFooter_income_closingbal.setText(String.format("%.2f",fclosingbal_income));
                        highsFooter_income_closingbal.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_income_closingbal.setGravity(Gravity.RIGHT);

                        tblrowFooter_income.addView(highsFooter_income_description);
                        tblrowFooter_income.addView(highsFooter_income_closingbal);

                        tableincome.addView(tblrowFooter_income);




                        //    LinearLayout sv = new LinearLayout(MainActivity.this);

                        //        sv.addView(table);

                        //hsw.addView(sv);
                        //setContentView(hsw);

                        // setContentView(table);

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

        TextView textView = new TextView(FA_Profit_LossAC.this);
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
        TextView textView = new TextView(FA_Profit_LossAC.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_Profit_LossAC.this);
        textView.setPadding(10, 10, 10, 10);

        textView.setBackgroundColor(Color.parseColor("#142BAD"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setTextSize(16);
        return textView;
    }

    @SuppressWarnings("deprecation")
    public void RefreshData(View view) {
        invokeWS_Profit_Loss_Account();
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
        invokeWS_Profit_Loss_Account();
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
        invokeWS_Profit_Loss_Account();
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
            invokeWS_Profit_Loss_Account();
        } else if (id == R.id.action_detailed) {
            ReportType="Detailed";
            invokeWS_Profit_Loss_Account();
        } else if (id == R.id.action_ledgerwise) {
            Intent intent = new Intent(FA_Profit_LossAC.this, FA_LedgerwiseBalance.class);
            intent.putExtra("groupname","");
            intent.putExtra("statementtype","PL");
            intent.putExtra("fromdate",pickFromDate.getText().toString());
            intent.putExtra("todate",pickToDate.getText().toString());
            startActivity(intent);
        }else if (id == R.id.action_Download) {

        }
        return super.onOptionsItemSelected(item);
    }
}