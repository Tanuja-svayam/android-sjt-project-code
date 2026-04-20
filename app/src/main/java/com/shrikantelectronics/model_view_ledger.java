package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class model_view_ledger extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;
    TextView txtmodelcode;

    String groupcode;
    String modelcode1;

    String sysmodelno;
    String fromdate;
    String todate;
    String sysuserno;

    Button pickFromDate;
    Button pickToDate;
    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;

    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_view_ledger);

        Intent i = getIntent();
        sysmodelno = i.getStringExtra("sysmodelno");
        modelcode1 = i.getStringExtra("modelcode");

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        groupcode  = globalVariable.getgroupcode();

        txtmodelcode = (TextView) findViewById(R.id.modelcode1);
        txtmodelcode.setText(modelcode1);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickToDate = (Button) findViewById(R.id.pickToDate);

        pickFromDate.setText(Config.FROMDATE);
        pickToDate.setText(Config.TODATE);

        invokeWS_ModelLedger();

    }

    public void invokeWS_ModelLedger() {

        try {


            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno  = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysmodelno", sysmodelno);
            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());
            paramsMap.put("sysuserno", sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/GetModel_Ledger", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.removeAllViews();

                        //tabletoplocationstock.setStretchAllColumns(true);
                        //tabletoplocationstock.setShrinkAllColumns(true);

                        // systrnno, sysmodelno, sourcetablepk, particulars, trnid, drcr, trndt,
                           //     serialno, in_quantity, in_value, avg_amount, out_quantity, out_value, cl_quantity, cl_value, trndesc, trdate, partyname, documentno, menuurl, sororder, stockid, companycd

                        TableRow tblrowHeading = new TableRow(model_view_ledger.this);

                        TextView highsHeading_trndt = initPlainHeaderTextView();
                        highsHeading_trndt.setText("Date");
                        highsHeading_trndt.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_trndt.setGravity(Gravity.CENTER);

                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Particulars");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_trndesc = initPlainHeaderTextView();
                        highsHeading_trndesc.setText("Type");
                        highsHeading_trndesc.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_trndesc.setGravity(Gravity.LEFT);

                        TextView highsHeading_documentno = initPlainHeaderTextView();
                        highsHeading_documentno.setText("Document");
                        highsHeading_documentno.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_documentno.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("In Qty");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.RIGHT);

                        TextView highsHeading_serial_netlancost = initPlainHeaderTextView();
                        highsHeading_serial_netlancost.setText("Out Qty");
                        highsHeading_serial_netlancost.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_serial_netlancost.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("CL Qty");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        TextView highsHeading_InValue = initPlainHeaderTextView();
                        TextView highsHeading_OutValue = initPlainHeaderTextView();
                        TextView highsheading_ClosingValue = initPlainHeaderTextView();

                        if (groupcode.equals("SAG")) {

                            highsHeading_InValue.setText("In Value");
                            highsHeading_InValue.setTypeface(Typeface.DEFAULT_BOLD);
                            highsHeading_InValue.setGravity(Gravity.RIGHT);

                            highsHeading_OutValue.setText("Out Value");
                            highsHeading_OutValue.setTypeface(Typeface.DEFAULT_BOLD);
                            highsHeading_OutValue.setGravity(Gravity.RIGHT);

                            highsheading_ClosingValue.setText("CL Value");
                            highsheading_ClosingValue.setTypeface(Typeface.DEFAULT_BOLD);
                            highsheading_ClosingValue.setGravity(Gravity.RIGHT);
                        }

                        tblrowHeading.addView(highsHeading_trndt);
                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_trndesc);
                        tblrowHeading.addView(highsHeading_documentno);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsHeading_serial_netlancost);
                        tblrowHeading.addView(highsheading_Value);

                        if (groupcode.equals("SAG")) {
                            tblrowHeading.addView(highsHeading_InValue);
                            tblrowHeading.addView(highsHeading_OutValue);
                            tblrowHeading.addView(highsheading_ClosingValue);
                        }

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("model_ledger");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(model_view_ledger.this);

                                TextView highsLabel_trndt = initPlainTextView(i);
                                highsLabel_trndt.setText(jsonObject.getString("trndt"));
                                highsLabel_trndt.setTypeface(Typeface.DEFAULT);
                                highsLabel_trndt.setGravity(Gravity.CENTER);

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("particulars"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                TextView highsLabel_trndesc = initPlainTextView(i);
                                highsLabel_trndesc.setText(jsonObject.getString("trndesc"));
                                highsLabel_trndesc.setTypeface(Typeface.DEFAULT);
                                highsLabel_trndesc.setGravity(Gravity.LEFT);

                                TextView highsLabel_documentno = initPlainTextView(i);
                                highsLabel_documentno.setText(jsonObject.getString("documentno"));
                                highsLabel_documentno.setTypeface(Typeface.DEFAULT);
                                highsLabel_documentno.setGravity(Gravity.LEFT);

                                TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("in_quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highslabel_serial_netlancost = initPlainTextView(i);
                                highslabel_serial_netlancost.setText(jsonObject.getString("out_quantity"));
                                highslabel_serial_netlancost.setTypeface(Typeface.DEFAULT);
                                highslabel_serial_netlancost.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("cl_quantity"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                TextView highslabel_InValue = initPlainTextView(i);
                                TextView highslabel_OutValue = initPlainTextView(i);
                                TextView highslabel_ClosingValue = initPlainTextView(i);


                                if (groupcode.equals("SAG")) {


                                    highslabel_InValue.setText(jsonObject.getString("in_value"));
                                    highslabel_InValue.setTypeface(Typeface.DEFAULT);
                                    highslabel_InValue.setGravity(Gravity.RIGHT);

                                    highslabel_OutValue.setText(jsonObject.getString("out_value"));
                                    highslabel_OutValue.setTypeface(Typeface.DEFAULT);
                                    highslabel_OutValue.setGravity(Gravity.RIGHT);

                                    highslabel_ClosingValue.setText(jsonObject.getString("cl_value"));
                                    highslabel_ClosingValue.setTypeface(Typeface.DEFAULT);
                                    highslabel_ClosingValue.setGravity(Gravity.RIGHT);

                                }
                                tblrowLabels.addView(highsLabel_trndt);
                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_trndesc);
                                tblrowLabels.addView(highsLabel_documentno);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highslabel_serial_netlancost);
                                tblrowLabels.addView(highslabel_Value);

                                if (groupcode.equals("SAG")) {
                                    tblrowLabels.addView(highslabel_InValue);
                                    tblrowLabels.addView(highslabel_OutValue);
                                    tblrowLabels.addView(highslabel_ClosingValue);
                                }

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

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(model_view_ledger.this);
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
        TextView textView = new TextView(model_view_ledger.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(model_view_ledger.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }



    @SuppressWarnings("deprecation")
    public void setFromDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }


    @SuppressWarnings("deprecation")
    public void setToDate(View view) {
        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    fromDateListener, year, month, day);
        }

        if (id == 998) {
            return new DatePickerDialog(this,
                    toDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener fromDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showFromDate(arg1, arg2, arg3);
                }
            };

    private void showFromDate(int year, int month, int day) {
        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        Config.FROMDATE = Utility.ConvetToDDMMMYYYY(year,month,day);

        invokeWS_ModelLedger();
    }

    private DatePickerDialog.OnDateSetListener toDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showToDate(arg1, arg2, arg3);
                }
            };

    private void showToDate(int year, int month, int day) {
        pickToDate = (Button) findViewById(R.id.pickToDate);
        pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        Config.TODATE = Utility.ConvetToDDMMMYYYY(year,month,day);

        invokeWS_ModelLedger();
    }

}
