package eprescription.tcc.ufam.com.e_prescription.Model;

public class UsersRole {

    private String email;
    private boolean isDoctor;

    public UsersRole() {}

    public UsersRole(String email, boolean isDoctor) {
        this.email = email;
        this.isDoctor = isDoctor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }
}
