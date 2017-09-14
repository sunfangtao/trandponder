package com.aioute.dao.impl;

import com.aioute.dao.VersionDao;
import com.aioute.model.VersionModel;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class VersionDaoImpl implements VersionDao {

    public VersionModel getVersionInfo(String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        VersionModel versionModel = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from phone_code where phone=?");

//        try {
//            con = sqlConnectionFactory.getConnection();
//            ps = con.prepareStatement(sb.toString());
//            ps.setString(1, phone);
//
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                codeModel = new CodeModel();
//                codeModel.setPhone(rs.getString("phone"));
//                codeModel.setCreate_date(rs.getString("create_date"));
//                codeModel.setCode(rs.getString("code"));
//                codeModel.setDuration(rs.getInt("effective_duration"));
//                break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            sqlConnectionFactory.closeConnetion(con, ps, rs);
//        }
        return versionModel;
    }
}
