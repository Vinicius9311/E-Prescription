package eprescription.tcc.ufam.com.e_prescription.Model;

import java.util.List;

public class Prescription {
    public List<PrescriptionItem> prescriptionItems;

    public Prescription() {
    }

    public Prescription(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
    }

    public List<PrescriptionItem> getPrescriptionItems() {
        return prescriptionItems;
    }

    public void setPrescriptionItems(List<PrescriptionItem> prescriptionItems) {
        this.prescriptionItems = prescriptionItems;
    }
}
