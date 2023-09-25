package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

@SuppressLint("ParcelCreator")
public class CartItemModel implements Parcelable {
    private String productId;
    private String productName;
    private int productRate;
    private int productMrp;

    public CartItemModel(String productId, String productName, int productRate, int productMrp, String imageUrl, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productRate = productRate;
        this.productMrp = productMrp;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public int getProductMrp() {
        return productMrp;
    }

    private String imageUrl;

    private int quantity;

    public void setProductMrp(int productMrp) {
        this.productMrp = productMrp;
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

    public int getProductRate() {
        return productRate;
    }

    public void setProductRate(int productRate) {
        this.productRate = productRate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
