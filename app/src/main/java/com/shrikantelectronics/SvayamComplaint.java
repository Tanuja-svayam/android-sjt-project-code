package com.shrikantelectronics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SvayamComplaint extends AppCompatActivity {

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	TableLayout tablesalesregister;

	String monthname;
	String status;
	String locationname;
	EditText inputSearch_customerET ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_svayam_complaint);

		inputSearch_customerET = (EditText)findViewById(R.id.inputSearch_customer);
		inputSearch_customerET.addTextChangedListener(new GenericTextWatcher_Mobile(inputSearch_customerET));

		Intent i = getIntent();
		status= i.getStringExtra("status");

		invokeWS_DailySales();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_model, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_New) {
			navigatetoCreateComplaintActivity();
		}
		return super.onOptionsItemSelected(item);
	}


	private class GenericTextWatcher_Mobile implements TextWatcher {

		private View view;
		private GenericTextWatcher_Mobile(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

		public void afterTextChanged(Editable editable) {

			Editable editableValue1 = inputSearch_customerET.getText();
			if (editableValue1.length() >= 4) {
				invokeWS_DailySales();
			}
		}
	}

	public void invokeWS_DailySales() {

		try {

			final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

			Editable editableValue1 = inputSearch_customerET.getText();

			Map<String, String> paramsMap = new HashMap<>();
			paramsMap.put("complaintid", "0");
			paramsMap.put("clientid", globalVariable.CLIENTID);
			paramsMap.put("subscriptionid", globalVariable.SUBSCRIPTIONID);
			paramsMap.put("userid", globalVariable.getuserid());
			paramsMap.put("search", "x" +editableValue1);
			paramsMap.put("status", "01" +status);

			final int DEFAULT_TIMEOUT = 200000 * 1000000000;
			//AsyncHttpClient client = new AsyncHttpClient();
			//client.setTimeout(DEFAULT_TIMEOUT);

			ApiHelper.post(URL + "Service1.asmx/SearchComplaint_ForSvayam", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {

						tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
						tablesalesregister.removeAllViews();

						tablesalesregister.setStretchAllColumns(true);
						tablesalesregister.setShrinkAllColumns(true);

						TableRow tblrowHeading = new TableRow(SvayamComplaint.this);

						TextView highsHeading_custname = initPlainHeaderTextView();
						highsHeading_custname.setText("Description");
						highsHeading_custname.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_custname.setGravity(Gravity.LEFT);

						TextView highsHeading_contactpersonmobile = initPlainHeaderTextView();
						highsHeading_contactpersonmobile.setText("Status");
						highsHeading_contactpersonmobile.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_contactpersonmobile.setGravity(Gravity.CENTER);

						tblrowHeading.addView(highsHeading_custname);
						//tblrowHeading.addView(highsHeading_contactpersonmobile);

						tablesalesregister.addView(tblrowHeading);

						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("crm_Complaint");

						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								final String complaintid;
								complaintid=jsonObject.getString("complaintid");

								TableRow tblrowLabels = new TableRow(SvayamComplaint.this);

								TextView highsLabel_location = initPlainTextView(i);
								highsLabel_location.setText(jsonObject.getString("complaintdescription"));
								highsLabel_location.setTypeface(Typeface.DEFAULT);
								highsLabel_location.setGravity(Gravity.LEFT);

								TextView highsLabel_contactpersonmobile = initPlainTextView(i);
								highsLabel_contactpersonmobile.setText(jsonObject.getString("status"));
								highsLabel_contactpersonmobile.setTypeface(Typeface.DEFAULT);
								highsLabel_contactpersonmobile.setGravity(Gravity.LEFT);

								tblrowLabels.addView(highsLabel_location);
								//tblrowLabels.addView(highsLabel_contactpersonmobile);

								tblrowLabels.setOnClickListener(new View.OnClickListener()
								{
									@Override
									public void onClick(View v)
									{
										Intent intent = new Intent(SvayamComplaint.this, SvayamCreateComplaint.class);
										intent.putExtra("complaintid",complaintid);
										startActivity(intent);
									}
								});

								tablesalesregister.addView(tblrowLabels);

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


				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			//	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
		}
	}

	public void navigatetoCreateComplaintActivity(){

		Editable editableValue1 = inputSearch_customerET.getText();
		String mobileno = editableValue1.toString();

		Intent homeIntent = new Intent(SvayamComplaint.this,SvayamCreateComplaint.class);
		homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		homeIntent.putExtra("complaintid","0");
		startActivity(homeIntent);
	}

	private TextView initPlainTextView(float n) {

		TextView textView = new TextView(SvayamComplaint.this);
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
		TextView textView = new TextView(SvayamComplaint.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_header);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}

	private TextView initPlainFooterTextView() {
		TextView textView = new TextView(SvayamComplaint.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_footer);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}
}
