package com.aioute.model.bean;

import com.aioute.model.PlatUserModel;

public class PlatUserBean extends PlatUserModel {

    private String distance;
    private String price;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
