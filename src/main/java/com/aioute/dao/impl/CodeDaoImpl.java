package com.aioute.dao.impl;

import com.aioute.dao.CodeDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.CodeModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class CodeDaoImpl implements CodeDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public boolean addCode(CodeModel codeModel, boolean isUpdate) {
        Connection con = null;
        PreparedStatement ps = null;

        StringBuffer sb = new StringBuffer();

        if (isUpdate) {
            sb.append("update phone_code set code=?,create_date=?,effective_duration=? where phone=?");
        } else {
            sb.append("insert into phone_code (code,create_date,effective_duration,phone) values (?,?,?,?)");
        }

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, codeModel.getCode());
            ps.setString(2, codeModel.getCreate_date());
            ps.setString(4, codeModel.getPhone());
            ps.setInt(3, codeModel.getDuration());

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

    public boolean updateCode(CodeModel codeModel) {
        Connection con = null;
        PreparedStatement ps = null;

        StringBuffer sb = new StringBuffer();
        sb.append("update phone_code set code = ?,create_date=?,effective_duration=? where phone=?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, codeModel.getCode());
            ps.setString(2, codeModel.getCreate_date());
            ps.setInt(3, codeModel.getDuration());
            ps.setString(4, codeModel.getPhone());

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

    public CodeModel getCodeByPhone(String phone) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        CodeModel codeModel = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from phone_code where phone=?");

        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, phone);

            rs = ps.executeQuery();
            while (rs.next()) {
                codeModel = new CodeModel();
                codeModel.setPhone(rs.getString("phone"));
                codeModel.setCreate_date(rs.getString("create_date"));
                codeModel.setCode(rs.getString("code"));
                codeModel.setDuration(rs.getInt("effective_duration"));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlConnectionFactory.closeConnetion(con, ps, rs);
        }
        return codeModel;
    }
}
