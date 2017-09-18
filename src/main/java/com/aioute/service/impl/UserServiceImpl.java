package com.aioute.service.impl;

import com.aioute.dao.UserDao;
import com.aioute.model.UserModel;
import com.aioute.service.UserService;
import com.aioute.util.DateUtil;
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

    public boolean updateUser(UserModel userModel, boolean isFromApp) {
        UserModel existUser = userDao.getUserInfoById(userModel.getId());
        if (userModel.getPassword() != null) {
            existUser.setPassword(userModel.getPassword());
        }
        if (userModel.getEmail() != null) {
            existUser.setEmail(userModel.getEmail());
        }
        if (userModel.getSex() != null) {
            existUser.setSex(userModel.getSex());
        }
        if (userModel.getName() != null) {
            existUser.setName(userModel.getName());
        }
        if (userModel.getPush_id() != null) {
            existUser.setPush_id(userModel.getPush_id());
        }
        if (userModel.getLogin_time() != null) {
            existUser.setLogin_time(userModel.getLogin_time());
        }
        if (userModel.getLogin_id() != null) {
            existUser.setLogin_id(userModel.getLogin_id());
        }
        if (userModel.getLogin_type() != null) {
            existUser.setLogin_type(userModel.getLogin_type());
        }
        if (!isFromApp && userModel.getPhoto() != null) {
            existUser.setPhoto(userModel.getPhoto());
        }
        existUser.setUpdate_time(DateUtil.getCurDate());
        return userDao.updateUser(userModel);
    }

    public boolean addUser(UserModel userModel) {
        return userDao.addUser(userModel);
    }
}
