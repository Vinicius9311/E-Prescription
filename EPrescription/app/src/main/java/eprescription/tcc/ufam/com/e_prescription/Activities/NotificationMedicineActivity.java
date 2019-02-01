package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.service.autofill.TextValueSanitizer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eprescription.tcc.ufam.com.e_prescription.R;

public class NotificationMedicineActivity extends AppCompatActivity {

    private static final String TAG = "NotificationMedicine";
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mDatabase;
    private DatabaseReference patMedRef;

    private String userID;
    private TextView medName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_medicine);

        medName = (TextView) findViewById(R.id.medNameID);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Log.d(TAG, "med:::" + bundle.getString("med/"));
            medName.setText(bundle.getString("med/"));
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        Log.d(TAG, "userID: " + userID.toString());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(NotificationMedicineActivity.this,"Email: " + user.getEmail() + "\nUserID: " + userID, Toast.LENGTH_LONG).show();
                 } else {
                    // user is signed out
                    Toast.makeText(NotificationMedicineActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(NotificationMedicineActivity.this, MainActivity.class));
                }
            }
        };

        // TODO See how to implement a way to user say that he has taken the medicine

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
