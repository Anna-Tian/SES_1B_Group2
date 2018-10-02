package com.example.anna.ses_1b_group2.models;

import com.example.anna.ses_1b_group2.hr.patientWithDoctor;

import java.io.Serializable;

public class newObject implements Serializable {

    patientWithDoctor pd;
    Doctor d;
    String name;

    public newObject(patientWithDoctor pd, Doctor d, String name) {
        this.pd = pd;
        this.d = d;
        this.name = name;
    }

    public newObject() {
    }

    public newObject(patientWithDoctor pd, String name) {
        this.pd = pd;
        this.name = name;
    }

    public Doctor getD() {
        return d;
    }

    public patientWithDoctor getPd() {
        return pd;
    }

    public String getName() {
        return name;
    }
}
