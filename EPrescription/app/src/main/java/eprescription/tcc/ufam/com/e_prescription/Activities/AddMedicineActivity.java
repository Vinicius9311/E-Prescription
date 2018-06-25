package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eprescription.tcc.ufam.com.e_prescription.Model.Medicine;
import eprescription.tcc.ufam.com.e_prescription.R;

public class AddMedicineActivity extends AppCompatActivity {

    private static final String TAG = "AddMedicineActivity";

    private EditText reference;
    private EditText activePrinciple;
    private EditText interchangeable;
    private EditText concentration;
    private EditText pharmaceuticForm;
    private Button add;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference medicineDatabaseReference;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        
        reference = (EditText) findViewById(R.id.referenceID);
        activePrinciple = (EditText) findViewById(R.id.ative_principle);
        interchangeable = (EditText) findViewById(R.id.interchangeableID);
        concentration = (EditText) findViewById(R.id.concentrationID);
        pharmaceuticForm = (EditText) findViewById(R.id.pharmaceuticFormID);
        add = (Button) findViewById(R.id.addButtonID);

        database = FirebaseDatabase.getInstance();
        medicineDatabaseReference = database.getReference().child("medicine");


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(AddMedicineActivity.this,"Email: " + user.getEmail() + "UserID: " + userID, Toast.LENGTH_LONG).show();


                } else {
                    // user is signed out
                    Log.d(TAG, "user signed out");
                    Toast.makeText(AddMedicineActivity.this,"Not Signed In", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddMedicineActivity.this, MainActivity.class));
                }
            }
        };

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Medicine newMedicine = new Medicine(reference.getText().toString(), activePrinciple.getText().toString(),
                        interchangeable.getText().toString(), concentration.getText().toString(),
                        pharmaceuticForm.getText().toString());
                medicineDatabaseReference.child("medicine").child(userID).setValue(newMedicine);
            }
        });
        
    }
}
