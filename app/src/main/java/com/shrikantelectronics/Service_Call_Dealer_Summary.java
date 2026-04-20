package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Service_Call_Dealer_Summary extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationstock;

    TextView header_toplocationstock;
    String brandname;
    ProgressDialog prgDialog;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_call_dealer_summary);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

       invokeWS_Service_dealer_Summary();

    }

    public void invokeWS_Service_dealer_Summary() {

        try {

            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("custcd", "0");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Service_Call_Dealer_Summary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                       // tabletoplocationstock.setStretchAllColumns(true);
                       // tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Service_Call_Dealer_Summary.this);
                        TextView highsHeading_Center = initPlainHeaderTextView();
                        highsHeading_Center.setText("Center");
                        highsHeading_Center.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Center.setGravity(Gravity.LEFT);

                        TextView highsHeading_status_01 = initPlainHeaderTextView();
                        highsHeading_status_01.setText("Open");
                        highsHeading_status_01.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_01.setGravity(Gravity.RIGHT);
                        highsHeading_status_01.setWidth(300);

                        TextView highsHeading_status_02 = initPlainHeaderTextView();
                        highsHeading_status_02.setText("Accepted");
                        highsHeading_status_02.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_02.setGravity(Gravity.RIGHT);
                        highsHeading_status_02.setWidth(300);

                        TextView highsHeading_status_03 = initPlainHeaderTextView();
                        highsHeading_status_03.setText("Allocated");
                        highsHeading_status_03.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_03.setGravity(Gravity.RIGHT);
                        highsHeading_status_03.setWidth(300);

                        TextView highsHeading_status_04 = initPlainHeaderTextView();
                        highsHeading_status_04.setText("Visited");
                        highsHeading_status_04.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_04.setGravity(Gravity.RIGHT);
                        highsHeading_status_04.setWidth(300);

                        TextView highsHeading_status_05 = initPlainHeaderTextView();
                        highsHeading_status_05.setText("Re-Scheduled");
                        highsHeading_status_05.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_05.setGravity(Gravity.RIGHT);
                        highsHeading_status_05.setWidth(300);

                        TextView highsHeading_status_06 = initPlainHeaderTextView();
                        highsHeading_status_06.setText("Workshop");
                        highsHeading_status_06.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_06.setGravity(Gravity.RIGHT);
                        highsHeading_status_06.setWidth(300);

                        TextView highsHeading_status_07 = initPlainHeaderTextView();
                        highsHeading_status_07.setText("Part Pending");
                        highsHeading_status_07.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_07.setGravity(Gravity.RIGHT);
                        highsHeading_status_07.setWidth(300);

                        TextView highsHeading_status_08 = initPlainHeaderTextView();
                        highsHeading_status_08.setText("Finish Repair");
                        highsHeading_status_08.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_08.setGravity(Gravity.RIGHT);
                        highsHeading_status_08.setWidth(300);

                        TextView highsHeading_status_09 = initPlainHeaderTextView();
                        highsHeading_status_09.setText("Replacement");
                        highsHeading_status_09.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_09.setGravity(Gravity.RIGHT);
                        highsHeading_status_09.setWidth(300);

                        TextView highsHeading_status_10 = initPlainHeaderTextView();
                        highsHeading_status_10.setText("Confirmed(Month)");
                        highsHeading_status_10.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_10.setGravity(Gravity.RIGHT);
                        highsHeading_status_10.setWidth(300);

                        TextView highsHeading_status_99 = initPlainHeaderTextView();
                        highsHeading_status_99.setText("Cancelled");
                        highsHeading_status_99.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_99.setGravity(Gravity.RIGHT);
                        highsHeading_status_99.setWidth(300);

                        TextView highsHeading_status_Total = initPlainHeaderTextView();
                        highsHeading_status_Total.setText("Total");
                        highsHeading_status_Total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_status_Total.setGravity(Gravity.RIGHT);
                        highsHeading_status_Total.setWidth(300);

                        tblrowHeading.addView(highsHeading_Center);
                        tblrowHeading.addView(highsHeading_status_Total);
                        tblrowHeading.addView(highsHeading_status_01);
                        tblrowHeading.addView(highsHeading_status_02);
                        tblrowHeading.addView(highsHeading_status_03);
                        tblrowHeading.addView(highsHeading_status_04);
                        tblrowHeading.addView(highsHeading_status_05);
                        tblrowHeading.addView(highsHeading_status_06);
                        tblrowHeading.addView(highsHeading_status_07);
                        tblrowHeading.addView(highsHeading_status_08);
                        tblrowHeading.addView(highsHeading_status_09);
                        tblrowHeading.addView(highsHeading_status_10);
                        tblrowHeading.addView(highsHeading_status_99);

                        tabletoplocationstock.addView(tblrowHeading);

                        double fstatus_01=0,fstatus_02=0,fstatus_03=0,fstatus_04=0,fstatus_05=0,fstatus_06=0,fstatus_07=0,fstatus_08=0,fstatus_09=0,fstatus_10=0,fstatus_99=0,fstatus_total=0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("Calls_Dealer_StatusWise");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Service_Call_Dealer_Summary.this);

                                final String custcd =  jsonObject.getString("custcd");
                                final String custname =  jsonObject.getString("custname");

                                final TextView highsLabel_Center = initPlainTextView(i);
                                highsLabel_Center.setText(jsonObject.getString("custname"));
                                highsLabel_Center.setTypeface(Typeface.DEFAULT);
                                highsLabel_Center.setGravity(Gravity.LEFT);

                                TextView highsLabel_status_01 = initPlainTextView(i);
                                highsLabel_status_01.setText(jsonObject.getString("qty_01_status"));
                                highsLabel_status_01.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_01.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_02 = initPlainTextView(i);
                                highsLabel_status_02.setText(jsonObject.getString("qty_02_status"));
                                highsLabel_status_02.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_02.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_03 = initPlainTextView(i);
                                highsLabel_status_03.setText(jsonObject.getString("qty_03_status"));
                                highsLabel_status_03.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_03.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_04 = initPlainTextView(i);
                                highsLabel_status_04.setText(jsonObject.getString("qty_04_status"));
                                highsLabel_status_04.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_04.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_05 = initPlainTextView(i);
                                highsLabel_status_05.setText(jsonObject.getString("qty_05_status"));
                                highsLabel_status_05.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_05.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_06 = initPlainTextView(i);
                                highsLabel_status_06.setText(jsonObject.getString("qty_06_status"));
                                highsLabel_status_06.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_06.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_07 = initPlainTextView(i);
                                highsLabel_status_07.setText(jsonObject.getString("qty_07_status"));
                                highsLabel_status_07.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_07.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_08 = initPlainTextView(i);
                                highsLabel_status_08.setText(jsonObject.getString("qty_08_status"));
                                highsLabel_status_08.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_08.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_09 = initPlainTextView(i);
                                highsLabel_status_09.setText(jsonObject.getString("qty_09_status"));
                                highsLabel_status_09.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_09.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_10 = initPlainTextView(i);
                                highsLabel_status_10.setText(jsonObject.getString("qty_10_status"));
                                highsLabel_status_10.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_10.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_99 = initPlainTextView(i);
                                highsLabel_status_99.setText(jsonObject.getString("qty_99_status"));
                                highsLabel_status_99.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_99.setGravity(Gravity.RIGHT);

                                TextView highsLabel_status_total = initPlainTextView(i);
                                highsLabel_status_total.setText(jsonObject.getString("qty_total_status"));
                                highsLabel_status_total.setTypeface(Typeface.DEFAULT);
                                highsLabel_status_total.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_Center);
                                tblrowLabels.addView(highsLabel_status_total);
                                tblrowLabels.addView(highsLabel_status_01);
                                tblrowLabels.addView(highsLabel_status_02);
                                tblrowLabels.addView(highsLabel_status_03);
                                tblrowLabels.addView(highsLabel_status_04);
                                tblrowLabels.addView(highsLabel_status_05);
                                tblrowLabels.addView(highsLabel_status_06);
                                tblrowLabels.addView(highsLabel_status_07);
                                tblrowLabels.addView(highsLabel_status_08);
                                tblrowLabels.addView(highsLabel_status_09);
                                tblrowLabels.addView(highsLabel_status_10);
                                tblrowLabels.addView(highsLabel_status_99);

                                tblrowLabels.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(Service_Call_Dealer_Summary.this, Service_Call_Status_Register.class);
                                        intent.putExtra("activitystatus","");
                                        intent.putExtra("servicecentercd","0");
                                        intent.putExtra("custcd",custcd);

                                        startActivity(intent);
                                    }
                                });

                                tabletoplocationstock.addView(tblrowLabels);

                                fstatus_01 += Double.valueOf(jsonObject.getString("qty_01_status"));
                                fstatus_02 += Double.valueOf(jsonObject.getString("qty_02_status"));
                                fstatus_03 += Double.valueOf(jsonObject.getString("qty_03_status"));
                                fstatus_04 += Double.valueOf(jsonObject.getString("qty_04_status"));
                                fstatus_05 += Double.valueOf(jsonObject.getString("qty_05_status"));
                                fstatus_06 += Double.valueOf(jsonObject.getString("qty_06_status"));
                                fstatus_07 += Double.valueOf(jsonObject.getString("qty_07_status"));
                                fstatus_08 += Double.valueOf(jsonObject.getString("qty_08_status"));
                                fstatus_09 += Double.valueOf(jsonObject.getString("qty_09_status"));
                                fstatus_10 += Double.valueOf(jsonObject.getString("qty_10_status"));
                                fstatus_99 += Double.valueOf(jsonObject.getString("qty_99_status"));
                                fstatus_total += Double.valueOf(jsonObject.getString("qty_total_status"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(Service_Call_Dealer_Summary.this);
                        TextView highsFooter_Center = initPlainFooterTextView();
                        highsFooter_Center.setText("TOTAL");
                        highsFooter_Center.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Center.setGravity(Gravity.CENTER);

                        TextView highsFooter_status_01 = initPlainFooterTextView();
                        highsFooter_status_01.setText(Utility.DoubleToString(fstatus_01));
                        highsFooter_status_01.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_01.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_02 = initPlainFooterTextView();
                        highsFooter_status_02.setText(Utility.DoubleToString(fstatus_02));
                        highsFooter_status_02.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_02.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_03 = initPlainFooterTextView();
                        highsFooter_status_03.setText(Utility.DoubleToString(fstatus_03));
                        highsFooter_status_03.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_03.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_04 = initPlainFooterTextView();
                        highsFooter_status_04.setText(Utility.DoubleToString(fstatus_04));
                        highsFooter_status_04.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_04.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_05 = initPlainFooterTextView();
                        highsFooter_status_05.setText(Utility.DoubleToString(fstatus_05));
                        highsFooter_status_05.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_05.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_06 = initPlainFooterTextView();
                        highsFooter_status_06.setText(Utility.DoubleToString(fstatus_06));
                        highsFooter_status_06.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_06.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_07 = initPlainFooterTextView();
                        highsFooter_status_07.setText(Utility.DoubleToString(fstatus_07));
                        highsFooter_status_07.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_07.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_08 = initPlainFooterTextView();
                        highsFooter_status_08.setText(Utility.DoubleToString(fstatus_08));
                        highsFooter_status_08.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_08.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_09 = initPlainFooterTextView();
                        highsFooter_status_09.setText(Utility.DoubleToString(fstatus_09));
                        highsFooter_status_09.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_09.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_10 = initPlainFooterTextView();
                        highsFooter_status_10.setText(Utility.DoubleToString(fstatus_10));
                        highsFooter_status_10.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_10.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_99 = initPlainFooterTextView();
                        highsFooter_status_99.setText(Utility.DoubleToString(fstatus_99));
                        highsFooter_status_99.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_99.setGravity(Gravity.RIGHT);

                        TextView highsFooter_status_total = initPlainFooterTextView();
                        highsFooter_status_total.setText(Utility.DoubleToString(fstatus_total));
                        highsFooter_status_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_status_total.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_Center);
                        tblrowFooter.addView(highsFooter_status_total);
                        tblrowFooter.addView(highsFooter_status_01);
                        tblrowFooter.addView(highsFooter_status_02);
                        tblrowFooter.addView(highsFooter_status_03);
                        tblrowFooter.addView(highsFooter_status_04);
                        tblrowFooter.addView(highsFooter_status_05);
                        tblrowFooter.addView(highsFooter_status_06);
                        tblrowFooter.addView(highsFooter_status_07);
                        tblrowFooter.addView(highsFooter_status_08);
                        tblrowFooter.addView(highsFooter_status_09);
                        tblrowFooter.addView(highsFooter_status_10);
                        tblrowFooter.addView(highsFooter_status_99);

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

        TextView textView = new TextView(Service_Call_Dealer_Summary.this);
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
        TextView textView = new TextView(Service_Call_Dealer_Summary.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Service_Call_Dealer_Summary.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}