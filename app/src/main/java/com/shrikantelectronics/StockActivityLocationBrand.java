//package com.shrikantelectronics;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ScrollView;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import java.util.Map;
//import java.util.HashMap;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class StockActivityLocationBrand extends AppCompatActivity {
//
//    private static String URL = Config.WEBSERVICE_URL;
//    private static String IMAGEURL = Config.IMAGEURL_URL;
//
//    OnClickListener tablerowOnClickListener;
//
//    TableLayout tabletoplocationstock;
//
//    TextView header_toplocationstock;
//    String categoryname;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_location_stock);
//
//        Intent i = getIntent();
//        categoryname = i.getStringExtra("categoryname");
//
//        invokeWS_TopCategoryLocationStock(categoryname);
//
//    }
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_locationstockactivity, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.btnExportToPdf) {
//
//            ScrollView activityLayout;
//            activityLayout = findViewById(R.id.activity_scrollview);
//            Utility.exportScrollViewContentToPdf(StockActivityLocationBrand.this,activityLayout);
//        }
//
//        if (id == R.id.btnExportToExcel) {
//
//            try {
//                TableLayout activityLayout;
//                activityLayout = findViewById(R.id.tabletoplocationstock);
//                Utility.ExportTableLayoutContentToExcel(StockActivityLocationBrand.this,activityLayout);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (id == R.id.action_brandwise) {
//            navigatetoStockActvitySummery_Brand();
//        } else if (id == R.id.action_categorywise) {
//            navigatetoStockActvitySummery_Category();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void navigatetoModelListActivity(){
//        Intent homeIntent = new Intent(StockActivityLocationBrand.this,model_view.class);
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
//    }
//
//    public void navigatetoStockAgeingSummery(){
//        Intent homeIntent = new Intent(StockActivityLocationBrand.this,StockAgeingSummery.class);
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
//    }
//
//    public void navigatetoStockActvitySummery_Category(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,StockActivityLocationCategory.class);
//        customerIntent.putExtra("brandname","");
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoStockActvitySummery_Brand(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,StockActivityLocationBrand.class);
//        customerIntent.putExtra("categoryname","");
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoLocationStockSummery(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,LocationStockSummery_List.class);
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoLocationStockSummery_Daily(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,LocationStockSummery_Daily_List.class);
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoPlanogram(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,company_view.class);
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void invokeWS_TopCategoryLocationStock(String categoryname) {
//        try {
//            Map<String, String> paramsMap = new HashMap<>();
//
//            paramsMap.put("companycd", "0");
//            paramsMap.put("categoryname", categoryname);
//
//            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
//            //AsyncHttpClient client = new AsyncHttpClient();
//            //client.setTimeout(DEFAULT_TIMEOUT);
//
//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
//            header_toplocationstock.setText(categoryname + " STOCK");
//
//            ApiHelper.post(URL + "Service1.asmx/Stock_LocationStock_Brand", paramsMap, new ApiHelper.ApiCallback()   {
//                @Override
//
//                public void onSuccess(JSONObject response) {
//                    try {
//
//                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
//
//                        tabletoplocationstock.setStretchAllColumns(true);
//                        tabletoplocationstock.setShrinkAllColumns(true);
//
//                        TableRow tblrowHeading = new TableRow(StockActivityLocationBrand.this);
//                        TextView highsHeading_category = initPlainHeaderTextView();
//                        highsHeading_category.setText("Brand");
//                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_category.setGravity(Gravity.LEFT);
//
//                        TextView highsHeading_total = initPlainHeaderTextView();
//                        highsHeading_total.setText("TOTAL");
//                        highsHeading_total.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_total.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_SR = initPlainHeaderTextView();
//                        highsHeading_SR.setText("SR");
//                        highsHeading_SR.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_SR.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_MBO = initPlainHeaderTextView();
//                        highsHeading_MBO.setText("MBO");
//                        highsHeading_MBO.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_MBO.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_WB = initPlainHeaderTextView();
//                        highsHeading_WB.setText("WB");
//                        highsHeading_WB.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_WB.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_SH = initPlainHeaderTextView();
//                        highsHeading_SH.setText("SH");
//                        highsHeading_SH.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_SH.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_SS = initPlainHeaderTextView();
//                        highsHeading_SS.setText("SS");
//                        highsHeading_SS.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_SS.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_AG = initPlainHeaderTextView();
//                        highsHeading_AG.setText("AG");
//                        highsHeading_AG.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_AG.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_HG = initPlainHeaderTextView();
//                        highsHeading_HG.setText("HG");
//                        highsHeading_HG.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_HG.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_KC = initPlainHeaderTextView();
//                        highsHeading_KC.setText("KC");
//                        highsHeading_KC.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_KC.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_LO = initPlainHeaderTextView();
//                        highsHeading_LO.setText("LO");
//                        highsHeading_LO.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_LO.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_DEF = initPlainHeaderTextView();
//                        highsHeading_DEF.setText("DEF");
//                        highsHeading_DEF.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_DEF.setGravity(Gravity.CENTER);
//
//                        tblrowHeading.addView(highsHeading_category);
//                        tblrowHeading.addView(highsHeading_total);
//                        tblrowHeading.addView(highsHeading_SR);
//                        tblrowHeading.addView(highsHeading_MBO);
//                        tblrowHeading.addView(highsHeading_WB);
//                        tblrowHeading.addView(highsHeading_SH);
//                        tblrowHeading.addView(highsHeading_SS);
//                        tblrowHeading.addView(highsHeading_AG);
//                        tblrowHeading.addView(highsHeading_HG);
//                        tblrowHeading.addView(highsHeading_KC);
//                        tblrowHeading.addView(highsHeading_LO);
//                        tblrowHeading.addView(highsHeading_DEF);
//
//                        tabletoplocationstock.addView(tblrowHeading);
//
//                        int ftotal;
//                        int fAK;
//                        int fGN;
//                        int fSH;
//                        int fmbo;
//
//                        int fSK;
//                        int fSL;
//
//                        int fLN;
//                        int fSM;
//                        int fGSN;
//                        int fSP;
//
//                        int fSR;
//                        int fSA;
//                        int fSJ;
//                        int fWA;
//                        int fWD;
//                        int fLO;
//
//                        ftotal = 0;
//                        fAK = 0;
//                        fGN = 0;
//                        fSH = 0;
//
//                        fmbo = 0;
//                        fSK = 0;
//                        fSL = 0;
//
//                        fLN = 0;
//                        fSM = 0;
//                        fGSN = 0;
//                        fSP = 0;
//
//                        fSR = 0;
//                        fSA = 0;
//                        fSJ = 0;
//                        fWA = 0;
//                        fWD = 0;
//                        fLO = 0;
//
//                        JSONObject obj = response;
//                        JSONArray new_array = obj.getJSONArray("top_category_stock");
//
//                        for (int i = 0, count = new_array.length(); i < count; i++) {
//                            try {
//                                JSONObject jsonObject = new_array.getJSONObject(i);
//
//                                TableRow tblrowLabels = new TableRow(StockActivityLocationBrand.this);
//
//                                final TextView highsLabel_category = initPlainTextView(i);
//                                highsLabel_category.setText(jsonObject.getString("brandname"));
//                                highsLabel_category.setTypeface(Typeface.DEFAULT);
//                                highsLabel_category.setGravity(Gravity.LEFT);
//                                highsLabel_category.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Category.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_total = initPlainTextView(i);
//                                highsLabel_total.setText(jsonObject.getString("TOTAL"));
//                                highsLabel_total.setTypeface(Typeface.DEFAULT);
//                                highsLabel_total.setGravity(Gravity.CENTER);
//                                highsLabel_total.setTextColor(Color.parseColor("#ff670f"));
//                                highsLabel_total.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Category.class);
//                                        intent.putExtra("categoryname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","TOTAL");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_SR = initPlainTextView(i);
//                                highsLabel_SR.setText(jsonObject.getString("SR"));
//                                highsLabel_SR.setTypeface(Typeface.DEFAULT);
//                                highsLabel_SR.setGravity(Gravity.CENTER);
//                                highsLabel_SR.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","SR");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_MBO = initPlainTextView(i);
//                                highsLabel_MBO.setText(jsonObject.getString("MBO"));
//                                highsLabel_MBO.setTypeface(Typeface.DEFAULT);
//                                highsLabel_MBO.setGravity(Gravity.CENTER);
//                                highsLabel_MBO.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","MBO");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_WB = initPlainTextView(i);
//                                highsLabel_WB.setText(jsonObject.getString("WB"));
//                                highsLabel_WB.setTypeface(Typeface.DEFAULT);
//                                highsLabel_WB.setGravity(Gravity.CENTER);
//                                highsLabel_WB.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","WB");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_SH = initPlainTextView(i);
//                                highsLabel_SH.setText(jsonObject.getString("SH"));
//                                highsLabel_SH.setTypeface(Typeface.DEFAULT);
//                                highsLabel_SH.setGravity(Gravity.CENTER);
//                                highsLabel_SH.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","SH");
//                                        startActivity(intent);
//                                    }
//                                });
//
//
//                                final TextView highsLabel_SS = initPlainTextView(i);
//                                highsLabel_SS.setText(jsonObject.getString("SS"));
//                                highsLabel_SS.setTypeface(Typeface.DEFAULT);
//                                highsLabel_SS.setGravity(Gravity.CENTER);
//                                highsLabel_SS.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","SS");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_AG = initPlainTextView(i);
//                                highsLabel_AG.setText(jsonObject.getString("AG"));
//                                highsLabel_AG.setTypeface(Typeface.DEFAULT);
//                                highsLabel_AG.setGravity(Gravity.CENTER);
//                                highsLabel_AG.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","AG");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_HG = initPlainTextView(i);
//                                highsLabel_HG.setText(jsonObject.getString("HG"));
//                                highsLabel_HG.setTypeface(Typeface.DEFAULT);
//                                highsLabel_HG.setGravity(Gravity.CENTER);
//                                highsLabel_HG.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","HG");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_KC = initPlainTextView(i);
//                                highsLabel_KC.setText(jsonObject.getString("KC"));
//                                highsLabel_KC.setTypeface(Typeface.DEFAULT);
//                                highsLabel_KC.setGravity(Gravity.CENTER);
//                                highsLabel_KC.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","KC");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_LO = initPlainTextView(i);
//                                highsLabel_LO.setText(jsonObject.getString("LO"));
//                                highsLabel_LO.setTypeface(Typeface.DEFAULT);
//                                highsLabel_LO.setGravity(Gravity.CENTER);
//                                highsLabel_LO.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","LO");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_DEF = initPlainTextView(i);
//                                highsLabel_DEF.setText(jsonObject.getString("DEF"));
//                                highsLabel_DEF.setTypeface(Typeface.DEFAULT);
//                                highsLabel_DEF.setGravity(Gravity.CENTER);
//                                highsLabel_DEF.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","DEF");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                tblrowLabels.addView(highsLabel_category);
//                                tblrowLabels.addView(highsLabel_total);
//                                tblrowLabels.addView(highsLabel_SR);
//                                tblrowLabels.addView(highsLabel_MBO);
//                                tblrowLabels.addView(highsLabel_WB);
//                                tblrowLabels.addView(highsLabel_SH);
//                                tblrowLabels.addView(highsLabel_SS);
//                                tblrowLabels.addView(highsLabel_AG);
//                                tblrowLabels.addView(highsLabel_HG);
//                                tblrowLabels.addView(highsLabel_KC);
//                                tblrowLabels.addView(highsLabel_LO);
//                                tblrowLabels.addView(highsLabel_DEF);
//
//                                tblrowLabels.setClickable(true);
//
//                                tabletoplocationstock.addView(tblrowLabels);
//
//                                ftotal += Integer.valueOf(0+jsonObject.getString("TOTAL"));
//                                fAK += Integer.valueOf(0+jsonObject.getString("SR"));
//                                fmbo += Integer.valueOf(0+jsonObject.getString("MBO"));
//                                fGN += Integer.valueOf(0+jsonObject.getString("WB"));
//                                fSH += Integer.valueOf(0+jsonObject.getString("SH"));
//                                fSK += Integer.valueOf(0+jsonObject.getString("SS"));
//                                fSL += Integer.valueOf(0+jsonObject.getString("AG"));
//                                fLN += Integer.valueOf(0+jsonObject.getString("HG"));
//                                fSM += Integer.valueOf(0+jsonObject.getString("KC"));
//                                fLO += Integer.valueOf(0+jsonObject.getString("LO"));
//                                fWD += Integer.valueOf(0+jsonObject.getString("DEF"));
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        TableRow tblrowFooter = new TableRow(StockActivityLocationBrand.this);
//                        TextView highsFooter_category = initPlainFooterTextView();
//                        highsFooter_category.setText("Total");
//                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_category.setGravity(Gravity.LEFT);
//
//                        TextView highsFooter_total = initPlainFooterTextView();
//                        highsFooter_total.setText(String.valueOf(ftotal));
//                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_total.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_SR = initPlainFooterTextView();
//                        highsFooter_SR.setText(String.valueOf(fAK));
//                        highsFooter_SR.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_SR.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_MBO = initPlainFooterTextView();
//                        highsFooter_MBO.setText(String.valueOf(fmbo));
//                        highsFooter_MBO.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_MBO.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_WB = initPlainFooterTextView();
//                        highsFooter_WB.setText(String.valueOf(fGN));
//                        highsFooter_WB.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_WB.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_SH = initPlainFooterTextView();
//                        highsFooter_SH.setText(String.valueOf(fSH));
//                        highsFooter_SH.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_SH.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_SS = initPlainFooterTextView();
//                        highsFooter_SS.setText(String.valueOf(fSK));
//                        highsFooter_SS.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_SS.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_AG = initPlainFooterTextView();
//                        highsFooter_AG.setText(String.valueOf(fSL));
//                        highsFooter_AG.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_AG.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_HG = initPlainFooterTextView();
//                        highsFooter_HG.setText(String.valueOf(fLN));
//                        highsFooter_HG.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_HG.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_KC = initPlainFooterTextView();
//                        highsFooter_KC.setText(String.valueOf(fSM));
//                        highsFooter_KC.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_KC.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_LO = initPlainFooterTextView();
//                        highsFooter_LO.setText(String.valueOf(fLO));
//                        highsFooter_LO.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_LO.setGravity(Gravity.CENTER);
//
//
//                        TextView highsFooter_DEF = initPlainFooterTextView();
//                        highsFooter_DEF.setText(String.valueOf(fWD));
//                        highsFooter_DEF.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_DEF.setGravity(Gravity.CENTER);
//
//                        tblrowFooter.addView(highsFooter_category);
//                        tblrowFooter.addView(highsFooter_total);
//                        tblrowFooter.addView(highsFooter_SR);
//                        tblrowFooter.addView(highsFooter_MBO);
//                        tblrowFooter.addView(highsFooter_WB);
//                        tblrowFooter.addView(highsFooter_SH);
//                        tblrowFooter.addView(highsFooter_SS);
//                        tblrowFooter.addView(highsFooter_AG);
//                        tblrowFooter.addView(highsFooter_HG);
//                        tblrowFooter.addView(highsFooter_KC);
//                        tblrowFooter.addView(highsFooter_LO);
//                        tblrowFooter.addView(highsFooter_DEF);
//
//                        tabletoplocationstock.addView(tblrowFooter);
//
//                        //    LinearLayout sv = new LinearLayout(MainActivity.this);
//
//                        //        sv.addView(table);
//
//                        //hsw.addView(sv);
//                        //setContentView(hsw);
//
//                        // setContentView(table);
//
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//
//                        Toast.makeText(getApplicationContext(), "Status code :"+ e.toString() +"errmsg : "+e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                // When the response returned by REST has Http response code other than '200'
//                @Override
//                public void onFailure(String errorMessage) {
//                    // Hide Progress Dialog
//                    //     prgDialog.hide();
//                    // When Http response code is '404'
//                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
//
//                }
//            });
//        }catch (Exception e) {
//            e.printStackTrace();
//            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//
//
//    private TextView initPlainTextView(float n) {
//
//        TextView textView = new TextView(StockActivityLocationBrand.this);
//        textView.setPadding(10, 10, 10, 10);
//
//
//        if((n%2)==0)
//        {
//            textView.setBackgroundResource(R.drawable.cell_shape);
//        }
//        else
//        {
//            textView.setBackgroundResource(R.drawable.cell_shape_oddrow);
//        }
//
//
//        textView.setTextColor(Color.parseColor("#000000"));
//        return textView;
//    }
//
//    private TextView initPlainHeaderTextView() {
//        TextView textView = new TextView(StockActivityLocationBrand.this);
//        textView.setPadding(10, 10, 10, 10);
//        textView.setBackgroundResource(R.drawable.cell_shape_header);
//        textView.setTextColor(Color.parseColor("#ffffff"));
//        return textView;
//    }
//
//    private TextView initPlainFooterTextView() {
//        TextView textView = new TextView(StockActivityLocationBrand.this);
//        textView.setPadding(10, 10, 10, 10);
//        textView.setBackgroundResource(R.drawable.cell_shape_footer);
//        textView.setTextColor(Color.parseColor("#ffffff"));
//        return textView;
//    }
//}

//Hardcode location code below
//package com.shrikantelectronics;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ScrollView;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import java.util.Map;
//import java.util.HashMap;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class StockActivityLocationBrand extends AppCompatActivity {
//
//    private static String URL = Config.WEBSERVICE_URL;
//    private static String IMAGEURL = Config.IMAGEURL_URL;
//
//    OnClickListener tablerowOnClickListener;
//
//    TableLayout tabletoplocationstock;
//
//    TextView header_toplocationstock;
//    String categoryname;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_location_stock);
//
//        Intent i = getIntent();
//        categoryname = i.getStringExtra("categoryname");
//
//        invokeWS_TopCategoryLocationStock(categoryname);
//
//    }
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_locationstockactivity, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.btnExportToPdf) {
//
//            ScrollView activityLayout;
//            activityLayout = findViewById(R.id.activity_scrollview);
//            Utility.exportScrollViewContentToPdf(StockActivityLocationBrand.this,activityLayout);
//        }
//
//        if (id == R.id.btnExportToExcel) {
//
//            try {
//                TableLayout activityLayout;
//                activityLayout = findViewById(R.id.tabletoplocationstock);
//                Utility.ExportTableLayoutContentToExcel(StockActivityLocationBrand.this,activityLayout);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (id == R.id.action_brandwise) {
//            navigatetoStockActvitySummery_Brand();
//        } else if (id == R.id.action_categorywise) {
//            navigatetoStockActvitySummery_Category();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void navigatetoModelListActivity(){
//        Intent homeIntent = new Intent(StockActivityLocationBrand.this,model_view.class);
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
//    }
//
//    public void navigatetoStockAgeingSummery(){
//        Intent homeIntent = new Intent(StockActivityLocationBrand.this,StockAgeingSummery.class);
//        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
//    }
//
//    public void navigatetoStockActvitySummery_Category(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,StockActivityLocationCategory.class);
//        customerIntent.putExtra("brandname","");
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoStockActvitySummery_Brand(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,StockActivityLocationBrand.class);
//        customerIntent.putExtra("categoryname","");
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoLocationStockSummery(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,LocationStockSummery_List.class);
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoLocationStockSummery_Daily(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,LocationStockSummery_Daily_List.class);
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void navigatetoPlanogram(){
//        Intent customerIntent = new Intent(StockActivityLocationBrand.this,company_view.class);
//        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(customerIntent);
//    }
//
//    public void invokeWS_TopCategoryLocationStock(String categoryname) {
//        try {
//            Map<String, String> paramsMap = new HashMap<>();
//
//            paramsMap.put("companycd", "0");
//            paramsMap.put("categoryname", categoryname);
//
//            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
//            //AsyncHttpClient client = new AsyncHttpClient();
//            //client.setTimeout(DEFAULT_TIMEOUT);
//
//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
//            header_toplocationstock.setText(categoryname + " STOCK");
//
//            ApiHelper.post(URL + "Service1.asmx/Stock_LocationStock_Brand", paramsMap, new ApiHelper.ApiCallback()   {
//                @Override
//
//                public void onSuccess(JSONObject response) {
//                    try {
//
//                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
//
//                        tabletoplocationstock.setStretchAllColumns(true);
//                        tabletoplocationstock.setShrinkAllColumns(true);
//
//                        TableRow tblrowHeading = new TableRow(StockActivityLocationBrand.this);
//                        TextView highsHeading_category = initPlainHeaderTextView();
//                        highsHeading_category.setText("Brand");
//                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_category.setGravity(Gravity.LEFT);
//
//                        TextView highsHeading_total = initPlainHeaderTextView();
//                        highsHeading_total.setText("TOTAL");
//                        highsHeading_total.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_total.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_HO = initPlainHeaderTextView();
//                        highsHeading_HO.setText("HO");
//                        highsHeading_HO.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_HO.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_NA = initPlainHeaderTextView();
//                        highsHeading_NA.setText("NA");
//                        highsHeading_NA.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_NA.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_SN = initPlainHeaderTextView();
//                        highsHeading_SN.setText("SN");
//                        highsHeading_SN.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_SN.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_GB = initPlainHeaderTextView();
//                        highsHeading_GB.setText("GB");
//                        highsHeading_GB.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_GB.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_VA = initPlainHeaderTextView();
//                        highsHeading_VA.setText("VA");
//                        highsHeading_VA.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_VA.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_SM = initPlainHeaderTextView();
//                        highsHeading_SM.setText("SM");
//                        highsHeading_SM.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_SM.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_MN = initPlainHeaderTextView();
//                        highsHeading_MN.setText("MN");
//                        highsHeading_MN.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_MN.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_PN = initPlainHeaderTextView();
//                        highsHeading_PN.setText("PN");
//                        highsHeading_PN.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_PN.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_DEF = initPlainHeaderTextView();
//                        highsHeading_DEF.setText("DEF");
//                        highsHeading_DEF.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_DEF.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_SD = initPlainHeaderTextView();
//                        highsHeading_SD.setText("SD");
//                        highsHeading_SD.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_SD.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_SIT = initPlainHeaderTextView();
//                        highsHeading_SIT.setText("SIT");
//                        highsHeading_SIT.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_SIT.setGravity(Gravity.CENTER);
//
//                        TextView highsHeading_CL = initPlainHeaderTextView();
//                        highsHeading_CL.setText("CL");
//                        highsHeading_CL.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsHeading_CL.setGravity(Gravity.CENTER);
//
//                        tblrowHeading.addView(highsHeading_category);
//                        tblrowHeading.addView(highsHeading_total);
//                        tblrowHeading.addView(highsHeading_HO);
//                        tblrowHeading.addView(highsHeading_NA);
//                        tblrowHeading.addView(highsHeading_SN);
//                        tblrowHeading.addView(highsHeading_GB);
//                        tblrowHeading.addView(highsHeading_VA);
//                        tblrowHeading.addView(highsHeading_SM);
//                        tblrowHeading.addView(highsHeading_MN);
//                        tblrowHeading.addView(highsHeading_PN);
//                        tblrowHeading.addView(highsHeading_DEF);
//                        tblrowHeading.addView(highsHeading_SD);
//                        tblrowHeading.addView(highsHeading_SIT);
//                        tblrowHeading.addView(highsHeading_CL);
//
//                        tabletoplocationstock.addView(tblrowHeading);
//
//                        int ftotal;
//                        int fHO;
//                        int fNA;
//                        int fSN;
//                        int fGB;
//                        int fVA;
//                        int fSM;
//                        int fMN;
//                        int fPN;
//                        int fDEF;
//                        int fSD;
//                        int fSIT;
//                        int fCL;
//
//                        ftotal = 0;
//                        fHO = 0;
//                        fNA = 0;
//                        fSN = 0;
//                        fGB = 0;
//                        fVA = 0;
//                        fSM = 0;
//                        fMN = 0;
//                        fPN = 0;
//                        fDEF = 0;
//                        fSD = 0;
//                        fSIT = 0;
//                        fCL = 0;
//
//                        JSONObject obj = response;
//                        JSONArray new_array = obj.getJSONArray("top_category_stock");
//
//                        for (int i = 0, count = new_array.length(); i < count; i++) {
//                            try {
//                                JSONObject jsonObject = new_array.getJSONObject(i);
//
//                                TableRow tblrowLabels = new TableRow(StockActivityLocationBrand.this);
//
//                                final TextView highsLabel_category = initPlainTextView(i);
//                                highsLabel_category.setText(jsonObject.getString("brandname"));
//                                highsLabel_category.setTypeface(Typeface.DEFAULT);
//                                highsLabel_category.setGravity(Gravity.LEFT);
//                                highsLabel_category.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Category.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_total = initPlainTextView(i);
//                                highsLabel_total.setText(jsonObject.getString("TOTAL"));
//                                highsLabel_total.setTypeface(Typeface.DEFAULT);
//                                highsLabel_total.setGravity(Gravity.CENTER);
//                                highsLabel_total.setTextColor(Color.parseColor("#ff670f"));
//                                highsLabel_total.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Category.class);
//                                        intent.putExtra("categoryname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","TOTAL");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_HO = initPlainTextView(i);
//                                highsLabel_HO.setText(jsonObject.getString("HO"));
//                                highsLabel_HO.setTypeface(Typeface.DEFAULT);
//                                highsLabel_HO.setGravity(Gravity.CENTER);
//                                highsLabel_HO.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","HO");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_NA = initPlainTextView(i);
//                                highsLabel_NA.setText(jsonObject.getString("NA"));
//                                highsLabel_NA.setTypeface(Typeface.DEFAULT);
//                                highsLabel_NA.setGravity(Gravity.CENTER);
//                                highsLabel_NA.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","NA");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_SN = initPlainTextView(i);
//                                highsLabel_SN.setText(jsonObject.getString("SN"));
//                                highsLabel_SN.setTypeface(Typeface.DEFAULT);
//                                highsLabel_SN.setGravity(Gravity.CENTER);
//                                highsLabel_SN.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","SN");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_GB = initPlainTextView(i);
//                                highsLabel_GB.setText(jsonObject.getString("GB"));
//                                highsLabel_GB.setTypeface(Typeface.DEFAULT);
//                                highsLabel_GB.setGravity(Gravity.CENTER);
//                                highsLabel_GB.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","GB");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_VA = initPlainTextView(i);
//                                highsLabel_VA.setText(jsonObject.getString("VA"));
//                                highsLabel_VA.setTypeface(Typeface.DEFAULT);
//                                highsLabel_VA.setGravity(Gravity.CENTER);
//                                highsLabel_VA.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","VA");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_SM = initPlainTextView(i);
//                                highsLabel_SM.setText(jsonObject.getString("SM"));
//                                highsLabel_SM.setTypeface(Typeface.DEFAULT);
//                                highsLabel_SM.setGravity(Gravity.CENTER);
//                                highsLabel_SM.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","SM");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_MN = initPlainTextView(i);
//                                highsLabel_MN.setText(jsonObject.getString("MN"));
//                                highsLabel_MN.setTypeface(Typeface.DEFAULT);
//                                highsLabel_MN.setGravity(Gravity.CENTER);
//                                highsLabel_MN.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","MN");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_PN = initPlainTextView(i);
//                                highsLabel_PN.setText(jsonObject.getString("PN"));
//                                highsLabel_PN.setTypeface(Typeface.DEFAULT);
//                                highsLabel_PN.setGravity(Gravity.CENTER);
//                                highsLabel_PN.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","PN");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_DEF = initPlainTextView(i);
//                                highsLabel_DEF.setText(jsonObject.getString("DEF"));
//                                highsLabel_DEF.setTypeface(Typeface.DEFAULT);
//                                highsLabel_DEF.setGravity(Gravity.CENTER);
//                                highsLabel_DEF.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","DEF");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_SD = initPlainTextView(i);
//                                highsLabel_SD.setText(jsonObject.getString("SD"));
//                                highsLabel_SD.setTypeface(Typeface.DEFAULT);
//                                highsLabel_SD.setGravity(Gravity.CENTER);
//                                highsLabel_SD.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","SD");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_SIT = initPlainTextView(i);
//                                highsLabel_SIT.setText(jsonObject.getString("SIT"));
//                                highsLabel_SIT.setTypeface(Typeface.DEFAULT);
//                                highsLabel_SIT.setGravity(Gravity.CENTER);
//                                highsLabel_SIT.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","SIT");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                final TextView highsLabel_CL = initPlainTextView(i);
//                                highsLabel_CL.setText(jsonObject.getString("CL"));
//                                highsLabel_CL.setTypeface(Typeface.DEFAULT);
//                                highsLabel_CL.setGravity(Gravity.CENTER);
//                                highsLabel_CL.setOnClickListener(new OnClickListener()
//                                {
//                                    @Override
//                                    public void onClick(View v)
//                                    {
//                                        Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
//                                        intent.putExtra("brandname",highsLabel_category.getText());
//                                        intent.putExtra("locationname","CL");
//                                        startActivity(intent);
//                                    }
//                                });
//
//                                tblrowLabels.addView(highsLabel_category);
//                                tblrowLabels.addView(highsLabel_total);
//                                tblrowLabels.addView(highsLabel_HO);
//                                tblrowLabels.addView(highsLabel_NA);
//                                tblrowLabels.addView(highsLabel_SN);
//                                tblrowLabels.addView(highsLabel_GB);
//                                tblrowLabels.addView(highsLabel_VA);
//                                tblrowLabels.addView(highsLabel_SM);
//                                tblrowLabels.addView(highsLabel_MN);
//                                tblrowLabels.addView(highsLabel_PN);
//                                tblrowLabels.addView(highsLabel_DEF);
//                                tblrowLabels.addView(highsLabel_SD);
//                                tblrowLabels.addView(highsLabel_SIT);
//                                tblrowLabels.addView(highsLabel_CL);
//
//                                tblrowLabels.setClickable(true);
//
//                                tabletoplocationstock.addView(tblrowLabels);
//
//                                ftotal += Integer.valueOf(0+jsonObject.getString("TOTAL"));
//                                fHO += Integer.valueOf(0+jsonObject.getString("HO"));
//                                fNA += Integer.valueOf(0+jsonObject.getString("NA"));
//                                fSN += Integer.valueOf(0+jsonObject.getString("SN"));
//                                fGB += Integer.valueOf(0+jsonObject.getString("GB"));
//                                fVA += Integer.valueOf(0+jsonObject.getString("VA"));
//                                fSM += Integer.valueOf(0+jsonObject.getString("SM"));
//                                fMN += Integer.valueOf(0+jsonObject.getString("MN"));
//                                fPN += Integer.valueOf(0+jsonObject.getString("PN"));
//                                fDEF += Integer.valueOf(0+jsonObject.getString("DEF"));
//                                fSD += Integer.valueOf(0+jsonObject.getString("SD"));
//                                fSIT += Integer.valueOf(0+jsonObject.getString("SIT"));
//                                fCL += Integer.valueOf(0+jsonObject.getString("CL"));
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        TableRow tblrowFooter = new TableRow(StockActivityLocationBrand.this);
//                        TextView highsFooter_category = initPlainFooterTextView();
//                        highsFooter_category.setText("Total");
//                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_category.setGravity(Gravity.LEFT);
//
//                        TextView highsFooter_total = initPlainFooterTextView();
//                        highsFooter_total.setText(String.valueOf(ftotal));
//                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_total.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_HO = initPlainFooterTextView();
//                        highsFooter_HO.setText(String.valueOf(fHO));
//                        highsFooter_HO.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_HO.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_NA = initPlainFooterTextView();
//                        highsFooter_NA.setText(String.valueOf(fNA));
//                        highsFooter_NA.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_NA.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_SN = initPlainFooterTextView();
//                        highsFooter_SN.setText(String.valueOf(fSN));
//                        highsFooter_SN.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_SN.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_GB = initPlainFooterTextView();
//                        highsFooter_GB.setText(String.valueOf(fGB));
//                        highsFooter_GB.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_GB.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_VA = initPlainFooterTextView();
//                        highsFooter_VA.setText(String.valueOf(fVA));
//                        highsFooter_VA.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_VA.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_SM = initPlainFooterTextView();
//                        highsFooter_SM.setText(String.valueOf(fSM));
//                        highsFooter_SM.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_SM.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_MN = initPlainFooterTextView();
//                        highsFooter_MN.setText(String.valueOf(fMN));
//                        highsFooter_MN.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_MN.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_PN = initPlainFooterTextView();
//                        highsFooter_PN.setText(String.valueOf(fPN));
//                        highsFooter_PN.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_PN.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_DEF = initPlainFooterTextView();
//                        highsFooter_DEF.setText(String.valueOf(fDEF));
//                        highsFooter_DEF.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_DEF.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_SD = initPlainFooterTextView();
//                        highsFooter_SD.setText(String.valueOf(fSD));
//                        highsFooter_SD.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_SD.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_SIT = initPlainFooterTextView();
//                        highsFooter_SIT.setText(String.valueOf(fSIT));
//                        highsFooter_SIT.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_SIT.setGravity(Gravity.CENTER);
//
//                        TextView highsFooter_CL = initPlainFooterTextView();
//                        highsFooter_CL.setText(String.valueOf(fCL));
//                        highsFooter_CL.setTypeface(Typeface.DEFAULT_BOLD);
//                        highsFooter_CL.setGravity(Gravity.CENTER);
//
//                        tblrowFooter.addView(highsFooter_category);
//                        tblrowFooter.addView(highsFooter_total);
//                        tblrowFooter.addView(highsFooter_HO);
//                        tblrowFooter.addView(highsFooter_NA);
//                        tblrowFooter.addView(highsFooter_SN);
//                        tblrowFooter.addView(highsFooter_GB);
//                        tblrowFooter.addView(highsFooter_VA);
//                        tblrowFooter.addView(highsFooter_SM);
//                        tblrowFooter.addView(highsFooter_MN);
//                        tblrowFooter.addView(highsFooter_PN);
//                        tblrowFooter.addView(highsFooter_DEF);
//                        tblrowFooter.addView(highsFooter_SD);
//                        tblrowFooter.addView(highsFooter_SIT);
//                        tblrowFooter.addView(highsFooter_CL);
//
//                        tabletoplocationstock.addView(tblrowFooter);
//
//                        //    LinearLayout sv = new LinearLayout(MainActivity.this);
//
//                        //        sv.addView(table);
//
//                        //hsw.addView(sv);
//                        //setContentView(hsw);
//
//                        // setContentView(table);
//
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//
//                        Toast.makeText(getApplicationContext(), "Status code :"+ e.toString() +"errmsg : "+e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                // When the response returned by REST has Http response code other than '200'
//                @Override
//                public void onFailure(String errorMessage) {
//                    // Hide Progress Dialog
//                    //     prgDialog.hide();
//                    // When Http response code is '404'
//                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
//
//                }
//            });
//        }catch (Exception e) {
//            e.printStackTrace();
//            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//
//
//    private TextView initPlainTextView(float n) {
//
//        TextView textView = new TextView(StockActivityLocationBrand.this);
//        textView.setPadding(10, 10, 10, 10);
//
//
//        if((n%2)==0)
//        {
//            textView.setBackgroundResource(R.drawable.cell_shape);
//        }
//        else
//        {
//            textView.setBackgroundResource(R.drawable.cell_shape_oddrow);
//        }
//
//
//        textView.setTextColor(Color.parseColor("#000000"));
//        return textView;
//    }
//
//    private TextView initPlainHeaderTextView() {
//        TextView textView = new TextView(StockActivityLocationBrand.this);
//        textView.setPadding(10, 10, 10, 10);
//        textView.setBackgroundResource(R.drawable.cell_shape_header);
//        textView.setTextColor(Color.parseColor("#ffffff"));
//        return textView;
//    }
//
//    private TextView initPlainFooterTextView() {
//        TextView textView = new TextView(StockActivityLocationBrand.this);
//        textView.setPadding(10, 10, 10, 10);
//        textView.setBackgroundResource(R.drawable.cell_shape_footer);
//        textView.setTextColor(Color.parseColor("#ffffff"));
//        return textView;
//    }
//}

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
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.HashMap;
//import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StockActivityLocationBrand extends BaseActivity {

    private static String URL = Config.WEBSERVICE_URL;

    TableLayout tableHeader, tableData;
    TextView header_toplocationstock;
    String categoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_stock);

        tableHeader = findViewById(R.id.table_header);
        tableData = findViewById(R.id.table_data);

        header_toplocationstock = findViewById(R.id.header_toplocationstock);

        Intent i = getIntent();
        categoryname = i.getStringExtra("categoryname");

        header_toplocationstock.setText(categoryname + " STOCK");

        invokeWS_TopCategoryLocationStock(categoryname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_locationstockactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnExportToPdf) {
            ScrollView activityLayout = findViewById(R.id.activity_scrollview);
            Utility.exportScrollViewContentToPdf(this, activityLayout);
        } else if (id == R.id.btnExportToExcel) {
            try {
                TableLayout activityLayout = findViewById(R.id.tabletoplocationstock);
                Utility.ExportTableLayoutContentToExcel(this, activityLayout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.action_brandwise) {
            navigatetoStockActvitySummery_Brand();
        } else if (id == R.id.action_categorywise) {
            navigatetoStockActvitySummery_Category();
        }
        return super.onOptionsItemSelected(item);
    }

    private void invokeWS_TopCategoryLocationStock(String categoryname) {
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("companycd", "0");
            paramsMap.put("categoryname", categoryname);

            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(30000);

            ApiHelper.post(URL + "Service1.asmx/Stock_LocationStock_Brand", paramsMap, new ApiHelper.ApiCallback() {
                @Override
                public void onSuccess(JSONObject obj) {
                    try {
                        //JSONObject obj = new JSONObject(response);
                        JSONArray dataArray = obj.getJSONArray("top_category_stock");

                        if (dataArray.length() == 0) return;

                        JSONObject firstItem = dataArray.getJSONObject(0);
                        Iterator<?> keys = firstItem.keys();

                        Set<String> hiddenColumns = new HashSet<>();
                        hiddenColumns.add("CL");

                        List<String> columnKeys = new ArrayList<>();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            if (!key.equalsIgnoreCase("brandname")) {
                                columnKeys.add(key);
                            }
                        }

                        boolean hasTotal = columnKeys.remove("TOTAL"); // remove if present
                        Collections.sort(columnKeys); // sort all other keys
                        if (hasTotal) columnKeys.add("TOTAL"); // add "Total" back at end

                        Map<String, Integer> columnTotals = new HashMap<>();
                        for (String key : columnKeys) columnTotals.put(key, 0);

                        tableHeader.removeAllViews();
                        tableData.removeAllViews();

                        TableRow headerRow = new TableRow(StockActivityLocationBrand.this);
                        headerRow.addView(createHeaderTextView("Brand", 2.0f));
                        for (String col : columnKeys) {
                            if (hiddenColumns.contains(col)) continue; // skip hidden columns
                            headerRow.addView(createHeaderTextView(col, 1.0f));
                        }
                        tableHeader.addView(headerRow);

                        for (int i = 0; i < dataArray.length(); i++) {


                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            TableRow row = new TableRow(StockActivityLocationBrand.this);

                            final String brandName = jsonObject.getString("brandname");
                            TextView brandCell = createDataTextView(brandName, i, 2.0f);
                            brandCell.setOnClickListener(v -> {
                                Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Category.class);
                                intent.putExtra("brandname", brandName);
                                startActivity(intent);
                            });
                            row.addView(brandCell);

                            for (String key : columnKeys) {
                                if (hiddenColumns.contains(key)) continue; // skip hidden columns
                                String value = jsonObject.optString(key, "0");
                                TextView cell = createDataTextView(value, i, 1.0f);
                                final String locationKey = key;
                                cell.setOnClickListener(v -> {
                                    Intent intent = new Intent(StockActivityLocationBrand.this, StockActivityLocationBrand_Model.class);
                                    intent.putExtra("brandname", brandName);
                                    intent.putExtra("locationname", locationKey);
                                    startActivity(intent);
                                });

                                try {
                                    int val = Integer.parseInt(value.trim().isEmpty() ? "0" : value.trim());
                                    columnTotals.put(locationKey, columnTotals.get(locationKey) + val);
                                } catch (Exception ignored) {
                                }

                                row.addView(cell);
                            }
                            tableData.addView(row);
                        }

                        TableRow footerRow = new TableRow(StockActivityLocationBrand.this);
                        footerRow.addView(createFooterTextView("Total", 2.0f));
                        for (String key : columnKeys) {
                            if (hiddenColumns.contains(key)) continue; // skip hidden columns
                            footerRow.addView(createFooterTextView(String.valueOf(columnTotals.get(key)),1.0f));
                        }
                        tableData.addView(footerRow);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Parsing Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView createHeaderTextView(String text, float weight) {
        TextView tv = new TextView(this);
        tv.setPadding(16, 16, 16, 16);
        tv.setTextSize(14);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setBackgroundResource(R.drawable.cell_shape_header);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        tv.setLayoutParams(params);
        return tv;
    }


    private TextView createDataTextView(String text, int rowIndex, float weight) {
        TextView tv = new TextView(this);
        tv.setPadding(16, 16, 16, 16);
        tv.setTextSize(13);
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setBackgroundResource((rowIndex % 2 == 0) ? R.drawable.cell_shape : R.drawable.cell_shape_oddrow);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        tv.setLayoutParams(params);
        return tv;
    }


    private TextView createFooterTextView(String text, float weight) {
        TextView tv = new TextView(this);
        tv.setPadding(16, 16, 16, 16);
        tv.setTextSize(14);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setBackgroundResource(R.drawable.cell_shape_footer);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        tv.setLayoutParams(params);
        return tv;
    }


    private void navigatetoStockActvitySummery_Category() {
        Intent intent = new Intent(this, StockActivityLocationCategory.class);
        intent.putExtra("brandname", "");
        startActivity(intent);
    }

    private void navigatetoStockActvitySummery_Brand() {
        Intent intent = new Intent(this, StockActivityLocationBrand.class);
        intent.putExtra("categoryname", "");
        startActivity(intent);
    }
}