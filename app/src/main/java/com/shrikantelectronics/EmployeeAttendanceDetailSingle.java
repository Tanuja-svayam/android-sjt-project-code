package com.shrikantelectronics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;




import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeAttendanceDetailSingle extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    private ProgressBar progressBar;
    private TextView txtDescription;
    private ImageView imgPreview;
    private VideoView videoPreview;
    public ImageLoader imageLoader;
    String sysattnno, attnstatus;
    String  latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendancesingledetail);

        Intent i = getIntent();
        sysattnno = i.getStringExtra("sysattnno");
        attnstatus= i.getStringExtra("attnstatus");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        txtDescription = (TextView) findViewById(R.id.txt_desc);
        imgPreview =(ImageView) findViewById(R.id.imgPreview);
        videoPreview = (VideoView)findViewById(R.id.videoPreview);

        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGoogleMapActivity();
            }
        });
       invokeWS_Attendance_Details();
    }

    public void invokeWS_Attendance_Details(){
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysattnno", "0"+ sysattnno);
            paramsMap.put("attnstatus",""+ attnstatus);

            imageLoader = new ImageLoader(EmployeeAttendanceDetailSingle.this);

            //AsyncHttpClient client1 = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetAttendanceDetailsSingle", paramsMap, new ApiHelper.ApiCallback()   {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("attendance_single");
                        JSONObject jsonObject = new_array.getJSONObject(0);
                        String imageurlpath = IMAGEURL + "Upload_Images/" + jsonObject.getString("imagefilename");

                        // hide video preview
                        txtDescription.setVisibility(View.GONE);
                        videoPreview.setVisibility(View.GONE);
                        imgPreview.setVisibility(View.VISIBLE);
                        imageLoader.DisplayImage(imageurlpath, imgPreview);

                        //fileuri.setText(jsonObject.getString("fileuri"));
                        latitude = jsonObject.getString("latitude");
                        longitude = jsonObject.getString("longitude");

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

    private void launchGoogleMapActivity(){
       Intent i = new Intent(EmployeeAttendanceDetailSingle.this, MarkerDemoActivity.class);
       i.putExtra("latitude",latitude);
       i.putExtra("longitude", longitude);
       startActivity(i);

      //  new EmployeeAttendanceDetailSingle.UploadFileToServer().execute();
     //   Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_LONG).show();

    }
}
