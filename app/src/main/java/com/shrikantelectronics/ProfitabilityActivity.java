package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.CheckBox;
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

public class ProfitabilityActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tabletoplocationprofitability;

    TextView header_toplocationprofitability;
    String categoryname;
    Button pickFromDate;
    Button pickToDate;
    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;
    int year, month, day;
    CheckBox chkShowInvoiceListCHK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profitability);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        chkShowInvoiceListCHK = (CheckBox) findViewById(R.id.chkShowInvoiceList);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        pickToDate = (Button) findViewById(R.id.pickToDate);
        pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        chkShowInvoiceListCHK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeWS_ProfitabilitySummary();
            }
        });

        invokeWS_ProfitabilitySummary();

    }

    public void invokeWS_ProfitabilitySummary() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("fromdt", pickFromDate.getText().toString());
            paramsMap.put("todt", pickToDate.getText().toString());
            paramsMap.put("companycd", "0");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Profitability_Summary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationprofitability  = (TableLayout) findViewById(R.id.tabletoplocationprofitability);

                        tabletoplocationprofitability.removeAllViews();
                        tabletoplocationprofitability.setStretchAllColumns(true);
                        tabletoplocationprofitability.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(ProfitabilityActivity.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Location");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Quantity");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Value");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);

                        tabletoplocationprofitability.addView(tblrowHeading);

                        double fQuantity;
                        double fValue;

                        fQuantity = 0;
                        fValue = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("profitability_summary");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(ProfitabilityActivity.this);

                                final String companycd;
                                companycd =jsonObject.getString("companycd");

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("location"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                pickFromDate = (Button) findViewById(R.id.pickFromDate);
                                pickToDate = (Button) findViewById(R.id.pickToDate);

                                if(chkShowInvoiceListCHK.isChecked())
                                {
                                    highsLabel_category.setOnClickListener(new OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent intent = new Intent(ProfitabilityActivity.this, Profitability_Invoice_List.class);
                                            intent.putExtra("fromdt",pickFromDate.getText().toString());
                                            intent.putExtra("todt",pickToDate.getText().toString());
                                            intent.putExtra("companycd",companycd);
                                            intent.putExtra("brandname","");
                                            intent.putExtra("categoryname","");
                                            startActivity(intent);
                                        }
                                    });
                                }
                                else
                                {
                                    highsLabel_category.setOnClickListener(new OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent intent = new Intent(ProfitabilityActivity.this, ProfitabilityActivityBrand.class);

                                            intent.putExtra("fromdt",pickFromDate.getText().toString());
                                            intent.putExtra("todt",pickToDate.getText().toString());
                                            intent.putExtra("companycd",companycd);
                                            intent.putExtra("categoryname","");
                                            startActivity(intent);
                                        }
                                    });
                                };




                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("profitability_value"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);

                                tblrowLabels.setClickable(true);

                                tabletoplocationprofitability.addView(tblrowLabels);

                                fQuantity += Double.valueOf(jsonObject.getString("quantity"));
                                fValue += Double.valueOf(jsonObject.getString("profitability_value"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(ProfitabilityActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fQuantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        //highsFooter_ra.setText(String.valueOf(fValue));
                        highsFooter_ra.setText(String.format("%.2f", fValue));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);

                        tabletoplocationprofitability.addView(tblrowFooter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_profitabilityactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_brandwise) {
            navigatetoProfitabilityActvitySummery_Brand();
        } else if (id == R.id.action_categorywise) {
            navigatetoProfitabilityActvitySummery_Category();
        } else if (id == R.id.action_locationwise) {
            navigatetoProfitabilityActvitySummery_Location();
        }

        return super.onOptionsItemSelected(item);
    }


       public void navigatetoProfitabilityActvitySummery_Category(){
        Intent customerIntent = new Intent(ProfitabilityActivity.this,ProfitabilityActivityCategory.class);

           pickFromDate = (Button) findViewById(R.id.pickFromDate);
           pickToDate = (Button) findViewById(R.id.pickToDate);

           customerIntent.putExtra("fromdt",pickFromDate.getText().toString());
           customerIntent.putExtra("todt",pickToDate.getText().toString());
           customerIntent.putExtra("companycd","0");

        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }


    public void navigatetoProfitabilityActvitySummery_Location(){
        Intent customerIntent = new Intent(ProfitabilityActivity.this,ProfitabilityActivityLocation.class);
        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickToDate = (Button) findViewById(R.id.pickToDate);

        customerIntent.putExtra("fromdt",pickFromDate.getText().toString());
        customerIntent.putExtra("todt",pickToDate.getText().toString());
        customerIntent.putExtra("companycd","0");
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoProfitabilityActvitySummery_Brand(){
        Intent customerIntent = new Intent(ProfitabilityActivity.this, ProfitabilityActivityBrand.class);
        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickToDate = (Button) findViewById(R.id.pickToDate);

        customerIntent.putExtra("fromdt",pickFromDate.getText().toString());
        customerIntent.putExtra("todt",pickToDate.getText().toString());
        customerIntent.putExtra("companycd","0");
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
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
        invokeWS_ProfitabilitySummary();
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
        invokeWS_ProfitabilitySummary();
    }





    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(ProfitabilityActivity.this);
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
        TextView textView = new TextView(ProfitabilityActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(ProfitabilityActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


}
