package com.shrikantelectronics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockActivityLocation extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationstock;

    TextView header_toplocationstock;
    String brandname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Intent i = getIntent();
        brandname = i.getStringExtra("brandname");

        invokeWS_LocationStock(brandname);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_exportpdf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        if (id == R.id.btnExportToPdf) {

            ScrollView activityLayout;
            activityLayout = findViewById(R.id.activity_scrollview);
            Utility.exportScrollViewContentToPdf(StockActivityLocation.this,activityLayout);
        }

        if (id == R.id.btnExportToExcel) {

            try {
                TableLayout activityLayout;
                activityLayout = findViewById(R.id.tabletoplocationstock);
                Utility.ExportTableLayoutContentToExcel(StockActivityLocation.this,activityLayout);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(StockActivityLocation.this,model_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoStockAgeingSummery(){
        Intent homeIntent = new Intent(StockActivityLocation.this,StockActivityAgeingLocationBrand.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    public void navigatetoStockActvitySummery_Category(){
        Intent customerIntent = new Intent(StockActivityLocation.this,StockActivityLocation.class);
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoStockActvitySummery_Brand(){
        Intent customerIntent = new Intent(StockActivityLocation.this,StockActivityBrand.class);
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }


    public void navigatetoLocationStockSummery(){
        Intent customerIntent = new Intent(StockActivityLocation.this,StockActivityLocationCategory.class);
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }


    public void navigatetoLocationStockSummery_Daily(){
        Intent customerIntent = new Intent(StockActivityLocation.this,LocationStockSummery_Daily_List.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoPlanogram(){
        Intent customerIntent = new Intent(StockActivityLocation.this,company_view.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void invokeWS_LocationStock(String brandname) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("companycd", "0");
            paramsMap.put("brandname", brandname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText(brandname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/Stock_LocationStockSummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {


                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(StockActivityLocation.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Location");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Quantity");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsHeading_NAlue = initPlainHeaderTextView();
                        highsHeading_NAlue.setText("Value");
                        highsHeading_NAlue.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_NAlue.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsHeading_NAlue);

                        tabletoplocationstock.addView(tblrowHeading);

                        int fQuantity;
                        int fNAlue;

                        fQuantity = 0;
                        fNAlue = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("top_location_stock");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(StockActivityLocation.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("companysname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highsLabel_NAlue = initPlainTextView(i);
                                highsLabel_NAlue.setText(jsonObject.getString("stock_value"));
                                highsLabel_NAlue.setTypeface(Typeface.DEFAULT);
                                highsLabel_NAlue.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highsLabel_NAlue);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

                                fQuantity += Integer.valueOf(0+jsonObject.getString("quantity"));
                                fNAlue += Integer.valueOf(0+jsonObject.getString("stock_value"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(StockActivityLocation.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fQuantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_SN = initPlainFooterTextView();
                        highsFooter_SN.setText(String.valueOf(fNAlue));
                        highsFooter_SN.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_SN.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_SN);

                        tabletoplocationstock.addView(tblrowFooter);


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

           TextView textView = new TextView(StockActivityLocation.this);
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
           TextView textView = new TextView(StockActivityLocation.this);
           textView.setPadding(10, 10, 10, 10);
           textView.setBackgroundResource(R.drawable.cell_shape_header);
           textView.setTextColor(Color.parseColor("#ffffff"));
           return textView;
       }

       private TextView initPlainFooterTextView() {
           TextView textView = new TextView(StockActivityLocation.this);
           textView.setPadding(10, 10, 10, 10);
           textView.setBackgroundResource(R.drawable.cell_shape_footer);
           textView.setTextColor(Color.parseColor("#ffffff"));
           return textView;
    }


}
