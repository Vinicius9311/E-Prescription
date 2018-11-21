package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Activities.DoctorHomeActivity;
import eprescription.tcc.ufam.com.e_prescription.Activities.TakeMedicineActivity;
import eprescription.tcc.ufam.com.e_prescription.Model.PrescriptionItem;
import eprescription.tcc.ufam.com.e_prescription.R;

public class PatientMedicineAdapter extends RecyclerView.Adapter<PatientMedicineAdapter.ViewHolder> {
    /*

        ADAPTER USED ON PATIENT VIEW

     */

    private Context context;
    public LayoutInflater inflater;
    private List<PrescriptionItem> medicines;

    public PatientMedicineAdapter(Context context,  List<PrescriptionItem> medicines) {
        this.context = context;
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public PatientMedicineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_medicine_prescription, parent, false);
        return new PatientMedicineAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientMedicineAdapter.ViewHolder holder, int position) {

        PrescriptionItem prescriptionItem = medicines.get(position);

        holder.medicine.setText(prescriptionItem.getMedicament());
        holder.via.setText(prescriptionItem.getVia());
        if (prescriptionItem.getDuration() == null) {
            holder.duration.setText(prescriptionItem.getDuration());
        } else if (prescriptionItem.getDuration() == String.valueOf(R.string.symptoms)){
            holder.duration.setText(R.string.symptoms);
        } else {
            holder.duration.setText("Durante " + prescriptionItem.getDuration() + " dias");
        }
        holder.frequency.setText("A cada " + prescriptionItem.getFrequency() + " horas");
        holder.observation.setText(prescriptionItem.getObservation());
    }

    @Override
    public int getItemCount() {
        return medicines != null ? medicines.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicine;
        public TextView via;
        public TextView duration;
        public TextView frequency;
        public TextView observation;
        public int id;

        public ViewHolder(final View view, final Context ctx) {
            super(view);

            context = ctx;
            medicine = (TextView) view.findViewById(R.id.MedicineTextID);
            via = (TextView) view.findViewById(R.id.viaID);
            duration = (TextView) view.findViewById(R.id.durationID);
            frequency = (TextView) view.findViewById(R.id.frequenceID);
            observation = (TextView) view.findViewById(R.id.obsRecyclerID);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Toast.makeText(view.getContext(), "position: "+ position, Toast.LENGTH_LONG).show();
                    // TODO GO TO TIME TO TAKE PILLS
                    PrescriptionItem prescriptionItem = medicines.get(position);
                    Intent intent = new Intent(context, TakeMedicineActivity.class);
                    intent.putExtra("medicine", prescriptionItem.getMedicament());
                    intent.putExtra("frequency", prescriptionItem.getFrequency());
                    intent.putExtra("duration", prescriptionItem.getDuration());
                    ctx.startActivity(intent);

                }
            });
        }
    }
}
