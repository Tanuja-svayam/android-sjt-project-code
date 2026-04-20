package com.shrikantelectronics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KPIActivity_Details_Brand_Category_Location extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String ValueType, YearType;
    String Brand_ID;
    String Category_ID;
    String Type;
    String ReportName;
    String location ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        Intent i = getIntent();

        Type = i.getStringExtra("TYPE");
        ValueType = i.getStringExtra("VALUETYPE");
        Brand_ID = i.getStringExtra("Brand_ID");
        Category_ID = i.getStringExtra("Category_ID");
        ReportName= i.getStringExtra("ReportName");
        YearType= i.getStringExtra("YearType");
        location= i.getStringExtra("location");

        invokeWS_MontlySales();
    }

    public void invokeWS_MontlySales() {

        try {
            Map<String, String> paramsMap = new HashMap<>();
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysemployeeno  = globalVariable.getsysemployeeno();
            final String groupcode = globalVariable.getgroupcode();
            if (groupcode.equals("SAG")) {
                paramsMap.put("sysemployeeno", "0");
            }
            else
            {
                paramsMap.put("sysemployeeno", "0"+sysemployeeno);
            }
            paramsMap.put("companycd", location);
            paramsMap.put("type", "LOCATION");
            paramsMap.put("ValueType", ValueType);
            paramsMap.put("Brand_ID", Brand_ID);
            paramsMap.put("Category_ID", Category_ID);
            paramsMap.put("ReportName", ReportName);
            paramsMap.put("YearType", YearType);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText(ValueType + " Values In Lacs" +  " " + location);

            ApiHelper.post(URL + "Service1.asmx/KPI_DetailValues_Location", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(KPIActivity_Details_Brand_Category_Location.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Brand");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_LY_Quantity = initPlainHeaderTextView();
                        highsHeading_LY_Quantity.setText("LY Qty");
                        highsHeading_LY_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_LY_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsheading_LY_Value = initPlainHeaderTextView();
                        highsheading_LY_Value.setText("LY Value");
                        highsheading_LY_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_LY_Value.setGravity(Gravity.RIGHT);

                        TextView highsheading_TY_Quantity = initPlainHeaderTextView();
                        highsheading_TY_Quantity.setText("CY Qty");
                        highsheading_TY_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_TY_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsheading_TY_Value = initPlainHeaderTextView();
                        highsheading_TY_Value.setText("CY Value");
                        highsheading_TY_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_TY_Value.setGravity(Gravity.RIGHT);

                        TextView highsHeading_Quantity_Growth = initPlainHeaderTextView();
                        highsHeading_Quantity_Growth.setText("Qty Growth");
                        highsHeading_Quantity_Growth.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity_Growth.setGravity(Gravity.RIGHT);

                        TextView highsHeading_Value_Growth = initPlainHeaderTextView();
                        highsHeading_Value_Growth.setText("Value Growth");
                        highsHeading_Value_Growth.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Value_Growth.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);

                        tblrowHeading.addView(highsHeading_LY_Quantity);
                        tblrowHeading.addView(highsheading_LY_Value);

                        tblrowHeading.addView(highsheading_TY_Quantity);
                        tblrowHeading.addView(highsheading_TY_Value);

                        tblrowHeading.addView(highsHeading_Quantity_Growth);
                        tblrowHeading.addView(highsHeading_Value_Growth);

                        tablesalesregister.addView(tblrowHeading);

                        double fQuantity_ly;
                        double fValue_ly;

                        double fQuantity_ty;
                        double fValue_ty;

                        double fQuantity_growth;
                        double fValue_growth;

                        fQuantity_ly = 0;
                        fValue_ly = 0;

                        fQuantity_ty = 0;
                        fValue_ty = 0;

                        fQuantity_growth = 0;
                        fValue_growth = 0;

                        double d_quantity_year_last;
                        double d_value_year_last;
                        double d_quantity_year_current;
                        double d_value_year_current;
                        double d_quantity_growth;
                        double d_value_growth;

                        d_quantity_year_last = 0;
                        d_value_year_last = 0;
                        d_quantity_year_current = 0;
                        d_value_year_current = 0;
                        d_quantity_growth = 0;
                        d_value_growth = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("kpidashboardvalue_brand");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(KPIActivity_Details_Brand_Category_Location.this);

                                final String s;
                                s = jsonObject.getString("ID");

                               final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("location"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                d_quantity_year_last = 0;
                                d_value_year_last = 0;
                                d_quantity_year_current = 0;
                                d_value_year_current = 0;

                                d_quantity_year_last = Double.valueOf(jsonObject.getString("quantity_year_last"));
                                d_value_year_last = Double.valueOf(jsonObject.getString("value_year_last"));

                                d_quantity_year_current = Double.valueOf(jsonObject.getString("quantity_year_current"));
                                d_value_year_current = Double.valueOf(jsonObject.getString("value_year_current"));

                                d_quantity_growth = Double.valueOf(jsonObject.getString("quantity_growth"));
                                d_value_growth = Double.valueOf(jsonObject.getString("value_growth"));

                                TextView highsLabel_LY_Quantity = initPlainTextView(i);
                                highsLabel_LY_Quantity.setText(Utility.DoubleToString(d_quantity_year_last));
                                highsLabel_LY_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_LY_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_LY_Value = initPlainTextView(i);
                                highslabel_LY_Value.setText(Utility.DoubleToString(d_value_year_last));
                                highslabel_LY_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_LY_Value.setGravity(Gravity.RIGHT);

                                TextView highsLabel_TY_Quantity = initPlainTextView(i);
                                highsLabel_TY_Quantity.setText(Utility.DoubleToString(d_quantity_year_current));
                                highsLabel_TY_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_TY_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_TY_Value = initPlainTextView(i);
                                highslabel_TY_Value.setText(Utility.DoubleToString(d_value_year_current));
                                highslabel_TY_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_TY_Value.setGravity(Gravity.RIGHT);

                                TextView highsLabel_Quantity_Growth = initPlainTextView(i);
                                highsLabel_Quantity_Growth.setText(Utility.DoubleToString(d_quantity_growth));
                                highsLabel_Quantity_Growth.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity_Growth.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value_Growth = initPlainTextView(i);
                                highslabel_Value_Growth.setText(Utility.DoubleToString(d_value_growth));
                                highslabel_Value_Growth.setTypeface(Typeface.DEFAULT);
                                highslabel_Value_Growth.setGravity(Gravity.RIGHT);

                                fQuantity_ly += d_quantity_year_last;
                                fValue_ly += d_value_year_last;

                                fQuantity_ty += d_quantity_year_current;
                                fValue_ty += d_value_year_current;

                                fQuantity_growth += d_quantity_growth;
                                fValue_growth += d_value_growth;

                                if (d_quantity_growth < 0)
                                {
                                    highsLabel_Quantity_Growth.setTextColor(Color.parseColor("#c90c0c"));
                                }
                                else
                                {
                                    highsLabel_Quantity_Growth.setTextColor(Color.parseColor("#23c1c5"));
                                };

                                if (d_value_growth < 0)
                                {
                                    highslabel_Value_Growth.setTextColor(Color.parseColor("#c90c0c"));
                                }
                                else
                                {
                                    highslabel_Value_Growth.setTextColor(Color.parseColor("#23c1c5"));
                                };

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_LY_Quantity);
                                tblrowLabels.addView(highslabel_LY_Value);
                                tblrowLabels.addView(highsLabel_TY_Quantity);
                                tblrowLabels.addView(highslabel_TY_Value);

                                tblrowLabels.addView(highsLabel_Quantity_Growth);
                                tblrowLabels.addView(highslabel_Value_Growth);

                                tablesalesregister.addView(tblrowLabels);

                                d_quantity_year_last=0;
                                d_value_year_last=0;
                                d_quantity_year_current=0;
                                d_value_year_current=0;
                                d_quantity_growth=0;
                                d_value_growth=0;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(KPIActivity_Details_Brand_Category_Location.this);
                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText("TOTAL");
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_LY_Quantity = initPlainFooterTextView();
                        highsFooter_LY_Quantity.setText(Utility.DoubleToString(fQuantity_ly));
                        highsFooter_LY_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_LY_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsFooter_LY_Value = initPlainFooterTextView();
                        highsFooter_LY_Value.setText(Utility.DoubleToString(fValue_ly));
                        highsFooter_LY_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_LY_Value.setGravity(Gravity.RIGHT);

                        TextView highsFooter_TY_Quantity = initPlainFooterTextView();
                        highsFooter_TY_Quantity.setText(Utility.DoubleToString(fQuantity_ty));
                        highsFooter_TY_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_TY_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsFooter_TY_Value = initPlainFooterTextView();
                        highsFooter_TY_Value.setText(Utility.DoubleToString(fValue_ty));
                        highsFooter_TY_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_TY_Value.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Quantity_Growth = initPlainFooterTextView();
                        highsFooter_Quantity_Growth.setText("");
                        highsFooter_Quantity_Growth.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Quantity_Growth.setGravity(Gravity.RIGHT);

                        TextView highsFooter_Value_Growth = initPlainFooterTextView();
                        highsFooter_Value_Growth.setText("");
                        highsFooter_Value_Growth.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_Value_Growth.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_LY_Quantity);
                        tblrowFooter.addView(highsFooter_LY_Value);
                        tblrowFooter.addView(highsFooter_TY_Quantity);
                        tblrowFooter.addView(highsFooter_TY_Value);
                        tblrowFooter.addView(highsFooter_Quantity_Growth);
                        tblrowFooter.addView(highsFooter_Value_Growth);

                        tablesalesregister.addView(tblrowFooter);

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

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }


	    private TextView initPlainTextView(float n) {

	        TextView textView = new TextView(KPIActivity_Details_Brand_Category_Location.this);
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
	        TextView textView = new TextView(KPIActivity_Details_Brand_Category_Location.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

	    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(KPIActivity_Details_Brand_Category_Location.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
