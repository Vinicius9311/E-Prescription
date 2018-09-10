package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Adapter.MedicineAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PrescriptionActivity extends AppCompatActivity {

    private static final String TAG = "PrescriptionActivity";
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private AutoCompleteTextView patientName;
    private RecyclerView medicineRecycler;
    private MedicineAdapter medicineAdapter;
    private Button finishBtn;
    private List<Patient> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        patientName = (AutoCompleteTextView) findViewById(R.id.patientID);
        medicineRecycler = (RecyclerView) findViewById(R.id.medicineRecyclerID);
        finishBtn = (Button) findViewById(R.id.finishPrescriptionID);

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
    }

    @Override
    protected void onStart() {
        super.onStart();

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

        }
        ArrayAdapter<String> patientNameAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, names);
        patientName.setAdapter(patientNameAdapter);
    }
}
