package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eprescription.tcc.ufam.com.e_prescription.Adapter.SpinnerAdapter;
import eprescription.tcc.ufam.com.e_prescription.Model.Customer;
import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientRegisterActivity extends AppCompatActivity {

    private TextView titlePatientRegister;
    private EditText firstName;
    private EditText lastName;
    private EditText patientEmail;
    private EditText dateOfBirth;
    private Spinner maritalStatus;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    public String sex;
    private Spinner bloodType;
    private EditText password;
    private EditText passwordConfirmation;
    private Button registerButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference patientDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        patientDatabaseReference = firebaseDatabase.getReference().child("patient");
        mAuth = FirebaseAuth.getInstance();

        titlePatientRegister = (TextView) findViewById(R.id.patientTitleRegisterID);
        firstName = (EditText) findViewById(R.id.patientEditTextNameID);
        lastName = (EditText) findViewById(R.id.patientEditTextSurnameID);
        patientEmail = (EditText) findViewById(R.id.patientEditTextEmailRegisterID);
        dateOfBirth = (EditText) findViewById(R.id.patientEditTextDateOfBirthID);
        maritalStatus = (Spinner) findViewById(R.id.patientSpinnerMaritalStateID);
        radioGroup = (RadioGroup) findViewById(R.id.patientRadioGroupID);
        bloodType = (Spinner) findViewById(R.id.patientSpinnerBloodTypeID);
        password = (EditText) findViewById(R.id.patientPasswordRegisterID);
        passwordConfirmation = (EditText) findViewById(R.id.patientConfPasswordRegisterID);
        registerButton = (Button) findViewById(R.id.registerPatientButtonID);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                switch (radioButton.getId()) {
                    case R.id.patientRadioButtonFemaleID: {
                        // Todo save to firebase
                        sex = "Female";
                    }
                    break;
                    case R.id.patientRadioButtonMaleID: {
                        // Todo save to firebase
                        sex = "Male";
                    }
                    break;

                }
            }
        });

        // Marital Sex
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterMartialStatus = ArrayAdapter.createFromResource(this,
                R.array.marital_status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMartialStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        maritalStatus.setAdapter(adapterMartialStatus);
        //maritalStatus.setOnItemSelectedListener(new SpinnerAdapter());

        // Blood Type
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterBloodType = ArrayAdapter.createFromResource(this,
                R.array.blood_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBloodType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        bloodType.setAdapter(adapterBloodType);
        //bloodType.setOnItemSelectedListener(new SpinnerAdapter());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!password.getText().toString().equals("") &&
                        !passwordConfirmation.getText().toString().equals("")) {

                    String name = firstName.getText().toString();
                    String surname = lastName.getText().toString();
                    String email = patientEmail.getText().toString();
                    String dob = dateOfBirth.getText().toString();
                    String mStatus = String.valueOf(maritalStatus.getSelectedItem());
                    //String sex = String.valueOf(radioButton.getId());
                    String blood = String.valueOf(bloodType.getSelectedItem());
                    String dateCreated = String.valueOf(java.lang.System.currentTimeMillis());
                    String dateModified = String.valueOf(java.lang.System.currentTimeMillis());
                    String pwd = password.getText().toString();
                    //Customer customer = new Customer("Vini", "Helena", "vini@gmail.com", 25);

                    Patient patient = new Patient(name, surname, email, dob, mStatus, sex, blood, dateModified, dateCreated, pwd);
                    patientDatabaseReference.setValue(patient);
                    //Toast.makeText(PatientRegisterActivity.this, "name is: " + name, Toast.LENGTH_LONG).show();
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(PatientRegisterActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(PatientRegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(PatientRegisterActivity.this, "Failed to create Account", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    startActivity(new Intent(
                            PatientRegisterActivity.this, PatientHomeDrawerActivity.class));
                    finish();
                } else {
                    Toast.makeText(PatientRegisterActivity.this, "Por favor, insira seus dados nos campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
