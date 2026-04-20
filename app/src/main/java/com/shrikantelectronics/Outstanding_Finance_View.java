package com.shrikantelectronics;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Outstanding_Finance_View extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ListViewAdapter_Customer_Ledger adapter;

    Button pickFromDate;
    Button pickToDate;

    ProgressDialog prgDialog;
    private ListView lv_customer ;
    EditText inputSearch_customer ;
    ArrayList<HashMap<String, String>> customerlist;

    ArrayList<Customer_Ledger> arraylist = new ArrayList<Customer_Ledger>();

    TextView header_customername;
    TableLayout tablesalesregister;

    String fromdate, todate, companycd, companyname, fincompanycd, financelocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_outstanding_view);

        Intent i = getIntent();

        fromdate = i.getStringExtra("fromdate");
        todate = i.getStringExtra("todate");
        companycd = i.getStringExtra("companycd");
        companyname = i.getStringExtra("companyname");
        fincompanycd = i.getStringExtra("fincompanycd");
        financelocation = i.getStringExtra("financelocation");

        header_customername = (TextView) findViewById(R.id.header_customername);
        header_customername.setText(companyname + "-" + financelocation);

        invokeWS_Finance_Ledger();
    }

    public void invokeWS_Finance_Ledger(){
        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno  = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();
            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", fromdate);
            paramsMap.put("todate", todate);
            paramsMap.put("companycd", companycd);
            paramsMap.put("fincompanycd", fincompanycd);
            paramsMap.put("sysuserno", sysuserno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetFinanceOutstanding", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablecustomerledger);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Outstanding_Finance_View.this);
                        TextView highsHeading_voucherdt = initPlainHeaderTextView();
                        highsHeading_voucherdt.setText("Loan. DT");
                        highsHeading_voucherdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_voucherdt.setGravity(Gravity.CENTER);

                        TextView highsHeading_trndesc = initPlainHeaderTextView();
                        highsHeading_trndesc.setText("Customer");
                        highsHeading_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_trndesc.setGravity(Gravity.LEFT);

                        TextView highsHeading_debit = initPlainHeaderTextView();
                        highsHeading_debit.setText("Approval");
                        highsHeading_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_debit.setGravity(Gravity.LEFT);

                        TextView highsHeading_credit = initPlainHeaderTextView();
                        highsHeading_credit.setText("Location");
                        highsHeading_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_credit.setGravity(Gravity.LEFT);

                        TextView highsHeading_ageing = initPlainHeaderTextView();
                        highsHeading_ageing.setText("Ageing");
                        highsHeading_ageing.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_ageing.setGravity(Gravity.CENTER);

                        TextView highsHeading_balance = initPlainHeaderTextView();
                        highsHeading_balance.setText("Balance");
                        highsHeading_balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_balance.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_voucherdt);
                        tblrowHeading.addView(highsHeading_trndesc);
                        tblrowHeading.addView(highsHeading_debit);
                        tblrowHeading.addView(highsHeading_credit);
                        tblrowHeading.addView(highsHeading_ageing);
                        tblrowHeading.addView(highsHeading_balance);

                        tablesalesregister.addView(tblrowHeading);

                        double fbalance;
                        double fDebit;

                        fbalance = 0;
                        fDebit = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("financeledger");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);



                                final String dCustcd;
                                dCustcd = jsonObject.getString("custcd");


                                TableRow tblrowLabels = new TableRow(Outstanding_Finance_View.this);


                                TextView highsLabel_voucherdt = initPlainTextView(i);
                                highsLabel_voucherdt.setText(jsonObject.getString("vloandt"));
                                highsLabel_voucherdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_voucherdt.setGravity(Gravity.CENTER);

                                TextView highsLabel_trndesc = initPlainTextView(i);
                                highsLabel_trndesc.setText(jsonObject.getString("custname"));
                                highsLabel_trndesc.setTypeface(Typeface.DEFAULT);
                                highsLabel_trndesc.setGravity(Gravity.LEFT);

                                TextView highsLabel_debit= initPlainTextView(i);
                                highsLabel_debit.setText(jsonObject.getString("approvalcode"));
                                highsLabel_debit.setTypeface(Typeface.DEFAULT);
                                highsLabel_debit.setGravity(Gravity.LEFT);

                                TextView highsLabel_credit = initPlainTextView(i);
                                highsLabel_credit.setText(jsonObject.getString("financelocation"));
                                highsLabel_credit.setTypeface(Typeface.DEFAULT);
                                highsLabel_credit.setGravity(Gravity.CENTER);

                                TextView highsLabel_Ageing = initPlainTextView(i);
                                highsLabel_Ageing.setText(jsonObject.getString("AGEING"));
                                highsLabel_Ageing.setTypeface(Typeface.DEFAULT);
                                highsLabel_Ageing.setGravity(Gravity.CENTER);

                                TextView highsLabel_balance = initPlainTextView(i);
                                highsLabel_balance.setText(jsonObject.getString("balanceamt"));
                                highsLabel_balance.setTypeface(Typeface.DEFAULT);
                                highsLabel_balance.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_voucherdt);
                                tblrowLabels.addView(highsLabel_trndesc);
                                tblrowLabels.addView(highsLabel_debit);
                                tblrowLabels.addView(highsLabel_credit);
                                tblrowLabels.addView(highsLabel_Ageing);
                                tblrowLabels.addView(highsLabel_balance);

                                tblrowLabels.setOnClickListener(new View.OnClickListener()
                                                                       {
                                                                           @Override
                                                                           public void onClick(View v)
                                                                           {
                                                                               Intent intent = new Intent(Outstanding_Finance_View.this, customer_view_single.class);
                                                                               intent.putExtra("custcd",dCustcd);
                                                                               startActivity(intent);
                                                                               startActivity(intent);
                                                                           }
                                                                       });

                                tablesalesregister.addView(tblrowLabels);

                                fbalance += Double.valueOf(jsonObject.getString("balanceamt"));
                                //fDebit += Integer.valueOf(0+jsonObject.getString("debit"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(Outstanding_Finance_View.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_trndesc = initPlainFooterTextView();
                        highsFooter_trndesc.setText("");
                        highsFooter_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_trndesc.setGravity(Gravity.CENTER);

                        TextView highsFooter_debit = initPlainFooterTextView();
                        highsFooter_debit.setText("");
                        highsFooter_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_debit.setGravity(Gravity.RIGHT);


                        TextView highsFooter_credit = initPlainFooterTextView();
                        highsFooter_credit.setText("");
                        highsFooter_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_credit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ageing = initPlainFooterTextView();
                        highsFooter_ageing.setText("");
                        highsFooter_ageing.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ageing.setGravity(Gravity.CENTER);

                        TextView highsFooter_balance = initPlainFooterTextView();
                        highsFooter_balance.setText(Utility.DoubleToString(fbalance));
                        highsFooter_balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_balance.setGravity(Gravity.CENTER);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_trndesc);
                        tblrowFooter.addView(highsFooter_debit);
                        tblrowFooter.addView(highsFooter_credit);
                        tblrowFooter.addView(highsFooter_ageing);
                        tblrowFooter.addView(highsFooter_balance);

                        tablesalesregister.addView(tblrowFooter);

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

        TextView textView = new TextView(Outstanding_Finance_View.this);
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
        TextView textView = new TextView(Outstanding_Finance_View.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Outstanding_Finance_View.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }



}


