package com.mahmoudbashir.pharmacy_app.models;

public class pharmacy_data {

    String ph_name;
    String ph_email;
    String ph_phone;
    String ph_location;
    String ph_pass;
    String image_uri;
    String ph_distance;
    String deviceToken;

    public pharmacy_data(final String ph_name, final String ph_email,final String ph_phone, final String ph_location, final String ph_pass, final String image_uri,final String ph_distance,final String deviceToken) {
        this.ph_name = ph_name;
        this.ph_email = ph_email;
        this.ph_phone = ph_phone;
        this.ph_location = ph_location;
        this.ph_pass = ph_pass;
        this.image_uri = image_uri;
        this.ph_distance = ph_distance;
        this.deviceToken = deviceToken;
    }

    public String getPh_name() {
        return this.ph_name;
    }

    public void setPh_name(final String ph_name) {
        this.ph_name = ph_name;
    }

    public String getPh_phone() {
        return this.ph_phone;
    }

    public void setPh_phone(final String ph_phone) {
        this.ph_phone = ph_phone;
    }

    public String getPh_location() {
        return this.ph_location;
    }

    public void setPh_location(final String ph_location) {
        this.ph_location = ph_location;
    }

    public String getPh_pass() {
        return this.ph_pass;
    }

    public void setPh_pass(final String ph_pass) {
        this.ph_pass = ph_pass;
    }

    public String getImage_uri() {
        return this.image_uri;
    }

    public void setImage_uri(final String image_uri) {
        this.image_uri = image_uri;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public void setDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPh_email() {
        return this.ph_email;
    }

    public void setPh_email(final String ph_email) {
        this.ph_email = ph_email;
    }

    public String getPh_distance() {
        return this.ph_distance;
    }

    public void setPh_distance(final String ph_distance) {
        this.ph_distance = ph_distance;
    }
}
