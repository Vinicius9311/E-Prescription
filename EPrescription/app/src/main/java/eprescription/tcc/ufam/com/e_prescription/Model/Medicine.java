package eprescription.tcc.ufam.com.e_prescription.Model;

public class Medicine {

    private String reference;
    private String activePrinciple;
    private String interchangeable;
    private String concentration;
    private String pharmaceuticForm;

    public Medicine() {
    }

    public Medicine(String reference, String activePrinciple, String interchangeable, String concentration, String pharmaceuticForm) {
        this.reference = reference;
        this.activePrinciple = activePrinciple;
        this.interchangeable = interchangeable;
        this.concentration = concentration;
        this.pharmaceuticForm = pharmaceuticForm;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getActivePrinciple() {
        return activePrinciple;
    }

    public void setActivePrinciple(String activePrinciple) {
        this.activePrinciple = activePrinciple;
    }

    public String getInterchangeable() {
        return interchangeable;
    }

    public void setInterchangeable(String interchangeable) {
        this.interchangeable = interchangeable;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getPharmaceuticForm() {
        return pharmaceuticForm;
    }

    public void setPharmaceuticForm(String pharmaceuticForm) {
        this.pharmaceuticForm = pharmaceuticForm;
    }
}
