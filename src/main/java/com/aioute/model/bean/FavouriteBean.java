package com.aioute.model.bean;

import com.aioute.model.FavouriteModel;

public class FavouriteBean extends FavouriteModel {
    private String photo;
    private String title;
    private float price;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
