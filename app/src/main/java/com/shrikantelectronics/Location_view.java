package com.shrikantelectronics;

import android.app.ProgressDialog;
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

public class Location_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;

    String sysinvorderno ="0";
    String sysorderdtlno ="0";
    String custcd ="0";
    String refbillno ="";
    String sysemployeeno ="0";
    String custname = "";
    String netinvoiceamt;
    String invoicedt;
    String sysmodelno1 ="0";
    String modelcode1 ="";;

    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();

        sysinvorderno = i.getStringExtra("sysinvorderno");
        sysorderdtlno = i.getStringExtra("sysorderdtlno");
        custcd = i.getStringExtra("custcd");
        custname= i.getStringExtra("custname");
        refbillno = i.getStringExtra("refbillno");
        sysmodelno1 = i.getStringExtra("sysmodelno");
        modelcode1 = i.getStringExtra("modelcode");
        invoicedt = i.getStringExtra("vinvoicedt");
        netinvoiceamt = i.getStringExtra("netinvoiceamt");

        invokeWS_LocationList();
    }

    public void invokeWS_LocationList() {

        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysuserno", sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            //          header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/PhysicalLocationDetail", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Location_view.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("ID");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Location");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("locations");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Location_view.this);

                                final String pick_companycd = jsonObject.getString("companycd");

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("companysname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                final TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("companyname"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.LEFT);
                                highsLabel_Quantity.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        if (Utility.isNotNull(pick_companycd)) {

                                            invokeUpdatePickLocationInOrderWS(pick_companycd);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Please select the pick location", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

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

    public void invokeUpdatePickLocationInOrderWS(String pick_companycd){
        // Show Progress Dialog
        prgDialog.show();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysinvorderno", sysinvorderno);
        paramsMap.put("sysorderdtlno", sysorderdtlno);
        paramsMap.put("pick_companycd", pick_companycd);
        paramsMap.put("companycd", companycd);
        paramsMap.put("userid", userid);

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/UpdatePickLocationInSalesOrder", paramsMap, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Location_view.this, SalesOrder_add_model.class);

                    intent.putExtra("sysinvorderno",sysinvorderno);
                    intent.putExtra("sysorderdtlno",sysorderdtlno);
                    intent.putExtra("refbillno",refbillno);
                    intent.putExtra("custcd",custcd);
                    intent.putExtra("custname",custname);
                    intent.putExtra("sysmodelno",sysmodelno1);
                    intent.putExtra("modelcode",modelcode1);
                    intent.putExtra("vinvoicedt",invoicedt);
                    intent.putExtra("netinvoiceamt",netinvoiceamt);
                    startActivity(intent);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
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
    }


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Location_view.this);
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
        TextView textView = new TextView(Location_view.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Location_view.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


}
