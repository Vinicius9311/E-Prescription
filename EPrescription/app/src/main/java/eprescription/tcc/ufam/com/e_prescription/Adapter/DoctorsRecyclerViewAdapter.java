package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Model.Appointment;
import eprescription.tcc.ufam.com.e_prescription.Model.Doctor;
import eprescription.tcc.ufam.com.e_prescription.R;

/*

    RecyclerView to show doctors from specific specialty

 */

public class DoctorsRecyclerViewAdapter extends RecyclerView.Adapter<DoctorsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Doctor> doctorList;

    public DoctorsRecyclerViewAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_doctors, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Doctor doctor = doctorList.get(position);

        holder.name.setText(doctor.getFirstName());
        holder.crm.setText(doctor.getCrm());
        holder.address.setText(doctor.getAddress());
        holder.phone.setText(doctor.getPhone());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView crm;
        public TextView address;
        public TextView phone;
        public Button makeAppointmentBtn;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            name = (TextView) view.findViewById(R.id.doctorNameID);
            crm = (TextView) view.findViewById(R.id.doctorCRMID);
            address = (TextView) view.findViewById(R.id.doctorAddressID);
            phone = (TextView) view.findViewById(R.id.doctorPhoneID);
            makeAppointmentBtn = (Button) view.findViewById(R.id.makeAppoinButtonID);
        }
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.makeAppoinButtonID) {
                int position = getAdapterPosition();
                Doctor doctor = doctorList.get(position);
                makeAppointment(doctor);
            }
        }

        private void makeAppointment(Doctor doctor) {
            // Todo make an appointment
        }
    }


}
