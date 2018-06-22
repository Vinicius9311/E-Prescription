package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientHomeActivity extends AppCompatActivity {

    private static final String TAG = "PatientHomeActivity";
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference patientDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView text;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientHomeActivity.this, MakeAppointmentActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        text = (TextView) findViewById(R.id.textViewID);
        Log.d(TAG, "patient signed in");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        patientDatabaseReference = database.getReference().child("users").child("patient");
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        Log.d(TAG, "userID: " + userID.toString());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "user: " + user.toString());

                if (user != null) {
                    // todo getUserID
                    // user is signed in
                    //mUser = mAuth.getCurrentUser();
//                    userID = mUser.getUid();
                    //Toast.makeText(PatientHomeActivity.this,"UserID" + userID.toString(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "patient signed in");
                    Log.d(TAG, "username: " + user.toString());
                    Log.d(TAG, "userID: " + user.getUid());

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(PatientHomeActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();

                }
            }
        };

//        patientDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                showData(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

//    private void showData(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            Patient patient = new Patient();
//            patient.setFirstName(ds.child(userID).getValue(Patient.class).getFirstName());
////            text = (TextView) findViewById(R.id.textViewID);
////            text.setText(patient.getFirstName().toString());
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_signout:
//                mAuth.signOut();
//                Toast.makeText(PatientHomeActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(PatientHomeActivity.this, MainActivity.class));
//                finish();
//                break;
//            case  R.id.action_make_appointment:
//
//                break;
//        }

        if (item.getItemId() == R.id.action_signout){
            mAuth.signOut();
            Toast.makeText(PatientHomeActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PatientHomeActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
