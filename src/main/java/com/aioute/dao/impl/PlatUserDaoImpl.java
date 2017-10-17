package com.aioute.dao.impl;

import com.aioute.dao.PlatUserDao;
import com.aioute.model.PlatUserModel;
import com.aioute.model.UserLevelModel;
import com.aioute.model.bean.PlatUserBean;
import com.aioute.util.CloudError;
import com.sft.db.SqlConnectionFactory;
import com.sft.util.SqlUtil;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlatUserDaoImpl implements PlatUserDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;
    @Resource
    private SqlUtil sqlUtil;

    public PlatUserModel getDetail(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        PlatUserModel platUserModel = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from sys_user where id = ? and user_type != '0'");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                platUserModel = new PlatUserModel();
                platUserModel.setPhone(rs.getString("phone"));
                platUserModel.setAddress(rs.getString("address"));
                platUserModel.setLongtitude(rs.getDouble("longtitude"));
                platUserModel.setLatitude(rs.getDouble("latitude"));
                platUserModel.setPhoto(rs.getString("photo"));
                platUserModel.setName(rs.getString("name"));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return platUserModel;
    }

    public List<PlatUserBean> queryList(String sortType, String userLevel, String serverId, double latitude, double longtitude, int page, int pageSize) {

        StringBuffer sb = new StringBuffer();
        sb.append("select u.*");
        if (StringUtils.hasText(serverId)) {
            // 查询服务
            sb.append(",m.price,");
        } else if (StringUtils.hasText(userLevel)) {
            // 新车店面
            sb.append(",c.value as level_name,");
        }
        sb.append("format(" + sqlUtil.getDistanceSql("latitude", "longtitude", latitude, longtitude) + ",2)");
        sb.append(" as distance from sys_user u");
        if (StringUtils.hasText(serverId)) {
            sb.append(",(select user_id,min(price) as price from sub_maintain where pid = '" + serverId + "' group by user_id) as m");
            sb.append(" where u.del_flag = 0");
            sb.append(" and m.user_id = u.id");
        } else if (StringUtils.hasText(userLevel)) {
            sb.append(",common_params c where u.del_flag = 0");
            sb.append(" and u.user_type = '").append(userLevel).append("'");
            sb.append(" and u.user_type = c.id and c.type = 'user_level'");
        } else {
            sb.append(" where u.del_flag = 0");
        }
        sb.append(" and u.user_type != '0'");
        // 排序
        if (StringUtils.hasText(serverId)) {
            if (sortType.equals(CloudError.SortEnum.DISTANCE.getValue())) {
                // 按距离排序
                sb.append(" order by distance");
            } else if (sortType.equals(CloudError.SortEnum.PRICE.getValue())) {
                sb.append(" order by m.price");
            } else if (sortType.equals(CloudError.SortEnum.PRICE_ASC.getValue())) {
                sb.append(" order by m.price asc");
            } else if (sortType.equals(CloudError.SortEnum.PRICE_DESC.getValue())) {
                sb.append(" order by m.price desc");
            }
        }
        if (page > 0 && pageSize > 0) {
            sb.append(" limit ").append((page - 1) * pageSize).append(",").append(pageSize);
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<PlatUserBean> userList = new ArrayList<PlatUserBean>();
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                PlatUserBean platUserModel = new PlatUserBean();
                platUserModel.setId(rs.getString("id"));
                platUserModel.setPhone(rs.getString("phone"));
                platUserModel.setDistance(rs.getString("distance"));
                if (StringUtils.hasText(serverId)) {
                    platUserModel.setPrice(rs.getString("price"));
                }
                platUserModel.setAddress(rs.getString("address"));
                platUserModel.setLongtitude(rs.getDouble("longtitude"));
                platUserModel.setLatitude(rs.getDouble("latitude"));
                platUserModel.setPhoto(rs.getString("photo"));
                platUserModel.setName(rs.getString("name"));
                userList.add(platUserModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return userList;
    }

    public List<UserLevelModel> getLevels() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select * from common_params where type = 'user_level' and parent_id = 0");

        List<UserLevelModel> levelList = new ArrayList<UserLevelModel>();
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                UserLevelModel userLevelModel = new UserLevelModel();
                userLevelModel.setId(rs.getString("id"));
                userLevelModel.setLevelName(rs.getString("value"));
                levelList.add(userLevelModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return levelList;
    }

}
