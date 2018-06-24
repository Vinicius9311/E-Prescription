package eprescription.tcc.ufam.com.e_prescription.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.util.List;

import eprescription.tcc.ufam.com.e_prescription.Model.Appointment;
import eprescription.tcc.ufam.com.e_prescription.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Appointment> medicalAppointments;

    public RecyclerViewAdapter(Context context, List<Appointment> medicalAppointments) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView crm;
        public TextView address;
        public TextView phone;
        public Button makeAppointment;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

//            name = (TextView) view.findViewById(R.id.doctorNameID);
//            crm = (TextView) view.findViewById(R.id.doctorCRMID);
//            address = (TextView) view.findViewById(R.id.doctorAddressID);
//            phone = (TextView) view.findViewById(R.id.doctorPhoneID);
//            makeAppointment = (Button) view.findViewById(R.id.makeAppoinButtonID);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
