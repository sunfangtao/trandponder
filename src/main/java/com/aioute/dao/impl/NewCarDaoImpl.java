package com.aioute.dao.impl;

import com.aioute.dao.NewCarDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.NewCarModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NewCarDaoImpl implements NewCarDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public NewCarModel getDetail(String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        NewCarModel newCarModel = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from new_car where id = ? and del_flag = 0");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                newCarModel = new NewCarModel();
                newCarModel.setId(rs.getString("id"));
                newCarModel.setUser_id(rs.getString("user_id"));
                newCarModel.setBrand(rs.getString("brand"));
                newCarModel.setPrice(rs.getString("price"));
                newCarModel.setParam(rs.getString("param"));
                newCarModel.setHotFlag(rs.getString("hot_flag"));
                newCarModel.setTitle(rs.getString("title"));
                newCarModel.setDiscounts(rs.getString("discounts"));
                newCarModel.setVehiclePic(rs.getString("vehicle_pic"));
                newCarModel.setManufacturer(rs.getString("manufacturer"));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return newCarModel;
    }

    public List<NewCarModel> queryList(String userId, int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<NewCarModel> newCarModelList = new ArrayList<NewCarModel>();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from new_car where user_id = ? and del_flag = 0");
        if (page > 0 && pageSize > 0) {
            sb.append(" limit ").append((page - 1) * pageSize).append(",").append(pageSize);
        }

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                NewCarModel newCarModel = new NewCarModel();
                newCarModel.setId(rs.getString("id"));
                newCarModel.setUser_id(rs.getString("user_id"));
                newCarModel.setBrand(rs.getString("brand"));
                newCarModel.setPrice(rs.getString("price"));
                newCarModel.setParam(rs.getString("param"));
                newCarModel.setHotFlag(rs.getString("hot_flag"));
                newCarModel.setTitle(rs.getString("title"));
                newCarModel.setDiscounts(rs.getString("discounts"));
                newCarModel.setVehiclePic(rs.getString("vehicle_pic"));
                newCarModel.setManufacturer(rs.getString("manufacturer"));
                newCarModelList.add(newCarModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return newCarModelList;
    }
}
