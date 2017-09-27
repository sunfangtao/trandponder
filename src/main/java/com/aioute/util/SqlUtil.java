package com.aioute.util;

import com.aioute.db.SqlConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class SqlUtil {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public String getDistanceSql(String latitudeKey, String longtitudeKey, double latitdue, double longtitude) {

        StringBuffer sb = new StringBuffer();
        sb.append("(round(6367000 * 2 * ");
        sb.append("asin(sqrt(");
        sb.append("pow(sin(((" + latitudeKey + " * pi()) / 180 - (" + latitdue + " * pi()) / 180) / 2), 2)");
        sb.append(" + cos((" + latitdue + " * pi()) / 180) * cos((" + latitudeKey + " * pi()) / 180) * ");
        sb.append("pow(sin(((" + longtitudeKey + " * pi()) / 180 - (" + longtitude + " * pi()) / 180) / 2), 2)");
        sb.append(")))/1000)");

        return sb.toString();
    }

    public boolean getUpdateResult(String sql) {
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = sqlConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
