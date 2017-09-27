package com.aioute.model;

public class NewCarModel {
    private String id;
    private String brand;        // 品牌
    private String user_id;        //
    private String oriPrice;        // 原价
    private String price;        // 售价
    private String param;        // 配置参数的图片地址
    private String vehiclePic;        // 车辆首图
    private String discounts;        // 折扣金额
    private String title;        // 标题
    private String hotFlag;        // 是否热销（热门活动中显示）
    private String manufacturer;// 厂商

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getVehiclePic() {
        return vehiclePic;
    }

    public void setVehiclePic(String vehiclePic) {
        this.vehiclePic = vehiclePic;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHotFlag() {
        return hotFlag;
    }

    public void setHotFlag(String hotFlag) {
        this.hotFlag = hotFlag;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
