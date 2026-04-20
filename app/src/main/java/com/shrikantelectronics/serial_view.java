package com.shrikantelectronics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.util.Locale;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class serial_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] modelcode;
    String[] mrp;
    String[] dp;
    String[] stock;
   // int[] modelimage;

    ProgressDialog prgDialog;
    ListViewAdapter adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;
    EditText inputSearch_serialET ;
    TableLayout tablesalesregister;

    ArrayList<Models> arraylist = new ArrayList<Models>();

    protected static final int RESULT_SPEECH = 1;
    public ArrayList<String> text;

    public static TextToSpeech toSpeech;
    public int result;

    String name = "";
    public static boolean speachFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_view);

        inputSearch_serialET = (EditText)findViewById(R.id.inputsearch_serial);
        inputSearch_serialET.addTextChangedListener(new GenericTextWatcher_ProductSerial(inputSearch_serialET));

      //  GetProductData();☺

    }

    private class GenericTextWatcher_ProductSerial implements TextWatcher {

        private View view;
        private GenericTextWatcher_ProductSerial(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = inputSearch_serialET.getText();
            if (editableValue1.length() >= 3) {
                GetProductData();
            }
        }
    }

    public void GetProductData(){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String sysemployeeno  = globalVariable.getsysemployeeno();

        Editable editableValue1 = inputSearch_serialET.getText();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("serialnumber", "" +editableValue1);
        paramsMap.put("sysemployeeno", "0" +sysemployeeno);

        invokeWS_Product(paramsMap);
    }

    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetSerialDetails_Employee", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {

                    tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
                    tablesalesregister.removeAllViews();

                    tablesalesregister.setStretchAllColumns(true);
                    tablesalesregister.setShrinkAllColumns(true);

                    TableRow tblrowHeading = new TableRow(serial_view.this);

                    TextView highsHeading_serialnumber = initPlainHeaderTextView();
                    highsHeading_serialnumber.setText("Serial Number");
                    highsHeading_serialnumber.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_serialnumber.setGravity(Gravity.LEFT);

                    TextView highsHeading_sticker = initPlainHeaderTextView();
                    highsHeading_sticker.setText("Sticker");
                    highsHeading_sticker.setTypeface(Typeface.DEFAULT_BOLD);
                    highsHeading_sticker.setGravity(Gravity.LEFT);

                    tblrowHeading.addView(highsHeading_serialnumber);
                    tblrowHeading.addView(highsHeading_sticker);

                    tablesalesregister.addView(tblrowHeading);

                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("productsserial");

                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);

                            final String sysproductno;
                            final String serialno;
                            final String barcodeno;

                            sysproductno=jsonObject.getString("sysproductno");
                            serialno=jsonObject.getString("serialno");
                            barcodeno=jsonObject.getString("barcodeno");

                            TableRow tblrowLabels = new TableRow(serial_view.this);

                            TextView highsLabel_serialnumber = initPlainTextView(i);
                            highsLabel_serialnumber.setText(jsonObject.getString("serialno"));
                            highsLabel_serialnumber.setTypeface(Typeface.DEFAULT);
                            highsLabel_serialnumber.setGravity(Gravity.LEFT);
                            highsLabel_serialnumber.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent = new Intent(serial_view.this, serial_view_single.class);
                                    intent.putExtra("sysproductno",sysproductno);
                                    intent.putExtra("serialno",serialno);
                                    intent.putExtra("barcodeno",barcodeno);
                                    startActivity(intent);
                                }
                            });

                            TextView highsLabel_sticker= initPlainTextView(i);
                            highsLabel_sticker.setText(jsonObject.getString("barcodeno"));
                            highsLabel_sticker.setTypeface(Typeface.DEFAULT);
                            highsLabel_sticker.setGravity(Gravity.LEFT);
                            highsLabel_sticker.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Intent intent = new Intent(serial_view.this, serial_view_single.class);
                                        intent.putExtra("sysproductno",sysproductno);
                                        intent.putExtra("serialno",serialno);
                                        intent.putExtra("barcodeno",barcodeno);
                                        startActivity(intent);
                                    }
                                });

                            tblrowLabels.addView(highsLabel_serialnumber);
                            tblrowLabels.addView(highsLabel_sticker);

                            tablesalesregister.addView(tblrowLabels);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //    LinearLayout sv = new LinearLayout(MainActivity.this);

                    //        sv.addView(table);

                    //hsw.addView(sv);
                    //setContentView(hsw);

                    // setContentView(table);

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
        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(serial_view.this);
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
        TextView textView = new TextView(serial_view.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(serial_view.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    //SETS UP THE MENU ON THE ACTIONBAR//
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_serial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.btnQrScanner) {

            try {

                captureSerialNumber(0);

            } catch (ActivityNotFoundException a) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT);
                t.show();
            }

            return true;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnSpeak) {

            try {
                startActivityForResult(intent, RESULT_SPEECH);
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
        //IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //scanIntegrator.setRequestCode(requestCode);
        //scanIntegrator.initiateScan();

        Intent homeIntent = new Intent(serial_view.this, com.shrikantelectronics.BarcodeScanner.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(homeIntent,requestCode);

    }

    //  THIS TAKES THE SPEECH AND PUTS IT IN A ARRAY LIST AND THEN DISPLAYS IT IN A TOAST FOR NOW//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanContent = bundle.getString("barcodeData");
                inputSearch_serialET.setText(scanContent);
                Editable editableValue1 = inputSearch_serialET.getText();
                GetProductData();
            }
        }


        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                        Toast.makeText(getApplicationContext(), "Feature not supported in your deavice", Toast.LENGTH_LONG).show();
                    else
                    {
                        Log.i("voice", text.get(0));
                        Toast.makeText(serial_view.this,text.get(0), Toast.LENGTH_LONG).show();

                        inputSearch_serialET = (EditText)findViewById(R.id.inputsearch_serial);

                        inputSearch_serialET.setText(text.get(0).toString().replace(" ",""));
                        GetProductData();

                    }

//                    Toast.makeText(this,text.get(0),Toast.LENGTH_LONG).show();
                }
                else
                {

                    //if(text == null)
                    //  toSpeech.speak("That was flames",TextToSpeech.QUEUE_FLUSH,null);
                    //else
                    toSpeech.speak("Sorry I could not hear you",TextToSpeech.QUEUE_FLUSH,null);




                }
                break;
            }
        }
    }

}


