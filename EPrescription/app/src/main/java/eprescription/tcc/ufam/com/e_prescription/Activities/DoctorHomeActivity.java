package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.w3c.dom.Text;

import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.Model.Prescription;
import eprescription.tcc.ufam.com.e_prescription.R;

public class DoctorHomeActivity extends AppCompatActivity {


    private static final String TAG = "DoctorHomeActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDoctorReference;

    private TextView doctorName;

    private Button med;
    private Button signout;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        doctorName = (TextView) findViewById(R.id.doctorTextHomeID);
        med = (Button)  findViewById(R.id.addMedBtnID);
        signout = (Button) findViewById(R.id.leaveBtnID);

        database = FirebaseDatabase.getInstance();
        mDoctorReference = database.getReference().child("users").child("doctor");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = mUser.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(DoctorHomeActivity.this,"UserID: " + userID, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "doctor signed in");
                    Log.d(TAG, "username: " + user.getEmail());
                    Log.d(TAG, "userID: " + userID);

                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(DoctorHomeActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DoctorHomeActivity.this, MainActivity.class));
                }
            }
        };

        mDoctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getName(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(DoctorHomeActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
                startActivity(new Intent(DoctorHomeActivity.this, MainActivity.class));
                finish();
            }
        });

        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorHomeActivity.this, AddMedicineActivity.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorHomeActivity.this, PrescriptionActivity.class));
//                startActivity(new Intent(PatientHomeActivity.this, AddMedicineActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_signout){
            mAuth.signOut();
            Toast.makeText(DoctorHomeActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getName(DataSnapshot dataSnapshot) {
        Doctor doctor = dataSnapshot.getValue(Doctor.class);
        Log.d("HERE", "VALUE IS: " + dataSnapshot.getValue()) ;
        // TODO a DoctorAdapter
        //doctor.setFirstName(dataSnapshot.child(userID).getValue(Doctor.class).getFirstName());
        //doctorName.setText(doctor.getFirstName());
    }
}