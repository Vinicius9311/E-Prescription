package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Adapter.PatientPrescriptionAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientPrescriptionListsActivity extends AppCompatActivity {

    private static final String TAG = "PatientPrescription";
    private TextView myPrescription;
    private RecyclerView prescRecycler;
    private PatientPrescriptionAdapter prescriptionAdapter;
    private List<PatientPrescription> patientPrescriptions;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPatRef = mRootRef.child("users").child("patient");
    private DatabaseReference mPatPresc = mRootRef.child("patientPrescriptions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription_list);

        patientPrescriptions = new ArrayList<>();
        myPrescription = (TextView) findViewById(R.id.myPrescID);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mPatPresc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getPrescriptions(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPrescriptions(DataSnapshot dataSnapshot) {
        patientPrescriptions = new ArrayList<>();
        prescRecycler = (RecyclerView) findViewById(R.id.prescriptionsRecyclerID);

        prescRecycler.setHasFixedSize(true);
        prescRecycler.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "ITEM COUNT HERE: " + patientPrescriptions.size());
        prescriptionAdapter = new PatientPrescriptionAdapter(this, patientPrescriptions);
        prescRecycler.setAdapter(prescriptionAdapter);
        prescriptionAdapter.notifyDataSetChanged();

        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            PatientPrescription patientPrescription = snap.getValue(PatientPrescription.class);
            Log.d(TAG, "PrescriptionID: " + patientPrescription.getPrescriptionID());
            patientPrescriptions.add(patientPrescription);
        }
        Log.d(TAG, "ITEM COUNT: " + patientPrescriptions.size());

    }
}
