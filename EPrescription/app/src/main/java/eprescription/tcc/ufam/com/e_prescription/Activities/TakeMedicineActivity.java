package eprescription.tcc.ufam.com.e_prescription.Activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import eprescription.tcc.ufam.com.e_prescription.R;

public class TakeMedicineActivity extends AppCompatActivity {

    private Button setDate;
    private DatePicker datePicker;
    private Button okButton;
    private AlertDialog calendarDialog;
    private AlertDialog.Builder calendarDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_medicine);

        setDate = (Button) findViewById(R.id.pickDateBtnID);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendarPopUp();
            }
        });
    }

    private void createCalendarPopUp() {
        calendarDialogBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.calendar_popup, null);
        // TODO FINISH CALENDAR POPUP
        datePicker = (DatePicker) view.findViewById(R.id.datePickerID);
        okButton = (Button) view.findViewById(R.id.okbtnID);
        calendarDialogBuilder.setView(view);
        calendarDialog = calendarDialogBuilder.create();
        calendarDialog.show();
    }
}
