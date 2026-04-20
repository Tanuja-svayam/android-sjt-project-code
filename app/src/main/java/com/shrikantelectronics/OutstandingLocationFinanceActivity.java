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
import android.view.View;
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

public class OutstandingLocationFinanceActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;



    ProgressDialog prgDialog;

    String name;

    TextView header_customername;
    TableLayout tablesalesregister;

    TableLayout tableLocationos;
    TextView header_locationos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_finance_outstanding_view);



        invokeWS_LocationOs();
    }

     public void invokeWS_LocationOs() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

            String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("fromdate", Config.FROMDATE);
            paramsMap.put("todate", Config.TODATE);
            paramsMap.put("sysuserno",sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_LocationFinanceOutstandingSummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_locationos= (TextView) findViewById(R.id.header_locationos);
                        header_locationos.setText("Location Outstanding");

                        tableLocationos  = (TableLayout) findViewById(R.id.tableLocationos);
                        tableLocationos.removeAllViews();

                        tableLocationos.setStretchAllColumns(true);
                        tableLocationos.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(OutstandingLocationFinanceActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                        TableRow.LayoutParams params = new TableRow.LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(OutstandingLocationFinanceActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsheading_creditcard = initPlainHeaderTextView();
                        highsheading_creditcard.setText("Amount");
                        highsheading_creditcard.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_creditcard.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsheading_creditcard);

                        tableLocationos.addView(tblrowHeading);


                        int fFINNANCE;


                        fFINNANCE = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("location_customer_outstanding");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(OutstandingLocationFinanceActivity.this);

                                final String companycd = jsonObject.getString("companycd");
                                final TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("location"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);
                                highsLabel_location.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(OutstandingLocationFinanceActivity.this, Outstanding_finance.class);

                                         intent.putExtra("fromdate",Config.FROMDATE);
                                         intent.putExtra("todate",Config.TODATE);
                                        intent.putExtra("companycd",companycd);
                                        intent.putExtra("locationname",highsLabel_location.getText());
                                          startActivity(intent);
                                    }
                                });

                                TextView highsLabel_cash = initPlainTextView(i);
                                highsLabel_cash.setText(jsonObject.getString("amount"));
                                highsLabel_cash.setTypeface(Typeface.DEFAULT);
                                highsLabel_cash.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_cash);

                                tableLocationos.addView(tblrowLabels);

                          //      fFINNANCE += Integer.valueOf(0 + jsonObject.getString("amount"));



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                       // tableLocationos.addView(tblrowFooter);

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

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(OutstandingLocationFinanceActivity.this);
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
        TextView textView = new TextView(OutstandingLocationFinanceActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(OutstandingLocationFinanceActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }



}


