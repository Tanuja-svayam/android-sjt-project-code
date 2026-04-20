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


public class Service_Call_Status_Register extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tablecall_status_summary;

    TextView header_call_status_summary;
    String brandname;
    ProgressDialog prgDialog;

    String servicecentercd = "0", servicecentername = "",  custcd = "0", call_type = "", activitystatus ="";

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_call_register);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

       Intent i = getIntent();
       servicecentercd = i.getStringExtra("servicecentercd");
       custcd = i.getStringExtra("custcd");
       activitystatus = i.getStringExtra("activitystatus");

       invokeWS_Service_Call_Status_Register();

    }

    public void invokeWS_Service_Call_Status_Register() {
        try {
            prgDialog.show();
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("servicecentercd", ""+servicecentercd);
            paramsMap.put("custcd", ""+custcd);
            paramsMap.put("call_type", ""+call_type);
            paramsMap.put("call_status", ""+activitystatus);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Service_Call_Status_Register", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        prgDialog.hide();

                       // myLinearLayout.someTableView.removeAllViews();

                        header_call_status_summary= (TextView) findViewById(R.id.header_call_status_summary);
                        header_call_status_summary.setText("Call Register");

                        tablecall_status_summary  = (TableLayout) findViewById(R.id.tablecall_status_summary);
                        tablecall_status_summary.removeAllViews();

                       // tablecall_status_summary.setStretchAllColumns(true);
                       // tablecall_status_summary.setShrinkAllColumns(true);


                        TableRow tblrowHeading = new TableRow(Service_Call_Status_Register.this);
                        TextView highsHeading_ID = initPlainHeaderTextView();
                        highsHeading_ID.setText("ID");
                        highsHeading_ID.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_ID.setGravity(Gravity.LEFT);

                        TextView highsHeading_Call_Type = initPlainHeaderTextView();
                        highsHeading_Call_Type.setText("Call Type");
                        highsHeading_Call_Type.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Call_Type.setGravity(Gravity.LEFT);

                        TextView highsHeading_Dealer = initPlainHeaderTextView();
                        highsHeading_Dealer.setText("Dealer");
                        highsHeading_Dealer.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Dealer.setGravity(Gravity.LEFT);

                        TextView highsheading_EndCustomer = initPlainHeaderTextView();
                        highsheading_EndCustomer.setText("End Customer");
                        highsheading_EndCustomer.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_EndCustomer.setGravity(Gravity.LEFT);




                        TextView highsheading_Date = initPlainHeaderTextView();
                        highsheading_Date.setText("Date");
                        highsheading_Date.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Date.setGravity(Gravity.CENTER);

                        TextView highsheading_modelname = initPlainHeaderTextView();
                        highsheading_modelname.setText("Model");
                        highsheading_modelname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_modelname.setGravity(Gravity.LEFT);

                        TextView highsheading_topcategoryname = initPlainHeaderTextView();
                        highsheading_topcategoryname.setText("Category");
                        highsheading_topcategoryname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_topcategoryname.setGravity(Gravity.LEFT);



                        TextView highsheading_servicecentername = initPlainHeaderTextView();
                        highsheading_servicecentername.setText("Service Center");
                        highsheading_servicecentername.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_servicecentername.setGravity(Gravity.LEFT);

                        TextView highsheading_serviceengineername = initPlainHeaderTextView();
                        highsheading_serviceengineername.setText("Service Engineer");
                        highsheading_serviceengineername.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_serviceengineername.setGravity(Gravity.LEFT);

                        TextView highsheading_activitystatusdesc = initPlainHeaderTextView();
                        highsheading_activitystatusdesc.setText("Status");
                        highsheading_activitystatusdesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_activitystatusdesc.setGravity(Gravity.LEFT);

                        tblrowHeading.addView(highsHeading_ID);
                        tblrowHeading.addView(highsHeading_Call_Type);
                        tblrowHeading.addView(highsHeading_Dealer);
                        tblrowHeading.addView(highsheading_EndCustomer);
                        tblrowHeading.addView(highsheading_Date);
                        tblrowHeading.addView(highsheading_modelname);
                        tblrowHeading.addView(highsheading_topcategoryname);
                        tblrowHeading.addView(highsheading_servicecentername);
                        tblrowHeading.addView(highsheading_serviceengineername);
                        tblrowHeading.addView(highsheading_activitystatusdesc);

                        tablecall_status_summary.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("Service_Calls_Register");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Service_Call_Status_Register.this);


                                final String syscustactno ;
                                syscustactno=jsonObject.getString("syscustactno");

                                final TextView highsLabel_ID = initPlainTextView(i);
                                highsLabel_ID.setText(jsonObject.getString("Row_id"));
                                highsLabel_ID.setTypeface(Typeface.DEFAULT);
                                highsLabel_ID.setGravity(Gravity.LEFT);

                                TextView highsLabel_Call_Type = initPlainTextView(i);
                                highsLabel_Call_Type.setText(jsonObject.getString("activitytypedesc"));
                                highsLabel_Call_Type.setTypeface(Typeface.DEFAULT);
                                highsLabel_Call_Type.setGravity(Gravity.LEFT);

                                TextView highsLabel_Dealer = initPlainTextView(i);
                                highsLabel_Dealer.setText(jsonObject.getString("custname"));
                                highsLabel_Dealer.setTypeface(Typeface.DEFAULT);
                                highsLabel_Dealer.setGravity(Gravity.LEFT);

                                TextView highsLabel_EndCustomer = initPlainTextView(i);
                                highsLabel_EndCustomer.setText(jsonObject.getString("endcustomer_name"));
                                highsLabel_EndCustomer.setTypeface(Typeface.DEFAULT);
                                highsLabel_EndCustomer.setGravity(Gravity.LEFT);

                                TextView highsLabel_Date = initPlainTextView(i);
                                highsLabel_Date.setText(jsonObject.getString("vactivitydt"));
                                highsLabel_Date.setTypeface(Typeface.DEFAULT);
                                highsLabel_Date.setGravity(Gravity.LEFT);

                                TextView highsLabel_modelname = initPlainTextView(i);
                                highsLabel_modelname.setText(jsonObject.getString("modelname"));
                                highsLabel_modelname.setTypeface(Typeface.DEFAULT);
                                highsLabel_modelname.setGravity(Gravity.LEFT);

                                TextView highsLabel_topcategoryname = initPlainTextView(i);
                                highsLabel_topcategoryname.setText(jsonObject.getString("topcategoryname"));
                                highsLabel_topcategoryname.setTypeface(Typeface.DEFAULT);
                                highsLabel_topcategoryname.setGravity(Gravity.LEFT);

                                TextView highsLabel_servicecentername = initPlainTextView(i);
                                highsLabel_servicecentername.setText(jsonObject.getString("servicecentername"));
                                highsLabel_servicecentername.setTypeface(Typeface.DEFAULT);
                                highsLabel_servicecentername.setGravity(Gravity.LEFT);

                                TextView highsLabel_serviceengineername = initPlainTextView(i);
                                highsLabel_serviceengineername.setText(jsonObject.getString("serviceengineername"));
                                highsLabel_serviceengineername.setTypeface(Typeface.DEFAULT);
                                highsLabel_serviceengineername.setGravity(Gravity.LEFT);

                                TextView highsLabel_activitystatusdesc = initPlainTextView(i);
                                highsLabel_activitystatusdesc.setText(jsonObject.getString("activitystatusdesc"));
                                highsLabel_activitystatusdesc.setTypeface(Typeface.DEFAULT);
                                highsLabel_activitystatusdesc.setGravity(Gravity.LEFT);

                                tblrowLabels.addView(highsLabel_ID);
                                tblrowLabels.addView(highsLabel_Call_Type);
                                tblrowLabels.addView(highsLabel_Dealer);
                                tblrowLabels.addView(highsLabel_EndCustomer);
                                tblrowLabels.addView(highsLabel_Date);
                                tblrowLabels.addView(highsLabel_modelname);
                                tblrowLabels.addView(highsLabel_topcategoryname);
                                tblrowLabels.addView(highsLabel_servicecentername);
                                tblrowLabels.addView(highsLabel_serviceengineername);
                                tblrowLabels.addView(highsLabel_activitystatusdesc);

                                tblrowLabels.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                       // Intent intent = new Intent(Service_Call_Status_Register.this, Service_Call_Status_Register.class);
                                      //  intent.putExtra("syscustactno",syscustactno);
                                      //  startActivity(intent);
                                    }
                                });

                                tablecall_status_summary.addView(tblrowLabels);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


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

        TextView textView = new TextView(Service_Call_Status_Register.this);
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
        TextView textView = new TextView(Service_Call_Status_Register.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Service_Call_Status_Register.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}