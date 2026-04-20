package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class SalesmenGetFinanceDO extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText financecompanyET, approvalcodeET, loanamtET,noofmonthET,upfrontemiET,processingfeeET,cardchargesET,dbdamountET,totalupfrontET,cashbackET,disbursementamtET,emipermonthET;
    TextView loandtET;
    CheckBox dbdoncustomerCHK;

    EditText ModelNoET;
    EditText CustomerPhoneNoET;
    EditText alternamemobilenoET;
    EditText CustomerFirstNameET;
    EditText CustomerMiddleNameET;
    EditText CustomerLastNameET;
    EditText AddressLine1ET;
    EditText AddressLine2ET;
    EditText AddressLine3ET;
    EditText CITYET;
    EditText STATEET;
    EditText AreaET;
    EditText LandmarkET;
    EditText PinCodeET;
    EditText customerGSTINET;
    EditText CustomerEmailIDET;
    EditText CustomerPANET;
    EditText brandnameET;
    EditText modelnameET ;
    EditText quantityET ;
    EditText unitprice_include_taxET ;

    EditText delivery_address1ET ;
    EditText delivery_address2ET ;
    EditText delivery_address3ET ;
    EditText delivery_address4ET ;
    EditText delivery_pincodeET ;
    EditText delivery_gstnET;
    EditText delivery_gstnstateET ;

    int year, month, day;

    Button Button_financecompany, btnCreateFinance, Button_getfinancedo, Button_modelname,  Button_brandname;
    String fincompanycd = "0", companycd = "0", sysinvorderno = "0", sysloanno = "0", dbdoncustomer = "Y", sysbrandno = "0", sysmodelno = "0", brandname="", modelname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesgetfinancedo);



        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Button_financecompany:
                        navigatetoFinanceCompanyListActivity();
                        break;
                    case R.id.Button_brandname:
                        navigatetoBrandListActivity();
                        break;

                    case R.id.Button_modelname:
                        navigatetoModelListActivity();
                        break;
                }
            }
        };

        Button_financecompany = (Button) findViewById(R.id.Button_financecompany);
        Button_financecompany.setOnClickListener(handler);

        Button_brandname = (Button) findViewById(R.id.Button_brandname);
        Button_brandname.setOnClickListener(handler);

        Button_modelname = (Button) findViewById(R.id.Button_modelname);
        Button_modelname.setOnClickListener(handler);



        btnCreateFinance= (Button) findViewById(R.id.btnCreateFinance);
        btnCreateFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit_CreateFinance();
            }
        });

        Button_getfinancedo= (Button) findViewById(R.id.Button_getfinancedo);
       // Button_getfinancedo.setOnClickListener(handler);
        Button_getfinancedo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GetinanceDO();
            }
        });

        fincompanycd = "0";
        sysloanno= "0";

        financecompanyET = (EditText)findViewById(R.id.financecompany);
        dbdoncustomerCHK = (CheckBox) findViewById(R.id.dbdoncustomer);
        approvalcodeET = (EditText)findViewById(R.id.approvalcode);
        loanamtET =  (EditText)findViewById(R.id.loanamt);
        noofmonthET = (EditText)findViewById(R.id.noofmonth);
        upfrontemiET = (EditText)findViewById(R.id.upfrontemi);
        emipermonthET = (EditText)findViewById(R.id.emipermonth);
        processingfeeET = (EditText)findViewById(R.id.processingfee);
        cardchargesET = (EditText)findViewById(R.id.cardcharges);
        dbdamountET = (EditText)findViewById(R.id.dbdamount);
        totalupfrontET = (EditText)findViewById(R.id.totalupfront);
        cashbackET = (EditText)findViewById(R.id.cashback);
        disbursementamtET = (EditText)findViewById(R.id.disbursementamt);
        loandtET = (TextView)findViewById(R.id.loandt);

        ModelNoET = (EditText)findViewById(R.id.ModelNo);
        CustomerPhoneNoET = (EditText)findViewById(R.id.CustomerPhoneNo);
        alternamemobilenoET = (EditText)findViewById(R.id.alternamemobileno);
        CustomerFirstNameET= (EditText)findViewById(R.id.CustomerFirstName);
        CustomerMiddleNameET= (EditText)findViewById(R.id.CustomerMiddleName);
        CustomerLastNameET= (EditText)findViewById(R.id. CustomerLastName);
        AddressLine1ET= (EditText)findViewById(R.id.AddressLine1);
        AddressLine2ET= (EditText)findViewById(R.id.AddressLine2);
        AddressLine3ET= (EditText)findViewById(R.id.AddressLine3);
        CITYET= (EditText)findViewById(R.id.CITY);
        STATEET= (EditText)findViewById(R.id.STATE);
        AreaET= (EditText)findViewById(R.id.Area);
        LandmarkET= (EditText)findViewById(R.id.Landmark);
        PinCodeET= (EditText)findViewById(R.id.PinCode);
        customerGSTINET= (EditText)findViewById(R.id.customerGSTIN);
        CustomerEmailIDET= (EditText)findViewById(R.id.CustomerEmailID);
        CustomerPANET= (EditText)findViewById(R.id.CustomerPAN);

        delivery_address1ET = (EditText)findViewById(R.id.delivery_address1);
        delivery_address2ET = (EditText)findViewById(R.id.delivery_address2);
        delivery_address3ET = (EditText)findViewById(R.id.delivery_address3);
        delivery_address4ET = (EditText)findViewById(R.id.delivery_address4);
        delivery_pincodeET = (EditText)findViewById(R.id.delivery_pincode);
        delivery_gstnET = (EditText)findViewById(R.id.delivery_gstn);
        delivery_gstnstateET = (EditText)findViewById(R.id.delivery_statename);

        brandnameET = (EditText)findViewById(R.id.brandname);
        modelnameET = (EditText)findViewById(R.id.modelname);
        quantityET = (EditText)findViewById(R.id.quantity);
        unitprice_include_taxET = (EditText)findViewById(R.id.unitprice_include_tax);

        Intent i = getIntent();
        if( i.getStringExtra("sysinvorderno") != null)
        {
                sysinvorderno = i.getStringExtra("sysinvorderno");
            Invoke_SalesOrderDetails_Finance(sysloanno, sysinvorderno);

        }
        else
        {
            sysinvorderno = "0";
        }

        // Find Error Msg Text View control by ID
        //errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        loandtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        approvalcodeET.setText("");
       prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        loanamtET.addTextChangedListener(disbursementWatcher);
        noofmonthET.addTextChangedListener(disbursementWatcher);
        upfrontemiET.addTextChangedListener(disbursementWatcher);
        processingfeeET.addTextChangedListener(disbursementWatcher);
        cardchargesET.addTextChangedListener(disbursementWatcher);
        dbdamountET.addTextChangedListener(disbursementWatcher);
        cashbackET.addTextChangedListener(disbursementWatcher);
    }


    TextWatcher disbursementWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            calculateDisbursementAmount();
        }
    };



    public void Invoke_SalesOrderDetails_Finance(String dsysloanno, String dsysinvorderno) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysloanno", "0" +  dsysloanno);
            paramsMap.put("sysinvorderno", "0" +  dsysinvorderno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSalesmen_SalesOrderDetails_Finance", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("salesorderfinancedetails");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sysloanno = jsonObject.getString("sysloanno");
                        sysinvorderno = jsonObject.getString("sysinvorderno");
                        fincompanycd = jsonObject.getString("fincompanycd");
                        companycd = jsonObject.getString("companycd");
                        financecompanyET.setText(jsonObject.getString("financecompany"));
                        if (jsonObject.getString("dbdoncustomer").equals("Y"))
                        {
                            dbdoncustomerCHK.setChecked(true);
                        }
                        else
                        {
                            dbdoncustomerCHK.setChecked(false);
                        }
                        approvalcodeET.setText(jsonObject.getString("approvalcode"));
                        loanamtET.setText(jsonObject.getString("loanamt"));
                        noofmonthET.setText(jsonObject.getString("noofmonth"));
                        upfrontemiET.setText(jsonObject.getString("upfrontemi"));
                        emipermonthET.setText(jsonObject.getString("emipermonth"));
                        processingfeeET.setText(jsonObject.getString("processingfee"));
                        cardchargesET.setText(jsonObject.getString("cardcharges"));
                        dbdamountET.setText(jsonObject.getString("dbdamount"));
                        totalupfrontET.setText(jsonObject.getString("totalupfront"));
                        cashbackET.setText(jsonObject.getString("cashback"));
                        disbursementamtET.setText(jsonObject.getString("disbursementamt"));
                        loandtET.setText(jsonObject.getString("vloandt"));

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysbrandno = bundle.getString("sysbrandno");
                brandnameET.setText(bundle.getString("description"));
            }
        }

        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysmodelno = bundle.getString("sysmodelno");
                modelnameET.setText(bundle.getString("modelcode"));
            }
        }

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                fincompanycd = bundle.getString("fincompanycd");
                financecompanyET.setText(bundle.getString("financename"));
            }
        }

    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(SalesmenGetFinanceDO.this,StockVerificationModel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysbrandno",sysbrandno);
        startActivityForResult(homeIntent,4);
    }

    public void navigatetoBrandListActivity(){
        Intent homeIntent = new Intent(SalesmenGetFinanceDO.this,StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,3);
    }

    public void navigatetoFinanceCompanyListActivity(){
        Intent homeIntent = new Intent(SalesmenGetFinanceDO.this,StockVerificationFinance_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }


    public void GetinanceDO(){

        String financecompany = financecompanyET.getText().toString();
        String approvalcode = approvalcodeET.getText().toString();


        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(approvalcode) && Utility.isNotNull(fincompanycd) ) {
            // When Email entered is Valid
            if (Utility.isNotNull(financecompany)) {
                // Put Http parameter username with value of Email Edit View control

                paramsMap.put("fincompanycd", "0"+fincompanycd);
                paramsMap.put("invorderno", "");
                paramsMap.put("approvalcode", ""+approvalcode.toUpperCase());

                invokeFinanceDODetailsWS(paramsMap);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter required product", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }


    public void invokeFinanceDODetailsWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GetDO", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    JSONObject obj = response;
                    JSONObject doDetails = obj.getJSONObject("doDetails");

                    String tenureStr = doDetails.getString("Tenure");
                    String netTenureStr = doDetails.getString("NetTenure");

                    int Tenure = Integer.parseInt(tenureStr);
                    int NetTenure = Integer.parseInt(netTenureStr);
                    int upfrontTenure = Tenure - NetTenure;

                    approvalcodeET.setText(doDetails.getString("DONumber"));
                    loandtET.setText(doDetails.getString("CreatedOn"));

                    dbdoncustomerCHK.setChecked(true);

                    Tenure = Integer.parseInt(Utility.getSafeString(doDetails, "Tenure"));
                    NetTenure = Integer.parseInt(Utility.getSafeString(doDetails, "NetTenure"));
                    upfrontTenure = Tenure - NetTenure;

                    String createdOnRaw = Utility.getSafeString(doDetails, "CreatedOn"); // e.g., "2025-05-06 17:30:01"
                    String formattedDate = "";

                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        Date date = inputFormat.parse(createdOnRaw);
                        formattedDate = outputFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace(); // Optional: Log error
                        formattedDate = "";  // Or fallback value
                    }

                    loandtET.setText(formattedDate);
                    approvalcodeET.setText(Utility.getSafeString(doDetails, "DONumber"));
                    //loandtET.setText(Utility.getSafeString(doDetails, "CreatedOn"));
                    loanamtET.setText(Utility.getSafeString(doDetails, "GrossLoanAmount"));
                    noofmonthET.setText(Utility.getSafeString(doDetails, "Tenure"));
                    upfrontemiET.setText(String.valueOf(upfrontTenure));
                    emipermonthET.setText(Utility.getSafeString(doDetails, "TotalEMI"));
                    processingfeeET.setText(Utility.getSafeString(doDetails, "ProcessingFees"));
                    cardchargesET.setText(Utility.getSafeString(doDetails, "CoBrandCardCharges"));
                    dbdamountET.setText(Utility.getSafeString(doDetails, "Subvention"));
                    totalupfrontET.setText(Utility.getSafeString(doDetails, "CustomerDownPayment"));
                    cashbackET.setText(Utility.getSafeString(doDetails, "Field5"));
                    disbursementamtET.setText(Utility.getSafeString(doDetails, "NetDisbursement"));

                    ModelNoET.setText(Utility.getSafeString(doDetails, "ModelNo"));
                    CustomerPhoneNoET.setText(Utility.getSafeString(doDetails, "CustomerPhoneNo"));
                    alternamemobilenoET.setText("");
                    CustomerFirstNameET.setText(Utility.getSafeString(doDetails, "CustomerFirstName"));
                    CustomerMiddleNameET.setText(Utility.getSafeString(doDetails, "CustomerMiddleName"));
                    CustomerLastNameET.setText(Utility.getSafeString(doDetails, "CustomerLastName"));
                    AddressLine1ET.setText(Utility.getSafeString(doDetails, "AddressLine1"));
                    AddressLine2ET.setText(Utility.getSafeString(doDetails, "AddressLine2"));
                    AddressLine3ET.setText(Utility.getSafeString(doDetails, "AddressLine3"));
                    CITYET.setText(Utility.getSafeString(doDetails, "CITY"));
                    STATEET.setText(Utility.getSafeString(doDetails, "STATE"));
                    AreaET.setText(Utility.getSafeString(doDetails, "Area"));
                    LandmarkET.setText(Utility.getSafeString(doDetails, "Landmark"));
                    PinCodeET.setText(Utility.getSafeString(doDetails, "PinCode"));
                    customerGSTINET.setText(Utility.getSafeString(doDetails, "customerGSTIN"));
                    CustomerEmailIDET.setText(Utility.getSafeString(doDetails, "CustomerEmailID"));
                    CustomerPANET.setText(Utility.getSafeString(doDetails, "CustomerPAN"));

                    delivery_address1ET.setText(Utility.getSafeString(doDetails, "AddressLine1"));
                    delivery_address2ET.setText(Utility.getSafeString(doDetails, "AddressLine2"));
                    delivery_address3ET.setText(Utility.getSafeString(doDetails, "AddressLine3") + " " + Utility.getSafeString(doDetails, "CITY") + " " + Utility.getSafeString(doDetails, "STATE"));
                    delivery_address4ET.setText(Utility.getSafeString(doDetails, "Area") + " " + Utility.getSafeString(doDetails, "Landmark"));
                    delivery_pincodeET.setText(Utility.getSafeString(doDetails, "PinCode"));
                    delivery_gstnET.setText(Utility.getSafeString(doDetails, "customerGSTIN"));

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

    public void Submit_CreateFinance(){


        String financecompany = financecompanyET.getText().toString();

        if(dbdoncustomerCHK.isChecked())
        {
             dbdoncustomer = "Y";
        }
        else
        {
            dbdoncustomer = "N";
        };

        String approvalcode = approvalcodeET.getText().toString();
        String loanamt = loanamtET.getText().toString();
        String noofmonth = noofmonthET.getText().toString();
        String upfrontemi = upfrontemiET.getText().toString();
        String processingfee = processingfeeET.getText().toString();
        String cardcharges = cardchargesET.getText().toString();
        String dbdamount = dbdamountET.getText().toString();
        String totalupfront = totalupfrontET.getText().toString();
        String cashback = cashbackET.getText().toString();
        String disbursementamt = disbursementamtET.getText().toString();
        String loandt = loandtET.getText().toString();
        String ModelNo = ModelNoET.getText().toString();
        String CustomerPhoneNo = CustomerPhoneNoET.getText().toString();
        String alternamemobileno = alternamemobilenoET.getText().toString();
        String CustomerFirstName = CustomerFirstNameET.getText().toString();
        String CustomerMiddleName = CustomerMiddleNameET.getText().toString();
        String CustomerLastName =  CustomerLastNameET.getText().toString();
        String AddressLine1 =  AddressLine1ET.getText().toString();
        String AddressLine2 =  AddressLine2ET.getText().toString();
        String AddressLine3 =  AddressLine3ET.getText().toString();
        String CITY =  CITYET.getText().toString();
        String STATE =  STATEET.getText().toString();
        String Area =  AreaET.getText().toString();
        String Landmark =  LandmarkET.getText().toString();
        String PinCode =  PinCodeET.getText().toString();
        String customerGSTIN =  customerGSTINET.getText().toString();
        String CustomerEmailID =  CustomerEmailIDET.getText().toString();
        String CustomerPAN =  CustomerPANET.getText().toString();

        String delivery_address1 = delivery_address1ET.getText().toString();
        String delivery_address2 = delivery_address2ET.getText().toString();
        String delivery_address3 = delivery_address3ET.getText().toString();
        String delivery_address4 = delivery_address4ET.getText().toString();
        String delivery_pincode = delivery_pincodeET.getText().toString();
        String delivery_gstn = delivery_gstnET.getText().toString();

        String quantity =  quantityET.getText().toString();
        String unitprice_include_tax =  unitprice_include_taxET.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();
        final String userid  = globalVariable.getuserid();
        final String sysemployeeno = globalVariable.getsysemployeeno();

        //   Toast.makeText(getApplicationContext(), consignor_sysmrno, Toast.LENGTH_LONG).show();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysinvorderno) && Utility.isNotNull(fincompanycd) ) {
            // When Email entered is Valid
            if (Utility.isNotNull(financecompany)) {
                // Put Http parameter username with value of Email Edit View control
                paramsMap.put("sysloanno", "0"+sysloanno);
                paramsMap.put("fincompanycd", "0"+fincompanycd);
                paramsMap.put("companycd", "0"+companycd);
                paramsMap.put("sysmrno", "0"+sysemployeeno);
                paramsMap.put("dbdoncustomer", ""+dbdoncustomer.toUpperCase());
                paramsMap.put("approvalcode", ""+approvalcode.toUpperCase());
                paramsMap.put("loanamt", "0"+loanamt);
                paramsMap.put("noofmonth", "0"+noofmonth);
                paramsMap.put("upfrontemi", "0"+upfrontemi);
                paramsMap.put("processingfee", "0"+processingfee);
                paramsMap.put("cardcharges", "0"+cardcharges);
                paramsMap.put("dbdamount", "0"+dbdamount);
                paramsMap.put("totalupfront", "0"+totalupfront);
                paramsMap.put("cashback", "0"+cashback);
                paramsMap.put("disbursementamt", "0"+disbursementamt);
                paramsMap.put("loandt", ""+loandt);
                paramsMap.put("ModelNo", ""+ModelNo);
                paramsMap.put("CustomerPhoneNo", ""+CustomerPhoneNo);
                paramsMap.put("alternamemobileno", ""+alternamemobileno);
                paramsMap.put("CustomerFirstName", ""+CustomerFirstName);
                paramsMap.put("CustomerMiddleName", ""+CustomerMiddleName);
                paramsMap.put("CustomerLastName", ""+CustomerLastName);
                paramsMap.put("AddressLine1", ""+AddressLine1);
                paramsMap.put("AddressLine2", ""+AddressLine2);
                paramsMap.put("AddressLine3", ""+AddressLine3);
                paramsMap.put("CITY", ""+CITY);
                paramsMap.put("STATE", ""+STATE);
                paramsMap.put("Area", ""+Area);
                paramsMap.put("Landmark", ""+Landmark);
                paramsMap.put("PinCode", ""+PinCode);
                paramsMap.put("customerGSTIN", ""+customerGSTIN);
                paramsMap.put("CustomerEmailID", ""+CustomerEmailID);
                paramsMap.put("CustomerPAN", ""+CustomerPAN);

                paramsMap.put("delivery_address1", ""+delivery_address1);
                paramsMap.put("delivery_address2", ""+delivery_address2);
                paramsMap.put("delivery_address3", ""+delivery_address3);
                paramsMap.put("delivery_address4", ""+delivery_address4);
                paramsMap.put("delivery_pincode", ""+delivery_pincode);
                paramsMap.put("delivery_gstn", ""+delivery_gstn);

                paramsMap.put("sysmodelno", "0" +sysmodelno);
                paramsMap.put("quantity", "0" +quantity);
                paramsMap.put("unitprice_include_tax", "0" +unitprice_include_tax);
                paramsMap.put("userid", ""+userid);

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
        ApiHelper.post(URL + "Service1.asmx/SalesOrderReceipt_Create_Customer_Order_Finance", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object

                    JSONObject header_obj_header = response;
                    JSONArray header_array = header_obj_header.getJSONArray("invoiceorderdetails");
                    JSONObject header_jsonObject = header_array.getJSONObject(0);

                    if (Integer.valueOf(header_jsonObject.getString("resultcode")) > 0) {

                        Toast.makeText(getApplicationContext(), header_jsonObject.getString("resultmessge"), Toast.LENGTH_LONG).show();

                        String sysinvorderno = header_jsonObject.getString("sysinvorderno");
                        Intent intent = new Intent(SalesmenGetFinanceDO.this, Salesmen_SalesOrder.class);
                        intent.putExtra("sysinvorderno", sysinvorderno);
                        intent.putExtra("custname", header_jsonObject.getString("custname"));
                        intent.putExtra("vinvoicedt", header_jsonObject.getString("vinvoicedt"));
                        intent.putExtra("netinvoiceamt", header_jsonObject.getString("netinvoiceamt"));
                        intent.putExtra("invorderno", header_jsonObject.getString("invorderno"));
                        intent.putExtra("sysproductno", header_jsonObject.getString("sysproductno"));
                        intent.putExtra("walkin_custcd", header_jsonObject.getString("walkin_custcd"));
                        intent.putExtra("customer_custcd", header_jsonObject.getString("customer_custcd"));
                        intent.putExtra("companycd", header_jsonObject.getString("companycd"));
                        intent.putExtra("companyname", header_jsonObject.getString("companyname"));
                        intent.putExtra("syslocno", header_jsonObject.getString("syslocno"));
                        intent.putExtra("sysaccledgerno", header_jsonObject.getString("sysaccledgerno"));

                        startActivity(intent);
                        finish();
                    };

                    if (Integer.valueOf(header_jsonObject.getString("resultcode")) == 0)
                    {
                        Toast.makeText(getApplicationContext(), header_jsonObject.getString("resultmessge"), Toast.LENGTH_LONG).show();
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
        sysloanno = "0";
        sysinvorderno = "0";
        fincompanycd = "0";
        companycd = "0";
        financecompanyET.setText("");
        approvalcodeET.setText("");
        loanamtET.setText("");
        noofmonthET.setText("");
        upfrontemiET.setText("");
        emipermonthET.setText("");
        processingfeeET.setText("");
        cardchargesET.setText("");
        dbdamountET.setText("");
        totalupfrontET.setText("");
        cashbackET.setText("");
        disbursementamtET.setText("");
        loandtET.setText("");
    }

    public void navigatetoFollowupActivity(){
        Intent homeIntent = new Intent(SalesmenGetFinanceDO.this,CrmActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("status","PENDING");
        finish();
        startActivity(homeIntent);
    }

    @SuppressWarnings("deprecation")
    public void setloandt(View view) {
        showDialog(997);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub


        if (id == 997) {
            return new DatePickerDialog(this,
                    NextfollowupDateListener, year, month, day);
        }
        return null;
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
        loandtET = (TextView) findViewById(R.id.loandt);
        loandtET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(SalesmenGetFinanceDO.this);
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
        TextView textView = new TextView(SalesmenGetFinanceDO.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(SalesmenGetFinanceDO.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    public void calculateDisbursementAmount() {
        double loanAmt = parseDoubleOrZero(loanamtET.getText().toString());
        double dbdAmt = parseDoubleOrZero(dbdamountET.getText().toString());
        double cardCharges = parseDoubleOrZero(cardchargesET.getText().toString());
        double processingFee = parseDoubleOrZero(processingfeeET.getText().toString());
        double noofmonth = parseDoubleOrZero(noofmonthET.getText().toString());
        double upfrontemi = parseDoubleOrZero(upfrontemiET.getText().toString());
        double cashback = parseDoubleOrZero(cashbackET.getText().toString());

        double emipermonth = loanAmt/noofmonth;
        double totalupfront = emipermonth*upfrontemi;
        double disbursementAmt = loanAmt - dbdAmt - cardCharges - processingFee - totalupfront - cashback;

        emipermonthET.setText(String.format("%.2f", emipermonth));
        totalupfrontET.setText(String.format("%.2f", totalupfront));
        disbursementamtET.setText(String.format("%.2f", disbursementAmt));
    }

    // Helper to safely parse EditText inputs
    private double parseDoubleOrZero(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }


}