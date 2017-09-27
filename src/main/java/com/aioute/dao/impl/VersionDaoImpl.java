package com.aioute.dao.impl;

import com.aioute.dao.VersionDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.VersionModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class VersionDaoImpl implements VersionDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public VersionModel getVersionInfo() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        VersionModel versionModel = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from version where 1=1 order by update_time desc limit 1");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                versionModel = new VersionModel();
                versionModel.setUrl(rs.getString("url"));
                versionModel.setVersion(rs.getString("version"));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return versionModel;
    }
}
