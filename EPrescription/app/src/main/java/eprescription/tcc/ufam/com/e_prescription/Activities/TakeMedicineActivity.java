package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
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

import java.sql.Time;
import java.util.Calendar;

import eprescription.tcc.ufam.com.e_prescription.R;

public class TakeMedicineActivity extends AppCompatActivity {

    private static final String TAG = "TAKEMEDICINE";
    private Button setDate;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button okButton;
    private TextView initialDay;
    private TextView initialHour;
    private Button setTime;
    private AlertDialog calendarDialog;
    private AlertDialog.Builder calendarDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_medicine);

        initialDay = (TextView) findViewById(R.id.initialDayID);
        initialHour = (TextView) findViewById(R.id.timeTextID);
        setTime = (Button) findViewById(R.id.timePickerBtn);
        setDate = (Button) findViewById(R.id.pickDateBtnID);

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
    }

    private void createCalendarPopUp() {
//        calendarDialogBuilder = new AlertDialog.Builder(this);
//        final View view = getLayoutInflater().inflate(R.layout.calendar_popup, null);
//        // TODO FINISH CALENDAR POPUP
//        datePicker = (DatePicker) view.findViewById(R.id.datePickerID);
//        calendarDialogBuilder.setView(view);
//        calendarDialog = calendarDialogBuilder.create();
//        calendarDialog.show();

        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                initialDay.setText(dayOfMonth + "/" + month + "/" + year);
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
    // TODO TIMEPICKER
}
