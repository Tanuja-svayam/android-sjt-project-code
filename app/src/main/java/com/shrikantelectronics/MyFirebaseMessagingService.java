package com.shrikantelectronics;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    private static final String CHANNEL_ID = "order_notifications";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        // TODO: Send token to your backend server
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Extract all data from payload
        Map<String, String> data = remoteMessage.getData();

        String title = remoteMessage.getNotification() != null
                ? remoteMessage.getNotification().getTitle()
                : data.get("title");

        String body = remoteMessage.getNotification() != null
                ? remoteMessage.getNotification().getBody()
                : data.get("body");

        String sound = data.getOrDefault("sound", "default");
        String transactiontype = data.get("transactiontype");
        String sourcetablepk = data.get("sourcetablepk");

        sendNotification(title, body, sound, transactiontype, data, sourcetablepk);
    }

    private void sendNotification(String title, String body, String sound,
                                  String transactiontype, Map<String,String> data, String sourcetablepk) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Sound URI
        Uri soundUri = "default".equals(sound)
                ? RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                : Uri.parse("android.resource://" + getPackageName() + "/raw/" + sound);

        // Decide activity based on transactiontype
        Intent intent;
        switch (transactiontype) {
            case "ORDER APPROVAL":

               String custname = data.get("custname");
                String vinvoicedt = data.get("vinvoicedt");
                String netinvoiceamt = data.get("netinvoiceamt");
                String invoiceno = data.get("invoiceno");
                String remarks = data.get("remarks");

                intent = new Intent(this, OrderApprovalPending_view_single.class);
                intent.putExtra("sysinvorderno",sourcetablepk);
                intent.putExtra("custname",custname);
                intent.putExtra("vinvoicedt",vinvoicedt);
                intent.putExtra("netinvoiceamt",netinvoiceamt);
                intent.putExtra("invoiceno",invoiceno);
                intent.putExtra("remarks",remarks);
                intent.putExtra("dbd_paid_by_customer","");
                intent.putExtra("dbd_amount","0");
                intent.putExtra("approved_by_manager","");
                intent.putExtra("gross_slc_amount","0");
                intent.putExtra("approvalreason","");
                intent.putExtra("gross_slc_deficit","0");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                break;
            case "COD APPROVAL":
                intent = new Intent(this, OrderApprovalPending_view_cod.class);
                break;
            case "SLC APPROVAL":
                intent = new Intent(this, OrderApprovalPending_view_single.class);
                break;
            case "SRN APPROVAL":
                intent = new Intent(this, OrderApprovalPending_view_srn.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }

        // Pass all extra data
        for (String key : data.keySet()) {
            intent.putExtra(key, data.get(key));
        }



        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Notification channel for Android 8+
        String channelId = "order_notifications_" + sound;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Order Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for order notifications");
            channel.setSound(soundUri, null);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .setBigContentTitle(title)
                .bigText(body);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(bigTextStyle)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }
}
