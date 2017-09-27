package com.aioute.model;

public class PushModel {
    private String id;
    private String title;        // 标题
    private String content;        // 推送内容
    private String createTime;        // 创建时间
    private String url;        // 跳转地址
    private String pushtype;        // 跳转地址
    private String user_id;        // 用户id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPushtype() {
        return pushtype;
    }

    public void setPushtype(String pushtype) {
        this.pushtype = pushtype;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
