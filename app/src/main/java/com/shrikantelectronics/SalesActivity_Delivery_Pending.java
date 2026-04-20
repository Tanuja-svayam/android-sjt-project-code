package com.shrikantelectronics;

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

public class SalesActivity_Delivery_Pending extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String fromdate;
    String locationname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        Intent i = getIntent();
        locationname= i.getStringExtra("locationname");

        invokeWS_DailySales(locationname, fromdate);

    }

    public void navigatetoSalesPerformanceActivity(){
        Intent homeIntent = new Intent(SalesActivity_Delivery_Pending.this,SalesPerformance_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    public void invokeWS_DailySales(String locationname, String fromdate) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("locationname", locationname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText(locationname  + " DELIVERY PENDING");

            ApiHelper.post(URL + "Service1.asmx/Sales_Delivery_Pending", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(SalesActivity_Delivery_Pending.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_custname = initPlainHeaderTextView();
                        highsHeading_custname.setText("Description");
                        highsHeading_custname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_custname.setGravity(Gravity.LEFT);

                        TextView highsHeading_invorderno = initPlainHeaderTextView();
                        highsHeading_invorderno.setText("Invoice");
                        highsHeading_invorderno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_invorderno.setGravity(Gravity.LEFT);

                        TextView highsHeading_invorderdt = initPlainHeaderTextView();
                        highsHeading_invorderdt.setText("Date");
                        highsHeading_invorderdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_invorderdt.setGravity(Gravity.LEFT);

                        //tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_invorderdt);
                        tblrowHeading.addView(highsHeading_custname);
                        tblrowHeading.addView(highsHeading_invorderno);

                        tablesalesregister.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_delivery_pending");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String custcd;

                                custcd=jsonObject.getString("custcd");

                                TableRow tblrowLabels = new TableRow(SalesActivity_Delivery_Pending.this);

                                TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("locationname"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_custname = initPlainTextView(i);
                                highsLabel_custname.setText(jsonObject.getString("custname"));
                                highsLabel_custname.setTypeface(Typeface.DEFAULT);
                                highsLabel_custname.setWidth(200);
                                highsLabel_custname.setGravity(Gravity.LEFT);
                                highsLabel_custname.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(SalesActivity_Delivery_Pending.this, customer_view_single.class);
                                        intent.putExtra("custcd",custcd);
                                        startActivity(intent);
                                    }
                                });

                                TextView highsLabel_invorderno = initPlainTextView(i);
                                highsLabel_invorderno.setText(jsonObject.getString("invorderno"));
                                highsLabel_invorderno.setTypeface(Typeface.DEFAULT);
                                highsLabel_invorderno.setGravity(Gravity.LEFT);


                                TextView highsLabel_invorderdt = initPlainTextView(i);
                                highsLabel_invorderdt.setText(jsonObject.getString("vinvorderdt"));
                                highsLabel_invorderdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_invorderdt.setGravity(Gravity.CENTER);


                                //tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_invorderdt);
                                tblrowLabels.addView(highsLabel_custname);
                                tblrowLabels.addView(highsLabel_invorderno);

                                tablesalesregister.addView(tblrowLabels);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //    LinearLayout sv = new LinearLayout(MainActivity.this);

                        //        sv.addView(table);

                        //hsw.addView(sv);
                        //setContentView(hsw);

                        // setContentView(table);

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

                    // When Http response code other than 404, 500
                                    }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }


	    private TextView initPlainTextView(float n) {

	        TextView textView = new TextView(SalesActivity_Delivery_Pending.this);
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
	        TextView textView = new TextView(SalesActivity_Delivery_Pending.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

	    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(SalesActivity_Delivery_Pending.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
