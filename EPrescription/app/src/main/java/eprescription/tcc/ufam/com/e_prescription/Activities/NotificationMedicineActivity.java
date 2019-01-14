package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import eprescription.tcc.ufam.com.e_prescription.R;

public class NotificationMedicineActivity extends AppCompatActivity {

    private TextView medName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_medicine);

        medName = (TextView) findViewById(R.id.medNameID);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            medName.setText(bundle.getString("med"));
        }
    }
}
