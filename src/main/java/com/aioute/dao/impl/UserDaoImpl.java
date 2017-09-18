package com.aioute.dao.impl;

import com.aioute.dao.UserDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.UserModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    private UserModel getUserBySql(String sql, String key) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        UserModel userModel = null;

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, key);

            rs = ps.executeQuery();
            while (rs.next()) {
                userModel = new UserModel();
                userModel.setPassword(rs.getString("password"));
                userModel.setId(rs.getString("id"));
                userModel.setLogin_name(rs.getString("login_name"));
                userModel.setLogin_id(rs.getString("login_id"));
                userModel.setLogin_name(rs.getString("login_name"));
                userModel.setEmail(rs.getString("email"));
                userModel.setPush_id(rs.getString("push_id"));
                userModel.setSex(rs.getString("sex"));
                userModel.setName(rs.getString("name"));
                userModel.setPhoto(rs.getString("photo"));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return userModel;
    }

    public UserModel getUserInfoById(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from app_user where id=?");
        return getUserBySql(sb.toString(), userId);
    }

    public UserModel getUserInfoByPhone(String phone) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from app_user where login_name=?");
        return getUserBySql(sb.toString(), phone);
    }

    public boolean updateUser(UserModel userModel) {
        Connection con = null;
        PreparedStatement ps = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update app_user set password=?,email=?,sex=?,name=?,push_id=?,login_time=?,login_id=?,update_time=? where id=?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userModel.getPassword());
            ps.setString(2, userModel.getEmail());
            ps.setString(3, userModel.getSex());
            ps.setString(4, userModel.getName());
            ps.setString(5, userModel.getPush_id());
            ps.setString(6, userModel.getLogin_time());
            ps.setString(7, userModel.getLogin_id());
            ps.setString(8, userModel.getUpdate_time());
            ps.setString(9, userModel.getId());
            int result = ps.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, null);
        }
        return false;
    }

    public boolean addUser(UserModel userModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into app_user (id,login_name,password,login_type,login_id,create_time) values (?,?,?,?,?,?)");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userModel.getId());
            ps.setString(2, userModel.getLogin_name());
            ps.setString(3, userModel.getPassword());
            ps.setString(4, userModel.getLogin_type());
            ps.setString(5, userModel.getLogin_id());
            ps.setString(6, userModel.getCreate_time());
            int result = ps.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }
}
