package com.aioute.controller;

import com.aioute.model.AppUserModel;
import com.aioute.service.AppUserService;
import com.aioute.shiro.password.DefaultPasswordEncoder;
import com.aioute.util.Util;
import com.sft.service.CodeService;
import com.sft.util.DateUtil;
import com.sft.util.Params;
import com.sft.util.SecurityUtil;
import com.sft.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("user")
public class AppUserController {

    private Logger logger = Logger.getLogger(AppUserController.class);

    @Resource
    private AppUserService userService;
    @Resource
    private CodeService codeService;
    @Resource
    private DefaultPasswordEncoder defaultPasswordEncoder;

    /**
     * 更新用户的信息
     *
     * @param req
     * @param res
     */
    @RequestMapping("/update")
    public void updateUser(AppUserModel updateUser, HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            AppUserModel user = new AppUserModel();

            if (StringUtils.hasText(updateUser.getName())) {
                user.setName(updateUser.getName());
            }
            if (StringUtils.hasText(updateUser.getSex())) {
                user.setSex(updateUser.getSex());
            }
            if (StringUtils.hasText(updateUser.getPush_id())) {
                user.setPush_id(updateUser.getPush_id());
            }
            if (StringUtils.hasText(updateUser.getEmail())) {
                user.setEmail(updateUser.getEmail());
            }
            if (StringUtils.hasText(updateUser.getLogin_id())) {
                user.setLogin_id(updateUser.getLogin_id());
            }
            if (StringUtils.hasText(updateUser.getLogin_type())) {
                user.setLogin_type(updateUser.getLogin_type());
            }
            if (StringUtils.hasText(updateUser.getPhoto())) {
                if (Util.isExistFiles(updateUser.getPhoto())) {
                    user.setPhoto(updateUser.getPhoto());
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NODATA.getValue(), "头像图片地址错误!");
                    res.getWriter().write(returnJson);
                    return;
                }
            }
            user.setId(SecurityUtil.getUserId(req));
            if (userService.updateUser(user, false)) {
                returnJson = SendAppJSONUtil.getNormalString("更新成功!");
            } else {
                returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "更新失败!");
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
    public void userDetail(HttpServletRequest req, HttpServletResponse res) {
        try {
            String userId = SecurityUtil.getUserId(req);
            AppUserModel userModel = userService.getUserInfoById(userId);
            if (userModel != null) {
                userModel = Util.handlerUser(userModel);
            }
            logger.info(SendAppJSONUtil.getNormalString(userModel));
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
            if ((returnJson = codeService.verifyCodeByPhone(phone, code)) != null) {
                returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NODATA.getValue(), returnJson);
            } else {
                AppUserModel userModel = userService.getUserInfoByPhone(phone);
                if (userModel == null) {
                    // 注册新用户
                    AppUserModel changeUser = new AppUserModel();
                    changeUser.setId(UUID.randomUUID().toString());
                    changeUser.setLogin_id(login_id);
                    changeUser.setLogin_name(phone);
                    changeUser.setPassword(defaultPasswordEncoder.encode(code));
                    changeUser.setCreate_time(DateUtil.getCurDate());
                    if (userService.addUser(changeUser)) {
                        res.sendRedirect("/loginController/thirdLogin?login_id=" + login_id);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
                } else {
                    // 更新已有用户的login_id
                    AppUserModel changeUser = new AppUserModel();
                    changeUser.setId(userModel.getId());
                    changeUser.setLogin_id(login_id);
                    if (userService.updateUser(changeUser, false)) {
                        // 绑定成功，模拟登录
                        res.sendRedirect("/loginController/thirdLogin?login_id=" + login_id);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
                }
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传驾驶证
     *
     * @param res
     */
    @RequestMapping("/verifyDriverCard")
    public void verifyDricerCard(String driveLicence, HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(driveLicence)) {
                if (!Util.isExistFiles(driveLicence)) {
                    returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NODATA.getValue(), "图片地址错误!");
                } else {
                    AppUserModel existUser = userService.getUserInfoById(SecurityUtil.getUserId(req));
                    if (existUser.getLicenceStatus() == Params.VerifyStatus.VERIFYING.getValue() || existUser.getLicenceStatus() == Params.VerifyStatus.VERIFY_SUCCESS.getValue()) {
                        returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "不允许更新驾驶证信息!");
                    } else {
                        AppUserModel user = new AppUserModel();
                        user.setId(SecurityUtil.getUserId(req));
                        user.setDriveLicence(driveLicence);
                        user.setLicenceStatus(Params.VerifyStatus.VERIFYING.getValue());
                        if (userService.updateUser(user, true)) {
                            existUser.setDriveLicence(driveLicence);
                            existUser.setLicenceStatus(Params.VerifyStatus.VERIFYING.getValue());
                            existUser = Util.handlerUser(existUser);
                            returnJson = SendAppJSONUtil.getNormalString(existUser);
                        } else {
                            returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "上传失败!");
                        }
                    }
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传图片地址!");
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传身份证
     *
     * @param res
     */
    @RequestMapping("/verifyIDCard")
    public void verifyIDCard(String hand_front, String hand_reverse, HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(hand_front) && StringUtils.hasText(hand_reverse)) {
                if (!Util.isExistFiles(hand_front, hand_reverse)) {
                    returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NODATA.getValue(), "图片地址错误!");
                } else {
                    AppUserModel existUser = userService.getUserInfoById(SecurityUtil.getUserId(req));
                    if (existUser.getVerify_status() == Params.VerifyStatus.VERIFYING.getValue() || existUser.getVerify_status() == Params.VerifyStatus.VERIFY_SUCCESS.getValue()) {
                        returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "不允许更新身份证信息!");
                    } else {
                        AppUserModel user = new AppUserModel();
                        user.setId(SecurityUtil.getUserId(req));
                        user.setHand_front(hand_front);
                        user.setHand_reverse(hand_reverse);
                        user.setVerify_status(Params.VerifyStatus.VERIFYING.getValue());
                        if (userService.updateUser(user, true)) {
                            existUser.setHand_front(hand_front);
                            existUser.setHand_reverse(hand_reverse);
                            existUser.setVerify_status(Params.VerifyStatus.VERIFYING.getValue());
                            existUser = Util.handlerUser(existUser);
                            returnJson = SendAppJSONUtil.getNormalString(existUser);
                        } else {
                            returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "上传失败!");
                        }
                    }
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传图片地址!");
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}