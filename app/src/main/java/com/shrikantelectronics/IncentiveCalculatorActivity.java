package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IncentiveCalculatorActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    TextView dateView;

    EditText sale_gross_valueET ;
    EditText modelnameET ;
    EditText brandnameET ;
    EditText topcategorynameET ;
    EditText mrpET ;
    EditText dpET ;
    EditText marginET ;
    EditText divET ;
    EditText slcET ;
    EditText slab_1_baseET ;
    EditText slab_2_baseET ;
    EditText slab_3_baseET ;
    EditText slab_4_baseET ;
    EditText slab_5_baseET ;
    EditText slab_1_incentiveET ;
    EditText slab_2_incentiveET ;
    EditText slab_3_incentiveET ;
    EditText slab_4_incentiveET ;
    EditText slab_5_incentiveET ;
    String sysmodelno, modelcode;
    private Button calculate_incentive_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incentivecalculator);

        Intent i = getIntent();
        sysmodelno = i.getStringExtra("sysmodelno");
        modelcode = i.getStringExtra("modelcode");

        sale_gross_valueET = (EditText)findViewById(R.id.sale_gross_value);
        modelnameET= (EditText)findViewById(R.id.modelname);
        brandnameET= (EditText)findViewById(R.id.brandname);
        topcategorynameET= (EditText)findViewById(R.id.topcategoryname);

        mrpET = (EditText)findViewById(R.id.mrp);
        dpET = (EditText)findViewById(R.id.dp);
        marginET = (EditText)findViewById(R.id.margin);
        divET = (EditText)findViewById(R.id.div);
        slcET = (EditText)findViewById(R.id.slc);
        slab_1_baseET = (EditText)findViewById(R.id.slab_1_base);
        slab_2_baseET = (EditText)findViewById(R.id.slab_2_base);
        slab_3_baseET = (EditText)findViewById(R.id.slab_3_base);
        slab_4_baseET = (EditText)findViewById(R.id.slab_4_base);
        slab_5_baseET = (EditText)findViewById(R.id.slab_5_base);
        slab_1_incentiveET = (EditText)findViewById(R.id.slab_1_incentive);
        slab_2_incentiveET = (EditText)findViewById(R.id.slab_2_incentive);
        slab_3_incentiveET = (EditText)findViewById(R.id.slab_3_incentive);
        slab_4_incentiveET = (EditText)findViewById(R.id.slab_4_incentive);
        slab_5_incentiveET = (EditText)findViewById(R.id.slab_5_incentive);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Find Email Edit View control by ID

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        calculate_incentive_button= (Button) findViewById(R.id.calculate_incentive_button);

        calculate_incentive_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                invokeDisplayIncentiveWS();
            }
        });
}


    public void invokeDisplayIncentiveWS(){
        // Show Progress Dialog
        //prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object

        Editable editableValue1 = sale_gross_valueET.getText();

        // Initializes the double values and result
        double value1 = 0.0;

        // If the Editable values are not null, obtains their double values by parsing
        if (editableValue1 != null &&  editableValue1.length()>0)
            value1 = Double.parseDouble(editableValue1.toString());
        else
        {
            value1 = 0.0;
        }

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("sysmodelno", "0" + sysmodelno);
        paramsMap.put("salesprice", "0" + value1);

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/GetSalesmenIncentiveCalculator_Salesmen", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog
                //prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("incentive_calculator");
                    JSONObject jsonObject = new_array.getJSONObject(0);

                    sale_gross_valueET.setText(jsonObject.getString("sale_gross_value"));
                    modelnameET.setText(jsonObject.getString("modelname"));
                    brandnameET.setText(jsonObject.getString("brandname"));
                    topcategorynameET.setText(jsonObject.getString("topcategoryname"));
                    mrpET.setText(jsonObject.getString("mrp"));
                    dpET.setText(jsonObject.getString("dp"));
                    marginET.setText(jsonObject.getString("margin"));
                    divET.setText(jsonObject.getString("div"));
                    slcET.setText(jsonObject.getString("slc"));
                    slab_1_baseET.setText(jsonObject.getString("slab_1_base"));
                    slab_2_baseET.setText(jsonObject.getString("slab_2_base"));
                    slab_3_baseET.setText(jsonObject.getString("slab_3_base"));
                    slab_4_baseET.setText(jsonObject.getString("slab_4_base"));
                    slab_5_baseET.setText(jsonObject.getString("slab_5_base"));
                    slab_1_incentiveET.setText(jsonObject.getString("slab_1_incentive"));
                    slab_2_incentiveET.setText(jsonObject.getString("slab_2_incentive"));
                    slab_3_incentiveET.setText(jsonObject.getString("slab_3_incentive"));
                    slab_4_incentiveET.setText(jsonObject.getString("slab_4_incentive"));
                    slab_5_incentiveET.setText(jsonObject.getString("slab_5_incentive"));

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
                //prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }


    public void setDefaultValues(){
        sale_gross_valueET.setText("");
        modelnameET.setText("");
        brandnameET.setText("");
        topcategorynameET.setText("");
        mrpET.setText("");
        dpET.setText("");
        marginET.setText("");
        divET.setText("");
        slcET.setText("");
        slab_1_baseET.setText("");
        slab_2_baseET.setText("");
        slab_4_baseET.setText("");
        slab_5_baseET.setText("");
        slab_1_incentiveET.setText("");
        slab_2_incentiveET.setText("");
        slab_3_incentiveET.setText("");
        slab_4_incentiveET.setText("");
        slab_5_incentiveET.setText("");
    }


}