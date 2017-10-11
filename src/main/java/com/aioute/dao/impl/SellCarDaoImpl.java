package com.aioute.dao.impl;

import com.aioute.dao.SellCarDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.SellCarModel;
import com.aioute.util.CloudError;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SellCarDaoImpl implements SellCarDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public boolean addSell(SellCarModel sellCarModel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuffer sb = new StringBuffer();
        sb.append("insert into sell_car (id,mobile,mileage,price_tax,sell_price,brand,car_license,displacement,location,gearbox," +
                "discharge_standard,type,user_id,photo,createdate,remark,frontImg,behindImg,sideImg) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, sellCarModel.getId());
            ps.setString(2, sellCarModel.getMobile());
            ps.setString(3, sellCarModel.getMileage());
            ps.setDouble(4, sellCarModel.getPriceTax());
            ps.setDouble(5, sellCarModel.getSellPrice());
            ps.setString(6, sellCarModel.getBrand());
            ps.setString(7, sellCarModel.getCarLicense());
            ps.setString(8, sellCarModel.getDisplacement());
            ps.setString(9, sellCarModel.getLocation());
            ps.setString(10, sellCarModel.getGearbox());
            ps.setString(11, sellCarModel.getDischargeStandard());
            ps.setString(12, sellCarModel.getUserType());
            ps.setString(13, sellCarModel.getUserId());
            ps.setString(14, sellCarModel.getPhoto());
            ps.setString(15, sellCarModel.getCreatedate());
            ps.setString(16, sellCarModel.getRemark());
            ps.setString(17, sellCarModel.getFrontImg());
            ps.setString(18, sellCarModel.getBehindImg());
            ps.setString(19, sellCarModel.getSideImg());

            int result = ps.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return false;
    }

    public SellCarModel getDetail(String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        SellCarModel sellCarModel = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from sell_car where id = ?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                sellCarModel = setSellCarValue(rs);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return sellCarModel;
    }

    public List<SellCarModel> queryList(String sortType, SellCarModel sellCarModel, String priceRange, int page, int pageSize) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<SellCarModel> sellCarModelList = new ArrayList<SellCarModel>();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from sell_car where 1 = 1");
        if (StringUtils.hasText(sellCarModel.getUserType())) {
            if ("0".equals(sellCarModel.getUserType())) {
                sb.append(" and type = '0'");
            } else {
                sb.append(" and type = '1'");
            }
        }
        if (StringUtils.hasText(sellCarModel.getBrand())) {
            sb.append(" and brand like %").append(sellCarModel.getBrand()).append("%");
        }
        if (StringUtils.hasText(priceRange) && priceRange.contains("_") && priceRange.length() > 1) {
            String[] prices = priceRange.split("_");
            if ("".equals(prices[0])) {
                // 小于
                sb.append(" and sell_price <= ").append(prices[1]);
            } else if (prices.length == 1) {
                // 大于
                sb.append(" and sell_price >= ").append(prices[0]);
            } else {
                sb.append(" and sell_price between ").append(prices[0]).append(" and ").append(prices[1]);
            }
        }

        if (StringUtils.hasText(sortType)) {
            if (sortType.equals(CloudError.SortEnum.CREATE_TIME.getValue())) {
                sb.append(" order by create_time desc");
            } else if (sortType.equals(CloudError.SortEnum.PRICE.getValue())) {
                sb.append(" order by sell_price");
            } else if (sortType.equals(CloudError.SortEnum.PRICE_ASC.getValue())) {
                sb.append(" order by sell_price asc");
            } else if (sortType.equals(CloudError.SortEnum.PRICE_DESC.getValue())) {
                sb.append(" order by sell_price desc");
            }
        }

        if (page > 0 && pageSize > 0) {
            sb.append(" limit ").append((page - 1) * pageSize).append(",").append(pageSize);
        }

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                sellCarModelList.add(setSellCarValue(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return sellCarModelList;
    }

    private SellCarModel setSellCarValue(ResultSet rs) throws Exception {
        SellCarModel sellCarModel = new SellCarModel();
        sellCarModel.setId(rs.getString("id"));
        sellCarModel.setMobile(rs.getString("mobile"));
        sellCarModel.setMileage(rs.getString("mileage"));
        sellCarModel.setPriceTax(rs.getDouble("price_tax"));
        sellCarModel.setSellPrice(rs.getDouble("sell_price"));
        sellCarModel.setBrand(rs.getString("brand"));
        sellCarModel.setCarLicense(rs.getString("car_license"));
        sellCarModel.setDisplacement(rs.getString("displacement"));
        sellCarModel.setLocation(rs.getString("location"));
        sellCarModel.setGearbox(rs.getString("gearbox"));
        sellCarModel.setDischargeStandard(rs.getString("discharge_standard"));
        sellCarModel.setUserType(rs.getString("type"));
        sellCarModel.setUserId(rs.getString("user_id"));
        sellCarModel.setPhoto(rs.getString("photo"));
        sellCarModel.setCreatedate(rs.getString("createdate"));
        sellCarModel.setRemark(rs.getString("remark"));
        sellCarModel.setBehindImg(rs.getString("behindImg"));
        sellCarModel.setFrontImg(rs.getString("frontImg"));
        sellCarModel.setSideImg(rs.getString("sideImg"));

        return sellCarModel;
    }
}
