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

public class SalesAnalysisB2BCustomer extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationstock;
    Button pickFromDate;
    Button pickToDate;
    TextView header_toplocationstock;
    String brandname;
    ProgressDialog prgDialog;
    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;

    int year, month, day;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b2b_customer_salesanalysis);

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
       pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,1));

       pickToDate = (Button) findViewById(R.id.pickToDate);
       pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

       invokeWS_B2BCustomerSalesAnalysis();

    }

    public void invokeWS_B2BCustomerSalesAnalysis() {

        try {

            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            String sysuserno = globalVariable.getSysuserno();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("sysuserno", "0"+ sysuserno);

            //paramsMap.put("fromdate", "01-JUL-2017");
            //paramsMap.put("todate", "03-AUG-2017");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

         //   header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
           // header_toplocationstock.setText(+ " CATEGORY SALES SUMMARY");

            ApiHelper.post(URL + "Service1.asmx/SalesAnalysis_B2B_Customer", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(SalesAnalysisB2BCustomer.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Month");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Quantity");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Value");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        TextView highsHeading_Quantityreturn = initPlainHeaderTextView();
                        highsHeading_Quantityreturn.setText("Ret. Qty");
                        highsHeading_Quantityreturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantityreturn.setGravity(Gravity.RIGHT);

                        TextView highsheading_Valuereturn = initPlainHeaderTextView();
                        highsheading_Valuereturn.setText("Ret. Value");
                        highsheading_Valuereturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Valuereturn.setGravity(Gravity.RIGHT);


                        TextView highsHeading_Quantity_net = initPlainHeaderTextView();
                        highsHeading_Quantity_net.setText("Net Qty");
                        highsHeading_Quantity_net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity_net.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value_net = initPlainHeaderTextView();
                        highsheading_Value_net.setText("Net Value");
                        highsheading_Value_net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value_net.setGravity(Gravity.RIGHT);


                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);

                        tblrowHeading.addView(highsHeading_Quantityreturn);
                        tblrowHeading.addView(highsheading_Valuereturn);

                        tblrowHeading.addView(highsHeading_Quantity_net);
                        tblrowHeading.addView(highsheading_Value_net);

                        tabletoplocationstock.addView(tblrowHeading);

                        double fQuantity;
                        double fValue;

                        double fQuantityreturn;
                        double fValuereturn;

                        double fQuantity_net;
                        double fValue_net;

                        fQuantity = 0;
                        fValue = 0;

                        fQuantityreturn = 0;
                        fValuereturn = 0;

                        fQuantity_net = 0;
                        fValue_net = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_analysis_customer");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(SalesAnalysisB2BCustomer.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("monthname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                //highsLabel_category.setOnClickListener(new View.OnClickListener()
                                //{
                                  //  @Override
                                    //public void onClick(View v)
                                   // {
                                     //   Intent intent = new Intent(SalesAnalysisB2BCustomer.this, SalesActivity_Daily.class);
                                     //   intent.putExtra("locationname","");
                                     //   intent.putExtra("monthname",highsLabel_category.getText());
                                     //   startActivity(intent);
                                   // }
                               // });

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("value"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                TextView highsLabel_QuantityReturn = initPlainTextView(i);
                                highsLabel_QuantityReturn.setText(jsonObject.getString("quantity_return"));
                                highsLabel_QuantityReturn.setTypeface(Typeface.DEFAULT);
                                highsLabel_QuantityReturn.setGravity(Gravity.RIGHT);

                                TextView highslabel_ValueReturn = initPlainTextView(i);
                                highslabel_ValueReturn.setText(jsonObject.getString("value_return"));
                                highslabel_ValueReturn.setTypeface(Typeface.DEFAULT);
                                highslabel_ValueReturn.setGravity(Gravity.RIGHT);


                                TextView highsLabel_Net_Quantity= initPlainTextView(i);
                                highsLabel_Net_Quantity.setText(jsonObject.getString("net_quantity"));
                                highsLabel_Net_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Net_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Net_Value= initPlainTextView(i);
                                highslabel_Net_Value.setText(jsonObject.getString("net_value"));
                                highslabel_Net_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Net_Value.setGravity(Gravity.RIGHT);


                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);
                                tblrowLabels.addView(highsLabel_QuantityReturn);
                                tblrowLabels.addView(highslabel_ValueReturn);

                                tblrowLabels.addView(highsLabel_Net_Quantity);
                                tblrowLabels.addView(highslabel_Net_Value);

                                tabletoplocationstock.addView(tblrowLabels);

                                fQuantity += Double.valueOf(jsonObject.getString("quantity"));
                                fValue += Double.valueOf(jsonObject.getString("value"));

                                fQuantityreturn += Double.valueOf(jsonObject.getString("quantity_return"));
                                fValuereturn += Double.valueOf(jsonObject.getString("value_return"));

                                fQuantity_net += Double.valueOf(jsonObject.getString("net_quantity"));
                                fValue_net += Double.valueOf(jsonObject.getString("net_value"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(SalesAnalysisB2BCustomer.this);
                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText("TOTAL");
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(Utility.DoubleToString(fQuantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(Utility.DoubleToString(fValue));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        TextView highsFooter_quantityReturn = initPlainFooterTextView();
                        highsFooter_quantityReturn.setText(Utility.DoubleToString(fQuantityreturn));
                        highsFooter_quantityReturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_quantityReturn.setGravity(Gravity.RIGHT);

                        TextView highsFooter_valueReturn = initPlainFooterTextView();
                        highsFooter_valueReturn.setText(Utility.DoubleToString(fValuereturn));
                        highsFooter_valueReturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_valueReturn.setGravity(Gravity.RIGHT);


                        TextView highsFooter_quantity_Net = initPlainFooterTextView();
                        highsFooter_quantity_Net.setText(Utility.DoubleToString(fQuantity_net));
                        highsFooter_quantity_Net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_quantity_Net.setGravity(Gravity.RIGHT);

                        TextView highsFooter_value_Net = initPlainFooterTextView();
                        highsFooter_value_Net.setText(Utility.DoubleToString(fValue_net));
                        highsFooter_value_Net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_value_Net.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);
                        tblrowFooter.addView(highsFooter_quantityReturn);
                        tblrowFooter.addView(highsFooter_valueReturn);
                        tblrowFooter.addView(highsFooter_quantity_Net);
                        tblrowFooter.addView(highsFooter_value_Net);

                        tabletoplocationstock.addView(tblrowFooter);

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
                         prgDialog.hide();
                    // When Http response code is '404'
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    // When Http response code is '500'

                    // When Http response code other than 404, 500
                                    }
            });

        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SalesAnalysisB2BCustomer.this);
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
        TextView textView = new TextView(SalesAnalysisB2BCustomer.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesAnalysisB2BCustomer.this);
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
        invokeWS_B2BCustomerSalesAnalysis();
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
        invokeWS_B2BCustomerSalesAnalysis();
    }

}