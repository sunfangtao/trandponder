package com.aioute.model;

import com.sft.util.DateUtil;

public class AppUserModel {

    private String id; // '编号'
    private String login_name;// '登录名称（手机号）'
    private String password;// '密码'
    private String login_type;// '第三方登录类型'
    private String login_id;// '第三方登录id'
    private String photo;// '头像'
    private String create_time;// '创建时间'
    private String name;// '昵称（显示用）'
    private String push_id;// '推送编号'
    private String sex;// '性别（0：男；1：女）'
    private String email;// '邮箱'
    private int del_flag;// 删除标志
    private String login_time;// 登录时间
    private String update_time;
    private String hand_reverse;// 身份证背面
    private String hand_front;// 身份证正面
    private int verify_status;// 认证状态
    private String driveLicence; // 驾驶证*
    private String driveLicenceReason;//
    private String driveLicenceNotice;//
    private int licenceStatus;
    private String frontReason;// 原因
    private String frontNotice;// 注意
    private String pass_val;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = DateUtil.getDeletePointDate(create_time);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(int del_flag) {
        this.del_flag = del_flag;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = DateUtil.getDeletePointDate(login_time);
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getHand_reverse() {
        return hand_reverse;
    }

    public void setHand_reverse(String hand_reverse) {
        this.hand_reverse = hand_reverse;
    }

    public String getHand_front() {
        return hand_front;
    }

    public void setHand_front(String hand_front) {
        this.hand_front = hand_front;
    }

    public int getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(int verify_status) {
        this.verify_status = verify_status;
    }

    public String getDriveLicence() {
        return driveLicence;
    }

    public void setDriveLicence(String driveLicence) {
        this.driveLicence = driveLicence;
    }

    public String getDriveLicenceReason() {
        return driveLicenceReason;
    }

    public void setDriveLicenceReason(String driveLicenceReason) {
        this.driveLicenceReason = driveLicenceReason;
    }

    public String getDriveLicenceNotice() {
        return driveLicenceNotice;
    }

    public void setDriveLicenceNotice(String driveLicenceNotice) {
        this.driveLicenceNotice = driveLicenceNotice;
    }

    public int getLicenceStatus() {
        return licenceStatus;
    }

    public void setLicenceStatus(int licenceStatus) {
        this.licenceStatus = licenceStatus;
    }

    public String getFrontReason() {
        return frontReason;
    }

    public void setFrontReason(String frontReason) {
        this.frontReason = frontReason;
    }

    public String getFrontNotice() {
        return frontNotice;
    }

    public void setFrontNotice(String frontNotice) {
        this.frontNotice = frontNotice;
    }

    public String getPass_val() {
        return pass_val;
    }

    public void setPass_val(String pass_val) {
        this.pass_val = pass_val;
    }
}
