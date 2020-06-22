package com.example.licenta.Model;

public class Appointment {
    String patient;
    String date;

    public Appointment() {
    }

    public Appointment(String patient, String date) {
        this.patient = patient;
        this.date = date;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
