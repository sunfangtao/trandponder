package com.aioute.dao;

import com.aioute.model.AppUserModel;

public interface UserDao {

    public AppUserModel getUserInfoByPhone(String phone);

    public boolean updateUser(AppUserModel userModel);

    public AppUserModel getUserInfoByLoginId(String login_id);

    public boolean addUser(AppUserModel userModel);
}
