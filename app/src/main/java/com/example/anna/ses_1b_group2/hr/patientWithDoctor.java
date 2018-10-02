package com.example.anna.ses_1b_group2.hr;
import java.io.Serializable;

public class patientWithDoctor implements Serializable{

    heartRateData hrd;
    String doctorName;
    public patientWithDoctor()
    {}
    public patientWithDoctor(heartRateData hrd, String doctorName) {
        this.hrd = hrd;
        this.doctorName = doctorName;
    }

    public heartRateData getHrd() {
        return hrd;
    }

    public String getDoctorName() {
        return doctorName;
    }
}