package com.example.flipcartClone;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubCategoryModel extends RecyclerView.Adapter {
    String title;
    String ProductId;
    int stocks;
    float rate;
    String description;
    float mrp;
    String imageUrl;
    String quantity;

    public SubCategoryModel(String productId, String title, String description, float rate, float mrp, String imageUrl, String quantity) {
        this.ProductId = productId;
        this.title = title;
        this.description = description;
        this.rate = rate;
        this.mrp = mrp;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public int getProductId() {
        return Integer.parseInt(ProductId);
    }

    public void setProductId(String productId) {
        this.ProductId = productId;
    }

    public float getMrp() {
        return mrp;
    }

    public void setMrp(float mrp) {
        this.mrp = mrp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
