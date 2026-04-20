package com.shrikantelectronics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class CreateCustomer extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText consignor_mobilenoET ;
    EditText consignor_nameET ;
    EditText consignor_address1ET ;
    EditText consignor_address2ET ;
    EditText consignor_pincodeET ;
    EditText consignor_emailET ;
    EditText consignor_inquiredproductET;
    EditText consignor_priceofferedET;
    EditText remarksET;

    String custcd;

    Button pickbirthdate;
    Button pickAniversoryDate;

    TextView pickNextFollowupDate;
    EditText txtNextFollowupTime;

    int year, month, day;
    String sbirthdate;
    String sAniversoryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcustomer);

        consignor_nameET = (EditText)findViewById(R.id.name);
        consignor_mobilenoET = (EditText)findViewById(R.id.mobileno);
        consignor_mobilenoET.addTextChangedListener(new GenericTextWatcher_Mobile(consignor_mobilenoET));
        consignor_address1ET = (EditText)findViewById(R.id.address1);
        consignor_address2ET = (EditText)findViewById(R.id.address2);
        consignor_pincodeET = (EditText)findViewById(R.id.pincode);
        consignor_emailET = (EditText)findViewById(R.id.email);
        consignor_inquiredproductET = (EditText)findViewById(R.id.inquiredproduct);
        consignor_priceofferedET = (EditText)findViewById(R.id.priceoffered);

        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        txtNextFollowupTime = (EditText)findViewById(R.id.NextFollowupTime);


        remarksET = (EditText)findViewById(R.id.remarks);

        Intent i = getIntent();

        custcd = i.getStringExtra("custcd");

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickbirthdate = (Button) findViewById(R.id.pickbirthdate);
        pickAniversoryDate = (Button) findViewById(R.id.pickAniversoryDate);

        pickbirthdate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        pickAniversoryDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }


    private class GenericTextWatcher_Mobile implements TextWatcher {

        private View view;
        private GenericTextWatcher_Mobile(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {


            Editable editableValue1 = consignor_mobilenoET.getText();
            if (editableValue1.length() == 10) {
                Invoke_CustomerDetails(editableValue1);
            }
        }
    }

    public void Invoke_CustomerDetails(final Editable editableValue1) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("mobileno1", "" +  editableValue1 );

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerDetailsByMobileNumber", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        consignor_nameET.setText(jsonObject.getString("name"));
                        consignor_mobilenoET.setText(jsonObject.getString("contactpersonmobile"));
                        consignor_address1ET.setText(jsonObject.getString("address1"));
                        consignor_address2ET.setText(jsonObject.getString("address2"));
                        consignor_pincodeET.setText(jsonObject.getString("pincode"));
                        consignor_emailET.setText(jsonObject.getString("emailid"));

                    } catch (JSONException e) {
                       // Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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

    public void onClick_submit(View view) {
        // Get Email Edit View Value

        String consignor_name = consignor_nameET.getText().toString();
        String consignor_mobileno = consignor_mobilenoET.getText().toString();
        String consignor_address1 = consignor_address1ET.getText().toString();
        String consignor_address2 = consignor_address2ET.getText().toString();
        String consignor_pincode = consignor_pincodeET.getText().toString();
        String consignor_email = consignor_emailET.getText().toString();
        String consignor_inquiredproduct = consignor_inquiredproductET.getText().toString();
        String consignor_priceoffered= consignor_priceofferedET.getText().toString();
        String remarks= remarksET.getText().toString();

        String followupdate = pickNextFollowupDate.getText().toString();
        String followuptime = txtNextFollowupTime.getText().toString();


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String consignor_companycd  = globalVariable.getcompanycd();
        final String consignor_sysmrno  = globalVariable.getsysemployeeno();

        //final String sysemployeeno  = globalVariable.getsysemployeeno();

       //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(consignor_mobileno) && Utility.isNotNull(consignor_name) ) {
            // When Email entered is Valid
            if (Utility.isNotNull(consignor_inquiredproduct)) {
                // Put Http parameter username with value of Email Edit View control

                paramsMap.put("consignor_mobileno", ""+consignor_mobileno);
                paramsMap.put("consignor_name",""+ consignor_name);
                paramsMap.put("consignor_address1", ""+consignor_address1);
                paramsMap.put("consignor_address2", ""+consignor_address2);
                paramsMap.put("consignor_pincode", ""+consignor_pincode);
                paramsMap.put("consignor_email", ""+consignor_email);
                paramsMap.put("sysmrno", "0"+consignor_sysmrno);
                paramsMap.put("companycd", "0"+consignor_companycd);
                paramsMap.put("inquiredproduct", ""+consignor_inquiredproduct);
                paramsMap.put("priceoffered", "0"+consignor_priceoffered);
                paramsMap.put("birthdate", ""+pickbirthdate.getText());
                paramsMap.put("aniversorydate",""+ pickAniversoryDate.getText());
                paramsMap.put("nextfollowupdate", "" + followupdate);
                paramsMap.put("nextfollowuptime", "" + followuptime);
                paramsMap.put("remarks", remarks);

               invokeWS(paramsMap);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter required product", Toast.LENGTH_LONG).show();
            }
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
        ApiHelper.post(URL + "Service1.asmx/AddWalkinCustomer", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    // When the JSON response has status boolean value assigned with true

                    //Toast.makeText(getApplicationContext(), "Customer is successfully added!", Toast.LENGTH_LONG).show();

                    if(obj.getBoolean("status")){
                        // Set Default Values for Edit View controls
                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Walk-In is successfully added!", Toast.LENGTH_LONG).show();
                        navigatetoFollowupActivity();
                    }
                    // Else display error message
                    else{
                        errorMsg.setText(obj.getString("error_msg"));
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

    public void setDefaultValues(){
        consignor_mobilenoET.setText("");
        consignor_nameET.setText("");
        consignor_address1ET.setText("");
        consignor_address2ET.setText("");
        consignor_pincodeET.setText("");
        consignor_emailET.setText("");
        consignor_inquiredproductET.setText("");
        consignor_priceofferedET.setText("");
    }

    public void navigatetoFollowupActivity(){
        Intent homeIntent = new Intent(CreateCustomer.this,CrmActivityEmployee.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("status","PENDING");
        finish();
        startActivity(homeIntent);
    }

    @SuppressWarnings("deprecation")
    public void setBirthDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @SuppressWarnings("deprecation")
    public void setAniversoryDate(View view) {
        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @SuppressWarnings("deprecation")
    public void setPickNextFollowupDate(View view) {
        showDialog(997);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    birthdateListener, year, month, day);
        }

        if (id == 998) {
            return new DatePickerDialog(this,
                    AniversoryDateListener, year, month, day);
        }

        if (id == 997) {
            return new DatePickerDialog(this,
                    NextfollowupDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener birthdateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showbirthdate(arg1, arg2, arg3);
                }
            };

    private void showbirthdate(int year, int month, int day) {
        pickbirthdate = (Button) findViewById(R.id.pickbirthdate);
        pickbirthdate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
      //  invokeWS_Customer_Ledger(custcd);
    }

    private DatePickerDialog.OnDateSetListener AniversoryDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showAniversoryDate(arg1, arg2, arg3);
                }
            };

    private void showAniversoryDate(int year, int month, int day) {
        pickAniversoryDate = (Button) findViewById(R.id.pickAniversoryDate);
        pickAniversoryDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
       // invokeWS_Customer_Ledger(custcd);
    }



    private DatePickerDialog.OnDateSetListener NextfollowupDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showNextfollowupDate(arg1, arg2, arg3);
                }
            };

    private void showNextfollowupDate(int year, int month, int day) {
        pickNextFollowupDate = (TextView) findViewById(R.id.pickAniversoryDate);
        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }
}