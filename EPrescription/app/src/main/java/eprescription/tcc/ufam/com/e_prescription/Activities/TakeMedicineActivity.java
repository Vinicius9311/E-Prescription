package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import eprescription.tcc.ufam.com.e_prescription.R;

public class TakeMedicineActivity extends AppCompatActivity {

    private static final String TAG = "TAKEMEDICINE";
    private Button setDate;
    private DatePicker datePicker;
    private Button okButton;
    private TextView initialDay;
    private TextView initialHour;
    private Button timePicker;
    private AlertDialog calendarDialog;
    private AlertDialog.Builder calendarDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_medicine);

        initialDay = (TextView) findViewById(R.id.initialDayID);
        initialHour = (TextView) findViewById(R.id.timeTextID);
        timePicker = (Button) findViewById(R.id.timePickerBtn);
        setDate = (Button) findViewById(R.id.pickDateBtnID);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendarPopUp();
            }
        });
    }

    private void createCalendarPopUp() {
        calendarDialogBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.calendar_popup, null);
        // TODO FINISH CALENDAR POPUP
        datePicker = (DatePicker) view.findViewById(R.id.datePickerID);
        calendarDialogBuilder.setView(view);
        calendarDialog = calendarDialogBuilder.create();
        calendarDialog.show();

        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        initialDay.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                });

        simpleNotification(view);

    }

    private void simpleNotification(View view) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Test")
                .setAutoCancel(true)
                .setContentText("Test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Log.d(TAG, "user signed in");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, mBuilder.build());
    }

    // TODO TIMEPICKER
}
