package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientPrescription extends AppCompatActivity {

    private TextView myPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription);

        myPrescription = (TextView) findViewById(R.id.myPrescID);
    }
}
