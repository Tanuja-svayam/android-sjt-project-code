package com.shrikantelectronics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONObject;

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

public class UploadActivity extends Activity {
	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	private ProgressBar progressBar;
	private String filePath = null;
	private TextView txtPercentage;
	private ImageView imgPreview;
	private VideoView vidPreview;
	private Button btnUpload;
	long totalSize = 0;
	public String foldername ="";
	String sysorderdtlno;
	public String filename ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		txtPercentage = (TextView) findViewById(R.id.txtPercentage);
		btnUpload = (Button) findViewById(R.id.btnUpload);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		vidPreview = (VideoView) findViewById(R.id.videoPreview);

		// Changing action bar background color
		//getActionBar().setBackgroundDrawable(
		//		new ColorDrawable(Color.parseColor(getResources().getString(
		//				R.color.action_bar))));

		// Receiving the data from previous activity
		Intent i = getIntent();

		// image or video path that is captured in previous activity
		filePath = i.getStringExtra("filePath");
		sysorderdtlno = i.getStringExtra("sysorderdtlno");
		foldername= i.getStringExtra("foldername");
		filename= i.getStringExtra("filename");

		// boolean flag to identify the media type, image or video
		boolean isImage = i.getBooleanExtra("isImage", true);

		if (filePath != null) {
			// Displaying the image or video on the screen
			previewMedia(isImage);
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
		}

		btnUpload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// uploading the file to server
				new UploadFileToServer().execute();
			}
		});

	}

	/**
	 * Displaying captured image/video on the screen
	 * */
	private void previewMedia(boolean isImage) {
		// Checking whether captured media is image or video
		if (isImage) {
			imgPreview.setVisibility(View.VISIBLE);
			vidPreview.setVisibility(View.GONE);
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// down sizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

			imgPreview.setImageBitmap(bitmap);
		} else {
			imgPreview.setVisibility(View.GONE);
			vidPreview.setVisibility(View.VISIBLE);
			vidPreview.setVideoPath(filePath);
			// start playing
			vidPreview.start();
		}
	}

	/**
	 * Uploading the file to server
	 * */
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
			String DELIVERY_DOCUMENT_FILE_UPLOAD_URL = Config.FILE_UPLOAD_URL + "DeliveryDocuments/fileUpload.php";

			File sourceFile = new File(filePath);
			if (!sourceFile.exists()) {
				return "File not found: " + filePath;
			}

			filename = sourceFile.getName();

			try {
				// Create multipart request body
				RequestBody fileBody = RequestBody.create(sourceFile, MediaType.parse("image/*"));

				MultipartBody requestBody = new MultipartBody.Builder()
						.setType(MultipartBody.FORM)
						.addFormDataPart("image", filename, fileBody)
						.addFormDataPart("foldername", foldername)
						.addFormDataPart("email", "abc@gmail.com")
						.build();

				// Build request
				Request request = new Request.Builder()
						.url(DELIVERY_DOCUMENT_FILE_UPLOAD_URL)
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
			super.onPostExecute(result);
		}
	}

	/**
	 * Method to show alert dialog
	 * */
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}



	public void invokeWS(){
		// Show Progress Dialog
		// Make RESTful webservice call using AsyncHttpClient object


		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		final String sysemployeeno  = globalVariable.getsysemployeeno();
		final String userid  = globalVariable.getuserid();

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("sysorderdtlno", "" + sysorderdtlno);
		paramsMap.put("signatureimage", "" + filename);

		//STARWING -- SERVER
		//AsyncHttpClient client = new AsyncHttpClient();
		ApiHelper.post(URL + "Service1.asmx/UpdateSignatureImageInvoiceOrderDetail", paramsMap, new ApiHelper.ApiCallback() {
			@Override
			public void onSuccess(JSONObject response) {
				// Hide Progress Dialog

				try {
					// JSON Object
					JSONObject obj = response;
					//Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();

				} catch (Exception e) {
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

}