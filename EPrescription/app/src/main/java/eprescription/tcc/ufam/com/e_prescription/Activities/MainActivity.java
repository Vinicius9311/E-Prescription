package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

import eprescription.tcc.ufam.com.e_prescription.Model.Customer;
import eprescription.tcc.ufam.com.e_prescription.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference patientDatabaseReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";

    private EditText email;
    private EditText password;
    private Button login;
    private Button signout;
    private Button createAcc;

    private TextView loginMsg;
    private EditText emailLogin;
    private EditText passwordLogin;
    private Button loginButton;
    private AlertDialog loginDialog;
    private AlertDialog.Builder loginDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.emailID);
        password = (EditText) findViewById(R.id.passwordID);
        login = (Button) findViewById(R.id.loginID);
        signout = (Button) findViewById(R.id.signoutID);
        createAcc = (Button) findViewById(R.id.createAccountID);
        mAuth = FirebaseAuth.getInstance();


        // Write a message to the database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        mUsersDatabaseReference = database.getReference().child("users");
        patientDatabaseReference = database.getReference().child("patient");

        //myRef.setValue("Hello, World!");

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(Customer.class);
//                Log.d("MainActivity", "Value is " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("MainActivity", "Failed to read value");
//            }
//        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // user is signed in
                    Log.d(TAG, "user signed in");
                    Log.d(TAG, "username: " +user.getEmail());
                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLoginPopupDialog();
//                String emailString = email.getText().toString();
//                String pwdString = password.getText().toString();
//
//                if (!emailString.equals("") && !pwdString.equals("")) {
//                    mAuth.signInWithEmailAndPassword(emailString, pwdString)
//                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (!task.isSuccessful()) {
//                                        Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_LONG).show();
//                                    } else {
//                                        Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_LONG).show();
//
//                                        Customer customer = new Customer("Vini", "Helena", "vini@gmail.com", 25);
//                                        // We can now write to the database
//                                        //myRef.setValue("We are in again");
//                                        myRef.setValue(customer);
//                                    }
//                                }
//                            });
//                }
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this,"You've signed out", Toast.LENGTH_LONG).show();
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PatientRegisterActivity.class));

//                String emailString = email.getText().toString();
//                String pwdString = password.getText().toString();
//
//                if (!emailString.equals("") && !pwdString.equals("")) {
//                    mAuth.createUserWithEmailAndPassword(emailString, pwdString).addOnCompleteListener(MainActivity.this,
//                            new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(MainActivity.this, "Account created", Toast.LENGTH_SHORT).show();
//
//                                    } else {
//                                        Toast.makeText(MainActivity.this, "Failed to create Account", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                }
            }
        });
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
        View view = getLayoutInflater().inflate(R.layout.login_popup, null);
        loginMsg = (TextView) view.findViewById(R.id.loginPopuptextViewID);
        emailLogin = (EditText) view.findViewById(R.id.emailEditTextID);
        passwordLogin = (EditText) view.findViewById(R.id.passwordEditTextID);
        loginButton = (Button) view.findViewById(R.id.loginButtonID);

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
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Failed to sign in", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(MainActivity.this, PatientHomeDrawerActivity.class));
//
//                                        Customer customer = new Customer("Vini", "Helena", "vini@gmail.com", 25);
//                                        // We can now write to the database
//                                        //myRef.setValue("We are in again");
//                                        mUsersDatabaseReference.setValue(customer);
                                    }
                                }
                            });
                }
            }
        });
    }
}

