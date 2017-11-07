package com.aioute.dao.impl;

import com.aioute.dao.UserDao;
import com.aioute.model.AppUserModel;
import com.sft.db.SqlConnectionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UserDaoImpl implements UserDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    private AppUserModel getUserBySql(String sql, String key) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        AppUserModel userModel = null;

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, key);

            rs = ps.executeQuery();
            while (rs.next()) {
                userModel = new AppUserModel();
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
                userModel.setHand_front(rs.getString("hand_front"));
                userModel.setHand_reverse(rs.getString("hand_reverse"));
                userModel.setFrontNotice(rs.getString("frontNotice"));
                userModel.setFrontReason(rs.getString("frontReason"));
                userModel.setVerify_status(rs.getInt("verify_status"));
                userModel.setDriveLicence(rs.getString("driveLicence"));
                userModel.setLicenceStatus(rs.getInt("licenceStatus"));
                userModel.setDriveLicenceNotice(rs.getString("driveLicenceNotice"));
                userModel.setDriveLicenceReason(rs.getString("driveLicenceReason"));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return userModel;
    }

    public AppUserModel getUserInfoById(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from app_user where id=?");
        return getUserBySql(sb.toString(), userId);
    }

    public AppUserModel getUserInfoByPhone(String phone) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from app_user where login_name=?");
        return getUserBySql(sb.toString(), phone);
    }

    public boolean updateUser(AppUserModel userModel) {
        Connection con = null;
        PreparedStatement ps = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update app_user set login_time=?,login_id = '" + userModel.getLogin_id() + "'where id=?");
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userModel.getLogin_time());
            ps.setString(2, userModel.getId());
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

    public AppUserModel getUserInfoByLoginId(String login_id) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from app_user where login_id=?");
        return getUserBySql(sb.toString(), login_id);
    }

    public boolean addUser(AppUserModel userModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into app_user (id,login_name,password,login_type,login_id,create_time) values (?,?,?,?,?,?)");
        try {
            con = this.sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userModel.getId());
            ps.setString(2, userModel.getLogin_name());
            ps.setString(3, userModel.getPassword());
            ps.setString(4, userModel.getLogin_type());
            ps.setString(5, userModel.getLogin_id());
            ps.setString(6, userModel.getCreate_time());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }
}
