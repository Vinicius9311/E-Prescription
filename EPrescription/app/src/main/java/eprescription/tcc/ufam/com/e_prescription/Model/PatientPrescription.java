package eprescription.tcc.ufam.com.e_prescription.Model;

public class PatientPrescription {
    public String doctorName;
    public String doctorKey;
    public String prescriptionID;
    public String description;
    public String datePrescripted;
    public String patKey;

    public PatientPrescription() {
    }

    public PatientPrescription(String doctorName, String doctorKey, String prescriptionID,
                               String description, String datePrescripted, String patKey) {
        this.doctorName = doctorName;
        this.doctorKey = doctorKey;
        this.prescriptionID = prescriptionID;
        this.description = description;
        this.datePrescripted = datePrescripted;
        this.patKey = patKey;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePrescripted() {
        return datePrescripted;
    }

    public void setDatePrescripted(String datePrescripted) {
        this.datePrescripted = datePrescripted;
    }

    public String getPatKey() {
        return patKey;
    }

    public void setPatKey(String patKey) {
        this.patKey = patKey;
    }
}
