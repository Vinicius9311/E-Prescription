package eprescription.tcc.ufam.com.e_prescription.Util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.HashMap;

import eprescription.tcc.ufam.com.e_prescription.R;

public class NotificationUpdate extends BroadcastReceiver {

    private static final String TAG = "NotificationUpdate";
    private String med;
    private String freq;
    private String dur;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
//        updateNotification(context);
//        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "Receiver: " + intent);
        if (bundle != null) {
            HashMap<String, String> presc = new HashMap<>();
            presc.put("medicine", bundle.getString("medicine"));
            presc.put("frequency", bundle.getString("frequency"));
            presc.put("duration", bundle.getString("duration"));
            med = bundle.getString("medicine");
            freq = bundle.getString("frequency");
            dur = bundle.getString("duration");
            Log.d(TAG, "medicine: " + bundle.getString("medicine"));
            Log.d(TAG, "frequency: " + bundle.getString("frequency"));
            Log.d(TAG, "duration: " + bundle.getString("duration"));
        }

        simpleNotification(context);

    }

    private void simpleNotification(Context context) {

        String CHANNEL_ID = "my_channel";
        createNotificationChannel(CHANNEL_ID, context);
        int notificationID = 1;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Test")
                .setContentText("Test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationID, mBuilder.build());
        Log.d(TAG, "user signed in");
    }

    private void createNotificationChannel(String channel_id, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
