package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import eprescription.tcc.ufam.com.e_prescription.Adapter.MedicineAdapter;
import eprescription.tcc.ufam.com.e_prescription.FirebaseEntities.DoctorPatient;
import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.Model.DoctorPrescription;
import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.Model.Prescription;
import eprescription.tcc.ufam.com.e_prescription.Model.PrescriptionItem;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PrescriptionActivity extends AppCompatActivity {

    private static final String TAG = "PrescriptionActivity";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mDocRef = mRootRef.child("users").child("doctor");
    private DatabaseReference mPatRef = mRootRef.child("users").child("patient");
    private DatabaseReference mPresc = mRootRef.child("prescription");
    private DatabaseReference mDocPresc = mRootRef.child("doctorPrescriptions");
    private DatabaseReference mPatPresc = mRootRef.child("patientPrescriptions");
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private AutoCompleteTextView patientName;
    private RecyclerView medicineRecycler;
    private MedicineAdapter medicineAdapter;
    private Button finishBtn;
    private List<Patient> patientList;
    private List<PrescriptionItem> itemList;
    private String doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<PrescriptionItem>();
        patientName = (AutoCompleteTextView) findViewById(R.id.patientID);
        medicineRecycler = (RecyclerView) findViewById(R.id.medicineRecyclerID);
        finishBtn = (Button) findViewById(R.id.finishPrescriptionID);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String userID = mUser.getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(PrescriptionActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "doctor signed in");
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(PrescriptionActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PrescriptionActivity.this, MainActivity.class));
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrescriptionActivity.this, MedicineActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        medicineRecycler.setHasFixedSize(true);
        medicineRecycler.setLayoutManager(new LinearLayoutManager(this));

        medicineAdapter = new MedicineAdapter(this, itemList);
        addToRecyclerView();
        medicineRecycler.setAdapter(medicineAdapter);
        medicineAdapter.notifyDataSetChanged();

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
                                Log.d(TAG, "Patient Key: " + patientKey);
                                // mPresc.push().setValue(patientKey);  funciona
                                String prescID = mPresc.push().getKey();
                                Log.d(TAG, "Prescription Key: " + prescID);
                                mPresc.child(prescID).setValue(itemList);
                                DoctorPrescription doctorPrescription = new DoctorPrescription(
                                        patientName.getText().toString(), patientKey, prescID);
                                mDocPresc.child(userID).setValue(doctorPrescription);
                                PatientPrescription patientPrescription = new PatientPrescription(
                                    doctorName, userID, prescID
                                );
                                mPatPresc.child(patientKey).setValue(patientPrescription);
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


    }


    private void addToRecyclerView() {
        Bundle bundle = getIntent().getExtras();
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

        List<String> names = new ArrayList<String>();
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
