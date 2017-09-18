package com.aioute.controller;

import com.aioute.model.CodeModel;
import com.aioute.model.UserModel;
import com.aioute.service.CodeService;
import com.aioute.service.UserService;
import com.aioute.shiro.password.PasswordHelper;
import com.aioute.util.*;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("user")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;
    @Resource
    private CodeService codeService;
    @Resource
    private PasswordHelper passwordHelper;

    /**
     * 更新用户的头像
     *
     * @param req
     * @param res
     */
    @RequestMapping("/headPic")
    public void updateUserHeadPic(MultipartFile file, HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;

            FtpUtil ftpUtil = new FtpUtil();
            ftpUtil.conn();
            String dir = "/data/ftp/";
            boolean boo = false;

            String temp = DateUtil.getTime(DateUtil.getCurDate()) + "";
            String fileName = SecurityUtil.getUserId().replace("-", "") + temp.substring(temp.length() - 7) + ".jpg";
            try {
                ftpUtil.createDirecrotys(dir);
                boo = ftpUtil.upload(dir, fileName, file.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ftpUtil.close();
            String picurl = "ftp://" + ftpUtil.getUsername() + ":" + ftpUtil.getPassword() + "@" + ftpUtil.getUrl() + ":" + ftpUtil.getPort() + "/" + fileName;

            if (boo) {
                // 上传成功
                UserModel user = new UserModel();
                user.setId(SecurityUtil.getUserId());
                user.setPhoto(picurl);
                userService.updateUser(user, false);

                // 返回数据中密码改为******
                user = userService.getUserInfoById(user.getId());
                user.setPassword("******");
                returnJson = SendAppJSONUtil.getNormalString(user);
            } else {
                // 上传失败
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.IOException.getValue(), "上传失败");
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新用户的信息
     *
     * @param req
     * @param res
     */
    @RequestMapping("/update")
    public void updateUser(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            UserModel user = new UserModel();

            String name = req.getParameter("name");
            String sex = req.getParameter("sex");
            String push_id = req.getParameter("push_id");
            String email = req.getParameter("email");
            String login_type = req.getParameter("login_type");
            String login_id = req.getParameter("login_id");

            if (StringUtils.hasText(name)) {
                user.setName(name);
            }
            if (StringUtils.hasText(sex)) {
                user.setSex(sex.equals("0") ? "男" : "女");
            }
            if (StringUtils.hasText(push_id)) {
                user.setPush_id(push_id);
            }
            if (StringUtils.hasText(email)) {
                user.setEmail(email);
            }
            if (StringUtils.hasText(login_id)) {
                user.setLogin_id(login_id);
            }
            if (StringUtils.hasText(login_type)) {
                user.setLogin_type(login_type);
            }
            user.setId(SecurityUtil.getUserId());
            if (userService.updateUser(user, true)) {
                returnJson = SendAppJSONUtil.getNormalString("更新成功!");
            } else {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "更新失败!");
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加用户
     */
    @RequestMapping("/add")
    public void addUser(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;

            UserModel user = new UserModel();
            String phone = req.getParameter("phone");
            String code = req.getParameter("code");
            String password = req.getParameter("password");
            if (!StringUtils.hasText(code)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入验证码!");
            }
            if (!StringUtils.hasText(password)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入密码!");
            }
            if (!StringUtils.hasText(phone)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入手机号!");
            }
            if (returnJson != null) {
                res.getWriter().write(returnJson);
                return;
            }
            UserModel existUser = userService.getUserInfoByPhone(phone);
            if (existUser != null) {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.REPEAT.getValue(), "手机号已被占用!");
                res.getWriter().write(returnJson);
                return;
            }
            CodeModel codeModel = codeService.getCodeByPhone(phone);
            if (codeModel == null) {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "请先获取验证码!");
                res.getWriter().write(returnJson);
                return;
            }
            long range = (DateUtil.getTime(DateUtil.getCurDate()) - DateUtil.getTime(codeModel.getCreate_date())) / 60 / 1000;
            if (range > codeModel.getDuration()) {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "请重新获取验证码!");
                res.getWriter().write(returnJson);
                return;
            }
            if (code.equals(codeModel.getCode())) {
                user.setCreate_time(DateUtil.getCurDate());
                user.setLogin_name(phone);
                user.setPassword(passwordHelper.encryptPassword(null, password));
                user.setId(UUID.randomUUID().toString());
                if (userService.addUser(user)) {
                    returnJson = SendAppJSONUtil.getNormalString("注册成功!");
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "注册失败!");
                }
            } else {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.PERMISSION.getValue(), "验证码错误!");
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单个用户详情
     *
     * @param res
     */
    @RequestMapping("/detail")
    public void userDetail(HttpServletResponse res) {
        try {
            String userId = SecurityUtil.getUserId();
            UserModel userModel = userService.getUserInfoById(userId);
            if (userModel != null) {
                userModel.setPassword("******");
            }
            res.getWriter().write(SendAppJSONUtil.getNormalString(userModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改用户密码
     *
     * @param res
     */
    @RequestMapping("/updPassword")
    public void updatePassword(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            String code = req.getParameter("code");
            String password = req.getParameter("password");
            if (!StringUtils.hasText(code)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入验证码!");
            }
            if (!StringUtils.hasText(password)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入密码!");
            }
            if (StringUtils.hasText(returnJson)) {
                res.getWriter().write(returnJson);
            }
            String phone = (String) SecurityUtils.getSubject().getPrincipal();
            CodeModel codeModel = codeService.getCodeByPhone(phone);
            if (codeModel == null) {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "请先获取验证码!");
                res.getWriter().write(returnJson);
                return;
            }
            long range = (DateUtil.getTime(DateUtil.getCurDate()) - DateUtil.getTime(codeModel.getCreate_date())) / 60 / 1000;
            if (range > codeModel.getDuration()) {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "请重新获取验证码!");
                res.getWriter().write(returnJson);
                return;
            }

            UserModel user = new UserModel();
            if (code.equals(codeModel.getCode())) {
                user.setPassword(passwordHelper.encryptPassword(null, password));
                user.setId(SecurityUtil.getUserId());
                if (userService.updateUser(user, true)) {
                    returnJson = SendAppJSONUtil.getNormalString("修改成功!");
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "修改失败!");
                }
            } else {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.PERMISSION.getValue(), "验证码错误!");
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
