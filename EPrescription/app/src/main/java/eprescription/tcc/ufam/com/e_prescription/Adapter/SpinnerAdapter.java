package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class SpinnerAdapter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        Toast.makeText(this,"OnItemSelectedListener: " +parent.getItemAtPosition(position).
                toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
