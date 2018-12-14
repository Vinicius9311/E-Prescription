package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.R;

public class DocPatientActionActivity extends AppCompatActivity {

    private static final String TAG = "DocPatActionActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mPatientsReference;

    private TextView patName;
    private TextView dob;
    private TextView sex;
    private TextView email;
    private Button receiptsListBtn;
    private Button prescript;


    private String userID;
    private String patKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_action);

        receiptsListBtn = (Button) findViewById(R.id.docPrescsBtnID);

        database = FirebaseDatabase.getInstance();
        mPatientsReference = database.getReference().child("users").child("patient");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(DocPatientActionActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(DocPatientActionActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DocPatientActionActivity.this, MainActivity.class));
                }
            }
        };

        receiptsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocPatientActionActivity.this, DoctorPrescriptionsActivity.class);
                intent.putExtra("patKey", patKey);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mRootRef.child("users").child("patient").orderByChild("fullName")
//                .equalTo(patientName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HashMap<String, String> pat = new HashMap<>();
            pat.put("patient", bundle.getString("patient"));
            Log.d(TAG, "Patient " + bundle.getString("patient"));

            mPatientsReference.orderByChild("fullName").equalTo(bundle.getString("patient")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getPatientInfo(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void getPatientInfo(DataSnapshot dataSnapshot) {

        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Patient patient = snap.getValue(Patient.class);
            patKey = patient.getKey();

            patName = (TextView) findViewById(R.id.patName);
            dob = (TextView) findViewById(R.id.dateBirth);
            sex = (TextView) findViewById(R.id.sexo);
            email = (TextView) findViewById(R.id.email);

            patName.setText(patient.getFullName());
            dob.setText(patient.getDateOfBirth());
            sex.setText(patient.getSex());
            email.setText(patient.getEmail());

            Log.d(TAG, "Patient Full Name " + patient.getFullName());
        }
    }
}
