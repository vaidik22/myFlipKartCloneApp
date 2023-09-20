package com.example.flipcartClone;

public class ProductModel {
    String title;
    String description;
    String textview2_price;
    String imageUrl;
    String MRP;
    int productId;
    double productRate;
    double productMrp;
    int icon;
    int position;

    public ProductModel(int productId, String title, String description, double productRate, double productMrp, String imageUrl) {
        this.title = title;
        this.productRate = productRate;
        this.productMrp = productMrp;
        this.productId = productId;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public ProductModel(String title, String description, String textview2_price, int icon, String mrp) {
        this.title = title;
        this.icon = icon;
        this.MRP = mrp;
        this.description = description;
        this.textview2_price = textview2_price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextview2_price() {
        return textview2_price;
    }

    public void setTextview2_price(String textview2_price) {
        this.textview2_price = textview2_price;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
