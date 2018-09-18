package eprescription.tcc.ufam.com.e_prescription.FirebaseEntities;

public class DoctorPatient {
    private String doctorKey;
    private String patKey;

    public DoctorPatient() {
    }

    public DoctorPatient(String doctorKey, String patKey) {
        this.doctorKey = doctorKey;
        this.patKey = patKey;
    }

    public String getDoctorKey() {
        return doctorKey;
    }

    public void setDoctorKey(String doctorKey) {
        this.doctorKey = doctorKey;
    }

    public String getPatKey() {
        return patKey;
    }

    public void setPatKey(String patKey) {
        this.patKey = patKey;
    }
}
