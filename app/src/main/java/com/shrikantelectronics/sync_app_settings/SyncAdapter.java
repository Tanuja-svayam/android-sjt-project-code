package com.shrikantelectronics.sync_app_settings;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.shrikantelectronics.ApiHelper;
import com.shrikantelectronics.Config;
import com.shrikantelectronics.DBHelper;
import com.shrikantelectronics.DBTables;
import com.shrikantelectronics.Event;
import com.shrikantelectronics.GlobalClass;
import com.shrikantelectronics.Notification;
import com.shrikantelectronics.R;
import com.shrikantelectronics.MainActivity;
import com.shrikantelectronics.ServiceAutoLauncher;
import com.shrikantelectronics.Utils;

/*
 * The real magic happens here.
 * This is adapter that will be called when system decide it is time to sync data.
 * Created by Kursulla on 07/09/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    //private static String URL = "http://182.70.113.254/Shrikant_WS/";
    private static String URL = "http://103.88.221.39/SJT_WS/";

    //private static String IMAGEURL = com.shrikantelectronics.Config.IMAGEURL_URL;
    private final String TAG = this.getClass().getSimpleName();

    private DBHelper dbHelper;
    public SQLiteDatabase sqLiteDatabase;

    private List<Notification> notifications;
    private Event event;
    private Notification notification;
    private int alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinute;
    private int notColor=0;
    public boolean mLocationPermissionGranted = false;

    ContentResolver contentResolver;
    private final AccountManager mAccountManager;
    private static final String NOTIFICATION_CHANNEL_ID = "my_sync_notification_channel_id";
    private static final int MOVIE_NOTIFICATION_ID      = 3004;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);

        dbHelper = new DBHelper(context);

    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        contentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
        dbHelper = new DBHelper(context);

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {

        //TODO get some data from the internet, api calls, etc.
        //TODO save the data to database, sqlite, couchbase, etc

        Log.i(TAG, "Call One: ");
        initVariables();

        Log.i(TAG, "Call Two: ");
        saveEventToLocalStorage();

        Log.i(TAG, "Call Three: ");
        notifyDataDownloaded();

    }

    /**
     * Send the notification message to the status bar
     */
    private void notifyDataDownloaded() {
        Context context = getContext();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.slogojpg)
                        .setContentTitle("Sync Adapter")
                        .setContentText("New Data Available from adaptor!")
                        .setChannelId(NOTIFICATION_CHANNEL_ID);

        // Opening the app when the user clicks on the notification.
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Sync Adapter", NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(channel);
            }

            mNotificationManager.notify(MOVIE_NOTIFICATION_ID, mBuilder.build()); // MOVIE_NOTIFICATION_ID allows you to update the notification later on.
        }
    }

/*----------------------------------------------------------------------------- */

    private void initVariables() {
        Log.i(TAG, "initVariables start: ");
        Context context = getContext();
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeZone(TimeZone.getDefault());
        alarmHour = mCal.get(Calendar.HOUR_OF_DAY);
        alarmMinute = mCal.get(Calendar.MINUTE);

//            try {
  //             mCal.setTime(Utils.eventDateFormat.parse(context.getIntent().getStringExtra("date")));
    //       } catch (ParseException e) {
      //         e.printStackTrace();
        //   }

        alarmYear = mCal.get(Calendar.YEAR);
        alarmMonth = mCal.get(Calendar.MONTH);
        alarmDay = mCal.get(Calendar.DAY_OF_MONTH);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }
        Log.i(TAG, "initVariables End: ");
    }

    public void saveEventToLocalStorage() {
        Context context = getContext();
        try {
            Log.i(TAG, "saveEventToLocalStorage start: ");
            final GlobalClass globalVariable = (GlobalClass) context;
            final String sysemployeeno = globalVariable.getsysemployeeno();

            Log.i(TAG, "Employee ID : " + sysemployeeno);

            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("sysemployeeno", "0"+sysemployeeno);

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);
            notifications = new ArrayList<>();

            //Log.i(TAG, "Webservice Shrikant Electronics: " + "http://182.70.113.254/Shrikant_WS/Service1.asmx/EventManager_GetEventList");
            Log.i(TAG, "Webservice Shrikant Electronics: " + "http://103.88.221.39/SJT_WS/Service1.asmx/EventManager_GetEventList");

            //ApiHelper.post("http://182.70.113.254/Shrikant_WS/Service1.asmx/EventManager_GetEventList", paramsMap, new ApiHelper.ApiCallback() {
            ApiHelper.post("http://103.88.221.39/SJT_WS/Service1.asmx/EventManager_GetEventList", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        Log.i(TAG, "API Successfull : " + response);

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("calander_get_events");

                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {

                                JSONObject jsonObject = new_array.getJSONObject(i);

                                if(dbHelper.readEventsByServerStatus(dbHelper.getWritableDatabase(), jsonObject.getString("sourcetable"), jsonObject.getString("sourcetablepk"))) {
                                    Context context = getContext();
                                    event = new Event();
                                    // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                    Date aDate = null;

                                    Log.i(TAG, "API Successfull : " + jsonObject.getString("event_title"));

                                    try {
                                        aDate = Utils.eventDateFormat.parse((String) jsonObject.getString("eventdateformat"));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.e("JainTraders", "An error has occurred while parsing the date string");
                                    }
                                    event.setTitle(jsonObject.getString("event_title"));
                                    if (jsonObject.getString("event_is_all_day").equals("1")) {
                                        event.setAllDay(TRUE);
                                    } else {
                                        event.setAllDay(FALSE);
                                    }

                                    event.setDate(Utils.eventDateFormat.format(aDate));
                                    event.setMonth(Utils.monthFormat.format(aDate));
                                    event.setYear(Utils.yearFormat.format(aDate));
                                    event.setTime(jsonObject.getString("event_time"));
                                    event.setDuration(jsonObject.getString("event_duration"));
                                    event.setNotify(!notifications.isEmpty());
                                    if (jsonObject.getString("event_is_recurring").equals("1")) {
                                        event.setRecurring(TRUE);
                                    } else {
                                        event.setRecurring(FALSE);
                                    }
                                    event.setRecurringPeriod(jsonObject.getString("recurring_pattern_type"));
                                    event.setNote(jsonObject.getString("event_note"));
                                    notColor = 0;
                                    if (notColor == 0) {
                                        notColor = Color.parseColor(jsonObject.getString("event_color"));
                                        event.setColor(notColor);
                                    } else {
                                        event.setColor(notColor);
                                    }
                                    event.setLocation(jsonObject.getString("event_location"));
                                    event.setPhoneNumber(jsonObject.getString("event_phone_number"));
                                    event.setMail(jsonObject.getString("event_mail"));
                                    event.setSourceTable(jsonObject.getString("sourcetable"));
                                    event.setSourceTablePk(jsonObject.getInt("sourcetablepk"));

                                    dbHelper.saveEvent(dbHelper.getWritableDatabase(), event);
                                    int event_id = getEventId(event.getTitle(), event.getDate(), event.getTime());
                                    notification = new Notification(event_id, 0, "At the time of event");
                                    dbHelper.saveNotification(dbHelper.getWritableDatabase(), notification);

                                    notifications = readNotifications(event_id);
                                    if (event.isNotify()) {
                                        Log.i(TAG, "Alarm Set : " + jsonObject.getString("event_title"));
                                        setAlarms();
                                    }
                                }
                            } catch (JSONException e) {
                                Log.i(TAG, "API Failed 1: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Log.i(TAG, "API Failed 2: " + e.getMessage());
                        //Toast.makeText(MainActivity.this, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(getContext(), "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(String errorMessage) {
                   Toast.makeText(getContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "API Failed 4: " + e.getMessage());
            e.printStackTrace();
            //	Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }


    private int getEventId(String eventTitle, String eventDate, String eventTime) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Event event = dbHelper.readEventByTimestamp(sqLiteDatabase, eventTitle, eventDate, eventTime);
        sqLiteDatabase.close();
        return event.getId();
    }

    private ArrayList<Notification> readNotifications(int eventId) {
        ArrayList<Notification> notifications = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.readEventNotifications(sqLiteDatabase, eventId);
        while (cursor.moveToNext()) {
            Notification notification = new Notification();
            notification.setId(cursor.getInt(cursor.getColumnIndex(DBTables.NOTIFICATION_ID)));
            notification.setEventId(cursor.getInt(cursor.getColumnIndex(DBTables.NOTIFICATION_EVENT_ID)));
            notification.setTime(cursor.getString(cursor.getColumnIndex(DBTables.NOTIFICATION_TIME)));
            notification.setChannelId(cursor.getInt(cursor.getColumnIndex(DBTables.NOTIFICATION_CHANNEL_ID)));
            notifications.add(notification);
        }

        return notifications;
    }


    private void setAlarms() {
        Context context = getContext();
        Calendar calendar = Calendar.getInstance();
        calendar.set(alarmYear, alarmMonth, alarmDay);

        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, 0);

        for (Notification notification : notifications) {
            Calendar aCal = (Calendar) calendar.clone();
            String notificationPreference = notification.getTime();

            if (notificationPreference.equals(context.getString(R.string._10_minutes_before))) {
                aCal.add(Calendar.MINUTE, -10);
            } else if (notificationPreference.equals(context.getString(R.string._1_hour_before))) {
                aCal.add(Calendar.HOUR_OF_DAY, -1);
            } else if (notificationPreference.equals(context.getString(R.string._1_day_before))) {
                aCal.add(Calendar.DAY_OF_MONTH, -1);
            } else {
                Log.i(TAG, "setAlarms: ");
            }

            setAlarm(notification, aCal.getTimeInMillis());
        }
    }

    private void setAlarm(Notification notification, long triggerAtMillis) {
        Context context = getContext();
        Intent intent = new Intent(context, ServiceAutoLauncher.class);
        intent.putExtra("eventTitle", event.getTitle());
        intent.putExtra("eventNote", event.getNote());
        intent.putExtra("eventColor", event.getColor());
        intent.putExtra("eventTimeStamp", event.getDate() + ", " + event.getTime());
        intent.putExtra("interval", getInterval(event.getRecurringPeriod()));
        intent.putExtra("notificationId", notification.getChannelId());
        intent.putExtra("soundName", "Indigo");

        Log.d(TAG, "setAlarm: " + notification.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notification.getId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    private String getInterval(String repeatingPeriod) {
        Context context = getContext();
        String interval = context.getString(R.string.one_time);

        if (repeatingPeriod.equals(context.getString(R.string.daily))) {
            interval = context.getString(R.string.daily);
        } else if (repeatingPeriod.equals(context.getString(R.string.weekly))) {
            interval = context.getString(R.string.weekly);
        } else if (repeatingPeriod.equals(context.getString(R.string.monthly))) {
            interval = context.getString(R.string.monthly);
        } else if (repeatingPeriod.equals(context.getString(R.string.yearly))) {
            interval = context.getString(R.string.yearly);
        }
        return interval;
    }


}