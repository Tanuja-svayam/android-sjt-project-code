package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

public class EmployeeAttendanceEscallationReport extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TableLayout tablesalesregister;

    String reporttype;
    Button pickFromDate;
    Button pickToDate;

    DatePicker datePicker;
    Calendar calendar;
    TextView dateView;

    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeeattendancereport);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickFromDate = (Button) findViewById(R.id.pickFromDate);
        pickFromDate.setText(Utility.ConvetToDDMMMYYYY(year,month,1));

        pickToDate = (Button) findViewById(R.id.pickToDate);
        pickToDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        invokeWS_MontlyAttendanceEscallation();

    }

    public void invokeWS_MontlyAttendanceEscallation() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysemployeeno  = globalVariable.getsysemployeeno();

            pickFromDate = (Button) findViewById(R.id.pickFromDate);
            pickToDate = (Button) findViewById(R.id.pickToDate);

            paramsMap.put("companycd", "");
            paramsMap.put("fromdate", pickFromDate.getText().toString());
            paramsMap.put("todate", pickToDate.getText().toString());

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Attendance_EscallationSummary", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(EmployeeAttendanceEscallationReport.this);
                        TextView highsHeading_NAME = initPlainHeaderTextView();
                        highsHeading_NAME.setText("EMPLOYEE");
                        highsHeading_NAME.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_NAME.setGravity(Gravity.CENTER);

                        TextView highsHeading_PRESENT = initPlainHeaderTextView();
                        highsHeading_PRESENT.setText("ESCALLATION");
                        highsHeading_PRESENT.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_PRESENT.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_NAME);
                        tblrowHeading.addView(highsHeading_PRESENT);

                        tablesalesregister.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("attendance_register");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(EmployeeAttendanceEscallationReport.this);

                                final TextView highsLabel_NAME = initPlainTextView(i);
                                highsLabel_NAME.setText(jsonObject.getString("employeename"));
                                highsLabel_NAME.setTypeface(Typeface.DEFAULT);
                                highsLabel_NAME.setGravity(Gravity.LEFT);

                                TextView highsLabel_PRESENT = initPlainTextView(i);
                                highsLabel_PRESENT.setText(jsonObject.getString("ESCALLATION"));
                                highsLabel_PRESENT.setTypeface(Typeface.DEFAULT);
                                highsLabel_PRESENT.setGravity(Gravity.CENTER);

                                tblrowLabels.addView(highsLabel_NAME);
                                tblrowLabels.addView(highsLabel_PRESENT);

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

	        TextView textView = new TextView(EmployeeAttendanceEscallationReport.this);
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
	        TextView textView = new TextView(EmployeeAttendanceEscallationReport.this);
	        textView.setPadding(10, 10, 10, 10);
	        textView.setBackgroundResource(R.drawable.cell_shape_header);
	        textView.setTextColor(Color.parseColor("#ffffff"));
	        return textView;
	    }

    private TextView initPlainFooterTextView() {
	        TextView textView = new TextView(EmployeeAttendanceEscallationReport.this);
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
        invokeWS_MontlyAttendanceEscallation();
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
        invokeWS_MontlyAttendanceEscallation();
    }


}
