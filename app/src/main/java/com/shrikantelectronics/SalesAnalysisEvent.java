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
import android.widget.RadioButton;
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

public class SalesAnalysisEvent extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;
    String brandname;
    ProgressDialog prgDialog;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleseventanalysis);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

       invokeWS_EventSalesAnalysis();
    }


    public void invokeWS_EventSalesAnalysis() {

        try {
            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("type", "");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText("EVENT SALES SUMMARY");

            ApiHelper.post(URL + "Service1.asmx/SalesAnalysis_Event", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(SalesAnalysisEvent.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Event");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity_Year_1 = initPlainHeaderTextView();
                        highsHeading_Quantity_Year_1.setText("PY Qty");
                        highsHeading_Quantity_Year_1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity_Year_1.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value_Year_1 = initPlainHeaderTextView();
                        highsheading_Value_Year_1.setText("PY Qty");
                        highsheading_Value_Year_1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value_Year_1.setGravity(Gravity.RIGHT);

                        TextView highsHeading_Quantity_Year_2 = initPlainHeaderTextView();
                        highsHeading_Quantity_Year_2.setText("CY Qty");
                        highsHeading_Quantity_Year_2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity_Year_2.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value_Year_2 = initPlainHeaderTextView();
                        highsheading_Value_Year_2.setText("CY Qty");
                        highsheading_Value_Year_2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value_Year_2.setGravity(Gravity.RIGHT);

                        TextView highsHeading_Quantity_Year_diff = initPlainHeaderTextView();
                        highsHeading_Quantity_Year_diff.setText("Diff Qty");
                        highsHeading_Quantity_Year_diff.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity_Year_diff.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value_Year_diff = initPlainHeaderTextView();
                        highsheading_Value_Year_diff.setText("Diff Value");
                        highsheading_Value_Year_diff.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value_Year_diff.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity_Year_1);
                        tblrowHeading.addView(highsheading_Value_Year_1);
                        tblrowHeading.addView(highsHeading_Quantity_Year_2);
                        tblrowHeading.addView(highsheading_Value_Year_2);
                        tblrowHeading.addView(highsHeading_Quantity_Year_diff);
                        tblrowHeading.addView(highsheading_Value_Year_diff);
                        tabletoplocationstock.addView(tblrowHeading);

                        double fQTY_year_1;
                        double fVALUE_year_1;
                        double fQTY_year_2;
                        double fVALUE_year_2;
                        double fQTY_DIFF;
                        double fVALUE_DIFF;

                        fQTY_year_1 = 0;
                        fVALUE_year_1 = 0;
                        fQTY_year_2 = 0;
                        fVALUE_year_2 = 0;
                        fQTY_DIFF = 0;
                        fVALUE_DIFF = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_analysis_event");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(SalesAnalysisEvent.this);

                                final String dsyseventno;
                                dsyseventno = jsonObject.getString("syseventno");

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("eventdescription"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(SalesAnalysisEvent.this, SalesAnalysisEvent_Brand.class);
                                        intent.putExtra("syseventno",dsyseventno);
                                        intent.putExtra("category","");
                                        intent.putExtra("eventdescription",highsLabel_category.getText());
                                        startActivity(intent);
                                    }
                                });


                                TextView highsLabel_Quantity_Year_1 = initPlainTextView(i);
                                highsLabel_Quantity_Year_1.setText(jsonObject.getString("quantity_year_1"));
                                highsLabel_Quantity_Year_1.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity_Year_1.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value_Year_1 = initPlainTextView(i);
                                highslabel_Value_Year_1.setText(jsonObject.getString("value_year_1"));
                                highslabel_Value_Year_1.setTypeface(Typeface.DEFAULT);
                                highslabel_Value_Year_1.setGravity(Gravity.RIGHT);

                                TextView highsLabel_Quantity_Year_2 = initPlainTextView(i);
                                highsLabel_Quantity_Year_2.setText(jsonObject.getString("quantity_year_2"));
                                highsLabel_Quantity_Year_2.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity_Year_2.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value_Year_2 = initPlainTextView(i);
                                highslabel_Value_Year_2.setText(jsonObject.getString("value_year_2"));
                                highslabel_Value_Year_2.setTypeface(Typeface.DEFAULT);
                                highslabel_Value_Year_2.setGravity(Gravity.RIGHT);

                                TextView highsLabel_Quantity_Year_diff = initPlainTextView(i);
                                highsLabel_Quantity_Year_diff.setText(jsonObject.getString("diff_quantity"));
                                highsLabel_Quantity_Year_diff.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity_Year_diff.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value_Year_diff = initPlainTextView(i);
                                highslabel_Value_Year_diff.setText(jsonObject.getString("diff_value"));
                                highslabel_Value_Year_diff.setTypeface(Typeface.DEFAULT);
                                highslabel_Value_Year_diff.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity_Year_1);
                                tblrowLabels.addView(highslabel_Value_Year_1);
                                tblrowLabels.addView(highsLabel_Quantity_Year_2);
                                tblrowLabels.addView(highslabel_Value_Year_2);
                                tblrowLabels.addView(highsLabel_Quantity_Year_diff);
                                tblrowLabels.addView(highslabel_Value_Year_diff);
                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

                                fQTY_year_1 += Double.valueOf(jsonObject.getString("quantity_year_1"));
                                fVALUE_year_1 += Double.valueOf(jsonObject.getString("value_year_1"));

                                fQTY_year_2 += Double.valueOf(jsonObject.getString("quantity_year_2"));
                                fVALUE_year_2 += Double.valueOf(jsonObject.getString("value_year_2"));

                                fQTY_DIFF+= Double.valueOf(jsonObject.getString("diff_quantity"));
                                fVALUE_DIFF+= Double.valueOf(jsonObject.getString("diff_value"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(SalesAnalysisEvent.this);

                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("TOTAL");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_Quantity_Year_1 = initPlainFooterTextView();
                        highsFooter_Quantity_Year_1.setText(String.format("%.0f", fQTY_year_1));
                        highsFooter_Quantity_Year_1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Quantity_Year_1.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Value_Year_1 = initPlainFooterTextView();
                        highsFooter_Value_Year_1.setText(String.format("%.2f", fVALUE_year_1));
                        highsFooter_Value_Year_1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Value_Year_1.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Quantity_Year_2 = initPlainFooterTextView();
                        highsFooter_Quantity_Year_2.setText(String.format("%.0f", fQTY_year_2));
                        highsFooter_Quantity_Year_2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Quantity_Year_2.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Value_Year_2 = initPlainFooterTextView();
                        highsFooter_Value_Year_2.setText(String.format("%.2f", fVALUE_year_2));
                        highsFooter_Value_Year_2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Value_Year_2.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Quantity_Diff = initPlainFooterTextView();
                        highsFooter_Quantity_Diff.setText(String.format("%.2f", fQTY_DIFF));
                        highsFooter_Quantity_Diff.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Quantity_Diff.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Value_Diff = initPlainFooterTextView();
                        highsFooter_Value_Diff.setText(String.format("%.2f", fVALUE_DIFF));
                        highsFooter_Value_Diff.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Value_Diff.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_Quantity_Year_1);
                        tblrowFooter.addView(highsFooter_Value_Year_1);
                        tblrowFooter.addView(highsFooter_Quantity_Year_2);
                        tblrowFooter.addView(highsFooter_Value_Year_2);
                        tblrowFooter.addView(highsFooter_Quantity_Diff);
                        tblrowFooter.addView(highsFooter_Value_Diff);

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

        TextView textView = new TextView(SalesAnalysisEvent.this);
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
        TextView textView = new TextView(SalesAnalysisEvent.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesAnalysisEvent.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}