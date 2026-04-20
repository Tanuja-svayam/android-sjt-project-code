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

public class Outstanding_finance extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;
    TextView header_salesregister;
    String fromdate, todate, companycd, locationname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeledger);

        Intent indent = getIntent();

        fromdate = indent.getStringExtra("fromdate");
        todate = indent.getStringExtra("todate");
        companycd = indent.getStringExtra("companycd");
        locationname= indent.getStringExtra("locationname");

        invokeWS_FinanceList();

    }

    public void invokeWS_FinanceList() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("fromdate", fromdate);
            paramsMap.put("todate", todate);
            paramsMap.put("companycd", companycd);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText("Finance Outstanding For " + locationname);

            ApiHelper.post(URL + "Service1.asmx/Mobile_LocationFinanceOutstanding", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Outstanding_finance.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Finance");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Amount");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                       tblrowHeading.addView(highsheading_Value);

                        tablesalesregister.addView(tblrowHeading);

                        int fValue;
                        fValue = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("financecompanyoutstanding");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String fincompanycd;
                                TableRow tblrowLabels = new TableRow(Outstanding_finance.this);
                                fincompanycd = jsonObject.getString("fincompanycd");

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("location"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(Outstanding_finance.this, Outstanding_Finance_View.class);
                                        intent.putExtra("fromdate",fromdate);
                                        intent.putExtra("todate",todate);
                                        intent.putExtra("companycd",companycd);
                                        intent.putExtra("companyname",locationname);
                                        intent.putExtra("fincompanycd",fincompanycd);
                                        intent.putExtra("financelocation",highsLabel_category.getText());
                                        startActivity(intent);
                                    }
                                });

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("amount"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highslabel_Value);

                                tablesalesregister.addView(tblrowLabels);

                                //fQuantity += Integer.valueOf(0+jsonObject.getString("quantity"));
                                //fValue += Integer.valueOf(0+jsonObject.getString("value"));

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

	        TextView textView = new TextView(Outstanding_finance.this);
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
	        TextView textView = new TextView(Outstanding_finance.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

	    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(Outstanding_finance.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
