package eprescription.tcc.ufam.com.e_prescription.Model;

import java.util.List;

public class Prescription {

    public String datePrescripted;
    public String description;
    public List<PrescriptionItem> prescriptionItems;

    public Prescription() {
    }

    public Prescription(String datePrescripted, String description, List<PrescriptionItem> prescriptionItems) {
        this.datePrescripted = datePrescripted;
        this.description = description;
        this.prescriptionItems = prescriptionItems;
    }

    public String getDatePrescripted() {
        return datePrescripted;
    }

    public void setDatePrescripted(String datePrescripted) {
        this.datePrescripted = datePrescripted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PrescriptionItem> getPrescriptionItems() {
        return prescriptionItems;
    }

    public void setPrescriptionItems(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
    }

    public int medicinesCount (List<PrescriptionItem> prescriptionItems) {
        return  prescriptionItems.size();
    }
}
