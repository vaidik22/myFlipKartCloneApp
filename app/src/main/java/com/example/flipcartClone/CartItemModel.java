package com.example.flipcartClone;

public class CartItemModel {
    private String productId;
    private String productName;
    private String productRate;
    private String imageUrl;
    private int quantity;

    public CartItemModel(String productId, String productName, String productRate, String imageUrl, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productRate = productRate;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductRate() {
        return productRate;
    }

    public void setProductRate(String productRate) {
        this.productRate = productRate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
