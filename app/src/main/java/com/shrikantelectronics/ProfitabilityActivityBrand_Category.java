package com.shrikantelectronics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

public class ProfitabilityActivityBrand_Category extends AppCompatActivity {


    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationprofitability;
    TextView dummyforvalue;

    TextView header_toplocationprofitability;
    String brandname,fromdt, todt, companycd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profitability_list_catbrand);

        Intent i = getIntent();
        fromdt = i.getStringExtra("fromdt");
        todt = i.getStringExtra("todt");
        companycd = i.getStringExtra("companycd");
        brandname = i.getStringExtra("brandname");

        invokeWS_TopCategoryProfitability(brandname);

    }

    public void invokeWS_TopCategoryProfitability(String brandname) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("fromdt", fromdt);
            paramsMap.put("todt", todt);
            paramsMap.put("companycd", companycd);
            paramsMap.put("brandname", brandname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationprofitability= (TextView) findViewById(R.id.header_toplocationprofitability);
            header_toplocationprofitability.setText(brandname + "");

            dummyforvalue= (TextView) findViewById(R.id.dummyforvalue);
            dummyforvalue.setText(brandname);


            ApiHelper.post(URL + "Service1.asmx/Profitability_TOPCategoryProfitabilitySummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {


                        tabletoplocationprofitability  = (TableLayout) findViewById(R.id.tabletoplocationprofitability);

                        tabletoplocationprofitability.setStretchAllColumns(true);
                        tabletoplocationprofitability.setShrinkAllColumns(true);

                      TableRow tblrowHeading = new TableRow(ProfitabilityActivityBrand_Category.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Category");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Quantity");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Value");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);
                        tabletoplocationprofitability.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("top_category_profitability");

                        double fQuantity;
                        double fValue;

                        fQuantity = 0;
                        fValue = 0;

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(ProfitabilityActivityBrand_Category.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("categoryname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityBrand_Category.this, ProfitabilityActivityLocationBrand_Category_Model.class);
                                        intent.putExtra("fromdt",fromdt);
                                        intent.putExtra("todt",todt);
                                        intent.putExtra("companycd",companycd);
                                        intent.putExtra("brandname",dummyforvalue.getText());
                                        intent.putExtra("categoryname",highsLabel_category.getText());
                                        startActivity(intent);
                                    }
                                });

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("profitability_value"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                if (Double.valueOf(jsonObject.getString("profitability_value")) < 0)
                                {
                                    highsLabel_Quantity.setTextColor(Color.parseColor("#c90c0c"));
                                    highslabel_Value.setTextColor(Color.parseColor("#c90c0c"));
                                }
                                else
                                {
                                    highsLabel_Quantity.setTextColor(Color.parseColor("#000000"));
                                    highslabel_Value.setTextColor(Color.parseColor("#000000"));
                                };

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);

                                tblrowLabels.setClickable(true);

                                tabletoplocationprofitability.addView(tblrowLabels);

                                fQuantity += Double.valueOf(jsonObject.getString("quantity"));
                                fValue += Double.valueOf(jsonObject.getString("profitability_value"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(ProfitabilityActivityBrand_Category.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fQuantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        //highsFooter_ra.setText(String.valueOf(fValue));
                        highsFooter_ra.setText(String.format("%.2f", fValue));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);

                        tabletoplocationprofitability.addView(tblrowFooter);

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

        TextView textView = new TextView(ProfitabilityActivityBrand_Category.this);
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
        TextView textView = new TextView(ProfitabilityActivityBrand_Category.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(ProfitabilityActivityBrand_Category.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}
