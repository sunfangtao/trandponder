package com.aioute.service.impl;

import com.aioute.dao.UserDao;
import com.aioute.model.AppUserModel;
import com.aioute.service.AppUserService;
import com.sft.util.DateUtil;
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

    public boolean updateUser(AppUserModel userModel, boolean isUpdateStatus) {
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
        if (userModel.getPhoto() != null) {
            existUser.setPhoto(userModel.getPhoto());
        }
        if (userModel.getHand_front() != null) {
            existUser.setHand_front(userModel.getHand_front());
        }
        if (userModel.getHand_reverse() != null) {
            existUser.setHand_reverse(userModel.getHand_reverse());
        }
        if (isUpdateStatus && userModel.getVerify_status() > 0) {
            existUser.setVerify_status(userModel.getVerify_status());
        }
        if (isUpdateStatus && userModel.getLicenceStatus() > 0) {
            existUser.setLicenceStatus(userModel.getLicenceStatus());
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
