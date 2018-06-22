package eprescription.tcc.ufam.com.e_prescription.Model;

import android.support.v7.app.AppCompatActivity;

public class Appointment {

    private String doctor;
    private String specialty;
    private String appointmentDate;

    public Appointment() {

    }

    public Appointment(String doctor, String specialty, String appointmentDate) {
        this.doctor = doctor;
        this.specialty = specialty;
        this.appointmentDate = appointmentDate;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
