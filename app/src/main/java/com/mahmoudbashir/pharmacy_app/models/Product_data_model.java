package com.mahmoudbashir.pharmacy_app.models;

public class Product_data_model {

    String drug_description;
    String drug_name;
    String drug_price;
    String drug_tablets;
    String image_uri;

    public Product_data_model(String drug_description, String drug_name, String drug_price, String drug_tablets, String image_uri) {
        this.drug_description = drug_description;
        this.drug_name = drug_name;
        this.drug_price = drug_price;
        this.drug_tablets = drug_tablets;
        this.image_uri = image_uri;
    }

    public String getDrug_description() {
        return drug_description;
    }

    public void setDrug_description(String drug_description) {
        this.drug_description = drug_description;
    }

    public String getDrug_name() {
        return drug_name;
    }

    public void setDrug_name(String drug_name) {
        this.drug_name = drug_name;
    }

    public String getDrug_price() {
        return drug_price;
    }

    public void setDrug_price(String drug_price) {
        this.drug_price = drug_price;
    }

    public String getDrug_tablets() {
        return drug_tablets;
    }

    public void setDrug_tablets(String drug_tablets) {
        this.drug_tablets = drug_tablets;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }
}
