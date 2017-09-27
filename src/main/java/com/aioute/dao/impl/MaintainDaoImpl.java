package com.aioute.dao.impl;

import com.aioute.dao.MaintainDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.MaintainModel;
import com.aioute.model.SubMaintainModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MaintainDaoImpl implements MaintainDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public List<MaintainModel> getAll() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<MaintainModel> maintainList = new ArrayList<MaintainModel>();
        StringBuffer sb = new StringBuffer();
        sb.append("select id,value from common_params where parent_id = 0 and type = 'maintain' order by id");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                MaintainModel maintainModel = new MaintainModel();
                maintainModel.setId(rs.getString("id"));
                maintainModel.setName(rs.getString("value"));
                maintainList.add(maintainModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return maintainList;
    }

    public List<MaintainModel> getMaintain(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<MaintainModel> maintainList = new ArrayList<MaintainModel>();
        StringBuffer sb = new StringBuffer();
        sb.append("select p.id,p.value from common_params p,sub_maintain s where p.parent_id = 0 and p.type = 'maintain'");
        sb.append(" and s.user_id = ? and s.pid = p.id group by p.id order by p.id");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                MaintainModel maintainModel = new MaintainModel();
                maintainModel.setId(rs.getString("id"));
                maintainModel.setName(rs.getString("value"));
                maintainList.add(maintainModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return maintainList;
    }

    public List<SubMaintainModel> getSubMaintain(String userId, String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<SubMaintainModel> maintainList = new ArrayList<SubMaintainModel>();
        StringBuffer sb = new StringBuffer();
        sb.append("select id,title,price,pid from sub_maintain where");
        sb.append(" user_id = ? and pid = ? order by id");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);
            ps.setString(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                SubMaintainModel maintainModel = new SubMaintainModel();
                maintainModel.setDescription("");
                maintainModel.setName(rs.getString("title"));
                maintainModel.setPid(rs.getString("pid"));
                maintainModel.setPrice(rs.getString("price"));
                maintainList.add(maintainModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return maintainList;
    }
}
