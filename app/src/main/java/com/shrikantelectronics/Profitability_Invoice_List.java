package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Profitability_Invoice_List extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    TableLayout tablesalesregister;

    String sysinvno;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ArrayList<ProfitabilityInvoice> arraylist = new ArrayList<ProfitabilityInvoice>();

    String brandname, categoryname,fromdt, todt, companycd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profitability_invoice_list);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        fromdt = i.getStringExtra("fromdt");
        todt = i.getStringExtra("todt");
        companycd = i.getStringExtra("companycd");
        brandname = i.getStringExtra("brandname");
        categoryname = i.getStringExtra("categoryname");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(0);

        invokeWS_Invoicelist();

    }

    public void invokeWS_Invoicelist() {

        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("fromdt", fromdt);
            paramsMap.put("todt", todt);
            paramsMap.put("companycd", "0"+companycd);
            paramsMap.put("brandname", ""+ brandname);
            paramsMap.put("categoryname", ""+categoryname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Profitability_Invoice_Details", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("profitability_Invoice_Product");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                ProfitabilityInvoice wp = new ProfitabilityInvoice(
                                        jsonObject.getString("Row_id"),
                                        jsonObject.getString("id"),
                                        jsonObject.getString("sysproductno"),
                                        jsonObject.getString("sysbrandno"),
                                        jsonObject.getString("location"),
                                        jsonObject.getString("executive_name"),
                                        jsonObject.getString("invoice_no"),
                                        jsonObject.getString("invoice_date"),
                                        jsonObject.getString("invoicedt"),
                                        jsonObject.getString("customer_name"),
                                        jsonObject.getString("brand"),
                                        jsonObject.getString("model"),
                                        jsonObject.getString("top_category"),
                                        jsonObject.getString("parent_category"),
                                        jsonObject.getString("category"),
                                        jsonObject.getString("serial_no"),
                                        jsonObject.getString("installer_name"),
                                        jsonObject.getString("installer_contact"),
                                        jsonObject.getString("slc_at_sale"),
                                        jsonObject.getString("kitty"),
                                        jsonObject.getString("remarks"),
                                        jsonObject.getString("gstn"),
                                        jsonObject.getString("customer_mobile"),
                                        jsonObject.getString("alternate_number"),
                                        jsonObject.getString("hsncode"),
                                        jsonObject.getString("ageingfromdate"),
                                        jsonObject.getString("ageing"),
                                        jsonObject.getString("basic"),
                                        jsonObject.getString("quantity"),
                                        jsonObject.getString("cgst_taxrate"),
                                        jsonObject.getString("cgst"),
                                        jsonObject.getString("sgst_taxrate"),
                                        jsonObject.getString("sgst"),
                                        jsonObject.getString("igst_taxrate"),
                                        jsonObject.getString("igst"),
                                        jsonObject.getString("tax"),
                                        jsonObject.getString("unitprice_include_tax"),
                                        jsonObject.getString("purchaseprice"),
                                        jsonObject.getString("gp"),
                                        jsonObject.getString("invoiceorder_creditnote_amount"),
                                        jsonObject.getString("transportcharges"),
                                        jsonObject.getString("installationcharges"),
                                        jsonObject.getString("bankcharges"),
                                        jsonObject.getString("overheads"),
                                        jsonObject.getString("exchangeshortexcess"),
                                        jsonObject.getString("salesexpenses"),
                                        jsonObject.getString("pricedropvalue"),
                                        jsonObject.getString("cdsupport"),
                                        jsonObject.getString("selloutsupport"),
                                        jsonObject.getString("general_creditnote"),
                                        jsonObject.getString("scheme_creditnote"),
                                        jsonObject.getString("purchasediscount"),
                                        jsonObject.getString("netsalesamount"),
                                        jsonObject.getString("net_nlc_including_pd_so"),
                                        jsonObject.getString("profitloss"));
                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter = new RecyclerAdapter_Invoice(Profitability_Invoice_List.this, arraylist);

                        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
                        // Binds the Adapter to the ListView
                        recyclerView.setAdapter(adapter);

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
                    //     prgDialog.hide();
                    // When Http response code is '404'
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    // When Http response code is '500'

                    // When Http response code other than 404, 500
                                    }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }
}

