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

public class ProfitabilityActivityLocationBrand_Model extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tabletoplocationstock;

    TextView header_toplocationstock;
    String brandname;
    String locationname;
    String categoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Intent i = getIntent();
        brandname = i.getStringExtra("brandname");
        locationname= i.getStringExtra("locationname");
        categoryname= i.getStringExtra("categoryname");


        if (brandname ==null)
        {
            brandname="";
        }

        if (categoryname ==null)
        {
            categoryname="";
        }

        if (locationname ==null)
        {
            locationname="";
        }


        invokeWS_TopBrandStock(brandname,locationname,categoryname);

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
            navigatetoStockActvitySummery_Brand();
        } else if (id == R.id.action_categorywise) {
            navigatetoStockActvitySummery_Category();
        //} else if (id == R.id.action_modelstock) {
       //     navigatetoModelListActivity();
     //   } else if (id == R.id.action_locationageingstock) {
     ///       navigatetoStockAgeingSummery();
    //    } else if (id == R.id.action_TodaysStockMovement) {
    //        navigatetoLocationStockSummery_Daily();
        }

        return super.onOptionsItemSelected(item);
    }
*/

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(ProfitabilityActivityLocationBrand_Model.this,model_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoStockAgeingSummery(){
        Intent homeIntent = new Intent(ProfitabilityActivityLocationBrand_Model.this,StockActivityAgeingLocationBrand.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoStockActvitySummery_Category(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand_Model.this,StockActivityCategory.class);
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoStockActvitySummery_Brand(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand_Model.this, ProfitabilityActivityLocationBrand_Model.class);
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoLocationStockSummery(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand_Model.this,StockActivityLocationCategory.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }


    public void navigatetoLocationStockSummery_Daily(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand_Model.this,LocationStockSummery_Daily_List.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoPlanogram(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand_Model.this,company_view.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void invokeWS_TopBrandStock(String brandname, String locationname, String categoryname) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("locationname", locationname);
            paramsMap.put("brandname", brandname);
            paramsMap.put("categoryname", categoryname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText(locationname + "-" + brandname + "-" + categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/Stock_LocationStock_Brand_Model", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                      TableRow tblrowHeading = new TableRow(ProfitabilityActivityLocationBrand_Model.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Model");
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

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("top_category_stock");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(ProfitabilityActivityLocationBrand_Model.this);

                                final String sysmodelno;

                                sysmodelno = jsonObject.getString("sysmodelno");

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("modelname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand_Model.this, model_view_single.class);
                                        intent.putExtra("sysmodelno",sysmodelno);
                                        startActivity(intent);
                                   }
                                });

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.CENTER);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("stock_value"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

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

	        TextView textView = new TextView(ProfitabilityActivityLocationBrand_Model.this);
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
	        TextView textView = new TextView(ProfitabilityActivityLocationBrand_Model.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

	    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(ProfitabilityActivityLocationBrand_Model.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }


}
