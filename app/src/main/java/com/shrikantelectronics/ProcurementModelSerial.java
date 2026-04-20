package com.shrikantelectronics;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProcurementModelSerial extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;

    EditText serialnoET ;
    EditText locationET ;
    EditText modelnameET ;
    EditText brandnameET ;
    EditText stickernumberET;
    EditText refdocumentnoET;

    String companycd, sysvendorno, sysbrandno, sysmodelno, sysprocurmentno, sysprocdtlno ;
    String companyname, vendorname, srefdocumentno, vprocurementdt, brandname, modelname, quantity, serialnnocount;
    String serialno, stickernumber, sysproduct_procurmentno;
    Button search_serial,search_sticker;
    String sSerialNumbere, sStickerNumbere;



    private Button scanBtn, scan_button_sticker;
    ListViewAdapter adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<Models> arraylist = new ArrayList<Models>();

    String TAG = "StockVerification.java";

    String popUpContents[];
    PopupWindow popupWindowTopCategory;
    Button Button_modelname;

    List<String> topCategoryList = new ArrayList<String>();

    ListView listViewTopCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurementmodelserial);

        serialnoET = (EditText)findViewById(R.id.serialno);
      //  locationET = (EditText)findViewById(R.id.location);
        modelnameET = (EditText)findViewById(R.id.modelname);
        brandnameET = (EditText)findViewById(R.id.brandname);
        stickernumberET = (EditText)findViewById(R.id.stickernumber);
        refdocumentnoET = (EditText)findViewById(R.id.refdocumentno);

        serialnoET.requestFocus();

        Intent i = getIntent();

        sysprocurmentno = i.getStringExtra("sysprocurmentno");
        sysprocdtlno = i.getStringExtra("sysprocdtlno");
        companycd = i.getStringExtra("companycd");
        sysvendorno = i.getStringExtra("sysvendorno");
        sysbrandno = i.getStringExtra("sysbrandno");
        sysmodelno = i.getStringExtra("sysmodelno");

        companyname = i.getStringExtra("companyname");
        vendorname = i.getStringExtra("vendorname");
        srefdocumentno = i.getStringExtra("refdocumentno");
        vprocurementdt = i.getStringExtra("vprocurementdt");
        brandname = i.getStringExtra("brandname");
        modelname = i.getStringExtra("modelname");
        quantity = i.getStringExtra("quantity");
        serialnnocount = i.getStringExtra("serialnnocount");

        refdocumentnoET.setText(srefdocumentno);
        brandnameET.setText(brandname);
        modelnameET.setText(modelname);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        OnClickListener handler = new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.Button_modelname:
                        navigatetoModelListActivity();
                        break;
                }
            }
        };

        // our button
       // Button_modelname = (Button) findViewById(R.id.Button_modelname);
      ///  Button_modelname.setOnClickListener(handler);

        scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureSerialNumber(7);
            }
        });

        scan_button_sticker = (Button)findViewById(R.id.scan_button_sticker);
        scan_button_sticker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureSerialNumber(8);
            }
        });

       invokeProcurementModelSerialListWS();
    }

    private void captureSerialNumber(int requestCode) {
//        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
  //      scanIntegrator.setRequestCode(requestCode);
    //    scanIntegrator.initiateScan( );

        Intent homeIntent = new Intent(ProcurementModelSerial.this, com.shrikantelectronics.BarcodeScanner.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestCode);

    }


    public void Invoke_ProductSerialDetails(String sSerialNo, String sstickernumber) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("serialno", "" +  sSerialNo );
            paramsMap.put("stickernumber", "" );

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/SearchProcurementSerialNumber", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("productdetails");

                        if(new_array.length() > 0 )
                        {
                            JSONObject jsonObject = new_array.getJSONObject(0);

                            modelnameET.setText(jsonObject.getString("modelname"));
                        serialnoET.setText(jsonObject.getString("serialno"));
                        //locationET.setText(jsonObject.getString("companyname"));
                        brandnameET.setText(jsonObject.getString("brandname"));
                        stickernumberET.setText(jsonObject.getString("stickernumber"));
                        refdocumentnoET.setText(jsonObject.getString("refdocumentno"));

                            sysprocurmentno= jsonObject.getString("sysprocurmentno");
                            sysprocdtlno= jsonObject.getString("sysprocdtlno");
                            sysvendorno= jsonObject.getString("sysvendorno");
                            companycd = jsonObject.getString("companycd");
                            sysbrandno = jsonObject.getString("sysbrandno");
                            sysmodelno = jsonObject.getString("sysmodelno");
                            sysproduct_procurmentno = jsonObject.getString("sysproduct_procurmentno");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "SERIAL NUMBER NOT FOUND", Toast.LENGTH_LONG).show();
                            setDefaultValues();
                        }

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
            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void onClick_submit(View view) {
        // Get Email Edit View Value

        String stickernumber;
        String refdocumentno;

        stickernumber = "";
        refdocumentno = "";

        String modelname = modelnameET.getText().toString();
        String serialno = serialnoET.getText().toString();
        String brandname = brandnameET.getText().toString();
        stickernumber= stickernumberET.getText().toString();
        refdocumentno= refdocumentnoET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(serialno) && Utility.isNotNull(modelname) && Utility.isNotNull(brandname) ) {
                // Put Http parameter usermodelname with value of Email Edit View control

            paramsMap.put("sysprocdtlno", "0"+sysprocdtlno);
            paramsMap.put("sysprocurmentno", "0"+sysprocurmentno);
            paramsMap.put("serialno", ""+serialno);
            paramsMap.put("sysmodelno", "0"+sysmodelno);
            paramsMap.put("stickernumber", "");
            paramsMap.put("sysproduct_procurmentno", "0");

            invokeWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS(Map<String, String> paramsMap){
        // Show Progress Dialog
//        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

       // Toast.makeText(getApplicationContext(), params.toString(), Toast.LENGTH_LONG).show();

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddProcurementProduct", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog

                setDefaultValues();
                Toast.makeText(getApplicationContext(), "Serial Number Added Successfully...!", Toast.LENGTH_LONG).show();

              //  prgDialog.hide();
                try {
                   final Map<String, String> paramsMap = new HashMap<>();

                    tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                    tabletoplocationstock.removeAllViews();
                    tabletoplocationstock.setStretchAllColumns(true);
                    tabletoplocationstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(ProcurementModelSerial.this);

                    TextView highsHeading_rowid= initPlainHeaderTextView();
                    highsHeading_rowid.setText("ID");

                    highsHeading_rowid.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_rowid.setGravity(Gravity.CENTER);

                    TextView highsHeading_Serial= initPlainHeaderTextView();
                    highsHeading_Serial.setText("Serial Number");

                    highsHeading_Serial.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Serial.setGravity(Gravity.CENTER);

                    TextView highsHeading_Sticker = initPlainHeaderTextView();
                    highsHeading_Sticker.setText("Sticker");
                    highsHeading_Sticker.setWidth(20);
                    highsHeading_Sticker.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Sticker.setGravity(Gravity.CENTER);

                    tblrowHeading.addView(highsHeading_rowid);
                    tblrowHeading.addView(highsHeading_Serial);
                   // tblrowHeading.addView(highsHeading_Sticker);

                    tabletoplocationstock.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("procurementdtl_serial");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            serialno=jsonObject.getString("serialno");
                            stickernumber=jsonObject.getString("stickernumber");
                            final String ssysproduct_procurmentno=jsonObject.getString("sysproduct_procurmentno");

                            TableRow tblrowLabels = new TableRow(ProcurementModelSerial.this);

                            TextView highsLabel_rowid = initPlainTextView(i);
                            highsLabel_rowid.setText(jsonObject.getString("Rank"));
                            highsLabel_rowid.setWidth(20);
                            highsLabel_rowid.setTypeface(Typeface.DEFAULT);
                            highsLabel_rowid.setGravity(Gravity.CENTER);

                            final TextView highsLabel_serialno = initPlainTextView(i);
                            highsLabel_serialno.setText(jsonObject.getString("serialno"));

                            highsLabel_serialno.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialno.setGravity(Gravity.RIGHT);
                            highsLabel_serialno.setOnClickListener(new OnClickListener()

                            {
                                @Override
                                public void onClick(View v)
                                {

                                    Invoke_ProductSerialDetails(highsLabel_serialno.getText().toString(),"");
                                }
                            });

                            TextView highsLabel_barcodeno = initPlainTextView(i);
                            highsLabel_barcodeno.setText(jsonObject.getString("stickernumber"));
                            highsLabel_barcodeno.setTypeface(Typeface.DEFAULT);
                            highsLabel_barcodeno.setGravity(Gravity.CENTER);

                            tblrowLabels.addView(highsLabel_rowid);
                            tblrowLabels.addView(highsLabel_serialno);
                            //tblrowLabels.addView(highsLabel_barcodeno);

                            tblrowLabels.setClickable(true);

                            tabletoplocationstock.addView(tblrowLabels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
             //   prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    public void invokeProcurementModelSerialListWS(){
        // Show Progress Dialog
     //   prgDialog.show();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysprocdtlno", "0"+sysprocdtlno);
        paramsMap.put("sysprocurmentno", "0"+sysprocurmentno);

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GetProcurementModelSerialList", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
            //    prgDialog.hide();
                try {
                    final Map<String, String> paramsMap = new HashMap<>();

                    tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                    tabletoplocationstock.removeAllViews();
                    tabletoplocationstock.setStretchAllColumns(true);
                    tabletoplocationstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(ProcurementModelSerial.this);

                    TextView highsHeading_rowid= initPlainHeaderTextView();
                    highsHeading_rowid.setText("ID");
                    highsHeading_rowid.setWidth(50);
                    highsHeading_rowid.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_rowid.setGravity(Gravity.CENTER);

                    TextView highsHeading_Serial= initPlainHeaderTextView();
                    highsHeading_Serial.setText("Serial Number");
                    highsHeading_Serial.setWidth(300);
                    highsHeading_Serial.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Serial.setGravity(Gravity.CENTER);

                    TextView highsHeading_Sticker = initPlainHeaderTextView();
                    highsHeading_Sticker.setText("Sticker");
                    highsHeading_Sticker.setWidth(20);
                    highsHeading_Sticker.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Sticker.setGravity(Gravity.CENTER);

                    tblrowHeading.addView(highsHeading_rowid);
                    tblrowHeading.addView(highsHeading_Serial);
                   // tblrowHeading.addView(highsHeading_Sticker);

                    tabletoplocationstock.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("procurementdtl_serial");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            serialno=jsonObject.getString("serialno");
                            stickernumber=jsonObject.getString("stickernumber");
                            final String ssysproduct_procurmentno=jsonObject.getString("sysproduct_procurmentno");

                            TableRow tblrowLabels = new TableRow(ProcurementModelSerial.this);

                            TextView highsLabel_rowid = initPlainTextView(i);
                            highsLabel_rowid.setText(jsonObject.getString("Rank"));
                            highsLabel_rowid.setWidth(20);
                            highsLabel_rowid.setTypeface(Typeface.DEFAULT);
                            highsLabel_rowid.setGravity(Gravity.CENTER);

                            final TextView highsLabel_serialno = initPlainTextView(i);
                            highsLabel_serialno.setText(jsonObject.getString("serialno"));
                            highsLabel_serialno.setWidth(300);
                            highsLabel_serialno.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialno.setGravity(Gravity.LEFT);
                            highsLabel_serialno.setOnClickListener(new OnClickListener()

                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Invoke_ProductSerialDetails(highsLabel_serialno.getText().toString(),"");
                                }
                            });

                            TextView highsLabel_barcodeno = initPlainTextView(i);
                            highsLabel_barcodeno.setText(jsonObject.getString("stickernumber"));
                            highsLabel_barcodeno.setWidth(20);
                            highsLabel_barcodeno.setTypeface(Typeface.DEFAULT);
                            highsLabel_barcodeno.setGravity(Gravity.CENTER);

                            tblrowLabels.addView(highsLabel_rowid);
                            tblrowLabels.addView(highsLabel_serialno);
                          //  tblrowLabels.addView(highsLabel_barcodeno);

                            tblrowLabels.setClickable(true);

                            tabletoplocationstock.addView(tblrowLabels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
              //  prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    public void setDefaultValues(){
        serialnoET.setText("");
        stickernumberET.setText("");
       }

    public void onClick_DeleteSerial(View view) {
        // Get Email Edit View Value

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysproduct_procurmentno)) {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            // Put Http parameter usermodelname with value of Email Edit View control
            paramsMap.put("sysprocurmentno", "0"+sysprocurmentno);
            paramsMap.put("sysprocdtlno", "0"+sysprocdtlno);
            paramsMap.put("sysproduct_procurmentno", "0"+sysproduct_procurmentno);
            paramsMap.put("userid", userid);

            invokeDeleteSeralWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeDeleteSeralWS(Map<String, String> paramsMap){
        // Show Progress Dialog
     //   prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/DeleteProcurement_Serial", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
             //   prgDialog.hide();
                try {
                    final Map<String, String> paramsMap = new HashMap<>();

                    tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);
                    tabletoplocationstock.removeAllViews();
                    tabletoplocationstock.setStretchAllColumns(true);
                    tabletoplocationstock.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(ProcurementModelSerial.this);

                    TextView highsHeading_rowid = initPlainHeaderTextView();
                    highsHeading_rowid.setText("ID");

                    highsHeading_rowid.setTypeface(Typeface.DEFAULT);
                    highsHeading_rowid.setGravity(Gravity.CENTER);

                    TextView highsHeading_Serial= initPlainHeaderTextView();
                    highsHeading_Serial.setText("Serial Number");

                    highsHeading_Serial.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Serial.setGravity(Gravity.CENTER);

                    TextView highsHeading_Sticker = initPlainHeaderTextView();
                    highsHeading_Sticker.setText("Sticker");
                    highsHeading_Sticker.setWidth(20);
                    highsHeading_Sticker.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_Sticker.setGravity(Gravity.CENTER);

                    tblrowHeading.addView(highsHeading_rowid);
                    tblrowHeading.addView(highsHeading_Serial);
                   // tblrowHeading.addView(highsHeading_Sticker);

                    tabletoplocationstock.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("procurementdtl_serial");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            serialno=jsonObject.getString("serialno");
                            stickernumber=jsonObject.getString("stickernumber");
                            final String ssysproduct_procurmentno=jsonObject.getString("sysproduct_procurmentno");

                            TableRow tblrowLabels = new TableRow(ProcurementModelSerial.this);

                            TextView highsLabel_rowid = initPlainTextView(i);
                            highsLabel_rowid.setText(jsonObject.getString("Rank"));
                            highsLabel_rowid.setWidth(20);
                            highsLabel_rowid.setTypeface(Typeface.DEFAULT);
                            highsLabel_rowid.setGravity(Gravity.CENTER);

                            final TextView highsLabel_serialno = initPlainTextView(i);
                            highsLabel_serialno.setText(jsonObject.getString("serialno") );
                            highsLabel_serialno.setWidth(300);
                            highsLabel_serialno.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialno.setGravity(Gravity.LEFT);
                            highsLabel_serialno.setOnClickListener(new OnClickListener()

                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Invoke_ProductSerialDetails(highsLabel_serialno.getText().toString(),"");
                                }
                            });

                            tblrowLabels.addView(highsLabel_rowid);
                            tblrowLabels.addView(highsLabel_serialno);

                            tblrowLabels.setClickable(true);

                            tabletoplocationstock.addView(tblrowLabels);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    serialnoET.setText("");
                    stickernumberET.setText("");
                    sysproduct_procurmentno= "0";

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
              //  prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    public void onClick_DeleteModel(View view) {
        // Get Email Edit View Value

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysprocdtlno)) {
            // Put Http parameter usermodelname with value of Email Edit View control
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            paramsMap.put("sysprocurmentno", "0"+sysprocurmentno);
            paramsMap.put("sysprocdtlno", "0"+sysprocdtlno);
            paramsMap.put("userid", "0"+userid);

            invokeDeleteModelWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeDeleteModelWS(Map<String, String> paramsMap){
        // Show Progress Dialog
       // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/DeleteProcurement_Model", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
              //  prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    // When the JSON response has status boolean value assigned with true

                    //Toast.makeText(getApplicationContext(), "Customer is successfully added!", Toast.LENGTH_LONG).show();

                    if(obj.getBoolean("status")){
                        // Set Default Values for Edit View controls
                        //setResetValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Product Deleted Successfully...!", Toast.LENGTH_LONG).show();
                        //navigatetoFollowupActivity();
                        finish();
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
              //  prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysmodelno = bundle.getString("sysmodelno");
                modelnameET.setText(bundle.getString("modelname"));

            }
        }

        if (requestCode == 7) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                // String scanFormat = scanningResult.getFormatName();
                //formatTxt.setText("FORMAT: " + scanFormat);
                serialnoET.setText(scanContent);
                // sSerialNumbere = serialnoET.getText().toString();
                //  Invoke_ProductSerialDetails(sSerialNumbere);
            }
        }

        if (requestCode == 8) {

            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                stickernumberET.setText(scanContent);
            }
        }
    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(ProcurementModelSerial.this,StockVerificationModel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysbrandno",sysbrandno);
        startActivityForResult(homeIntent,1);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(ProcurementModelSerial.this);
        textView.setPadding(10, 30, 10, 30);


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
        TextView textView = new TextView(ProcurementModelSerial.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(ProcurementModelSerial.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


}

