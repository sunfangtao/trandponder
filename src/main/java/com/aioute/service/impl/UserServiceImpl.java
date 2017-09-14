package com.aioute.service.impl;

import com.aioute.dao.UserDao;
import com.aioute.model.UserModel;
import com.aioute.service.UserService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public UserModel getUserInfoById(String userId) {
        return userDao.getUserInfoById(userId);
    }

    public UserModel getUserInfoByPhone(String phone) {
        return userDao.getUserInfoByPhone(phone);
    }

    public boolean updateUser(UserModel userModel) {
        return userDao.updateUser(userModel);
    }

    public boolean addUser(UserModel userModel) {
        return userDao.addUser(userModel);
    }
}
