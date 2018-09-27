package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientPrescriptionAdapter extends RecyclerView.Adapter<PatientPrescriptionAdapter.ViewHolder> {

    private Context context;
    private List<PatientPrescription> patientPrescriptions;

    public PatientPrescriptionAdapter(Context context, List<PatientPrescription> patientPrescriptions) {
        this.context = context;
        this.patientPrescriptions = patientPrescriptions;
    }

    @NonNull
    @Override
    public PatientPrescriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_patient_prescriptions, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientPrescriptionAdapter.ViewHolder holder, int position) {
        PatientPrescription patientPrescription = patientPrescriptions.get(position);

        holder.doctorName.setText(patientPrescription.getDoctorName());
        holder.datePrescripted.setText(patientPrescription.getDatePrescripted());
        holder.description.setText(patientPrescription.getDescription());
    }

    @Override
    public int getItemCount() {
        return patientPrescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView doctorName;
        public TextView datePrescripted;
        public TextView description;

        @Override
        public void onClick(View v) {

        }

        public ViewHolder(View view, Context context) {
            super(view);

            doctorName = (TextView) view.findViewById(R.id.DoctorNameID);
            datePrescripted = (TextView) view.findViewById(R.id.datePrescID);
            description = (TextView) view.findViewById(R.id.prescrDescID);


        }
    }
}
