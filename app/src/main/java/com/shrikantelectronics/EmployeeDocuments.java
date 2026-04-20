package com.shrikantelectronics;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.shrikantelectronics.PermissionManager;

public class EmployeeDocuments extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] custname;
    String[] netinvoiceamt;
    String[] vinvoicedt;
    String[] invoiceno;
   // int[] invoiceimage;

    ListViewAdapter_Employee_Documents adapter;

    ProgressDialog prgDialog;
    private ListView lv;
    EditText inputSearch;

    ArrayList<Employee_Documents> arraylist = new ArrayList<Employee_Documents>();

    String sysdocumentno, sysemployeeno  = "0";
    String foldername = "";
    TableLayout tablesalesregister;

    PermissionManager permissionManager;
    DownloadManager downloadManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_document_view);

        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        sysemployeeno  = globalVariable.getsysemployeeno();
        foldername  = globalVariable.getemployeename();
        GetProductData();
    }

    public void GetProductData(){
        //EditText inputSearch1;
        //inputSearch1 = (EditText) findViewById(R.id.inputSearch);

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("sysemployeeno", "0"+sysemployeeno);

        invokeWS_Product(paramsMap);
    }


    public void invokeWS_Product(Map<String, String> paramsMap){
        try {

            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetEmployeeDocumentsDetails", paramsMap, new ApiHelper.ApiCallback()   {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);

                        tablesalesregister.removeAllViews();
                        tablesalesregister.setStretchAllColumns(true);
                        tablesalesregister.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(EmployeeDocuments.this);
                        TextView highsHeading_documentname = initPlainHeaderTextView();
                        highsHeading_documentname.setText("Document Name");
                        highsHeading_documentname.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_documentname.setGravity(Gravity.LEFT);

                        TextView highsHeading_validupto = initPlainHeaderTextView();
                        highsHeading_validupto.setText("Valid Upto");
                        highsHeading_validupto.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_validupto.setGravity(Gravity.LEFT);

                        TextView highsHeading_downoad = initPlainHeaderTextView();
                        highsHeading_downoad.setText("");
                        highsHeading_downoad.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_downoad.setGravity(Gravity.LEFT);

                        TextView highsHeading_delete = initPlainHeaderTextView();
                        highsHeading_delete.setText("");
                        highsHeading_delete.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_delete.setGravity(Gravity.CENTER);

                        tblrowHeading.addView(highsHeading_documentname);
                        tblrowHeading.addView(highsHeading_validupto);
                        tblrowHeading.addView(highsHeading_downoad);
                        tblrowHeading.addView(highsHeading_delete);

                        tablesalesregister.addView(tblrowHeading);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("employee_documents");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                final String sysdocumentno1;
                                final String url;

                                sysdocumentno1=jsonObject.getString("sysdocumentno");
                                url=jsonObject.getString("documenturl");

                                TableRow tblrowLabels = new TableRow(EmployeeDocuments.this);

                                TextView highsLabel_documentname = initPlainTextView(i);
                                highsLabel_documentname.setText(jsonObject.getString("document_name"));
                                highsLabel_documentname.setTypeface(Typeface.DEFAULT);
                                highsLabel_documentname.setGravity(Gravity.LEFT);

                                TextView highsLabel_validupto= initPlainTextView(i);
                                highsLabel_validupto.setText(jsonObject.getString("vvaliddate"));
                                highsLabel_validupto.setTypeface(Typeface.DEFAULT);
                                highsLabel_validupto.setGravity(Gravity.LEFT);

                                ImageButton highsLabel_downoad = new ImageButton(EmployeeDocuments.this);
                                highsLabel_downoad.setImageResource(R.drawable.download);
                                highsLabel_downoad.setBackgroundColor(getResources().getColor(R.color.white) );
                                   highsLabel_downoad.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)

                                    {
                                        if (checkPermission()) {
                                            //Get the URL entered
                                            new DownloadFile(EmployeeDocuments.this).execute(url);
                                        } else {
                                        }
                                    }
                                });

                                ImageButton highsLabel_delete = new ImageButton(EmployeeDocuments.this);
                                highsLabel_delete.setImageResource(R.drawable.delete);
                                highsLabel_delete.setBackgroundColor(getResources().getColor(R.color.white) );
                                highsLabel_delete.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        invoke_Delete_WS(sysdocumentno1);
                                    }
                                });

                                tblrowLabels.addView(highsLabel_documentname);
                                tblrowLabels.addView(highsLabel_validupto);
                                tblrowLabels.addView(highsLabel_downoad);
                                tblrowLabels.addView(highsLabel_delete);

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

                }

            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void invoke_Delete_WS(String sysdocumentno){
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object


        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("sysdocumentno", "0" + sysdocumentno);

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/DeleteEmployeeDocument", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
                    JSONObject obj = response;
                    Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    GetProductData();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_employee_document, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_employee_document) {
            navigatetoAdd_Employee_Document_Activity();

        }
        return super.onOptionsItemSelected(item);
    }



    public void navigatetoAdd_Employee_Document_Activity(){

        foldername=foldername.replace("'","");
        foldername=foldername.replace(".","");
        foldername=foldername.replace(" ","_");
        foldername=foldername.replace("/","");
        foldername=foldername.replace("-","");
        foldername=foldername.replace("\\","");
        foldername=foldername.replace("//","");

        Intent homeIntent = new Intent(EmployeeDocuments.this,GalaryImageActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra("sysdocumentno",  "0");
        homeIntent.putExtra("sysemployeeno",sysemployeeno);
        homeIntent.putExtra("foldername",foldername);
        startActivityForResult(homeIntent,23);
    }


    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(EmployeeDocuments.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    private TextView initPlainHeaderTextView() {
        TextView textView = new TextView(EmployeeDocuments.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(EmployeeDocuments.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private static class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        Context context;

        DownloadFile(Context c) {
            context = c;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                java.net.URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "employee_download/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("xxx", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded : " + fileName + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}


