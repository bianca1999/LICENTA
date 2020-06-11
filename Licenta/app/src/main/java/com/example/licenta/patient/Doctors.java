package com.example.licenta.patient;

public class Doctors {
    private String firstName;
    private String lastName;
    private String specializare;

    public Doctors(String firstName, String lastName, String specializare) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specializare = specializare;
    }

    public Doctors(){

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecializare() {
        return specializare;
    }
}
