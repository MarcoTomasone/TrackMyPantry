package com.example.trackmypantry.DataType;

import java.util.List;

public class GetProductSchema {
    private List<Product> productList;
    private String token;

    public List<Product> getProductList() {
        return productList;
    }

    public String getToken() {
        return token;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
