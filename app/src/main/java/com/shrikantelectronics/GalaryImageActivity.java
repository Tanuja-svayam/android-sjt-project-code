package com.shrikantelectronics;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import android.util.Base64;

public class GalaryImageActivity extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    private ProgressBar progressBar;
    long totalSize = 0;
    public String foldername ="";
    // One Button
    Button BSelectImage, btnUpload;
    private static final String TAG = MainActivity.class.getSimpleName();
    // One Preview Image
    ImageView IVPreviewImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    private static String imageStoragePath;
    String sysemployeeno = "0";
    String sysdocumentno  = "0";
    TextView validdateET;
    EditText employeenameET ;
    EditText document_nameET;
    EditText uploaddateET ;
    EditText file_nameET ;

    int year, month, day;


    DownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaryimageactivity);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        employeenameET = (EditText)findViewById(R.id.employeename);
        document_nameET = (EditText)findViewById(R.id.document_name);
        uploaddateET = (EditText)findViewById(R.id.uploaddate);
        validdateET = (TextView)findViewById(R.id.validdate);
        file_nameET = (EditText)findViewById(R.id.file_name);

        // register the UI widgets with their appropriate IDs
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);


        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        btnUpload= (Button)findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchUploadActivity(true);
            }
        });

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        employeenameET.setText(globalVariable.getemployeename());
        uploaddateET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        validdateET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));

        Intent i = getIntent();
        sysdocumentno= i.getStringExtra("sysdocumentno");
        sysemployeeno = i.getStringExtra("sysemployeeno");
        foldername= i.getStringExtra("foldername");

        if(sysdocumentno == null){
            sysdocumentno="0";
        }


    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    String filePath = "";
                    String wholeID = DocumentsContract.getDocumentId(selectedImageUri);

                    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];

                    String[] column = { MediaStore.Images.Media.DATA };

                    // where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";

                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);

                    int columnIndex = cursor.getColumnIndex(column[0]);

                    if (cursor.moveToFirst()) {
                        imageStoragePath = cursor.getString(columnIndex);
                    }
                    cursor.close();


                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }



    private void launchUploadActivity(boolean isImage){
        // Intent i = new Intent(ServiceCallDocumentImageActivity.this, UploadActivity.class);
        // i.putExtra("filePath",imageStoragePath);
        // i.putExtra("isImage", isImage);
        // startActivity(i);

        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        private String uploadFile() {
            String responseString = null;

            File sourceFile = new File(imageStoragePath);
            if (!sourceFile.exists()) {
                return "File not found: " + imageStoragePath;
            }

            try {
                // Create multipart request body
                RequestBody fileBody = RequestBody.create(sourceFile, MediaType.parse("image/*"));

                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", sourceFile.getName(), fileBody)
                        .addFormDataPart("foldername", foldername)
                        .addFormDataPart("email", "abc@gmail.com")
                        .build();

                // Build request
                Request request = new Request.Builder()
                        .url(Config.EMPLOYEE_DOCUMENT_UPLOAD_URL)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    responseString = response.body().string();
                } else {
                    responseString = "Error occurred! Http Status Code: " + response.code();
                }

            } catch (IOException e) {
                responseString = "Upload failed: " + e.getMessage();
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            showAlert(result);
            invokeWS();
            finish();

            super.onPostExecute(result);
        }
    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public void invokeWS(){
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object

       final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        final String document_name  = document_nameET.getText().toString();
        final String validdate  = validdateET.getText().toString();
        final String userid  = globalVariable.getuserid();

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("sysdocumentno", "0" + sysdocumentno);
        paramsMap.put("sysemployeeno", "0" + sysemployeeno );
        paramsMap.put("document_name", "" + document_name);
        paramsMap.put("validdate",validdate);
        paramsMap.put("userid", "" + userid);
        paramsMap.put("latitude", "");
        paramsMap.put("longitude","" );
        paramsMap.put("fileuri", "" + imageStoragePath);
        paramsMap.put("foldername", "" + foldername);

        //STARWING -- SERVER
        //AsyncHttpClient client = new AsyncHttpClient();
        ApiHelper.post(URL + "Service1.asmx/AddEmployeeDocument", paramsMap, new ApiHelper.ApiCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                // Hide Progress Dialog

                try {
                    // JSON Object
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
                //  prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setvaliddate(View view) {
        showDialog(1);
        Toast.makeText(getApplicationContext(), "Document Valid Date",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

        if (id == 1) {
            return new DatePickerDialog(this,
                    validdateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener validdateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showValidDate(arg1, arg2, arg3);
                }
            };

    private void showValidDate(int year, int month, int day) {
        validdateET = (TextView) findViewById(R.id.validdate);
        validdateET.setText(Utility.ConvetToDDMMMYYYY(year,month,day));
        // invokeWS_Customer_Ledger(custcd);
    }

    public void invokeDownloadWS(View view){
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        final String document_name  = document_nameET.getText().toString();
        final String validdate  = validdateET.getText().toString();
        final String userid  = globalVariable.getuserid();

        final EditText url = (EditText) view.findViewById(R.id.file_name);

        Map<String, String> paramsMap = new HashMap<>();

            //check if app has permission to write to the external storage.
            if (checkPermission()) {
                //Get the URL entered
                new DownloadFile(this).execute(url.getText().toString());
            } else {

            }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       // permissionManager.checkResult(requestCode, permissions, grantResults);
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
                folder = Environment.getExternalStorageDirectory() + File.separator + "abir_download/";

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
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

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
