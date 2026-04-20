package com.shrikantelectronics;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StockAgeing_Category extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<LocationStock> arraylist = new ArrayList<LocationStock>();
	ListViewAdapter_Dashboard_Stock adapter;
	private ListView lv;

	// Creating JSON Parser object
	//JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> inboxList;

	// products JSONArray
	JSONArray inbox = null;

	// Inbox JSON url
	private static final String INBOX_URL = "http://api.androidhive.info/mail/inbox.json";

	// ALL JSON node names

	String companycd1;
	String type1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox_list);

		Intent i = getIntent();
		companycd1 = i.getStringExtra("companycd");
		type1= i.getStringExtra("type");

        inboxList = new ArrayList<HashMap<String, String>>();

		GetProductData_Summery();
	}


	public void GetProductData_Summery(){

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("companycd", companycd1);
		paramsMap.put("type", type1);
		invokeWS_Product(paramsMap);
 	}

	public void invokeWS_Product(Map<String, String> paramsMap){
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/GetBranchStockDetails_Ageing_Category", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("stock");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								LocationStock wp = new LocationStock(jsonObject.getString("companycd"),jsonObject.getString("stocklocation"), jsonObject.getString("purchase_value"), jsonObject.getString("totalqty"), jsonObject.getString("days_30"), jsonObject.getString("days_31_60"), jsonObject.getString("days_61_90"), jsonObject.getString("days_91_180"), jsonObject.getString("days_180_above"));
								arraylist.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view );
						adapter = new ListViewAdapter_Dashboard_Stock(StockAgeing_Category.this, arraylist);

						// Binds the Adapter to the ListView
						lv.setAdapter(adapter);

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
