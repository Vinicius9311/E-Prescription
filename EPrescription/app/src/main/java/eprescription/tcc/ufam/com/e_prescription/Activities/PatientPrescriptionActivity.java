package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.Model.Prescription;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientPrescriptionActivity extends AppCompatActivity {

    private static final String TAG = "PATIENTPRESCRIPTION";
    private TextView docName;
    private TextView datePresc;
    private TextView descPresc;
    private RecyclerView medRecyclerView;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPresc = mRootRef.child("prescription");


    // TODO PUT INFO ON RECYCLER VIEW 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription);

        docName = (TextView) findViewById(R.id.docNameID);
        datePresc = (TextView) findViewById(R.id.datePrescID);
        descPresc = (TextView) findViewById(R.id.prescDescID);
        medRecyclerView = (RecyclerView) findViewById(R.id.patPrescRecID);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Log.d(TAG, "Prescription Key: " + bundle.getString("prescriptionKey"));
            Toast.makeText(PatientPrescriptionActivity.this, "Prescription Key: "
                    + bundle.getString("prescriptionKey"), Toast.LENGTH_LONG).show();

            mPresc.orderByKey().equalTo(bundle.getString("prescriptionKey")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Log.d(TAG, "ENTROU ");

                        Prescription prescription = snap.getValue(Prescription.class);
                        Log.d(TAG, "Description: " + prescription.getDescription().toString());
                        Log.d(TAG, "Prescription Items: " + prescription.medicinesCount(prescription.prescriptionItems));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "Error");
                }
            });

            mRootRef.child("patientPrescriptions").orderByChild("prescriptionID").equalTo(bundle.getString("prescriptionKey"))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Log.d(TAG, "ENTROU ");

                        PatientPrescription patientPrescription = snap.getValue(PatientPrescription.class);
                        Log.d(TAG, "Doctor Name: " + patientPrescription.getDoctorName().toString());
                        docName.setText(patientPrescription.getDoctorName());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}