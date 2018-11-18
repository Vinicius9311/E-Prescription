package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import eprescription.tcc.ufam.com.e_prescription.Adapter.DoctorsRecyclerViewAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.Model.User;
import eprescription.tcc.ufam.com.e_prescription.R;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference usersDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "MainActivity";
    private String userType;

    private Button createPatient;
    private Button createDoctor;

    private TextView loginMsg;
    private EditText emailLogin;
    private EditText passwordLogin;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button loginButton;
    private AlertDialog loginDialog;
    private AlertDialog.Builder loginDialogBuilder;

    //private ProgressBar mBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        loginButton = (Button) findViewById(R.id.loginID);
        createPatient = (Button) findViewById(R.id.createPatientID);
        createDoctor = (Button) findViewById(R.id.createDoctortID);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();

                if (mUser != null) {
                    // user is signed in
                   // Toast.makeText(MainActivity.this,"User Signed In", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "user signed in");
                    Log.d(TAG, "username: " + mUser.getEmail());
                    //startActivity(new Intent(MainActivity.this, PatientHomeActivity.class));
                    //finish();
                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(MainActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();

                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLoginPopupDialog();
            }
        });

        createPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser == null) {
                    startActivity(new Intent(MainActivity.this, CreatePatientAccountActivity.class));
                }
            }
        });

        createDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser == null) {
                    startActivity(new Intent(MainActivity.this, CreateDoctorAccountActivity.class));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_signout){
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createLoginPopupDialog() {

        loginDialogBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.login_popup, null);
        loginMsg = (TextView) view.findViewById(R.id.loginPopuptextViewID);
        emailLogin = (EditText) view.findViewById(R.id.emailEditTextID);
        passwordLogin = (EditText) view.findViewById(R.id.passwordEditTextID);
        radioGroup = (RadioGroup) view.findViewById(R.id.loginRadioGroupID);
        loginButton = (Button) view.findViewById(R.id.loginButtonID);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) view.findViewById(checkedId);
                switch (radioButton.getId()) {
                    case R.id.patientRadioButtonID:
                        userType = "patient";
                        String selectedID = radioButton.getText().toString();
                        Log.d("RADIOBUTTON", String.valueOf(selectedID));
                        break;
                    case R.id.doctorRadioButtonID:
                        selectedID = radioButton.getText().toString();
                        userType = "doctor";
                        Log.d("RADIOBUTTON", String.valueOf(selectedID));
                        break;
                }
            }
        });

        loginDialogBuilder.setView(view);
        loginDialog = loginDialogBuilder.create();
        loginDialog.show();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = emailLogin.getText().toString();
                String pwdString = passwordLogin.getText().toString();


                if (!emailString.equals("") && !pwdString.equals("")) {
                    mAuth.signInWithEmailAndPassword(emailString, pwdString)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Todo Correct this way to sign in different users

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userID = user.getUid();
                                        getUser(user.getEmail());

                                        Log.d("USERTYPE", String.valueOf(userType));

                                        if (userType.equals("patient")) {
                                            Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this, PatientHomeActivity.class));
                                            finish();
                                            // Todo a progress bar
                                        } else if (userType.equals("doctor")) {
                                            Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this, DoctorHomeActivity.class));
                                            finish();
                                        }
                                            // Todo a progress bar
                                        //Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_LONG).show();
//                                        startActivity(new Intent(MainActivity.this, PatientHomeBottomActivity.class));
//                                        finish();
//
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void getUser(String email) {

        usersDatabaseReference.child("patient").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d(TAG, "PATIENT: " +snap);
                    Patient patient = snap.getValue(Patient.class);
                    Log.d(TAG, "PATIENT: " + patient.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        usersDatabaseReference.child("doctor").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d(TAG, "DOCTOR: " +snap);
                    Doctor doctor = snap.getValue(Doctor.class);
                    Log.d(TAG, "DOCTOR: " + doctor.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Load Cancelled");
            }
        });
    }
}

