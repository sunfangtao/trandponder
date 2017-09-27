package com.aioute.dao.impl;

import com.aioute.dao.ActivityDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.ActivityModel;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActivityDaoImpl implements ActivityDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public List<ActivityModel> queryList(int page, int pageSize) {

        StringBuffer sb = new StringBuffer();
        sb.append("select * from advertisement where del_type = 0 order by create_time desc");
        if (page > 0 && pageSize > 0) {
            sb.append(" limit ").append((page - 1) * pageSize).append(",").append(pageSize);
        }

        return queryList(sb.toString(), null);
    }

    public List<ActivityModel> queryUserActivityList(String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from advertisement where del_type = 0 and user_id = ? order by create_time desc");
        return queryList(sb.toString(), userId);
    }

    private List<ActivityModel> queryList(String sql, String key) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ActivityModel> activityModelLits = new ArrayList<ActivityModel>();
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            if (StringUtils.hasText(key))
                ps.setString(1, key);

            rs = ps.executeQuery();
            while (rs.next()) {
                ActivityModel activityModel = new ActivityModel();
                activityModel.setPicUrl(rs.getString("pic_url"));
                activityModel.setMessage(rs.getString("message"));
                activityModel.setCreateTime(rs.getString("create_time"));
                activityModel.setId(rs.getString("id"));
                activityModel.setUrl(rs.getString("url"));
                activityModelLits.add(activityModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return activityModelLits;
    }
}
