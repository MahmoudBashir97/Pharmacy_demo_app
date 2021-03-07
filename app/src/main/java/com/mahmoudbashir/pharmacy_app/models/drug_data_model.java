package com.mahmoudbashir.pharmacy_app.models;

public class drug_data_model {
    public String image_uri;
    public String drug_name;
    public String drug_price;
    public String drug_tablets;
    public String drug_description;

    public drug_data_model(String image_uri, String drug_name, String drug_price, String drug_tablets, String drug_description) {
        this.image_uri = image_uri;
        this.drug_name = drug_name;
        this.drug_price = drug_price;
        this.drug_tablets = drug_tablets;
        this.drug_description = drug_description;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public void setDrug_price(String drug_price) {
        this.drug_price = drug_price;
    }

    public void setDrug_tablets(String drug_tablets) {
        this.drug_tablets = drug_tablets;
    }

    public void setDrug_description(String drug_description) {
        this.drug_description = drug_description;
    }
}
