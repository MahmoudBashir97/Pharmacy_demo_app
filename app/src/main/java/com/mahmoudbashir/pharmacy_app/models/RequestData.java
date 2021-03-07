package com.mahmoudbashir.pharmacy_app.models;

public class RequestData {

    String drug_name;
    String drug_img;
    String drug_price;
    String drug_mg;
    String drug_tablets;
    String drug_description;
    String ph_phone;
    String distance;
    String status;
    String RequestId;
    String patientId;

    public RequestData() {
    }

    public String getDrug_name() {
        return this.drug_name;
    }

    public void setDrug_name(final String drug_name) {
        this.drug_name = drug_name;
    }

    public String getDrug_img() {
        return this.drug_img;
    }

    public void setDrug_img(final String drug_img) {
        this.drug_img = drug_img;
    }

    public String getDrug_price() {
        return this.drug_price;
    }

    public void setDrug_price(final String drug_price) {
        this.drug_price = drug_price;
    }

    public String getDrug_mg() {
        return this.drug_mg;
    }

    public void setDrug_mg(final String drug_mg) {
        this.drug_mg = drug_mg;
    }

    public String getDrug_tablets() {
        return this.drug_tablets;
    }

    public void setDrug_tablets(final String drug_tablets) {
        this.drug_tablets = drug_tablets;
    }

    public String getDrug_description() {
        return this.drug_description;
    }

    public void setDrug_description(final String drug_description) {
        this.drug_description = drug_description;
    }

    public String getPh_phone() {
        return this.ph_phone;
    }

    public void setPh_phone(final String ph_phone) {
        this.ph_phone = ph_phone;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(final String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getRequestId() {
        return this.RequestId;
    }

    public void setRequestId(final String requestId) {
        this.RequestId = requestId;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public void setPatientId(final String patientId) {
        this.patientId = patientId;
    }
}
