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

public class SalesAnalysisCategoryBrand_Model extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String monthname;
    String locationname, brandname, categoryname, fromdate,todate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesanalysiscategorybrand_model);

        Intent i = getIntent();
        fromdate = i.getStringExtra("fromdate");
        todate = i.getStringExtra("todate");
        locationname= i.getStringExtra("locationname");
        categoryname= i.getStringExtra("categoryname");
        brandname= i.getStringExtra("brandname");

        invokeWS_DailySales();

    }

     // String sysemployeeno, String fromdate, String todate, String brandname

    public void invokeWS_DailySales() {

        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

            final String sysemployeeno  = globalVariable.getsysemployeeno();
            final String groupcode = globalVariable.getgroupcode();

            Map<String, String> paramsMap = new HashMap<>();
            if (groupcode.equals("SAG")) {
                paramsMap.put("sysemployeeno", "0");
            }
            else
            {
                paramsMap.put("sysemployeeno", "0"+sysemployeeno);
            }
            paramsMap.put("fromdate", fromdate);
            paramsMap.put("todate", todate);
            paramsMap.put("categoryname", categoryname);
            paramsMap.put("brandname", brandname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText(fromdate  + "-" + todate + "[" + locationname + " " + brandname + " " + categoryname +"]");

            ApiHelper.post(URL + "Service1.asmx/SalesAnalysis_Brand_Category_Model", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        //tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(SalesAnalysisCategoryBrand_Model.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Model");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Quantity");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Value");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        TextView highsheading_percent = initPlainHeaderTextView();
                        highsheading_percent.setText("%");
                        highsheading_percent.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_percent.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);
                        tblrowHeading.addView(highsheading_percent);

                        tablesalesregister.addView(tblrowHeading);

                        int fQTY;
                        int fINVOICE_AMT;
                        int fPERCENTSHARE;

                        fQTY = 0;
                        fINVOICE_AMT = 0;
                        fPERCENTSHARE = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_analysis_brand_category");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String custcd;

                                final String sysmodelno;
                                sysmodelno = jsonObject.getString("ID");

                                TableRow tblrowLabels = new TableRow(SalesAnalysisCategoryBrand_Model.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("STORE"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("QTY"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("INVOICE_AMT"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                TextView highslabel_percent = initPlainTextView(i);
                                highslabel_percent.setText(jsonObject.getString("PERCENTSHARE"));
                                highslabel_percent.setTypeface(Typeface.DEFAULT);
                                highslabel_percent.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);
                                tblrowLabels.addView(highslabel_percent);

                                tblrowLabels.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(SalesAnalysisCategoryBrand_Model.this, SalesAnalysisCategoryBrand_Invoice.class);
                                        intent.putExtra("sysmodelno",sysmodelno);
                                        intent.putExtra("fromdate",fromdate);
                                        intent.putExtra("todate",todate);
                                        intent.putExtra("locationname",locationname);
                                        intent.putExtra("brandname",brandname);
                                        intent.putExtra("categoryname",categoryname);
                                        startActivity(intent);
                                    }
                                });


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

	        TextView textView = new TextView(SalesAnalysisCategoryBrand_Model.this);
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
	        TextView textView = new TextView(SalesAnalysisCategoryBrand_Model.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(SalesAnalysisCategoryBrand_Model.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
