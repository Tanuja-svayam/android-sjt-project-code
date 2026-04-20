package com.shrikantelectronics;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SchemePurchaseList extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<SchemePurchase> arraylist_SchemePurchase = new ArrayList<SchemePurchase>();
	ListViewAdapter_Dashboard_SchemePurchase adapter_SchemePurchase;
	private ListView lv;
	EditText inputSearch_customer ;
	String sysschemesno;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supplierschemepurchase_list);

		Intent i = getIntent();

		sysschemesno = i.getStringExtra("sysschemesno");
		GetProductData_Summery();
	}

	public void GetProductData_Summery(){

		 Map<String, String> paramsMap = new HashMap<>();
		   paramsMap.put("sysschemesno", sysschemesno);
		  invokeWS_SchemePurchase(paramsMap);
	}

	public void invokeWS_SchemePurchase(Map<String, String> paramsMap){
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/SupplierSchemePurchaseDetails", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("supplierschemePurchase");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								SchemePurchase wp = new SchemePurchase(
										jsonObject.getString("sysschemesno"),
										jsonObject.getString("vendorname"),
										jsonObject.getString("brandname"),
										jsonObject.getString("modelname"),
										jsonObject.getString("vpurorderdt"),
										jsonObject.getString("schemacode"),
										jsonObject.getString("refdocumentno"),
										jsonObject.getString("quantity"),
										jsonObject.getString("grossamt")
								);
								arraylist_SchemePurchase.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view_SupplierScheme );
						inputSearch_customer  = (EditText) findViewById(R.id.inputSearch_customer);

						adapter_SchemePurchase = new ListViewAdapter_Dashboard_SchemePurchase(SchemePurchaseList.this, arraylist_SchemePurchase);

						// Binds the Adapter to the ListView
						lv.setAdapter(adapter_SchemePurchase);


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
								adapter_SchemePurchase.filter(text);
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