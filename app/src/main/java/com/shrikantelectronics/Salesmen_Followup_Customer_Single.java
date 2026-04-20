package com.shrikantelectronics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;

public class Salesmen_Followup_Customer_Single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    Button Update_new_walkin;
    Button btnCapturePicture;
    EditText activitytypedescTV;
    EditText folupconversationET;
    EditText txtNextFollowupTime;
    String activitytype;
    String activitytypedesc;
    String address1;
    String companycd;
    String custname ="0";
    String custname1;
    String customer_custcd;
    String emailid1;
    String inquiredproduct;
    String locationname;
    String mobile1;
    String netinvoiceamt1;
    String outstanding1;
    String syscustactno;
    String sysfollowupno;
    String sysinvorderno ="0" ;
    String todate;
    String vinvoicedt1;
    String walkin_custcd;

    String syslocno="0";
    String sysaccledgerno="0";

    TableLayout tablefollowup_history;
    TableLayout tablesalescustomerorder;
    TextView errorMsg;
    TextView header_followuphistory;
    TextView pickNextFollowupDate;
    TextView txtaddress;
    TextView txtcustcd;
    TextView txtcustname;
    TextView txtemailid;
    TextView txtinquiredproduct;
    TextView txtmobile;
    TextView txtoutstanding;
    TextView txttelephone;
    int year, month, day;
    ProgressDialog prgDialog;
    String foldername ="";
    String activitystatus;
    EditText activityrefnoET;
    EditText modenameET ;
    EditText purposenameET ;

    Button Button_modename, Button_purposename;
    String mode="", purpose="";
    ImageView call_mobile, call_telephone;

    ListViewAdapter_Customer_Product adapter;
    private ListView lv_customer_Product ;
    ArrayList<Customer_Product> arraylist = new ArrayList<Customer_Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesmen_followup_customer_single);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        syscustactno= i.getStringExtra("syscustactno");
        walkin_custcd = i.getStringExtra("walkin_custcd");
        customer_custcd = i.getStringExtra("customer_custcd");
        custname1= i.getStringExtra("custname");
        companycd= i.getStringExtra("companycd");
        locationname= i.getStringExtra("locationname");

        foldername= i.getStringExtra("foldername");
        activitystatus= i.getStringExtra("activitystatus");
        syslocno= i.getStringExtra("syslocno");
        sysaccledgerno= i.getStringExtra("sysaccledgerno");

        sysfollowupno = "";
        activitytypedesc = "Sales Leads";
        activitytype ="04";

       // activitytypedescTV = (EditText) findViewById(R.id.activitytypedesc);

        // Locate the TextViews in singleitemview.xml
        txtcustname = (TextView) findViewById(R.id.name);
        txtmobile = (TextView) findViewById(R.id.mobile);
        txttelephone= (TextView) findViewById(R.id.telephone);
        txtemailid = (TextView) findViewById(R.id.emailid);
        txtoutstanding = (TextView) findViewById(R.id.outstanding);
        txtaddress = (TextView) findViewById(R.id.address);
        txtinquiredproduct = (TextView) findViewById(R.id.inquiredproduct);
        activityrefnoET= (EditText)findViewById(R.id.activityrefno);
        modenameET = (EditText)findViewById(R.id.modename);
        purposenameET = (EditText)findViewById(R.id.purposename);

        txtNextFollowupTime= (EditText) findViewById(R.id.NextFollowupTime);
        folupconversationET= (EditText)findViewById(R.id.folupconversation);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        pickNextFollowupDate = (TextView) findViewById(R.id.pickNextFollowupDate);
        pickNextFollowupDate.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        Update_new_walkin= (Button) findViewById(R.id.Update_new_walkin);
        Update_new_walkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigatetoCreateCustomeerActivity();
            }
        });

        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                //captureImage();
                navigatetoImageCaptureActivity();
            }
        });

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {



                    case R.id.Button_modename:
                        navigatetomodeListActivity();
                        break;

                    case R.id.Button_purposename:
                        navigatetopurposenameListActivity();
                        break;
                }
            }
        };

        Button_purposename = (Button) findViewById(R.id.Button_purposename);
        Button_purposename.setOnClickListener(handler);

        Button_modename = (Button) findViewById(R.id.Button_modename);
        Button_modename.setOnClickListener(handler);

        txtmobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mobile;
                mobile= txtmobile.getText().toString();
                CallDialer(mobile);
            }
        });

        txttelephone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mobile;
                mobile= txttelephone.getText().toString();
                CallDialer(mobile);
            }
        });


        call_mobile = (ImageView) findViewById(R.id.call_mobile);
        call_telephone = (ImageView) findViewById(R.id.call_telephone);

        call_mobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mobile;
                mobile= txtmobile.getText().toString();
                CallDialer(mobile);
            }
        });

        call_telephone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mobile;
                mobile= txttelephone.getText().toString();
                CallDialer(mobile);
            }
        });


        invokeWS_Customer_Details();

    }

    public void navigatetomodeListActivity(){
        Intent homeIntent = new Intent(Salesmen_Followup_Customer_Single.this,GeneralCodeFollowupMode_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,2);
    }

    public void navigatetopurposenameListActivity(){
        Intent homeIntent = new Intent(Salesmen_Followup_Customer_Single.this,GeneralCodeFollowupPurpose_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,3);
    }


    public void CallDialer(String mobile) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));

        startActivity(intent);
    }

    public void navigatetoCreateCustomeerActivity(){
        Intent homeIntent = new Intent(Salesmen_Followup_Customer_Single.this,SalesmenCreateCustomer.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (syscustactno != "0" && walkin_custcd != "0" ) {
            homeIntent.putExtra("walkin_custcd",walkin_custcd);
            homeIntent.putExtra("customer_custcd",customer_custcd);
            homeIntent.putExtra("syscustactno",syscustactno);
            homeIntent.putExtra("syslocno",syslocno);
            homeIntent.putExtra("sysaccledgerno",sysaccledgerno);
        }
        else
        {
            homeIntent.putExtra("walkin_custcd","0");
            homeIntent.putExtra("customer_custcd",customer_custcd);
            homeIntent.putExtra("syscustactno","0");
            homeIntent.putExtra("syslocno",syslocno);
            homeIntent.putExtra("sysaccledgerno",sysaccledgerno);

        }

        startActivityForResult(homeIntent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                mode = bundle.getString("gccode");
                modenameET.setText(bundle.getString("gcname"));
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                purpose = bundle.getString("gccode");
                purposenameET.setText(bundle.getString("gcname"));
            }
        }
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

    public void onClick_add_product(View view) {
        Intent homeIntent = new Intent(Salesmen_Followup_Customer_Single.this,Salesmen_SalesOrder.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        homeIntent.putExtra("sysinvorderno",sysinvorderno);
        homeIntent.putExtra("walkin_custcd",walkin_custcd);
        homeIntent.putExtra("customer_custcd",customer_custcd);
        homeIntent.putExtra("custname",custname1);
        homeIntent.putExtra("companycd",companycd);
        homeIntent.putExtra("companyname",locationname);
        homeIntent.putExtra("invorderno","");
        homeIntent.putExtra("syslocno",syslocno);
        homeIntent.putExtra("sysaccledgerno",sysaccledgerno);
        finish();
        startActivity(homeIntent);
    }

    public void onClick_create_quotation(View view) {

        if (syscustactno== null || syscustactno.equals("0") ) {

           // Submit_CreateWalkin();
        }

        if (Utility.isNotNull(syscustactno) && !syscustactno.equals("0") ) {

           String consignor_name = txtcustname.getText().toString();


            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String consignor_companycd  = globalVariable.getcompanycd();
            final String consignor_companyname  = globalVariable.getcompanyname();

            Intent homeIntent = new Intent(Salesmen_Followup_Customer_Single.this,Salesmen_SalesQuotation.class);

            homeIntent.putExtra("sysinvorderno","0");
            homeIntent.putExtra("walkin_custcd",walkin_custcd);
            homeIntent.putExtra("customer_custcd",customer_custcd);
            homeIntent.putExtra("syscustactno","0" + syscustactno);

            homeIntent.putExtra("custname",consignor_name);
            homeIntent.putExtra("companycd",consignor_companycd);
            homeIntent.putExtra("companyname",consignor_companyname);

            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            finish();
            startActivity(homeIntent);
        }
    }

    public void invokeWS_Customer_Details(){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("walkin_custcd", "0"+ walkin_custcd);
            paramsMap.put("customer_custcd", "0"+customer_custcd);
            paramsMap.put("syscustactno", "0"+syscustactno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/SalemenGetFollowupCustomerDetailsSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        activityrefnoET.setText(jsonObject.getString("activityrefno"));
                        txtcustname.setText(jsonObject.getString("custname"));
                        txtmobile.setText(jsonObject.getString("contactpersonmobile"));
                        txttelephone.setText(jsonObject.getString("telephone"));
                        txtemailid.setText(jsonObject.getString("emailid"));
                        txtoutstanding.setText(jsonObject.getString("productvalue"));
                        txtaddress.setText(jsonObject.getString("address"));
                        txtinquiredproduct.setText(jsonObject.getString("productenquiry"));

                        custname1 = jsonObject.getString("custname");
                        mobile1 = jsonObject.getString("contactpersonmobile");
                        emailid1 = jsonObject.getString("emailid");
                        address1 = jsonObject.getString("address");
                        outstanding1 = jsonObject.getString("productvalue");
                        inquiredproduct = jsonObject.getString("productenquiry");



                        invokeWS_Followup_History(syscustactno);

                      //  GetSalemen_CustomerOrderData(syscustactno);

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

        if (!Utility.isNotNull(folupconversation) || syscustactno.equals("0") ) {

            Toast.makeText(getApplicationContext(), "Please specify the reason for lead cancel", Toast.LENGTH_LONG).show();

        }
else
        {

            paramsMap.put("syscustactno", syscustactno);
        paramsMap.put("custcd", walkin_custcd);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("userid", userid);

        invokeWalkInCustomerCloseWS(paramsMap);
        }
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

                        invokeWS_Customer_Details();
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

                // When Http response code other than 404, 500
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


        if (!Utility.isNotNull(folupconversation) || !Utility.isNotNull(purpose) || !Utility.isNotNull(mode) ) {

            Toast.makeText(getApplicationContext(), "Please specify the followup purpose, mode and conversation", Toast.LENGTH_LONG).show();

        }

        else
        {
        paramsMap.put("syscustactno", "0" + syscustactno);
        paramsMap.put("custcd", "0" + walkin_custcd);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("nextfollowupdate", "" + followupdate);
        paramsMap.put("nextfollowuptime", "" + followuptime);
        paramsMap.put("purpose", "" + purpose);
        paramsMap.put("mode", "" + mode);
        paramsMap.put("userid", userid);

        invokeUpdateFollowupWS(paramsMap);
        }

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

                        //Intent intent = new Intent(getApplicationContext(), Salesmen_Customer_Search.class);
                        //intent.putExtra("activitytype",activitytype);
                        //intent.putExtra("activitytypedesc",activitytypedesc);
                        //intent.putExtra("locationname","");
                        //intent.putExtra("followupstatus","'00'");
                        //intent.putExtra("status","PENDING");
                        //finish();
                        //startActivity(intent);

                        invokeWS_Customer_Details();

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

                // When Http response code other than 404, 500
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

                        TableRow tblrowHeading = new TableRow(Salesmen_Followup_Customer_Single.this);
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

                                TableRow tblrowLabels = new TableRow(Salesmen_Followup_Customer_Single.this);

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

                    // When Http response code other than 404, 500
                                    }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Salesmen_Followup_Customer_Single.this);
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
        TextView textView = new TextView(Salesmen_Followup_Customer_Single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Salesmen_Followup_Customer_Single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    public void navigatetoImageCaptureActivity(){
        Intent homeIntent = new Intent(Salesmen_Followup_Customer_Single.this,ServiceCallDocumentImageActivity.class);

        String folupconversation = folupconversationET.getText().toString();
        String followupdate = pickNextFollowupDate.getText().toString();
        String followuptime = txtNextFollowupTime.getText().toString();

        foldername=activityrefnoET.getText().toString();

        foldername=foldername.replace("/","");
        foldername=foldername.replace("-","");
        foldername=foldername.replace("\\","");

        homeIntent.putExtra("syscustactno",syscustactno);
        homeIntent.putExtra("foldername",foldername);
        homeIntent.putExtra("activitystatus",activitystatus);
        homeIntent.putExtra("folupconversation",folupconversation);
        homeIntent.putExtra("nextfollowupdate",followupdate);
        homeIntent.putExtra("nextfollowuptime",followuptime);
        homeIntent.putExtra("purpose",purpose);
        homeIntent.putExtra("mode",mode);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        //finish();
    }

}
