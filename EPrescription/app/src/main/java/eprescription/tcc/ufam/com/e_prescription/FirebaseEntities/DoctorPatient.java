package eprescription.tcc.ufam.com.e_prescription.FirebaseEntities;

public class DoctorPatient {
    private String patName;
    private String patKey;

    public DoctorPatient() {
    }

    public DoctorPatient(String patName, String patKey) {
        this.patName = patName;
        this.patKey = patKey;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatKey() {
        return patKey;
    }

    public void setPatKey(String patKey) {
        this.patKey = patKey;
    }
}
