package com.aioute.model;

public class SellCarModel {

    private String id;                 //
    private String userId;
    private String mobile;            // 手机号
    private String mileage;           // 行驶里程（万公里）
    private double priceTax;         // 新车含税价
    private double sellPrice;        // 期望售价（万元）
    private String brand;             // 品牌
    private String carLicense;       // 上牌时间
    private String displacement;     // 排量
    private String location;          // 所在地区
    private String gearbox;           // 变速箱
    private String dischargeStandard;// 排放标准
    private String userType;          // 发布者类型（0：个人；1：组织）
    private String photo;             //  展示图
    private String remark;            //  备注说明
    private String createdate;        //  创建时间
    private String maxprice;          //  价格上限
    private String minprice;          //  价格下限
    private String frontImg;          // '前面图片',
    private String behindImg;         // '后面图片',
    private String sideImg;           // '侧面图片',

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public double getPriceTax() {
        return priceTax;
    }

    public void setPriceTax(double priceTax) {
        this.priceTax = priceTax;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getDischargeStandard() {
        return dischargeStandard;
    }

    public void setDischargeStandard(String dischargeStandard) {
        this.dischargeStandard = dischargeStandard;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public String getMinprice() {
        return minprice;
    }

    public void setMinprice(String minprice) {
        this.minprice = minprice;
    }

    public String getFrontImg() {
        return frontImg;
    }

    public void setFrontImg(String frontImg) {
        this.frontImg = frontImg;
    }

    public String getBehindImg() {
        return behindImg;
    }

    public void setBehindImg(String behindImg) {
        this.behindImg = behindImg;
    }

    public String getSideImg() {
        return sideImg;
    }

    public void setSideImg(String sideImg) {
        this.sideImg = sideImg;
    }
}
