package com.shrikantelectronics;

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





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiptActivity extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<Todays_Receipts> arraylist_Todays_Receipts = new ArrayList<Todays_Receipts>();
	ListViewAdapter_Dashboard_Todays_Receipts adapter_Todays_Receipts;
	private ListView lv;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receipt_list);

		GetProductData_Summery();
	}

	public void GetProductData_Summery(){

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("companycd", "");
		invokeWS_Todays_Receipt(paramsMap);
 	}

	public void invokeWS_Todays_Receipt(Map<String, String> paramsMap){
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/GetBranchTodays_ReceiptsDetails", paramsMap, new ApiHelper.ApiCallback()   {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("todays_receipts");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								Todays_Receipts wp = new Todays_Receipts(jsonObject.getString("companycd"),jsonObject.getString("stocklocation"),  jsonObject.getString("cash_lastdate"),  jsonObject.getString("cash"),  jsonObject.getString("cheque_lastdate"), jsonObject.getString("cheque"),  jsonObject.getString("creditcard_lastdate"), jsonObject.getString("creditcard"),  jsonObject.getString("clearcheque_lastdate"),  jsonObject.getString("clearcheque"),  jsonObject.getString("finnance_lastdate"), jsonObject.getString("finnance"), jsonObject.getString("total"));
								arraylist_Todays_Receipts.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view_Todays_Receipts);
						adapter_Todays_Receipts= new ListViewAdapter_Dashboard_Todays_Receipts(ReceiptActivity.this, arraylist_Todays_Receipts);

						// Binds the Adapter to the ListView
						lv.setAdapter(adapter_Todays_Receipts);


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
