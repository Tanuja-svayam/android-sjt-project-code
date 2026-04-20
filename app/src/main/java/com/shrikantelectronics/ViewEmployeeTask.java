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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

public class ViewEmployeeTask extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    OnClickListener tablerowOnClickListener;

    TableLayout tablermployeetask;
    Button pickAttendanceDate;
    String sysemployeeno;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewemployeetask);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();
        sysemployeeno = globalVariable.getsysemployeeno();

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickAttendanceDate = (Button) findViewById(R.id.pickAttendanceDate);
        pickAttendanceDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        invokeWS_TopCategoryLocationStock();

    }

    public void invokeWS_TopCategoryLocationStock() {
        try {

            pickAttendanceDate = (Button) findViewById(R.id.pickAttendanceDate);
            String attndate = pickAttendanceDate.getText().toString();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysemployeeno", ""+ sysemployeeno);
            paramsMap.put("attndate", ""+ attndate);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);


            ApiHelper.post(URL + "Service1.asmx/Employee_Task_Activity", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablermployeetask  = (TableLayout) findViewById(R.id.tablermployeetask);
                        tablermployeetask.removeAllViews();


                      TableRow tblrowHeading = new TableRow(ViewEmployeeTask.this);
                        TextView highsHeading_description = initPlainHeaderTextView();
                        highsHeading_description.setText("Task");
                        highsHeading_description.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_description.setGravity(Gravity.LEFT);

                        TextView highsHeading_taskstatus= initPlainHeaderTextView();
                        highsHeading_taskstatus.setText("Status");
                        highsHeading_taskstatus.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_taskstatus.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_description);
                        tblrowHeading.addView(highsHeading_taskstatus);

                        tablermployeetask.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("charges_list");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(ViewEmployeeTask.this);

                                final TextView highsLabel_chargename = initPlainTextView(i);
                                highsLabel_chargename.setText(jsonObject.getString("description"));
                                highsLabel_chargename.setTypeface(Typeface.DEFAULT);
                                highsLabel_chargename.setGravity(Gravity.LEFT);

                                CheckBox highsLabel_taskstatus = new CheckBox(ViewEmployeeTask.this);
                                highsLabel_taskstatus.setPadding(10, 10, 10, 10);

                                if (jsonObject.getString("taskstatus").equals("01"))
                                {
                                    highsLabel_taskstatus.setChecked(true);
                                }
                                else
                                {
                                    highsLabel_taskstatus.setChecked(false);
                                }

                                highsLabel_taskstatus.setTypeface(Typeface.DEFAULT);
                                highsLabel_taskstatus.setGravity(Gravity.CENTER);
                                highsLabel_taskstatus.setTextColor(Color.parseColor("#ff670f"));

                                tblrowLabels.addView(highsLabel_chargename);
                                tblrowLabels.addView(highsLabel_taskstatus);

                                tblrowLabels.setClickable(true);

                                tablermployeetask.addView(tblrowLabels);


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



    private EditText initPlainEditText(float n) {

        EditText editText = new EditText(ViewEmployeeTask.this);
        editText.setPadding(10, 10, 10, 10);

        return editText;
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(ViewEmployeeTask.this);
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
        TextView textView = new TextView(ViewEmployeeTask.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(ViewEmployeeTask.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    public void onClick_Update_CallCharges(View view) {

        TableLayout layout = (TableLayout) findViewById(R.id.tablermployeetask);

        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                if (i>0) {
                    View responsibltyname = row.getChildAt(0);
                    View input_taskstatus = row.getChildAt(1);

                    String responsibltyname1 = "";
                            String  input_taskstatus1  = "00";

                    if (responsibltyname != null && responsibltyname instanceof TextView) {
                        try {
                            TextView textView = (TextView) responsibltyname;
                            responsibltyname1 = textView.getText().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (input_taskstatus != null && input_taskstatus instanceof CheckBox) {
                        try {
                            CheckBox editText = (CheckBox) input_taskstatus;

                            if(editText.isChecked())
                            {
                                input_taskstatus1 = "01";
                            }
                            else
                            {
                                input_taskstatus1 = "00";
                            };


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    invokeWS(responsibltyname1, input_taskstatus1);
                }
            }
        }

        Toast.makeText(getApplicationContext(),"Charges Update", Toast.LENGTH_LONG).show();
        invokeWS_TopCategoryLocationStock();
    }


    public void invokeWS(String taskdescription, String taskstatus){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        pickAttendanceDate = (Button) findViewById(R.id.pickAttendanceDate);
        String attndate = pickAttendanceDate.getText().toString();
        String folupconversation = "";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("attndate", "0" + attndate);
        paramsMap.put("sysemployeeno", "0" + sysemployeeno);
        paramsMap.put("taskdescription",  "" + taskdescription);
        paramsMap.put("folupconversation",  "" + folupconversation);
        paramsMap.put("taskstatus", "" +taskstatus);
        paramsMap.put("userid", "" + userid);
        paramsMap.put("latitude", "" );
        paramsMap.put("longitude","" );
        paramsMap.put("fileuri", "" );

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddEmployeeTask", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = response;
                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

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
                //  prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }





    @SuppressWarnings("deprecation")
    public void setAttendanceDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    AttendanceDateListener, year, month, day);
        }


        return null;
    }

    private DatePickerDialog.OnDateSetListener AttendanceDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showPurchaseDate(arg1, arg2, arg3);
                }
            };


    private void showPurchaseDate(int year, int month, int day) {
        pickAttendanceDate = (Button) findViewById(R.id.pickAttendanceDate);
        pickAttendanceDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        invokeWS_TopCategoryLocationStock();
    }


}
