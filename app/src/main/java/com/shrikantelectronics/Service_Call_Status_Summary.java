package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
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
import android.content.Intent;


public class Service_Call_Status_Summary extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tablecall_status_summary;

    TextView header_call_status_summary;
    String brandname;
    ProgressDialog prgDialog;

    String  servicecentername;
    String servicecentercd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_call_summary);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

       Intent i = getIntent();
       servicecentercd =i.getStringExtra("servicecentercd");
       servicecentername= i.getStringExtra("servicecentername");


           invokeWS_Service_Call_Status_Summary();

    }

    public void invokeWS_Service_Call_Status_Summary() {
        try {
            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("servicecentercd", "0"+servicecentercd);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Service_Call_Status_Summary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        header_call_status_summary= (TextView) findViewById(R.id.header_call_status_summary);
                        header_call_status_summary.setText(servicecentername + " - Call Status");

                        tablecall_status_summary  = (TableLayout) findViewById(R.id.tablecall_status_summary);
                        tablecall_status_summary.removeAllViews();

                       // tablecall_status_summary.setStretchAllColumns(true);
                       // tablecall_status_summary.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Service_Call_Status_Summary.this);
                        TextView highsHeading_Status = initPlainHeaderTextView();
                        highsHeading_Status.setText("Status");
                        highsHeading_Status.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Status.setGravity(Gravity.LEFT);

                        TextView highsHeading_Last_30_Days = initPlainHeaderTextView();
                        highsHeading_Last_30_Days.setText("Last 30 Days");
                        highsHeading_Last_30_Days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Last_30_Days.setGravity(Gravity.RIGHT);
                        highsHeading_Last_30_Days.setWidth(200);

                        TextView highsheading_Current_Month = initPlainHeaderTextView();
                        highsheading_Current_Month.setText("Current Month");
                        highsheading_Current_Month.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Current_Month.setGravity(Gravity.RIGHT);
                        highsheading_Current_Month.setWidth(200);

                        TextView highsheading_All = initPlainHeaderTextView();
                        highsheading_All.setText("All");
                        highsheading_All.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_All.setGravity(Gravity.RIGHT);
                        highsheading_All.setWidth(200);

                        tblrowHeading.addView(highsHeading_Status);
                        tblrowHeading.addView(highsHeading_Last_30_Days);
                        tblrowHeading.addView(highsheading_Current_Month);
                        tblrowHeading.addView(highsheading_All);

                        tablecall_status_summary.addView(tblrowHeading);

                        double fLast_30_Days;
                        double fCurrent_Month;
                        double fAll;

                        fLast_30_Days = 0;
                        fCurrent_Month = 0;
                        fAll = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("Calls_Service_Pending_StatusWise");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Service_Call_Status_Summary.this);
                                final String activitystatus =  jsonObject.getString("activitystatus");

                                final TextView highsLabel_activitystatus = initPlainTextView(i);
                                highsLabel_activitystatus.setText(jsonObject.getString("activitystatusdesc"));
                                highsLabel_activitystatus.setTypeface(Typeface.DEFAULT);
                                highsLabel_activitystatus.setGravity(Gravity.LEFT);

                                TextView highsLabel_qty_30_days = initPlainTextView(i);
                                highsLabel_qty_30_days.setText(jsonObject.getString("qty_30_days"));
                                highsLabel_qty_30_days.setTypeface(Typeface.DEFAULT);
                                highsLabel_qty_30_days.setGravity(Gravity.RIGHT);

                                TextView highsLabel_qty_current_month = initPlainTextView(i);
                                highsLabel_qty_current_month.setText(jsonObject.getString("qty_current_month"));
                                highsLabel_qty_current_month.setTypeface(Typeface.DEFAULT);
                                highsLabel_qty_current_month.setGravity(Gravity.RIGHT);

                                TextView highslabel_qty_all = initPlainTextView(i);
                                highslabel_qty_all.setText(jsonObject.getString("qty_all"));
                                highslabel_qty_all.setTypeface(Typeface.DEFAULT);
                                highslabel_qty_all.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_activitystatus);
                                tblrowLabels.addView(highsLabel_qty_30_days);
                                tblrowLabels.addView(highsLabel_qty_current_month);
                                tblrowLabels.addView(highslabel_qty_all);

                                tblrowLabels.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(Service_Call_Status_Summary.this, Service_Call_Status_Register.class);
                                        intent.putExtra("activitystatus",activitystatus);
                                        intent.putExtra("servicecentercd",servicecentercd);
                                        intent.putExtra("custcd","0");
                                        startActivity(intent);
                                    }
                                });

                                tablecall_status_summary.addView(tblrowLabels);

                                fLast_30_Days += Double.valueOf(jsonObject.getString("qty_30_days"));
                                fCurrent_Month += Double.valueOf(jsonObject.getString("qty_current_month"));
                                fAll += Double.valueOf(jsonObject.getString("qty_all"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(Service_Call_Status_Summary.this);
                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText("TOTAL");
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(Utility.DoubleToString(fLast_30_Days));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(Utility.DoubleToString(fCurrent_Month));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        TextView highsFooter_quantityReturn = initPlainFooterTextView();
                        highsFooter_quantityReturn.setText(Utility.DoubleToString(fAll));
                        highsFooter_quantityReturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_quantityReturn.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);
                        tblrowFooter.addView(highsFooter_quantityReturn);

                        tablecall_status_summary.addView(tblrowFooter);

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

        TextView textView = new TextView(Service_Call_Status_Summary.this);
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
        TextView textView = new TextView(Service_Call_Status_Summary.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Service_Call_Status_Summary.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}