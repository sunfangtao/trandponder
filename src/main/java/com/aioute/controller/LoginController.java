package com.aioute.controller;

import com.aioute.model.AppUserModel;
import com.aioute.service.AppUserService;
import com.aioute.shiro.UserNamePasswordToken;
import com.aioute.util.Util;
import com.sft.util.DateUtil;
import com.sft.util.Params;
import com.sft.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("loginController")
public class LoginController {

    private Logger logger = Logger.getLogger(LoginController.class);

    @Resource
    private AppUserService userService;

    /**
     * 用户登录
     *
     * @param req
     * @param res
     */
    @RequestMapping("login")
    public void login(HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("login");
            String resultJson = null;
            String errorClassName = (String) req.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
            if (errorClassName == null) {
                // 成功
                Subject subject = SecurityUtils.getSubject();
                String phone = (String) subject.getPrincipal();

                if (phone == null || phone.length() == 0) {
                    // 非法请求
                    logger.info("用户请求未认证");
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOTLOGIN.getValue(), "请先登录！");
                } else {
                    AppUserModel user = userService.getUserInfoByPhone(phone);
                    resultJson = loginSuccess(user);
                    logger.info("用户开始登录 ：" + resultJson);
                }
            } else {
                // 失败
                logger.info("用户登录失败 " + errorClassName);
                if (errorClassName.contains("IncorrectCredentialsException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PASSWORDERROR.getValue(), "密码错误！");
                } else if (errorClassName.contains("UnknownAccountException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOACCOUNT.getValue(), "用户不存在！");
                } else {
                    resultJson = SendAppJSONUtil.getFailResultObject("", "登录失败！");
                }
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登录
     *
     * @param req
     * @param res
     */
    @RequestMapping("thirdLogin")
    public void thirdLogin(HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("thirdLogin");
            String resultJson = null;
            String login_id = req.getParameter("login_id");

            if (StringUtils.hasText(login_id)) {
                AppUserModel userModel = userService.getUserInfoByLoginId(login_id);
                if (userModel != null) {
                    // 模拟登录
                    UserNamePasswordToken token = new UserNamePasswordToken(userModel.getLogin_name(), userModel.getPassword(), true);
                    try {
                        SecurityUtils.getSubject().login(token);
                        resultJson = loginSuccess(userModel);
                        logger.info("用户开始登录 ：" + resultJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NODATA.getValue(), "请先绑定手机号!");
                }
            } else {
                // 失败
                resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOREQUIREPARAMS.getValue(), "缺少第三方认证信息！");
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loginSuccess(AppUserModel userModel) {
        userModel.setLogin_time(DateUtil.getCurDate());

        if (userModel.getDel_flag() > 0) {
            // 用户被禁用
            return SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "账号被禁用，无权登录");
        } else {
            userService.updateUser(userModel, false);
            userModel = Util.handlerUser(userModel);
            return SendAppJSONUtil.getNormalString(userModel);
        }
    }

    @RequestMapping("test")
    public void testLogin(HttpServletRequest req, HttpServletResponse res) {
        try {
            String resultJson = null;
            String phone = req.getParameter("phone");
            String code = req.getParameter("code");
            // 模拟登录
            UserNamePasswordToken token = new UserNamePasswordToken(phone, code, false);
            try {
                SecurityUtils.getSubject().login(token);
                resultJson = "用户开始登录";
                logger.info("用户开始登录 ：");
            } catch (Exception e) {
                resultJson = "用户登录失败";
                e.printStackTrace();
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
