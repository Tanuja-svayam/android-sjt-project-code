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

public class StockNegetiveSummery extends AppCompatActivity {


    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tabletoplocationstock;

    TextView header_toplocationstock;
    String categoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Intent i = getIntent();
        categoryname = i.getStringExtra("categoryname");

        invokeWS_NegetiveStock(categoryname);

    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(StockNegetiveSummery.this,model_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

       public void navigatetoStockActvitySummery_Category(){
        Intent customerIntent = new Intent(StockNegetiveSummery.this,StockActivityCategory.class);
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }


    public void navigatetoStockActvitySummery_Location(){
        Intent customerIntent = new Intent(StockNegetiveSummery.this,StockActivityLocation.class);
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }



    public void navigatetoNegetiveSummery(){
        Intent customerIntent = new Intent(StockNegetiveSummery.this,StockNegetiveSummery.class);
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoStockActvitySummery_Brand(){
        Intent customerIntent = new Intent(StockNegetiveSummery.this,StockNegetiveSummery.class);
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoLocationStockSummery(){
        Intent customerIntent = new Intent(StockNegetiveSummery.this,StockActivityLocationBrand.class);
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoLocationStockSummery_Daily(){
        Intent customerIntent = new Intent(StockNegetiveSummery.this,LocationStockSummery_Daily_List.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoPlanogram(){
        Intent customerIntent = new Intent(StockNegetiveSummery.this,company_view.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoStockAgeingSummery(){
        Intent homeIntent = new Intent(StockNegetiveSummery.this,StockActivityAgeingLocationBrand.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    public void invokeWS_NegetiveStock(String categoryname) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("sysbrandno", "0");
            paramsMap.put("categoryname", categoryname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText(categoryname + " STOCK");


            ApiHelper.post(URL + "Service1.asmx/Stock_NegetiveSummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {


                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                      TableRow tblrowHeading = new TableRow(StockNegetiveSummery.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Brand");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_model = initPlainHeaderTextView();
                        highsHeading_model.setText("Model");
                        highsHeading_model.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_model.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Stock");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsHeading_DelPending = initPlainHeaderTextView();
                        highsHeading_DelPending.setText("Del. Pending");
                        highsHeading_DelPending.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_DelPending.setGravity(Gravity.CENTER);

                        TextView highsHeading_PoPending = initPlainHeaderTextView();
                        highsHeading_PoPending.setText("PO Pending");
                        highsHeading_PoPending.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_PoPending.setGravity(Gravity.CENTER);

                        TextView highsHeading_NStock = initPlainHeaderTextView();
                        highsHeading_NStock.setText("Stortage");
                        highsHeading_NStock.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_NStock.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_model);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsHeading_DelPending);
                        tblrowHeading.addView(highsHeading_PoPending);
                        tblrowHeading.addView(highsHeading_NStock);

                        tabletoplocationstock.addView(tblrowHeading);

                        int fQuantity;
                        int fValue;

                        fQuantity = 0;
                        fValue = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("top_category_stock");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(StockNegetiveSummery.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("brandname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                final TextView highsLabel_model = initPlainTextView(i);
                                highsLabel_model.setText(jsonObject.getString("modelname"));
                                highsLabel_model.setTypeface(Typeface.DEFAULT);
                                highsLabel_model.setGravity(Gravity.LEFT);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("bd_stock"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_delivery_pending = initPlainTextView(i);
                                highslabel_delivery_pending.setText(jsonObject.getString("delivery_pending"));
                                highslabel_delivery_pending.setTypeface(Typeface.DEFAULT);
                                highslabel_delivery_pending.setGravity(Gravity.RIGHT);

                                TextView highslabel_pending_po = initPlainTextView(i);
                                highslabel_pending_po.setText(jsonObject.getString("pending_po"));
                                highslabel_pending_po.setTypeface(Typeface.DEFAULT);
                                highslabel_pending_po.setGravity(Gravity.RIGHT);


                                TextView highslabel_balancestock = initPlainTextView(i);
                                highslabel_balancestock.setText(jsonObject.getString("balancestock"));
                                highslabel_balancestock.setTypeface(Typeface.DEFAULT);
                                highslabel_balancestock.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_model);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_delivery_pending);
                                tblrowLabels.addView(highslabel_pending_po);
                                tblrowLabels.addView(highslabel_balancestock);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

                               // fQuantity += Integer.valueOf(0+jsonObject.getString("quantity"));
                            //    fValue += Integer.valueOf(0+jsonObject.getString("stock_value"));

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

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }


    }




       private TextView initPlainTextView(float n) {

           TextView textView = new TextView(StockNegetiveSummery.this);
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
           TextView textView = new TextView(StockNegetiveSummery.this);
           textView.setPadding(10, 10, 10, 10);
           textView.setBackgroundResource(R.drawable.cell_shape_header);
           textView.setTextColor(Color.parseColor("#ffffff"));
           return textView;
       }

       private TextView initPlainFooterTextView() {
           TextView textView = new TextView(StockNegetiveSummery.this);
           textView.setPadding(10, 10, 10, 10);
           textView.setBackgroundResource(R.drawable.cell_shape_footer);
           textView.setTextColor(Color.parseColor("#ffffff"));
           return textView;
    }


}
