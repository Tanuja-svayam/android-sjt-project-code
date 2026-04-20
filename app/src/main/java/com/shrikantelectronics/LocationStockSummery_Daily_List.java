package com.shrikantelectronics;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationStockSummery_Daily_List extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;


	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<LocationStockSummery> arraylist = new ArrayList<LocationStockSummery>();
	ListViewAdapter_Location_Stock adapter;
	private ListView lv;


	// Creating JSON Parser object
	//JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> LocationStockSummery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_stock_summery_view);

		// Hashmap for ListView
		LocationStockSummery = new ArrayList<HashMap<String, String>>();

        // Loading INBOX in Background Thread
 //       new LoadInbox().execute();

		GetProductData_Summery();
	}

	public void GetProductData_Summery(){

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("companycd", "0");
		invokeWS_Product(paramsMap);
 	}


	public void invokeWS_Product(Map<String, String> paramsMap){
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/GetStock_Summery_Location_Daily", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("Stock_Summery_Location");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								LocationStockSummery wp = new LocationStockSummery(
										jsonObject.getString("from_date"),
										jsonObject.getString("to_date"),
										jsonObject.getString("companycd"),
										jsonObject.getString("stocklocation"),
										jsonObject.getString("op_balquantity"),
										jsonObject.getString("op_valueprice"),
										jsonObject.getString("pu_quantity"),
										jsonObject.getString("pu_valueprice"),
										jsonObject.getString("tr_in_quantity"),
										jsonObject.getString("tr_in_valueprice"),
										jsonObject.getString("tr_out_quantity"),
										jsonObject.getString("tr_out_valueprice"),
										jsonObject.getString("sa_in_quantity"),
										jsonObject.getString("sa_in_valueprice"),
										jsonObject.getString("sa_out_quantity"),
										jsonObject.getString("sa_out_valueprice"),
										jsonObject.getString("sa_return_in_quantity"),
										jsonObject.getString("sa_return_in_valueprice"),
										jsonObject.getString("sa_return_out_quantity"),
										jsonObject.getString("sa_return_out_valueprice"),
										jsonObject.getString("pu_return_in_quantity"),
										jsonObject.getString("pu_return_in_valueprice"),
										jsonObject.getString("pu_return_out_quantity"),
										jsonObject.getString("pu_return_out_valueprice"),
										jsonObject.getString("cl_balquantity"),
										jsonObject.getString("cl_valueprice"));

								arraylist.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view );
						adapter = new ListViewAdapter_Location_Stock(LocationStockSummery_Daily_List.this, arraylist);

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
