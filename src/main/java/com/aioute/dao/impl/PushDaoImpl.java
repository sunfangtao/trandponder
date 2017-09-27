package com.aioute.dao.impl;

import com.aioute.dao.PushDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.PushModel;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PushDaoImpl implements PushDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public List<PushModel> getPushList(String userId, String pushType, int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select * from user_push where user_id=?");
        if (StringUtils.hasText(pushType)) {
            sb.append(" and pushType = ?");
        }
        sb.append(" limit ").append((page - 1) * pageSize).append(",").append(pageSize);

        List<PushModel> pushList = new ArrayList<PushModel>();
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);
            if (StringUtils.hasText(pushType)) {
                ps.setString(2, pushType);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                PushModel pushModel = new PushModel();
                pushModel.setId(rs.getString("id"));
                pushModel.setContent(rs.getString("content"));
                pushModel.setTitle(rs.getString("title"));
                pushModel.setCreateTime(rs.getString("createDate"));
                pushModel.setUrl(rs.getString("url"));
                pushModel.setPushtype(rs.getString("pushtype"));
                pushList.add(pushModel);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return pushList;
    }
}
