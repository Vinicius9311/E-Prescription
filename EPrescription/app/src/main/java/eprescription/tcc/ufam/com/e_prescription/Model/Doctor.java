package eprescription.tcc.ufam.com.e_prescription.Model;

public class Doctor {
    private String firstName;
    private String lastName;
    private String specialty;
    private String crm;
    private String email;
    private String dateOfBirth;
    private String maritalStatus; // estado civil
    private String sex;
    private String bloodType;
    private String dateModified; // user modification date
    private String dateCreated;
    private String password;

    public Doctor(String firstName, String lastName, String specialty, String crm,
                  String email, String dateOfBirth, String maritalStatus, String sex,
                  String bloodType, String dateModified, String dateCreated, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.crm = crm;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.maritalStatus = maritalStatus;
        this.sex = sex;
        this.bloodType = bloodType;
        this.dateModified = dateModified;
        this.dateCreated = dateCreated;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
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

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
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
}
