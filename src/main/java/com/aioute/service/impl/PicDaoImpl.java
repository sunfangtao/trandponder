package com.aioute.service.impl;

import com.aioute.dao.PicDao;
import com.aioute.db.SqlConnectionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PicDaoImpl implements PicDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public boolean addPic(String uid, List<String> picList, List<String> idList) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("insert into picture (id,url,uid,del_flag) values ");
        int length = picList.size();
        for (int i = 0; i < length; i++) {
            sb.append("(").append(idList.get(i)).append(",").append(picList.get(i)).append(",");
            sb.append(uid).append(",0)");
            if (i < length - 1) {
                sb.append(",");
            }
        }

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, null);
        }
        return false;
    }

    public List<String> getPics(String uid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<String> picList = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        sb.append("select url from picture where del_flag=0 and uid = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, uid);

            rs = ps.executeQuery();
            while (rs.next()) {
                picList.add(rs.getString("url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return picList;
    }
}
