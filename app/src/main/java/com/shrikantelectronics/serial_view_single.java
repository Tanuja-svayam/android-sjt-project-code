package com.shrikantelectronics;

import android.app.Activity;
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
import java.util.HashMap;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class serial_view_single extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    EditText txtserialno;
    TextView txtbarcodeno;
    TextView txtstocklocation;
    TextView txttopcategoryname;
    TextView txtparentcategoryname;
    TextView txtproductcategorydesc;
    TextView txtbrandname;
    TextView txtmodelname;
    TextView txtinvetoryage;
    TextView txtrefdocumentno;
    TextView txtvpurchasedt;
    TextView txtstatusdesc;
    TextView txtserial_netlancost;
    TextView txtmrp;
    TextView txtvpp;

    TextView txtvendorname;

    String sysproductno;
    String sysbrandno;
    String sysmodelno;

    String serialno;
    String barcodeno;

    ProgressDialog prgDialog;

    private Button update_sticker_button, submit_addtoorder;
    private Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_view_single);


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        Intent i = getIntent();
        sysproductno = i.getStringExtra("sysproductno");
        serialno = i.getStringExtra("serialno");
        barcodeno = i.getStringExtra("barcodeno");

        // Locate the TextViews in singleitemview.xml
      //  txtserialno = (TextView) findViewById(R.id.serialno);
        txtbarcodeno = (TextView) findViewById(R.id.barcodeno);
        txtstocklocation= (TextView) findViewById(R.id.stocklocation);
        txttopcategoryname = (TextView) findViewById(R.id.topcategoryname);
        txtparentcategoryname = (TextView) findViewById(R.id.parentcategoryname);
        txtproductcategorydesc = (TextView) findViewById(R.id.productcategorydesc);
        txtbrandname = (TextView) findViewById(R.id.brandname);
        txtmodelname = (TextView) findViewById(R.id.modelname);
        txtinvetoryage = (TextView) findViewById(R.id.invetoryage);
        txtrefdocumentno = (TextView) findViewById(R.id.refdocumentno);
        txtvpurchasedt = (TextView) findViewById(R.id.vpurchasedt);
        txtstatusdesc = (TextView) findViewById(R.id.statusdesc);
        txtserial_netlancost = (TextView) findViewById(R.id.serial_netlancost);
        txtmrp= (TextView) findViewById(R.id.mrp);
        txtvpp= (TextView) findViewById(R.id.vpp);
        txtvendorname = (TextView) findViewById(R.id.vendorname);

        txtserialno = (EditText)findViewById(R.id.serialno);
      //  txtserialno.addTextChangedListener(new GenericTextWatcher_ProductSerial(txtserialno));

        txtserialno.setText(serialno);
        txtbarcodeno.setText(barcodeno);
        scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                captureSerialNumber(0);
            }
        });

        if (!sysproductno.equals("0"))
        {
            GetProductData_Single();
        }

    }

    private class GenericTextWatcher_ProductSerial implements TextWatcher {

        private View view;
        private GenericTextWatcher_ProductSerial(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = txtserialno.getText();
            if (editableValue1.length() >= 3) {
                GetProductData_Single_BySerial();
            }
        }
    }

    public void GetProductData_Single(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysproductno", sysproductno);
        paramsMap.put("sysemployeeno", "0" +sysemployeeno);
        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

          //  imageLoader = new ImageLoader(serial_view_single.this);

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSerialDetails_Single_Employee", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;

                        JSONArray new_array = obj.getJSONArray("productsserial_single");

                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sysproductno=jsonObject.getString("sysproductno");
                        sysbrandno=jsonObject.getString("sysbrandno");
                        sysmodelno=jsonObject.getString("sysmodelno");

                        txtserialno.setText(jsonObject.getString("serialno"));
                        txtbarcodeno.setText(jsonObject.getString("barcodeno"));

                        txtstocklocation.setText(jsonObject.getString("stocklocation"));
                        txttopcategoryname.setText(jsonObject.getString("topcategoryname"));
                        txtparentcategoryname.setText(jsonObject.getString("parentcategoryname"));
                        txtproductcategorydesc.setText(jsonObject.getString("productcategorydesc"));
                        txtbrandname.setText(jsonObject.getString("brandname"));
                        txtmodelname.setText(jsonObject.getString("modelname"));

                        final String sSpecification = jsonObject.getString("mSpecification");

                        txtmodelname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sSpecification));
                                startActivity(intent);
                            }
                        });

                        txtinvetoryage.setText(jsonObject.getString("invetoryage"));
                        txtrefdocumentno.setText(jsonObject.getString("refdocumentno"));
                        txtvpurchasedt.setText(jsonObject.getString("vpurchasedt"));
                        txtstatusdesc.setText(jsonObject.getString("statusdesc"));
                        txtserial_netlancost.setText(jsonObject.getString("serial_netlancost"));

                        txtmrp.setText(jsonObject.getString("mrp"));
                        txtvpp.setText(jsonObject.getString("vpp"));

                        txtvendorname.setText(jsonObject.getString("vendorname"));

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
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetProductData_Single_BySerial(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();

        Editable editableValue1 = txtserialno.getText();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("serialnumber", "" +editableValue1);
        paramsMap.put("sysemployeeno", "0" +sysemployeeno);
        invokeWS_Product_BySerial(paramsMap);
    }

    public void invokeWS_Product_BySerial(Map<String, String> paramsMap){
        try {

            //  imageLoader = new ImageLoader(serial_view_single.this);

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSerialDetails_Employee", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;

                        JSONArray new_array = obj.getJSONArray("productsserial");

                        JSONObject jsonObject = new_array.getJSONObject(0);

                        sysproductno=jsonObject.getString("sysproductno");
                        sysbrandno=jsonObject.getString("sysbrandno");
                        sysmodelno=jsonObject.getString("sysmodelno");

                        txtserialno.setText(jsonObject.getString("serialno"));
                        txtbarcodeno.setText(jsonObject.getString("barcodeno"));

                        txtstocklocation.setText(jsonObject.getString("stocklocation"));
                        txttopcategoryname.setText(jsonObject.getString("topcategoryname"));
                        txtparentcategoryname.setText(jsonObject.getString("parentcategoryname"));
                        txtproductcategorydesc.setText(jsonObject.getString("productcategorydesc"));
                        txtbrandname.setText(jsonObject.getString("brandname"));
                        txtmodelname.setText(jsonObject.getString("modelname"));
                        txtinvetoryage.setText(jsonObject.getString("invetoryage"));
                        txtrefdocumentno.setText(jsonObject.getString("refdocumentno"));
                        txtvpurchasedt.setText(jsonObject.getString("vpurchasedt"));
                        txtstatusdesc.setText(jsonObject.getString("statusdesc"));
                        txtserial_netlancost.setText(jsonObject.getString("serial_netlancost"));
                        txtvendorname.setText(jsonObject.getString("vendorname"));
                        txtmrp.setText(jsonObject.getString("mrp"));
                        txtvpp.setText(jsonObject.getString("vpp"));


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
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_search_serialno(View view) {

        Map<String, String> paramsMap = new HashMap<>();
        txtserialno = (EditText) findViewById(R.id.serialno);
        String serialno;
        serialno = txtserialno.getText().toString();

        if (Utility.isNotNull(serialno) ) {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysemployeeno  = globalVariable.getsysemployeeno();

            paramsMap.put("serialnumber", "" +serialno);
            paramsMap.put("sysemployeeno", "0" +sysemployeeno);
            invokeWS_Product_BySerial(paramsMap);

        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(serial_view_single.this);
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
        TextView textView = new TextView(serial_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(serial_view_single.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    public void onClick_update_sticker(View view) {

        txtbarcodeno = (TextView) findViewById(R.id.barcodeno);
        String barcodeno1;

        barcodeno1 = txtbarcodeno.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();
        if (Utility.isNotNull(barcodeno1) && !sysproductno.equals("0") ) {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysemployeeno  = globalVariable.getsysemployeeno();

            paramsMap.put("sysemployeeno", "0"+sysemployeeno);
            paramsMap.put("sysproductno", "0"+sysproductno);
            paramsMap.put("barcodeno1", ""+barcodeno1);

            invokeWSUpdateStickerNumber(paramsMap);

        }
    }

    public void invokeWSUpdateStickerNumber(Map<String, String> paramsMap){
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/UpdateStickerNumber", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                try {
                    // JSON Object
                    JSONObject obj = response;

                    JSONArray new_array = obj.getJSONArray("productsserial_single");

                    JSONObject jsonObject = new_array.getJSONObject(0);

                    txtserialno.setText(jsonObject.getString("serialno"));
                    txtbarcodeno.setText(jsonObject.getString("barcodeno"));
                    txtstocklocation.setText(jsonObject.getString("stocklocation"));
                    txttopcategoryname.setText(jsonObject.getString("topcategoryname"));
                    txtparentcategoryname.setText(jsonObject.getString("parentcategoryname"));
                    txtproductcategorydesc.setText(jsonObject.getString("productcategorydesc"));
                    txtbrandname.setText(jsonObject.getString("brandname"));
                    txtmodelname.setText(jsonObject.getString("modelname"));
                    txtinvetoryage.setText(jsonObject.getString("invetoryage"));
                    txtrefdocumentno.setText(jsonObject.getString("refdocumentno"));
                    txtvpurchasedt.setText(jsonObject.getString("vpurchasedt"));
                    txtstatusdesc.setText(jsonObject.getString("statusdesc"));
                    txtserial_netlancost.setText(jsonObject.getString("serial_netlancost"));
                    txtmrp.setText(jsonObject.getString("mrp"));
                    txtvpp.setText(jsonObject.getString("vpp"));
                    txtvendorname.setText(jsonObject.getString("vendorname"));
                    // NavigateToCustoemerOrder();
                    Toast.makeText(getApplicationContext(), "Sticker Number Updated Successfully..", Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured!", Toast.LENGTH_LONG).show();
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

                // When Http response code other than 404, 500
                            }
        });
    }

    public void onClick_serialno_add_to_Stock(View view) {

        UpdateSerialStatus();

    }

    public void UpdateSerialStatus() {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();
        if(sysproductno != null && !sysproductno.equals("0") )
        {
            paramsMap.put("sysproductno", sysproductno);
            paramsMap.put("userid", userid);
            invokeUpdateInvoiceWS(paramsMap);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please Add Product and Then Create Invoice ", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeUpdateInvoiceWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Update_Serial_Status", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("serial_status");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                    Toast.makeText(getApplicationContext(), jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();

                    InitializedControls();
                   // recreate();


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

    public void onClick_serialno_add_toOrder(View view) {

        txtserialno = (EditText) findViewById(R.id.serialno);
        txtbrandname = (EditText) findViewById(R.id.brandname);
        txtmodelname = (EditText) findViewById(R.id.modelname);

        String serialno = "", brandname = "", modelname = "", vpp= "0", mrp = "0";
        serialno = txtserialno.getText().toString();
        brandname = txtbrandname.getText().toString();
        modelname = txtmodelname.getText().toString();
        vpp = txtvpp.getText().toString();
        mrp = txtmrp.getText().toString();

        if (!sysproductno.equals("0"))  {

            Intent intent = new Intent(serial_view_single.this, Salesmen_SalesOrder.class);

            intent.putExtra("sysproductno", sysproductno);
            intent.putExtra("serialno",serialno);
            intent.putExtra("sysbrandno",sysbrandno);
            intent.putExtra("brandname",brandname);
            intent.putExtra("sysmodelno",sysmodelno);
            intent.putExtra("modelname",modelname);
            intent.putExtra("vpp",vpp);
            intent.putExtra("mrp",mrp);

            startActivity(intent);
            finish();
        }
    }


    private void captureSerialNumber(int requestCode) {
//        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //      scanIntegrator.setRequestCode(requestCode);
        //    scanIntegrator.initiateScan( );

        Intent homeIntent = new Intent(serial_view_single.this, com.shrikantelectronics.BarcodeScanner.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestCode);

    }


    public void InitializedControls()
    {
        txtserialno.setText("");
        txtbarcodeno.setText("");
        txtstocklocation.setText("");
        txttopcategoryname.setText("");
        txtparentcategoryname.setText("");
        txtproductcategorydesc.setText("");
        txtbrandname.setText("");
        txtmodelname.setText("");
        txtinvetoryage.setText("");
        txtrefdocumentno.setText("");
        txtvpurchasedt.setText("");
        txtstatusdesc.setText("");
        txtserial_netlancost.setText("");
        txtmrp.setText("");
        txtvpp.setText("");
        txtvendorname.setText("");
        sysproductno ="0";
        sysbrandno ="0";
        sysmodelno ="0";
        serialno ="";
        barcodeno ="";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                txtserialno.setText(scanContent);
            }
        }


    }
}
