package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import eprescription.tcc.ufam.com.e_prescription.Model.Patient;
import eprescription.tcc.ufam.com.e_prescription.R;

public class CreatePatientAccountActivity extends AppCompatActivity {

    private TextView titlePatientRegister;
    private EditText fullName;
    private EditText patientEmail;
    private EditText dateOfBirth;
    private EditText cpf;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    public String sex;
    private EditText sus;
    private EditText password;
    private EditText passwordConfirmation;
    private Button registerButton;

    private FirebaseDatabase database;
    private DatabaseReference patientDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);

        database = FirebaseDatabase.getInstance();
        patientDatabaseReference = database.getReference();
                //.child("users").child("patient");


        patientDatabaseReference.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        titlePatientRegister = (TextView) findViewById(R.id.patientTitleRegisterID);
        fullName = (EditText) findViewById(R.id.patientEditTextNameID);
        patientEmail = (EditText) findViewById(R.id.patientEditTextEmailRegisterID);
        dateOfBirth = (EditText) findViewById(R.id.patientEditTextDateOfBirthID);
        cpf = (EditText) findViewById(R.id.cpfEditTextID);
        radioGroup = (RadioGroup) findViewById(R.id.patientRadioGroupID);
        sus = (EditText) findViewById(R.id.SUSeditTextID);
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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = fullName.getText().toString();
                final String email = patientEmail.getText().toString();
                final String dob = dateOfBirth.getText().toString();
                final String cpfNumber = cpf.getText().toString();
                //String sex = String.valueOf(radioButton.getId());
                final String dateCreated = String.valueOf(java.lang.System.currentTimeMillis());
                final String dateModified = String.valueOf(java.lang.System.currentTimeMillis());
                final String susNumber = sus.getText().toString();
                final String pwd = password.getText().toString();
                final String type = "Patient";

                if (!password.getText().toString().equals("") &&
                        !passwordConfirmation.getText().toString().equals("") &&
                        !name.equals("") && !email.equals("") &&
                        !dob.equals("") && !cpfNumber.equals("") && !susNumber.equals("")) {

                    if (passwordConfirmation.getText().toString().equals(password.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(email,pwd).
                                addOnCompleteListener(CreatePatientAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String userID = user.getUid();

                                            Patient patient = new Patient(name, email, dob, cpfNumber, sex, susNumber, dateModified,
                                                    dateCreated, pwd, type);
                                            patientDatabaseReference.child("users").child("patient").child(userID).setValue(patient);

                                            Intent intent = new Intent(CreatePatientAccountActivity.this, PatientHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            Toast.makeText(CreatePatientAccountActivity.this, "Conta Criada", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(CreatePatientAccountActivity.this, "Criação de conta falhou", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CreatePatientAccountActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(CreatePatientAccountActivity.this, "Senhas diferentes digitadas, insira novamente", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(CreatePatientAccountActivity.this, "Por favor, insira seus dados nos campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
