package com.shrikantelectronics;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class customer_view_single_for_loan extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TextView txtcustname;
    TextView txtcontactpersonmobile;
    TextView txttelephone;
    TextView txtcusaddress;
    TextView txtreferenceperson_name;
    TextView txtreferenceperson_mobile;
    TextView txtemailid;
    TextView txtinvoiceno;
    TextView txtvinvoicedt;
    TextView txtvloandt;
    TextView txtvfirstemidt;
    TextView txtvlastemidt;
   // TextView txtfincompanyname;
   // TextView txtfinancelocation;
    TextView txtloanamt;
  //  TextView txtinsuranceamt;
    TextView txtnoofmonth;
  //  TextView txtupfrontemi;
    TextView txtemipermonth;
    TextView txtprocessingfee;
    TextView txtcardcharges;
    TextView txtdbdamount;
    //TextView txttotalupfront;
   // TextView txtroundoff;
    //TextView txtdisbursementamt;
  //  TextView txtcustcd;
   // TextView txtsysloanno;
    //TextView txtbalamt;
    TextView txtoverdue;
    TextView txtduedate;

    String custname;
    String contactpersonmobile;

    String telephone;
    String cusaddress;
    String referenceperson_name;
    String referenceperson_mobile;
    String emailid;
    String sysinvno;
    String invoiceno;
    String vinvoicedtvvloandt;
    String vfirstemidt;
    String vlastemidt;
    String fincompanyname;
    String financelocation;
    String loanamt;
    String insuranceamt;
    String noofmonth;
    String upfrontemi;
    String emipermonth;
    String processingfee;
    String cardcharges;
    String dbdamount;
    String totalupfront;
    String roundoff;
    String disbursementamt;
    String custcd;
    String sysloanno;
    String balamt;
    String overdue;
    String duedate;
    String fromdate;

    ListViewAdapter_Loan_EMI adapter;
    private ListView lv_Loan_Emi ;
    ArrayList<Loan_Emi> arraylist = new ArrayList<Loan_Emi>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_single_for_loan);

        Intent i = getIntent();
        sysloanno = i.getStringExtra("sysloanno");
        custcd = i.getStringExtra("custcd");
        fromdate= i.getStringExtra("fromdate");

        txtcustname		       = (TextView) findViewById(R.id.custname);
        txtcontactpersonmobile	       = (TextView) findViewById(R.id.contactpersonmobile);
        txttelephone	       = (TextView) findViewById(R.id.telephone);
        txtcusaddress		       = (TextView) findViewById(R.id.cusaddress);
        txtreferenceperson_name	       = (TextView) findViewById(R.id.referenceperson_name);
        txtreferenceperson_mobile      = (TextView) findViewById(R.id.referenceperson_mobile);
        txtemailid		       = (TextView) findViewById(R.id.emailid);
        txtinvoiceno		       = (TextView) findViewById(R.id.invoiceno);
        txtvinvoicedt		       = (TextView) findViewById(R.id.vinvoicedt);
        txtvloandt		       = (TextView) findViewById(R.id.vloandt);
        txtvfirstemidt		       = (TextView) findViewById(R.id.vfirstemidt);
        txtvlastemidt		       = (TextView) findViewById(R.id.vlastemidt);
       // txtfincompanyname	       = (TextView) findViewById(R.id.fincompanyname);
       // txtfinancelocation	       = (TextView) findViewById(R.id.financelocation);
        txtloanamt		       = (TextView) findViewById(R.id.loanamt);
        //txtinsuranceamt		       = (TextView) findViewById(R.id.insuranceamt);
        txtnoofmonth		       = (TextView) findViewById(R.id.noofmonth);
      //  txtupfrontemi		       = (TextView) findViewById(R.id.upfrontemi);
        txtemipermonth		       = (TextView) findViewById(R.id.emipermonth);
        txtprocessingfee	       = (TextView) findViewById(R.id.processingfee);
        txtcardcharges		       = (TextView) findViewById(R.id.cardcharges);
        txtdbdamount		       = (TextView) findViewById(R.id.dbdamount);
       // txttotalupfront		       = (TextView) findViewById(R.id.totalupfront);
        //txtroundoff		       = (TextView) findViewById(R.id.roundoff);
       // txtdisbursementamt	       = (TextView) findViewById(R.id.disbursementamt);
       // txtcustcd		       = (TextView) findViewById(R.id.custcd);
        //txtsysloanno		       = (TextView) findViewById(R.id.sysloanno);
        //txtbalamt		       = (TextView) findViewById(R.id.balamt);
        txtoverdue		       = (TextView) findViewById(R.id.overdue);
        txtduedate		       = (TextView) findViewById(R.id.duedate);


        txtcontactpersonmobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mobile;
                mobile= txtcontactpersonmobile.getText().toString();
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


        txtinvoiceno.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(customer_view_single_for_loan.this, Invoice_view_single.class);

                intent.putExtra("sysinvno",sysinvno);
                intent.putExtra("custname","");
                intent.putExtra("netinvoiceamt","");
                intent.putExtra("vinvoicedt","");
                intent.putExtra("invoiceno","");
                intent.putExtra("PURPOSE","DOWNLOAD");

                startActivity(intent);
            }
        });


        txtvinvoicedt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(customer_view_single_for_loan.this, Invoice_view_single.class);

                intent.putExtra("sysinvno",sysinvno);
                intent.putExtra("custname","");
                intent.putExtra("netinvoiceamt","");
                intent.putExtra("vinvoicedt","");
                intent.putExtra("invoiceno","");
                intent.putExtra("PURPOSE","DOWNLOAD");

                startActivity(intent);
            }
        });


        invokeWS_Customer_Loan_Details();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_custaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

          if (id == R.id.action_wa) {
            CallSms();
        } else if (id == R.id.action_ledger) {
            CustomerLedger();
          } else if (id == R.id.action_giftcardledger) {
              CustomerGiftCardLedger();
        }
        return super.onOptionsItemSelected(item);
    }



    public void CallDialer(String mobile) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));

        startActivity(intent);
    }


    public void CallEmail(View view) {

        String emailid;
        emailid= txtemailid.getText().toString();

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailid});
        email.putExtra(Intent.EXTRA_SUBJECT, "Outstanding");
        email.putExtra(Intent.EXTRA_TEXT, "" );
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }


    public void CallSms() {

        invokeSendOutstandingSMWS();

        /*
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"+ mobile1));
        sendIntent.putExtra("sms_body", "" );
        startActivity(sendIntent);
*/

    }




    public void CustomerGiftCardLedger(){
        Intent customerIntent = new Intent(customer_view_single_for_loan.this,Customer_Giftcard_Ledger_View.class);

        String name;
        name= txtcustname.getText().toString();

        customerIntent.putExtra("custcd",custcd);
        customerIntent.putExtra("name",name);
        customerIntent.putExtra("fromdate","");
        customerIntent.putExtra("todate","");

        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void CustomerLedger(){
        Intent customerIntent = new Intent(customer_view_single_for_loan.this,Customer_Ledger_View.class);

        String name;
        name= txtcustname.getText().toString();

        customerIntent.putExtra("custcd",custcd);
        customerIntent.putExtra("name",name);
        customerIntent.putExtra("fromdate","");
        customerIntent.putExtra("todate","");

        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void GetCustomer_EMIData(){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysloanno", sysloanno);

            invokeWS_Loan_Emi(paramsMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeWS_Loan_Emi(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerLoanEMIList", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("loanemilist");
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                Loan_Emi wp = new Loan_Emi(jsonObject.getString("sysemino"), jsonObject.getString("sysloanno"), jsonObject.getString("emino"), jsonObject.getString("vemidt"), jsonObject.getString("vemipaiddt"), jsonObject.getString("vreceiptdt"), jsonObject.getString("emiamt"), jsonObject.getString("adjamt"),jsonObject.getString("balamt"), jsonObject.getString("overduedays") );

                                arraylist.add(wp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lv_Loan_Emi  = (ListView) findViewById(R.id.list_view );

                        adapter = new ListViewAdapter_Loan_EMI(customer_view_single_for_loan.this, arraylist);

                        lv_Loan_Emi.setAdapter(adapter);

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

    public void invokeWS_Customer_Loan_Details(){
        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("fromdate", fromdate);
            paramsMap.put("todate", fromdate);
            paramsMap.put("sysloanno", sysloanno);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetCustomerLoanDetailsSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("customersingle");
                        JSONObject jsonObject = new_array.getJSONObject(0);

                        txtcustname.setText(jsonObject.getString("custname"));
                        txtcontactpersonmobile.setText(jsonObject.getString("contactpersonmobile"));
                        txttelephone.setText(jsonObject.getString("telephone"));
                        txtcusaddress.setText(jsonObject.getString("cusaddress"));
                        txtreferenceperson_name.setText(jsonObject.getString("referenceperson_name"));
                        txtreferenceperson_mobile.setText(jsonObject.getString("referenceperson_mobile"));
                        txtemailid.setText(jsonObject.getString("emailid"));
                        txtinvoiceno.setText(jsonObject.getString("invoiceno"));
                        txtvinvoicedt.setText(jsonObject.getString("vinvoicedt"));
                        txtvloandt.setText(jsonObject.getString("vloandt"));
                        txtvfirstemidt.setText(jsonObject.getString("vfirstemidt"));
                        txtvlastemidt.setText(jsonObject.getString("vlastemidt"));
                        //txtfincompanyname.setText(jsonObject.getString("fincompanyname"));
                        //txtfinancelocation.setText(jsonObject.getString("financelocation"));
                        txtloanamt.setText(jsonObject.getString("loanamt"));
                        //txtinsuranceamt.setText(jsonObject.getString("insuranceamt"));
                        txtnoofmonth.setText(jsonObject.getString("noofmonth"));
                     //   txtupfrontemi.setText(jsonObject.getString("upfrontemi"));
                        txtemipermonth.setText(jsonObject.getString("emipermonth"));
                        txtprocessingfee.setText(jsonObject.getString("processingfee"));
                        txtcardcharges.setText(jsonObject.getString("cardcharges"));
                        txtdbdamount.setText(jsonObject.getString("dbdamount"));
                     //   txttotalupfront.setText(jsonObject.getString("totalupfront"));
                        //txtroundoff.setText(jsonObject.getString("roundoff"));
                     //   txtdisbursementamt.setText(jsonObject.getString("disbursementamt"));
                        custcd = jsonObject.getString("custcd");
                        sysloanno = jsonObject.getString("sysloanno");
                        sysinvno = jsonObject.getString("sysinvno");
                        //txtbalamt.setText(jsonObject.getString("balamt"));
                        txtoverdue.setText(jsonObject.getString("overdue"));
                        txtduedate.setText(jsonObject.getString("duedate"));

                        GetCustomer_EMIData();

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

    public void invokeSendOutstandingSMWS(){
        // Show Progress Dialog
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysloanno", sysloanno);

        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SendOutstandingSMSToCustomer", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {

                try {
                    JSONObject obj = response;

                    //
                    // When the JSON response has status boolean value assigned with true
                    //
                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

                    }
                    // Else display error message
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
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'


            }
        });
    }


}
