package com.aioute.service.impl;

import com.aioute.dao.UserDao;
import com.aioute.model.AppUserModel;
import com.aioute.service.AppUserService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AppUserServiceImpl implements AppUserService {

    @Resource
    private UserDao userDao;

    public AppUserModel getUserInfoByPhone(String phone) {
        return userDao.getUserInfoByPhone(phone);
    }

    public boolean updateUser(AppUserModel userModel) {
        return userDao.updateUser(userModel);
    }

    public AppUserModel getUserInfoByLoginId(String login_id) {
        return userDao.getUserInfoByLoginId(login_id);
    }

    public boolean addUser(AppUserModel userModel) {
        return userDao.addUser(userModel);
    }
}
