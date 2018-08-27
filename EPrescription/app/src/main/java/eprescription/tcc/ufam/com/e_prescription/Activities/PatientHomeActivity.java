package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientHomeActivity extends AppCompatActivity {

    private static final String TAG = "PatientHomeActivity";
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference patientDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ChildEventListener childEventListener;

    private TextView patientInfo;
    private ListView patientListView;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        patientInfo = (TextView) findViewById(R.id.PatientInfo);
        patientListView = (ListView) findViewById(R.id.listView);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        patientDatabaseReference = database.getReference().child("users").child("patient");
        // todo ver aqui os childs
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        Log.d(TAG, "userID: " + userID.toString());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(PatientHomeActivity.this,"Email: " + user.getEmail() + "UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "patient signed in");
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(PatientHomeActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PatientHomeActivity.this, MainActivity.class));
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientHomeActivity.this, MakeAppointmentActivity.class));
//                startActivity(new Intent(PatientHomeActivity.this, AddMedicineActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        patientDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showData(DataSnapshot dataSnapshot) {
        Log.d("HERE", "VALUE IS: " + dataSnapshot.getValue()) ;
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        Log.d("HERE", "USERID: " + userID);

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Patient patient = new Patient();
            patient.setFirstName(dataSnapshot.child(userID).getValue(Patient.class).getFirstName());
            patient.setLastName(dataSnapshot.child(userID).getValue(Patient.class).getLastName());
            patient.setEmail(dataSnapshot.child(userID).getValue(Patient.class).getEmail());
            patient.setDateOfBirth(dataSnapshot.child(userID).getValue(Patient.class).getDateOfBirth());
            patient.setSex(dataSnapshot.child(userID).getValue(Patient.class).getSex());
            patient.setDateModified(dataSnapshot.child(userID).getValue(Patient.class).getDateModified());
            patient.setDateCreated(dataSnapshot.child(userID).getValue(Patient.class).getDateCreated());
            patient.setPassword(dataSnapshot.child(userID).getValue(Patient.class).getPassword());

            Log.d(TAG, "First Name: " + patient.getFirstName());
            patientInfo.setText(patient.getFirstName().toString());
            Log.d(TAG, "Last Name: " + patient.getLastName());
            Log.d(TAG, "Email: " + patient.getEmail());
            Log.d(TAG, "Date of Birth: " + patient.getDateOfBirth());
            Log.d(TAG, "Marital Status: " + patient.getMaritalStatus());
            Log.d(TAG, "Sex: " + patient.getSex());
            Log.d(TAG, "Date Modified: " + patient.getDateModified());
            Log.d(TAG, "Date Created: " + patient.getDateCreated());
            Log.d(TAG, "Password: " + patient.getPassword());


            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(patient.getFirstName());
            arrayList.add(patient.getLastName());
            arrayList.add(patient.getEmail());
            arrayList.add(patient.getDateOfBirth());
            arrayList.add(patient.getSex());
            arrayList.add(patient.getDateModified());
            arrayList.add(patient.getDateCreated());
            arrayList.add(patient.getPassword());
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.content_patient_home, R.id.PatientInfo, arrayList);
            patientListView.setAdapter(arrayAdapter);
        }
    }

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
//
//        if (item.getItemId() == R.id.action_add){
//            Toast.makeText(PatientHomeActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(PatientHomeActivity.this, AddMedicineActivity.class));
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
