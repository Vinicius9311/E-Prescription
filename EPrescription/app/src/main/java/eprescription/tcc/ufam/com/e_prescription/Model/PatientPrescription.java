package eprescription.tcc.ufam.com.e_prescription.Model;

public class PatientPrescription {
    public String doctorName;
    public String doctorKey;
    public String prescriptionID;

    public PatientPrescription() {
    }

    public PatientPrescription(String doctorName, String doctorKey, String prescriptionID) {
        this.doctorName = doctorName;
        this.doctorKey = doctorKey;
        this.prescriptionID = prescriptionID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorKey() {
        return doctorKey;
    }

    public void setDoctorKey(String doctorKey) {
        this.doctorKey = doctorKey;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }
}
