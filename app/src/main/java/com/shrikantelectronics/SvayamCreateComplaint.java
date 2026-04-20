package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

import java.util.Calendar;
import android.content.Intent;

public class SvayamCreateComplaint extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    EditText remarksET;
    EditText complaint_typeET ;
    EditText complaintpriorityET ;
    EditText complaintnumberET;

    String complaintid="0";
    Button Button_complaint_type, Button_complaintpriority, btnCapturePicture;
    public String foldername ="";

    String priority, complaint_type, issue_type, allocated_to, prioritydesc, complaint_typedesc, issue_typedesc, allocated_toname, resolutiondt, vresolutiondt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svayamcreatecomplaint);

        complaint_typeET = (EditText)findViewById(R.id.complaint_type);
        complaintpriorityET = (EditText)findViewById(R.id.complaintpriority);
        remarksET = (EditText)findViewById(R.id.remarks);

        complaintnumberET = (EditText)findViewById(R.id.complaintnumber);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        errorMsg = (TextView) findViewById(R.id.login_error);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.Button_complaint_type:
                        navigatetocomplaint_typeActivity();
                        break;

                    case R.id.Button_complaintpriority:
                        navigatetoComplaintPriorityListActivity();
                        break;
                }
            }
        };

        Button_complaint_type = (Button) findViewById(R.id.Button_complaint_type);
        Button_complaint_type.setOnClickListener(handler);

        Button_complaintpriority = (Button) findViewById(R.id.Button_complaintpriority);
        Button_complaintpriority.setOnClickListener(handler);

        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                //captureImage();
                navigatetoImageCaptureActivity();
            }
        });

        Intent i = getIntent();
        complaintid = i.getStringExtra("complaintid");

        if (complaintid != null && !complaintid.equals("0")) {
            Invoke_CustomerComplaintDetails();
        } else {
            complaintid = "0";
        }
    }

    public void navigatetoImageCaptureActivity(){


        if (!complaintid.equals("0"))
        {


        String remarks= remarksET.getText().toString();

        Intent homeIntent = new Intent(SvayamCreateComplaint.this,SvayamComplaintDocumentImageActivity.class);

        foldername=complaintnumberET.getText().toString();

        foldername=foldername.replace("/","");
        foldername=foldername.replace("-","");
        foldername=foldername.replace("\\","");

        homeIntent.putExtra("complaintid",complaintid);
        homeIntent.putExtra("foldername",foldername);
        homeIntent.putExtra("remarks",remarks);
        homeIntent.putExtra("priority",priority);
        homeIntent.putExtra("complaint_type",complaint_type);

        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        //finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Complaint Not Saved", Toast.LENGTH_LONG).show();
        }
    }

    public void navigatetocomplaint_typeActivity(){
        Intent homeIntent = new Intent(SvayamCreateComplaint.this,SvayamGeneralCodes_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("gctype","CMTP");
        startActivityForResult(homeIntent,2);
    }

    public void navigatetoComplaintPriorityListActivity(){
        Intent homeIntent = new Intent(SvayamCreateComplaint.this,SvayamGeneralCodes_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("gctype","PRRT");
        startActivityForResult(homeIntent,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                complaint_type = bundle.getString("gccode");
                complaint_typeET.setText(bundle.getString("gcname"));
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                priority = bundle.getString("gccode");
                complaintpriorityET.setText(bundle.getString("gcname"));
            }
        }
    }

    public void onClick_submit(View view) {
        Submit_CreateWalkin();
    }

    public void Submit_CreateWalkin(){

        // Get Email Edit View Value

        String remarks= remarksET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String clientid  = globalVariable.CLIENTID;
        final String subscriptionid  = globalVariable.SUBSCRIPTIONID;
        final String userid  = globalVariable.getuserid();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();
     Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(remarks)) {
            paramsMap.put("complaintid", "0"+complaintid);
                paramsMap.put("clientid", clientid);
                paramsMap.put("subscriptionid", subscriptionid);
                paramsMap.put("complaintby", userid);
                paramsMap.put("complaintdescription",""+ remarks);
                paramsMap.put("priority", ""+priority);
                paramsMap.put("complaint_type", ""+complaint_type);
                paramsMap.put("userid", userid);
                paramsMap.put("complaintattachmentfilename", "X");
            paramsMap.put("allocated_to", "0");
            paramsMap.put("issue_type", "00");

                invokeWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        //STARWING -- SERVER

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddEditComplaint_ForSvayam", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("crm_Complaint");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            if(jsonObject.getString("ResponseMessage").equals("Success")){
                                complaintid = jsonObject.getString("complaintid");
                                complaintnumberET.setText(jsonObject.getString("complaintnumber"));
                                complaintpriorityET.setText(jsonObject.getString("prioritydesc"));
                                complaintpriorityET.setText(jsonObject.getString("complaint_typedesc"));
                                remarksET.setText(jsonObject.getString("complaintdescription"));

                                priority = jsonObject.getString("priority");
                                complaint_type = jsonObject.getString("complaint_type");
                                issue_type = jsonObject.getString("issue_type");
                                allocated_to = jsonObject.getString("allocated_to");
                                issue_typedesc = jsonObject.getString("issue_typedesc");
                                allocated_toname = jsonObject.getString("allocated_todesc");
                                resolutiondt = jsonObject.getString("resolutiondt");
                                vresolutiondt = jsonObject.getString("vresolutiondt");

                                Toast.makeText(getApplicationContext(), "Complaint Added Successfully", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Complaint Registration Failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                   // NavigateToCustoemerOrder();
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

                // When Http response code other than 404, 500
                            }
        });
    }

    public void setDefaultValues(){
        remarksET.setText("");
        complaint_typeET.setText("");
        complaintpriorityET.setText("");

    }

    public void navigatetoFollowupActivity(){
        Intent homeIntent = new Intent(SvayamCreateComplaint.this,CrmActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("status","PENDING");
        finish();
        startActivity(homeIntent);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SvayamCreateComplaint.this);
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
        TextView textView = new TextView(SvayamCreateComplaint.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SvayamCreateComplaint.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    public void Invoke_CustomerComplaintDetails() {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("complaintid", complaintid);
        paramsMap.put("clientid", globalVariable.CLIENTID);
        paramsMap.put("subscriptionid", globalVariable.SUBSCRIPTIONID);
        paramsMap.put("userid", globalVariable.getuserid());
        paramsMap.put("search", "x");
        paramsMap.put("status", "01");

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SearchComplaint_ForSvayam", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("crm_Complaint");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                    complaintid = jsonObject.getString("complaintid");
                    complaintnumberET.setText(jsonObject.getString("complaintnumber"));
                    remarksET.setText(jsonObject.getString("complaintdescription"));

                    complaintpriorityET.setText(jsonObject.getString("prioritydesc"));
                    complaint_typeET.setText(jsonObject.getString("complaint_typedesc"));

                    priority = jsonObject.getString("priority");
                    complaint_type = jsonObject.getString("complaint_type");
                    issue_type = jsonObject.getString("issue_type");
                    allocated_to = jsonObject.getString("allocated_to");
                    issue_typedesc = jsonObject.getString("issue_typedesc");
                    allocated_toname = jsonObject.getString("allocated_todesc");
                    resolutiondt = jsonObject.getString("resolutiondt");
                    vresolutiondt = jsonObject.getString("vresolutiondt");

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
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

}