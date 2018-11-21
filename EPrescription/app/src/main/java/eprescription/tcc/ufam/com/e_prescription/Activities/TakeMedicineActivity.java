package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;

import eprescription.tcc.ufam.com.e_prescription.R;
import eprescription.tcc.ufam.com.e_prescription.Util.NotificationUpdate;

public class TakeMedicineActivity extends AppCompatActivity {

    private static final String TAG = "TAKEMEDICINE";
    private Button setDate;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button okButton;
    private TextView medicine;
    private TextView frequency;
    private TextView duration;
    private TextView initialDay;
    private TextView initialHour;
    private Button setTime;
    private AlertDialog calendarDialog;
    private AlertDialog.Builder calendarDialogBuilder;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_medicine);

        initialDay = (TextView) findViewById(R.id.initialDayID);
        initialHour = (TextView) findViewById(R.id.timeTextID);
        setTime = (Button) findViewById(R.id.timePickerBtn);
        setDate = (Button) findViewById(R.id.pickDateBtnID);
        medicine = (TextView) findViewById(R.id.medID);
        frequency = (TextView) findViewById(R.id.freqTextID);
        duration = (TextView) findViewById(R.id.durationTextID);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HashMap<String, String> med = new HashMap<>();
            med.put("medicine", bundle.getString("medicine"));
            med.put("frequency", bundle.getString("frequency"));
            med.put("duration", bundle.getString("duration"));
            Log.d(TAG, "medicine: " + bundle.getString("medicine"));
            Log.d(TAG, "frequency: " + bundle.getString("frequency"));
            Log.d(TAG, "duration: " + bundle.getString("duration"));
        }

        medicine.setText(bundle.getString("medicine"));
        frequency.setText("A cada " + bundle.getString("frequency")+ " hora(s)");
        duration.setText("Durante " + bundle.getString("duration") + " dias");
        NotificationUpdate notificationUpdate = new NotificationUpdate();
        Intent intent = new Intent(this, NotificationUpdate.class);
        notificationUpdate.onReceive(this, intent);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendarPopUp();
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClockPopUp();
            }
        });

       /*

        Notice that in the manifest, the boot receiver is set to android:enabled="false".
        This means that the receiver will not be called unless the application explicitly
        enables it. This prevents the boot receiver from being called unnecessarily. You can
        enable a receiver (for example, if the user sets an alarm) as follows:
         */
//        ComponentName receiver = new ComponentName(this, NotificationUpdate.class);
//        PackageManager pm = this.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);

//        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, NotificationUpdate.class);
//        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 1000, alarmIntent);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY,20);
//        calendar.set(Calendar.MINUTE, 06);
//        Log.d(TAG, "ENTROU AQUI");
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  60 * 1000 * 1, alarmIntent);
    }

    private void createCalendarPopUp() {

        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                initialDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
        simpleNotification();

    }

    private void createClockPopUp() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                initialHour.setText(hourOfDay + ":" + minute);
            }
        }, hour, mMinute, true);
        timePickerDialog.show();
    }

    private void simpleNotification() {

        String CHANNEL_ID = "my_channel";
        createNotificationChannel(CHANNEL_ID);
        int notificationID = 1;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Test")
                .setContentText("Test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationID, mBuilder.build());
        Log.d(TAG, "user signed in");
    }

    private void createNotificationChannel(String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
