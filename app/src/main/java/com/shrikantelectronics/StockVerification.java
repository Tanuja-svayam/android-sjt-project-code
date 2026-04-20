package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class StockVerification extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText serialnoET ;
    EditText locationET ;
    EditText modelnameET ;
    EditText brandnameET ;
    EditText topcategoryET;
    EditText parentcategoryET;
    EditText productcategoryET;
    EditText stickernumberET;

    EditText lastserialno;
    EditText laststickernumber;

    String sysrecono;
    String companycd,sysproductno,sysbrandno,sysmodelno,sysproductcategoryno_top,sysproductcategoryno_parent,sysproductcategoryno="0";

    Button search_serial;
    String sSerialNumbere;

    ListViewAdapter adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<Models> arraylist = new ArrayList<Models>();

    String TAG = "StockVerification.java";

    String popUpContents[];
    PopupWindow popupWindowTopCategory;
    Button Button_topcategory, Button_modelname, Button_parentcategory, Button_productcategory, Button_brandname;
    Button Button_location;
    private Button scanBtn, scan_button_sticker;
    List<String> topCategoryList = new ArrayList<String>();

    ListView listViewTopCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockverification);

        sysrecono = "0";
        sysproductno = "0";

        serialnoET = (EditText)findViewById(R.id.serialno);
        locationET = (EditText)findViewById(R.id.location);
        modelnameET = (EditText)findViewById(R.id.modelname);
        brandnameET = (EditText)findViewById(R.id.brandname);
        topcategoryET = (EditText)findViewById(R.id.topcategory);
        parentcategoryET = (EditText)findViewById(R.id.parentcategory);
        productcategoryET = (EditText)findViewById(R.id.productcategory);
        stickernumberET = (EditText)findViewById(R.id.stickernumber);

        lastserialno= (EditText)findViewById(R.id.lastserialno);
        laststickernumber= (EditText)findViewById(R.id.laststickernumber);

        Intent i = getIntent();

       // sysrecono = i.getStringExtra("sysrecono");

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                captureSerialNumber(7);
            }
        });

        scan_button_sticker = (Button)findViewById(R.id.scan_button_sticker);

        scan_button_sticker.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        captureSerialNumber(8);
                    }
                });

        search_serial= (Button) findViewById(R.id.search_serial);
        search_serial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                sSerialNumbere = serialnoET.getText().toString();
                Invoke_ProductSerialDetails(sSerialNumbere);
            }
        });
        // button on click listener

        OnClickListener handler = new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.Button_modelname:
                        navigatetoModelListActivity();
                        break;
                    case R.id.Button_topcategory:
                        navigatetoTopCategoryListActivity();
                        break;

                    case R.id.Button_parentcategory:
                        navigatetoParentCategoryListActivity();
                        break;

                    case R.id.Button_productcategory:
                        navigatetoProductCategoryListActivity();
                        break;

                    case R.id.Button_brandname:
                        navigatetoBrandListActivity();
                        break;

                    case R.id.Button_location:
                        navigatetoLocationListActivity();
                        break;
                }
            }
        };

        // our button
        Button_modelname = (Button) findViewById(R.id.Button_modelname);
        Button_modelname.setOnClickListener(handler);

        Button_topcategory = (Button) findViewById(R.id.Button_topcategory);
        Button_topcategory.setOnClickListener(handler);

        Button_parentcategory = (Button) findViewById(R.id.Button_parentcategory);
        Button_parentcategory.setOnClickListener(handler);

        Button_productcategory = (Button) findViewById(R.id.Button_productcategory);
        Button_productcategory.setOnClickListener(handler);

        Button_brandname = (Button) findViewById(R.id.Button_brandname);
        Button_brandname.setOnClickListener(handler);

        Button_location = (Button) findViewById(R.id.Button_location);
        Button_location.setOnClickListener(handler);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_stockverificationl, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.btnSubmit) {

            try {

                onClick_submit();

            } catch (ActivityNotFoundException a) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT);
                t.show();
            }

            return true;
        }

        if (id == R.id.action_New) {

            try {

                onClick_reset();

            } catch (ActivityNotFoundException a) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT);
                t.show();
            }

            return true;
        }



        return super.onOptionsItemSelected(item);
    }



    private void captureSerialNumber(int requestCode) {
     //   IntentIntegrator scanIntegrator = new IntentIntegrator(this);
      //  scanIntegrator.initiateScan( );

        Intent homeIntent = new Intent(StockVerification.this, com.shrikantelectronics.BarcodeScanner.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestCode);
     }

    public void Invoke_ProductSerialDetails(String sSerialNo) {
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("serialno", "" +  sSerialNo );

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetProductDetailsBySerialNumber", paramsMap, new ApiHelper.ApiCallback()   {
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
                        locationET.setText(jsonObject.getString("stocklocation"));
                        brandnameET.setText(jsonObject.getString("brandname"));
                        topcategoryET.setText(jsonObject.getString("topcategoryname"));
                        parentcategoryET.setText(jsonObject.getString("parentcategoryname"));
                        productcategoryET.setText(jsonObject.getString("productcategorydesc"));
                            stickernumberET.setText(jsonObject.getString("stickernumber"));


                        sysproductno = jsonObject.getString("sysproductno");
                        companycd = jsonObject.getString("companycd");
                        sysproductcategoryno_top = jsonObject.getString("sysproductcategoryno_top");
                        sysproductcategoryno_parent = jsonObject.getString("sysproductcategoryno_parent");
                        sysproductcategoryno = jsonObject.getString("sysproductcategoryno");
                        sysbrandno = jsonObject.getString("sysbrandno");
                        sysmodelno = jsonObject.getString("sysmodelno");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "SERIAL NUMBER NOT FOUND", Toast.LENGTH_LONG).show();
                           // setDefaultValues();
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

    public void onClick_submit() {
        // Get Email Edit View Value

        String modelname = modelnameET.getText().toString();
        String serialno = serialnoET.getText().toString();
        String location = locationET.getText().toString();
        String brandname = brandnameET.getText().toString();
        String topcategory = topcategoryET.getText().toString();
        String parentcategory= parentcategoryET.getText().toString();
        String productcategory= productcategoryET.getText().toString();
        String stickernumber= stickernumberET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(serialno) && Utility.isNotNull(modelname) && Utility.isNotNull(location) && Utility.isNotNull(brandname) && Utility.isNotNull(topcategory) && Utility.isNotNull(parentcategory) && Utility.isNotNull(productcategory) ) {
                // Put Http parameter usermodelname with value of Email Edit View control


            paramsMap.put("sysrecodtlno", "0");
            paramsMap.put("sysrecono", "0"+sysrecono);
                paramsMap.put("serialno", "" + serialno);
                paramsMap.put("companycd", "0"+companycd);
                paramsMap.put("sysproductno", "0"+sysproductno);
                paramsMap.put("sysmodelno", "0"+sysmodelno);
                paramsMap.put("sysbrandno", "0"+sysbrandno);
                paramsMap.put("sysproductcategoryno_top", "0"+sysproductcategoryno_top);
                paramsMap.put("sysproductcategoryno_parent", "0"+sysproductcategoryno_parent);
                paramsMap.put("sysproductcategoryno", "0"+sysproductcategoryno);
                paramsMap.put("stickernumber", "" + stickernumber);

               invokeWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }


    public void onClick_reset() {
        // Get Email Edit View Value
        serialnoET.setText("");
        stickernumberET.setText("");
          modelnameET.setText("");
          locationET.setText("");
          brandnameET.setText("");
          topcategoryET.setText("");
         parentcategoryET.setText("");
          productcategoryET.setText("");

         sysproductno = "0";
        companycd = "0";
        sysproductcategoryno_top = "0";
         sysproductcategoryno_parent = "0";
         sysproductcategoryno = "0";
         sysbrandno = "0";
         sysmodelno = "0";

        lastserialno.setText("");
        laststickernumber.setText("");


    }


    public void invokeWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddStockverificationProduct", paramsMap, new ApiHelper.ApiCallback() {

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
                        Toast.makeText(getApplicationContext(), "Product Verified Successfully...!", Toast.LENGTH_LONG).show();
                        //navigatetoFollowupActivity();
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
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    public void setDefaultValues(){


        String stickernumber = "";
        String serialno = "";

        serialno =  stickernumberET.getText().toString();
        stickernumber =  serialnoET.getText().toString();

        lastserialno.setText(serialno);
        laststickernumber.setText(stickernumber);

        serialnoET.setText("");
        sysproductno = "0";

        stickernumber = "";
        serialno = "";

        String stickerprefix = "";


        //stickerprefix =  stickernumberET.getText().toString().substring(0,4);

        //stickernumber =  stickernumberET.getText().toString().substring(5,9);

     //   int dstickernumber = 0;
      //  dstickernumber = Integer.parseInt(stickernumber);

    //    dstickernumber = dstickernumber + 1;

      //  stickernumber = String.format("%05d", dstickernumber);

        //stickernumberET.setText(stickerprefix +stickernumber );

      //  modelnameET.setText("");
      //  locationET.setText("");
      //  brandnameET.setText("");
      //  topcategoryET.setText("");
       // parentcategoryET.setText("");
      //  productcategoryET.setText("");

       // sysproductno = "0";
        //companycd = "0";
        //sysproductcategoryno_top = "0";
       // sysproductcategoryno_parent = "0";
       // sysproductcategoryno = "0";
       // sysbrandno = "0";
       // sysmodelno = "0";

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                // String scanFormat = scanningResult.getFormatName();
                //formatTxt.setText("FORMAT: " + scanFormat);
                serialnoET.setText(scanContent);
                sSerialNumbere = serialnoET.getText().toString();
                Invoke_ProductSerialDetails(sSerialNumbere);
            }
        }

        if (requestCode == 8) {

            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                stickernumberET.setText(scanContent);
            }

        }

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysmodelno = bundle.getString("sysmodelno");
                modelnameET.setText(bundle.getString("modelcode"));

                sysproductcategoryno_top = bundle.getString("sysproductcategoryno_top");
                topcategoryET.setText(bundle.getString("topcategoryname"));

                sysproductcategoryno_parent = bundle.getString("sysproductcategoryno_parent");
                parentcategoryET.setText(bundle.getString("parentcategoryname"));

                sysproductcategoryno = bundle.getString("sysproductcategoryno");
                productcategoryET.setText(bundle.getString("categoryname"));

            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysproductcategoryno_top = bundle.getString("sysproductcategoryno");
                topcategoryET.setText(bundle.getString("description"));
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysproductcategoryno_parent = bundle.getString("sysproductcategoryno");
                parentcategoryET.setText(bundle.getString("description"));
            }
        }

        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysproductcategoryno = bundle.getString("sysproductcategoryno");
                productcategoryET.setText(bundle.getString("description"));
            }
        }

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                sysbrandno = bundle.getString("sysbrandno");
                brandnameET.setText(bundle.getString("description"));
            }
        }

        if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();
                companycd = bundle.getString("companycd");
                locationET.setText(bundle.getString("companyname"));
            }
        }
    }

    public void navigatetoModelListActivity(){
        Intent homeIntent = new Intent(StockVerification.this,StockVerificationModel_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysbrandno",sysbrandno);
        startActivityForResult(homeIntent,1);
    }

    public void navigatetoTopCategoryListActivity(){
        Intent homeIntent = new Intent(StockVerification.this,StockVerificationTopCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,2);
    }

    public void navigatetoParentCategoryListActivity(){
        Intent homeIntent = new Intent(StockVerification.this,StockVerificationParentCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("under",sysproductcategoryno_top);
        startActivityForResult(homeIntent,3);
    }

    public void navigatetoProductCategoryListActivity(){
        Intent homeIntent = new Intent(StockVerification.this,StockVerificationProductCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("under",sysproductcategoryno_parent);
        startActivityForResult(homeIntent,4);
    }

    public void navigatetoBrandListActivity(){
        Intent homeIntent = new Intent(StockVerification.this,StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }

    public void navigatetoLocationListActivity(){
        Intent homeIntent = new Intent(StockVerification.this,StockVerificationLocation_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("trftype","TO");
        startActivityForResult(homeIntent,6);
    }
}

