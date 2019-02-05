package eprescription.tcc.ufam.com.e_prescription.Activities;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import eprescription.tcc.ufam.com.e_prescription.R;
import eprescription.tcc.ufam.com.e_prescription.Util.NotificationUpdate;

public class TakeMedicineActivity extends AppCompatActivity {

    private static final String TAG = "TAKEMEDICINE";
    private DatePicker datePicker;
    private TextView timePicker;
    private Button okButton;
    private TextView medicine;
    private TextView frequency;
    private TextView duration;
    private TextView observation;
    private TextView initialDay;
    private TextView initialHour;
    private Button setAlarmButton;
    private AlertDialog calendarDialog;
    private AlertDialog.Builder calendarDialogBuilder;

    private String med;
    private String freq;
    private String dur;
    private String obs;
    private int mHour, mMinute;

    private FirebaseDatabase database;
    private DatabaseReference patMedRef;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_medicine_1);

        medicine = (TextView) findViewById(R.id.medName);
        frequency = (TextView) findViewById(R.id.freqQtyID);
        duration = (TextView) findViewById(R.id.durNameID);
        observation = (TextView) findViewById(R.id.observationId);
        setAlarmButton = (Button) findViewById(R.id.finishDateID);
        timePicker = (TextView) findViewById(R.id.timeView);

        database = FirebaseDatabase.getInstance();
        patMedRef = database.getReference().child("patientPrescriptionMedicine");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        Log.d(TAG, "userID: " + userID.toString());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(TakeMedicineActivity.this,"Email: " + user.getEmail() + "\nUserID: " + userID, Toast.LENGTH_LONG).show();
                } else {
                    // user is signed out
                    Toast.makeText(TakeMedicineActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(TakeMedicineActivity.this, MainActivity.class));
                }
            }
        };

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HashMap<String, String> presc = new HashMap<>();
            presc.put("medicine", bundle.getString("medicine"));
            presc.put("frequency", bundle.getString("frequency"));
            presc.put("duration", bundle.getString("duration"));
            med = bundle.getString("medicine");
            freq = bundle.getString("frequency");
            dur = bundle.getString("duration");
            obs = bundle.getString("observation");
            Log.d(TAG, "medicine: " + med);
            Log.d(TAG, "frequency: " + freq);
            Log.d(TAG, "duration: " + dur);
            Log.d(TAG, "observation: " + obs);
        }

        medicine.setText(bundle.getString("medicine"));
        frequency.setText("A cada " + bundle.getString("frequency")+ " hora(s)");
        duration.setText("Durante " + bundle.getString("duration") + " dias");
        observation.setText(obs);

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TakeMedicineActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker1, int selectedHour, int selectedMinute) {
                        mHour = selectedHour;
                        mMinute = selectedMinute;
                        if (mMinute >= 0 && mMinute <= 9) {
                            timePicker.setText( selectedHour + ":0" + selectedMinute);
                        } else {
                            timePicker.setText( selectedHour + ":" + selectedMinute);
                        }
                        Log.d(TAG, "Hour: " + mHour);
                        Log.d(TAG, "Minute: " + mMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

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
//
//        BroadcastReceiver nu = new NotificationUpdate();
//        IntentFilter filter = new IntentFilter();
//        registerReceiver(nu, filter);
    }


    private void sendMyBroadcast() {

        // TODO Write requisites to set a time alarm

        Calendar calendar = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= 23) {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    mHour,
                    mMinute,
                    0
            );
        } else {
            calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    mHour,
                    mMinute,
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
        intent.putExtra("observation", obs);
        intent.putExtra("userID", userID);
        Log.d(TAG, "medicine: " + med);
        Log.d(TAG, "frequency: " + freq);
        Log.d(TAG, "duration: " + dur);
        Log.d(TAG, "repetition: " + repetition);
        Log.d(TAG, "observation: " + obs);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TakeMedicineActivity.this, 0, intent, 0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        getBaseContext().sendBroadcast(intent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * Long.parseLong(freq)*60, pendingIntent);

        if (Build.VERSION.SDK_INT >= 23) {
            if (mMinute >= 0 && mMinute <= 9) {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + mHour + ":0" + mMinute, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + mHour + ":" + mMinute, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mMinute >= 0 && mMinute <= 9) {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + mHour + ":0" + mMinute, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TakeMedicineActivity.this,
                        "Alarm set to " + mHour + ":" + mMinute, Toast.LENGTH_SHORT).show();
            }
        }

        patMedRef.child(userID).child(med).setValue(calendar.getTimeInMillis());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
