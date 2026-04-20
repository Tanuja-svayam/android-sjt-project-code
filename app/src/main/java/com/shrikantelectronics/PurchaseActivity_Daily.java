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

public class PurchaseActivity_Daily extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String monthname;
    String locationname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_daily);

        Intent i = getIntent();
        monthname = i.getStringExtra("monthname");
        locationname= i.getStringExtra("locationname");

        invokeWS_DailyPurchase(locationname, monthname);

    }

    public void invokeWS_DailyPurchase(String locationname, String monthname) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("locationname", locationname);
            paramsMap.put("monthname", monthname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText(locationname + " " + monthname + " DAILY PURCHASE");

            ApiHelper.post(URL + "Service1.asmx/Purchase_DailySummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        //tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(PurchaseActivity_Daily.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_vendorname = initPlainHeaderTextView();
                        highsHeading_vendorname.setText("Supplier");
                        highsHeading_vendorname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_vendorname.setGravity(Gravity.LEFT);

                        TextView highsHeading_refdocumentno = initPlainHeaderTextView();
                        highsHeading_refdocumentno.setText("Invoice");
                        highsHeading_refdocumentno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_refdocumentno.setGravity(Gravity.LEFT);

                        TextView highsHeading_purorderdt = initPlainHeaderTextView();
                        highsHeading_purorderdt.setText("Date");
                        highsHeading_purorderdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_purorderdt.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Quantity");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Value");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        //tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_purorderdt);
                        tblrowHeading.addView(highsHeading_vendorname);
                        tblrowHeading.addView(highsHeading_refdocumentno);
                        //tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);

                        tablesalesregister.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("purchase_daily");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String sysvendorno;

                                sysvendorno=jsonObject.getString("sysvendorno");

                                TableRow tblrowLabels = new TableRow(PurchaseActivity_Daily.this);

                                TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("locationname"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_vendorname = initPlainTextView(i);
                                highsLabel_vendorname.setText(jsonObject.getString("vendorname"));
                                highsLabel_vendorname.setTypeface(Typeface.DEFAULT);
                                highsLabel_vendorname.setGravity(Gravity.LEFT);

                                TextView highsLabel_refdocumentno = initPlainTextView(i);
                                highsLabel_refdocumentno.setText(jsonObject.getString("refdocumentno"));
                                highsLabel_refdocumentno.setTypeface(Typeface.DEFAULT);
                                highsLabel_refdocumentno.setGravity(Gravity.LEFT);

                                TextView highsLabel_purorderdt = initPlainTextView(i);
                                highsLabel_purorderdt.setText(jsonObject.getString("vpurorderdt"));
                                highsLabel_purorderdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_purorderdt.setGravity(Gravity.CENTER);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("value"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);


                                //tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_purorderdt);
                                tblrowLabels.addView(highsLabel_vendorname);
                                tblrowLabels.addView(highsLabel_refdocumentno);

                               // tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);

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

	        TextView textView = new TextView(PurchaseActivity_Daily.this);
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
	        TextView textView = new TextView(PurchaseActivity_Daily.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(PurchaseActivity_Daily.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
