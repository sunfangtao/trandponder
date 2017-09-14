package com.aioute.dao.impl;

import com.aioute.dao.PermissionDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.Permission;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class PermissionDaoImpl implements PermissionDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public Permission getUrlByType(String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Permission permission = new Permission();
        StringBuffer sb = new StringBuffer();
        sb.append("select url from app_permission where type = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, type);
            rs = ps.executeQuery();
            while (rs.next()) {
                permission.setUrl(rs.getString("url"));
                permission.setType(rs.getString("type"));
                permission.setIs_user(rs.getInt("is_user") == 0 ? true : false);
                return permission;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return permission;
    }
}
