package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Adapter.DoctorMedicineAdapter;
import eprescription.tcc.ufam.com.e_prescription.Adapter.PatientMedicineAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.Model.Prescription;
import eprescription.tcc.ufam.com.e_prescription.Model.PrescriptionItem;
import eprescription.tcc.ufam.com.e_prescription.R;

public class DoctorPrescriptionActivity extends AppCompatActivity {

    /*
        Activity to display one patient Prescription on Doctor View
     */

    private static final String TAG = "DOCTORPRESCRIPTION";
    private TextView docName;
    private TextView datePresc;
    private TextView descPresc;
    private RecyclerView medRecyclerView;
    private DoctorMedicineAdapter medicineAdapter;
    private Prescription prescription;
    private List<PrescriptionItem> medicines;
    private String userID;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPresc = mRootRef.child("prescription");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescription);
        docName = (TextView) findViewById(R.id.docID);
        datePresc = (TextView) findViewById(R.id.date);
        descPresc = (TextView) findViewById(R.id.prescDescID);
        medRecyclerView = (RecyclerView) findViewById(R.id.docPrescRecID);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(DoctorPrescriptionActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "patient signed in");
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(DoctorPrescriptionActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DoctorPrescriptionActivity.this, MainActivity.class));
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Log.d(TAG, "Prescription Key: " + bundle.getString("prescriptionKey"));
            Toast.makeText(DoctorPrescriptionActivity.this, "Prescription Key: "
                    + bundle.getString("prescriptionKey"), Toast.LENGTH_LONG).show();

            mPresc.orderByKey().equalTo(bundle.getString("prescriptionKey")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getMedicines(dataSnapshot);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "Error");
                }
            });

            // TODO change medicine adapter


            mRootRef.child("patientPrescriptions").child(userID).orderByChild("prescriptionID").equalTo(bundle.getString("prescriptionKey"))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                Log.d(TAG, "ENTROU " + snap);

                                PatientPrescription patientPrescription = snap.getValue(PatientPrescription.class);
                                Log.d(TAG, "Doctor Name: " + patientPrescription.getDoctorName());
                                String doctor = "Dr(a). " + patientPrescription.getDoctorName();
                                docName.setText(doctor);
                                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                                String FormatDate = "Data prescrita: " + dateFormat.format(new Date(Long.parseLong(patientPrescription.getDatePrescripted())).getTime());
                                datePresc.setText(FormatDate);
                                String description = "Descrição: " + patientPrescription.getDescription();
                                descPresc.setText(description);
                            }
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

    private void getMedicines(DataSnapshot snapshot) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            Log.d(TAG, "ENTROU ");

            prescription = snap.getValue(Prescription.class);
            medicines = prescription.prescriptionItems;
            Log.d(TAG, "Description: " + prescription.getDescription());
            Log.d(TAG, "Prescription Items: " + prescription.medicinesCount(prescription.prescriptionItems));
            medRecyclerView.setHasFixedSize(true);
            medRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            medicineAdapter = new DoctorMedicineAdapter(this, prescription.prescriptionItems);
            medRecyclerView.setAdapter(medicineAdapter);
            medicineAdapter.notifyDataSetChanged();

        }
    }
}