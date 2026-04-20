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
import android.widget.Button;
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

public class Salesmen_Customer_Search extends AppCompatActivity {

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
		setContentView(R.layout.activity_salesmen_customer_search);

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
			navigatetoCreateCustomeerActivity();
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
			final String sysemployeeno  = globalVariable.getsysemployeeno();

			Editable editableValue1 = inputSearch_customerET.getText();

			Map<String, String> paramsMap = new HashMap<>();

			paramsMap.put("sysemployeeno", sysemployeeno);
			paramsMap.put("search", "" +editableValue1);
			paramsMap.put("status", "" +status);

			final int DEFAULT_TIMEOUT = 200000 * 1000000000;
			//AsyncHttpClient client = new AsyncHttpClient();
			//client.setTimeout(DEFAULT_TIMEOUT);

			ApiHelper.post(URL + "Service1.asmx/Salesmen_Customer_Search", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {

						tablesalesregister  = (TableLayout) findViewById(R.id.tablesalesregister);
						tablesalesregister.removeAllViews();

						tablesalesregister.setStretchAllColumns(true);
						tablesalesregister.setShrinkAllColumns(true);

						TableRow tblrowHeading = new TableRow(Salesmen_Customer_Search.this);
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
						//tblrowHeading.addView(highsheading_productvalue);

						tablesalesregister.addView(tblrowHeading);

						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("crm_daily_walkin");

						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								final String customer_custcd;
								final String walkin_custcd;
								final String syscustactno;
								final String syslocno;
								final String sysaccledgerno;

								final String custname;
								final String companycd;
								final String locationname;

								syslocno=jsonObject.getString("syslocno");
								sysaccledgerno=jsonObject.getString("sysaccledgerno");
								customer_custcd=jsonObject.getString("customer_custcd");
								walkin_custcd=jsonObject.getString("walkin_custcd");
								syscustactno=jsonObject.getString("syscustactno");
								custname=jsonObject.getString("custname");
								companycd=jsonObject.getString("companycd");
								locationname=jsonObject.getString("locationname");

								TableRow tblrowLabels = new TableRow(Salesmen_Customer_Search.this);

								TextView highsLabel_location = initPlainTextView(i);
								highsLabel_location.setText(jsonObject.getString("locationname"));
								highsLabel_location.setTypeface(Typeface.DEFAULT);
								highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_custname = initPlainTextView(i);
                                highsLabel_custname.setText(jsonObject.getString("custname"));
                                highsLabel_custname.setTypeface(Typeface.DEFAULT);
                                highsLabel_custname.setGravity(Gravity.LEFT);
								if (jsonObject.getString("syscustactno").equals("0"))
                                {
									highsLabel_custname.setOnClickListener(new View.OnClickListener()
									{
										@Override
										public void onClick(View v)
										{
											Intent intent = new Intent(Salesmen_Customer_Search.this, SalesmenCreateCustomer.class);
											intent.putExtra("customer_custcd",customer_custcd);
											intent.putExtra("walkin_custcd",walkin_custcd);
											intent.putExtra("syscustactno",syscustactno);
											intent.putExtra("syslocno",syslocno);
											intent.putExtra("sysaccledgerno",sysaccledgerno);
											finish();
											startActivity(intent);
										}
									});
                                }
                                else
                                {
									highsLabel_custname.setOnClickListener(new View.OnClickListener()
									{
										@Override
										public void onClick(View v)
										{
											Intent intent = new Intent(Salesmen_Customer_Search.this, Salesmen_Followup_Customer_Single.class);
											intent.putExtra("walkin_custcd",walkin_custcd);
											intent.putExtra("syscustactno",syscustactno);
											intent.putExtra("customer_custcd",customer_custcd);
											intent.putExtra("custname",custname);
											intent.putExtra("companycd",companycd);
											intent.putExtra("locationname",locationname);
											intent.putExtra("syslocno",syslocno);
											intent.putExtra("sysaccledgerno",sysaccledgerno);

											finish();
											startActivity(intent);
										}
									});
                                };

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
								//tblrowLabels.addView(highslabel_productvalue);

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
			//	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
		}

	}

	public void navigatetoCreateCustomeerActivity(){

		Editable editableValue1 = inputSearch_customerET.getText();
String mobileno = editableValue1.toString();

		Intent homeIntent = new Intent(Salesmen_Customer_Search.this,SalesmenCreateCustomer.class);
		homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		homeIntent.putExtra("custcd","0");
		homeIntent.putExtra("syscustactno","0");
		homeIntent.putExtra("mobileno",mobileno);
		startActivity(homeIntent);
	}

	private TextView initPlainTextView(float n) {

		TextView textView = new TextView(Salesmen_Customer_Search.this);
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
		TextView textView = new TextView(Salesmen_Customer_Search.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_header);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}

	private TextView initPlainFooterTextView() {
		TextView textView = new TextView(Salesmen_Customer_Search.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_footer);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}
}
