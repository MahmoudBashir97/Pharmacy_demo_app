package com.mahmoudbashir.pharmacy_app.models;

public class PharmacyListPatient_model {
    String ph_name;
    String ph_phone;
    String ph_location;
    String image_uri;

    public PharmacyListPatient_model(String ph_name, String ph_phone, String ph_location, String image_uri) {
        this.ph_name = ph_name;
        this.ph_phone = ph_phone;
        this.ph_location = ph_location;
        this.image_uri = image_uri;
    }

    public String getPh_name() {
        return ph_name;
    }

    public void setPh_name(String ph_name) {
        this.ph_name = ph_name;
    }

    public String getPh_phone() {
        return ph_phone;
    }

    public void setPh_phone(String ph_phone) {
        this.ph_phone = ph_phone;
    }

    public String getPh_location() {
        return ph_location;
    }

    public void setPh_location(String ph_location) {
        this.ph_location = ph_location;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
