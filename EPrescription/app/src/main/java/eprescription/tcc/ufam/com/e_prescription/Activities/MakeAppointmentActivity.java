package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.ls.LSInput;

import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Adapter.DoctorsRecyclerViewAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.R;

public class MakeAppointmentActivity extends AppCompatActivity {

    private static final String TAG = "MakeAppointmentActivity";
    private TextView selectText;
    private Spinner specialtySpinner;
    private RecyclerView recyclerView;
    private DoctorsRecyclerViewAdapter doctorsRecyclerViewAdapter;

    private FirebaseDatabase database;
    private DatabaseReference docDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userID;
    private List<Doctor> doctorListFirebase;
    private List<Doctor> doctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        selectText = (TextView) findViewById(R.id.appointmentID);
        specialtySpinner = (Spinner) findViewById(R.id.appointmentSpinnerID);
        recyclerView = (RecyclerView) findViewById(R.id.appointmentRecyclerViewID);

        recyclerView.setHasFixedSize(true);  // items are fixed correctly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ArrayAdapter<CharSequence> adapterSpecialty = ArrayAdapter.createFromResource(this,
                R.array.specialty, android.R.layout.simple_spinner_item);
        adapterSpecialty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialtySpinner.setAdapter(adapterSpecialty);

        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();
        database = FirebaseDatabase.getInstance();
        docDatabaseReference = database.getReference().child("users").child("doctor");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(MakeAppointmentActivity.this,"Email: " + user.getEmail() + "UserID: " + userID, Toast.LENGTH_LONG).show();
                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(MakeAppointmentActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MakeAppointmentActivity.this, MainActivity.class));
                }
            }
        };

        docDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
