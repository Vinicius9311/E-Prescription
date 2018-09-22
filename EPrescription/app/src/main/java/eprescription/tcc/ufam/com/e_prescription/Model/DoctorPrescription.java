package eprescription.tcc.ufam.com.e_prescription.Model;

/*
    Class to represent all prescriptions from doctors
    child(doctorID).setValue(doctorprescription)
 */

public class DoctorPrescription {
    public String patientName;
    public String patientKey;
    public String prescriptionKey;

    public DoctorPrescription() {
    }

    public DoctorPrescription(String patientName, String patientKey, String prescriptionKey) {
        this.patientName = patientName;
        this.patientKey = patientKey;
        this.prescriptionKey = prescriptionKey;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatient(String patientName) {
        this.patientName = patientName;
    }

    public String getPrescriptionKey() {
        return prescriptionKey;
    }

    public void setPrescriptionKey(String prescriptionKey) {
        this.prescriptionKey = prescriptionKey;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientKey() {
        return patientKey;
    }

    public void setPatientKey(String patientKey) {
        this.patientKey = patientKey;
    }
}
