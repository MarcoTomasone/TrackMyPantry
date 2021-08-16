package com.example.trackmypantry.DataType;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetProductSchema implements Serializable {
    @SerializedName("products")
    private List<Product> products;

    private String token;

    public GetProductSchema(List<Product> list, String token){
        this.products = new ArrayList<Product>(list);
        this.token = token;
    }
    public List<Product> getProducts() {
        return products;
    }

    public String getToken() {
        return token;
    }

    public void setProducts(List<Product> productList) {
        this.products = productList;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
