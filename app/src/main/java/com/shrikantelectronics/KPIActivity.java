package com.shrikantelectronics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class KPIActivity extends AppCompatActivity {

    CardView mycardsales ;
    CardView mycardeventsales ;
    CardView mycardpurchase ;
    CardView mycardmargin ;

    Intent i_eventsales ;
    Intent i_sales ;
    Intent i_purchase ;
    Intent i_margin ;

    LinearLayout ll;

    TextView header_toplocationstock, header_yeartype;

    TextView eventsales_value_year_current;
    TextView eventsales_growth_value;

    TextView sales_value_year_current;
    TextView sales_growth_value;

    TextView purchase_value_year_current;
    TextView purchase_growth_value;

    TextView margin_value_year_current;
    TextView margin_growth_value;

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String ValueType, YearType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kpi);

        Intent indent = getIntent();
        ValueType = indent.getStringExtra("VALUETYPE");
        YearType = indent.getStringExtra("YearType");

        alertDialog();

    }

    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Select Year Type");
        dialog.setTitle("");
        dialog.setPositiveButton("Calender Year",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(),"Calender year is selected",Toast.LENGTH_LONG).show();
                        YearType = "CALENDER";
                        header_yeartype= (TextView) findViewById(R.id.header_yeartype);
                        header_yeartype.setText("Date Range : Calender Year");

                        invokeWS_DashBoardValues();
                    }
                });
        dialog.setNegativeButton("Fiscal Year",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Fiscal year is selected",Toast.LENGTH_LONG).show();
                YearType = "FISCAL";
                header_yeartype= (TextView) findViewById(R.id.header_yeartype);
                header_yeartype.setText("Date Range : Fiscal Year");

                invokeWS_DashBoardValues();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_kpiactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_ytd) {
            ValueType= "YTD";
            navigatetoKPIActivity();
        } else if (id == R.id.action_mtd) {
            ValueType= "MTD";
            navigatetoKPIActivity();
        } else if (id == R.id.action_dtd) {
            ValueType= "DTD";
            navigatetoKPIActivity();
        } else if (id == R.id.action_golm) {
            ValueType= "GOLM";
            navigatetoKPIActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigatetoKPIActivity(){
        Intent customerIntent = new Intent(getApplicationContext(),KPIActivity.class);
        customerIntent.putExtra("VALUETYPE",ValueType);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void invokeWS_DashBoardValues() {

        try {
            ll = (LinearLayout) findViewById(R.id.ll);
            mycardeventsales = (CardView) findViewById(R.id.eventsales);
            i_eventsales = new Intent(this,SalesAnalysisEvent.class);
            i_eventsales.putExtra("type","Q");
            mycardeventsales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i_eventsales);
                }
            });

            ll = (LinearLayout) findViewById(R.id.ll);
            mycardsales = (CardView) findViewById(R.id.sales);
            i_sales = new Intent(this,KPIActivity_Details_Brand.class);
            i_sales.putExtra("VALUETYPE",ValueType);
            i_sales.putExtra("ID","");
            i_sales.putExtra("ReportName","SALES");
            i_sales.putExtra("YearType",YearType);
            i_sales.putExtra("location","");
            mycardsales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i_sales);
                }
            });


            ll = (LinearLayout) findViewById(R.id.ll);
            mycardpurchase = (CardView) findViewById(R.id.purchase);
            i_purchase = new Intent(this,KPIActivity_Details_Brand.class);
            i_purchase.putExtra("VALUETYPE",ValueType);
            i_purchase.putExtra("ID","");
            i_purchase.putExtra("ReportName","PURCHASE");
            i_purchase.putExtra("YearType",YearType);
            i_purchase.putExtra("location","");
            mycardpurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i_purchase);
                }
            });

            ll = (LinearLayout) findViewById(R.id.ll);
            mycardmargin = (CardView) findViewById(R.id.margin);
            i_margin = new Intent(this,KPIActivity_Details_Brand.class);
            i_margin.putExtra("VALUETYPE",ValueType);
            i_margin.putExtra("ID","");
            i_margin.putExtra("ReportName","MARGIN");
            i_margin.putExtra("YearType",YearType);
            i_margin.putExtra("location","");
            mycardmargin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i_margin);
                }
            });

            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            header_toplocationstock.setText(ValueType+ " Values In Lacs");


            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("companycd", "0");
            paramsMap.put("ValueType", ValueType);
            paramsMap.put("YearType", YearType);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

               ApiHelper.post(URL + "Service1.asmx/KPI_DashboardValues", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        double fQuantity_growth;
                        double fValue_growth;

                        double fQuantity_this_year;
                        double fValue_this_year;

                        fQuantity_growth = 0;
                        fValue_growth = 0;

                        fQuantity_this_year = 0;
                        fValue_this_year = 0;

                        String sType = "";

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("kpidashboardvalue");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                fQuantity_growth = Double.valueOf(jsonObject.getString("quantity_growth"));
                                fValue_growth = Double.valueOf(jsonObject.getString("value_growth"));

                                fQuantity_this_year = Double.valueOf(jsonObject.getString("quantity_year_current"));
                                fValue_this_year = Double.valueOf(jsonObject.getString("value_year_current"));

                                sType = jsonObject.getString("location");

                                if  (sType.equals("SALES"))
                                {
                                    sales_value_year_current= (TextView) findViewById(R.id.sales_value_year_current);
                                    sales_value_year_current.setText(Utility.DoubleToString(fValue_this_year));

                                    sales_growth_value= (TextView) findViewById(R.id.sales_growth_value);
                                    sales_growth_value.setText(Utility.DoubleToString(fValue_growth) + "%");
                                    if (fValue_growth < 0)
                                    {
                                        sales_growth_value.setTextColor(Color.parseColor("#c90c0c"));
                                    }
                                    else
                                    {
                                        sales_growth_value.setTextColor(Color.parseColor("#23c1c5"));
                                    };
                                }

                                if  (sType.equals("PURCHASE"))
                                {
                                    purchase_value_year_current= (TextView) findViewById(R.id.purchase_value_year_current);
                                    purchase_value_year_current.setText(Utility.DoubleToString(fValue_this_year));

                                    purchase_growth_value= (TextView) findViewById(R.id.purchase_growth_value);
                                    purchase_growth_value.setText(Utility.DoubleToString(fValue_growth) + "%");
                                    if (fValue_growth < 0)
                                    {
                                        purchase_growth_value.setTextColor(Color.parseColor("#c90c0c"));
                                    }
                                    else
                                    {
                                        purchase_growth_value.setTextColor(Color.parseColor("#23c1c5"));
                                    };
                                }

                                if  (sType.equals("MARGIN"))
                                {
                                    margin_value_year_current= (TextView) findViewById(R.id.margin_value_year_current);
                                    margin_value_year_current.setText(Utility.DoubleToString(fValue_this_year));

                                    margin_growth_value= (TextView) findViewById(R.id.margin_growth_value);
                                    margin_growth_value.setText(Utility.DoubleToString(fValue_growth) + "%");
                                    if (fValue_growth < 0)
                                    {
                                        margin_growth_value.setTextColor(Color.parseColor("#c90c0c"));
                                    }
                                    else
                                    {
                                        margin_growth_value.setTextColor(Color.parseColor("#23c1c5"));
                                    };
                                }

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


}



