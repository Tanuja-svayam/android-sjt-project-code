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

public class SalesComparision_Brand extends AppCompatActivity {


    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tabletoplocationstock;
    Button pickFromDate;
    Button pickToDate;
    TextView header_toplocationstock;
    String categoryname;
    ProgressDialog prgDialog;

    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;
    String fromdate;
    String todate;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesanalysis);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        categoryname = i.getStringExtra("categoryname");
        fromdate = i.getStringExtra("fromdate");
        todate = i.getStringExtra("todate");

        if (categoryname==null)
        {
            categoryname="";
        }


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

        invokeWS_TopBrandSalesAnalysis(categoryname);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_salesanalysis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_brandwise) {
            navigatetoSalesComparision_Brand();
        } else if (id == R.id.action_categorywise) {
            navigatetoSalesComparision_Category();
        } else if (id == R.id.action_locationwise) {
            navigatetoSalesComparision_Location();
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigatetoSalesComparision_Category(){
        Intent homeIntent = new Intent(SalesComparision_Brand.this,SalesComparision_Category.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("fromdate",pickFromDate.getText().toString());
        homeIntent.putExtra("todate",pickToDate.getText().toString());
        startActivity(homeIntent);
    }

    public void navigatetoSalesComparision_Brand(){
        Intent homeIntent = new Intent(SalesComparision_Brand.this,SalesComparision_Brand.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("fromdate",pickFromDate.getText().toString());
        homeIntent.putExtra("todate",pickToDate.getText().toString());
        startActivity(homeIntent);
    }

    public void navigatetoSalesComparision_Location(){
        Intent homeIntent = new Intent(SalesComparision_Brand.this,SalesComparision_Location.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("fromdate",pickFromDate.getText().toString());
        homeIntent.putExtra("todate",pickToDate.getText().toString());
        startActivity(homeIntent);
    }

    public void invokeWS_TopBrandSalesAnalysis(String categoryname) {

        try {

            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText(categoryname + " BRAND SALES SUMMARY");

            ApiHelper.post(URL + "Service1.asmx/SalesComparision_Brand", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.removeAllViews();

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(SalesComparision_Brand.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Brand");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Year 1");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Year 2");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        TextView highsheading_percent = initPlainHeaderTextView();
                        highsheading_percent.setText("Year 3");
                        highsheading_percent.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_percent.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);
                        tblrowHeading.addView(highsheading_percent);

                        tabletoplocationstock.addView(tblrowHeading);

                        int fQTY;
                        int fINVOICE_AMT;
                        int fPERCENTSHARE;

                        fQTY = 0;
                        fINVOICE_AMT = 0;
                        fPERCENTSHARE = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_comparision_brand");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(SalesComparision_Brand.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("location"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(SalesComparision_Brand.this, SalesAnalysisBrand_Category.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("fromdate",pickFromDate.getText().toString());
                                        intent.putExtra("todate",pickToDate.getText().toString());
                                        startActivity(intent);
                                   }
                                });

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity_year_1"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("quantity_year_2"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                TextView highslabel_percent = initPlainTextView(i);
                                highslabel_percent.setText(jsonObject.getString("quantity_year_3"));
                                highslabel_percent.setTypeface(Typeface.DEFAULT);
                                highslabel_percent.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);
                                tblrowLabels.addView(highslabel_percent);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

                                fQTY += Integer.valueOf(0+jsonObject.getString("quantity_year_1"));
                                fINVOICE_AMT += Integer.valueOf(0+jsonObject.getString("quantity_year_2"));
                                fPERCENTSHARE += Integer.valueOf(0+jsonObject.getString("quantity_year_3"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(SalesComparision_Brand.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("TOTAL");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText(String.valueOf(fQTY));
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.RIGHT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fINVOICE_AMT));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(fPERCENTSHARE));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);

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

	        TextView textView = new TextView(SalesComparision_Brand.this);
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
	        TextView textView = new TextView(SalesComparision_Brand.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

	    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(SalesComparision_Brand.this);
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
        invokeWS_TopBrandSalesAnalysis("");
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
        invokeWS_TopBrandSalesAnalysis("");
    }


}
