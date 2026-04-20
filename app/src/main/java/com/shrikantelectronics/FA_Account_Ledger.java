package com.shrikantelectronics;

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

public class FA_Account_Ledger extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] sysaccledgerno;
    String[] name;
    String[] address;
    String[] mobile;
    String[] emailid;
    String[] outstanding;
    String[] invoiceno;
    String[] vinvoicedt;
    String[] netinvoiceamt;

    EditText inputSearch_account_ledger ;
    TableLayout tablesalesregister;

    ProgressDialog prgDialog;
    private ListView lv_Account_Ledger ;
    EditText inputSearch_Account_Ledger ;
    ArrayList<HashMap<String, String>> Account_Ledgerlist;
    ListViewAdapter_FA_Ledger adapter;
    ArrayList<FA_Ledger> arraylist = new ArrayList<FA_Ledger>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_account_ledger);
       // GetAccount_Ledger();

        inputSearch_account_ledger = (EditText)findViewById(R.id.inputSearch_account_ledger);
        inputSearch_account_ledger.addTextChangedListener(new FA_Account_Ledger.GenericTextWatcher_Mobile(inputSearch_account_ledger));

    }

    private class GenericTextWatcher_Mobile implements TextWatcher {

        private View view;
        private GenericTextWatcher_Mobile(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            Editable editableValue1 = inputSearch_account_ledger.getText();
            if (editableValue1.length() >= 4) {
                GetAccount_Ledger();
            }
        }
    }

    public void GetAccount_Ledger(){
        try {

            Editable editableValue1 = inputSearch_account_ledger.getText();
            if (editableValue1.length() >= 4) {

                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("SearchTerm", "" +editableValue1);
                invokeWS_Account_Ledger(paramsMap);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeWS_Account_Ledger(Map<String, String> paramsMap){
        try {
            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetAccountLedgerDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {

                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
                        tablesalesregister.removeAllViews();

                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(FA_Account_Ledger.this);

                        TextView highsHeading_accountledgername = initPlainHeaderTextView();
                        highsHeading_accountledgername.setText("Account Ledger");
                        highsHeading_accountledgername.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_accountledgername.setGravity(Gravity.LEFT);

                        tblrowHeading.addView(highsHeading_accountledgername);

                        tablesalesregister.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("account_ledger");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String sysaccledgerno;
                                final String accountledgername;

                                sysaccledgerno=jsonObject.getString("sysaccledgerno");
                                accountledgername=jsonObject.getString("accountledgername");

                                TableRow tblrowLabels = new TableRow(FA_Account_Ledger.this);

                                TextView highsLabel_accountledgername = initPlainTextView(i);
                                highsLabel_accountledgername.setText(jsonObject.getString("accountledgername"));
                                highsLabel_accountledgername.setTypeface(Typeface.DEFAULT);
                                highsLabel_accountledgername.setGravity(Gravity.LEFT);

                                highsLabel_accountledgername.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent intent = new Intent(FA_Account_Ledger.this, FA_Account_Ledger_View.class);

                                            intent.putExtra("sysaccledgerno",sysaccledgerno);
                                            intent.putExtra("accountledgername",accountledgername);
                                            intent.putExtra("fromdate","");
                                            intent.putExtra("todate","");

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    });

                                tblrowLabels.addView(highsLabel_accountledgername);

                                tablesalesregister.addView(tblrowLabels);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
            e.printStackTrace();
        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(FA_Account_Ledger.this);
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
        TextView textView = new TextView(FA_Account_Ledger.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(FA_Account_Ledger.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }
}


