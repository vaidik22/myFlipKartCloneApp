package com.example.flipcartClone;

public class SliderDataFrag {
    // image url is used to
    // store the url of image
    private String imgUrlfrag;

    // Constructor method.
    public SliderDataFrag(String imgUrlfrag) {
        this.imgUrlfrag = imgUrlfrag;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrlfrag;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrlfrag = imgUrl;
    }
}
