package com.mahmoudbashir.pharmacy_app.models;

public class patient_data {

    String patient_name;
    String patient_email;
    String patient_phone;
    String patient_pass;
    String deviceToken;

    public patient_data(final String patient_name, final String patient_email, final String patient_phone, final String patient_pass,final String deviceToken) {
        this.patient_name = patient_name;
        this.patient_email = patient_email;
        this.patient_phone = patient_phone;
        this.patient_pass = patient_pass;
        this.deviceToken = deviceToken;
    }

    public String getPatient_name() {
        return this.patient_name;
    }

    public void setPatient_name(final String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_phone() {
        return this.patient_phone;
    }

    public void setPatient_phone(final String patient_phone) {
        this.patient_phone = patient_phone;
    }

    public String getPatient_pass() {
        return this.patient_pass;
    }

    public void setPatient_pass(final String patient_pass) {
        this.patient_pass = patient_pass;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public void setDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPatient_email() {
        return this.patient_email;
    }

    public void setPatient_email(final String patient_email) {
        this.patient_email = patient_email;
    }
}
