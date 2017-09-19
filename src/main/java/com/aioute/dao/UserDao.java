package com.aioute.dao;

import com.aioute.model.UserModel;

public interface UserDao {

    public UserModel getUserInfoById(String userId);

    public UserModel getUserInfoByPhone(String phone);

    public boolean updateUser(UserModel userModel);

    public boolean addUser(UserModel userModel);

    public UserModel getUserInfoByLoginId(String login_id);
}
