package com.aioute.dao.impl;

import com.aioute.dao.UserDao;
import com.aioute.model.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    public UserModel getUserInfoById(String userId) {
        return null;
    }

    public UserModel getUserInfoByPhone(String phone) {
        return null;
    }

    public boolean updateUser(UserModel userModel) {
        return false;
    }

    public boolean addUser(UserModel userModel) {
        return false;
    }
}
