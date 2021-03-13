package com.mahmoudbashir.pharmacy_app.models;

public class delivery_data {

    String del_name;
    String email;
    String del_phone;
    String del_pass;
    String deviceToken;

    public delivery_data(final String del_name, final String email,final String del_phone, final String del_pass,final String deviceToken) {
        this.del_name = del_name;
        this.email=email;
        this.del_phone = del_phone;
        this.del_pass = del_pass;
        this.deviceToken = deviceToken;
    }

    public String getDel_name() {
        return this.del_name;
    }

    public void setDel_name(final String del_name) {
        this.del_name = del_name;
    }

    public String getDel_phone() {
        return this.del_phone;
    }

    public void setDel_phone(final String del_phone) {
        this.del_phone = del_phone;
    }

    public String getDel_pass() {
        return this.del_pass;
    }

    public void setDel_pass(final String del_pass) {
        this.del_pass = del_pass;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public void setDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
