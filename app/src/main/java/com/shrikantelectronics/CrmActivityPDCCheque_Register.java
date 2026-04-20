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

public class CrmActivityPDCCheque_Register extends AppCompatActivity {

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	TableLayout table_recovery;
	TextView header_recovery;

	String monthname;
	String status;
	String locationname;
	EditText inputSearch_customerET ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crmpdccheuqecustomerlist);

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

			header_recovery= (TextView) findViewById(R.id.header_recovery);
			header_recovery.setText("CHEQUE FOLLOWUP");

			ApiHelper.post(URL + "Service1.asmx/CRM_DailyPDCCheque_Employee", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {

						table_recovery  = (TableLayout) findViewById(R.id.table_recovery);
						table_recovery.removeAllViews();

						table_recovery.setStretchAllColumns(true);
						table_recovery.setColumnShrinkable(0,true);

						TableRow tblrowHeading = new TableRow(CrmActivityPDCCheque_Register.this);
						TextView highsHeading_location = initPlainHeaderTextView();
						highsHeading_location.setText("Location");
						highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_location.setGravity(Gravity.LEFT);

						TextView highsHeading_custname = initPlainHeaderTextView();
						highsHeading_custname.setText("Customer");
						highsHeading_custname.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_custname.setGravity(Gravity.LEFT);

						TextView highsHeading_custmobile = initPlainHeaderTextView();
						highsHeading_custmobile.setText("Mobile");
						highsHeading_custmobile.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_custmobile.setGravity(Gravity.CENTER);

						TextView highsHeading_duedate = initPlainHeaderTextView();
						highsHeading_duedate.setText("Followup Date");
						highsHeading_duedate.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_duedate.setGravity(Gravity.CENTER);

						TextView highsHeading_OMount = initPlainHeaderTextView();
						highsHeading_OMount.setText("Amount");
						highsHeading_OMount.setTypeface(Typeface.DEFAULT_BOLD);
						highsHeading_OMount.setGravity(Gravity.RIGHT);

						//tblrowHeading.addView(highsHeading_location);
						tblrowHeading.addView(highsHeading_custname);
						tblrowHeading.addView(highsHeading_custmobile);
						tblrowHeading.addView(highsHeading_duedate);
						tblrowHeading.addView(highsHeading_OMount);

						table_recovery.addView(tblrowHeading);

						double trn_amount = 0.00, trn_amount_total = 0.00;

						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("crm_daily_walkin");

						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								final String custcd;
								final String sysreceiptno;
								final String amount;

								trn_amount= jsonObject.getDouble("amount");

								custcd=jsonObject.getString("custcd");
								sysreceiptno=jsonObject.getString("sysreceiptno");
								amount=jsonObject.getString("amount");

								TableRow tblrowLabels = new TableRow(CrmActivityPDCCheque_Register.this);

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
										Intent intent = new Intent(CrmActivityPDCCheque_Register.this, CrmActivityPDCCheque_Single.class);
										intent.putExtra("custcd",custcd);
										intent.putExtra("sysreceiptno",sysreceiptno);
										intent.putExtra("amount",amount);
										finish();
										startActivity(intent);
									}
								});

								TextView highsLabel_custmobile = initPlainTextView(i);
								highsLabel_custmobile.setText(jsonObject.getString("custmobile"));
								highsLabel_custmobile.setTypeface(Typeface.DEFAULT);
								highsLabel_custmobile.setGravity(Gravity.CENTER);

								TextView highsLabel_duedate = initPlainTextView(i);
								highsLabel_duedate.setText(jsonObject.getString("vduedate"));
								highsLabel_duedate.setTypeface(Typeface.DEFAULT);
								highsLabel_duedate.setGravity(Gravity.CENTER);

								TextView highsLabel_OMount = initPlainTextView(i);
								highsLabel_OMount.setText(String.format("%.2f",trn_amount));
								highsLabel_OMount.setTypeface(Typeface.DEFAULT);
								highsLabel_OMount.setGravity(Gravity.RIGHT);

								//tblrowLabels.addView(highsLabel_location);
								tblrowLabels.addView(highsLabel_custname);
								tblrowLabels.addView(highsLabel_custmobile);
								tblrowLabels.addView(highsLabel_duedate);
								tblrowLabels.addView(highsLabel_OMount);

								table_recovery.addView(tblrowLabels);

								trn_amount_total = trn_amount_total + trn_amount;

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						TableRow tblrowFooter = new TableRow(CrmActivityPDCCheque_Register.this);

						TextView highsFooter_custname = initPlainFooterTextView();
						highsFooter_custname.setText("GRAND TOTAL");
						highsFooter_custname.setTypeface(Typeface.DEFAULT_BOLD);
						highsFooter_custname.setGravity(Gravity.LEFT);

						TextView highsFooter_custmobile = initPlainFooterTextView();
						highsFooter_custmobile.setText("");
						highsFooter_custmobile.setTypeface(Typeface.DEFAULT_BOLD);
						highsFooter_custmobile.setGravity(Gravity.LEFT);

						TextView highsFooter_duedate= initPlainFooterTextView();
						highsFooter_duedate.setText("");
						highsFooter_duedate.setTypeface(Typeface.DEFAULT_BOLD);
						highsFooter_duedate.setGravity(Gravity.LEFT);

						TextView highsFooter_OMount = initPlainFooterTextView();
						highsFooter_OMount.setText(String.format("%.2f",trn_amount_total));
						highsFooter_OMount.setTypeface(Typeface.DEFAULT_BOLD);
						highsFooter_OMount.setGravity(Gravity.RIGHT);

						tblrowFooter.addView(highsFooter_custname);
						tblrowFooter.addView(highsFooter_custmobile);
						tblrowFooter.addView(highsFooter_duedate);
						tblrowFooter.addView(highsFooter_OMount);

						table_recovery.addView(tblrowFooter);

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

		TextView textView = new TextView(CrmActivityPDCCheque_Register.this);
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
		TextView textView = new TextView(CrmActivityPDCCheque_Register.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_header);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}

	private TextView initPlainFooterTextView() {
		TextView textView = new TextView(CrmActivityPDCCheque_Register.this);
		textView.setPadding(10, 10, 10, 10);
		textView.setBackgroundResource(R.drawable.cell_shape_footer);
		textView.setTextColor(Color.parseColor("#ffffff"));
		return textView;
	}
}
