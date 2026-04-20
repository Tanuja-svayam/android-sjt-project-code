package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class B2BOutstandingCustomerInvoiceActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    String name;

    TextView header_customername;
    TableLayout tablesalesregister;

    TableLayout tableLocationos;
    TextView header_locationos;

    String custcd, custname;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b2blocation_outstanding_view);


        Intent i = getIntent();

        custcd = i.getStringExtra("custcd");
        custname = i.getStringExtra("custname");

        invokeWS_CustomerInvoiceOs();
    }

    public void invokeWS_CustomerInvoiceOs() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("sysuserno", "0"+sysuserno);
            paramsMap.put("custcd", "0"+custcd);
            paramsMap.put("reptype", "S");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_CustomerInvoiceOsSummary_B2B", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_locationos= (TextView) findViewById(R.id.header_locationos);
                        header_locationos.setText(custname + " Outstanding Ageing");

                        tableLocationos  = (TableLayout) findViewById(R.id.tableLocationos);
                        tableLocationos.removeAllViews();

                        tableLocationos.setStretchAllColumns(true);
                        tableLocationos.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(B2BOutstandingCustomerInvoiceActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                        TableRow.LayoutParams params = new TableRow.LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(B2BOutstandingCustomerInvoiceActivity.this);

                        TextView highsHeading_InvoiceNo = initPlainHeaderTextView();
                        highsHeading_InvoiceNo.setText("Ref No");
                        highsHeading_InvoiceNo.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_InvoiceNo.setGravity(Gravity.LEFT);

                        TextView highsHeading_InvoiceDate = initPlainHeaderTextView();
                        highsHeading_InvoiceDate.setText("Date");
                        highsHeading_InvoiceDate.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_InvoiceDate.setGravity(Gravity.LEFT);


                        TextView highsHeading_ageing = initPlainHeaderTextView();
                        highsHeading_ageing.setText("Age");
                        highsHeading_ageing.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_ageing.setGravity(Gravity.LEFT);

                        TextView highsHeading_7days = initPlainHeaderTextView();
                        highsHeading_7days.setText("7 Days");
                        highsHeading_7days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_7days.setGravity(Gravity.RIGHT);

                        TextView highsHeading_15days = initPlainHeaderTextView();
                        highsHeading_15days.setText("15 Days");
                        highsHeading_15days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_15days.setGravity(Gravity.RIGHT);

                        TextView highsHeading_30days = initPlainHeaderTextView();
                        highsHeading_30days.setText("30 Days");
                        highsHeading_30days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_30days.setGravity(Gravity.RIGHT);

                        TextView highsHeading_45days = initPlainHeaderTextView();
                        highsHeading_45days.setText("45 Days");
                        highsHeading_45days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_45days.setGravity(Gravity.RIGHT);

                        TextView highsHeading_60days = initPlainHeaderTextView();
                        highsHeading_60days.setText("60 Days");
                        highsHeading_60days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_60days.setGravity(Gravity.RIGHT);

                        TextView highsHeading_90days = initPlainHeaderTextView();
                        highsHeading_90days.setText("90 Days");
                        highsHeading_90days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_90days.setGravity(Gravity.RIGHT);

                        TextView highsHeading_90abovedays = initPlainHeaderTextView();
                        highsHeading_90abovedays.setText("90< Days");
                        highsHeading_90abovedays.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_90abovedays.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_InvoiceNo);
                        tblrowHeading.addView(highsHeading_InvoiceDate);
                        tblrowHeading.addView(highsHeading_ageing);
                        tblrowHeading.addView(highsHeading_7days);
                        tblrowHeading.addView(highsHeading_15days);
                        tblrowHeading.addView(highsHeading_30days);
                        tblrowHeading.addView(highsHeading_45days);
                        tblrowHeading.addView(highsHeading_60days);
                        tblrowHeading.addView(highsHeading_90days);
                        tblrowHeading.addView(highsHeading_90abovedays);

                        tableLocationos.addView(tblrowHeading);

                        double f7days;
                        double f15days;
                        double f30days;
                        double f45days;
                        double f60days;
                        double f90days;
                        double f90abovedays;

                        f7days = 0;
                        f15days = 0;
                        f30days= 0;
                        f45days= 0;
                        f60days= 0;
                        f90days= 0;
                        f90abovedays= 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("location_customer_outstanding_b2b");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(B2BOutstandingCustomerInvoiceActivity.this);

                                final String dsysinvno;
                                dsysinvno = jsonObject.getString("sysinvno");

                                final TextView highsLabel_description = initPlainTextView(i);
                                highsLabel_description.setText(jsonObject.getString("invoiceno"));
                                highsLabel_description.setTypeface(Typeface.DEFAULT);
                                highsLabel_description.setGravity(Gravity.LEFT);

                                final TextView highsLabel_invoicedate = initPlainTextView(i);
                                highsLabel_invoicedate.setText(jsonObject.getString("vinvoicedt"));
                                highsLabel_invoicedate.setTypeface(Typeface.DEFAULT);
                                highsLabel_invoicedate.setGravity(Gravity.LEFT);

                                final TextView highsLabel_ageing = initPlainTextView(i);
                                highsLabel_ageing.setText(jsonObject.getString("ageing"));
                                highsLabel_ageing.setTypeface(Typeface.DEFAULT);
                                highsLabel_ageing.setGravity(Gravity.LEFT);

                                TextView highsLabel_7days = initPlainTextView(i);
                                highsLabel_7days.setText(jsonObject.getString("days_7_os"));
                                highsLabel_7days.setTypeface(Typeface.DEFAULT);
                                highsLabel_7days.setGravity(Gravity.RIGHT);

                                TextView highslabel_15days = initPlainTextView(i);
                                highslabel_15days.setText(jsonObject.getString("days_15_os"));
                                highslabel_15days.setTypeface(Typeface.DEFAULT);
                                highslabel_15days.setGravity(Gravity.RIGHT);

                                TextView highslabel_30days = initPlainTextView(i);
                                highslabel_30days.setText(jsonObject.getString("days_30_os"));
                                highslabel_30days.setTypeface(Typeface.DEFAULT);
                                highslabel_30days.setGravity(Gravity.RIGHT);

                                TextView highslabel_45days = initPlainTextView(i);
                                highslabel_45days.setText(jsonObject.getString("days_45_os"));
                                highslabel_45days.setTypeface(Typeface.DEFAULT);
                                highslabel_45days.setGravity(Gravity.RIGHT);

                                TextView highslabel_60days = initPlainTextView(i);
                                highslabel_60days.setText(jsonObject.getString("days_60_os"));
                                highslabel_60days.setTypeface(Typeface.DEFAULT);
                                highslabel_60days.setGravity(Gravity.RIGHT);

                                TextView highslabel_90days = initPlainTextView(i);
                                highslabel_90days.setText(jsonObject.getString("days_90_os"));
                                highslabel_90days.setTypeface(Typeface.DEFAULT);
                                highslabel_90days.setGravity(Gravity.RIGHT);

                                TextView highslabel_90abovedays = initPlainTextView(i);
                                highslabel_90abovedays.setText(jsonObject.getString("days_above_90_os"));
                                highslabel_90abovedays.setTypeface(Typeface.DEFAULT);
                                highslabel_90abovedays.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_description);
                                tblrowLabels.addView(highsLabel_invoicedate);
                                tblrowLabels.addView(highsLabel_ageing);
                                tblrowLabels.addView(highsLabel_7days);
                                tblrowLabels.addView(highslabel_15days);
                                tblrowLabels.addView(highslabel_30days);
                                tblrowLabels.addView(highslabel_45days);
                                tblrowLabels.addView(highslabel_60days);
                                tblrowLabels.addView(highslabel_90days);
                                tblrowLabels.addView(highslabel_90abovedays);

                                tableLocationos.addView(tblrowLabels);

                                f7days+= Double.valueOf(jsonObject.getString("days_7_os"));
                                f15days+= Double.valueOf(jsonObject.getString("days_15_os"));
                                f30days+= Double.valueOf(jsonObject.getString("days_30_os"));
                                f45days+= Double.valueOf(jsonObject.getString("days_45_os"));
                                f60days+= Double.valueOf(jsonObject.getString("days_60_os"));
                                f90days+= Double.valueOf(jsonObject.getString("days_90_os"));
                                f90abovedays+= Double.valueOf(jsonObject.getString("days_above_90_os"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(B2BOutstandingCustomerInvoiceActivity.this);

                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_invoicedate = initPlainFooterTextView();
                        highsFooter_invoicedate.setText("");
                        highsFooter_invoicedate.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_invoicedate.setGravity(Gravity.LEFT);

                        TextView highsFooter_ageing = initPlainFooterTextView();
                        highsFooter_ageing.setText("");
                        highsFooter_ageing.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ageing.setGravity(Gravity.LEFT);

                        TextView highsFooter_7days = initPlainFooterTextView();
                        highsFooter_7days.setText(Utility.DoubleToString(f7days));
                        highsFooter_7days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_7days.setGravity(Gravity.RIGHT);

                        TextView highsFooter_15days = initPlainFooterTextView();
                        highsFooter_15days.setText(Utility.DoubleToString(f15days));
                        highsFooter_15days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_15days.setGravity(Gravity.RIGHT);

                        TextView highsFooter_30days = initPlainFooterTextView();
                        highsFooter_30days.setText(Utility.DoubleToString(f30days));
                        highsFooter_30days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_30days.setGravity(Gravity.RIGHT);

                        TextView highsFooter_45days = initPlainFooterTextView();
                        highsFooter_45days.setText(Utility.DoubleToString(f45days));
                        highsFooter_45days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_45days.setGravity(Gravity.RIGHT);

                        TextView highsFooter_60days = initPlainFooterTextView();
                        highsFooter_60days.setText(Utility.DoubleToString(f60days));
                        highsFooter_60days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_60days.setGravity(Gravity.RIGHT);

                        TextView highsFooter_90days = initPlainFooterTextView();
                        highsFooter_90days.setText(Utility.DoubleToString(f90days));
                        highsFooter_90days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_90days.setGravity(Gravity.RIGHT);

                        TextView highsFooter_90abovedays = initPlainFooterTextView();
                        highsFooter_90abovedays.setText(Utility.DoubleToString(f90abovedays));
                        highsFooter_90abovedays.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_90abovedays.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_invoicedate);
                        tblrowFooter.addView(highsFooter_ageing);
                        tblrowFooter.addView(highsFooter_7days);
                        tblrowFooter.addView(highsFooter_15days);
                        tblrowFooter.addView(highsFooter_30days);
                        tblrowFooter.addView(highsFooter_45days);
                        tblrowFooter.addView(highsFooter_60days);
                        tblrowFooter.addView(highsFooter_90days);
                        tblrowFooter.addView(highsFooter_90abovedays);

                                tableLocationos.addView(tblrowFooter);

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
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(B2BOutstandingCustomerInvoiceActivity.this);
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
        TextView textView = new TextView(B2BOutstandingCustomerInvoiceActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(B2BOutstandingCustomerInvoiceActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}


