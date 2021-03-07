package com.mahmoudbashir.pharmacy_app.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mahmoudbashir.pharmacy_app.MainActivity;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseMessaging";
    public static final String MESSAGE = "request";
    public ImageView imageView;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(LOG_TAG, remoteMessage.getData().toString() + "");

        Log.d(LOG_TAG, "onMessageReceived: ");
        String ID = SharedPrefranceManager.getInastance(getApplicationContext()).getUser_Phone();
        if (remoteMessage.getData().get("ph_phone").equals("+201558440959")){
            handleInviteIntent();
        }
    }

    private void handleInviteIntent() {
        Intent reciveMessage = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("message")
                .putExtra("Data", "data");

        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 2, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(reciveMessage);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        2,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification build = new NotificationCompat.Builder(this, MESSAGE)
                //.setSmallIcon(R.drawable.chat_icon2)
                .setPriority(PRIORITY_MAX)
                .setContentTitle(String.format("You have new drug request"))
                .addAction(R.drawable.ic_baseline_check_24, "Open", pendingIntentAccept)
                .setVibrate(new long[3000])
                .setChannelId(MESSAGE)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(MESSAGE, MESSAGE, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(1, build);

    }
}
