package eprescription.tcc.ufam.com.e_prescription.Util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import eprescription.tcc.ufam.com.e_prescription.Activities.NotificationMedicineActivity;
import eprescription.tcc.ufam.com.e_prescription.R;

public class NotificationUpdate extends BroadcastReceiver {

    private static final String TAG = "NotificationUpdate";
    private String med;
    private String freq;
    private String dur;
    private String obs;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
//        updateNotification(context);
//        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            HashMap<String, String> presc = new HashMap<>();
            presc.put("medicine", bundle.getString("medicine"));
            presc.put("frequency", bundle.getString("frequency"));
            presc.put("duration", bundle.getString("duration"));
            med = bundle.getString("medicine");
            freq = bundle.getString("frequency");
            dur = bundle.getString("duration");
            obs = bundle.getString("observation");
            Log.d(TAG, "medicine: " + bundle.getString("medicine"));
            Log.d(TAG, "frequency: " + bundle.getString("frequency"));
            Log.d(TAG, "duration: " + bundle.getString("duration"));
            Log.d(TAG, "observation: " + bundle.getString("observation"));
        }

        simpleNotification(context);

    }

    private void simpleNotification(Context context) {

        String CHANNEL_ID = "my_channel";
        createNotificationChannel(CHANNEL_ID, context);
        int notificationID = 1;

        String medication[] = med.split(" ", 2);

        // Todo Pending intent to add a tap action
        // TODO https://developer.android.com/training/notify-user/build-notification#java
        Intent intent = new Intent(context, NotificationMedicineActivity.class);
        Log.d(TAG, "medmed: " + med);
        intent.putExtra("med", med);
        intent.putExtra("table", 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(context.getString(R.string.medicine_time_title))
                .setContentText(med + "\n\n" + obs)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(med + "\n\n" + obs))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationID, mBuilder.build());
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
