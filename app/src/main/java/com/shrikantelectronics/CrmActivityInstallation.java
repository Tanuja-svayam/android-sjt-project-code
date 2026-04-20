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

public class CrmActivityInstallation extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;


	private static String URL = Config.WEBSERVICE_URL;
	private static String IMAGEURL = Config.IMAGEURL_URL;

	ArrayList<CrmActivityStatus> arraylist = new ArrayList<CrmActivityStatus>();
	ListViewAdapter_CrmActivityStatus adapter;
	private ListView lv;

	// Creating JSON Parser object
	//JSONParser jsonParser = new JSONParser();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crmactivity_list);

		GetProductData_Summery();
	}

	public void GetProductData_Summery() {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("ActivityType", "03");
		invokeWS_Product(paramsMap);
	}

	public void invokeWS_Product(Map<String, String> paramsMap) {
		try {

			//AsyncHttpClient client = new AsyncHttpClient();
			ApiHelper.post(URL + "Service1.asmx/GenerateActivityStatus", paramsMap, new ApiHelper.ApiCallback() {
				@Override

				public void onSuccess(JSONObject response) {
					try {
						JSONObject obj = response;
						JSONArray new_array = obj.getJSONArray("activitystatus");
						for (int i = 0, count = new_array.length(); i < count; i++) {
							try {
								JSONObject jsonObject = new_array.getJSONObject(i);

								CrmActivityStatus wp = new CrmActivityStatus(
										jsonObject.getString("companycd"),
										jsonObject.getString("stocklocation"),
										jsonObject.getString("op_pending"),
										jsonObject.getString("op_inprocess"),
										jsonObject.getString("added_during_month"),
										jsonObject.getString("cl_currentyear"),
										jsonObject.getString("cl_currentmonth"),
										jsonObject.getString("total_in_open_in_hand"));
								arraylist.add(wp);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						lv = (ListView) findViewById(R.id.list_view);
						adapter = new ListViewAdapter_CrmActivityStatus(CrmActivityInstallation.this, arraylist);

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
					Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
									}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

