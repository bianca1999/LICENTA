package com.example.licenta.model;

public class Doctor {
    private String firstName;
    private String lastName;
    private String specializare;

    public Doctor(String firstName, String lastName, String specializare) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specializare = specializare;
    }

    public Doctor(){

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
