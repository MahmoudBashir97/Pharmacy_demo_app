package com.mahmoudbashir.pharmacy_app.FCM;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive: " + intent.getAction());

                      //  String senderId = intent.getExtras().getString("senderID");



        /*
                         if (intent.getAction().equals("message")) {
                            context.startActivity(new Intent(context, Chat_Activity.class)
                                    .putExtra("senderId", senderId)
                                    .putExtra("senderName", senderName)
                                    .putExtra("message", message)
                                    .putExtra("visit_user_id",receiverId)
                                    //.putExtra("image",image)
                                    .putExtra("countbadge",countbadge)
                                    .addFlags(FLAG_ACTIVITY_NEW_TASK));

                         }*/
    }
}
