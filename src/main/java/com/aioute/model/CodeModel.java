package com.aioute.model;

public class CodeModel {

    private String phone;
    private String code;
    private String create_date;
    private int duration;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        if (create_date != null) {
            create_date.replace(".0", "");
        }
        this.create_date = create_date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
