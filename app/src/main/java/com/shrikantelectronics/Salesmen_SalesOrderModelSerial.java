package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.util.List;

public class Salesmen_SalesOrderModelSerial extends AppCompatActivity {

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
    EditText invordernoET;
    EditText deliveryinstructionET;

    String companycd, customer_custcd,walkin_custcd, sysbrandno, sysmodelno, sysinvorderno, sysorderdtlno ;
    String companyname, custname, invorderno, vinvorderdt, brandname, modelname, quantity, unitprice, deliveryinstruction = "";
    String serialno, stickernumber, sysproductno;
    Button search_serial,search_sticker;
    String sSerialNumbere, sStickerNumbere;

    ListViewAdapter adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<Models> arraylist = new ArrayList<Models>();

    String TAG = "StockVerification.java";

    String popUpContents[];
    PopupWindow popupWindowTopCategory;
    Button Button_modelname;
    private Button scanBtn, scan_button_sticker;

    List<String> topCategoryList = new ArrayList<String>();

    ListView listViewTopCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesmen_salesordermodelserial);

        serialnoET = (EditText)findViewById(R.id.serialno);
        locationET = (EditText)findViewById(R.id.location);
        modelnameET = (EditText)findViewById(R.id.modelname);
        brandnameET = (EditText)findViewById(R.id.brandname);
        stickernumberET = (EditText)findViewById(R.id.stickernumber);
        invordernoET = (EditText)findViewById(R.id.invorderno);
        deliveryinstructionET = (EditText)findViewById(R.id.deliveryinstruction);

        serialnoET.requestFocus();

        Intent i = getIntent();

        sysinvorderno = i.getStringExtra("sysinvorderno");
        sysorderdtlno = i.getStringExtra("sysorderdtlno");
        companycd = i.getStringExtra("companycd");
        walkin_custcd = i.getStringExtra("walkin_custcd");
        customer_custcd = i.getStringExtra("customer_custcd");
        sysbrandno = i.getStringExtra("sysbrandno");
        sysmodelno = i.getStringExtra("sysmodelno");

        companyname = i.getStringExtra("companyname");
        custname = i.getStringExtra("custname");
        invorderno = i.getStringExtra("invorderno");
        vinvorderdt = i.getStringExtra("vinvorderdt");
        brandname = i.getStringExtra("brandname");
        modelname = i.getStringExtra("modelname");
        quantity = i.getStringExtra("quantity");
        unitprice = i.getStringExtra("unitprice");

        sysproductno = i.getStringExtra("sysproductno");
        stickernumber = i.getStringExtra("stickernumber");
        serialno = i.getStringExtra("serialno");
        deliveryinstruction = i.getStringExtra("deliveryinstruction");

        invordernoET.setText(invorderno);
        brandnameET.setText(brandname);
        modelnameET.setText(modelname);
        serialnoET.setText(serialno);
        stickernumberET.setText(stickernumber);
        deliveryinstructionET.setText(deliveryinstruction);

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

        serialnoET.addTextChangedListener(new GenericTextWatcher_ProductSerial(serialnoET));
        stickernumberET.addTextChangedListener(new GenericTextWatcher_ProductSerial(stickernumberET));

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

        invokeSearchSalesmen_SalesOrder_DetailWS();
    }


    private class GenericTextWatcher_ProductSerial implements TextWatcher {

        private View view;
        private GenericTextWatcher_ProductSerial(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = serialnoET.getText();

                invokeWS_SerialStock();

        }
    }

    private void captureSerialNumber(int requestCode) {
//        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //      scanIntegrator.setRequestCode(requestCode);
        //    scanIntegrator.initiateScan( );

        Intent homeIntent = new Intent(Salesmen_SalesOrderModelSerial.this, com.shrikantelectronics.BarcodeScanner.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestCode);

    }



    public void invokeSearchSalesmen_SalesOrder_DetailWS(){
        // Show Progress Dialog
        //  prgDialog.show();

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("sysinvorderno", "0" +sysinvorderno);
        paramsMap.put("sysorderdtlno", "0" + sysorderdtlno);

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SearchSalesmen_SalesOrder_Detail", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("invorderdtl");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                    sysinvorderno=jsonObject.getString("sysinvorderno");
                    sysorderdtlno=jsonObject.getString("sysorderdtlno");
                    companycd=jsonObject.getString("companycd");
                    customer_custcd=jsonObject.getString("customer_custcd");
                    walkin_custcd=jsonObject.getString("walkin_custcd");
                    sysbrandno=jsonObject.getString("sysbrandno");
                    sysmodelno=jsonObject.getString("sysmodelno");
                    companyname=jsonObject.getString("companyname");
                    custname=jsonObject.getString("custname");
                    invorderno=jsonObject.getString("invorderno");
                    vinvorderdt=jsonObject.getString("vinvorderdt");
                    brandname=jsonObject.getString("brandname");
                    modelname=jsonObject.getString("modelname");
                    quantity=jsonObject.getString("quantity");

                    serialno=jsonObject.getString("serialno");
                    stickernumber=jsonObject.getString("stickernumber");
                    deliveryinstruction=jsonObject.getString("deliveryinstruction");

                    invordernoET.setText(invorderno);
                    brandnameET.setText(brandname);
                    modelnameET.setText(modelname);
                    serialnoET.setText(serialno);
                    stickernumberET.setText(stickernumber);
                    deliveryinstructionET.setText(deliveryinstruction);

                    invokeWS_SerialStock();

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
                //     prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }


    public void invokeWS_SerialStock() {
        try {

            String editableValue1 = "" + serialnoET.getText().toString();
            String editableValue2 = "" + stickernumberET.getText().toString();

            Editable deliveryinstruction2 = deliveryinstructionET.getText();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("modelcode", sysmodelno);
            paramsMap.put("companycd", companycd);
            paramsMap.put("serialno", editableValue1.toString());
            paramsMap.put("barcodeno", editableValue2.toString());

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            //          header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetModelDeliveryDetails_SerialNos", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.removeAllViews();

                        tabletoplocationstock.setStretchAllColumns(true);
                        tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Salesmen_SalesOrderModelSerial.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Location");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Serial");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Age");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Value);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("products");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Salesmen_SalesOrderModelSerial.this);

                                final String sysproductno = jsonObject.getString("sysproductno");
                                final String sysmodelno = jsonObject.getString("sysmodelno");

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("companyname"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                final TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("balancestock"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);
                                highsLabel_Quantity.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)

                                    {
                                        String serialno = highsLabel_Quantity.getText().toString();

                                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                        final String consignor_companycd  = globalVariable.getcompanycd();
                                        final String sysemployeeno  = globalVariable.getsysemployeeno();
                                        final String userid  = globalVariable.getuserid();

                                        Map<String, String> paramsMap = new HashMap<>();

                                        // When Email Edit View and Password Edit View have values other than Null
                                        if (Utility.isNotNull(sysproductno)) {
                                            // When Email entered is Valid

                                            paramsMap.put("sysproductno", sysproductno);
                                            paramsMap.put("sysorderdtlno", sysorderdtlno);
                                            paramsMap.put("sysmodelno", sysmodelno);
                                            paramsMap.put("serialno", serialno);
                                            paramsMap.put("userid", userid);
                                            paramsMap.put("deliveryinstruction", deliveryinstruction);

                                            invokeUpdateProductInOrderWS(paramsMap);

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Please select the serial number, don't leave any field blank", Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });


                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("delivery_pending"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
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

    public void invokeUpdateProductInOrderWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/UpdateProductInSalesOrder", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
                    JSONObject obj = response;

                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    invokeSearchSalesmen_SalesOrder_DetailWS();
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
        serialnoET.setText("");
        stickernumberET.setText("");
       }

    public void onClick_DeleteSerial(View view) {
        // Get Email Edit View Value

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(sysproductno)) {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            // Put Http parameter usermodelname with value of Email Edit View control
            paramsMap.put("sysinvorderno", "0"+sysinvorderno);
            paramsMap.put("sysorderdtlno", "0"+sysorderdtlno);
            paramsMap.put("sysproductno", "0"+sysproductno);
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
        ApiHelper.post(URL + "Service1.asmx/DeleteSalesmen_SalesOrder_Serial", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
             //   prgDialog.hide();
                try {
                    JSONObject obj = response;

                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    invokeSearchSalesmen_SalesOrder_DetailWS();
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
        if (Utility.isNotNull(sysorderdtlno)) {
            // Put Http parameter usermodelname with value of Email Edit View control
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            paramsMap.put("sysinvorderno", "0"+sysinvorderno);
            paramsMap.put("sysorderdtlno", "0"+sysorderdtlno);
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
        ApiHelper.post(URL + "Service1.asmx/DeleteSalesmen_SalesOrder_Model", paramsMap, new ApiHelper.ApiCallback() {

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
                invokeWS_SerialStock();
            }
        }

        if (requestCode == 8) {

            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                stickernumberET.setText(scanContent);
                invokeWS_SerialStock();
            }
        }


    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(Salesmen_SalesOrderModelSerial.this,StockVerificationModel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysbrandno",sysbrandno);
        startActivityForResult(homeIntent,1);
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Salesmen_SalesOrderModelSerial.this);
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
        TextView textView = new TextView(Salesmen_SalesOrderModelSerial.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Salesmen_SalesOrderModelSerial.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }



    public void invokeUpdate_Remarks_In_SalesOrderWS(View view){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        deliveryinstructionET = (EditText)findViewById(R.id.deliveryinstruction);
        deliveryinstruction = deliveryinstructionET.getText().toString();
        Map<String, String> paramsMap = new HashMap<>();


        paramsMap.put("sysorderdtlno", sysorderdtlno);
        paramsMap.put("deliveryinstruction", deliveryinstruction);
        paramsMap.put("userid", userid);

        invokeUpdateRemarkInOrderProductWS(paramsMap);

    }


    public void invokeUpdateRemarkInOrderProductWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Update_Order_Order_Product", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {
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



}

