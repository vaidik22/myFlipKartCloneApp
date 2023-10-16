package com.example.flipcartClone;

public class WishListItem {
    private int productId;
    private String productName;
    private double productRate;
    private double productMrp;
    private String description;
    private String imageUrl;
    private int quantity;

    public WishListItem(int productId, String productName, double productRate, double productMrp, String imageUrl, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productRate = productRate;
        this.productMrp = productMrp;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductRate() {
        return productRate;
    }

    public void setProductRate(double productRate) {
        this.productRate = productRate;
    }

    public double getProductMrp() {
        return productMrp;
    }

    public void setProductMrp(double productMrp) {
        this.productMrp = productMrp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
