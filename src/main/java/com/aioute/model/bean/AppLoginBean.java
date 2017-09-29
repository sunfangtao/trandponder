package com.aioute.model.bean;

public class AppLoginBean {

    private String _eventId = "submit";
    private String lt;
    private String execution;
    private String username;
    private String password;
    private String server;

    @Override
    public String toString() {
        return "AppLoginBean{" +
                "_eventId='" + _eventId + '\'' +
                ", lt='" + lt + '\'' +
                ", execution='" + execution + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", server='" + server + '\'' +
                '}';
    }

    public String get_eventId() {
        return _eventId;
    }

    public void set_eventId(String _eventId) {
        this._eventId = _eventId;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
