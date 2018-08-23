package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.R;

public class CreateDoctorAccountActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private Spinner specialty;
    private EditText crm;
    private EditText address;
    private EditText phone;
    private EditText doctorEmail;
    private EditText dateOfBirth;
    private RadioGroup sexRadioGroup;
    private RadioButton radioButton;
    private String sex;
    private EditText password;
    private EditText passwordConfirmation;
    private Button register;

    private FirebaseDatabase database;
    private DatabaseReference doctorsDatabaseReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doctor);

        database = FirebaseDatabase.getInstance();
        doctorsDatabaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        firstName = (EditText) findViewById(R.id.doctorEditTextNameID);
        lastName = (EditText) findViewById(R.id.doctorEditTextSurnameID);
        specialty = (Spinner) findViewById(R.id.doctorSpinnerSpecialtyID);
        crm = (EditText) findViewById(R.id.doctorEditTextCrmID);
        address = (EditText) findViewById(R.id.docAddressID);
        phone = (EditText) findViewById(R.id.docPhoneID);
        doctorEmail = (EditText) findViewById(R.id.doctorEditTextEmailRegisterID);
        dateOfBirth = (EditText) findViewById(R.id.doctorEditTextDateOfBirthID);
        sexRadioGroup = (RadioGroup) findViewById(R.id.doctorRadioGroupID);
        password = (EditText) findViewById(R.id.doctorPasswordRegisterID);
        passwordConfirmation = (EditText) findViewById(R.id.doctorConfPasswordRegisterID);
        register = (Button) findViewById(R.id.registerDoctorID);

        sexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        ArrayAdapter<CharSequence> adapterSpecialty = ArrayAdapter.createFromResource(this,
                R.array.specialty, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpecialty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        specialty.setAdapter(adapterSpecialty);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createDoctorAccount();
                final String name = firstName.getText().toString();
                final String surname = lastName.getText().toString();
                final String doctorSpecialty = String.valueOf(specialty.getSelectedItem());
                final String doctorCrm = crm.getText().toString();
                final String docAddress = address.getText().toString();
                final String docPhone = phone.getText().toString();
                final String email = doctorEmail.getText().toString();
                final String dob = dateOfBirth.getText().toString();
                final String dateCreated = String.valueOf(java.lang.System.currentTimeMillis());
                final String dateModified = String.valueOf(java.lang.System.currentTimeMillis());
                final String pwd = password.getText().toString();
                final String confPwd = passwordConfirmation.getText().toString();
                final String Type = "Doctor";

                if (!password.getText().toString().equals("") &&
                        !passwordConfirmation.getText().toString().equals("") &&
                        !name.equals("") && !surname.equals("") && !email.equals("") &&
                        !dob.equals("") && !doctorSpecialty.equals("") && !doctorCrm.equals("") &&
                        !docAddress.equals("") && !docPhone.equals("")) {

                    if (confPwd.equals(password.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(email, pwd).
                                addOnCompleteListener(CreateDoctorAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String userID = user.getUid();
                                            Doctor doctor = new Doctor(name, surname, doctorSpecialty, doctorCrm, docAddress,
                                                    docPhone, email, dob, sex, dateModified, dateCreated, pwd, Type);
                                            //doctorsDatabaseReference.child("users").child("doctor").child(doctorSpecialty).child(userID).setValue(doctor);
                                            doctorsDatabaseReference.child("users").child("doctor").child(userID).setValue(doctor);
                                            Intent intent = new Intent(CreateDoctorAccountActivity.this, DoctorHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            Toast.makeText(CreateDoctorAccountActivity.this, "Conta Criada", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Intent intent = new Intent(CreateDoctorAccountActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            Toast.makeText(CreateDoctorAccountActivity.this, "Falha ao Criar Conta", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
}
