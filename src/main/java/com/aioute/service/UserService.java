package com.aioute.service;

import com.aioute.model.UserModel;

public interface UserService {

    public UserModel getUserInfoById(String userId);

    public UserModel getUserInfoByPhone(String phone);

    public boolean updateUser(UserModel userModel);

    public boolean addUser(UserModel userModel);
}
