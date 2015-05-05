package com.bahri.spray.Model;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.bahri.spray.Controller.MainTabActivity;
import com.bahri.spray.R;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by mac on 4/29/15.
 */
public class SprayBroadcastReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        Intent intent1 = new Intent(context, MainTabActivity.class);
        intent1.putExtra("tab",1);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo_final)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        context.startActivity(intent1);
    }
}
