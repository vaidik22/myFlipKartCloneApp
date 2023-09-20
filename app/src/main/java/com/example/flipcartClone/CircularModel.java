package com.example.flipcartClone;

public class CircularModel {
    String title;
    int icon;
    int position;

    public CircularModel(String title, int icon, int position) {
        this.title = title;
        this.icon = icon;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
