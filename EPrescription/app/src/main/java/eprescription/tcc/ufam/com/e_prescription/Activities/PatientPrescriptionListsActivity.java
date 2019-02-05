package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Adapter.PatientPrescriptionAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientPrescriptionListsActivity extends AppCompatActivity {

    /*
        Activity to show patient prescriptions on Patient View
     */
    private static final String TAG = "PatPrescriptionList";
    private TextView myPrescription;
    private RecyclerView prescRecycler;
    private PatientPrescriptionAdapter prescriptionAdapter;
    private List<PatientPrescription> patientPrescriptions;
    private ProgressBar progressBar;
    private String userID;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPatRef = mRootRef.child("users").child("patient");
    private DatabaseReference mPatPresc = mRootRef.child("patientPrescriptions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription_list);

        patientPrescriptions = new ArrayList<>();
        myPrescription = (TextView) findViewById(R.id.myPrescID);
        progressBar = (ProgressBar) findViewById(R.id.prescsProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(PatientPrescriptionListsActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "patient signed in");
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(PatientPrescriptionListsActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PatientPrescriptionListsActivity.this, MainActivity.class));
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        mPatPresc.orderByKey().equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "FOUND CHILD" + dataSnapshot);
                getPrescriptions(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void getPrescriptions(DataSnapshot dataSnapshot) {
        patientPrescriptions = new ArrayList<>();
        prescRecycler = (RecyclerView) findViewById(R.id.prescriptionsRecyclerID);


        Log.d(TAG, "ITEM COUNT HERE: " + patientPrescriptions.size());
        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            for (DataSnapshot childSnap : snap.getChildren()) {
                PatientPrescription patientPrescription = childSnap.getValue(PatientPrescription.class);
                Log.d(TAG, "Doctor Name: " + patientPrescription.getDoctorName());
                Log.d(TAG, "DataSnapshot: " + childSnap);
                Log.d(TAG, "PrescriptionID: " + patientPrescription.getPrescriptionID());
                patientPrescriptions.add(patientPrescription);
                Collections.reverse(patientPrescriptions);
            }

            prescRecycler.setHasFixedSize(true);
            prescRecycler.setLayoutManager(new LinearLayoutManager(this));
            prescriptionAdapter = new PatientPrescriptionAdapter(this, patientPrescriptions);
            prescRecycler.setAdapter(prescriptionAdapter);
            prescriptionAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "ITEM COUNT: " + patientPrescriptions.size());
        progressBar.setVisibility(View.GONE);



    }
}
