package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


public class Registration extends AppCompatActivity {
     private DatabaseHelper dbHelper;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    Button btnregister;
    private EditText editTextUsername,editTextPassword,editTextregisteredmobile,editTextsvayamauthkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextregisteredmobile = (EditText) findViewById(R.id.registeredmobile);

        editTextsvayamauthkey = (EditText) findViewById(R.id.svayamauthkey);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        btnregister = (Button) findViewById(R.id.btnregister);
                btnregister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        dbHelper = new DatabaseHelper(this);
    }

    private void registerUser() {

        ValidateMobileRegisterUser();

    }

    public void navigatetoLoginActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),login.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    public void ValidateMobileRegisterUser(){
        // Show Progress Dialog
        prgDialog.show();

        final String userid = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String registeredmobile = editTextregisteredmobile.getText().toString();
        final String svayamauthkey = editTextsvayamauthkey.getText().toString();

        final String baseURL = "http://184.168.121.181:9099/commonfunctions/ValidateSvayamMobileUserCredentials";

        String url = baseURL + "?userid=" + userid +
                "&password=" + password +
                "&registeredmobile=" + registeredmobile +
                "&svayamauthkey=" + svayamauthkey +
                "&svayam_clientid=" + GlobalClass.SVAYAM_CLIENTID +
                "&svayam_subscriptionid=" + GlobalClass.SVAYAM_SUBSCRIPTIONID;

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("password", password);
        paramsMap.put("registeredmobile", registeredmobile);
        paramsMap.put("svayamauthkey",svayamauthkey);
        paramsMap.put("svayam_clientid",GlobalClass.SVAYAM_CLIENTID);
        paramsMap.put("svayam_subscriptionid",GlobalClass.SVAYAM_SUBSCRIPTIONID);

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(baseURL, paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    // Get the first object from the array
                    JSONObject obj = jsonArray.getJSONObject(0);

                    //
                    // When the JSON response has status boolean value assigned with true
                    //
                    if(obj.length()>0){
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.put(DatabaseHelper.COLUMN_MOBILE, registeredmobile);
                        values.put(DatabaseHelper.COLUMN_USERID, userid);
                        values.put(DatabaseHelper.COLUMN_NAME, obj.getString("company_name"));
                        values.put(DatabaseHelper.COLUMN_STATICIP, obj.getString("svayam_webservice_server_ip"));
                        values.put(DatabaseHelper.WEBSERVICE_URL, obj.getString("svayam_webservice_url"));
                        values.put(DatabaseHelper.IMAGEURL_URL, obj.getString("svayam_imageurl_url"));

                        long id = db.insert(DatabaseHelper.TABLE_USER, null, values);
                        if (id != -1) {
                            // Registration successful
                            navigatetoLoginActivity();
                        }

                    }
                    // Else display error message
                    else
                    {
                        prgDialog.hide();
                        errorMsg.setText("Invalid Login");
                        Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
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