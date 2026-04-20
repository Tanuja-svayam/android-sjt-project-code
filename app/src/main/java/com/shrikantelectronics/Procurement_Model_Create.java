package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

public class Procurement_Model_Create extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog  prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    EditText item_aliasET;
    EditText modelnameET ;
    EditText brandnameET ;
    EditText topcategoryET;
    EditText parentcategoryET;
    EditText productcategoryET;
    EditText shorthsncodeET;
    EditText hsncodeET;
    String sysbrandno,sysmodelno,sysproductcategoryno_top,sysproductcategoryno_parent,sysproductcategoryno;

    ListViewAdapter adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<Models> arraylist = new ArrayList<Models>();

    String TAG = "";
    Button Button_topcategory,  Button_parentcategory, Button_productcategory, Button_brandname;

    List<String> topCategoryList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_model_create);

        item_aliasET= (EditText)findViewById(R.id.item_alias);
        modelnameET = (EditText)findViewById(R.id.modelname);
        brandnameET = (EditText)findViewById(R.id.brandname);
        topcategoryET = (EditText)findViewById(R.id.topcategory);
        parentcategoryET = (EditText)findViewById(R.id.parentcategory);
        productcategoryET = (EditText)findViewById(R.id.productcategory);
        shorthsncodeET = (EditText)findViewById(R.id.shorthsncode);
        hsncodeET = (EditText)findViewById(R.id.hsncode);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        // prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        // prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        // prgDialog.setCancelable(false);

        OnClickListener handler = new OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

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
                }
            }
        };

        Button_topcategory = (Button) findViewById(R.id.Button_topcategory);
        Button_topcategory.setOnClickListener(handler);

        Button_parentcategory = (Button) findViewById(R.id.Button_parentcategory);
        Button_parentcategory.setOnClickListener(handler);

        Button_productcategory = (Button) findViewById(R.id.Button_productcategory);
        Button_productcategory.setOnClickListener(handler);

        Button_brandname = (Button) findViewById(R.id.Button_brandname);
        Button_brandname.setOnClickListener(handler);

    }

    public void onClick_submit(View view) {
        // Get Email Edit View Value

        String item_alias = "";

        item_alias = item_aliasET.getText().toString();
        String modelname = modelnameET.getText().toString();
        String brandname = brandnameET.getText().toString();
        String topcategory = topcategoryET.getText().toString();
        String parentcategory = parentcategoryET.getText().toString();
        String productcategory = productcategoryET.getText().toString();
        String shorthsncode = shorthsncodeET.getText().toString();
        String hsncode = hsncodeET.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();

        // When Email Edit View and Password Edit View have values other than Null
        if (Utility.isNotNull(modelname) && Utility.isNotNull(brandname) && Utility.isNotNull(topcategory) && Utility.isNotNull(parentcategory) && Utility.isNotNull(productcategory)) {
                // Put Http parameter usermodelname with value of Email Edit View control

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            paramsMap.put("sysmodelno", "0");
            paramsMap.put("modelname", ""+modelname);
            paramsMap.put("item_alias", ""+item_alias);
            paramsMap.put("sysbrandno", "0"+sysbrandno);
            paramsMap.put("sysproductcategoryno_top", "0"+sysproductcategoryno_top);
            paramsMap.put("sysproductcategoryno_parent", "0"+sysproductcategoryno_parent);
            paramsMap.put("sysproductcategoryno", "0"+sysproductcategoryno);
            paramsMap.put("shorthsncode", ""+shorthsncode);
            paramsMap.put("hsncode", ""+hsncode);
            paramsMap.put("userid", ""+userid);

            invokeWS(paramsMap);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS(Map<String, String> paramsMap){
        // Show Progress Dialog
        // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER -- AddWalkinCustomer
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddProcurementNewModel", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                // prgDialog.hide();
                try {
                    // JSON Object

                    setDefaultValues();

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("newmodeldetails");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                        Intent intent = new Intent(Procurement_Model_Create.this, Procurement.class);

                    intent.putExtra("sysbrandno",jsonObject.getString("sysbrandno"));
                    intent.putExtra("brandname",jsonObject.getString("brandname"));

                    intent.putExtra("sysmodelno",jsonObject.getString("sysmodelno"));
                        intent.putExtra("modelcode",jsonObject.getString("modelcode"));

                        ((Procurement_Model_Create)Procurement_Model_Create.this).setResult(Procurement_Model_Create.RESULT_OK,intent);
                        ((Procurement_Model_Create)Procurement_Model_Create.this).finish();

                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Model Created Successfully...!", Toast.LENGTH_LONG).show();
                        //navigatetoFollowupActivity();

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
                // prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    public void setDefaultValues(){

        //modelnameET.setText("");
        //locationET.setText("");
        //brandnameET.setText("");
        //topcategoryET.setText("");
        //parentcategoryET.setText("");
        //productcategoryET.setText("");
        //companycd = "0";
        //sysproductcategoryno_top = "0";
        //sysproductcategoryno_parent = "0";
        //sysproductcategoryno = "0";
        //sysbrandno = "0";
        //sysmodelno = "0";

    }

    public void setResetValues(){

        modelnameET.setText("");
        brandnameET.setText("");
        topcategoryET.setText("");
        parentcategoryET.setText("");
        productcategoryET.setText("");
        shorthsncodeET.setText("");
        hsncodeET.setText("");

        sysproductcategoryno_top = "0";
        sysproductcategoryno_parent = "0";
        sysproductcategoryno = "0";
        sysbrandno = "0";
        sysmodelno = "0";

    }

    public void onClick_Reset(View view) {
        setResetValues();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
    }

    public void navigatetoBrandListActivity(){
        Intent homeIntent = new Intent(Procurement_Model_Create.this,StockVerificationBrand_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,5);
    }

    public void navigatetoTopCategoryListActivity(){
        Intent homeIntent = new Intent(Procurement_Model_Create.this,StockVerificationTopCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,2);
    }

    public void navigatetoParentCategoryListActivity(){
        Intent homeIntent = new Intent(Procurement_Model_Create.this,StockVerificationParentCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("under",sysproductcategoryno_top);
        startActivityForResult(homeIntent,3);
    }

    public void navigatetoProductCategoryListActivity(){
        Intent homeIntent = new Intent(Procurement_Model_Create.this,StockVerificationProductCategory_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("under",sysproductcategoryno_parent);
        startActivityForResult(homeIntent,4);
    }
}

