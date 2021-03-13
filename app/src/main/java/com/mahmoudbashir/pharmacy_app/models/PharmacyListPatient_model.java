package com.mahmoudbashir.pharmacy_app.models;

public class PharmacyListPatient_model implements Comparable<PharmacyListPatient_model> {
    String ph_name;
    String ph_phone;
    String ph_location;
    String image_uri;
    int ph_distance;

    public PharmacyListPatient_model(String ph_name, String ph_phone, String ph_location, String image_uri,int ph_distance) {
        this.ph_name = ph_name;
        this.ph_phone = ph_phone;
        this.ph_location = ph_location;
        this.image_uri = image_uri;
        this.ph_distance=ph_distance;
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

    public int getPh_distance() {
        return this.ph_distance;
    }

    public void setPh_distance(final int ph_distance) {
        this.ph_distance = ph_distance;
    }


    @Override
    public int compareTo(PharmacyListPatient_model comparestu) {
        int compareage = (int) ((PharmacyListPatient_model) comparestu).getPh_distance();
        return  (this.getPh_distance() - compareage);
    }

}
