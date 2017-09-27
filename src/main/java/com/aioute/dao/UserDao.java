package com.aioute.dao;

import com.aioute.model.AppUserModel;

public interface UserDao {

    public AppUserModel getUserInfoById(String userId);

    public AppUserModel getUserInfoByPhone(String phone);

    public boolean updateUser(AppUserModel userModel);

    public boolean addUser(AppUserModel userModel);

    public AppUserModel getUserInfoByLoginId(String login_id);
}
