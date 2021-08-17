package com.example.trackmypantry.DataType;

public class CreateProductSchema {
    String token;
    String name;
    String description;
    String barcode;
    boolean test;

    public CreateProductSchema(String token, String name, String description, String barcode, boolean test) {
        this.token = token;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.test = test;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBarcode() {
        return barcode;
    }

    public boolean getTest(){
        return test;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
