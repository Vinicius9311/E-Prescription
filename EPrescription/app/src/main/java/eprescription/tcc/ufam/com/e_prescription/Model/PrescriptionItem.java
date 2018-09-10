package eprescription.tcc.ufam.com.e_prescription.Model;

public class PrescriptionItem {
    private String medicament;
    private String via;
    private String duration;
    private String frequence;
    private String observation;

    public PrescriptionItem(String medicament, String via, String duration, String frequence, String observation) {
        this.medicament = medicament;
        this.via = via;
        this.duration = duration;
        this.frequence = frequence;
        this.observation = observation;
    }

    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
