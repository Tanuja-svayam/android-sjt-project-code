package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;





import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EmployeeAttendanceCreate extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    private static final String TAG = MainActivity.class.getSimpleName();


    private Uri fileUri; // file url to store image/video

    ProgressDialog prgDialog;
    TextView errorMsg;
    TextView employee_nameET ;
    EditText remarksET ;

    RadioGroup radioAttedanceGroup;
    RadioButton radioAttedanceButton;

    String attnstatus;
    String attntype;
    String remarks;

    private Button  submit_button;
    public String foldername ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeeattendancecreate);


            employee_nameET = (TextView)findViewById(R.id.name);

        submit_button= (Button) findViewById(R.id.submit_button);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        employee_nameET.setText(globalVariable.getemployeename());
        // Find Error Msg Text View control by ID
        //errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        foldername=globalVariable.getemployeename();
        foldername=foldername.replace("'","");
        foldername=foldername.replace(".","");
        foldername=foldername.replace(" ","");
        foldername=foldername.replace("/","");
        foldername=foldername.replace("-","");
        foldername=foldername.replace("\\","");

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioAttedanceGroup = (RadioGroup) findViewById(R.id.radioAttedance);
                int selectedId = radioAttedanceGroup.getCheckedRadioButtonId();
                radioAttedanceButton = (RadioButton) findViewById(selectedId);

                if (selectedId==R.id.radioTimeIn)
                {
                    invokeCheckExistingArrendanceWS();
                }
                else
                {
                    invokeWS();
                    finish();
                }
            }
        });
    }


    public void navigatetoImageCaptureActivity(){

        foldername=employee_nameET.getText().toString();

        Intent homeIntent = new Intent(EmployeeAttendanceCreate.this,AttendanceImageActivity.class);
        homeIntent.putExtra("attnstatus","01");
        homeIntent.putExtra("attntype","I");
        homeIntent.putExtra("fileuri","I");
        homeIntent.putExtra("foldername",foldername);

        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
    }

    public void invokeWS(){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        remarksET = (EditText)findViewById(R.id.remarks);

        radioAttedanceGroup = (RadioGroup) findViewById(R.id.radioAttedance);
        int selectedId = radioAttedanceGroup.getCheckedRadioButtonId();
        radioAttedanceButton = (RadioButton) findViewById(selectedId);

        switch(selectedId){
            case R.id.radioTimeIn:
                attnstatus = "01";
                attntype = "I";
                break;
            case R.id.radioTimeOut:
                attnstatus = "01";
                attntype = "O";
                break;
            case R.id.radioWeeklyOff:
                attnstatus = "06";
                attntype = "";
                break;
            case R.id.radioLeave:
                attnstatus = "04";
                attntype = "";
                break;
            case R.id.radioHoliday:
                attnstatus = "05";
                attntype = "";
                break;
        }

        String sLatitude, sLongitude;
        sLatitude = "0";
        sLongitude ="0";

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysemployeeno", sysemployeeno);
        paramsMap.put("attnstatus", attnstatus);
        paramsMap.put("attntype", attntype);
        paramsMap.put("remarks"," " + remarksET.getText());
        paramsMap.put("userid", userid);
        paramsMap.put("latitude", sLatitude);
        paramsMap.put("longitude",sLongitude);
        paramsMap.put("fileuri", "");

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddEmployeeAttendance", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
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
                prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }


    public void invokeCheckExistingArrendanceWS(){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();
        final String userid  = globalVariable.getuserid();

        remarksET = (EditText)findViewById(R.id.remarks);

        radioAttedanceGroup = (RadioGroup) findViewById(R.id.radioAttedance);
        int selectedId = radioAttedanceGroup.getCheckedRadioButtonId();
        radioAttedanceButton = (RadioButton) findViewById(selectedId);

        switch(selectedId){
            case R.id.radioTimeIn:
                attnstatus = "01";
                attntype = "I";
                break;
            case R.id.radioTimeOut:
                attnstatus = "01";
                attntype = "O";
                break;
            case R.id.radioWeeklyOff:
                attnstatus = "06";
                attntype = "";
                break;
            case R.id.radioLeave:
                attnstatus = "04";
                attntype = "";
                break;
            case R.id.radioHoliday:
                attnstatus = "05";
                attntype = "";
                break;
        }

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysemployeeno", sysemployeeno);
        paramsMap.put("attnstatus", attnstatus);
        paramsMap.put("attntype", attntype);
        paramsMap.put("remarks"," " + remarksET.getText());
        paramsMap.put("userid", userid);
        paramsMap.put("fileuri", "");

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/CheckEmployeeAttendance", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;

                    if(obj.getBoolean("status"))
                    {
                        navigatetoImageCaptureActivity();
                    }
                    else
                    {
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

                // When Http response code other than 404, 500
                            }
        });
    }
}