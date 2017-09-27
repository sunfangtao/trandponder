package com.aioute.service;

import com.aioute.model.AppUserModel;

public interface AppUserService {

    public AppUserModel getUserInfoById(String userId);

    public AppUserModel getUserInfoByPhone(String phone);

    public boolean updateUser(AppUserModel userModel, boolean isFromApp);

    public boolean addUser(AppUserModel userModel);

    public AppUserModel getUserInfoByLoginId(String login_id);
}
