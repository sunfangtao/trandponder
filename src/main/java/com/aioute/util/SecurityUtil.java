package com.aioute.util;

import com.aioute.model.UserModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtil {

    public static String getUserId() {
        return (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
    }

    public static UserModel handlerUser(UserModel userModel) {
        if (userModel != null) {
            userModel.setPassword("******");
            userModel.setLogin_id("******");
        }
        return userModel;
    }

    public static String uploadPic(String dir, MultipartFile file) {
        FtpUtil ftpUtil = new FtpUtil();
        ftpUtil.conn();
        boolean boo = false;
        String baseDir = "/data/ftp/";
        String temp = DateUtil.getTime(DateUtil.getCurDate()) + "";
        String fileName = getUserId().replace("-", "") + temp.substring(temp.length() - 7) + ".jpg";
        try {
            ftpUtil.createDirecrotys(dir);
            if (ftpUtil.upload(dir, fileName, file.getInputStream())) {
                return "ftp://" + ftpUtil.getUsername() + ":" + ftpUtil.getPassword() + "@" + ftpUtil.getUrl() + ":" + ftpUtil.getPort() + "/" + dir + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpUtil.close();
        }
        return null;
    }

    public static List<String> uploadPics(String dir, MultipartFile[] file) {

        List<String> urlList = null;
        if (file != null) {
            urlList = new ArrayList<String>();
            int length = file.length;
            for (int i = 0; i < length; i++) {
                String url = uploadPic(dir, file[i]);
                if (StringUtils.hasText(url)) {
                    urlList.add(url);
                } else {
                    return null;
                }
            }
        }
        return urlList;
    }
}
