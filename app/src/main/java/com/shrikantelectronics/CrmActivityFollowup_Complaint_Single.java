package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;

public class CrmActivityFollowup_Complaint_Single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;


    ProgressDialog prgDialog;

    TextView txtcustcd;
    TextView txtcustname;
    TextView txtmobile;
    TextView txtemailid;
    TextView txtoutstanding;
    TextView txtaddress;
    TextView txtinquiredproduct;
    TextView errorMsg;

    String syscustactno;
    String custcd1;
    String custname1;
    String mobile1;
    String address1;
    String sysinvorderno ="0" ;
    String custcd ="0";
    String custname ="0";

    EditText folupconversationET;
    TextView pickNextFollowupDate;
    int year, month, day;
    String todate;

    EditText txtNextFollowupTime;

    String sysfollowupno;

    String activitytype;
    String activitytypedesc;
    EditText activitytypedescTV;

    TableLayout tablefollowup_history;
    TextView header_followuphistory;



    ListViewAdapter_Customer_Product adapter;
    private ListView lv_customer_Product ;
    ArrayList<Customer_Product> arraylist = new ArrayList<Customer_Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crmcomplaintcustomer_view_single);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        syscustactno= i.getStringExtra("syscustactno");
        custcd1 = i.getStringExtra("custcd");
        custname1= i.getStringExtra("custname");

        sysfollowupno = "";
        activitytypedesc = "Customer Complaints";
        activitytype ="01";
        activitytypedescTV = (EditText) findViewById(R.id.activitytypedesc);

        // Locate the TextViews in singleitemview.xml
        txtcustname = (TextView) findViewById(R.id.name);
        txtmobile = (TextView) findViewById(R.id.mobile);
        txtaddress = (TextView) findViewById(R.id.address);
        txtNextFollowupTime= (EditText) findViewById(R.id.NextFollowupTime);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        invokeWS_Customer_Details(custcd1);
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

        if (id == 998) {
            return new DatePickerDialog(this,
                    toDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener toDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {showToDate(arg1, arg2, arg3);    }
            };

    private void showToDate(int year, int month, int day) {
        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

    }

    public void CallDialer(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile1));

        startActivity(intent);
    }

    public void CallSms(View view) {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"+ mobile1));
        sendIntent.putExtra("sms_body", "" );
        startActivity(sendIntent);
    }

    public void CallEmail(View view) {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, "");
        email.putExtra(Intent.EXTRA_SUBJECT, "");
        email.putExtra(Intent.EXTRA_TEXT, "" );
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    public void invokeWS_Customer_Details(String custcd){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("custcd", custcd1);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetFollowupCustomerDetailsSingle_Complaint", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        txtcustname.setText(jsonObject.getString("name"));
                        txtmobile.setText(jsonObject.getString("contactpersonmobile"));
                        txtaddress.setText(jsonObject.getString("address"));

                        custname1 = jsonObject.getString("name");
                        mobile1 = jsonObject.getString("contactpersonmobile");
                        address1 = jsonObject.getString("address");

                        invokeWS_Followup_History(syscustactno);

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

    public void WalkInCustomerClose(View view) {
        // Get Email Edit View Value
        Map<String, String> paramsMap = new HashMap<>();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        folupconversationET = (EditText) findViewById(R.id.folupconversation);

        String folupconversation = folupconversationET.getText().toString();

        paramsMap.put("syscustactno", syscustactno);
        paramsMap.put("custcd", custcd1);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("userid", userid);

        invokeWalkInCustomerCloseWS(paramsMap);
    }

    public void invokeWalkInCustomerCloseWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/CloseWalkinCustomer", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "Customer is closed successfully..!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), CrmActivityComplaints.class);
                        intent.putExtra("activitytype",activitytype);
                        intent.putExtra("activitytypedesc",activitytypedesc);
                        intent.putExtra("locationname","");
                        intent.putExtra("followupstatus","'00'");
                        intent.putExtra("status","PENDING");
                        finish();
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
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
                prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'


            }
        });
    }

    public void WalkInCustomerCreate(View view) {
        // Get Email Edit View Value
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("custcd", custcd1);
        paramsMap.put("userid", userid);
        invokeWalkInCustomerCreateWS(paramsMap);
    }

    public void invokeWalkInCustomerCreateWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/CreateWalkinCustomerToCustomer", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                   Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                    Intent homeIntent = new Intent(CrmActivityFollowup_Complaint_Single.this,CrmActivityComplaints.class);
                    homeIntent.putExtra("status","PENDING");
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(homeIntent);

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
                prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void Update_Followup(View view) {
        // Get Email Edit View Value
        Map<String, String> paramsMap = new HashMap<>();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        folupconversationET = (EditText) findViewById(R.id.folupconversation);

        String folupconversation = folupconversationET.getText().toString();
        String followupdate = pickNextFollowupDate.getText().toString();
        String followuptime = txtNextFollowupTime.getText().toString();

        paramsMap.put("syscustactno", "0" + syscustactno);
        paramsMap.put("custcd", "0" + custcd1);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("nextfollowupdate", "" + followupdate);
        paramsMap.put("nextfollowuptime", "" + followuptime);
        paramsMap.put("userid", userid);

        invokeUpdateFollowupWS(paramsMap);

    }

    public void invokeUpdateFollowupWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/UpdateFolloupStatus", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "Followup is updated successfully..!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CrmActivityComplaints.class);
                        intent.putExtra("activitytype",activitytype);
                        intent.putExtra("activitytypedesc",activitytypedesc);
                        intent.putExtra("locationname","");
                        intent.putExtra("followupstatus","'00'");
                        intent.putExtra("status","PENDING");
                        finish();
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
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
                prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void invokeWS_Followup_History(String syscustactno) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("syscustactno", syscustactno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            header_followuphistory= (TextView) findViewById(R.id.header_followuphistory);
            header_followuphistory.setText("Followup Details");

            ApiHelper.post(URL + "Service1.asmx/CRM_Followup_Details_By_Activity", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablefollowup_history  = (TableLayout) findViewById(R.id.tablefollowup_history);

                        tablefollowup_history.removeAllViews();
                        tablefollowup_history.setStretchAllColumns(true);
                        tablefollowup_history.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(CrmActivityFollowup_Complaint_Single.this);
                        TextView highsHeading_followupdate = initPlainHeaderTextView();
                        highsHeading_followupdate.setText("Followup Date");
                        highsHeading_followupdate.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_followupdate.setGravity(Gravity.LEFT);

                        TextView highsHeading_conversation = initPlainHeaderTextView();
                        highsHeading_conversation.setText("Conversation");
                        highsHeading_conversation.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_conversation.setGravity(Gravity.LEFT);

                        tblrowHeading.addView(highsHeading_followupdate);
                        tblrowHeading.addView(highsHeading_conversation);

                        tablefollowup_history.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("crm_daily_followup_details");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String sysfollowupno;

                                sysfollowupno=jsonObject.getString("sysfollowupno");

                                TableRow tblrowLabels = new TableRow(CrmActivityFollowup_Complaint_Single.this);

                                TextView highsLabel_followupdate = initPlainTextView(i);
                                highsLabel_followupdate.setText(jsonObject.getString("vfollowupdt"));
                                highsLabel_followupdate.setTypeface(Typeface.DEFAULT);
                                highsLabel_followupdate.setGravity(Gravity.CENTER);

                                TextView highsLabel_conversation = initPlainTextView(i);
                                highsLabel_conversation.setText(jsonObject.getString("folupconversation"));
                                highsLabel_conversation.setTypeface(Typeface.DEFAULT);
                                highsLabel_conversation.setGravity(Gravity.LEFT);

                                tblrowLabels.addView(highsLabel_followupdate);
                                tblrowLabels.addView(highsLabel_conversation);

                                tablefollowup_history.addView(tblrowLabels);

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
                    // Hide Progress Dialog
                    //     prgDialog.hide();
                    // When Http response code is '404'
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    // When Http response code is '500'

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(CrmActivityFollowup_Complaint_Single.this);
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
        TextView textView = new TextView(CrmActivityFollowup_Complaint_Single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(CrmActivityFollowup_Complaint_Single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}
