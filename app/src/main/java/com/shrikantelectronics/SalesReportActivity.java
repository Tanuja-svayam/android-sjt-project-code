package com.shrikantelectronics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SalesReportActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    private RecyclerView recyclerView;
    private SalesReportAdapter adapter;
    private List<SalesReport> salesReportList;


    TextView header_salesregister, tvQuantityValue, tvSalesValue;
    String fromdate,quantity,value;
    String locationname,type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        recyclerView = findViewById(R.id.recyclerViewSales);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = getIntent();
        fromdate = i.getStringExtra("fromdate");
        locationname= i.getStringExtra("locationname");
        type= i.getStringExtra("type");
        quantity= i.getStringExtra("quantity");
        value= i.getStringExtra("value");

        tvQuantityValue= (TextView) findViewById(R.id.tvQuantityValue);
        tvSalesValue= (TextView) findViewById(R.id.tvSalesValue);

        tvQuantityValue.setText(quantity);
        tvSalesValue.setText(value);




        if (type.equals("ORDER"))
        {
            invokeWS_DailySales_Order(locationname, fromdate);
        }
        else
        {
            invokeWS_DailySales_Invoice(locationname, fromdate);
        }


        // Dummy Data (Replace with actual database data)
        /*
        salesReportList = new ArrayList<>();
        salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
        salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
        salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
        salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
        salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
        salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
        salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
        salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
        salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
        salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
        salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
        salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
        salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
        salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
        salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));

        adapter = new SalesReportAdapter(salesReportList);
        recyclerView.setAdapter(adapter);
        */
    }




    public void invokeWS_DailySales_Order(String locationname, String fromdate) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("locationname", locationname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);
            salesReportList = new ArrayList<>();

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText(locationname + " " + fromdate + " DAILY ORDERS");

            ApiHelper.post(URL + "Service1.asmx/Sales_DailySummary_Orders", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_daily");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String custcd;

                                custcd=jsonObject.getString("custcd");

                                // Dummy Data (Replace with actual database data)

                                salesReportList.add(new SalesReport(jsonObject.getString("invorderno"), jsonObject.getString("vinvorderdt"), jsonObject.getString("modelname"), jsonObject.getString("custname"), jsonObject.getString("quantity"), jsonObject.getString("value"), jsonObject.getString("locationname"),  jsonObject.getString("salesmenname"),  jsonObject.getString("model_instock_qty"),  jsonObject.getString("profitloss"), jsonObject.getString("sysinvno"), jsonObject.getString("custcd")));

/*                              salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
                                salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
                                salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
                                salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
                                salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
                                salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
                                salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
                                salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
                                salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
                                salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
                                salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));
                                salesReportList.add(new SalesReport("Model X", "John Doe", 2, 50000.0, "New York", "Michael"));
                                salesReportList.add(new SalesReport("Model Y", "Jane Smith", 1, 30000.0, "Los Angeles", "Sarah"));
                                salesReportList.add(new SalesReport("Model Z", "Chris Brown", 3, 75000.0, "Chicago", "David"));

                                */

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter = new SalesReportAdapter(SalesReportActivity.this, salesReportList);
                        recyclerView.setAdapter(adapter);
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

    public void invokeWS_DailySales_Invoice(String locationname, String fromdate) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("locationname", locationname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            salesReportList = new ArrayList<>();

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
           header_salesregister.setText(locationname + " " + fromdate + " DAILY INVOICES");

            ApiHelper.post(URL + "Service1.asmx/Sales_DailySummary_Invoices", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {


                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_daily");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String custcd;
                                custcd=jsonObject.getString("custcd");

                                salesReportList.add(new SalesReport(jsonObject.getString("invorderno"), jsonObject.getString("vinvorderdt"), jsonObject.getString("modelname"), jsonObject.getString("custname"), jsonObject.getString("quantity"), jsonObject.getString("value"), jsonObject.getString("locationname"),  jsonObject.getString("salesmenname"),  jsonObject.getString("model_instock_qty"),  jsonObject.getString("profitloss"), jsonObject.getString("sysinvno"), jsonObject.getString("custcd")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter = new SalesReportAdapter(SalesReportActivity.this, salesReportList);
                        recyclerView.setAdapter(adapter);

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

}
