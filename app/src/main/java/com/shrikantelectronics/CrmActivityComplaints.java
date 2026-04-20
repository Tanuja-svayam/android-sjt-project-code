package com.shrikantelectronics;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CrmActivityComplaints extends AppCompatActivity {

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	TableLayout tablesalesregister;

	TextView header_salesregister;
	String monthname;
	String status;
	String locationname;
	EditText inputSearch_customerET ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesleads);

		inputSearch_customerET = (EditText)findViewById(R.id.inputSearch_customer);
		inputSearch_customerET.addTextChangedListener(new GenericTextWatcher_Mobile(inputSearch_customerET));

		Intent i = getIntent();
		status= i.getStringExtra("status");


		invokeWS_DailySales();

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
			//if (editableValue1.length() >= 3) {
				invokeWS_DailySales();
			//}
		}
	}

	public void invokeWS_DailySales() {

		try {

			final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
			final String sysemployeeno  = globalVariable.getsysemployeeno();

			Editable editableValue1 = inputSearch_customerET.getText();

			Map<String, String> paramsMap = new HashMap<>();

			paramsMap.put("sysemployeeno", "0");
			paramsMap.put("search", "" +editableValue1);
			paramsMap.put("status", "" +status);

			final int DEFAULT_TIMEOUT = 200000 * 1000000000;
			//AsyncHttpClient client = new AsyncHttpClient();
			//client.setTimeout(DEFAULT_TIMEOUT);

			header_salesregister= (TextView) findViewById(R.id.header_salesregister);
			header_salesregister.setText("COMPLAINTS FOLLOWUP");

			ApiHelper.post(URL + "Service1.asmx/CRM_DailyComplaints_Employee", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {

						tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
						tablesalesregister.removeAllViews();

						tablesalesregister.setStretchAllColumns(true);
						tablesalesregister.setShrinkAllColumns(true);

						TableRow tblrowHeading = new TableRow(CrmActivityComplaints.this);
						TextView highsHeading_location = initPlainHeaderTextView();
						highsHeading_location.setText("Location");
						highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_location.setGravity(Gravity.LEFT);

						TextView highsHeading_custname = initPlainHeaderTextView();
						highsHeading_custname.setText("Customer");
						highsHeading_custname.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_custname.setGravity(Gravity.LEFT);

						TextView highsHeading_contactpersonmobile = initPlainHeaderTextView();
						highsHeading_contactpersonmobile.setText("Mobile");
						highsHeading_contactpersonmobile.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_contactpersonmobile.setGravity(Gravity.CENTER);

						TextView highsHeading_invorderdt = initPlainHeaderTextView();
						highsHeading_invorderdt.setText("Followup Date");
						highsHeading_invorderdt.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_invorderdt.setGravity(Gravity.CENTER);

						TextView highsHeading_Quantity = initPlainHeaderTextView();
						highsHeading_Quantity.setText("Quantity");
						highsHeading_Quantity.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_Quantity.setGravity(Gravity.CENTER);

						TextView highsheading_productvalue = initPlainHeaderTextView();
						highsheading_productvalue.setText("Time");
						highsheading_productvalue.setTypeface(Typeface.DEFAULT_BOLD);
						highsheading_productvalue.setGravity(Gravity.RIGHT);

						//tblrowHeading.addView(highsHeading_location);
						tblrowHeading.addView(highsHeading_invorderdt);
						tblrowHeading.addView(highsHeading_custname);
						tblrowHeading.addView(highsHeading_contactpersonmobile);
						//tblrowHeading.addView(highsHeading_Quantity);
						tblrowHeading.addView(highsheading_productvalue);

						tablesalesregister.addView(tblrowHeading);

						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("crm_daily_walkin");

						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								final String custcd;
								final String syscustactno;

								custcd=jsonObject.getString("custcd");
								syscustactno=jsonObject.getString("syscustactno");

								TableRow tblrowLabels = new TableRow(CrmActivityComplaints.this);

								TextView highsLabel_location = initPlainTextView(i);
								highsLabel_location.setText(jsonObject.getString("locationname"));
								highsLabel_location.setTypeface(Typeface.DEFAULT);
								highsLabel_location.setGravity(Gravity.LEFT);

								TextView highsLabel_custname = initPlainTextView(i);
								highsLabel_custname.setText(jsonObject.getString("custname"));
								highsLabel_custname.setTypeface(Typeface.DEFAULT);
								highsLabel_custname.setGravity(Gravity.LEFT);
								highsLabel_custname.setOnClickListener(new View.OnClickListener()
								{
									@Override
									public void onClick(View v)
									{
										Intent intent = new Intent(CrmActivityComplaints.this, CrmActivityFollowup_Complaint_Single.class);
										intent.putExtra("custcd",custcd);
										intent.putExtra("syscustactno",syscustactno);
										finish();
										startActivity(intent);
									}
								});

								TextView highsLabel_contactpersonmobile = initPlainTextView(i);
								highsLabel_contactpersonmobile.setText(jsonObject.getString("contactpersonmobile"));
								highsLabel_contactpersonmobile.setTypeface(Typeface.DEFAULT);
								highsLabel_contactpersonmobile.setGravity(Gravity.CENTER);


								TextView highsLabel_invorderdt = initPlainTextView(i);
								highsLabel_invorderdt.setText(jsonObject.getString("vnextfollowupdt"));
								highsLabel_invorderdt.setTypeface(Typeface.DEFAULT);
								highsLabel_invorderdt.setGravity(Gravity.CENTER);


								TextView highsLabel_Quantity = initPlainTextView(i);
								highsLabel_Quantity.setText(jsonObject.getString("quantity"));
								highsLabel_Quantity.setTypeface(Typeface.DEFAULT);
								highsLabel_Quantity.setGravity(Gravity.RIGHT);

								TextView highslabel_productvalue = initPlainTextView(i);
								highslabel_productvalue.setText(jsonObject.getString("nexttimehour"));
								highslabel_productvalue.setTypeface(Typeface.DEFAULT);
								highslabel_productvalue.setGravity(Gravity.RIGHT);


								//tblrowLabels.addView(highsLabel_location);
								tblrowLabels.addView(highsLabel_invorderdt);
								tblrowLabels.addView(highsLabel_custname);
								tblrowLabels.addView(highsLabel_contactpersonmobile);

								// tblrowLabels.addView(highsLabel_Quantity);
								tblrowLabels.addView(highslabel_productvalue);

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


				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			//	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
		}

	}


	private TextView initPlainTextView(float n) {

		TextView textView = new TextView(CrmActivityComplaints.this);
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
		TextView textView = new TextView(CrmActivityComplaints.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_header);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}

	private TextView initPlainFooterTextView() {
		TextView textView = new TextView(CrmActivityComplaints.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_footer);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}
}
