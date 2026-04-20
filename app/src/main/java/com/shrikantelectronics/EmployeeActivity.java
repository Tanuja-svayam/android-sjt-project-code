package com.shrikantelectronics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    TextView header_salesregister;
    String reporttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        invokeWS_MontlyAttendance();

    }

    public void invokeWS_MontlyAttendance() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysemployeeno  = globalVariable.getsysemployeeno();

            paramsMap.put("sysemployeeno", sysemployeeno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_salesregister= (TextView) findViewById(R.id.header_salesregister);
            header_salesregister.setText("THIS MONTH ATTENDANCE DASHBOARD");

            ApiHelper.post(URL + "Service1.asmx/Attendance_MonthSummary_Employee", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(EmployeeActivity.this);
                        TextView highsHeading_PRESENT = initPlainHeaderTextView();
                        highsHeading_PRESENT.setText("PRESENT");
                        highsHeading_PRESENT.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_PRESENT.setGravity(Gravity.CENTER);

                        TextView highsHeading_ABSENT = initPlainHeaderTextView();
                        highsHeading_ABSENT.setText("ABSENT");
                        highsHeading_ABSENT.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_ABSENT.setGravity(Gravity.CENTER);

                        TextView highsheading_HALF_DAY = initPlainHeaderTextView();
                        highsheading_HALF_DAY.setText("HALF DAY");
                        highsheading_HALF_DAY.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_HALF_DAY.setGravity(Gravity.CENTER);

                        TextView highsheading_LEAVE = initPlainHeaderTextView();
                        highsheading_LEAVE.setText("LEAVE");
                        highsheading_LEAVE.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_LEAVE.setGravity(Gravity.CENTER);

                        TextView highsheading_HOLIDAY = initPlainHeaderTextView();
                        highsheading_HOLIDAY.setText("HOLIDAY");
                        highsheading_HOLIDAY.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_HOLIDAY.setGravity(Gravity.CENTER);

                        TextView highsheading_WEEKLY_OFF = initPlainHeaderTextView();
                        highsheading_WEEKLY_OFF.setText("WEEKLY_OFF");
                        highsheading_WEEKLY_OFF.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_WEEKLY_OFF.setGravity(Gravity.CENTER);


                        tblrowHeading.addView(highsHeading_PRESENT);
                        tblrowHeading.addView(highsHeading_ABSENT);

                        tblrowHeading.addView(highsheading_LEAVE);
                        tblrowHeading.addView(highsheading_HOLIDAY);
                        tblrowHeading.addView(highsheading_WEEKLY_OFF);

                        tablesalesregister.addView(tblrowHeading);



                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("attendance_register");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(EmployeeActivity.this);

                               final TextView highsLabel_PRESENT = initPlainTextView(i);
                                highsLabel_PRESENT.setText(jsonObject.getString("PRESENT"));
                                highsLabel_PRESENT.setTypeface(Typeface.DEFAULT);
                                highsLabel_PRESENT.setGravity(Gravity.CENTER);

                                TextView highsLabel_ABSENT = initPlainTextView(i);
                                highsLabel_ABSENT.setText(jsonObject.getString("ABSENT"));
                                highsLabel_ABSENT.setTypeface(Typeface.DEFAULT);
                                highsLabel_ABSENT.setGravity(Gravity.CENTER);

                                TextView highslabel_HALF_DAY = initPlainTextView(i);
                                highslabel_HALF_DAY.setText(jsonObject.getString("HALF_DAY"));
                                highslabel_HALF_DAY.setTypeface(Typeface.DEFAULT);
                                highslabel_HALF_DAY.setGravity(Gravity.CENTER);

                                TextView highslabel_LEAVE = initPlainTextView(i);
                                highslabel_LEAVE.setText(jsonObject.getString("LEAVE"));
                                highslabel_LEAVE.setTypeface(Typeface.DEFAULT);
                                highslabel_LEAVE.setGravity(Gravity.CENTER);

                                TextView highslabel_HOLIDAY = initPlainTextView(i);
                                highslabel_HOLIDAY.setText(jsonObject.getString("HOLIDAY"));
                                highslabel_HOLIDAY.setTypeface(Typeface.DEFAULT);
                                highslabel_HOLIDAY.setGravity(Gravity.CENTER);

                                TextView highslabel_WEEKLY_OFF = initPlainTextView(i);
                                highslabel_WEEKLY_OFF.setText(jsonObject.getString("WEEKLY_OFF"));
                                highslabel_WEEKLY_OFF.setTypeface(Typeface.DEFAULT);
                                highslabel_WEEKLY_OFF.setGravity(Gravity.CENTER);

                                tblrowLabels.addView(highsLabel_PRESENT);
                                tblrowLabels.addView(highsLabel_ABSENT);

                                tblrowLabels.addView(highslabel_LEAVE);
                                tblrowLabels.addView(highslabel_HOLIDAY);
                                tblrowLabels.addView(highslabel_WEEKLY_OFF);

                                tablesalesregister.addView(tblrowLabels);

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

	        TextView textView = new TextView(EmployeeActivity.this);
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
	        TextView textView = new TextView(EmployeeActivity.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(EmployeeActivity.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_footer);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
    }
}
