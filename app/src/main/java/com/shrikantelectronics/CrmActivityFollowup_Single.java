package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CrmActivityFollowup_Single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    EditText activitytypedescTV;
    EditText activityrefnoTV;
    EditText referencecodeTV;
    EditText vactivitydtTV;
    EditText vfollowupdtTV;
    EditText employeenameTV;
    EditText purposedescTV;
    EditText modedescTV;
    EditText custnameTV;
    EditText contactpersonmobileTV;
    EditText telephoneTV;
    EditText emailidTV;
    EditText brandnameTV;
    EditText modelnameTV;
    EditText serialnoTV;
    EditText salesmenTV;
    EditText locationTV;
    EditText chargesTV;
    EditText folupconversationET;

    TextView pickNextFollowupDate;
    int year, month, day;
    String todate;

    String sysfollowupno;

    String activitytypedesc;
    String activitytype;
    String activityrefno;
    String referencecode;
    String vactivitydt;
    String vfollowupdt;
    String employeename;
    String purposedesc;
    String modedesc;
    String custname;
    String contactpersonmobile;
    String telephone;
    String emailid;
    String brandname;
    String modelname;
    String serialno;
    String salesmen;
    String location;
    String charges;
    String sfolupconversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crmfollowup_view_single);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        sysfollowupno = i.getStringExtra("sysfollowupno");
        activitytypedesc = i.getStringExtra("activitytypedesc");
        activitytype = i.getStringExtra("activitytype");

        activitytypedescTV = (EditText) findViewById(R.id.activitytypedesc);
        activityrefnoTV = (EditText) findViewById(R.id.activityrefno);
        referencecodeTV = (EditText) findViewById(R.id.referencecode);
        vactivitydtTV = (EditText) findViewById(R.id.vactivitydt);
        vfollowupdtTV = (EditText) findViewById(R.id.vfollowupdt);
        employeenameTV = (EditText) findViewById(R.id.employeename);
        purposedescTV = (EditText) findViewById(R.id.purposedesc);
        modedescTV = (EditText) findViewById(R.id.modedesc);
        custnameTV = (EditText) findViewById(R.id.custname);
        contactpersonmobileTV = (EditText) findViewById(R.id.contactpersonmobile);
        telephoneTV = (EditText) findViewById(R.id.telephone);
        emailidTV = (EditText) findViewById(R.id.emailid);
        brandnameTV = (EditText) findViewById(R.id.brandname);
        modelnameTV = (EditText) findViewById(R.id.modelname);
        serialnoTV = (EditText) findViewById(R.id.serialno);
        salesmenTV = (EditText) findViewById(R.id.salesmen);
        locationTV = (EditText) findViewById(R.id.location);
        chargesTV = (EditText) findViewById(R.id.charges);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        invokeWS_Followup_Details(sysfollowupno);
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

      //  Intent intent = new Intent(Intent.ACTION_DIAL);
        //intent.setData(Uri.parse("tel:" + mobile1));

      //  startActivity(intent);
    }

    public void CallSms(View view) {

        ///Intent sendIntent = new Intent(Intent.ACTION_VIEW);
       // sendIntent.setData(Uri.parse("sms:"+ mobile1));
      //  sendIntent.putExtra("sms_body", "Outstanding of Rs. " +  outstanding1 + " is due from " + vinvoicedt1 + ". Kindly make the payment at earliest. Please ignore if already paid." );
       // startActivity(sendIntent);
    }

    public void CallEmail(View view) {

      //  Intent email = new Intent(Intent.ACTION_SEND);
      //  email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailid1});
      //  email.putExtra(Intent.EXTRA_SUBJECT, "Outstanding");
      //  email.putExtra(Intent.EXTRA_TEXT, "Outstanding of Rs. " +  outstanding1 + " is due from " + vinvoicedt1 + ". Kindly make the payment at earliest. Please ignore if already paid." );
      //  email.setType("message/rfc822");
      //  startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    public void invokeWS_Followup_Details(String sysfollowupno){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysfollowupno", sysfollowupno);


            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetFollowupDetailSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("followupsingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        final String sysserviceno;
                        final String sysemployeeno;
                        sysserviceno = jsonObject.getString("sysserviceno");
                        sysemployeeno = jsonObject.getString("sysemployeeno");
                        activitytype = jsonObject.getString("activitytype");
                        activitytypedesc = jsonObject.getString("activitytypedesc");

                        activitytypedescTV.setText(jsonObject.getString("activitytypedesc"));
                        activityrefnoTV.setText(jsonObject.getString("activityrefno"));
                        referencecodeTV.setText(jsonObject.getString("referencecode"));
                        vactivitydtTV.setText(jsonObject.getString("vactivitydt"));
                        vfollowupdtTV.setText(jsonObject.getString("vfollowupdt"));
                        employeenameTV.setText(jsonObject.getString("employeename"));
                        purposedescTV.setText(jsonObject.getString("purposedesc"));
                        modedescTV.setText(jsonObject.getString("modedesc"));
                        custnameTV.setText(jsonObject.getString("custname"));
                        contactpersonmobileTV.setText(jsonObject.getString("contactpersonmobile"));
                        telephoneTV.setText(jsonObject.getString("telephone"));
                        emailidTV.setText(jsonObject.getString("emailid"));
                        brandnameTV.setText(jsonObject.getString("brandname"));
                        modelnameTV.setText(jsonObject.getString("modelname"));
                        serialnoTV.setText(jsonObject.getString("serialno"));
                        salesmenTV.setText(jsonObject.getString("salesmen"));
                        locationTV.setText(jsonObject.getString("location"));
                        chargesTV.setText(jsonObject.getString("charges"));



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

    public void Update_Followup(View view) {
        // Get Email Edit View Value
        Map<String, String> paramsMap = new HashMap<>();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        folupconversationET = (EditText) findViewById(R.id.folupconversation);

        String folupconversation = folupconversationET.getText().toString();

        paramsMap.put("sysfollowupno", sysfollowupno);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("nextfollowupdate", pickNextFollowupDate.getText().toString());
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
                        Intent intent = new Intent(getApplicationContext(), CrmActivity.class);
                        intent.putExtra("activitytype",activitytype);
                        intent.putExtra("activitytypedesc",activitytypedesc);
                        intent.putExtra("locationname","");
                        intent.putExtra("followupstatus","'00'");
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


    public void Update_Resolved(View view) {
        // Get Email Edit View Value
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid = globalVariable.getuserid();

        folupconversationET = (EditText) findViewById(R.id.folupconversation);
        String folupconversation = folupconversationET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysfollowupno", sysfollowupno);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("activitystatus", "05");
        paramsMap.put("userid", userid);

        invokeUpdateLeadStatusWS(paramsMap);
    }

    public void Update_Cancelled(View view) {
        // Get Email Edit View Value
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid = globalVariable.getuserid();

        folupconversationET = (EditText) findViewById(R.id.folupconversation);
        String folupconversation = folupconversationET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysfollowupno", sysfollowupno);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("activitystatus", "99");
        paramsMap.put("userid", userid);

        invokeUpdateLeadStatusWS(paramsMap);
    }

    public void invokeUpdateLeadStatusWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/UpdateActivityStatus", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "Activity Updated Successfully..!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CrmActivity.class);
                        intent.putExtra("activitytype",activitytype);
                        intent.putExtra("activitytypedesc",activitytypedesc);
                        intent.putExtra("locationname","");
                        intent.putExtra("followupstatus","'00'");
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

}
