package com.shrikantelectronics;

import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockActivityAgeingLocationCategory extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationstock;

    TextView header_toplocationstock;
    String categoryname;
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_ageing_stock);

       // Intent i = getIntent();
       // categoryname = i.getStringExtra("categoryname");
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        invokeWS_TopCategoryLocationStock("Q");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_locationstockactivity, menu);
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
        } else if (id == R.id.action_modelwise) {
            navigatetoStockActvitySummery_Model();
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigatetoStockActvitySummery_Brand(){
        Intent customerIntent = new Intent(StockActivityAgeingLocationCategory.this,StockActivityAgeingLocationCategory.class);
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoStockActvitySummery_Category(){
        Intent customerIntent = new Intent(StockActivityAgeingLocationCategory.this,StockActivityAgeingLocationCategory.class);
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoStockActvitySummery_Model(){
        Intent customerIntent = new Intent(StockActivityAgeingLocationCategory.this,StockActivityAgeingLocationModel.class);
        customerIntent.putExtra("brandname","");
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void invokeWS_TopCategoryLocationStock(String type) {
        try {
            prgDialog.show();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysemployeeno  = globalVariable.getsysemployeeno();

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("companycd", "0");
            paramsMap.put("type", type);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

          //  header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
         //   header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetBranchStockDetails_Ageing_Category", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {
                        prgDialog.hide();

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                        tabletoplocationstock.removeAllViews();

                    //    tabletoplocationstock.setStretchAllColumns(true);
                     //   tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(StockActivityAgeingLocationCategory.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Brand");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_total = initPlainHeaderTextView();
                        highsHeading_total.setText("TOTAL");
                        highsHeading_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_total.setGravity(Gravity.CENTER);

                        TextView highsHeading_va = initPlainHeaderTextView();
                        highsHeading_va.setText("30 Days");
                        highsHeading_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_va.setGravity(Gravity.CENTER);

                        TextView highsheading_ra = initPlainHeaderTextView();
                        highsheading_ra.setText("31-60");
                        highsheading_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_ra.setGravity(Gravity.CENTER);

                        TextView highsHeading_OM = initPlainHeaderTextView();
                        highsHeading_OM.setText("61-90");
                        highsHeading_OM.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_OM.setGravity(Gravity.CENTER);

                        TextView highsheading_pc = initPlainHeaderTextView();
                        highsheading_pc.setText("91-180");
                        highsheading_pc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_pc.setGravity(Gravity.CENTER);

                        TextView highsheading_mn = initPlainHeaderTextView();
                        highsheading_mn.setText("180 Above");
                        highsheading_mn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_mn.setGravity(Gravity.CENTER);


                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_total);
                        tblrowHeading.addView(highsHeading_va);
                        tblrowHeading.addView(highsheading_ra);
                        tblrowHeading.addView(highsHeading_OM);
                        tblrowHeading.addView(highsheading_pc);
                        tblrowHeading.addView(highsheading_mn);

                        tabletoplocationstock.addView(tblrowHeading);

                        int ftotal;
                        int fka;
                        int fsu;
                        int fba;
                        int fun;
                        int fmn;
                        int fsg;
                        int fbg;

                        ftotal = 0;
                        fka = 0;
                        fsu = 0;
                        fba = 0;
                        fun = 0;
                        fmn = 0;
                        fsg = 0;
                        fbg = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("stock");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(StockActivityAgeingLocationCategory.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("stocklocation"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(StockActivityAgeingLocationCategory.this, StockActivityAgeingLocationModel.class);
                                        intent.putExtra("brandname","");
                                        intent.putExtra("categoryname",highsLabel_category.getText());
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_total = initPlainTextView(i);
                                highsLabel_total.setText(jsonObject.getString("totalqty"));
                                highsLabel_total.setTypeface(Typeface.DEFAULT);
                                highsLabel_total.setGravity(Gravity.CENTER);
                                highsLabel_total.setTextColor(Color.parseColor("#ff670f"));

                                final TextView highsLabel_va = initPlainTextView(i);
                                highsLabel_va.setText(jsonObject.getString("days_30"));
                                highsLabel_va.setTypeface(Typeface.DEFAULT);
                                highsLabel_va.setGravity(Gravity.CENTER);

                                final TextView highslabel_ra = initPlainTextView(i);
                                highslabel_ra.setText(jsonObject.getString("days_31_60"));
                                highslabel_ra.setTypeface(Typeface.DEFAULT);
                                highslabel_ra.setGravity(Gravity.CENTER);

                                final TextView highsLabel_OM = initPlainTextView(i);
                                highsLabel_OM.setText(jsonObject.getString("days_61_90"));
                                highsLabel_OM.setTypeface(Typeface.DEFAULT);
                                highsLabel_OM.setGravity(Gravity.CENTER);

                                final TextView highslabel_pc = initPlainTextView(i);
                                highslabel_pc.setText(jsonObject.getString("days_91_180"));
                                highslabel_pc.setTypeface(Typeface.DEFAULT);
                                highslabel_pc.setGravity(Gravity.CENTER);

                                final TextView highslabel_mn = initPlainTextView(i);
                                highslabel_mn.setText(jsonObject.getString("days_180_above"));
                                highslabel_mn.setTypeface(Typeface.DEFAULT);
                                highslabel_mn.setGravity(Gravity.CENTER);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_total);
                                tblrowLabels.addView(highsLabel_va);
                                tblrowLabels.addView(highslabel_ra);
                                tblrowLabels.addView(highsLabel_OM);
                                tblrowLabels.addView(highslabel_pc);
                                tblrowLabels.addView(highslabel_mn);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);

                                ftotal += Integer.valueOf(0+jsonObject.getString("totalqty"));
                                fka += Integer.valueOf(0+jsonObject.getString("days_30"));
                                fsu += Integer.valueOf(0+jsonObject.getString("days_31_60"));
                                fba += Integer.valueOf(0+jsonObject.getString("days_61_90"));
                                fun += Integer.valueOf(0+jsonObject.getString("days_91_180"));
                                fmn += Integer.valueOf(0+jsonObject.getString("days_180_above"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(StockActivityAgeingLocationCategory.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText(String.valueOf(ftotal));
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fka));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.CENTER);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(fsu));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.CENTER);

                        TextView highsFooter_OM = initPlainFooterTextView();
                        highsFooter_OM.setText(String.valueOf(fba));
                        highsFooter_OM.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_OM.setGravity(Gravity.CENTER);

                        TextView highsFooter_pc = initPlainFooterTextView();
                        highsFooter_pc.setText(String.valueOf(fun));
                        highsFooter_pc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_pc.setGravity(Gravity.CENTER);

                        TextView highsFooter_mn = initPlainFooterTextView();
                        highsFooter_mn.setText(String.valueOf(fmn));
                        highsFooter_mn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_mn.setGravity(Gravity.CENTER);


                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);
                        tblrowFooter.addView(highsFooter_OM);
                        tblrowFooter.addView(highsFooter_pc);
                        tblrowFooter.addView(highsFooter_mn);

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

        TextView textView = new TextView(StockActivityAgeingLocationCategory.this);
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
        TextView textView = new TextView(StockActivityAgeingLocationCategory.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(StockActivityAgeingLocationCategory.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_quantity:
                if (checked)
                    invokeWS_TopCategoryLocationStock("Q");
                break;
            case R.id.radio_value:
                if (checked)
                    invokeWS_TopCategoryLocationStock("V");
                break;
        }
    }
}
