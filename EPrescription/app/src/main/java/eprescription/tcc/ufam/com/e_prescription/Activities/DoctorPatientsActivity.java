package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.Model.PrescriptionItem;
import eprescription.tcc.ufam.com.e_prescription.R;


    /*
        Activity to show search patients
        by name
     */
public class DoctorPatientsActivity extends AppCompatActivity {

    private static final String TAG = "PATIENTSACTIVITY";

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mDocRef = mRootRef.child("users").child("doctor");
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private AutoCompleteTextView patientName;
    private Button advance;

    private List<String> names;
    private List<Patient> patientList;
    private List<PrescriptionItem> itemList;
    private String doctorName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        patientName = (AutoCompleteTextView) findViewById(R.id.patientNameID);
        advance = (Button) findViewById(R.id.nextID);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String userID = mUser.getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(DoctorPatientsActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(DoctorPatientsActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DoctorPatientsActivity.this, MainActivity.class));
                }
            }
        };

        advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dPatientName = patientName.getText().toString();
                if (!dPatientName.matches("")) {
                    // TODO Check if patient is registered on app before going to next activity
                    if (names.contains(dPatientName)) {
                        Intent intent = new Intent(DoctorPatientsActivity.this, DocPatientActionActivity.class);
                        intent.putExtra("patient", patientName.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(DoctorPatientsActivity.this,"Paciente não registrado", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DoctorPatientsActivity.this,"Insira o nome do paciente", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        DatabaseReference patientRef = mRootRef.child("users").child("patient");

        patientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getPatientList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDocRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getPatientInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPatientInfo(DataSnapshot dataSnapshot) {
        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Doctor doctor = snap.getValue(Doctor.class);
            Log.d(TAG, "Doctor Name: " + doctor.getFirstName());
            doctorName = doctor.getFirstName();
        }
    }


    private void getPatientList(DataSnapshot dataSnapshot) {

        names = new ArrayList<String>();
        patientList = new ArrayList<>();

        for (DataSnapshot snap: dataSnapshot.getChildren()) {
            Patient patient = snap.getValue(Patient.class);
            Log.d(TAG, "PATIENT NAME: " + patient.getFullName());
            names.add(patient.getFullName());
            Log.d(TAG, "NAME ADDED");
            patientList.add(patient);
            Log.d(TAG, "PATIENT ADDED");

            for (Patient pat : patientList) {
                Log.d(TAG, "Patient " + snap.getKey());
            }

        }
        ArrayAdapter<String> patientNameAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, names);
        patientName.setAdapter(patientNameAdapter);
    }
}
