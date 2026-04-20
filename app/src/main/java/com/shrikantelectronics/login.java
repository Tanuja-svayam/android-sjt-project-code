package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.ContentValues;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class login extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;
    private static String STATICIP = Config.STATICIP;

    String macwlan ;
    String refreshedToken;

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;

    Button btnLogin;
    Button btnSubmit;

    String master_userid="";

    private String username,password;
    private Button ok;
    private EditText editTextUsername,editTextpassword;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find username Edit View control by ID
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        // Find password Edit View control by ID
        editTextpassword = (EditText) findViewById(R.id.editTextPassword);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            editTextpassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
            //loginUser();
        }

        errorMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                navigatetoShare(errorMsg.getText().toString());
            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private boolean isUserRegistered() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);

            //    cursor.getColumnIndex("user_count")

            cursor.close();
            return count > 0; // Return true if user is registered
        }

        if (cursor != null) cursor.close();
        return false; // No user registered
    }

    private void getUserRegistation() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve the username value from the cursor
                String storename = cursor.getString(cursor.getColumnIndex("storename"));
                String registeredmobile = cursor.getString(cursor.getColumnIndex("registeredmobile"));
                String registeredemail = cursor.getString(cursor.getColumnIndex("registeredemail"));
                String staticip = cursor.getString(cursor.getColumnIndex("staticip"));
                String authdatabase = cursor.getString(cursor.getColumnIndex("authdatabase"));
                String authkey = cursor.getString(cursor.getColumnIndex("authkey"));
                String webservice_url = cursor.getString(cursor.getColumnIndex("webservice_url"));
                String webapplication_url = cursor.getString(cursor.getColumnIndex("imageurl_url"));
                String svayam_url_ip = cursor.getString(cursor.getColumnIndex("svayam_url_ip"));
                String svayam_image_for_htdocs_upload = cursor.getString(cursor.getColumnIndex("svayam_image_for_htdocs_upload"));

                master_userid= cursor.getString(cursor.getColumnIndex("userid"));

                cursor.close();

                Config.WEBSERVICE_URL = "http://" + staticip + "/" + webservice_url + "/";
                Config.IMAGEURL_URL = "http://" + staticip + "/" + webapplication_url + "/";
                Config.FILE_UPLOAD_URL = "http://" + staticip + ":4033/" + svayam_image_for_htdocs_upload + "/";
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions gracefully
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure the cursor is closed to prevent memory leaks
            }
        }
    }

    private void redirectToRegistration() {
        //Intent homeIntent = new Intent(getApplicationContext(),Registration.class);
        //homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(homeIntent);

        username = editTextUsername.getText().toString();
        password = editTextpassword.getText().toString();

        master_userid= editTextUsername.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_MOBILE, "");
        values.put(DatabaseHelper.COLUMN_USERID, username);

        //values.put(DatabaseHelper.COLUMN_NAME, "Shrikant Electronics");
        values.put(DatabaseHelper.COLUMN_NAME, "SHREE JAIN TRADERS");

        //values.put(DatabaseHelper.COLUMN_STATICIP, "182.70.113.254");
        // values.put(DatabaseHelper.COLUMN_STATICIP, "10.0.2.2");
        values.put(DatabaseHelper.COLUMN_STATICIP, "103.88.221.39");

        //values.put(DatabaseHelper.WEBSERVICE_URL, "Shrikant_WS");
        values.put(DatabaseHelper.WEBSERVICE_URL, "SJT_WS");

        //values.put(DatabaseHelper.IMAGEURL_URL, "Shrikant");
        values.put(DatabaseHelper.IMAGEURL_URL, "3033");

        //values.put(DatabaseHelper.SVAYAM_IMAGE_FOR_HTDOCS_UPLOAD, "ShrikantElectronics");
        values.put(DatabaseHelper.SVAYAM_IMAGE_FOR_HTDOCS_UPLOAD, "SvayamV11");

        long id = db.insert(DatabaseHelper.TABLE_USER, null, values);
        if (id != -1) {
            // Registration successful
            //navigatetoLoginActivity();
        }
    }

    public void loginUser() {

        if (!isUserRegistered()) {
            redirectToRegistration();
        }

        getUserRegistation();

        URL = Config.WEBSERVICE_URL;
        IMAGEURL = Config.IMAGEURL_URL;

        username = editTextUsername.getText().toString();
        password = editTextpassword.getText().toString();

        if (Utility.isNotNull(username) && Utility.isNotNull(password)) {
            // When username entered is Valid
            if (Utility.isNotNull(username)) {

                if (username.toString().equals(master_userid)) {

                    if (saveLoginCheckBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", username);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.apply();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.apply();
                    }

                    invokeWS();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You are not registered user..", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter valid username", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS() {
        prgDialog.show();

        // Get IMEI
        IMEI_Number_Holder = DeviceInfoUtils.getIMEI(getApplicationContext());

        // Prepare parameters dynamically
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", username);
        paramsMap.put("password", password);
        paramsMap.put("macwlan", IMEI_Number_Holder);
        paramsMap.put("AppType", "MGMT");

        // Call API using ApiHelper
        ApiHelper.post(URL + "Service1.asmx/doLoginCounterWith_IMEI", paramsMap, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();

                try {
                    JSONArray new_array = response.getJSONArray("usersdetails");

                    if (new_array.length() > 0) {
                        JSONObject jsonObject = new_array.getJSONObject(0);
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                        switch (jsonObject.getString("errmessage")) {
                            case "SUCCESS":
                                Toast.makeText(getApplicationContext(),
                                        "Welcome " + jsonObject.getString("employeename"),
                                        Toast.LENGTH_LONG).show();

                                // Set global variables
                                globalVariable.setSysuserno(jsonObject.getString("sysuserno"));
                                globalVariable.setsysemployeeno(jsonObject.getString("sysemployeeno"));
                                globalVariable.setcompanycd(jsonObject.getString("companycd"));
                                globalVariable.setreportingto(jsonObject.getString("reportingto"));
                                globalVariable.setreporting_fullname(jsonObject.getString("reporting_fullname"));
                                globalVariable.setcashsales_custcd(jsonObject.getString("cashsales_custcd"));
                                globalVariable.setcashsales_customername(jsonObject.getString("cashsales_customername"));
                                globalVariable.setcompanyname(jsonObject.getString("companyname"));
                                globalVariable.setparentcompanyname(jsonObject.getString("parentcompanyname"));
                                globalVariable.setemployeename(jsonObject.getString("employeename"));
                                globalVariable.setuserid(jsonObject.getString("userid"));
                                globalVariable.setgroupcode(jsonObject.getString("groupcode"));
                                globalVariable.setusertype(jsonObject.getString("usertype"));
                                globalVariable.setemployeeimagename(jsonObject.getString("employeeimagename"));
                                globalVariable.setAccountingStartDate(jsonObject.getString("vfromdate"));
                                globalVariable.setAccountingEndDate(jsonObject.getString("vtodt"));
                                globalVariable.setdefault_state(jsonObject.getString("default_state"));

                                final String sysuserno = globalVariable.getSysuserno();

                                // ✅ Get FCM token
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(task -> {
                                            if (!task.isSuccessful()) {
                                                Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }
                                            refreshedToken = task.getResult();
                                            Log.d("FCM", "Device token: " + refreshedToken);

                                            // Send token to server
                                            if (Utility.isNotNull(refreshedToken)) {
                                                globalVariable.setddevice_token( refreshedToken);

                                                Map<String, String> tokenParams = new HashMap<>();
                                                tokenParams.put("sysuserno", sysuserno);
                                                tokenParams.put("mobileapptype", "MGR");
                                                tokenParams.put("token", refreshedToken);
                                                invoke_Add_UpdateDeviceIDWS(tokenParams);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Token Not Generated", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                navigatetoHomeActivity();
                                TextView errorMsg = findViewById(R.id.login_error);
                                errorMsg.setText("");
                                break;

                            case "ATTENDANCE_IN_NOT_FOUND":
                                Toast.makeText(getApplicationContext(), "Attendance not found", Toast.LENGTH_LONG).show();
                                setGlobalVariables(globalVariable, jsonObject);
                                navigatetoAttendanceActivity();
                                break;

                            case "REGISTER_NEW_DEVICE":
                                Toast.makeText(getApplicationContext(), "Register new device", Toast.LENGTH_LONG).show();
                                setGlobalVariables(globalVariable, jsonObject);
                                TextView errorMsgDev = findViewById(R.id.login_error);
                                errorMsgDev.setText(IMEI_Number_Holder);
                                break;

                            default:
                                Toast.makeText(getApplicationContext(), jsonObject.getString("errmessage"), Toast.LENGTH_LONG).show();
                                break;
                        }

                    } else {
                        TextView errorMsg = findViewById(R.id.login_error);
                        errorMsg.setText("Invalid Login");
                        Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                prgDialog.hide();
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setGlobalVariables(GlobalClass globalVariable, JSONObject jsonObject) throws JSONException {
        globalVariable.setSysuserno(jsonObject.getString("sysuserno"));
        globalVariable.setsysemployeeno(jsonObject.getString("sysemployeeno"));
        globalVariable.setcompanycd(jsonObject.getString("companycd"));
        globalVariable.setcompanyname(jsonObject.getString("companyname"));
        globalVariable.setparentcompanyname(jsonObject.getString("parentcompanyname"));
        globalVariable.setemployeename(jsonObject.getString("employeename"));
        globalVariable.setreportingto(jsonObject.getString("reportingto"));
        globalVariable.setreporting_fullname(jsonObject.getString("reporting_fullname"));
        globalVariable.setuserid(jsonObject.getString("userid"));
        globalVariable.setgroupcode(jsonObject.getString("groupcode"));
        globalVariable.setusertype(jsonObject.getString("usertype"));
        globalVariable.setemployeeimagename(jsonObject.getString("employeeimagename"));
        globalVariable.setAccountingStartDate(jsonObject.getString("vfromdate"));
        globalVariable.setAccountingEndDate(jsonObject.getString("vtodt"));
        globalVariable.setdefault_state(jsonObject.getString("default_state"));
    }


    public void navigatetoHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoAttendanceActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),EmployeeAttendanceCreate.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
                /*try {
                    // this is so Linux hack
                    return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
                } catch (IOException ex) {
                    return null;
                }*/
    }

    public void invoke_Add_UpdateDeviceIDWS(Map<String, String> paramsMap) {
        ApiHelper.post(URL + "Service1.asmx/Add_Update_User_Device_ID", paramsMap, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray new_array = response.getJSONArray("walkincustomerdetails");

                    Toast.makeText(getApplicationContext(),
                            "Token registered successfully!",
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(),
                        errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void navigatetoShare(String errorMsgtext){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, errorMsgtext);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);

    }

}