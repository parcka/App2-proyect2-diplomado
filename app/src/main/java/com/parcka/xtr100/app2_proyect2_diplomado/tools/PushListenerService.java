package com.parcka.xtr100.app2_proyect2_diplomado.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.parcka.xtr100.app2_proyect2_diplomado.ImageActivity;
import com.parcka.xtr100.app2_proyect2_diplomado.MusicActivity;
import com.parcka.xtr100.app2_proyect2_diplomado.R;

/**
 * Created by XTR100 on 15/08/2016.
 */
public class PushListenerService extends GcmListenerService {
    String TAG = getClass().getName();
    int countNotification = 0;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.d(TAG, "GCM Data received is " + data);
        //Get the message.
        String msg = data.getString("message");
        int action = Integer.parseInt(data.getString("action"));
        Log.d(TAG, "GCM Message received is " + msg);
        // Notifying to user.
        notifyUser(getApplicationContext(), msg, action);
    }

    public void notifyUser(Context context, String data, int action) {
        Intent intent;
        if (action == 1) {
            intent = launchActivity(context, data, MusicActivity.class);

        } else {
            intent = launchActivity(context, data, ImageActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.iconandroid);
        builder.setAutoCancel(true);
        builder.setContentTitle("Abre el Activity");
        builder.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.setContentIntent(pendingIntent);
        builder.setContentText(data);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
//        builder.setVibrate(new long[]{1000, 1000});
        notificationManager.notify(countNotification++, builder.build());
        Log.v(TAG, "count " + countNotification);
    }

    private Intent launchActivity(Context context, String data, Class activity) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("data", data);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
}