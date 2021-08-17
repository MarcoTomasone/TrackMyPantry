package com.example.trackmypantry.DataType;

public class CreateVoteSchema {
    String token;
    int rating;
    String productId;

    public CreateVoteSchema(String token, int rating, String productId) {
        this.token = token;
        this.rating = rating;
        this.productId = productId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
