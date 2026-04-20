package com.shrikantelectronics;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;





import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.messaging.FirebaseMessaging;
import com.shrikantelectronics.sync_app_settings.SyncAdapterManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
   private static final String TAG = PaymentActivity.class.getSimpleName();
    private AlertDialog.Builder alertDialogBuilder;

    private final String TAG1 = this.getClass().getSimpleName();

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;
    private ProgressBar progressBar;
    ProgressDialog prgDialog;
    private ListView lv;
    EditText inputSearch;
    TableLayout table;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    TableLayout tableSales;
    TableLayout tableSalesNegetive;
    TableLayout tableSalesLeads;
    TableLayout tableFinanceos;
    TableLayout tablecashbalance;
    TableLayout tableLocationos;
    TableLayout tableLocationTask;
    TableLayout tableLocationcrmTask;

    TextView header_salesleadstatus;
    TextView header_salesstatus, header_salesstatusInvoice;
    TextView header_salesstatusNegetive;

    TextView header_finos;
    TextView header_cashbalance;
    TextView header_locationos;
    TextView header_locationtask;
    TextView header_locationcrmtask;

    Calendar calendar;
    TextView dateView, textname, textversion, companyname;
    ImageView empimage;
    int year, month, day;
    String fromdate, todate;

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    public ImageLoader imageLoader;

    protected static final int RESULT_SPEECH = 1;
    public ArrayList<String> text;

    public static TextToSpeech toSpeech;
    public int result;
    private static final String NOTIFICATION_CHANNEL_ID = "my_sync_notification_channel_id";
    private static final int MOVIE_NOTIFICATION_ID = 3004;

    public String sysemployeeno;
    public String groupcode;
    String refreshedToken;

    String closing, emailid, mobile, allowed_concerent_mobile_users = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //progressBar = (ProgressBar) findViewById(R.id.AddNewEventActivity_ProgressBar);



        invokeWS_CheckSvayamMGMTStatus();


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        groupcode = globalVariable.getgroupcode();
        sysemployeeno = globalVariable.getsysemployeeno();
        //
        // toolbar = findViewById(R.id.toolbar);
        // toolbar.setTitle(getResources().getString(R.string.app_name));
        // setSupportActionBar(toolbar);
        //
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        fromdate = Utility.ConvetToDDMMMYYYY(year, month, day);
        todate = Utility.ConvetToDDMMMYYYY(year, month, day);

        invokeWS_SalesLeads();
        invokeWS_TodaySalesOrders();
        invokeWS_TodaySalesInvoice();
        //invokeWS_TodaySales_Negetive();
        invokeWS_Location_PaymentMode_Balances();
        // invokeWS_FinanceOs();

        invokeWS_PendingTask();

        if (groupcode.equals("SAG")) {
            invokeWS_LocationOs();
        }

        //GetDashboard_Location_PaymentMode_Balances

       // invokeWS_PendingCrmStatus();

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView textname = (TextView) headerView.findViewById(R.id.textname);
        TextView parentcompanyname = (TextView) headerView.findViewById(R.id.parentcompanyname);
        TextView textversion = (TextView) headerView.findViewById(R.id.textversion);
        ImageView empimage = (ImageView) headerView.findViewById(R.id.empimage);

        String imageurlpath = IMAGEURL + "Profile/" + globalVariable.getemployeeimagename();

        textname.setText(globalVariable.getemployeename());
        parentcompanyname.setText(globalVariable.getparentcompanyname());
        imageLoader = new ImageLoader(this.getApplicationContext());

        imageLoader.DisplayImage(imageurlpath, empimage);

        toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if (i == TextToSpeech.SUCCESS) {
                    result = toSpeech.setLanguage(Locale.ENGLISH);
                } else {
                    Toast.makeText(getApplicationContext(), "Feature not supported in your deavice", Toast.LENGTH_LONG).show();
                }
            }
        });



        final String sysuserno = globalVariable.getSysuserno();

        // ✅ Get FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    refreshedToken = task.getResult();
                    Log.d("FCM", "Device token: " + refreshedToken);

                    // Send token to server
                    if (Utility.isNotNull(refreshedToken)) {
                        globalVariable.setddevice_token( refreshedToken);

                        Map<String, String> tokenParams = new HashMap<>();
                        tokenParams.put("sysuserno", sysuserno);
                        tokenParams.put("mobileapptype", "MGR");
                        tokenParams.put("token", refreshedToken);
                        invoke_Add_UpdateDeviceIDWS(tokenParams);
                    } else {
                        Toast.makeText(getApplicationContext(), "Token Not Generated", Toast.LENGTH_LONG).show();
                    }
                });

    }



    public void invoke_Add_UpdateDeviceIDWS(Map<String, String> paramsMap) {
        ApiHelper.post(URL + "Service1.asmx/Add_Update_User_Device_ID", paramsMap, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray new_array = response.getJSONArray("walkincustomerdetails");

                    Toast.makeText(getApplicationContext(),
                            "Token registered successfully!",
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(),
                        errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //SETS UP THE MENU ON THE ACTIONBAR//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        //noinspection SimplifiableIfStatement

      /*
        if (id == R.id.btnSpeak) {

            try {
                startActivityForResult(intent, RESULT_SPEECH);
            } catch (ActivityNotFoundException a) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT);
                t.show();
            }

            return true;
        }
*/
        if (id == R.id.btnlocation) {

            Intent customerIntent = new Intent(MainActivity.this, MapActivity.class);
            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(customerIntent);

        }

        if (id == R.id.btnSpeak) {

            Intent homeIntent = new Intent(MainActivity.this, MarkerDemoActivity.class);

          //  homeIntent.putExtra("closing", closing);
           // homeIntent.putExtra("emailid", emailid);
           // homeIntent.putExtra("mobile", mobile);
           // homeIntent.putExtra("allowed_concerent_mobile_users", allowed_concerent_mobile_users);

            startActivity(homeIntent);
        }

        if (id == R.id.btnPay) {

            Intent homeIntent = new Intent(MainActivity.this, PaymentActivity.class);

            homeIntent.putExtra("closing", closing);
            homeIntent.putExtra("emailid", emailid);
            homeIntent.putExtra("mobile", mobile);
            homeIntent.putExtra("allowed_concerent_mobile_users", allowed_concerent_mobile_users);

            startActivity(homeIntent);
        }


        if (id == R.id.btnComplaint) {
            Intent homeIntent = new Intent(MainActivity.this, SvayamComplaint.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareMenuData() {
        try {

            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String groupcode = globalVariable.getgroupcode();
            final String usertype = globalVariable.getusertype();

            paramsMap.put("groupcode", "" + groupcode);
            paramsMap.put("usertype", "" + usertype);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/GenerateMainMenu", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        JSONObject obj = response;

                        JSONArray schedule_Array = obj.getJSONArray("menupermission");

                        MenuModel menuModel;
                        MenuModel childModel;
                        List<MenuModel> childModelsList = new ArrayList<>();

                        /*
                        ------------------------
                        */

                        if (CheckMenuPermissson(response, "Dashboard")) {
                            menuModel = new MenuModel("Dashboard", "Y", "N", "");
                            headerList.add(menuModel);
                            childList.put(menuModel, null);
                        }

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Exibition", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Statistics")) {
                            childModel = new MenuModel("Statistics", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Exibition")) {
                            headerList.add(menuModel);
                        }


                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Purchase", "Y", "Y", "");

                        if (CheckMenuPermissson(response, "Create Purchase")) {
                            childModel = new MenuModel("Create Purchase", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Purchase Invoice")) {
                            childModel = new MenuModel("Purchase Invoice", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Purchase Book")) {
                            childModel = new MenuModel("Purchase Book", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Purchase Summery")) {
                            childModel = new MenuModel("Purchase Summery", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Schemes")) {
                            childModel = new MenuModel("Schemes", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Purchase")) {
                            headerList.add(menuModel);
                        }
                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Sales", "Y", "Y", "");

                        if (CheckMenuPermissson(response, "Create Lead")) {
                            childModel = new MenuModel("Create Lead", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Lead Register")) {
                            childModel = new MenuModel("Lead Register", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Quotation")) {
                            childModel = new MenuModel("Quotation", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Get Finance DO")) {
                            childModel = new MenuModel("Get Finance DO", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Order")) {
                            childModel = new MenuModel("Order", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Cash Sales")) {
                            childModel = new MenuModel("Cash Sales", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Approval Awaiting")) {
                            childModel = new MenuModel("Approval Awaiting", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Invoice")) {
                            childModel = new MenuModel("Invoice", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Sales Book")) {
                            childModel = new MenuModel("Sales Book", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Sales Summery")) {
                            childModel = new MenuModel("Sales Summery", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Event Sales Summery")) {
                            childModel = new MenuModel("Event Sales Summery", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Salesmen Performance")) {
                            childModel = new MenuModel("Salesmen Performance", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Sales Target Summery")) {
                            childModel = new MenuModel("Sales Target Summery", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Profitability")) {
                            childModel = new MenuModel("Profitability", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Sales Sheet")) {
                            childModel = new MenuModel("Sales Sheet", "N", "N", "");
                            childModelsList.add(childModel);
                        }




                        if (CheckMenuPermissson(response, "Sales")) {
                            headerList.add(menuModel);
                        }
                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("B2B", "Y", "Y", "");

                        if (CheckMenuPermissson(response, "B2B Sales Book")) {
                            childModel = new MenuModel("B2B Sales Book", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "B2B O/S Ageing")) {
                            childModel = new MenuModel("B2B O/S Ageing", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "B2B Sales Summary")) {
                            childModel = new MenuModel("B2B Sales Summary", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Dealer Target")) {
                            childModel = new MenuModel("Dealer Target", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Dealer Ledger")) {
                            childModel = new MenuModel("Dealer Ledger", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "B2B")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Inventory", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Brandwise")) {
                            childModel = new MenuModel("Brandwise", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Categorywise")) {
                            childModel = new MenuModel("Categorywise", "N", "N", "");
                            childModelsList.add(childModel);
                        }


                        if (CheckMenuPermissson(response, "Model")) {
                            childModel = new MenuModel("Model", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Serial Status")) {
                            childModel = new MenuModel("Serial Status", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Todays Stock Movement")) {
                            childModel = new MenuModel("Todays Stock Movement", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Location Stock Sheet")) {
                            childModel = new MenuModel("Location Stock Sheet", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Ageing Stock")) {
                            childModel = new MenuModel("Ageing Stock", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Transfer Issue")) {
                            childModel = new MenuModel("Transfer Issue", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Transfer Receipt")) {
                            childModel = new MenuModel("Transfer Receipt", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Add Serial In Order")) {
                            childModel = new MenuModel("Add Serial In Order", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Opening Stock")) {
                            childModel = new MenuModel("Opening Stock", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Inventory")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }



                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("CRM", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Delivery Status")) {
                            childModel = new MenuModel("Delivery Status", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Confirm Delivery")) {
                            childModel = new MenuModel("Confirm Delivery", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Sales On Map")) {
                            childModel = new MenuModel("Sales On Map", "N", "N", "");
                            childModelsList.add(childModel);
                        }


                        if (CheckMenuPermissson(response, "Allocation")) {
                            childModel = new MenuModel("Allocation", "N", "N", "");
                            childModelsList.add(childModel);
                        }




                        if (CheckMenuPermissson(response, "CRM")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }


                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Serice Center", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Create Complaint")) {
                            childModel = new MenuModel("Create Complaint", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Complaint Register")) {
                            childModel = new MenuModel("Complaint Register", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Call Register")) {
                            childModel = new MenuModel("Call Register", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Center Summary")) {
                            childModel = new MenuModel("Center Summary", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Dealer Summary")) {
                            childModel = new MenuModel("Dealer Summary", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Serice Center")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Recovery", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Cash On Delivery")) {
                            childModel = new MenuModel("Cash On Delivery", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Inward Cheques")) {
                            childModel = new MenuModel("Inward Cheques", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Finance Schedule")) {
                            childModel = new MenuModel("Finance Schedule", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Recovery")) {
                            headerList.add(menuModel);
                        }
                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }
                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Ledger", "Y", "Y", "");
                       // headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Customer Ledger")) {
                            childModel = new MenuModel("Customer Ledger", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Supplier Ledger")) {
                            childModel = new MenuModel("Supplier Ledger", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Cash Ledger")) {
                            childModel = new MenuModel("Cash Ledger", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Finance Ledger")) {
                            childModel = new MenuModel("Finance Ledger", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Account Ledger")) {
                            childModel = new MenuModel("Account Ledger", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Ledger")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }
                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Outstanding", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Customer Outstanding")) {
                            childModel = new MenuModel("Customer Outstanding", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Supplier Outstanding")) {
                            childModel = new MenuModel("Supplier Outstanding", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Finance Outstanding")) {
                            childModel = new MenuModel("Finance Outstanding", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Outstanding Ageing")) {
                            childModel = new MenuModel("Outstanding Ageing", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Outstanding")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("KPI", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "YTD")) {
                            childModel = new MenuModel("YTD", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "MTD")) {
                            childModel = new MenuModel("MTD", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "DTD")) {
                            childModel = new MenuModel("DTD", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "GOLM")) {
                            childModel = new MenuModel("GOLM", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "KPI")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }


                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Final Accounts", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Group Summary")) {
                            childModel = new MenuModel("Group Summary", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Day Book")) {
                            childModel = new MenuModel("Day Book", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Trial Balance")) {
                            childModel = new MenuModel("Trial Balance", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Trading Account")) {
                            childModel = new MenuModel("Trading Account", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "P & L")) {
                            childModel = new MenuModel("P & L", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Balance Sheet")) {
                            childModel = new MenuModel("Balance Sheet", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Voucher Statistics")) {
                            childModel = new MenuModel("Voucher Statistics", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Financial Ratios")) {
                            childModel = new MenuModel("Financial Ratios", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Final Accounts")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */


                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Employee", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Daily Task")) {
                            childModel = new MenuModel("Daily Task", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "My Documents")) {
                            childModel = new MenuModel("My Documents", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Attendance")) {
                            childModel = new MenuModel("Attendance", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Attandance Report")) {
                            childModel = new MenuModel("Attandance Report", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Employee")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }

                        /* ------------------------ */

                        childModelsList = new ArrayList<>();
                        menuModel = new MenuModel("Support", "Y", "Y", "");
                        //headerList.add(menuModel);

                        if (CheckMenuPermissson(response, "Contact Us")) {
                            childModel = new MenuModel("Contact Us", "N", "N", "");
                            childModelsList.add(childModel);
                        }
                        if (CheckMenuPermissson(response, "Call For Demo")) {
                            childModel = new MenuModel("Call For Demo", "N", "N", "");
                            childModelsList.add(childModel);
                        }

                        if (CheckMenuPermissson(response, "Support")) {
                            headerList.add(menuModel);
                        }

                        if (menuModel.hasChildren) {
                            childList.put(menuModel, childModelsList);
                        }


                        menuModel = new MenuModel("Logout", "Y", "N", "");
                        headerList.add(menuModel);
                        childList.put(menuModel, null);


                        populateExpandableList();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    private boolean CheckMenuPermissson(JSONObject response, String MenuName) {
        boolean result = false;
        try {

            JSONObject obj = response;

            JSONArray schedule_Array = obj.getJSONArray("menupermission");
            for (int i = 0; i < schedule_Array.length(); i++) {
                JSONObject jsonObject = schedule_Array.getJSONObject(i);
                if (jsonObject.getString("menuName").equalsIgnoreCase(MenuName)) {
                    if (jsonObject.getString("vpermission").equalsIgnoreCase("1")) {
                        result = true;
                    }
                    if (jsonObject.getString("vpermission").equalsIgnoreCase("0")) {
                        result = false;
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            // Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {

                        if (headerList.get(groupPosition).menuName == "Sales") {

                            Intent customerIntent = new Intent(MainActivity.this, SalesActivity.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (headerList.get(groupPosition).menuName == "Logout") {

                            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                            loginPrefsEditor = loginPreferences.edit();

                            loginPrefsEditor.clear();
                            loginPrefsEditor.commit();

                            Intent homeIntent = new Intent(getApplicationContext(), FullscreenActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);

                    /*----------------------------------------------------------------*/

                    if (model.menuName == "Dashboard") {
                        Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }


                    if (model.menuName == "Create Purchase") {
                        Intent homeIntent = new Intent(MainActivity.this, Procurement.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Purchase Invoice") {
                        Intent homeIntent = new Intent(MainActivity.this, PurchaseInvoice_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Purchase Book") {
                        Intent homeIntent = new Intent(MainActivity.this, PurchaseActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Purchase Summery") {
                        Intent homeIntent = new Intent(MainActivity.this, PurchaseAnalysisCategory.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Schemes") {
                        Intent customerIntent = new Intent(MainActivity.this, SchemeStatus.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Create Lead") {
                        Intent customerIntent = new Intent(MainActivity.this, Salesmen_Customer_Search.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Lead Register") {
                        Intent homeIntent = new Intent(MainActivity.this, CrmActivityEmployee.class);
                        //   homeIntent.putExtra("status","PENDING");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Quotation") {
                        Intent homeIntent = new Intent(MainActivity.this, Quotation_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Get Finance DO") {
                        Intent customerIntent = new Intent(MainActivity.this, SalesmenGetFinanceDO.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Order") {
                        Intent homeIntent = new Intent(MainActivity.this, Order_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }


                    if (model.menuName == "Cash Sales") {
                        Intent homeIntent = new Intent(MainActivity.this, Salesmen_SalesOrder.class);

                        homeIntent.putExtra("sysinvorderno", "0");
                        homeIntent.putExtra("custname", "");
                        homeIntent.putExtra("vinvoicedt", "");
                        homeIntent.putExtra("netinvoiceamt", "0");
                        homeIntent.putExtra("invorderno", "");

                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }


                    if (model.menuName == "Approval Awaiting") {
                        Intent homeIntent = new Intent(MainActivity.this, OrderApprovalPending_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }


                    if (model.menuName == "Invoice") {
                        Intent homeIntent = new Intent(MainActivity.this, Invoice_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Sales Book") {
                        Intent customerIntent = new Intent(MainActivity.this, SalesActivity.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Sales Summery") {
                        Intent homeIntent = new Intent(MainActivity.this, SalesAnalysisCategory.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Event Sales Summery") {
                        Intent homeIntent = new Intent(MainActivity.this, SalesAnalysisEvent.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Salesmen Performance") {
                        Intent homeIntent = new Intent(MainActivity.this, SalesPerformance_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                    if (model.menuName == "Sales Target Summery") {
                        Intent homeIntent = new Intent(MainActivity.this, OutboxActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Profitability") {
                        Intent customerIntent = new Intent(MainActivity.this, ProfitabilityActivity.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Sales Sheet") {
                        Intent customerIntent = new Intent(MainActivity.this, SalesReportActivity.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }



                    if (model.menuName == "B2B Sales Book") {
                        Intent homeIntent = new Intent(MainActivity.this, B2BActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "B2B O/S Ageing") {
                        Intent homeIntent = new Intent(MainActivity.this, B2BOutstandingLocationActivity.class);
                        homeIntent.putExtra("LOCATIONTYPE", "BB");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "B2B Sales Summary") {
                        Intent customerIntent = new Intent(MainActivity.this, SalesAnalysisB2BCustomer.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Dealer Target") {
                        Intent customerIntent = new Intent(MainActivity.this, DealerSchemeStatus.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Dealer Ledger") {
                        Intent customerIntent = new Intent(MainActivity.this, SalesAnalysisB2BCustomer.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Brandwise") {
                        Intent homeIntent = new Intent(MainActivity.this, StockActivityBrand.class);
                        homeIntent.putExtra("categoryname", "");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Categorywise") {
                        Intent customerIntent = new Intent(MainActivity.this, StockActivityCategory.class);
                        customerIntent.putExtra("brandname", "");
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);

                    }
                    if (model.menuName == "Model") {
                        Intent homeIntent = new Intent(MainActivity.this, model_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);

                    }

                    if (model.menuName == "Serial Status") {
                        Intent homeIntent = new Intent(MainActivity.this, serial_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Todays Stock Movement") {
                        Intent customerIntent = new Intent(MainActivity.this, LocationStockSummery_Daily_List.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Location Stock Sheet") {
                        Intent customerIntent = new Intent(MainActivity.this, StockActivityLocationBrand.class);
                        customerIntent.putExtra("categoryname", "");
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Ageing Stock") {
                        Intent homeIntent = new Intent(MainActivity.this, StockActivityAgeingLocationBrand.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Transfer Issue") {
                        Intent homeIntent = new Intent(MainActivity.this, TransferStock.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Transfer Receipt") {
                        Intent homeIntent = new Intent(MainActivity.this, TransferSerial_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Add Serial In Order") {
                        Intent customerIntent = new Intent(MainActivity.this, pendingdelivery_view.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Opening Stock") {
                        Intent homeIntent = new Intent(MainActivity.this, StockVerification.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }


                    if (model.menuName == "Delivery Status") {
                        Intent homeIntent = new Intent(getApplicationContext(), OrderDeliveryPending_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Confirm Delivery") {
                        Intent homeIntent = new Intent(MainActivity.this, confirmdelivery_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Sales On Map") {
                        Intent homeIntent = new Intent(MainActivity.this, MapActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Allocation") {
                        Intent homeIntent = new Intent(MainActivity.this, CrmActivity_Installation_Allocation.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Call Register") {
                        Intent homeIntent = new Intent(getApplicationContext(), Service_Call_Status_Summary.class);
                        homeIntent.putExtra("servicecentercd", "0");
                        homeIntent.putExtra("servicecentername", "");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Center Summary") {
                        Intent homeIntent = new Intent(MainActivity.this, Service_Call_Service_Center_Summary.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Dealer Summary") {
                        Intent homeIntent = new Intent(MainActivity.this, Service_Call_Dealer_Summary.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Create Complaint") {
                        Intent customerIntent = new Intent(MainActivity.this, CreateCustomerComplaint.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    /*
                    if (model.menuName == "Complaint Summary")    {
                        Intent customerIntent = new Intent(MainActivity.this,ComplaintsActivity.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }
                    */

                    if (model.menuName == "Complaint Register") {
                        Intent homeIntent = new Intent(MainActivity.this, CrmActivityComplaintsRegister.class);
                        homeIntent.putExtra("status", "PENDING");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Cash On Delivery") {
                        Intent homeIntent = new Intent(MainActivity.this, CrmActivityCOD_Register.class);
                        homeIntent.putExtra("status", "PENDING");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Inward Cheques") {
                        Intent homeIntent = new Intent(MainActivity.this, CrmActivityPDCCheque_Register.class);
                        homeIntent.putExtra("status", "PENDING");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Finance Schedule") {
                        Intent homeIntent = new Intent(MainActivity.this, FinanceRecoveryScheduleActivity.class);
                        homeIntent.putExtra("status", "PENDING");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Customer Ledger") {
                        Intent homeIntent = new Intent(MainActivity.this, customer_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        homeIntent.putExtra("calledfrom", "LEDGER");
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Supplier Ledger") {
                        Intent homeIntent = new Intent(MainActivity.this, supplier_view.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Cash Ledger") {
                        Intent customerIntent = new Intent(MainActivity.this, maincash_view.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        customerIntent.putExtra("legdertype", "CAL");
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Finance Ledger") {
                        Intent customerIntent = new Intent(MainActivity.this, Ledger_finance.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }


                    if (model.menuName == "Account Ledger") {
                        Intent homeIntent = new Intent(MainActivity.this, FA_Account_Ledger.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Outstanding Summary") {
                        Intent homeIntent = new Intent(MainActivity.this, OutstandingLocationActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Customer Outstanding") {

                        Intent homeIntent = new Intent(MainActivity.this, OutstandingLocationActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Supplier Outstanding") {

                        Intent homeIntent = new Intent(MainActivity.this, OutstandingSupplierActivity.class);
                        homeIntent.putExtra("type", "GROUP");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Finance Outstanding") {

                       // Intent homeIntent = new Intent(MainActivity.this, Outstanding_finance.class);
                        Intent homeIntent = new Intent(MainActivity.this, OutstandingLocationFinanceActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Outstanding Ageing") {
                        Intent homeIntent = new Intent(MainActivity.this, B2BOutstandingLocationActivity.class);
                        homeIntent.putExtra("LOCATIONTYPE", "SH");
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }


                    if (model.menuName == "YTD") {

                        Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                        customerIntent.putExtra("VALUETYPE", "YTD");
                        customerIntent.putExtra("YearType", "CALENDER");
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }


                    if (model.menuName == "MTD") {
                        Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                        customerIntent.putExtra("VALUETYPE", "MTD");
                        customerIntent.putExtra("YearType", "CALENDER");
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);

                    }

                    if (model.menuName == "DTD") {
                        Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                        customerIntent.putExtra("VALUETYPE", "DTD");
                        customerIntent.putExtra("YearType", "CALENDER");
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);

                    }

                    if (model.menuName == "GOLM") {
                        Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                        customerIntent.putExtra("VALUETYPE", "GOLM");
                        customerIntent.putExtra("YearType", "CALENDER");
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }


                    /*
                    if (CheckMenuPermissson(response,"")) {

                    }*/
                    if (model.menuName == "Group Summary") {
                        Intent intent = new Intent(MainActivity.this, FA_GroupSummary.class);
                        intent.putExtra("groupname", "");
                        intent.putExtra("fromdate", "");
                        intent.putExtra("todate", "");
                        startActivity(intent);
                    }
                    if (model.menuName == "Day Book") {
                        Intent customerIntent = new Intent(MainActivity.this, DayBook.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }
                    if (model.menuName == "Trial Balance") {
                        Intent customerIntent = new Intent(MainActivity.this, FA_TrialBalance.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }
                    if (model.menuName == "Trading Account") {
                        Intent customerIntent = new Intent(MainActivity.this, FA_TradingAccount.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "P & L") {
                        Intent customerIntent = new Intent(MainActivity.this, FA_Profit_LossAC.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }
                    if (model.menuName == "Balance Sheet") {
                        Intent customerIntent = new Intent(MainActivity.this, FA_BalanceSheet.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Voucher Statistics") {
                        Intent customerIntent = new Intent(MainActivity.this, FA_VoucherStatistics.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }

                    if (model.menuName == "Daily Task") {
                        Intent customerIntent = new Intent(MainActivity.this, ViewEmployeeTask.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }
                    if (model.menuName == "My Documents") {
                        Intent homeIntent = new Intent(MainActivity.this, EmployeeDocuments.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                    if (model.menuName == "Attendance") {
                        Intent customerIntent = new Intent(MainActivity.this, EmployeeAttendanceCreate.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }
                    if (model.menuName == "Attandance Report") {
                        Intent homeIntent = new Intent(MainActivity.this, EmployeeAttendanceReport.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }



                    /*
                    }
                    if (CheckMenuPermissson(response,"Contact Us")) {

                    }
                    if (CheckMenuPermissson(response,"Call For Demo")) {

                    }
                    if (CheckMenuPermissson(response,"Downloads")) {

                    }

*/


                }
                onBackPressed();
                return false;
            }
        });
    }

    public void navigatetoDayBookActivity() {
        Intent customerIntent = new Intent(MainActivity.this, DayBook.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoSuppluerSchemeActivity() {
        Intent customerIntent = new Intent(MainActivity.this, SchemeStatus.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoB2BActivity() {
        Intent customerIntent = new Intent(MainActivity.this, B2BActivity.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoSalesOrderActivity() {
        Intent homeIntent = new Intent(MainActivity.this, OrderApprovalPending_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(homeIntent);
    }

    public void navigatetoPedingDeliveryOrderActivity() {
        Intent homeIntent = new Intent(MainActivity.this, OrderDeliveryPending_view.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public void navigatetoComplaints() {
        Intent customerIntent = new Intent(MainActivity.this, ComplaintsActivity.class);
        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(customerIntent);
    }

    public void navigatetoTransferActivity() {
        Intent homeIntent = new Intent(MainActivity.this, TransferStock.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(homeIntent);
    }

    public void invokeWS_CheckSvayamMGMTStatus() {

        try {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("clientid", globalVariable.CLIENTID);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/CheckSvayamMGMTStatus_ForSvayam", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("crm_customermgmtstatus");
                        JSONObject jsonObject = new_array.getJSONObject(0);
                        //
                        // When the JSON response has status boolean value assigned with true
                        // closing":250000,"closing_drcr":"DR","allowed_concerent_mobile_users":5}] }
                        //
                        double dclosing;
                        dclosing = Double.valueOf(jsonObject.getString("closing"));

                        closing = jsonObject.getString("closing");
                        emailid = jsonObject.getString("emailid");
                        mobile = jsonObject.getString("mobile");
                        allowed_concerent_mobile_users=jsonObject.getString("allowed_concerent_mobile_users");

                        if(dclosing > 0) {
                            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);

                            intent.putExtra("closing", jsonObject.getString("closing"));
                            intent.putExtra("emailid", jsonObject.getString("emailid"));
                            intent.putExtra("mobile", jsonObject.getString("mobile"));
                            intent.putExtra("allowed_concerent_mobile_users", jsonObject.getString("allowed_concerent_mobile_users"));
                            intent.putExtra("closing_for_gateway", jsonObject.getString("closing_for_gateway"));

                            startActivity(intent);

                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }


    public void invokeWS_SalesLeads() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            if (groupcode.equals("SAG")) {
                paramsMap.put("sysemployeeno", "0");
            } else {
                paramsMap.put("sysemployeeno", "0" + sysemployeeno);
            }

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_SalesLeadsDetails", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_salesleadstatus = (TextView) findViewById(R.id.header_salesleadstatus);
                        header_salesleadstatus.setText("Walk-In Month Status");

                        table = (TableLayout) findViewById(R.id.tableSalesLeads);

                        table.setStretchAllColumns(true);
                        //table.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_addedinmonth = initPlainHeaderTextView();
                        highsHeading_addedinmonth.setText("Added");
                        highsHeading_addedinmonth.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_addedinmonth.setGravity(Gravity.CENTER);

                        TextView highsHeading_addedtoday = initPlainHeaderTextView();
                        highsHeading_addedtoday.setText("Today's");
                        highsHeading_addedtoday.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_addedtoday.setGravity(Gravity.CENTER);

                        TextView highsHeading_open = initPlainHeaderTextView();
                        highsHeading_open.setText("Open");
                        highsHeading_open.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_open.setGravity(Gravity.CENTER);

                        TextView highsHeading_successduringmonth = initPlainHeaderTextView();
                        highsHeading_successduringmonth.setText("Success");
                        highsHeading_successduringmonth.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_successduringmonth.setGravity(Gravity.RIGHT);

                        TextView highsHeading_faileddurringmonth = initPlainHeaderTextView();
                        highsHeading_faileddurringmonth.setText("Failed");
                        highsHeading_faileddurringmonth.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_faileddurringmonth.setGravity(Gravity.RIGHT);

                        TextView highsHeading_avgwalkin = initPlainHeaderTextView();
                        highsHeading_avgwalkin.setText("Avg. Walkin");
                        highsHeading_avgwalkin.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_avgwalkin.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_addedinmonth);
                        tblrowHeading.addView(highsHeading_addedtoday);
                        tblrowHeading.addView(highsHeading_open);
                        tblrowHeading.addView(highsHeading_successduringmonth);
                        tblrowHeading.addView(highsHeading_faileddurringmonth);
                        tblrowHeading.addView(highsHeading_avgwalkin);

                        table.addView(tblrowHeading);

                        double dadded_durring_month;
                        double dtoday_leads;
                        double dopen_leads;
                        double dsuccess_close_durring_month;
                        double dfailed_close_durring_month;

                        dadded_durring_month = 0;
                        dtoday_leads = 0;
                        dopen_leads = 0;
                        dsuccess_close_durring_month = 0;
                        dfailed_close_durring_month = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("sales_leads");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                final TextView highsLabel_location = initPlainTextView(i);

                                highsLabel_location.setText(jsonObject.getString("companyname"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_addedinmonth = initPlainTextView(i);
                                highsLabel_addedinmonth.setText(jsonObject.getString("added_durring_month"));
                                highsLabel_addedinmonth.setTypeface(Typeface.DEFAULT);
                                highsLabel_addedinmonth.setGravity(Gravity.CENTER);

                                TextView highsLabel_today = initPlainTextView(i);
                                highsLabel_today.setText(jsonObject.getString("today_leads"));
                                highsLabel_today.setTypeface(Typeface.DEFAULT);
                                highsLabel_today.setGravity(Gravity.CENTER);

                                TextView highsLabel_open = initPlainTextView(i);
                                highsLabel_open.setText(jsonObject.getString("open_leads"));
                                highsLabel_open.setTypeface(Typeface.DEFAULT);
                                highsLabel_open.setGravity(Gravity.CENTER);

                                TextView highsLabel_successduringmonth = initPlainTextView(i);
                                highsLabel_successduringmonth.setText(jsonObject.getString("success_close_durring_month"));
                                highsLabel_successduringmonth.setTypeface(Typeface.DEFAULT);
                                highsLabel_successduringmonth.setGravity(Gravity.RIGHT);

                                TextView highsLabel_failedclosedurringmonth = initPlainTextView(i);
                                highsLabel_failedclosedurringmonth.setText(jsonObject.getString("failed_close_durring_month"));
                                highsLabel_failedclosedurringmonth.setTypeface(Typeface.DEFAULT);
                                highsLabel_failedclosedurringmonth.setGravity(Gravity.RIGHT);

                                TextView highsLabel_averagewalkinper_days = initPlainTextView(i);
                                highsLabel_averagewalkinper_days.setText(jsonObject.getString("average_walkin_per_days"));
                                highsLabel_averagewalkinper_days.setTypeface(Typeface.DEFAULT);
                                highsLabel_averagewalkinper_days.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_addedinmonth);
                                tblrowLabels.addView(highsLabel_today);
                                tblrowLabels.addView(highsLabel_open);
                                tblrowLabels.addView(highsLabel_successduringmonth);
                                tblrowLabels.addView(highsLabel_failedclosedurringmonth);
                                tblrowLabels.addView(highsLabel_averagewalkinper_days);

                                table.addView(tblrowLabels);

                                dadded_durring_month += Double.valueOf(0 + jsonObject.getString("dadded_durring_month"));
                                dtoday_leads += Double.valueOf(0 + jsonObject.getString("today_leads"));
                                dopen_leads += Double.valueOf(0 + jsonObject.getString("dopen_leads"));
                                dsuccess_close_durring_month += Double.valueOf(0 + jsonObject.getString("dsuccess_close_durring_month"));
                                dfailed_close_durring_month += Double.valueOf(0 + jsonObject.getString("dfailed_close_durring_month"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_location = initPlainFooterTextView();
                        highsFooter_location.setText("Total");
                        highsFooter_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_location.setGravity(Gravity.LEFT);

                        TextView highsFooter_added_durring_month = initPlainFooterTextView();
                        highsFooter_added_durring_month.setText(Utility.DoubleToString(dadded_durring_month));
                        highsFooter_added_durring_month.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_added_durring_month.setGravity(Gravity.CENTER);

                        TextView highsFooter_today_leads = initPlainFooterTextView();
                        highsFooter_today_leads.setText(Utility.DoubleToString(dtoday_leads));
                        highsFooter_today_leads.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_today_leads.setGravity(Gravity.CENTER);

                        TextView highsFooter_open_leads = initPlainFooterTextView();
                        highsFooter_open_leads.setText(Utility.DoubleToString(dopen_leads));
                        highsFooter_open_leads.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_open_leads.setGravity(Gravity.CENTER);

                        TextView highsFooter_success_close_durring_month = initPlainFooterTextView();
                        highsFooter_success_close_durring_month.setText(Utility.DoubleToString(dsuccess_close_durring_month));
                        highsFooter_success_close_durring_month.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_success_close_durring_month.setGravity(Gravity.RIGHT);

                        TextView highsFooter_failed_close_durring_month = initPlainFooterTextView();
                        highsFooter_failed_close_durring_month.setText(Utility.DoubleToString(dfailed_close_durring_month));
                        highsFooter_failed_close_durring_month.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_failed_close_durring_month.setGravity(Gravity.RIGHT);

                        TextView highsFooter_average_walkin_per_days = initPlainFooterTextView();
                        highsFooter_average_walkin_per_days.setText("");
                        highsFooter_average_walkin_per_days.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_average_walkin_per_days.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_location);
                        tblrowFooter.addView(highsFooter_added_durring_month);
                        tblrowFooter.addView(highsFooter_today_leads);
                        tblrowFooter.addView(highsFooter_open_leads);
                        tblrowFooter.addView(highsFooter_success_close_durring_month);
                        tblrowFooter.addView(highsFooter_failed_close_durring_month);
                        tblrowFooter.addView(highsFooter_average_walkin_per_days);

                        table.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    public void invokeWS_TodaySalesOrders() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("sysemployeeno", "0" + sysemployeeno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/MobileTodays_SalesDetails_Orders", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_salesstatus = (TextView) findViewById(R.id.header_salesstatus);
                        header_salesstatus.setText("Todays Sales Orders ");

                        table = (TableLayout) findViewById(R.id.tableSales);

                        table.setStretchAllColumns(true);
                        //table.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_model = initPlainHeaderTextView();
                        highsHeading_model.setText("Location");
                        highsHeading_model.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_model.setGravity(Gravity.LEFT);

                        TextView highsHeading_KITTY = initPlainHeaderTextView();
                        highsHeading_KITTY.setText("KITTY");
                        highsHeading_KITTY.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_KITTY.setGravity(Gravity.CENTER);

                        TextView highsHeading_noofbills = initPlainHeaderTextView();
                        highsHeading_noofbills.setText("Order");
                        highsHeading_noofbills.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_noofbills.setGravity(Gravity.CENTER);

                        TextView highsHeading_va = initPlainHeaderTextView();
                        highsHeading_va.setText("Qty");
                        highsHeading_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_va.setGravity(Gravity.CENTER);

                        TextView highsHeading_ra = initPlainHeaderTextView();
                        highsHeading_ra.setText("Amount");
                        highsHeading_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_ra.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_model);
                        //tblrowHeading.addView(highsHeading_KITTY);

                        tblrowHeading.addView(highsHeading_noofbills);
                        tblrowHeading.addView(highsHeading_va);
                        if (groupcode.equals("SAG")) {
                            tblrowHeading.addView(highsHeading_ra);
                        }
                        table.addView(tblrowHeading);

                        int fnoofbills;
                        int fquantity;
                        int finvoice_amount;
                        int kitty_amount;

                        fnoofbills = 0;
                        fquantity = 0;
                        finvoice_amount = 0;
                        kitty_amount = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("todays_sales");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                final TextView highsLabel_model = initPlainTextView(i);

                                highsLabel_model.setText(jsonObject.getString("stocklocation"));
                                highsLabel_model.setTypeface(Typeface.DEFAULT);
                                highsLabel_model.setGravity(Gravity.LEFT);


                                //tblrowLabels.setBackgroundResource(R.color.black_overlay);
                                TextView highsLabel_kitty = initPlainTextView(i);
                                highsLabel_kitty.setText(jsonObject.getString("kitty"));
                                highsLabel_kitty.setTypeface(Typeface.DEFAULT);
                                highsLabel_kitty.setGravity(Gravity.CENTER);

                                TextView highsLabel_noofbills = initPlainTextView(i);
                                highsLabel_noofbills.setText(jsonObject.getString("noofbills"));
                                highsLabel_noofbills.setTypeface(Typeface.DEFAULT);
                                highsLabel_noofbills.setGravity(Gravity.CENTER);

                                TextView highsLabel_va = initPlainTextView(i);
                                highsLabel_va.setText(jsonObject.getString("quantity"));
                                highsLabel_va.setTypeface(Typeface.DEFAULT);
                                highsLabel_va.setGravity(Gravity.CENTER);

                                TextView highsLabel_ra = initPlainTextView(i);
                                highsLabel_ra.setText(jsonObject.getString("invoice_amount"));
                                highsLabel_ra.setTypeface(Typeface.DEFAULT);
                                highsLabel_ra.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_model);
//                                    tblrowLabels.addView(highsLabel_kitty);
                                tblrowLabels.addView(highsLabel_noofbills);
                                tblrowLabels.addView(highsLabel_va);
                                if (groupcode.equals("SAG")) {
                                    tblrowLabels.addView(highsLabel_ra);
                                }


                                tblrowLabels.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, SalesReportActivity.class);
                                        intent.putExtra("locationname", highsLabel_model.getText());
                                        intent.putExtra("fromdate", fromdate);
                                        intent.putExtra("todate", todate);
                                        intent.putExtra("type", "ORDER");

                                        intent.putExtra("quantity", highsLabel_va.getText());
                                        intent.putExtra("value", highsLabel_ra.getText());

                                        startActivity(intent);
                                    }
                                });

                                table.addView(tblrowLabels);

                                //             kitty_amount += Integer.valueOf(0+jsonObject.getString("kitty"));
                                fnoofbills += Integer.valueOf(0 + jsonObject.getString("noofbills"));
                                fquantity += Integer.valueOf(0 + jsonObject.getString("quantity"));
                                finvoice_amount += Integer.valueOf(0 + jsonObject.getString("invoice_amount"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_kitty = initPlainFooterTextView();
                        highsFooter_kitty.setText(String.valueOf(kitty_amount));
                        highsFooter_kitty.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_kitty.setGravity(Gravity.CENTER);

                        TextView highsFooter_noofbills = initPlainFooterTextView();
                        highsFooter_noofbills.setText(String.valueOf(fnoofbills));
                        highsFooter_noofbills.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_noofbills.setGravity(Gravity.CENTER);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fquantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.CENTER);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(finvoice_amount));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        //                      tblrowFooter.addView(highsFooter_kitty);
                        tblrowFooter.addView(highsFooter_noofbills);
                        tblrowFooter.addView(highsFooter_va);
                        if (groupcode.equals("SAG")) {
                            tblrowFooter.addView(highsFooter_ra);
                        }
                        table.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    public void invokeWS_TodaySalesInvoice() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("sysemployeeno", "0" + sysemployeeno);


            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/MobileTodays_SalesDetails_Invoices", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_salesstatusInvoice = (TextView) findViewById(R.id.header_salesstatusInvoice);
                        header_salesstatusInvoice.setText("Todays Sales Invoices ");

                        table = (TableLayout) findViewById(R.id.tableSalesInvoice);

                        table.setStretchAllColumns(true);
                        //table.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_model = initPlainHeaderTextView();
                        highsHeading_model.setText("Location");
                        highsHeading_model.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_model.setGravity(Gravity.LEFT);

                        TextView highsHeading_KITTY = initPlainHeaderTextView();
                        highsHeading_KITTY.setText("KITTY");
                        highsHeading_KITTY.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_KITTY.setGravity(Gravity.CENTER);

                        TextView highsHeading_noofbills = initPlainHeaderTextView();
                        highsHeading_noofbills.setText("Invoices");
                        highsHeading_noofbills.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_noofbills.setGravity(Gravity.CENTER);

                        TextView highsHeading_va = initPlainHeaderTextView();
                        highsHeading_va.setText("Qty");
                        highsHeading_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_va.setGravity(Gravity.CENTER);

                        TextView highsHeading_ra = initPlainHeaderTextView();
                        highsHeading_ra.setText("Amount");
                        highsHeading_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_ra.setGravity(Gravity.RIGHT);


                        tblrowHeading.addView(highsHeading_model);
                        //tblrowHeading.addView(highsHeading_KITTY);

                        tblrowHeading.addView(highsHeading_noofbills);
                        tblrowHeading.addView(highsHeading_va);
                        if (groupcode.equals("SAG")) {
                            tblrowHeading.addView(highsHeading_ra);
                        }
                        table.addView(tblrowHeading);

                        int fnoofbills;
                        int fquantity;
                        int finvoice_amount;
                        int kitty_amount;

                        fnoofbills = 0;
                        fquantity = 0;
                        finvoice_amount = 0;
                        kitty_amount = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("todays_sales");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                final TextView highsLabel_model = initPlainTextView(i);

                                highsLabel_model.setText(jsonObject.getString("stocklocation"));
                                highsLabel_model.setTypeface(Typeface.DEFAULT);
                                highsLabel_model.setGravity(Gravity.LEFT);


                                //tblrowLabels.setBackgroundResource(R.color.black_overlay);
                                TextView highsLabel_kitty = initPlainTextView(i);
                                highsLabel_kitty.setText(jsonObject.getString("kitty"));
                                highsLabel_kitty.setTypeface(Typeface.DEFAULT);
                                highsLabel_kitty.setGravity(Gravity.CENTER);

                                TextView highsLabel_noofbills = initPlainTextView(i);
                                highsLabel_noofbills.setText(jsonObject.getString("noofbills"));
                                highsLabel_noofbills.setTypeface(Typeface.DEFAULT);
                                highsLabel_noofbills.setGravity(Gravity.CENTER);

                                TextView highsLabel_va = initPlainTextView(i);
                                highsLabel_va.setText(jsonObject.getString("quantity"));
                                highsLabel_va.setTypeface(Typeface.DEFAULT);
                                highsLabel_va.setGravity(Gravity.CENTER);

                                TextView highsLabel_ra = initPlainTextView(i);
                                highsLabel_ra.setText(jsonObject.getString("invoice_amount"));
                                highsLabel_ra.setTypeface(Typeface.DEFAULT);
                                highsLabel_ra.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_model);
//                                    tblrowLabels.addView(highsLabel_kitty);
                                tblrowLabels.addView(highsLabel_noofbills);
                                tblrowLabels.addView(highsLabel_va);
                                if (groupcode.equals("SAG")) {
                                    tblrowLabels.addView(highsLabel_ra);
                                }


                                //uncomment for sjt - Block the TodayInvoice table row click
                                tblrowLabels.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, SalesReportActivity.class);
                                        intent.putExtra("locationname", highsLabel_model.getText());
                                        intent.putExtra("fromdate", fromdate);
                                        intent.putExtra("todate", todate);
                                        intent.putExtra("type", "INVOICE");
                                        intent.putExtra("quantity", highsLabel_va.getText());
                                        intent.putExtra("value", highsLabel_ra.getText());


                                        startActivity(intent);
                                    }
                                });


                                table.addView(tblrowLabels);

                                // kitty_amount += Integer.valueOf(0+jsonObject.getString("kitty"));
                                fnoofbills += Integer.valueOf(0 + jsonObject.getString("noofbills"));
                                fquantity += Integer.valueOf(0 + jsonObject.getString("quantity"));
                                finvoice_amount += Integer.valueOf(0 + jsonObject.getString("invoice_amount"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_kitty = initPlainFooterTextView();
                        highsFooter_kitty.setText(String.valueOf(kitty_amount));
                        highsFooter_kitty.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_kitty.setGravity(Gravity.CENTER);

                        TextView highsFooter_noofbills = initPlainFooterTextView();
                        highsFooter_noofbills.setText(String.valueOf(fnoofbills));
                        highsFooter_noofbills.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_noofbills.setGravity(Gravity.CENTER);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fquantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.CENTER);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(finvoice_amount));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        //                      tblrowFooter.addView(highsFooter_kitty);
                        tblrowFooter.addView(highsFooter_noofbills);
                        tblrowFooter.addView(highsFooter_va);
                        if (groupcode.equals("SAG")) {
                            tblrowFooter.addView(highsFooter_ra);
                        }
                        table.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    public void invokeWS_TodaySales_Negetive() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            if (groupcode.equals("SAG")) {
                paramsMap.put("sysemployeeno", "0");
            } else {
                paramsMap.put("sysemployeeno", "0" + sysemployeeno);
            }

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/MobileTodays_Negetive_SalesDetails", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_salesstatusNegetive = (TextView) findViewById(R.id.header_salesstatusNegetive);
                        header_salesstatusNegetive.setText("Todays Negetive Sales");

                        table = (TableLayout) findViewById(R.id.tableSalesNegetive);

                        table.setStretchAllColumns(true);
                        //table.setShrinkAllColumns(true);

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_model = initPlainHeaderTextView();
                        highsHeading_model.setText("Location");
                        highsHeading_model.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_model.setGravity(Gravity.LEFT);

                        TextView highsHeading_noofbills = initPlainHeaderTextView();
                        highsHeading_noofbills.setText("No of Order");
                        highsHeading_noofbills.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_noofbills.setGravity(Gravity.CENTER);

                        TextView highsHeading_va = initPlainHeaderTextView();
                        highsHeading_va.setText("Quantity");
                        highsHeading_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_va.setGravity(Gravity.CENTER);

                        TextView highsHeading_ra = initPlainHeaderTextView();
                        highsHeading_ra.setText("Invoice Amount");
                        highsHeading_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_ra.setGravity(Gravity.RIGHT);


                        tblrowHeading.addView(highsHeading_model);
                        //tblrowHeading.addView(highsHeading_noofbills);
                        tblrowHeading.addView(highsHeading_va);
                        if (groupcode.equals("SAG")) {
                            tblrowHeading.addView(highsHeading_ra);
                        }
                        table.addView(tblrowHeading);

                        int fnoofbills;
                        int fquantity;
                        int finvoice_amount;

                        fnoofbills = 0;
                        fquantity = 0;
                        finvoice_amount = 0;


                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("todays_sales");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                final TextView highsLabel_model = initPlainTextView(i);

                                highsLabel_model.setText(jsonObject.getString("stocklocation"));
                                highsLabel_model.setTypeface(Typeface.DEFAULT);
                                highsLabel_model.setGravity(Gravity.LEFT);
                                highsLabel_model.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, SalesActivity_Daily_Negetive.class);
                                        intent.putExtra("locationname", highsLabel_model.getText());
                                        intent.putExtra("fromdate", fromdate);
                                        intent.putExtra("todate", todate);
                                        startActivity(intent);
                                    }
                                });

                                //tblrowLabels.setBackgroundResource(R.color.black_overlay);

                                TextView highsLabel_noofbills = initPlainTextView(i);
                                highsLabel_noofbills.setText(jsonObject.getString("noofbills"));
                                highsLabel_noofbills.setTypeface(Typeface.DEFAULT);
                                highsLabel_noofbills.setGravity(Gravity.CENTER);

                                TextView highsLabel_va = initPlainTextView(i);
                                highsLabel_va.setText(jsonObject.getString("quantity"));
                                highsLabel_va.setTypeface(Typeface.DEFAULT);
                                highsLabel_va.setGravity(Gravity.CENTER);

                                TextView highsLabel_ra = initPlainTextView(i);
                                highsLabel_ra.setText(jsonObject.getString("invoice_amount"));
                                highsLabel_ra.setTypeface(Typeface.DEFAULT);
                                highsLabel_ra.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_model);
                                //tblrowLabels.addView(highsLabel_noofbills);
                                tblrowLabels.addView(highsLabel_va);
                                if (groupcode.equals("SAG")) {
                                    tblrowLabels.addView(highsLabel_ra);
                                }
                                table.addView(tblrowLabels);

                                fnoofbills += Integer.valueOf(0 + jsonObject.getString("noofbills"));
                                fquantity += Integer.valueOf(0 + jsonObject.getString("quantity"));
                                finvoice_amount += Integer.valueOf(0 + jsonObject.getString("invoice_amount"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_noofbills = initPlainFooterTextView();
                        highsFooter_noofbills.setText(String.valueOf(fnoofbills));
                        highsFooter_noofbills.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_noofbills.setGravity(Gravity.CENTER);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fquantity));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.CENTER);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(finvoice_amount));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        //tblrowFooter.addView(highsFooter_noofbills);
                        tblrowFooter.addView(highsFooter_va);
                        if (groupcode.equals("SAG")) {
                            tblrowFooter.addView(highsFooter_ra);
                        }
                        table.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    public void invokeWS_Location_PaymentMode_Balances() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("sysuserno", sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/GetDashboard_Location_PaymentMode_Balances", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_cashbalance = (TextView) findViewById(R.id.header_cashbalance);
                        header_cashbalance.setText("Location Cash Balance");

                        tablecashbalance = (TableLayout) findViewById(R.id.tablecashbalance);

                        tablecashbalance.setStretchAllColumns(true);
                        tablecashbalance.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(MainActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        LayoutParams params = new LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_Credit = initPlainHeaderTextView();
                        highsHeading_Credit.setText("Credit");
                        highsHeading_Credit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Credit.setGravity(Gravity.RIGHT);

                        TextView highsHeading_Debit = initPlainHeaderTextView();
                        highsHeading_Debit.setText("Debit");
                        highsHeading_Debit.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Debit.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_Credit);
                        tblrowHeading.addView(highsHeading_Debit);

                        tablecashbalance.addView(tblrowHeading);

                        double fbalanceamt;
                        double fCredit;
                        double fDebit;

                        fbalanceamt = 0.00;
                        fCredit = 0.00;
                        fDebit = 0.00;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("paymentmode_balances");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("modedesc"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_CREDIT = initPlainTextView(i);
                                highsLabel_CREDIT.setText(jsonObject.getString("CREDIT"));
                                highsLabel_CREDIT.setTypeface(Typeface.DEFAULT);
                                highsLabel_CREDIT.setGravity(Gravity.RIGHT);

                                TextView highsLabel_DEBIT = initPlainTextView(i);
                                highsLabel_DEBIT.setText(jsonObject.getString("DEBIT"));
                                highsLabel_DEBIT.setTypeface(Typeface.DEFAULT);
                                highsLabel_DEBIT.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_CREDIT);
                                tblrowLabels.addView(highsLabel_DEBIT);

                                tablecashbalance.addView(tblrowLabels);

                                fCredit += Double.valueOf(jsonObject.getString("CREDIT"));
                                fDebit += Double.valueOf(jsonObject.getString("DEBIT"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_CREDIT = initPlainFooterTextView();
                        highsFooter_CREDIT.setText(String.valueOf(fCredit));
                        highsFooter_CREDIT.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_CREDIT.setGravity(Gravity.RIGHT);

                        TextView highsFooter_DEBIT = initPlainFooterTextView();
                        highsFooter_DEBIT.setText(String.valueOf(fDebit));
                        highsFooter_DEBIT.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_DEBIT.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_CREDIT);
                        tblrowFooter.addView(highsFooter_DEBIT);

                        tablecashbalance.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }


    public void invokeWS_FinanceOs() {

        try {
            Map<String, String> paramsMap = new HashMap<>();


            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("sysuserno", sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_Finance_OS_Details", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_finos = (TextView) findViewById(R.id.header_finos);
                        header_finos.setText("Finance Outstanding");

                        tableFinanceos = (TableLayout) findViewById(R.id.tableFinanceos);

                        tableFinanceos.setStretchAllColumns(true);
                        tableFinanceos.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(MainActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                        LayoutParams params = new LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Finance");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_CH = initPlainHeaderTextView();
                        highsHeading_CH.setText("CH");
                        highsHeading_CH.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_CH.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_CH);

                        tableFinanceos.addView(tblrowHeading);

                        int fCH_balanceamt;

                        fCH_balanceamt = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("financeos");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);
/*
                                    View.OnTouchListener rowClick = new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event)
                                        {
                                            if(lastRowId>0){
                                                ((TableRow)findViewById(lastRowId)).setBackgroundResource(R.color.colorPrimary);
                                            }
                                            lastRowId = v.getId();
                                            v.setBackgroundResource(R.color.red);
                                            return true;
                                        }
                                    };
*/
                                // TextView highsLabel_model = new TextView(MainActivity.this);

                                TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("Finance"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_CH = initPlainTextView(i);
                                highsLabel_CH.setText(jsonObject.getString("CH"));
                                highsLabel_CH.setTypeface(Typeface.DEFAULT);
                                highsLabel_CH.setGravity(Gravity.RIGHT);


                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_CH);

                                tableFinanceos.addView(tblrowLabels);

                                fCH_balanceamt += Integer.valueOf(0 + jsonObject.getString("CH_balanceamt"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //    LinearLayout sv = new LinearLayout(MainActivity.this);

                        //        sv.addView(table);

                        //hsw.addView(sv);
                        //setContentView(hsw);
                        // setContentView(table);

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_CH = initPlainFooterTextView();
                        highsFooter_CH.setText(String.valueOf(fCH_balanceamt));
                        highsFooter_CH.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_CH.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_CH);

                        tableFinanceos.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }

    public void invokeWS_LocationOs() {

        try {
            Map<String, String> paramsMap = new HashMap<>();


            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("sysuserno", sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Mobile_LocationOsSummary", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_locationos = (TextView) findViewById(R.id.header_locationos);
                        header_locationos.setText("Location Outstanding");

                        tableLocationos = (TableLayout) findViewById(R.id.tableLocationos);

                        tableLocationos.setStretchAllColumns(true);
                        tableLocationos.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(MainActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                        LayoutParams params = new LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_cash = initPlainHeaderTextView();
                        highsHeading_cash.setText("Cash");
                        highsHeading_cash.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_cash.setGravity(Gravity.RIGHT);

                        TextView highsheading_cheque = initPlainHeaderTextView();
                        highsheading_cheque.setText("Bank");
                        highsheading_cheque.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_cheque.setGravity(Gravity.RIGHT);

                        TextView highsheading_creditcard = initPlainHeaderTextView();
                        highsheading_creditcard.setText("Swipe");
                        highsheading_creditcard.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_creditcard.setGravity(Gravity.RIGHT);

                        TextView highsheading_finnance = initPlainHeaderTextView();
                        highsheading_finnance.setText("Finance");
                        highsheading_finnance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_finnance.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_cash);
                        tblrowHeading.addView(highsheading_creditcard);
                        tblrowHeading.addView(highsheading_finnance);

                        tableLocationos.addView(tblrowHeading);


                        int fCASH;
                        int fCHEQUE;
                        int fCREDITCARD;
                        int fFINNANCE;

                        fCASH = 0;
                        fCHEQUE = 0;
                        fCREDITCARD = 0;
                        fFINNANCE = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("location_outstanding");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("stocklocation"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_cash = initPlainTextView(i);
                                highsLabel_cash.setText(jsonObject.getString("cash"));
                                highsLabel_cash.setTypeface(Typeface.DEFAULT);
                                highsLabel_cash.setGravity(Gravity.RIGHT);

                                TextView highslabel_cheque = initPlainTextView(i);
                                highslabel_cheque.setText(jsonObject.getString("cheque"));
                                highslabel_cheque.setTypeface(Typeface.DEFAULT);
                                highslabel_cheque.setGravity(Gravity.RIGHT);

                                TextView highslabel_creditcard = initPlainTextView(i);
                                highslabel_creditcard.setText(jsonObject.getString("creditcard"));
                                highslabel_creditcard.setTypeface(Typeface.DEFAULT);
                                highslabel_creditcard.setGravity(Gravity.RIGHT);

                                TextView highslabel_finnance = initPlainTextView(i);
                                highslabel_finnance.setText(jsonObject.getString("finnance"));
                                highslabel_finnance.setTypeface(Typeface.DEFAULT);
                                highslabel_finnance.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_cash);
                                tblrowLabels.addView(highslabel_creditcard);
                                tblrowLabels.addView(highslabel_finnance);

                                tableLocationos.addView(tblrowLabels);

                                //  fCASH += Integer.valueOf(0+jsonObject.getString("cash"));
                                //  fCHEQUE += Integer.valueOf(0+jsonObject.getString("cheque"));
                                //  fCREDITCARD += Integer.valueOf(0+jsonObject.getString("creditcard"));
                                // fFINNANCE += Integer.valueOf(0+jsonObject.getString("finnance"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fCASH));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(fCHEQUE));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra1 = initPlainFooterTextView();
                        highsFooter_ra1.setText(String.valueOf(fCREDITCARD));
                        highsFooter_ra1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra1.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra2 = initPlainFooterTextView();
                        highsFooter_ra2.setText(String.valueOf(fFINNANCE));
                        highsFooter_ra2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra2.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_va);
                        tblrowFooter.addView(highsFooter_ra1);
                        tblrowFooter.addView(highsFooter_ra2);

//                        tableLocationos.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }


    public void invokeWS_PendingTask() {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno = globalVariable.getSysuserno();

            paramsMap.put("sysuserno", sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Location_DeliveryStatus", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_locationtask = (TextView) findViewById(R.id.header_locationtask);
                        header_locationtask.setText("Location Delivery Status");

                        tableLocationTask = (TableLayout) findViewById(R.id.tableLocationTask);

                        tableLocationTask.setStretchAllColumns(true);
                        tableLocationTask.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(MainActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                        LayoutParams params = new LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_Billed = initPlainHeaderTextView();
                        highsHeading_Billed.setText("Billed");
                        highsHeading_Billed.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_Billed.setGravity(Gravity.RIGHT);

                        TextView highsheading_Pending = initPlainHeaderTextView();
                        highsheading_Pending.setText("Pending");
                        highsheading_Pending.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Pending.setGravity(Gravity.RIGHT);

                        TextView highsheading_Delivered = initPlainHeaderTextView();
                        highsheading_Delivered.setText("Delivered");
                        highsheading_Delivered.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Delivered.setGravity(Gravity.RIGHT);

                        TextView highsheading_Opening = initPlainHeaderTextView();
                        highsheading_Opening.setText("Opening");
                        highsheading_Opening.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Opening.setGravity(Gravity.RIGHT);

                        TextView highsheading_Total = initPlainHeaderTextView();
                        highsheading_Total.setText("Total");
                        highsheading_Total.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_Total.setGravity(Gravity.RIGHT);



                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsheading_Opening);
                        tblrowHeading.addView(highsHeading_Billed);
                        tblrowHeading.addView(highsheading_Delivered);
                        tblrowHeading.addView(highsheading_Pending);
                        tblrowHeading.addView(highsheading_Total);

                        tableLocationTask.addView(tblrowHeading);

                        int allcount;
                        int pending;
                        int delivered;
                        int openingpending;
                        int totalpending;

                        allcount = 0;
                        pending = 0;
                        delivered = 0;
                        openingpending = 0;
                        totalpending = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("locationpendingtask");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                // companycd, companyname, companysname, allcount, pending, delivered, openingpending, totalpending

                                final TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("companyname"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);

                                TextView highsLabel_allcount = initPlainTextView(i);
                                highsLabel_allcount.setText(jsonObject.getString("allcount"));
                                highsLabel_allcount.setTypeface(Typeface.DEFAULT);
                                highsLabel_allcount.setGravity(Gravity.RIGHT);

                                TextView highslabel_pending = initPlainTextView(i);
                                highslabel_pending.setText(jsonObject.getString("pending"));
                                highslabel_pending.setTypeface(Typeface.DEFAULT);
                                highslabel_pending.setGravity(Gravity.RIGHT);

                                TextView highslabel_delivered = initPlainTextView(i);
                                highslabel_delivered.setText(jsonObject.getString("delivered"));
                                highslabel_delivered.setTypeface(Typeface.DEFAULT);
                                highslabel_delivered.setGravity(Gravity.RIGHT);

                                TextView highslabel_openingpending = initPlainTextView(i);
                                highslabel_openingpending.setText(jsonObject.getString("openingpending"));
                                highslabel_openingpending.setTypeface(Typeface.DEFAULT);
                                highslabel_openingpending.setGravity(Gravity.RIGHT);

                                TextView highslabel_totalpending = initPlainTextView(i);
                                highslabel_totalpending.setText(jsonObject.getString("totalpending"));
                                highslabel_totalpending.setTypeface(Typeface.DEFAULT);
                                highslabel_totalpending.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highslabel_openingpending);
                                tblrowLabels.addView(highsLabel_allcount);
                                tblrowLabels.addView(highslabel_delivered);
                                tblrowLabels.addView(highslabel_pending);
                                tblrowLabels.addView(highslabel_totalpending);

                                tblrowLabels.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, SalesActivity_Task_Pending.class);
                                        intent.putExtra("locationname", highsLabel_location.getText());
                                        startActivity(intent);
                                    }
                                });

                                tableLocationTask.addView(tblrowLabels);

                                allcount += Integer.valueOf(jsonObject.getString("allcount"));
                                pending += Integer.valueOf(jsonObject.getString("pending"));
                                delivered += Integer.valueOf(jsonObject.getString("delivered"));
                                openingpending += Integer.valueOf(jsonObject.getString("openingpending"));
                                totalpending += Integer.valueOf(jsonObject.getString("totalpending"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_allcount = initPlainFooterTextView();
                        highsFooter_allcount.setText(String.valueOf(allcount));
                        highsFooter_allcount.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_allcount.setGravity(Gravity.RIGHT);

                        TextView highsFooter_pending = initPlainFooterTextView();
                        highsFooter_pending.setText(String.valueOf(pending));
                        highsFooter_pending.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_pending.setGravity(Gravity.RIGHT);

                        TextView highsFooter_delivered = initPlainFooterTextView();
                        highsFooter_delivered.setText(String.valueOf(delivered));
                        highsFooter_delivered.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_delivered.setGravity(Gravity.RIGHT);

                        TextView highsFooter_openingpending = initPlainFooterTextView();
                        highsFooter_openingpending.setText(String.valueOf(openingpending));
                        highsFooter_openingpending.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_openingpending.setGravity(Gravity.RIGHT);

                        TextView highsFooter_totalpending = initPlainFooterTextView();
                        highsFooter_totalpending.setText(String.valueOf(totalpending));
                        highsFooter_totalpending.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_totalpending.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_openingpending);
                        tblrowFooter.addView(highsFooter_allcount);
                        tblrowFooter.addView(highsFooter_delivered);
                        tblrowFooter.addView(highsFooter_pending);
                        tblrowFooter.addView(highsFooter_totalpending);

                        tableLocationTask.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    public void invokeWS_PendingCrmStatus() {

        try {

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String sysuserno = globalVariable.getSysuserno();

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("sysuserno", sysuserno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/Location_PendingCRMTask", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        header_locationcrmtask = (TextView) findViewById(R.id.header_locationcrmtask);
                        header_locationcrmtask.setText("Delivery Pending Status");

                        tableLocationcrmTask = (TableLayout) findViewById(R.id.tableLocationcrmTask);

                        tableLocationcrmTask.setStretchAllColumns(true);
                        tableLocationcrmTask.setShrinkAllColumns(true);

                        TableRow rowTitle = new TableRow(MainActivity.this);
                        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

                        // title column/row

                        LayoutParams params = new LayoutParams();
                        params.span = 6;

                        TableRow tblrowHeading = new TableRow(MainActivity.this);
                        TextView highsHeading_location = initPlainHeaderTextView();
                        highsHeading_location.setText("Location");
                        highsHeading_location.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_location.setGravity(Gravity.LEFT);

                        TextView highsHeading_cash = initPlainHeaderTextView();
                        highsHeading_cash.setText("Delivery");
                        highsHeading_cash.setTypeface(Typeface.DEFAULT_BOLD);
                        highsHeading_cash.setGravity(Gravity.RIGHT);

                        TextView highsheading_cheque = initPlainHeaderTextView();
                        highsheading_cheque.setText("Demo");
                        highsheading_cheque.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_cheque.setGravity(Gravity.RIGHT);

                        TextView highsheading_creditcard = initPlainHeaderTextView();
                        highsheading_creditcard.setText("Inst.");
                        highsheading_creditcard.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_creditcard.setGravity(Gravity.RIGHT);

                        TextView highsheading_finnance = initPlainHeaderTextView();
                        highsheading_finnance.setText("Comp.");
                        highsheading_finnance.setTypeface(Typeface.DEFAULT_BOLD);
                        highsheading_finnance.setGravity(Gravity.RIGHT);

                        tblrowHeading.addView(highsHeading_location);
                        tblrowHeading.addView(highsHeading_cash);
                        //tblrowHeading.addView(highsheading_cheque);
                        //tblrowHeading.addView(highsheading_creditcard);
                        //tblrowHeading.addView(highsheading_finnance);

                        tableLocationcrmTask.addView(tblrowHeading);

                        int fproduct_not_added;
                        int fproduct_added_but_not_invoiced;
                        int fdo_pending;
                        int fpurchase_pending;

                        fproduct_not_added = 0;
                        fproduct_added_but_not_invoiced = 0;
                        fdo_pending = 0;
                        fpurchase_pending = 0;

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("locationpendingcrmtask");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);

                                TableRow tblrowLabels = new TableRow(MainActivity.this);

                                final TextView highsLabel_location = initPlainTextView(i);
                                highsLabel_location.setText(jsonObject.getString("companyname"));
                                highsLabel_location.setTypeface(Typeface.DEFAULT);
                                highsLabel_location.setGravity(Gravity.LEFT);
                                highsLabel_location.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, SalesActivity_Delivery_Pending.class);
                                        intent.putExtra("locationname", highsLabel_location.getText());
                                        startActivity(intent);
                                    }
                                });

                                TextView highsLabel_cash = initPlainTextView(i);
                                highsLabel_cash.setText(jsonObject.getString("pending_delivery"));
                                highsLabel_cash.setTypeface(Typeface.DEFAULT);
                                highsLabel_cash.setGravity(Gravity.RIGHT);

                                TextView highslabel_cheque = initPlainTextView(i);
                                highslabel_cheque.setText(jsonObject.getString("pending_demo"));
                                highslabel_cheque.setTypeface(Typeface.DEFAULT);
                                highslabel_cheque.setGravity(Gravity.RIGHT);

                                TextView highslabel_creditcard = initPlainTextView(i);
                                highslabel_creditcard.setText(jsonObject.getString("pending_installation"));
                                highslabel_creditcard.setTypeface(Typeface.DEFAULT);
                                highslabel_creditcard.setGravity(Gravity.RIGHT);

                                TextView highslabel_finnance = initPlainTextView(i);
                                highslabel_finnance.setText(jsonObject.getString("pending_complaints"));
                                highslabel_finnance.setTypeface(Typeface.DEFAULT);
                                highslabel_finnance.setGravity(Gravity.RIGHT);

                                tblrowLabels.addView(highsLabel_location);
                                tblrowLabels.addView(highsLabel_cash);
                                //tblrowLabels.addView(highslabel_cheque);
                                //tblrowLabels.addView(highslabel_creditcard);
                                //tblrowLabels.addView(highslabel_finnance);

                                tableLocationcrmTask.addView(tblrowLabels);

                                fproduct_not_added += Integer.valueOf(0 + jsonObject.getString("pending_delivery"));
                                fproduct_added_but_not_invoiced += Integer.valueOf(0 + jsonObject.getString("pending_demo"));
                                fdo_pending += Integer.valueOf(0 + jsonObject.getString("pending_installation"));
                                fpurchase_pending += Integer.valueOf(0 + jsonObject.getString("pending_complaints"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TableRow tblrowFooter = new TableRow(MainActivity.this);
                        TextView highsFooter_category = initPlainFooterTextView();
                        highsFooter_category.setText("Total");
                        highsFooter_category.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_category.setGravity(Gravity.LEFT);

                        TextView highsFooter_va = initPlainFooterTextView();
                        highsFooter_va.setText(String.valueOf(fproduct_not_added));
                        highsFooter_va.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_va.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra = initPlainFooterTextView();
                        highsFooter_ra.setText(String.valueOf(fproduct_added_but_not_invoiced));
                        highsFooter_ra.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra1 = initPlainFooterTextView();
                        highsFooter_ra1.setText(String.valueOf(fdo_pending));
                        highsFooter_ra1.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra1.setGravity(Gravity.RIGHT);

                        TextView highsFooter_ra2 = initPlainFooterTextView();
                        highsFooter_ra2.setText(String.valueOf(fpurchase_pending));
                        highsFooter_ra2.setTypeface(Typeface.DEFAULT_BOLD);
                        highsFooter_ra2.setGravity(Gravity.RIGHT);

                        tblrowFooter.addView(highsFooter_category);
                        tblrowFooter.addView(highsFooter_va);
                        //tblrowFooter.addView(highsFooter_ra);
                        //tblrowFooter.addView(highsFooter_ra1);
                        //tblrowFooter.addView(highsFooter_ra2);

                        tableLocationcrmTask.addView(tblrowFooter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(MainActivity.this, "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    private TextView initPlainTextView(float n) {

        TextView textView = new TextView(MainActivity.this);
        textView.setPadding(10, 10, 10, 10);

        if ((n % 2) == 0) {
            textView.setBackgroundResource(R.drawable.cell_shape);
        } else {
            textView.setBackgroundResource(R.drawable.cell_shape_oddrow);
        }

        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    private TextView initPlainHeaderTextView() {
        TextView textView = new TextView(MainActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_header);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    private TextView initPlainFooterTextView() {
        TextView textView = new TextView(MainActivity.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(R.drawable.cell_shape_footer);
        textView.setTextColor(Color.parseColor("#ffffff"));
        return textView;
    }

    //  THIS TAKES THE SPEECH AND PUTS IT IN A ARRAY LIST AND THEN DISPLAYS IT IN A TOAST FOR NOW//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                        Toast.makeText(getApplicationContext(), "Feature not supported in your deavice", Toast.LENGTH_LONG).show();
                    else {
                        Log.i("voice", text.get(0));
                        Toast.makeText(MainActivity.this, text.get(0), Toast.LENGTH_LONG).show();

                        if (text.get(0).equals("dashboard")) {
                            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("create purchase")) {
                            Intent homeIntent = new Intent(MainActivity.this, Procurement.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("purchase invoice")) {
                            Intent homeIntent = new Intent(MainActivity.this, PurchaseInvoice_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("purchase book")) {
                            Intent homeIntent = new Intent(MainActivity.this, PurchaseActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("purchase summery")) {
                            Intent homeIntent = new Intent(MainActivity.this, PurchaseAnalysisCategory.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("schemes")) {
                            Intent customerIntent = new Intent(MainActivity.this, SchemeStatus.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("create lead")) {
                            Intent customerIntent = new Intent(MainActivity.this, Salesmen_Customer_Search.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("lead register")) {
                            Intent homeIntent = new Intent(MainActivity.this, CrmActivityEmployee.class);
                            //   homeIntent.putExtra("status","PENDING");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("quotation")) {
                            Intent homeIntent = new Intent(MainActivity.this, Quotation_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("order")) {
                            Intent homeIntent = new Intent(MainActivity.this, Order_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        if (text.get(0).equals("get finance do")) {
                            Intent homeIntent = new Intent(MainActivity.this, Order_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("cash sales")) {
                            Intent homeIntent = new Intent(MainActivity.this, Salesmen_SalesOrder.class);

                            homeIntent.putExtra("sysinvorderno", "0");
                            homeIntent.putExtra("custname", "");
                            homeIntent.putExtra("vinvoicedt", "");
                            homeIntent.putExtra("netinvoiceamt", "0");
                            homeIntent.putExtra("invorderno", "");

                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        if (text.get(0).equals("approval awaiting")) {
                            Intent homeIntent = new Intent(MainActivity.this, OrderApprovalPending_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        if (text.get(0).equals("invoice")) {
                            Intent homeIntent = new Intent(MainActivity.this, Invoice_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("sales book")) {
                            Intent customerIntent = new Intent(MainActivity.this, SalesActivity.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("sales summary")) {
                            Intent homeIntent = new Intent(MainActivity.this, SalesAnalysisCategory.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("event sales summary")) {
                            Intent homeIntent = new Intent(MainActivity.this, SalesAnalysisEvent.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("salesmen performance")) {
                            Intent homeIntent = new Intent(MainActivity.this, SalesPerformance_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                        if (text.get(0).equals("sales target summary")) {
                            Intent homeIntent = new Intent(MainActivity.this, OutboxActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("serial profitability")) {
                            Intent customerIntent = new Intent(MainActivity.this, ProfitabilityActivity.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }


                        if (text.get(0).equals("b2b sales book")) {
                            Intent homeIntent = new Intent(MainActivity.this, B2BActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("b2b ageing wise outstanding ")) {
                            Intent homeIntent = new Intent(MainActivity.this, B2BOutstandingLocationActivity.class);
                            homeIntent.putExtra("LOCATIONTYPE", "BB");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("b2b sales summary")) {
                            Intent customerIntent = new Intent(MainActivity.this, SalesAnalysisB2BCustomer.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("dealer target")) {
                            Intent customerIntent = new Intent(MainActivity.this, DealerSchemeStatus.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("dealer ledger")) {
                            Intent customerIntent = new Intent(MainActivity.this, SalesAnalysisB2BCustomer.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("brand wise stock")) {
                            Intent homeIntent = new Intent(MainActivity.this, StockActivityBrand.class);
                            homeIntent.putExtra("categoryname", "");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("category wise stock")) {
                            Intent customerIntent = new Intent(MainActivity.this, StockActivityCategory.class);
                            customerIntent.putExtra("brandname", "");
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);

                        }
                        if (text.get(0).equals("model")) {
                            Intent homeIntent = new Intent(MainActivity.this, model_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);

                        }

                        if (text.get(0).equals("serial status")) {
                            Intent homeIntent = new Intent(MainActivity.this, serial_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("stock movement")) {
                            Intent customerIntent = new Intent(MainActivity.this, LocationStockSummery_Daily_List.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("location stock sheet")) {
                            Intent customerIntent = new Intent(MainActivity.this, StockActivityLocationBrand.class);
                            customerIntent.putExtra("categoryname", "");
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("ageing stock")) {
                            Intent homeIntent = new Intent(MainActivity.this, StockActivityAgeingLocationBrand.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("transfer issue")) {
                            Intent homeIntent = new Intent(MainActivity.this, TransferStock.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("transfer receipt")) {
                            Intent homeIntent = new Intent(MainActivity.this, TransferSerial_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("add serial in order")) {
                            Intent customerIntent = new Intent(MainActivity.this, pendingdelivery_view.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("opening stock")) {
                            Intent homeIntent = new Intent(MainActivity.this, StockVerification.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        if (text.get(0).equals("delivery status")) {
                            Intent homeIntent = new Intent(getApplicationContext(), OrderDeliveryPending_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("confirm delivery")) {
                            Intent homeIntent = new Intent(MainActivity.this, confirmdelivery_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        if (text.get(0).equals("allocation")) {
                            Intent homeIntent = new Intent(MainActivity.this, CrmActivity_Installation_Allocation.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("call register")) {
                            Intent homeIntent = new Intent(getApplicationContext(), Service_Call_Status_Summary.class);
                            homeIntent.putExtra("servicecentercd", "0");
                            homeIntent.putExtra("servicecentername", "");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("center summary")) {
                            Intent homeIntent = new Intent(MainActivity.this, Service_Call_Service_Center_Summary.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("dealer summary")) {
                            Intent homeIntent = new Intent(MainActivity.this, Service_Call_Dealer_Summary.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("create complaint")) {
                            Intent customerIntent = new Intent(MainActivity.this, CreateCustomerComplaint.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                    /*
                    if (text.get(0).equals("complaint summary")    {
                        Intent customerIntent = new Intent(MainActivity.this,ComplaintsActivity.class);
                        customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(customerIntent);
                    }
*/

                        if (text.get(0).equals("complaint register")) {
                            Intent homeIntent = new Intent(MainActivity.this, CrmActivityComplaintsRegister.class);
                            homeIntent.putExtra("status", "PENDING");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("cash on delivery")) {
                            Intent homeIntent = new Intent(MainActivity.this, CrmActivityCOD_Register.class);
                            homeIntent.putExtra("status", "PENDING");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("inward cheques")) {
                            Intent homeIntent = new Intent(MainActivity.this, CrmActivityPDCCheque_Register.class);
                            homeIntent.putExtra("status", "PENDING");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("finance schedule")) {
                            Intent homeIntent = new Intent(MainActivity.this, FinanceRecoveryScheduleActivity.class);
                            homeIntent.putExtra("status", "PENDING");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("customer")) {
                            Intent homeIntent = new Intent(MainActivity.this, customer_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            homeIntent.putExtra("calledfrom", "LEDGER");
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("supplier")) {
                            Intent homeIntent = new Intent(MainActivity.this, supplier_view.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("cash")) {
                            Intent customerIntent = new Intent(MainActivity.this, maincash_view.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            customerIntent.putExtra("legdertype", "CAL");
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("finance")) {
                            Intent customerIntent = new Intent(MainActivity.this, Ledger_finance.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }


                        if (text.get(0).equals("account ledger")) {
                            Intent homeIntent = new Intent(MainActivity.this, FA_Account_Ledger.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        if (text.get(0).equals("outstanding summary")) {
                            Intent homeIntent = new Intent(MainActivity.this, OutstandingLocationActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("customer outstanding")) {

                            Intent homeIntent = new Intent(MainActivity.this, OutstandingLocationActivity.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                        if (text.get(0).equals("supplier outstanding")) {

                            Intent homeIntent = new Intent(MainActivity.this, OutstandingSupplierActivity.class);
                            homeIntent.putExtra("type", "GROUP");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("finance outstanding")) {
                            Intent homeIntent = new Intent(MainActivity.this, Outstanding_finance.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("outstanding ageing")) {
                            Intent homeIntent = new Intent(MainActivity.this, B2BOutstandingLocationActivity.class);
                            homeIntent.putExtra("LOCATIONTYPE", "SH");
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }


                        if (text.get(0).equals("YTD")) {

                            Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                            customerIntent.putExtra("VALUETYPE", "YTD");
                            customerIntent.putExtra("YearType", "CALENDER");
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }


                        if (text.get(0).equals("MTD")) {
                            Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                            customerIntent.putExtra("VALUETYPE", "MTD");
                            customerIntent.putExtra("YearType", "CALENDER");
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);

                        }

                        if (text.get(0).equals("DTD")) {
                            Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                            customerIntent.putExtra("VALUETYPE", "DTD");
                            customerIntent.putExtra("YearType", "CALENDER");
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);

                        }

                        if (text.get(0).equals("growth over last month")) {
                            Intent customerIntent = new Intent(MainActivity.this, KPIActivity.class);
                            customerIntent.putExtra("VALUETYPE", "GOLM");
                            customerIntent.putExtra("YearType", "CALENDER");
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }


                    /*
                    if (CheckMenuPermissson(response,"")) {

                    }*/
                        if (text.get(0).equals("group summary")) {
                            Intent intent = new Intent(MainActivity.this, FA_GroupSummary.class);
                            intent.putExtra("groupname", "");
                            intent.putExtra("fromdate", "");
                            intent.putExtra("todate", "");
                            startActivity(intent);
                        }
                        if (text.get(0).equals("day book")) {
                            Intent customerIntent = new Intent(MainActivity.this, DayBook.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }
                        if (text.get(0).equals("trial balance")) {
                            Intent customerIntent = new Intent(MainActivity.this, FA_TrialBalance.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }
                        if (text.get(0).equals("trading account")) {
                            Intent customerIntent = new Intent(MainActivity.this, FA_TradingAccount.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("profit and loss")) {
                            Intent customerIntent = new Intent(MainActivity.this, FA_Profit_LossAC.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }
                        if (text.get(0).equals("balance sheet")) {
                            Intent customerIntent = new Intent(MainActivity.this, FA_BalanceSheet.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("voucher statistics")) {
                            Intent customerIntent = new Intent(MainActivity.this, FA_VoucherStatistics.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }

                        if (text.get(0).equals("daily task")) {
                            Intent customerIntent = new Intent(MainActivity.this, ViewEmployeeTask.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }
                        if (text.get(0).equals("my document")) {
                            Intent homeIntent = new Intent(MainActivity.this, EmployeeDocuments.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                        if (text.get(0).equals("attendance")) {
                            Intent customerIntent = new Intent(MainActivity.this, EmployeeAttendanceCreate.class);
                            customerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(customerIntent);
                        }
                        if (text.get(0).equals("attandance report")) {
                            Intent homeIntent = new Intent(MainActivity.this, EmployeeAttendanceReport.class);
                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }

                    }

                } else {

                    //if(text == null)
                    //  toSpeech.speak("That was flames",TextToSpeech.QUEUE_FLUSH,null);
                    //else
                    toSpeech.speak("Sorry I could not hear you", TextToSpeech.QUEUE_FLUSH, null);

                }
                break;
            }
        }
    }
}