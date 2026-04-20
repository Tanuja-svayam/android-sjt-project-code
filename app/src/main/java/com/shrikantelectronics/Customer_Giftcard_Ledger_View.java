package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
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
import java.util.HashMap;

public class Customer_Giftcard_Ledger_View extends AppCompatActivity {

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

    String custcd;
    String name;


    TextView header_customername;
    TableLayout tablesalesregister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_giftcard_ledger_view);

        Intent i = getIntent();

        custcd = i.getStringExtra("custcd");
        name = i.getStringExtra("name");


        header_customername = (TextView) findViewById(R.id.header_customername);
        header_customername.setText(name);

        invokeWS_Customer_GiftCard_Ledger(custcd);
    }

    public void invokeWS_Customer_GiftCard_Ledger(String custcd){
        try {

            Map<String, String> paramsMap = new HashMap<>();


            paramsMap.put("dcustcd", custcd);
            paramsMap.put("fromdate", "");
            paramsMap.put("todate", "");

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerGiftCardLedger", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablecustomerledger);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Customer_Giftcard_Ledger_View.this);
                        TextView highsHeading_voucherdt = initPlainHeaderTextView();
                        highsHeading_voucherdt.setText("Date");
                        highsHeading_voucherdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_voucherdt.setGravity(Gravity.CENTER);

                        TextView highsHeading_trndesc = initPlainHeaderTextView();
                        highsHeading_trndesc.setText("Type");
                        highsHeading_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_trndesc.setGravity(Gravity.LEFT);

                        TextView highsHeading_refno = initPlainHeaderTextView();
                        highsHeading_refno.setText("Ref No");
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
                        tblrowHeading.addView(highsHeading_trndesc);
                        tblrowHeading.addView(highsHeading_refno);
                        tblrowHeading.addView(highsHeading_debit);
                        tblrowHeading.addView(highsHeading_credit);
                        tblrowHeading.addView(highsHeading_balance);

                        tablesalesregister.addView(tblrowHeading);

                        double fCredit;
                        double fDebit;

                        fCredit = 0;
                        fDebit = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customer");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String custcd;

                                custcd=jsonObject.getString("custcd");

                                TableRow tblrowLabels = new TableRow(Customer_Giftcard_Ledger_View.this);

                                TextView highsLabel_voucherdt = initPlainTextView(i);
                                highsLabel_voucherdt.setText(jsonObject.getString("voucherdt"));
                                highsLabel_voucherdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_voucherdt.setGravity(Gravity.CENTER);

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
                                tblrowLabels.addView(highsLabel_trndesc);
                                tblrowLabels.addView(highsLabel_refno);
                                tblrowLabels.addView(highsLabel_debit);
                                tblrowLabels.addView(highsLabel_credit);
                                tblrowLabels.addView(highsLabel_balance);

                                tablesalesregister.addView(tblrowLabels);

                                fCredit += Double.valueOf(0+jsonObject.getString("credit"));
                                fDebit += Double.valueOf(0+jsonObject.getString("debit"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(Customer_Giftcard_Ledger_View.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText("TOTAL");
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);


                        TextView highsFooter_refno = initPlainFooterTextView();
                        highsFooter_refno.setText("");
                        highsFooter_refno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_refno.setGravity(Gravity.CENTER);


                        TextView highsFooter_debit = initPlainFooterTextView();
                        highsFooter_debit.setText(String.valueOf(fDebit));
                        highsFooter_debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_debit.setGravity(Gravity.RIGHT);


                        TextView highsFooter_credit = initPlainFooterTextView();
                        highsFooter_credit.setText(String.valueOf(fCredit));
                        highsFooter_credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_credit.setGravity(Gravity.RIGHT);

                        TextView highsFooter_balance = initPlainFooterTextView();
                        highsFooter_balance.setText("");
                        highsFooter_balance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_balance.setGravity(Gravity.CENTER);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_refno);
                        tblrowFooter.addView(highsFooter_debit);
                        tblrowFooter.addView(highsFooter_credit);
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

        TextView textView = new TextView(Customer_Giftcard_Ledger_View.this);
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
        TextView textView = new TextView(Customer_Giftcard_Ledger_View.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Customer_Giftcard_Ledger_View.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

 }


