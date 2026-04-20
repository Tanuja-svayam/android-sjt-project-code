package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class B2BOutstandingLocationActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    String name;

    TextView header_customername;
   // TableLayout tablesalesregister;

    TableLayout tablesalesregister;
    TextView header_salesregister;

    public String LocationType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b2blocation_outstanding_view);

        Intent indent = getIntent();
        LocationType = indent.getStringExtra("LOCATIONTYPE");

        invokeWS_LocationOs();
    }

    public void invokeWS_LocationOs() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("sysuserno", "0"+sysuserno);
            paramsMap.put("LocationType", LocationType);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_CustomerAgeingOutstanding_B2B", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {



                        header_salesregister= (TextView) findViewById(R.id.header_salesregister);
                        header_salesregister.setText("Location Outstanding");

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        //tablesalesregister.setShrinkAllColumns(true);

                       // TableRow rowTitle = new TableRow(B2BOutstandingLocationActivity.this);
                       // rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                       // TableRow.LayoutParams params = new TableRow.LayoutParams();
                       // params.span = 6;

                        TableRow tblrowHeading = new TableRow(B2BOutstandingLocationActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Customer");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_netinvoiceamt = initPlainHeaderTextView();
                        highsHeading_netinvoiceamt.setText("Inv Amount");
                        highsHeading_netinvoiceamt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_netinvoiceamt.setGravity(Gravity.RIGHT);

                        TextView highsHeading_30days = initPlainHeaderTextView();
                        highsHeading_30days.setText("30 Days");
                        highsHeading_30days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_30days.setGravity(Gravity.RIGHT);
                        highsHeading_30days.setBackgroundColor(getResources().getColor(R.color.col_30));

                        TextView highsHeading_45days = initPlainHeaderTextView();
                        highsHeading_45days.setText("45 Days");
                        highsHeading_45days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_45days.setGravity(Gravity.RIGHT);
                        highsHeading_45days.setBackgroundColor(getResources().getColor(R.color.col_45));

                        TextView highsHeading_60days = initPlainHeaderTextView();
                        highsHeading_60days.setText("60 Days");
                        highsHeading_60days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_60days.setGravity(Gravity.RIGHT);
                        highsHeading_60days.setBackgroundColor(getResources().getColor(R.color.col_60));

                        TextView highsHeading_90days = initPlainHeaderTextView();
                        highsHeading_90days.setText("90 Days");
                        highsHeading_90days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_90days.setGravity(Gravity.RIGHT);
                        highsHeading_90days.setBackgroundColor(getResources().getColor(R.color.col_90));

                        TextView highsHeading_90abovedays = initPlainHeaderTextView();
                        highsHeading_90abovedays.setText("90< Days");
                        highsHeading_90abovedays.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_90abovedays.setGravity(Gravity.RIGHT);
                        highsHeading_90abovedays.setBackgroundColor(getResources().getColor(R.color.col_ab_90));

                        TextView highsHeading_nettpayable = initPlainHeaderTextView();
                        highsHeading_nettpayable.setText("Balance");
                        highsHeading_nettpayable.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_nettpayable.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_netinvoiceamt);
                        tblrowHeading.addView(highsHeading_30days);
                        tblrowHeading.addView(highsHeading_45days);
                        tblrowHeading.addView(highsHeading_60days);
                        tblrowHeading.addView(highsHeading_90days);
                        tblrowHeading.addView(highsHeading_90abovedays);
                        tblrowHeading.addView(highsHeading_nettpayable);

                        tablesalesregister.addView(tblrowHeading);

                        double fnetinvoiceamt;
                        double f30days;
                        double f45days;
                        double f60days;
                        double f90days;
                        double f90abovedays;
                        double fnettpayable;

                        fnetinvoiceamt=0;
                        f30days= 0;
                        f45days= 0;
                        f60days= 0;
                        f90days= 0;
                        f90abovedays= 0;
                        fnettpayable=0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customer_outstanding_b2b");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(B2BOutstandingLocationActivity.this);

                                final String did;
                                did = jsonObject.getString("custcd");

                                final TextView highsLabel_description = initPlainTextView(i);
                                highsLabel_description.setText(jsonObject.getString("custname"));
                                highsLabel_description.setTypeface(Typeface.DEFAULT);
                                highsLabel_description.setGravity(Gravity.LEFT);
                                highsLabel_description.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                          Intent intent = new Intent(B2BOutstandingLocationActivity.this, B2BOutstandingCustomerActivity.class);
                                         intent.putExtra("location",highsLabel_description.getText());
                                         intent.putExtra("custcd",did);
                                          startActivity(intent);
                                    }
                                });

                                TextView highslabel_netinvoiceamt = initPlainTextView(i);
                                highslabel_netinvoiceamt.setText(jsonObject.getString("netinvoiceamt"));
                                highslabel_netinvoiceamt.setTypeface(Typeface.DEFAULT);
                                highslabel_netinvoiceamt.setGravity(Gravity.RIGHT);

                                TextView highslabel_30days = initPlainTextView(i);
                                highslabel_30days.setText(jsonObject.getString("days_30_os"));
                                highslabel_30days.setTypeface(Typeface.DEFAULT);
                                highslabel_30days.setGravity(Gravity.RIGHT);
                                highslabel_30days.setBackgroundColor(getResources().getColor(R.color.col_30));

                                TextView highslabel_45days = initPlainTextView(i);
                                highslabel_45days.setText(jsonObject.getString("days_45_os"));
                                highslabel_45days.setTypeface(Typeface.DEFAULT);
                                highslabel_45days.setGravity(Gravity.RIGHT);
                                highslabel_45days.setBackgroundColor(getResources().getColor(R.color.col_45));

                                TextView highslabel_60days = initPlainTextView(i);
                                highslabel_60days.setText(jsonObject.getString("days_60_os"));
                                highslabel_60days.setTypeface(Typeface.DEFAULT);
                                highslabel_60days.setGravity(Gravity.RIGHT);
                                highslabel_60days.setBackgroundColor(getResources().getColor(R.color.col_60));

                                TextView highslabel_90days = initPlainTextView(i);
                                highslabel_90days.setText(jsonObject.getString("days_90_os"));
                                highslabel_90days.setTypeface(Typeface.DEFAULT);
                                highslabel_90days.setGravity(Gravity.RIGHT);
                                highslabel_90days.setBackgroundColor(getResources().getColor(R.color.col_90));

                                TextView highslabel_90abovedays = initPlainTextView(i);
                                highslabel_90abovedays.setText(jsonObject.getString("days_above_90_os"));
                                highslabel_90abovedays.setTypeface(Typeface.DEFAULT);
                                highslabel_90abovedays.setGravity(Gravity.RIGHT);
                                highslabel_90abovedays.setBackgroundColor(getResources().getColor(R.color.col_ab_90));

                                TextView highslabel_nettpayable = initPlainTextView(i);
                                highslabel_nettpayable.setText(jsonObject.getString("nettpayable"));
                                highslabel_nettpayable.setTypeface(Typeface.DEFAULT);
                                highslabel_nettpayable.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_description);
                                tblrowLabels.addView(highslabel_netinvoiceamt);
                                tblrowLabels.addView(highslabel_30days);
                                tblrowLabels.addView(highslabel_45days);
                                tblrowLabels.addView(highslabel_60days);
                                tblrowLabels.addView(highslabel_90days);
                                tblrowLabels.addView(highslabel_90abovedays);
                                tblrowLabels.addView(highslabel_nettpayable);
                                tablesalesregister.addView(tblrowLabels);

                                fnetinvoiceamt+= Double.valueOf(jsonObject.getString("netinvoiceamt"));
                                f30days+= Double.valueOf(jsonObject.getString("days_30_os"));
                                f45days+= Double.valueOf(jsonObject.getString("days_45_os"));
                                f60days+= Double.valueOf(jsonObject.getString("days_60_os"));
                                f90days+= Double.valueOf(jsonObject.getString("days_90_os"));
                                f90abovedays+= Double.valueOf(jsonObject.getString("days_above_90_os"));
                                fnettpayable+= Double.valueOf(jsonObject.getString("nettpayable"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(B2BOutstandingLocationActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_netinvoiceamt = initPlainFooterTextView();
                        highsFooter_netinvoiceamt.setText(Utility.DoubleToString(fnetinvoiceamt));
                        highsFooter_netinvoiceamt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_netinvoiceamt.setGravity(Gravity.RIGHT);


                        TextView highsFooter_30days = initPlainFooterTextView();
                        highsFooter_30days.setText(Utility.DoubleToString(f30days));
                        highsFooter_30days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_30days.setGravity(Gravity.RIGHT);
                        highsFooter_30days.setBackgroundColor(getResources().getColor(R.color.col_30));

                        TextView highsFooter_45days = initPlainFooterTextView();
                        highsFooter_45days.setText(Utility.DoubleToString(f45days));
                        highsFooter_45days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_45days.setGravity(Gravity.RIGHT);
                        highsFooter_45days.setBackgroundColor(getResources().getColor(R.color.col_45));

                        TextView highsFooter_60days = initPlainFooterTextView();
                        highsFooter_60days.setText(Utility.DoubleToString(f60days));
                        highsFooter_60days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_60days.setGravity(Gravity.RIGHT);
                        highsFooter_60days.setBackgroundColor(getResources().getColor(R.color.col_60));

                        TextView highsFooter_90days = initPlainFooterTextView();
                        highsFooter_90days.setText(Utility.DoubleToString(f90days));
                        highsFooter_90days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_90days.setGravity(Gravity.RIGHT);
                        highsFooter_90days.setBackgroundColor(getResources().getColor(R.color.col_90));

                        TextView highsFooter_90abovedays = initPlainFooterTextView();
                        highsFooter_90abovedays.setText(Utility.DoubleToString(f90abovedays));
                        highsFooter_90abovedays.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_90abovedays.setGravity(Gravity.RIGHT);
                        highsFooter_90abovedays.setBackgroundColor(getResources().getColor(R.color.col_ab_90));

                        TextView highsFooter_nettpayable = initPlainFooterTextView();
                        highsFooter_nettpayable.setText(Utility.DoubleToString(fnettpayable));
                        highsFooter_nettpayable.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_nettpayable.setGravity(Gravity.RIGHT);


                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_netinvoiceamt);

                        tblrowFooter.addView(highsFooter_30days);
                        tblrowFooter.addView(highsFooter_45days);
                        tblrowFooter.addView(highsFooter_60days);
                        tblrowFooter.addView(highsFooter_90days);
                        tblrowFooter.addView(highsFooter_90abovedays);
                        tblrowFooter.addView(highsFooter_nettpayable);

                        tablesalesregister.addView(tblrowFooter);

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

        TextView textView = new TextView(B2BOutstandingLocationActivity.this);
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
        TextView textView = new TextView(B2BOutstandingLocationActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(B2BOutstandingLocationActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}


