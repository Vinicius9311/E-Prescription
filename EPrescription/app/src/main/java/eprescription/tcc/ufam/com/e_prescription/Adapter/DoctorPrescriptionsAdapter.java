package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Activities.DoctorPrescriptionActivity;
import eprescription.tcc.ufam.com.e_prescription.Model.PatientPrescription;
import eprescription.tcc.ufam.com.e_prescription.R;

public class DoctorPrescriptionsAdapter extends RecyclerView.Adapter<DoctorPrescriptionsAdapter.ViewHolder>{

    /*
        Recycler View Adapter to display prescriptions from Patient on Doctor view
     */

    private Context context;
    private List<PatientPrescription> patientPrescriptions;

    public DoctorPrescriptionsAdapter(Context context, List<PatientPrescription> patientPrescriptions) {
        this.context = context;
        this.patientPrescriptions = patientPrescriptions;
    }

    @NonNull
    @Override
    public DoctorPrescriptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_doctor_prescriptions, parent, false);
        return new DoctorPrescriptionsAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorPrescriptionsAdapter.ViewHolder holder, int position) {
        PatientPrescription patientPrescription = patientPrescriptions.get(position);
        String doctor = "Dr(a). " + patientPrescription.getDoctorName();
        holder.doctorName.setText(doctor);
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String FormatDate = "Data prescrita: " + dateFormat.format(new Date(Long.parseLong(patientPrescription.getDatePrescripted())).getTime());
        holder.datePrescribed.setText(FormatDate);
        String description = "Descrição: " + patientPrescription.getDescription();
        holder.description.setText(description);
    }

    @Override
    public int getItemCount() {
        return patientPrescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView doctorName;
        public TextView datePrescribed;
        public TextView description;

        @Override
        public void onClick(View v) {

        }

        public ViewHolder(View view, final Context context) {
            super(view);

            doctorName = (TextView) view.findViewById(R.id.DoctorID);
            datePrescribed = (TextView) view.findViewById(R.id.dateID);
            description = (TextView) view.findViewById(R.id.descrID);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    PatientPrescription patientPrescription = patientPrescriptions.get(position);

                    Intent intent = new Intent(context, DoctorPrescriptionActivity.class);
                    // TODO  Put intent to get prescription
                    intent.putExtra("prescriptionKey", patientPrescription.getPrescriptionID());
                    intent.putExtra("patientKey", patientPrescription.getPatKey());
                    Log.d("HEREEEE", "PRESC KEY" + patientPrescription.getPrescriptionID());
                    context.startActivity(intent);
                }
            });
        }
    }
}
