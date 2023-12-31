package com.example.flipcartClone;

public class OrderModel {
    private String phoneNumber;

    public OrderModel(String phoneNumber, String productId, String productName, String productImage, String orderDate) {
        this.phoneNumber = phoneNumber;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.orderDate = orderDate;
        // Initialize other fields here
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String productId;
    private String productName;
    private String productImage;
    private String orderDate;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }
    // Add more fields for other order details like image, price, etc.

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    // Add getters and setters for other fields
}
