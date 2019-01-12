package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Adapter.DoctorMedicineAdapter;
import eprescription.tcc.ufam.com.e_prescription.Adapter.PatientMedicineAdapter;
import eprescription.tcc.ufam.com.e_prescription.FirebaseEntities.DoctorPatient;
import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.Model.DoctorPrescription;
import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.Model.Prescription;
import eprescription.tcc.ufam.com.e_prescription.Model.PrescriptionItem;
import eprescription.tcc.ufam.com.e_prescription.R;


/*
    Activity that shows when doctor prescripts to patient
 */

public class PrescriptionActivity extends AppCompatActivity {

    private static final String TAG = "PrescriptionActivity";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mDocRef = mRootRef.child("users").child("doctor");
    private DatabaseReference mPatRef = mRootRef.child("users").child("patient");
    private DatabaseReference mPresc = mRootRef.child("prescription");
    private DatabaseReference mDocPresc = mRootRef.child("doctorPrescriptions");
    private DatabaseReference mPatPresc = mRootRef.child("patientPrescriptions");
    private DatabaseReference mDocPat = mRootRef.child("doctorPatients");
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private TextView patientName;
    private TextView noItem;
    private EditText description;
    private RecyclerView medicineRecycler;
    private DoctorMedicineAdapter doctorMedicineAdapter;
    private Button finishBtn;
    private List<Patient> patientList;
    private List<PrescriptionItem> itemList;
    private String doctorName;
    private String patKey;
    private String patName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<PrescriptionItem>();
        patientName = (TextView) findViewById(R.id.patientID);
        noItem = (TextView) findViewById(R.id.noItemID);
        medicineRecycler = (RecyclerView) findViewById(R.id.medicineRecyclerID);
        finishBtn = (Button) findViewById(R.id.finishPrescriptionID);
        description = (EditText) findViewById(R.id.descriptionID);
        progressBar = (ProgressBar) findViewById(R.id.prescProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String userID = mUser.getUid();
        DatabaseReference patientRef = mRootRef.child("users").child("patient");

        Log.d(TAG, "Activity Created");


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(PrescriptionActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "doctor signed in");
//                    Log.d(TAG, "username: " + user.getEmail());
//                    Log.d(TAG, "userID: " + userID);

                } else {
//                    user is signed out
//                    Log.d(TAG, "user signed out");
                    Toast.makeText(PrescriptionActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PrescriptionActivity.this, MainActivity.class));
                }
            }
        };

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            HashMap<String, String> pat = new HashMap<>();
            pat.put("patient", bundle.getString("patient"));
            patKey = bundle.getString("patientKey");
            patName = bundle.getString("patient");

//            savedInstanceState.putString("PatientKey", bundle.getString("patientKey"));

            Log.d(TAG, "Patient " + bundle.getString("patient"));
            Log.d(TAG, "Patient Key " + patKey);
            patientName.setText(bundle.getString("patient"));

            patientRef.orderByChild("fullName")
                    .equalTo(bundle.getString("patient"))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getPatInfo(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

//        mDocRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                getPatInfo(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrescriptionActivity.this, MedicineActivity.class);
                intent.putExtra("patientKey", patKey);
                intent.putExtra("patient", patName);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        medicineRecycler.setHasFixedSize(true);
        medicineRecycler.setLayoutManager(new LinearLayoutManager(this));

        if (itemList.size() > 0) {

            progressBar.setVisibility(View.GONE);
            noItem.setVisibility(View.INVISIBLE);
            doctorMedicineAdapter = new DoctorMedicineAdapter(this, itemList);
            addToRecyclerView();
            medicineRecycler.setAdapter(doctorMedicineAdapter);
            doctorMedicineAdapter.notifyDataSetChanged();

            finishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!patientName.equals("") || itemList.size() != 0) {
                        // TODO save realtime database
                        //mPresc.child(userID).child(patientName.getText().toString()).setValue(true);
                        mRootRef.child("users").child("patient").orderByChild("fullName")
                                .equalTo(patientName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    String patientKey = snap.getKey();
//                                Log.d(TAG, "Patient Key: " + patientKey);
//                                mPresc.push().setValue(patientKey);  funciona
                                    String prescID = mPresc.push().getKey();
//                                Log.d(TAG, "Prescription Key: " + prescID);

                                    Prescription prescription = new Prescription(
                                            String.valueOf(java.lang.System.currentTimeMillis()),
                                            description.getText().toString(),
                                            itemList);
                                    mPresc.child(prescID).setValue(prescription);

                                    DoctorPrescription doctorPrescription = new DoctorPrescription(
                                            patientName.getText().toString(),
                                            patientKey,
                                            prescID);
                                    mDocPresc.child(userID).push().setValue(doctorPrescription);

                                    PatientPrescription patientPrescription = new PatientPrescription(
                                            doctorName,
                                            userID,
                                            prescID,
                                            description.getText().toString(),
                                            String.valueOf(java.lang.System.currentTimeMillis()),
                                            patientKey
                                    );
                                    mPatPresc.child(patientKey).push().setValue(patientPrescription);

                                    // TODO Treat when there is a already a patient doctor
                                    DoctorPatient doctorPatient = new DoctorPatient(
                                            patientName.getText().toString(),
                                            patientKey);
                                    mDocPat.child(userID).child("patients").push().setValue(doctorPatient);

                                    Toast.makeText(PrescriptionActivity.this, "Receita prescrita com sucesso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PrescriptionActivity.this, DoctorHomeActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //DoctorPatient doctorPatient = new DoctorPatient(userID, getPatientKey());

                    }
                }
            });

        } else {
            noItem.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void addToRecyclerView() {
        Bundle bundle = getIntent().getExtras();
        Log.d(TAG, "BUNDLE " + bundle);

        if (bundle != null) {
            HashMap<String, String> med = new HashMap<>();
            med.put("medicine", bundle.getString("medicine"));
            med.put("duration", bundle.getString("duration"));
            med.put("frequency", bundle.getString("frequency"));
            med.put("via", bundle.getString("via"));
            med.put("obs", bundle.getString("obs"));
            Log.d(TAG, "MEDICINE " + bundle.getString("medicine"));
            PrescriptionItem newItem = new PrescriptionItem(
                    bundle.getString("medicine"),
                    bundle.getString("via"),
                    bundle.getString("duration"),
                    bundle.getString("frequency"),
                    bundle.getString("obs"));
            itemList.add(newItem);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        Log.d(TAG, "Activity Started");


//        patientRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                getPatientList(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "Activity Restored");

    }

    private void getPatInfo(DataSnapshot dataSnapshot) {

        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Patient patient = snap.getValue(Patient.class);
            //patientName.setText(patient.getFullName());
            Log.d(TAG, "Patient Full Name " + patient.getFullName());
        }
    }

    private void getPatientInfo(DataSnapshot dataSnapshot) {
        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Doctor doctor = snap.getValue(Doctor.class);
            Log.d(TAG, "Doctor Name: " + doctor.getFirstName());
            doctorName = doctor.getFirstName();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Activity Paused");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Activity Resumed");
        Bundle bundle1 = getIntent().getExtras();
        Log.d(TAG, "BUNDLE " + bundle1);

        if (bundle1 != null) {
//            HashMap<String, String> med = new HashMap<>();
//            med.put("medicine", bundle.getString("medicine"));
//            med.put("duration", bundle.getString("duration"));
//            med.put("frequency", bundle.getString("frequency"));
//            med.put("via", bundle.getString("via"));
//            med.put("obs", bundle.getString("obs"));
            Log.d(TAG, "MEDICINE " + bundle1.getString("medicine"));
            PrescriptionItem newItem = new PrescriptionItem(
                    bundle1.getString("medicine"),
                    bundle1.getString("via"),
                    bundle1.getString("duration"),
                    bundle1.getString("frequency"),
                    bundle1.getString("obs"));
            itemList.add(newItem);
        }

    }

    //    private void getPatientList(DataSnapshot dataSnapshot) {
//
//        List<String> names = new ArrayList<String>();
//        patientList = new ArrayList<>();
//
//        for (DataSnapshot snap: dataSnapshot.getChildren()) {
//            Patient patient = snap.getValue(Patient.class);
//            Log.d(TAG, "PATIENT NAME: " + patient.getFullName());
//            names.add(patient.getFullName());
//            Log.d(TAG, "NAME ADDED");
//            patientList.add(patient);
//            Log.d(TAG, "PATIENT ADDED");
//
//            for (Patient pat : patientList) {
//                Log.d(TAG, "Patient " + snap.getKey());
//            }
//
//        }
//        ArrayAdapter<String> patientNameAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, names);
//        patientName.setAdapter(patientNameAdapter);
//        progressBar.setVisibility(View.GONE);
//    }
}
