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

public class PurchaseAnalysisBrand_Category extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationstock;

    TextView header_toplocationstock;
    String brandname;
    String fromdate;
    String todate;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_analysis);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        brandname = i.getStringExtra("brandname");

        fromdate = i.getStringExtra("fromdate");
        todate = i.getStringExtra("todate");

        invokeWS_TopCategoryStock(brandname);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_brandwise) {
          //  navigatetoStockActvitySummery_Brand();
        } else if (id == R.id.action_categorywise) {
           // navigatetoStockActvitySummery_Category();
        } else if (id == R.id.action_modelstock) {
          //  navigatetoModelListActivity();
        } else if (id == R.id.action_TodaysStockMovement) {
          //  navigatetoLocationStockSummery_Daily();
        }

        return super.onOptionsItemSelected(item);
    }
*/

    public void invokeWS_TopCategoryStock(String brandname) {

        try {
            prgDialog.show();

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("fromdate", fromdate);
            paramsMap.put("todate", todate);
            paramsMap.put("brandname", brandname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText(brandname + " Purchase");

            ApiHelper.post(URL + "Service1.asmx/PurchaseAnalysis_Brand_Category", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {
                        prgDialog.hide();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                     //   tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(PurchaseAnalysisBrand_Category.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Category");
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

                        tabletoplocationstock.addView(tblrowHeading);

                        double fQTY;
                        double fINVOICE_AMT;
                        double fPERCENTSHARE;
                        double fINVOICE_AMT_TOTAL;

                        fQTY = 0.00;
                        fINVOICE_AMT = 0.00;
                        fPERCENTSHARE = 0;
                        fINVOICE_AMT_TOTAL= 0.00;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("purchase_analysis_brand_category");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                fINVOICE_AMT = jsonObject.getDouble("INVOICE_AMT");

                                TableRow tblrowLabels = new TableRow(PurchaseAnalysisBrand_Category.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("STORE"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("QTY"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(String.format("%.2f",fINVOICE_AMT));
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

                                tblrowLabels.setClickable(true);
                                tblrowLabels.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(PurchaseAnalysisBrand_Category.this, PurchaseAnalysisCategoryBrand_Model.class);
                                        intent.putExtra("fromdate",fromdate);
                                        intent.putExtra("todate",todate);
                                        intent.putExtra("locationname","");
                                        intent.putExtra("brandname",brandname);
                                        intent.putExtra("categoryname",highsLabel_category.getText().toString());
                                        startActivity(intent);
                                    }
                                });

                                tabletoplocationstock.addView(tblrowLabels);

                                fQTY += Integer.valueOf(0+jsonObject.getString("QTY"));
                                fINVOICE_AMT_TOTAL = fINVOICE_AMT_TOTAL + fINVOICE_AMT ;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(PurchaseAnalysisBrand_Category.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("TOTAL");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText(String.valueOf(fQTY));
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.RIGHT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fINVOICE_AMT_TOTAL));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText("");
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);

                        tabletoplocationstock.addView(tblrowFooter);

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

        TextView textView = new TextView(PurchaseAnalysisBrand_Category.this);
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
        TextView textView = new TextView(PurchaseAnalysisBrand_Category.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(PurchaseAnalysisBrand_Category.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


    @SuppressWarnings("deprecation")
    public void RefreshData(View view) {
        invokeWS_TopCategoryStock(brandname);
    }

}
