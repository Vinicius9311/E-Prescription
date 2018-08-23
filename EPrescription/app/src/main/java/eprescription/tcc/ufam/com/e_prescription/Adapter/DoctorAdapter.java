package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;

public class DoctorAdapter extends ArrayAdapter<Doctor> {
    public DoctorAdapter(Context context, int resource, List<Doctor> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

        }

        return super.getView(position, convertView, parent);
    }
}
