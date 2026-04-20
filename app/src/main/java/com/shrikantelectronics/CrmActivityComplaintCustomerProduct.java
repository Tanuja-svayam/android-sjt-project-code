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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CrmActivityComplaintCustomerProduct extends AppCompatActivity {

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ProgressDialog prgDialog;

	TableLayout table_recovery;
	TextView header_recovery;

	String custcd ;
	String consignor_name;
	String consignor_mobileno ;
	String consignor_address1 ;
	String consignor_address2 ;
	String consignor_pincode ;
	String consignor_email ;
	String consignor_inquiredproduct ;
	String consignor_priceoffered ;
	String consignor_companycd ;
	String consignor_sysmrno ;

	EditText inputSearch_customerET ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recoverylist);

		inputSearch_customerET = (EditText)findViewById(R.id.inputSearch_customer);

		Intent i = getIntent();

		custcd = i.getStringExtra("custcd");
		consignor_name = i.getStringExtra("consignor_name");
		consignor_mobileno = i.getStringExtra("consignor_mobileno");
		consignor_address1 = i.getStringExtra("consignor_address1");
		consignor_address2 = i.getStringExtra("consignor_address2");
		consignor_pincode = i.getStringExtra("consignor_pincode");
		consignor_email = i.getStringExtra("consignor_email");
		consignor_inquiredproduct = i.getStringExtra("consignor_inquiredproduct");
		consignor_priceoffered= i.getStringExtra("consignor_priceoffered");

		invokeWS_CustomerProductList();


		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);
	}

	public void invokeWS_CustomerProductList() {

		try {

			Map<String, String> paramsMap = new HashMap<>();

			paramsMap.put("custcd", custcd);

			final int DEFAULT_TIMEOUT = 200000 * 1000000000;
			//AsyncHttpClient client = new AsyncHttpClient();
			//client.setTimeout(DEFAULT_TIMEOUT);

			header_recovery= (TextView) findViewById(R.id.header_recovery);
			header_recovery.setText("CUSTOMER PRODUCT DETAILS");

			ApiHelper.post(URL + "Service1.asmx/ProductDetailsByCustomer", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {

						table_recovery  = (TableLayout) findViewById(R.id.table_recovery);
						table_recovery.removeAllViews();

						table_recovery.setStretchAllColumns(true);
						table_recovery.setShrinkAllColumns(true);

						TableRow tblrowHeading = new TableRow(CrmActivityComplaintCustomerProduct.this);
						TextView highsHeading_location = initPlainHeaderTextView();
						highsHeading_location.setText("Product");
						highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_location.setGravity(Gravity.LEFT);

						tblrowHeading.addView(highsHeading_location);

						table_recovery.addView(tblrowHeading);

						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("crm_daily_walkin");

						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								final String sysdtlno;

								sysdtlno=jsonObject.getString("sysdtlno");

								TableRow tblrowLabels = new TableRow(CrmActivityComplaintCustomerProduct.this);

								TextView highsLabel_location = initPlainTextView(i);
								highsLabel_location.setText(jsonObject.getString("productdescription"));
								highsLabel_location.setTypeface(Typeface.DEFAULT);
								highsLabel_location.setGravity(Gravity.LEFT);
								highsLabel_location.setOnClickListener(new View.OnClickListener()
								{
									@Override
									public void onClick(View v)
									{

										Map<String, String> paramsMap = new HashMap<>();

										// When Email Edit View and Password Edit View have values other than Null
										if (Utility.isNotNull(sysdtlno)) {
											// When Email entered is Valid

											final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
											consignor_sysmrno  = globalVariable.getsysemployeeno();

											paramsMap.put("custcd", "0" + custcd);
											paramsMap.put("consignor_mobileno", "" + consignor_mobileno);
											paramsMap.put("consignor_name", "" + consignor_name);
											paramsMap.put("consignor_address1", "" + consignor_address1);
											paramsMap.put("consignor_address2", "" + consignor_address2);
											paramsMap.put("consignor_pincode", "" + consignor_pincode);
											paramsMap.put("consignor_email", "" + consignor_email);
											paramsMap.put("sysmrno", "0" + consignor_sysmrno);
											paramsMap.put("companycd", "0");
											paramsMap.put("inquiredproduct", "" + consignor_inquiredproduct);
											paramsMap.put("priceoffered", "0" + consignor_priceoffered);
											paramsMap.put("sysdtlno", sysdtlno);

											invokeCreateComplaintWithProductWS(paramsMap);

										}
										else {
											Toast.makeText(getApplicationContext(), "Please select the serial number, don't leave any field blank", Toast.LENGTH_LONG).show();
										}




									}
								});

								tblrowLabels.addView(highsLabel_location);

								table_recovery.addView(tblrowLabels);

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
					Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			//	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
		}

	}


	public void invokeCreateComplaintWithProductWS(Map<String, String> paramsMap){
		// Show Progress Dialog
		prgDialog.show();
		// Make RESTful webservice call using AsyncHttpClient object

		//STARWING -- SERVER
		//AsyncHttpClient client = new AsyncHttpClient();
		ApiHelper.post(URL + "Service1.asmx/AddWalkinCustomerComplaints", paramsMap, new ApiHelper.ApiCallback() {

			@Override
			public void onSuccess(JSONObject response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					// JSON Object
					JSONObject obj = response;
					// When the JSON response has status boolean value assigned with true

					//Toast.makeText(getApplicationContext(), "Customer is successfully added!", Toast.LENGTH_LONG).show();

					if(obj.getBoolean("status")){
						// Set Default Values for Edit View controls
						// Display successfully registered message using Toast
						Toast.makeText(getApplicationContext(), "Complaint is successfully added!", Toast.LENGTH_LONG).show();
						navigatetoFollowupActivity();
					}
					// Else display error message
					else{

						Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
					}
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
				prgDialog.hide();
				Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
			}
		});
	}


	public void navigatetoFollowupActivity(){
		Intent homeIntent = new Intent(CrmActivityComplaintCustomerProduct.this,ComplaintsActivity.class);
		homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		startActivity(homeIntent);
	}

	private TextView initPlainTextView(float n) {

		TextView textView = new TextView(CrmActivityComplaintCustomerProduct.this);
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
		TextView textView = new TextView(CrmActivityComplaintCustomerProduct.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_header);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}

	private TextView initPlainFooterTextView() {
		TextView textView = new TextView(CrmActivityComplaintCustomerProduct.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_footer);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}
}
