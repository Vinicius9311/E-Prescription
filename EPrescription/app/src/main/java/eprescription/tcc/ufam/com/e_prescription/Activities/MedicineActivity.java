package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Model.Medicine;
import eprescription.tcc.ufam.com.e_prescription.R;

public class MedicineActivity extends AppCompatActivity {

    private static final String TAG = "MedicineActivity";
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private TextView medicine;
    private AutoCompleteTextView medicineAutoComplete;
    private TextView adminVia;
    private Spinner viaSpinner;
    private TextView durationText;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView durationTextView;
    private TextView frequenceText;
    private TextView frequenceBtn;
    private TextView observation;
    private EditText obsEditText;
    private Button addButton;

    private List<Medicine> medicineList;
    private AlertDialog daysDialog;
    private AlertDialog.Builder daysDialogBuilder;
    private AlertDialog freqDialog;
    private AlertDialog.Builder freqDialogBuilder;
    private NumberPicker daysPicker;
    private NumberPicker freqPicker;
    private TextView during;
    private TextView days;
    private TextView ok;
    private TextView cancel;
    private TextView each;
    private TextView hourS;
    private TextView okFreq;
    public String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        medicine = (TextView) findViewById(R.id.medTextID);
        medicineAutoComplete = (AutoCompleteTextView) findViewById(R.id.medicineAutoTextviewID);
        adminVia = (TextView) findViewById(R.id.viaID);
        viaSpinner = (Spinner) findViewById(R.id.viaSpinnerID);
        durationText = (TextView) findViewById(R.id.duratioTextID);
        radioGroup = (RadioGroup) findViewById(R.id.prescriptionRadioGroupID);
        durationTextView = (TextView) findViewById(R.id.durationTextID);
        frequenceText = (TextView) findViewById(R.id.frequenceTextID);
        frequenceBtn = (TextView) findViewById(R.id.frequenceBtnID);
        observation = (TextView) findViewById(R.id.obsID);
        obsEditText = (EditText) findViewById(R.id.obsTextID);
        addButton = (Button) findViewById(R.id.addMedicineID);

        ArrayAdapter<CharSequence> viaSpecialty = ArrayAdapter.createFromResource(this,
                R.array.admin_via, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        viaSpecialty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        viaSpinner.setAdapter(viaSpecialty);

        durationTextView.setVisibility(View.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioButton = (RadioButton) findViewById(checkedId);

                switch (radioButton.getId()) {
                    case R.id.noDueDateButtonID: {
                        durationTextView.setVisibility(View.INVISIBLE);
                        duration = null;
                    }
                    break;
                    case R.id.daysQtyID: {
                        durationTextView.setVisibility(View.VISIBLE);
                        durationTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chooseDaysQty();
                            }
                        });
                        //duration = String.valueOf(daysPicker.getValue());
                    }
                    break;
                    case R.id.symptomID: {
                        durationTextView.setVisibility(View.VISIBLE);
                        duration = "Até o desaparecimento dos sintomas";
                        durationTextView.setText(duration);
                    }
                    break;
                }
            }
        });

        frequenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFrequency();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicineActivity.this, PrescriptionActivity.class);
                Log.d(TAG, "Medicine: " +medicineAutoComplete.getText().toString());
                intent.putExtra("medicine", medicineAutoComplete.getText().toString());

                Log.d(TAG, "duration: " + duration);
                if (duration != null || duration!= "Até o desaparecimento dos sintomas") {
                    intent.putExtra("duration", String.valueOf(daysPicker.getValue()));
                    Log.d(TAG, "duration: " + String.valueOf(daysPicker.getValue()));
                } else {
                    intent.putExtra("duration", duration);
                    Log.d(TAG, "duration: " + duration);
                }
                Log.d(TAG, "frequency: " +String.valueOf(freqPicker.getValue()));
                intent.putExtra("frequency", String.valueOf(freqPicker.getValue()));

                Log.d(TAG, "via: " +String.valueOf(viaSpinner.getSelectedItem()));
                intent.putExtra("via", String.valueOf(viaSpinner.getSelectedItem()));

                Log.d(TAG, "obs: " +obsEditText.getText().toString());
                intent.putExtra("obs", obsEditText.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void chooseFrequency() {
        freqDialogBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.frequency, null);
        freqPicker = (NumberPicker) view.findViewById(R.id.frequencyPickerID);
        each = (TextView) view.findViewById(R.id.eachHourID);
        hourS = (TextView) view.findViewById(R.id.hourSID);
        okFreq = (TextView) view.findViewById(R.id.okFreqID);

        freqPicker.setMinValue(0);
        freqPicker.setMaxValue(24);
        freqPicker.setWrapSelectorWheel(true);
        freqPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                frequenceBtn.setText("A cada " + newVal + " hora(s)");
                Log.d(TAG, "FREQUENCY PICKER: " +newVal);
            }
        });

        freqDialogBuilder.setView(view);
        freqDialog = freqDialogBuilder.create();
        freqDialog.show();
    }

    private void chooseDaysQty() {
        daysDialogBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.days, null);
        daysPicker = (NumberPicker) view.findViewById(R.id.numberPickerID);
        during = (TextView) view.findViewById(R.id.duringID);
        days = (TextView) view.findViewById(R.id.daysID);
        ok = (TextView) view.findViewById(R.id.okID);
        cancel = (TextView) view.findViewById(R.id.cancelID);

        daysPicker.setMaxValue(120);
        daysPicker.setMinValue(0);
        daysPicker.setWrapSelectorWheel(true);
        daysPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                durationTextView.setText("Durante " + newVal + " dias");
                Log.d(TAG, "DAYS PICKER: " +newVal);
            }
        });

        daysDialogBuilder.setView(view);
        daysDialog = daysDialogBuilder.create();
        daysDialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daysDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference medRef = mRootRef.child("medicine");
        medRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getMedicaments(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMedicaments(DataSnapshot dataSnapshot) {

        medicineList = new ArrayList<>();
        ArrayList<String> meds = new ArrayList<String>();

        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Medicine medicine = snap.getValue(Medicine.class);
            Log.d(TAG, "PATIENT NAME: " + medicine.getMedicament());
            meds.add(medicine.getMedicament());
            medicineList.add(medicine);
        }

        ArrayAdapter<String> medicineAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, meds);
        medicineAutoComplete.setAdapter(medicineAdapter);
    }
}
