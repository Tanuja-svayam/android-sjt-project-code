package com.shrikantelectronics;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.EditText;




import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SchemeStatus extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<SupplierScheme> arraylist_SupplierScheme = new ArrayList<SupplierScheme>();
	ListViewAdapter_Dashboard_SupplierScheme adapter_SupplierScheme;
	private ListView lv;
	EditText inputSearch_customer ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supplierscheme_list);

		GetProductData_Summery();
	}

	public void GetProductData_Summery(){

		 Map<String, String> paramsMap = new HashMap<>();
		   paramsMap.put("sysschemesno", "");
		  invokeWS_SupplierScheme(paramsMap);
	}

	public void invokeWS_SupplierScheme(Map<String, String> paramsMap){
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/SupplierSchemeDetails", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("supplierscheme");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								SupplierScheme wp = new SupplierScheme(
										jsonObject.getString("sysschemesno"),
										jsonObject.getString("vendorname"),
										jsonObject.getString("brandname"),
										jsonObject.getString("vsupschfromdate"),
										jsonObject.getString("vsupschtodate"),
										jsonObject.getString("schemacode"),
										jsonObject.getString("schematypedesc"),
										jsonObject.getString("schemval"),
										jsonObject.getString("passforslc"),
										jsonObject.getString("statusdesc"),
										jsonObject.getString("product_added"),
										jsonObject.getString("targetvalue"),
										jsonObject.getString("quantity_purchased"),
										jsonObject.getString("value_purchased"),
										jsonObject.getString("quantity_sold"),
										jsonObject.getString("value_sold"),
										jsonObject.getString("valuationmethod"),
										jsonObject.getString("pending_target_value"),
										jsonObject.getString("pending_target_percent")
								);
								arraylist_SupplierScheme.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view_SupplierScheme );
						inputSearch_customer  = (EditText) findViewById(R.id.inputSearch_customer);

						adapter_SupplierScheme = new ListViewAdapter_Dashboard_SupplierScheme(SchemeStatus.this, arraylist_SupplierScheme);

						// Binds the Adapter to the ListView
						lv.setAdapter(adapter_SupplierScheme);


						inputSearch_customer.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

							}

							@Override
							public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
														  int arg3) {

							}

							@Override
							public void afterTextChanged(Editable arg0) {
								String text = inputSearch_customer.getText().toString().toLowerCase(Locale.getDefault());
								adapter_SupplierScheme.filter(text);
							}
						});


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
					//     prgDialog.hide();
					// When Http response code is '404'
					Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
					// When Http response code is '500'

					// When Http response code other than 404, 500
									}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}