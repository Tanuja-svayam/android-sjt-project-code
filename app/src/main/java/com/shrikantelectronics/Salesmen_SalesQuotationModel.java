package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

public class Salesmen_SalesQuotationModel extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    TableLayout tabletoplocationstock;
    TextView header_toplocationstock;


    EditText locationET ;
    EditText modelnameET ;
    EditText brandnameET ;

    EditText invordernoET;

    String companycd, customer_custcd,walkin_custcd, sysbrandno, sysmodelno, sysinvorderno, sysorderdtlno ;
    String companyname, custname, invorderno, vinvorderdt, brandname, modelname, quantity, unitprice;

    Button search_serial,search_sticker;
    String sSerialNumbere;

    ListViewAdapter adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<Models> arraylist = new ArrayList<Models>();

    String TAG = "StockVerification.java";

    String popUpContents[];
    PopupWindow popupWindowTopCategory;


    List<String> topCategoryList = new ArrayList<String>();

    ListView listViewTopCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesmen_salesquotationmodel);


        locationET = (EditText)findViewById(R.id.location);
        modelnameET = (EditText)findViewById(R.id.modelname);
        brandnameET = (EditText)findViewById(R.id.brandname);

        invordernoET = (EditText)findViewById(R.id.invorderno);


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

        invordernoET.setText(invorderno);
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

              invokeSearchSalesmen_SalesQuotation_DetailWS();

    }

    public void invokeSearchSalesmen_SalesQuotation_DetailWS(){
        // Show Progress Dialog
        //  prgDialog.show();

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("sysinvorderno", "0" +sysinvorderno);
        paramsMap.put("sysorderdtlno", "0" + sysorderdtlno);

        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/SearchSalesmen_Quotation_Detail", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                prgDialog.hide();
                try {

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("quotationdetail");
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


                    invordernoET.setText(invorderno);
                    brandnameET.setText(brandname);
                    modelnameET.setText(modelname);

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

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("sysinvorderno", sysinvorderno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

//            header_toplocationstock= (TextView) findViewById(R.id.header_toplocationstock);
            //          header_toplocationstock.setText(categoryname + " STOCK");

            ApiHelper.post(URL + "Service1.asmx/GetQuotation_Model_LowSide", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {


                        tabletoplocationstock  = (TableLayout) findViewById(R.id.tabletoplocationstock);

                        tabletoplocationstock.setStretchAllColumns(true);
                      //  tabletoplocationstock.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(Salesmen_SalesQuotationModel.this);
                        TextView highsHeading_category = initPlainHeaderTextView();
                        highsHeading_category.setText("Description");
                        highsHeading_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_category.setGravity(Gravity.LEFT);

                        TextView highsHeading_Quantity = initPlainHeaderTextView();
                        highsHeading_Quantity.setText("Qty");
                        highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Quantity.setGravity(Gravity.CENTER);

                        TextView highsheading_Rate = initPlainHeaderTextView();
                        highsheading_Rate.setText("Basic");
                        highsheading_Rate.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Rate.setGravity(Gravity.RIGHT);

                        TextView highsheading_Value = initPlainHeaderTextView();
                        highsheading_Value.setText("Amount");
                        highsheading_Value.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Value.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_category);
                        tblrowHeading.addView(highsHeading_Quantity);
                        tblrowHeading.addView(highsheading_Rate);
                        tblrowHeading.addView(highsheading_Value);

                        tabletoplocationstock.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("lowside");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(Salesmen_SalesQuotationModel.this);

                                final String sysquotlowsideno = jsonObject.getString("sysquotlowsideno");
                                final String syslowsideno = jsonObject.getString("syslowsideno");

                                //  , , sysinvorderno, productcategorydesc, description, quantity, unitprice,  quantity * unitprice amount

                                final TextView highsLabel_category = initPlainTextView(i);
                                highsLabel_category.setText(jsonObject.getString("description"));
                                highsLabel_category.setTypeface(Typeface.DEFAULT);
                                highsLabel_category.setGravity(Gravity.LEFT);

                                final TextView highsLabel_Quantity = initPlainTextView(i);
                                highsLabel_Quantity.setText(jsonObject.getString("quantity"));
                                highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
                                highsLabel_Quantity.setGravity(Gravity.RIGHT);

                                TextView highsLabel_Rate = initPlainTextView(i);
                                highsLabel_Rate.setText(jsonObject.getString("unitprice"));
                                highsLabel_Rate.setTypeface(Typeface.DEFAULT);
                                highsLabel_Rate.setGravity(Gravity.RIGHT);

                                TextView highslabel_Value = initPlainTextView(i);
                                highslabel_Value.setText(jsonObject.getString("amount"));
                                highslabel_Value.setTypeface(Typeface.DEFAULT);
                                highslabel_Value.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_category);
                                tblrowLabels.addView(highsLabel_Quantity);
                                tblrowLabels.addView(highsLabel_Rate);
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
                    invokeSearchSalesmen_SalesQuotation_DetailWS();
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
        ApiHelper.post(URL + "Service1.asmx/DeleteSalesmen_SalesQuotation_Model", paramsMap, new ApiHelper.ApiCallback() {

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

    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(Salesmen_SalesQuotationModel.this);
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
        TextView textView = new TextView(Salesmen_SalesQuotationModel.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(Salesmen_SalesQuotationModel.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }


}

