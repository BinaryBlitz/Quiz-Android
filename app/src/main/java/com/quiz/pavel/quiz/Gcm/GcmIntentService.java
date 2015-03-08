package com.quiz.pavel.quiz.Gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.controller.MainTabsActivity;

/**
 * Created by pavel on 08/03/15.
 */
public class GcmIntentService extends IntentService {
    public static final String TAG = "GcmIntentService";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;



    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "something happened");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
//                for (int i=0; i<5; i++) {
//                    Log.i(TAG, "Working... " + (i + 1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
//                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
//                // Post notification of received message.
//                sendNotification("Received: " + extras.toString());
//                Log.i(TAG, "Received: " + extras.toString());

issueNotification(intent, "");

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainTabsActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_stat_gcm)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void issueNotification(Intent intent, String msg) {

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        String str1 =  intent.getExtras().getString("title");
        String str2 = intent.getExtras().getString("message");

        // Sets up the Snooze and Dismiss action buttons that will appear in the
        // expanded view of the notification.
//        Intent dismissIntent = new Intent(this, PingService.class);
//        dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
//        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);
//
//        Intent snoozeIntent = new Intent(this, PingService.class);
//        snoozeIntent.setAction(CommonConstants.ACTION_SNOOZE);
//        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

        // Constructs the Builder object.
        builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setContentTitle(str1)
                        .setContentText(str2)
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                /*
                 * Sets the big view "big text" style and supplies the
                 * text (the user's reminder message) that will be displayed
                 * in the detail area of the expanded notification.
                 * These calls are ignored by the support library for
                 * pre-4.1 devices.
                 */
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));
//                .addAction (R.drawable.ic_stat_dismiss,
//                        getString(R.string.dismiss), piDismiss)
//                .addAction (R.drawable.ic_stat_snooze,
//                        getString(R.string.snooze), piSnooze);

        /*
         * Clicking the notification itself displays ResultActivity, which provides
         * UI for snoozing or dismissing the notification.
         * This is available through either the normal view or big view.
         */
        Intent resultIntent = new Intent(this, MainTabsActivity.class);
//        resultIntent.putExtra(CommonConstants.EXTRA_MESSAGE, msg);
//         resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
        issueNotification(builder);
    }



    private void issueNotification(NotificationCompat.Builder builder) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Including the notification ID allows you to update the notification later on.
        mNotificationManager.notify(001, builder.build());
    }
}