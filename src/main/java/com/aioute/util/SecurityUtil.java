package com.aioute.util;

import com.aioute.model.AppUserModel;
import com.sft.util.DateUtil;
import com.sft.util.FtpUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecurityUtil {

    private static Logger logger = Logger.getLogger(SecurityUtil.class);

    public static String getUserId() {
        try {
            return (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
        } catch (Exception e) {
            return null;
        }
    }

    public static AppUserModel handlerUser(AppUserModel userModel) {
        if (userModel != null) {
            userModel.setPassword("******");
            userModel.setLogin_id("******");
            userModel.setId("******");
            userModel.setPush_id("******");
        }
        return userModel;
    }

    public static String uploadPic(MultipartFile file) {
        FtpUtils ftpUtil = new FtpUtils();
        String temp = DateUtil.getTime(DateUtil.getCurDate()) + "";
        String fileName = UUID.randomUUID().toString().replace("-", "") + temp.substring(temp.length() - 7) + ".jpg";
        try {
            String dir = makePath(fileName);
            if (ftpUtil.makeDirecrotys(dir, "/")) {
                if (ftpUtil.upload(dir, file, fileName)) {
                    return "ftp://" + ftpUtil.getUsername() + ":" + ftpUtil.getPassword() + "@" + ftpUtil.getUrl() + ":" + ftpUtil.getPort() + "/" + dir + "/" + fileName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpUtil.close();
        }
        return null;
    }

    public static List<String> uploadPics(MultipartFile[] file) {
        List<String> urlList = null;
        if (file != null) {
            urlList = new ArrayList<String>();
            int length = file.length;
            for (int i = 0; i < length; i++) {
                String url = uploadPic(file[i]);
                if (StringUtils.hasText(url)) {
                    urlList.add(url);
                } else {
                    return null;
                }
            }
        }
        return urlList;
    }

    public static boolean isExistFiles(String... fileNames) {
        if (fileNames.length == 0) {
            return false;
        }
        boolean isExist = true;
        FtpUtils ftpUtil = new FtpUtils();
        for (int i = 0; i < fileNames.length; i++) {
            isExist &= ftpUtil.isExistFile(fileNames[i]);
        }
        return isExist;
    }

    private static String makePath(String filename) {
        int hashcode = filename.hashCode();
        int dir1 = hashcode & 0xF;
        int dir2 = (hashcode & 0xF0) >> 4;

        return dir1 + "/" + dir2;
    }
}
