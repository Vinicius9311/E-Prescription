package eprescription.tcc.ufam.com.e_prescription.Model;

public class Patient {
    private String fullName;
    private String email;
    private String dateOfBirth;
    private String maritalStatus; // estado civil
    private String sex;
    private String sus;
    private String dateModified; // user modification date
    private String dateCreated;
    private String password;
    private String user;

    public Patient() {
    }

    public Patient(String user) {
        this.user = user;
    }

    public Patient(String firstName, String email, String dateOfBirth,
                   String maritalStatus, String sex, String sus, String dateModified,
                   String dateCreated, String password, String user) {
        this.fullName = firstName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.maritalStatus = maritalStatus;
        this.sex = sex;
        this.sus = sus;
        this.dateModified = dateModified;
        this.dateCreated = dateCreated;
        this.password = password;
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSus() {
        return sus;
    }

    public void setSus(String sus) {
        this.sus = sus;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUserID(String user) {
        this.user = user;
    }
}
