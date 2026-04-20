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

public class ProfitabilityActivityLocationBrand extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tabletoplocationprofitability;

    TextView header_toplocationprofitability;
    String categoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_profitability);

        Intent i = getIntent();
        categoryname = i.getStringExtra("categoryname");

        invokeWS_TopCategoryLocationProfitability(categoryname);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_locationprofitabilityactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_brandwise) {
            navigatetoProfitabilityActvitySummery_Brand();
        } else if (id == R.id.action_categorywise) {
            navigatetoProfitabilityActvitySummery_Category();
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(ProfitabilityActivityLocationBrand.this,model_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoProfitabilityActvitySummery_Category(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand.this,ProfitabilityActivityLocationCategory.class);
        customerIntent.putExtra("brandname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoProfitabilityActvitySummery_Brand(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand.class);
        customerIntent.putExtra("categoryname","");
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }


    public void navigatetoPlanogram(){
        Intent customerIntent = new Intent(ProfitabilityActivityLocationBrand.this,company_view.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void invokeWS_TopCategoryLocationProfitability(String categoryname) {
        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("companycd", "0");
            paramsMap.put("categoryname", categoryname);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_toplocationprofitability= (TextView) findViewById(R.id.header_toplocationprofitability);
            header_toplocationprofitability.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/Profitability_LocationProfitability_Brand", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationprofitability  = (TableLayout) findViewById(R.id.tabletoplocationprofitability);

                        tabletoplocationprofitability.setStretchAllColumns(true);
                        tabletoplocationprofitability.setShrinkAllColumns(true);

                      TableRow tblrowHeading = new TableRow(ProfitabilityActivityLocationBrand.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Brand");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_total = initPlainHeaderTextView();
                        highsHeading_total.setText("TOTAL");
                        highsHeading_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_total.setGravity(Gravity.CENTER);

                        TextView highsHeading_MF = initPlainHeaderTextView();
                        highsHeading_MF.setText("MF");
                        highsHeading_MF.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_MF.setGravity(Gravity.CENTER);

                        TextView highsHeading_RC = initPlainHeaderTextView();
                        highsHeading_RC.setText("RC");
                        highsHeading_RC.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_RC.setGravity(Gravity.CENTER);

                        TextView highsHeading_GM = initPlainHeaderTextView();
                        highsHeading_GM.setText("GM");
                        highsHeading_GM.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_GM.setGravity(Gravity.CENTER);

                        TextView highsHeading_GR = initPlainHeaderTextView();
                        highsHeading_GR.setText("GB");
                        highsHeading_GR.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_GR.setGravity(Gravity.CENTER);

                        TextView highsHeading_G1 = initPlainHeaderTextView();
                        highsHeading_G1.setText("MN");
                        highsHeading_G1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_G1.setGravity(Gravity.CENTER);

                        TextView highsHeading_G2 = initPlainHeaderTextView();
                        highsHeading_G2.setText("PN");
                        highsHeading_G2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_G2.setGravity(Gravity.CENTER);

                        TextView highsHeading_LX = initPlainHeaderTextView();
                        highsHeading_LX.setText("LX");
                        highsHeading_LX.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_LX.setGravity(Gravity.CENTER);

                        TextView highsHeading_DEF = initPlainHeaderTextView();
                        highsHeading_DEF.setText("DEF");
                        highsHeading_DEF.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_DEF.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_total);
                        tblrowHeading.addView(highsHeading_MF);
                        tblrowHeading.addView(highsHeading_RC);
                        tblrowHeading.addView(highsHeading_GM);
                        tblrowHeading.addView(highsHeading_GR);
                        tblrowHeading.addView(highsHeading_G1);
                        tblrowHeading.addView(highsHeading_G2);
                        tblrowHeading.addView(highsHeading_LX);
                        tblrowHeading.addView(highsHeading_DEF);

                        tabletoplocationprofitability.addView(tblrowHeading);

                        int ftotal;
                        int fmf;
                        int frc;
                        int fgm;
                        int fgr;
                        int fg1;
                        int fg2;
                        int flx;
                        int fdef;

                        ftotal = 0;
                        fmf = 0;
                        frc = 0;
                        fgm = 0;
                        fgr = 0;
                        fg1 = 0;
                        fg2 = 0;
                        flx = 0;
                        fdef = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("top_category_profitability");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(ProfitabilityActivityLocationBrand.this);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("brandname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Category.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_total = initPlainTextView(i);
                                highsLabel_total.setText(jsonObject.getString("TOTAL"));
                                highsLabel_total.setTypeface(Typeface.DEFAULT);
                                highsLabel_total.setGravity(Gravity.CENTER);
                                highsLabel_total.setTextColor(Color.parseColor("#ff670f"));
                                highsLabel_total.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Category.class);
                                        intent.putExtra("categoryname",highsLabel_category.getText());
                                        intent.putExtra("locationname","TOTAL");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_MF = initPlainTextView(i);
                                highsLabel_MF.setText(jsonObject.getString("MF"));
                                highsLabel_MF.setTypeface(Typeface.DEFAULT);
                                highsLabel_MF.setGravity(Gravity.CENTER);
                                highsLabel_MF.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","MF");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_RC = initPlainTextView(i);
                                highsLabel_RC.setText(jsonObject.getString("RC"));
                                highsLabel_RC.setTypeface(Typeface.DEFAULT);
                                highsLabel_RC.setGravity(Gravity.CENTER);
                                highsLabel_RC.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","RC");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_GM = initPlainTextView(i);
                                highsLabel_GM.setText(jsonObject.getString("GM"));
                                highsLabel_GM.setTypeface(Typeface.DEFAULT);
                                highsLabel_GM.setGravity(Gravity.CENTER);
                                highsLabel_GM.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","GM");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_GR = initPlainTextView(i);
                                highsLabel_GR.setText(jsonObject.getString("GB"));
                                highsLabel_GR.setTypeface(Typeface.DEFAULT);
                                highsLabel_GR.setGravity(Gravity.CENTER);
                                highsLabel_GR.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","GB");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_G1 = initPlainTextView(i);
                                highsLabel_G1.setText(jsonObject.getString("MN"));
                                highsLabel_G1.setTypeface(Typeface.DEFAULT);
                                highsLabel_G1.setGravity(Gravity.CENTER);
                                highsLabel_G1.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","MN");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_G2 = initPlainTextView(i);
                                highsLabel_G2.setText(jsonObject.getString("PN"));
                                highsLabel_G2.setTypeface(Typeface.DEFAULT);
                                highsLabel_G2.setGravity(Gravity.CENTER);
                                highsLabel_G2.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","PN");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_LX = initPlainTextView(i);
                                highsLabel_LX.setText(jsonObject.getString("LX"));
                                highsLabel_LX.setTypeface(Typeface.DEFAULT);
                                highsLabel_LX.setGravity(Gravity.CENTER);
                                highsLabel_LX.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","LX");
                                        startActivity(intent);
                                    }
                                });

                                final TextView highsLabel_DEF = initPlainTextView(i);
                                highsLabel_DEF.setText(jsonObject.getString("DEF"));
                                highsLabel_DEF.setTypeface(Typeface.DEFAULT);
                                highsLabel_DEF.setGravity(Gravity.CENTER);
                                highsLabel_DEF.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(ProfitabilityActivityLocationBrand.this, ProfitabilityActivityLocationBrand_Model.class);
                                        intent.putExtra("brandname",highsLabel_category.getText());
                                        intent.putExtra("locationname","DEF");
                                        startActivity(intent);
                                    }
                                });

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_total);
                                tblrowLabels.addView(highsLabel_MF);
                                tblrowLabels.addView(highsLabel_RC);
                                tblrowLabels.addView(highsLabel_GM);
                                tblrowLabels.addView(highsLabel_GR);
                                tblrowLabels.addView(highsLabel_G1);
                                tblrowLabels.addView(highsLabel_G2);
                                tblrowLabels.addView(highsLabel_LX);
                                tblrowLabels.addView(highsLabel_DEF);

                                tblrowLabels.setClickable(true);

                                tabletoplocationprofitability.addView(tblrowLabels);

                                ftotal += Integer.valueOf(0+jsonObject.getString("TOTAL"));
                                fmf += Integer.valueOf(0+jsonObject.getString("MF"));
                                frc += Integer.valueOf(0+jsonObject.getString("RC"));
                                fgm += Integer.valueOf(0+jsonObject.getString("GM"));
                                fgr += Integer.valueOf(0+jsonObject.getString("GB"));
                                fg1 += Integer.valueOf(0+jsonObject.getString("MN"));
                                fg2 += Integer.valueOf(0+jsonObject.getString("PN"));
                                flx += Integer.valueOf(0+jsonObject.getString("LX"));
                                fdef += Integer.valueOf(0+jsonObject.getString("DEF"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(ProfitabilityActivityLocationBrand.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText(String.valueOf(ftotal));
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_MF = initPlainFooterTextView();
                        highsFooter_MF.setText(String.valueOf(fmf));
                        highsFooter_MF.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_MF.setGravity(Gravity.CENTER);

                        TextView highsFooter_RC = initPlainFooterTextView();
                        highsFooter_RC.setText(String.valueOf(frc));
                        highsFooter_RC.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_RC.setGravity(Gravity.CENTER);

                        TextView highsFooter_GM = initPlainFooterTextView();
                        highsFooter_GM.setText(String.valueOf(fgm));
                        highsFooter_GM.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_GM.setGravity(Gravity.CENTER);

                        TextView highsFooter_GR = initPlainFooterTextView();
                        highsFooter_GR.setText(String.valueOf(fgr));
                        highsFooter_GR.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_GR.setGravity(Gravity.CENTER);

                        TextView highsFooter_G1 = initPlainFooterTextView();
                        highsFooter_G1.setText(String.valueOf(fg1));
                        highsFooter_G1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_G1.setGravity(Gravity.CENTER);

                        TextView highsFooter_G2 = initPlainFooterTextView();
                        highsFooter_G2.setText(String.valueOf(fg2));
                        highsFooter_G2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_G2.setGravity(Gravity.CENTER);

                        TextView highsFooter_LX = initPlainFooterTextView();
                        highsFooter_LX.setText(String.valueOf(flx));
                        highsFooter_LX.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_LX.setGravity(Gravity.CENTER);

                        TextView highsFooter_DEF = initPlainFooterTextView();
                        highsFooter_DEF.setText(String.valueOf(fdef));
                        highsFooter_DEF.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_DEF.setGravity(Gravity.CENTER);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_MF);
                        tblrowFooter.addView(highsFooter_RC);
                        tblrowFooter.addView(highsFooter_GM);
                        tblrowFooter.addView(highsFooter_GR);
                        tblrowFooter.addView(highsFooter_G1);
                        tblrowFooter.addView(highsFooter_G2);
                        tblrowFooter.addView(highsFooter_LX);
                        tblrowFooter.addView(highsFooter_DEF);

                        tabletoplocationprofitability.addView(tblrowFooter);

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

        TextView textView = new TextView(ProfitabilityActivityLocationBrand.this);
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
        TextView textView = new TextView(ProfitabilityActivityLocationBrand.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(ProfitabilityActivityLocationBrand.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}
