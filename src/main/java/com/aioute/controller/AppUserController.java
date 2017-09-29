package com.aioute.controller;

import com.aioute.model.AppUserModel;
import com.aioute.service.AppUserService;
import com.aioute.service.CodeService;
import com.aioute.shiro.password.DefaultPasswordEncoder;
import com.aioute.util.CloudError;
import com.aioute.util.DateUtil;
import com.aioute.util.SecurityUtil;
import com.aioute.util.SendAppJSONUtil;
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
    public void updateUser(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            AppUserModel user = new AppUserModel();

            String name = req.getParameter("name");
            String sex = req.getParameter("sex");
            String push_id = req.getParameter("push_id");
            String email = req.getParameter("email");
            String login_type = req.getParameter("login_type");
            String login_id = req.getParameter("login_id");
            String photo = req.getParameter("photo");

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
            if (StringUtils.hasText(photo)) {
                if (SecurityUtil.isExistFiles(photo)) {
                    user.setPhoto(photo);
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "图片地址错误!");
                    res.getWriter().write(returnJson);
                    return;
                }
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
            AppUserModel userModel = userService.getUserInfoById(userId);
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
            if ((returnJson = codeService.verifyCodeByPhone(phone, code)) != null) {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), returnJson);
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
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
                } else {
                    // 更新已有用户的login_id
                    AppUserModel changeUser = new AppUserModel();
                    changeUser.setId(userModel.getId());
                    changeUser.setLogin_id(login_id);
                    if (userService.updateUser(changeUser, true)) {
                        // 绑定成功，模拟登录
                        res.sendRedirect("/loginController/thirdLogin?login_id=" + login_id);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
                }
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
    public void verifyIDCard(String hand_front, String hand_reverse, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(hand_front) && StringUtils.hasText(hand_reverse)) {
                if (!SecurityUtil.isExistFiles(hand_front, hand_reverse)) {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "图片地址错误!");
                } else {
                    AppUserModel existUser = userService.getUserInfoById(SecurityUtil.getUserId());
                    if (existUser.getVerify_status() == 1 || existUser.getVerify_status() == 2) {
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.PERMISSION.getValue(), "不允许更新身份证信息!");
                    } else {
                        AppUserModel user = new AppUserModel();
                        user.setId(SecurityUtil.getUserId());
                        user.setHand_front(hand_front);
                        user.setHand_reverse(hand_reverse);
                        user.setVerify_status(1);
                        if (userService.updateUser(user, false)) {
                            existUser.setHand_front(hand_front);
                            existUser.setHand_reverse(hand_reverse);
                            existUser.setVerify_status(1);
                            existUser = SecurityUtil.handlerUser(existUser);
                            returnJson = SendAppJSONUtil.getNormalString(existUser);
                        } else {
                            returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "上传失败!");
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
