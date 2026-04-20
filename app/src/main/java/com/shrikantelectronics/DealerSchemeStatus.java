package com.shrikantelectronics;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import android.content.Intent;

public class DealerSchemeStatus extends AppCompatActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<DealerScheme> arraylist_DealerScheme = new ArrayList<DealerScheme>();
	ListViewAdapter_DealerScheme adapter_DealerScheme;
	private ListView lv;
	EditText inputSearch_customer ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dealerscheme_list);

		GetProductData_Summery();
	}

	public void GetProductData_Summery(){

		Map<String, String> paramsMap = new HashMap<>();

		paramsMap.put("sysschemesno", "0");
		paramsMap.put("custcd", "0");
		paramsMap.put("sysbrandno", "0");

		 invokeWS_DealerScheme(paramsMap);
	}


	public void invokeWS_DealerScheme(Map<String, String> paramsMap){
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/DealerSchemeAnalysis", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("dealer_scheme_analysis");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								DealerScheme wp = new DealerScheme(
										jsonObject.getString("sysschemesno"),
										jsonObject.getString("custname"),
										jsonObject.getString("brandname"),
										jsonObject.getString("vsupschfromdate"),
										jsonObject.getString("vsupschtodate"),
										jsonObject.getString("schemacode"),
										jsonObject.getString("valuationmethod"),
										jsonObject.getString("schemval"),
										jsonObject.getString("targetvalue"),
										jsonObject.getString("soldvalue"),
										jsonObject.getString("returnvalue"),
										jsonObject.getString("achievedvalue"),
										jsonObject.getString("diffvalue"),
										jsonObject.getString("value_net"),
										jsonObject.getString("schemval_achieved")
								);
								arraylist_DealerScheme.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view_DealerScheme );
						inputSearch_customer  = (EditText) findViewById(R.id.inputSearch_customer);

						adapter_DealerScheme = new ListViewAdapter_DealerScheme(DealerSchemeStatus.this, arraylist_DealerScheme);

						// Binds the Adapter to the ListView
						lv.setAdapter(adapter_DealerScheme);


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
								adapter_DealerScheme.filter(text);
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


				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}