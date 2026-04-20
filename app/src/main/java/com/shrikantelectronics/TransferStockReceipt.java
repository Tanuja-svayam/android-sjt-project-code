package com.shrikantelectronics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TransferStockReceipt extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    TextView txttransferno;
    TextView txtvtransferdt;
    TextView txtsource_companyname;
    TextView txtdestination_companyname;
    TextView txtcategoryname;
    TextView txtbrandname;
    TextView txtmodelname;
    TextView txtserialno;
    TextView txtbarcodeno;

    String systrfdtlno;
    String systrfno;
    String sysproductno;
    String companycd;
    String serialno;
    String barcodeno;

    private Button update_sticker_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferstockreceipt);

        Intent i = getIntent();

        systrfdtlno = i.getStringExtra("systrfdtlno");
        systrfno = i.getStringExtra("systrfno");
        sysproductno = i.getStringExtra("sysproductno");
        serialno = i.getStringExtra("serialno");
        barcodeno = i.getStringExtra("barcodeno");

        // Locate the TextViews in singleitemview.xml
        txtserialno = (TextView) findViewById(R.id.serialno);
        txtbarcodeno = (TextView) findViewById(R.id.barcodeno);
        txtdestination_companyname= (TextView) findViewById(R.id.destination_companyname);
        txtcategoryname = (TextView) findViewById(R.id.categoryname);
        txtbrandname = (TextView) findViewById(R.id.brandname);
        txtmodelname = (TextView) findViewById(R.id.modelname);
        txttransferno = (TextView) findViewById(R.id.transferno);
        txtvtransferdt = (TextView) findViewById(R.id.vtransferdt);
        txtsource_companyname = (TextView) findViewById(R.id.source_companyname);

        txtserialno.setText(serialno);
        txtbarcodeno.setText(barcodeno);

        GetProductData_Single();

    }

    public void GetProductData_Single(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String companycd  = globalVariable.getcompanycd();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("systrfdtlno","0" + systrfdtlno);
        paramsMap.put("systrfno","0" + systrfno);
        paramsMap.put("sysproductno", "0" +sysproductno);
        paramsMap.put("companycd", "0" +companycd);
        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

          //  imageLoader = new ImageLoader(serial_view_single.this);

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetPendingTransferSerialDetails_Single", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;

                        JSONArray new_array = obj.getJSONArray("products");

                        JSONObject jsonObject = new_array.getJSONObject(0);

                        txttransferno.setText(jsonObject.getString("transferno"));
                        txtvtransferdt.setText(jsonObject.getString("vtransferdt"));
                        txtsource_companyname.setText(jsonObject.getString("source_companyname"));
                        txtdestination_companyname.setText(jsonObject.getString("destination_companyname"));
                        txtcategoryname.setText(jsonObject.getString("categoryname"));
                        txtbrandname.setText(jsonObject.getString("brandname"));
                        txtmodelname.setText(jsonObject.getString("modelname"));
                        txtserialno.setText(jsonObject.getString("serialno"));
                        txtbarcodeno.setText(jsonObject.getString("barcodeno"));

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




    public void onClick_update_sticker(View view) {

        txtserialno = (TextView) findViewById(R.id.serialno);
        String serialno;

        serialno = txtserialno.getText().toString();

        Map<String, String> paramsMap = new HashMap<>();
        if (Utility.isNotNull(serialno) && !sysproductno.equals("0") ) {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String userid  = globalVariable.getuserid();

            paramsMap.put("syshdrno", systrfdtlno);
            paramsMap.put("userid", userid);
            paramsMap.put("authid", "AUTHORISE");
            paramsMap.put("authstatus", "10");
            paramsMap.put("sysauthno", "0");

            invokeWSAuthoriseTransferStock(paramsMap);

        }
    }

    public void invokeWSAuthoriseTransferStock(Map<String, String> paramsMap){
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/Authorise_Location_Transfer_Receipt", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                try {
                    JSONObject obj = response;

                    String sysinvorderno_new = obj.getString("sysinvorderno");

                    if (Integer.valueOf(sysinvorderno_new) > 0)
                    {

                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }

                    finish();
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

                // When Http response code other than 404, 500
                            }
        });
    }

}
