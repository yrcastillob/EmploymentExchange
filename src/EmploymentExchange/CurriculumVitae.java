package EmploymentExchange;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CurriculumsVitae")
public class CurriculumVitae {

    //ATTRIBUTES
    @DatabaseField(id = true)
    private String nationalID;
    @DatabaseField
    private String fullName;
    @DatabaseField
    private int age;
    @DatabaseField
    private double workExperience;
    @DatabaseField
    private String profession;
    @DatabaseField
    private String phoneNumber;

    // CONSTRUCTORS

    public CurriculumVitae() {};

    public CurriculumVitae(String nationalID, String fullName, int age, double workExperience, String profession, String phoneNumber) {
        this.nationalID = nationalID;
        this.fullName = fullName;
        this.age = age;
        this.workExperience = workExperience;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
    }

    // GETTERS AND SETTERS
    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(double workExperience) {
        this.workExperience = workExperience;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
