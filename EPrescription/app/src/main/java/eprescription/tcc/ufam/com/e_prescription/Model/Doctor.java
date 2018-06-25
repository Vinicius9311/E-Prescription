package eprescription.tcc.ufam.com.e_prescription.Model;

public class Doctor {
    private String firstName;
    private String lastName;
    private String specialty;
    private String crm;
    private String address;
    private String phone;
    private String email;
    private String dateOfBirth;
    private String sex;
    private String dateModified; // user modification date
    private String dateCreated;
    private String password;
    private String user;

    public Doctor(){
    }

    public Doctor(String firstName){
        this.firstName = firstName;
    }


    public Doctor(String firstName, String lastName, String specialty, String crm,
                  String address, String phone, String email, String dateOfBirth, String sex,
                  String dateModified, String dateCreated, String password, String user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.crm = crm;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.dateModified = dateModified;
        this.dateCreated = dateCreated;
        this.password = password;
        this.user = user;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public void setUser(String user) {
        this.user = user;
    }
}
