package com.aioute.dao.impl;

import com.aioute.dao.PermissionDao;
import com.aioute.model.Permission;
import com.sft.db.SqlConnectionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        sb.append("select p.*,s.address,s.is_redict from app_permission p,sub_server s where p.type = ?");
        sb.append(" and p.server_id = s.id");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, type);
            rs = ps.executeQuery();
            while (rs.next()) {
                permission.setUrl(rs.getString("url"));
                permission.setType(rs.getString("type"));
                permission.setAddress(rs.getString("address"));
                permission.setIs_user(rs.getInt("is_user") == 0 ? false : true);
                permission.setRedict(rs.getInt("is_redict") == 0 ? false : true);
                return permission;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return permission;
    }

    public List<Permission> getAllAppPermission() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Permission> permissionList = new ArrayList<Permission>();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from app_permission where is_user = 1");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                Permission permission = new Permission();
                permission.setUrl(rs.getString("url"));
                permission.setType(rs.getString("type"));
                permissionList.add(permission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return permissionList;
    }
}
