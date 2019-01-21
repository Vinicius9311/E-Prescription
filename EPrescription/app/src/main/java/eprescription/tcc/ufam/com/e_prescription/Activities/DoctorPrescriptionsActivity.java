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
import java.util.Date;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Adapter.DoctorPrescriptionsAdapter;
import eprescription.tcc.ufam.com.e_prescription.Adapter.PatientPrescriptionAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.R;

public class DoctorPrescriptionsActivity extends AppCompatActivity {

    /*
        Activity to show patient Prescriptions List from Doctor View
     */

    private static final String TAG = "DocPrescsActivity";
    private TextView myPrescription;
    private RecyclerView prescRecycler;
    private DoctorPrescriptionsAdapter prescriptionAdapter;
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
        setContentView(R.layout.activity_doctor_prescriptions);

        patientPrescriptions = new ArrayList<>();
        myPrescription = (TextView) findViewById(R.id.myPrescID);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(DoctorPrescriptionsActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "patient signed in");
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(DoctorPrescriptionsActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DoctorPrescriptionsActivity.this, MainActivity.class));
                }
            }
        };

//        mRootRef.child("users")
//                .child("doctors")
//                .equalTo(userID)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                            Log.d(TAG, "ENTROU " + snap);
//
//                            PatientPrescription patientPrescription = snap.getValue(PatientPrescription.class);
//                            Log.d(TAG, "Doctor Name: " + patientPrescription.getDoctorName());
//                            String doctor = "Dr(a). " + patientPrescription.getDoctorName();
//                            docName.setText(doctor);
//                            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
//                            String FormatDate = "Data prescrita: " + dateFormat.format(new Date(Long.parseLong(patientPrescription.getDatePrescripted())).getTime());
//                            datePresc.setText(FormatDate);
//                            String description = "Descrição: " + patientPrescription.getDescription();
//                            descPresc.setText(description);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "Patient Key: " + bundle.getString("patKey"));
            mPatPresc.orderByKey().equalTo(bundle.getString("patKey")).addValueEventListener(new ValueEventListener() {
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
        prescRecycler = (RecyclerView) findViewById(R.id.patientPrescriptionsRecyclerID);


        Log.d(TAG, "ITEM COUNT HERE: " + patientPrescriptions.size());
        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            for (DataSnapshot childSnap : snap.getChildren()) {
                PatientPrescription patientPrescription = childSnap.getValue(PatientPrescription.class);
                Log.d(TAG, "DataSnapshot: " + childSnap);
                Log.d(TAG, "PrescriptionID: " + patientPrescription.getPrescriptionID());
                patientPrescriptions.add(patientPrescription);
            }

            prescRecycler.setHasFixedSize(true);
            prescRecycler.setLayoutManager(new LinearLayoutManager(this));
            prescriptionAdapter = new DoctorPrescriptionsAdapter(this, patientPrescriptions);
            prescRecycler.setAdapter(prescriptionAdapter);
            prescriptionAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "ITEM COUNT: " + patientPrescriptions.size());
        progressBar.setVisibility(View.GONE);



    }
}