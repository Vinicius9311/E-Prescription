package eprescription.tcc.ufam.com.e_prescription.Activities;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
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
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import eprescription.tcc.ufam.com.e_prescription.R;
import eprescription.tcc.ufam.com.e_prescription.Util.NotificationUpdate;

public class TakeMedicineActivity extends AppCompatActivity {

    private static final String TAG = "TAKEMEDICINE";
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button okButton;
    private TextView medicine;
    private TextView frequency;
    private TextView duration;
    private TextView initialDay;
    private TextView initialHour;
    private Button setAlarmButton;
    private AlertDialog calendarDialog;
    private AlertDialog.Builder calendarDialogBuilder;

    private String med;
    private String freq;
    private String dur;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_medicine_1);

        medicine = (TextView) findViewById(R.id.medName);
        frequency = (TextView) findViewById(R.id.freqQtyID);
        duration = (TextView) findViewById(R.id.durNameID);
        setAlarmButton = (Button) findViewById(R.id.finishDateID);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HashMap<String, String> presc = new HashMap<>();
            presc.put("medicine", bundle.getString("medicine"));
            presc.put("frequency", bundle.getString("frequency"));
            presc.put("duration", bundle.getString("duration"));
            med = bundle.getString("medicine");
            freq = bundle.getString("frequency");
            dur = bundle.getString("duration");
            Log.d(TAG, "medicine: " + med);
            Log.d(TAG, "frequency: " + freq);
            Log.d(TAG, "duration: " + dur);
        }

        medicine.setText(bundle.getString("medicine"));
        frequency.setText("A cada " + bundle.getString("frequency")+ " hora(s)");
        duration.setText("Durante " + bundle.getString("duration") + " dias");

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMyBroadcast();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        BroadcastReceiver nu = new NotificationUpdate();
        IntentFilter filter = new IntentFilter();
        registerReceiver(nu, filter);
    }


    private void sendMyBroadcast(){
        Calendar calendar = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= 23) {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getHour(),
                    timePicker.getMinute(),
                    0
            );
        } else {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute(),
                    0
            );
        }

                /*

                    1 day = 86.400 s
                    1 hour = 3.600 s

                 */
        int repetition = (24 * Integer.parseInt(dur))/(Integer.parseInt(freq));

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(TakeMedicineActivity.this, NotificationUpdate.class);

        intent.putExtra("medicine", med);
        intent.putExtra("frequency", freq);
        intent.putExtra("duration", dur);
        intent.putExtra("repetition", repetition);
        Log.d(TAG, "medicine: " + med);
        Log.d(TAG, "frequency: " + freq);
        Log.d(TAG, "duration: " + dur);
        Log.d(TAG, "repetition: " + repetition);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TakeMedicineActivity.this, 0, intent, 0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        getBaseContext().sendBroadcast(intent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * Long.parseLong(freq)*60, pendingIntent);

        if (Build.VERSION.SDK_INT >= 23) {
            if (timePicker.getMinute() >= 0 && timePicker.getMinute()<=9) {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + timePicker.getHour() + ":0" + timePicker.getMinute(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + timePicker.getHour() + ":" + timePicker.getMinute(), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (timePicker.getCurrentMinute() >= 0 && timePicker.getCurrentMinute()<=9) {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + timePicker.getCurrentHour() + ":0" + timePicker.getCurrentMinute(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute(), Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void simpleNotification() {
//
//        String CHANNEL_ID = "my_channel";
//        createNotificationChannel(CHANNEL_ID);
//        int notificationID = 1;
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
//                .setContentTitle("Test")
//                .setContentText("Test")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(notificationID, mBuilder.build());
//        Log.d(TAG, "user signed in");
//    }
//
//    private void createNotificationChannel(String channel_id) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    //    private void createCalendarPopUp() {
//
//        Calendar calendar = Calendar.getInstance();
//        int mYear = calendar.get(Calendar.YEAR);
//        int mMonth = calendar.get(Calendar.MONTH);
//        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                initialDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
//                fYear = year;
//                fMonth = month;
//                fDay = dayOfMonth;
//            }
//        }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//        simpleNotification();
//
//    }
//
//    private void createClockPopUp() {
//
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int mMinute = calendar.get(Calendar.MINUTE);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                initialHour.setText(hourOfDay + ":" + minute);
//                fHour = hourOfDay;
//                fMinute = minute;
//            }
//        }, hour, mMinute, true);
//        timePickerDialog.show();
//    }
}
