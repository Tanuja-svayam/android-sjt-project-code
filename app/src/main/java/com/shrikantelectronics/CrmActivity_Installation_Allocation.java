package com.shrikantelectronics;

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

public class CrmActivity_Installation_Allocation extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String activitytypedesc;
    String locationname;
    String activitytype;
    String followupstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crminstallationallocation);

        invokeWS_Followuplist(locationname, activitytypedesc);
    }

    public void invokeWS_Followuplist(String locationname, String activitytypedesc) {

        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String companycd  = globalVariable.getcompanycd();

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("activitytype", "03");
            paramsMap.put("locationname", "");
            paramsMap.put("followupstatus", "'00'");
            paramsMap.put("companycd", companycd);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);
//public void CRM_Activity_Followup_Inst_Allocation_Pending(String activitytype, String locationname, String followupstatus)
            //header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            //header_salesregister.setText(locationname + " " + activitytypedesc);

            ApiHelper.post(URL + "Service1.asmx/CRM_Activity_Followup_Inst_Allocation_Pending", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(CrmActivity_Installation_Allocation.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_custname = initPlainHeaderTextView();
                        highsHeading_custname.setText("Customer");
                        highsHeading_custname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_custname.setGravity(Gravity.LEFT);

                        TextView highsHeading_contactpersonmobile = initPlainHeaderTextView();
                        highsHeading_contactpersonmobile.setText("Mobile");
                        highsHeading_contactpersonmobile.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_contactpersonmobile.setGravity(Gravity.CENTER);

                        TextView highsHeading_invorderdt = initPlainHeaderTextView();
                        highsHeading_invorderdt.setText("Date");
                        highsHeading_invorderdt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_invorderdt.setGravity(Gravity.CENTER);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("SP");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.LEFT);

                        TextView highsheading_productvalue = initPlainHeaderTextView();
                        highsheading_productvalue.setText("Brand");
                        highsheading_productvalue.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_productvalue.setGravity(Gravity.LEFT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_invorderdt);
                        tblrowHeading.addView(highsHeading_custname);
                       // tblrowHeading.addView(highsHeading_contactpersonmobile);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_productvalue);

                        tablesalesregister.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("crm_daily_followup");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String sysorderdtlno;

                                sysorderdtlno=jsonObject.getString("sysorderdtlno");

                                TableRow tblrowLabels = new TableRow(CrmActivity_Installation_Allocation.this);

                                TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("location"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_custname = initPlainTextView(i);
                                highsLabel_custname.setText(jsonObject.getString("custname"));
                                highsLabel_custname.setTypeface(Typeface.DEFAULT);
                                highsLabel_custname.setGravity(Gravity.LEFT);
                                highsLabel_custname.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent( CrmActivity_Installation_Allocation.this, CrmActivity_Installation_Allocation_Single.class);
                                        intent.putExtra("sysorderdtlno",sysorderdtlno);
                                        startActivity(intent);
                                    }
                                });

                                TextView highsLabel_contactpersonmobile = initPlainTextView(i);
                                highsLabel_contactpersonmobile.setText(jsonObject.getString("contactpersonmobile"));
                                highsLabel_contactpersonmobile.setTypeface(Typeface.DEFAULT);
                                highsLabel_contactpersonmobile.setGravity(Gravity.CENTER);

                                TextView highsLabel_invorderdt = initPlainTextView(i);
                                highsLabel_invorderdt.setText(jsonObject.getString("vactivitydt"));
                                highsLabel_invorderdt.setTypeface(Typeface.DEFAULT);
                                highsLabel_invorderdt.setGravity(Gravity.CENTER);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("vendorname"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.LEFT);

                                TextView highslabel_productvalue = initPlainTextView(i);
                                highslabel_productvalue.setText(jsonObject.getString("brandname"));
                                highslabel_productvalue.setTypeface(Typeface.DEFAULT);
                                highslabel_productvalue.setGravity(Gravity.LEFT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_invorderdt);
                                tblrowLabels.addView(highsLabel_custname);
                                //tblrowLabels.addView(highsLabel_contactpersonmobile);
                                 tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_productvalue);

                                tablesalesregister.addView(tblrowLabels);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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

	        TextView textView = new TextView(CrmActivity_Installation_Allocation.this);
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
	        TextView textView = new TextView(CrmActivity_Installation_Allocation.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

	    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(CrmActivity_Installation_Allocation.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
