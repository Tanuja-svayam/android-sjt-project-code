package com.shrikantelectronics;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class model_view_single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    public ImageLoader imageLoader;

    private ListView lv_model_location ;

    ArrayList<HashMap<String, String>> customerlist;
    ListViewAdapter_Model_Location adapter;
    ArrayList<Model_Location> arraylist = new ArrayList<Model_Location>();

    TableLayout tabletoplocationstock;

    TextView header_toplocationstock;
    TextView txtsysmodelno;
    TextView txtmodelcode;
    TextView txtmrp;
    TextView txtdp;
    TextView txtslc;
    TextView txtgtm_cayegory;
    TextView txtminquantity;
    TextView txtgst_category_purchase_account;
    TextView txtgst_category_sales_account;
    TextView txtcgst_taxname;
    TextView txtsgst_taxname;
    TextView txtigst_taxname;
    TextView txtstock;
    ImageView imgmodelimage;

    String groupcode;

    String sysmodelno1;
    String modelcode1;
    String mrp1;
    String dp1;
    String stock1;
    ImageView modelimage;
    private Button calculate_incentive_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_view_single);

        Intent i = getIntent();
        sysmodelno1 = i.getStringExtra("sysmodelno");
        modelcode1 = i.getStringExtra("modelcode");
        // Get the results of mrp
        mrp1 = i.getStringExtra("mrp");
        // Get the results of dp
        dp1 = i.getStringExtra("dp");
        // Get the results of modelimage
        stock1 = i.getStringExtra("stock");

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        groupcode  = globalVariable.getgroupcode();

        // Locate the TextViews in singleitemview.xml
        txtmodelcode = (TextView) findViewById(R.id.modelcode1);
        txtmrp = (TextView) findViewById(R.id.mrp1);
        txtdp = (TextView) findViewById(R.id.dp1);

        txtslc = (TextView) findViewById(R.id.slc);
       // txtgtm_cayegory = (TextView) findViewById(R.id.gtm_cayegory);
        txtminquantity = (TextView) findViewById(R.id.minquantity);
        txtgst_category_purchase_account = (TextView) findViewById(R.id.gst_category_purchase_account);
        txtgst_category_sales_account = (TextView) findViewById(R.id.gst_category_sales_account);
        txtcgst_taxname = (TextView) findViewById(R.id.cgst_taxname);
        txtsgst_taxname = (TextView) findViewById(R.id.sgst_taxname);
        txtigst_taxname = (TextView) findViewById(R.id.igst_taxname);

        txtstock = (TextView) findViewById(R.id.stock1);

        txtmodelcode.setText(modelcode1);
        txtmrp.setText(mrp1);
        txtdp.setText(dp1);
        txtstock.setText(stock1);

        calculate_incentive_button= (Button) findViewById(R.id.calculate_incentive_button);

        calculate_incentive_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                navigatetoIncentiveCalculatorActivity();
            }
        });

        GetProductData_Single(sysmodelno1);

     //   GetModelLocationData(sysmodelno1);
     //   invokeWS_SerialStock(sysmodelno1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_model_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.btnSerialList) {

            try {

                navigatetoModelSerialListActivity();

            } catch (ActivityNotFoundException a) {

            }

            return true;
        }


        if (id == R.id.action_New) {
            navigatetoModelCreateActivity();
        }

        if (id == R.id.btnLedger) {

            try {
                navigatetoModelLedgerActivity();
            } catch (ActivityNotFoundException a) {

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void navigatetoModelCreateActivity(){
        Intent homeIntent = new Intent(model_view_single.this,Procurement_Model_Create.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,99);
    }

    public void navigatetoModelLedgerActivity(){
        Intent homeIntent = new Intent(model_view_single.this,model_view_ledger.class);
        homeIntent.putExtra("sysmodelno",sysmodelno1);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoModelSerialListActivity(){

        Intent homeIntent = new Intent(model_view_single.this,model_view_seriallist.class);
        homeIntent.putExtra("sysmodelno",sysmodelno1);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }


    public void invokeWS_SerialStock(String sysmodelno1) {

        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("modelcode", sysmodelno1);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
  //          header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetModelLocationDetails_SerialNos", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(model_view_single.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Location");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Serial");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsHeading_serial_netlancost = initPlainHeaderTextView();
                        highsHeading_serial_netlancost.setText("NLC");
                        highsHeading_serial_netlancost.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_serial_netlancost.setGravity(Gravity.CENTER);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Age");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);


                        if (groupcode.equals("SAG")) {
                            tblrowHeading.addView(highsHeading_serial_netlancost);
                        }
                        tblrowHeading.addView(highsheading_Value);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(model_view_single.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("companyname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("balancestock"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_serial_netlancost = initPlainTextView(i);
                                highslabel_serial_netlancost.setText(jsonObject.getString("serial_netlancost"));
                                highslabel_serial_netlancost.setTypeface(Typeface.DEFAULT);
                                highslabel_serial_netlancost.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("delivery_pending"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                if (groupcode.equals("SAG")) {
                                    tblrowLabels.addView(highslabel_serial_netlancost);
                                }
                                tblrowLabels.addView(highslabel_Value);

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

    public void GetProductData_Single(String sysmodelno){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysmodelno", sysmodelno);
        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            imageLoader = new ImageLoader(model_view_single.this);

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetModelDetails_Single", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;

                        JSONArray new_array = obj.getJSONArray("products");

                        JSONObject jsonObject = new_array.getJSONObject(0);

                        String imageurlpath = IMAGEURL + "ProductImage/" + jsonObject.getString("imageurl");

                        TextView mSpecification = (TextView) findViewById(R.id.mSpecification);
                        mSpecification.setText(jsonObject.getString("mSpecification"));

                        txtmodelcode.setText(jsonObject.getString("modelcode"));
                        txtmrp.setText(jsonObject.getString("mrp"));
                        txtdp.setText(jsonObject.getString("dp"));

                        txtslc.setText(jsonObject.getString("slc"));
                       // txtgtm_cayegory.setText(jsonObject.getString("gtm_cayegory"));
                        txtminquantity.setText(jsonObject.getString("minquantity"));
                        txtgst_category_purchase_account.setText(jsonObject.getString("gst_category_purchase_account"));
                        txtgst_category_sales_account.setText(jsonObject.getString("gst_category_sales_account"));
                        txtcgst_taxname.setText(jsonObject.getString("cgst_taxname"));
                        txtsgst_taxname.setText(jsonObject.getString("sgst_taxname"));
                        txtigst_taxname.setText(jsonObject.getString("igst_taxname"));
                        txtstock.setText(jsonObject.getString("stock"));

                        final String sSpecification = jsonObject.getString("mSpecification");

                        txtmodelcode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sSpecification));
                                startActivity(intent);
                            }
                        });

                        ImageView image = (ImageView) findViewById(R.id.modelimage_single);

                        //DisplayImage function from ImageLoader Class
                        imageLoader.DisplayImage(imageurlpath, image);


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
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetModelLocationData(String sysmodelno1){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("modelcode", sysmodelno1);

            invokeWS_ModelLocation(paramsMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeWS_ModelLocation(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetModelLocationDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                Model_Location wp = new Model_Location(jsonObject.getString("companycd"), jsonObject.getString("sysmodelno"), jsonObject.getString("companyname"), jsonObject.getString("lifetime_sales"), jsonObject.getString("recent_sales"), jsonObject.getString("bd_stock"), jsonObject.getString("delivery_pending"),jsonObject.getString("balancestock"));

                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lv_model_location  = (ListView) findViewById(R.id.list_view );

                        adapter = new ListViewAdapter_Model_Location(model_view_single.this, arraylist);

                        lv_model_location.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

                                    }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(model_view_single.this);
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
        TextView textView = new TextView(model_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(model_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    public void navigatetoIncentiveCalculatorActivity(){
        Intent homeIntent = new Intent(model_view_single.this,IncentiveCalculatorActivity.class);
        homeIntent.putExtra("sysmodelno",sysmodelno1);

        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}
