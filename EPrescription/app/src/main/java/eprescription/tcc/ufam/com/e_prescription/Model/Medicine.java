package eprescription.tcc.ufam.com.e_prescription.Model;

public class Medicine {

    private String medicament;

    public Medicine() {
    }

    public Medicine(String medicament) {
        this.medicament = medicament;
    }

    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }
}
