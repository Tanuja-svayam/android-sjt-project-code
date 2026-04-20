package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
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

public class CrmActivity_Recovery_Allocation_Single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    ProgressDialog prgDialog;

    EditText activitytypedescTV;
    EditText activityrefnoTV;
    EditText referencecodeTV;
    EditText vactivitydtTV;
    EditText employeefullnameTV;
    EditText custfullnameTV;
    EditText contactpersonmobileTV;
    EditText telephoneTV;
    EditText emailidTV;
    EditText salesmenTV;
    EditText locationTV;
    EditText chargesTV;
    EditText folupconversationET;

    TextView pickNextFollowupDate;
    int year, month, day;
    String todate;

    String syscustactno;

    String activitytypedesc;
    String activitytype;
    String activityrefno;
    String referencecode;
    String vactivitydt;
    String vfollowupdt;
    String employeefullname;
    String purposedesc;
    String modedesc;
    String custfullname;
    String contactpersonmobile;
    String telephone;
    String emailid;
    String brandfullname;
    String modelfullname;
    String serialno;
    String salesmen;
    String location;
    String charges;
    String sfolupconversation;

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crmfollowup_recovery_allocation_single);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        syscustactno = i.getStringExtra("syscustactno");
        activitytypedesc = i.getStringExtra("activitytypedesc");
        activitytype = i.getStringExtra("activitytype");

        activitytypedescTV = (EditText) findViewById(R.id.activitytypedesc);
        activityrefnoTV = (EditText) findViewById(R.id.activityrefno);
        referencecodeTV = (EditText) findViewById(R.id.referencecode);
        vactivitydtTV = (EditText) findViewById(R.id.vactivitydt);
        employeefullnameTV = (EditText) findViewById(R.id.employeename);
        custfullnameTV = (EditText) findViewById(R.id.custname);
        contactpersonmobileTV = (EditText) findViewById(R.id.contactpersonmobile);
        telephoneTV = (EditText) findViewById(R.id.telephone);
        emailidTV = (EditText) findViewById(R.id.emailid);
        salesmenTV = (EditText) findViewById(R.id.salesmen);
        locationTV = (EditText) findViewById(R.id.location);
        chargesTV = (EditText) findViewById(R.id.charges);

        invokeWS_Followup_Details(syscustactno);

    }

    public void invokeWS_EmployeeList() {

        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String companycd  = globalVariable.getcompanycd();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("companycd", companycd);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            //          header_toplocationstock.setText(categoryfullname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetEmployeeDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(CrmActivity_Recovery_Allocation_Single.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Employee");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Contact");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("In Hand");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("employeelist");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(CrmActivity_Recovery_Allocation_Single.this);

                                final String sysemployeeno = jsonObject.getString("sysemployeeno");

                                final TextView highsLabel_fullname = initPlainTextView(i);
                                highsLabel_fullname.setText(jsonObject.getString("fullname"));
                                highsLabel_fullname.setTypeface(Typeface.DEFAULT);
                                highsLabel_fullname.setGravity(Gravity.LEFT);
                                highsLabel_fullname.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)

                                    {
                                        // When Email Edit View and Password Edit View have values other than Null
                                        if (Utility.isNotNull(sysemployeeno)) {
                                            // When Email entered is Valid

                                            invokeUpdateEmployeeInCustomerWS(sysemployeeno);

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Please select the service provider", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("mobile"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("noofcustomerhandelled"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_fullname);
                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highslabel_Value);

                                tblrowLabels.setClickable(true);

                                tabletoplocationstock.addView(tblrowLabels);


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
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    public void invokeUpdateEmployeeInCustomerWS(String sysemployeeno){
        // Show Progress Dialog
        prgDialog.show();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();
        folupconversationET = (EditText) findViewById(R.id.folupconversation);
        String folupconversation = folupconversationET.getText().toString();

        paramsMap.put("sysemployeeno", sysemployeeno);
        paramsMap.put("syscustactno", syscustactno);
        paramsMap.put("folupconversation", "" + folupconversation);
        paramsMap.put("userid", userid);

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/UpdateEmployeeInCustomer", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                    Intent homeIntent = new Intent(CrmActivity_Recovery_Allocation_Single.this,CrmActivity_Recovery_Allocation.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
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

    public void invokeWS_Followup_Details(String syscustactno){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("syscustactno", syscustactno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetActivityDetailSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("crm_activity");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        activitytype = jsonObject.getString("activitytype");
                        activitytypedesc = jsonObject.getString("activitytypedesc");

                        activitytypedescTV.setText(jsonObject.getString("activitytypedesc"));
                        activityrefnoTV.setText(jsonObject.getString("activityrefno"));
                        referencecodeTV.setText(jsonObject.getString("referencecode"));
                        vactivitydtTV.setText(jsonObject.getString("vactivitydt"));
                        employeefullnameTV.setText(jsonObject.getString("followupbyemployeename"));
                        custfullnameTV.setText(jsonObject.getString("custname"));
                        contactpersonmobileTV.setText(jsonObject.getString("contactpersonmobile"));
                        telephoneTV.setText(jsonObject.getString("telephone"));
                        emailidTV.setText(jsonObject.getString("emailid"));
                        salesmenTV.setText(jsonObject.getString("salesmen"));
                        locationTV.setText(jsonObject.getString("location"));
                        chargesTV.setText(jsonObject.getString("charges"));

                        invokeWS_EmployeeList();

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


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(CrmActivity_Recovery_Allocation_Single.this);
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
        TextView textView = new TextView(CrmActivity_Recovery_Allocation_Single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(CrmActivity_Recovery_Allocation_Single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

}
