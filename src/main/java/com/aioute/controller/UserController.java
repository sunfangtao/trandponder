package com.aioute.controller;

import com.aioute.model.CodeModel;
import com.aioute.model.UserModel;
import com.aioute.service.CodeService;
import com.aioute.service.UserService;
import com.aioute.shiro.password.PasswordHelper;
import com.aioute.util.CloudError;
import com.aioute.util.DateUtil;
import com.aioute.util.SecurityUtil;
import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
            String picUrl = null;
            if ((picUrl = SecurityUtil.uploadPic("", file)) != null) {
                // 上传成功
                UserModel user = new UserModel();
                user.setId(SecurityUtil.getUserId());
                user.setPhoto(picUrl);
                userService.updateUser(user, false);

                // 返回数据中密码改为******
                user = userService.getUserInfoById(user.getId());
                user = SecurityUtil.handlerUser(user);
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
                userModel = SecurityUtil.handlerUser(userModel);
            }
            res.getWriter().write(SendAppJSONUtil.getNormalString(userModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户第三方登录信息
     *
     * @param res
     */
    @RequestMapping("/updLoginId")
    public void updLoginId(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            String login_id = req.getParameter("login_id");
            String phone = req.getParameter("phone");
            String code = req.getParameter("code");
            if (!StringUtils.hasText(phone)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入手机号!");
            }
            if (!StringUtils.hasText(code)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入验证码!");
            }
            if (!StringUtils.hasText(login_id)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请第三方登录标识!");
            }
            if (StringUtils.hasText(returnJson)) {
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
                UserModel userModel = userService.getUserInfoByPhone(phone);
                if (userModel == null) {
                    // 注册新用户
                    UserModel changeUser = new UserModel();
                    changeUser.setId(UUID.randomUUID().toString());
                    changeUser.setLogin_id(login_id);
                    changeUser.setLogin_name(phone);
                    changeUser.setPassword(passwordHelper.encryptPassword(null, code));
                    changeUser.setCreate_time(DateUtil.getCurDate());
                    if (userService.addUser(changeUser)) {
                        res.sendRedirect("/loginController/thirdLogin?login_id=" + login_id);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
                } else {
                    // 更新已有用户的login_id
                    UserModel changeUser = new UserModel();
                    changeUser.setId(userModel.getId());
                    changeUser.setLogin_id(login_id);
                    if (userService.updateUser(changeUser, true)) {
                        // 绑定成功，模拟登录
                        res.sendRedirect("/loginController/thirdLogin?login_id=" + login_id);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
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
    @RequestMapping("/verifyIDCard")
    public void verifyIDCard(@RequestParam("files") MultipartFile[] files, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (files != null) {
                if (files.length == 2) {
                    List<String> picUrl = null;
                    if ((picUrl = SecurityUtil.uploadPics("", files)) != null) {
                        // 上传成功
                        UserModel user = new UserModel();
                        user.setId(SecurityUtil.getUserId());
                        user.setHand_front(picUrl.get(0));
                        user.setHand_reverse(picUrl.get(1));
                        user.setVerify_status(1);
                        if (userService.updateUser(user, false)) {
                            String userId = SecurityUtil.getUserId();
                            UserModel userModel = userService.getUserInfoById(userId);
                            if (userModel != null) {
                                userModel = SecurityUtil.handlerUser(userModel);
                            }
                            returnJson = SendAppJSONUtil.getNormalString(userModel);
                        } else {
                            returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "上传失败!");
                        }
                    } else {
                        // 上传失败
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.IOException.getValue(), "上传失败");
                    }
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "请上传2张图片!");
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传图片!");
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
