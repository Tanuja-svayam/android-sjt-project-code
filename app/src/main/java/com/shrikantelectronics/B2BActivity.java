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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class B2BActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String reporttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b2b);

        invokeWS_MontlySales();


    }

    public void invokeWS_MontlySales() {

        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            String sysuserno = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("companycd", "0");
            paramsMap.put("sysuserno", "0"+ sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText("SALES BOOK");

            ApiHelper.post(URL + "Service1.asmx/Sales_MonthSummary_B2B", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(B2BActivity.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Month");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Quantity");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Value");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        TextView highsHeading_Quantityreturn = initPlainHeaderTextView();
                        highsHeading_Quantityreturn.setText("Ret. Qty");
                        highsHeading_Quantityreturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantityreturn.setGravity(Gravity.RIGHT);

                        TextView highsheading_Valuereturn = initPlainHeaderTextView();
                        highsheading_Valuereturn.setText("Ret. Value");
                        highsheading_Valuereturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Valuereturn.setGravity(Gravity.RIGHT);


                        TextView highsHeading_Quantity_net = initPlainHeaderTextView();
                        highsHeading_Quantity_net.setText("Net Qty");
                        highsHeading_Quantity_net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity_net.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value_net = initPlainHeaderTextView();
                        highsheading_Value_net.setText("Net Value");
                        highsheading_Value_net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value_net.setGravity(Gravity.RIGHT);


                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);

                        tblrowHeading.addView(highsHeading_Quantityreturn);
                        tblrowHeading.addView(highsheading_Valuereturn);

                        tblrowHeading.addView(highsHeading_Quantity_net);
                        tblrowHeading.addView(highsheading_Value_net);

                        tablesalesregister.addView(tblrowHeading);

                        double fQuantity;
                        double fValue;

                        double fQuantityreturn;
                        double fValuereturn;

                        double fQuantity_net;
                        double fValue_net;

                        fQuantity = 0;
                        fValue = 0;

                        fQuantityreturn = 0;
                        fValuereturn = 0;

                        fQuantity_net = 0;
                        fValue_net = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_register");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(B2BActivity.this);

                               final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("monthname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);
                                highsLabel_category.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(B2BActivity.this, SalesActivity_Daily.class);
                                        intent.putExtra("locationname","");
                                        intent.putExtra("monthname",highsLabel_category.getText());
                                        startActivity(intent);
                                    }
                                });

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("value"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                TextView highsLabel_QuantityReturn = initPlainTextView(i);
                                highsLabel_QuantityReturn.setText(jsonObject.getString("quantity_return"));
                                highsLabel_QuantityReturn.setTypeface(Typeface.DEFAULT);
                                highsLabel_QuantityReturn.setGravity(Gravity.RIGHT);

                                TextView highslabel_ValueReturn = initPlainTextView(i);
                                highslabel_ValueReturn.setText(jsonObject.getString("value_return"));
                                highslabel_ValueReturn.setTypeface(Typeface.DEFAULT);
                                highslabel_ValueReturn.setGravity(Gravity.RIGHT);


                                TextView highsLabel_Net_Quantity= initPlainTextView(i);
                                highsLabel_Net_Quantity.setText(jsonObject.getString("net_quantity"));
                                highsLabel_Net_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Net_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_Net_Value= initPlainTextView(i);
                                highslabel_Net_Value.setText(jsonObject.getString("net_value"));
                                highslabel_Net_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Net_Value.setGravity(Gravity.RIGHT);


                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_Value);
                                tblrowLabels.addView(highsLabel_QuantityReturn);
                                tblrowLabels.addView(highslabel_ValueReturn);

                                tblrowLabels.addView(highsLabel_Net_Quantity);
                                tblrowLabels.addView(highslabel_Net_Value);

                                tablesalesregister.addView(tblrowLabels);

                                fQuantity += Double.valueOf(0+jsonObject.getString("quantity"));
                                fValue += Double.valueOf(0+jsonObject.getString("value"));

                                fQuantityreturn += Double.valueOf(0+jsonObject.getString("quantity_return"));
                                fValuereturn += Double.valueOf(0+jsonObject.getString("value_return"));

                                fQuantity_net += Double.valueOf(0+jsonObject.getString("net_quantity"));
                                fValue_net += Double.valueOf(0+jsonObject.getString("net_value"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(B2BActivity.this);
                        TextView highsFooter_total = initPlainFooterTextView();
                        highsFooter_total.setText("TOTAL");
                        highsFooter_total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_total.setGravity(Gravity.CENTER);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(Utility.DoubleToString(fQuantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(Utility.DoubleToString(fValue));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        TextView highsFooter_quantityReturn = initPlainFooterTextView();
                        highsFooter_quantityReturn.setText(Utility.DoubleToString(fQuantityreturn));
                        highsFooter_quantityReturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_quantityReturn.setGravity(Gravity.RIGHT);

                        TextView highsFooter_valueReturn = initPlainFooterTextView();
                        highsFooter_valueReturn.setText(Utility.DoubleToString(fValuereturn));
                        highsFooter_valueReturn.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_valueReturn.setGravity(Gravity.RIGHT);


                        TextView highsFooter_quantity_Net = initPlainFooterTextView();
                        highsFooter_quantity_Net.setText(Utility.DoubleToString(fQuantity_net));
                        highsFooter_quantity_Net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_quantity_Net.setGravity(Gravity.RIGHT);

                        TextView highsFooter_value_Net = initPlainFooterTextView();
                        highsFooter_value_Net.setText(Utility.DoubleToString(fValue_net));
                        highsFooter_value_Net.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_value_Net.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_total);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra);
                        tblrowFooter.addView(highsFooter_quantityReturn);
                        tblrowFooter.addView(highsFooter_valueReturn);
                        tblrowFooter.addView(highsFooter_quantity_Net);
                        tblrowFooter.addView(highsFooter_value_Net);

                        tablesalesregister.addView(tblrowFooter);

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
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    private TextView initPlainTextView(float n) {

	        TextView textView = new TextView(B2BActivity.this);
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
	        TextView textView = new TextView(B2BActivity.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(B2BActivity.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
