package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Model.PrescriptionItem;
import eprescription.tcc.ufam.com.e_prescription.R;


public class DoctorMedicineAdapter extends RecyclerView.Adapter<DoctorMedicineAdapter.ViewHolder> {
    /*

        ADAPTER USED ON DOCTOR VIEW

     */
    private static final String TAG = "DoctorMedicineAdapter";
    private Context context;
    public LayoutInflater inflater;
    private List<PrescriptionItem> medicines;

    public DoctorMedicineAdapter(Context context, List<PrescriptionItem> medicines) {
        this.context = context;
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_medicine_prescription, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PrescriptionItem prescriptionItem = medicines.get(position);
        Log.d(TAG, "Prescription Item " + prescriptionItem);

        holder.medicine.setText(prescriptionItem.getMedicament());
        holder.via.setText(prescriptionItem.getVia());
        holder.duration.setText(prescriptionItem.getDuration());
            if (!prescriptionItem.getDuration().equals("Até o desaparecimento dos sintomas")) {
                holder.duration.setText("Durante " + prescriptionItem.getDuration() + " dias");
            } else if (prescriptionItem.getDuration().equals("Até o desaparecimento dos sintomas")) {
                holder.duration.setText(prescriptionItem.getDuration());
            }
        holder.frequency.setText("A cada " + prescriptionItem.getFrequency() + " horas");
        holder.observation.setText(prescriptionItem.getObservation());
//

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

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;
            medicine = (TextView) view.findViewById(R.id.MedicineTextID);
            via = (TextView) view.findViewById(R.id.viaID);
            duration = (TextView) view.findViewById(R.id.durationID);
            frequency = (TextView) view.findViewById(R.id.frequenceID);
            observation = (TextView) view.findViewById(R.id.obsRecyclerID);
        }
    }
}
