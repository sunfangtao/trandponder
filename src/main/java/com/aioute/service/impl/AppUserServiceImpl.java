package com.aioute.service.impl;

import com.aioute.dao.UserDao;
import com.aioute.model.AppUserModel;
import com.aioute.service.AppUserService;
import com.aioute.util.DateUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AppUserServiceImpl implements AppUserService {

    @Resource
    private UserDao userDao;

    public AppUserModel getUserInfoById(String userId) {
        return userDao.getUserInfoById(userId);
    }

    public AppUserModel getUserInfoByPhone(String phone) {
        return userDao.getUserInfoByPhone(phone);
    }

    public boolean updateUser(AppUserModel userModel, boolean isFromApp) {
        AppUserModel existUser = userDao.getUserInfoById(userModel.getId());
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
        if (!isFromApp && userModel.getHand_front() != null) {
            existUser.setHand_front(userModel.getHand_front());
        }
        if (!isFromApp && userModel.getHand_reverse() != null) {
            existUser.setHand_reverse(userModel.getHand_reverse());
        }
        existUser.setUpdate_time(DateUtil.getCurDate());
        return userDao.updateUser(existUser);
    }

    public boolean addUser(AppUserModel userModel) {
        return userDao.addUser(userModel);
    }

    public AppUserModel getUserInfoByLoginId(String login_id) {
        return userDao.getUserInfoByLoginId(login_id);
    }
}
