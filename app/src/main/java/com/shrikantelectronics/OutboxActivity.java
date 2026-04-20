package com.shrikantelectronics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;





public class OutboxActivity extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;


	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<Todays_Sales> arraylist_Todays_Sales = new ArrayList<Todays_Sales>();
	ListViewAdapter_Dashboard_Todays_Sales adapter_Todays_Sales;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox_list);

		GetProductData_Summery();
	}

	public void GetProductData_Summery(){

		 Map<String, String> paramsMap = new HashMap<>();
		   paramsMap.put("companycd", "");
		  invokeWS_Todays_Sales(paramsMap);
	}

	public void invokeWS_Todays_Sales(Map<String, String> paramsMap){
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/GetBranchTodays_SalesDetails", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("todays_sales");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								Todays_Sales wp = new Todays_Sales(
										jsonObject.getString("companycd"),
										jsonObject.getString("stocklocation"),
										jsonObject.getString("sales_target"),
										jsonObject.getString("sales_mtd_target"),
										jsonObject.getString("order_sales_date"),
										jsonObject.getString("order_sales_ach"),
										jsonObject.getString("op_sales_date"),
										jsonObject.getString("op_sales_ach"),
										jsonObject.getString("today_sales_ach"),
										jsonObject.getString("today_sales_quantity"),
										jsonObject.getString("sales_ach"),
										jsonObject.getString("sales_total_gap"),
										jsonObject.getString("sales_mtd_gap"),
										jsonObject.getString("sales_kitty"),
										jsonObject.getString("sales_ach_percent"),
										jsonObject.getString("sales_month_name"),
										jsonObject.getString("sales_month_target"),
										jsonObject.getString("month_sales_ach"),
										jsonObject.getString("sales_month_gap")

								);
								arraylist_Todays_Sales.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view_Todays_Sales );
						adapter_Todays_Sales = new ListViewAdapter_Dashboard_Todays_Sales(OutboxActivity.this, arraylist_Todays_Sales);

						// Binds the Adapter to the ListView
						lv.setAdapter(adapter_Todays_Sales);

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