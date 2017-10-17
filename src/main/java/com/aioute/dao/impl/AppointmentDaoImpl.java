package com.aioute.dao.impl;

import com.aioute.dao.AppointmentDao;
import com.aioute.model.AppointmentModel;
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
public class AppointmentDaoImpl implements AppointmentDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;
    @Resource
    private SqlUtil sqlUtil;

    public boolean addAppointment(AppointmentModel appointmentModel) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into bespeak (id,user_id,adminid,createDate,status,serviceid,type,username" +
                ",phone,appointmenttime,content,updateDate) values ('");
        sb.append(appointmentModel.getId()).append("','");
        sb.append(appointmentModel.getUser_id()).append("','");
        sb.append(appointmentModel.getAdminId()).append("','");
        sb.append(appointmentModel.getCreateDate()).append("','");
        sb.append(appointmentModel.getStatus()).append("','");
        sb.append(appointmentModel.getServiceId()).append("','");
        sb.append(appointmentModel.getServerType()).append("','");
        sb.append(appointmentModel.getUsername()).append("','");
        sb.append(appointmentModel.getPhone()).append("','");
        sb.append(appointmentModel.getAppointmentTime()).append("','");
        sb.append(appointmentModel.getContent()).append("','");
        sb.append(appointmentModel.getUpdateDate()).append("')");

        return sqlUtil.getUpdateResult(sb.toString());
    }

    public boolean cancleAppointment(AppointmentModel appointmentModel) {
        Connection con = null;
        PreparedStatement ps = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update bespeak set status = 2,updateDate = ? where userId = ? and id = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, appointmentModel.getUpdateDate());
            ps.setString(2, appointmentModel.getUser_id());
            ps.setString(3, appointmentModel.getId());

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

    public List<AppointmentModel> queryList(AppointmentModel appointmentModel, int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select * from bespeak where userId = ?");
        if (StringUtils.hasText(appointmentModel.getServerType())) {
            sb.append(" and type = ?");
        }
        if (StringUtils.hasText(appointmentModel.getStatus())) {
            sb.append(" and status = ?");
        }
        sb.append(" order by createDate desc");
        if (page > 0 && pageSize > 0) {
            sb.append(" limit ").append((page - 1) * pageSize).append(",").append(pageSize);
        }

        List<AppointmentModel> appointmentModelList = new ArrayList<AppointmentModel>();
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, appointmentModel.getUser_id());
            if (StringUtils.hasText(appointmentModel.getServerType())) {
                ps.setString(2, appointmentModel.getServerType());
                if (StringUtils.hasText(appointmentModel.getStatus())) {
                    ps.setString(3, appointmentModel.getStatus());
                }
            } else {
                if (StringUtils.hasText(appointmentModel.getStatus())) {
                    ps.setString(2, appointmentModel.getStatus());
                }
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                AppointmentModel tempAppointmentModel = new AppointmentModel();
                tempAppointmentModel.setAdminId(rs.getString("adminid"));
                tempAppointmentModel.setCreateDate(rs.getString("createDate"));
                tempAppointmentModel.setStatus(rs.getString("status"));
                tempAppointmentModel.setServiceId(rs.getString("serviceid"));
                tempAppointmentModel.setServerType(rs.getString("type"));
                tempAppointmentModel.setUsername(rs.getString("username"));
                tempAppointmentModel.setPhone(rs.getString("phone"));
                tempAppointmentModel.setAppointmentTime(rs.getString("appointmenttime"));
                tempAppointmentModel.setContent(rs.getString("content"));
                tempAppointmentModel.setUpdateDate(rs.getString("updateDate"));
                appointmentModelList.add(tempAppointmentModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return appointmentModelList;
    }
}
