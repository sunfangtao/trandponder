package com.aioute.controller;

import com.aioute.model.AppUserModel;
import com.aioute.service.AppUserService;
import com.aioute.shiro.UserNamePasswordToken;
import com.sft.password.DefaultPasswordEncoder;
import com.sft.service.CodeService;
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
import java.util.UUID;

@Controller
@RequestMapping("loginController")
public class LoginController {

    private Logger logger = Logger.getLogger(LoginController.class);

    @Resource
    private AppUserService userService;
    @Resource
    private CodeService codeService;
    @Resource
    private DefaultPasswordEncoder defaultPasswordEncoder;

    /**
     * 用户登录
     *
     * @param req
     * @param res
     */
    @RequestMapping("logout")
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        try {
            String phone = (String) SecurityUtils.getSubject().getPrincipal();
            logger.info(phone + " logout");
            AppUserModel userModel = userService.getUserInfoByPhone(phone);
            SecurityUtils.getSubject().getSession().removeAttribute("userId");
            SecurityUtils.getSubject().logout();
            userModel.setPass_val("0");
            userService.updateUser(userModel);
            String result = SendAppJSONUtil.getNormalString("退出登录成功!");
            logger.info(result);
            res.getWriter().write(result);
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
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PASSWORDERROR.getValue(), "验证码错误！");
                } else if (errorClassName.contains("UnknownAccountException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOACCOUNT.getValue(), "用户不存在！");
                } else if (errorClassName.contains("NoCodeException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "请获取验证码！");
                } else if (errorClassName.contains("CodeErrorException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "验证码错误！");
                } else if (errorClassName.contains("CodeInvalicException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "验证码失效！");
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
                    changeUser.setPass_val("1");
                    changeUser.setPassword(defaultPasswordEncoder.encode(code));
                    changeUser.setCreate_time(DateUtil.getCurDate());
                    if (userService.addUser(changeUser)) {
                        res.sendRedirect("../loginController/thirdLogin?login_id=" + login_id);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
                } else {
                    // 更新已有用户的login_id
                    AppUserModel changeUser = new AppUserModel();
                    changeUser.setId(userModel.getId());
                    changeUser.setLogin_id(login_id);
                    changeUser.setPass_val(null);
                    if (userService.updateUser(changeUser)) {
                        // 绑定成功，模拟登录
                        res.sendRedirect("../loginController/thirdLogin?login_id=" + login_id);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "绑定失败!");
                    }
                }
            }
            if (StringUtils.hasText(returnJson)) {
                res.getWriter().write(returnJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loginSuccess(AppUserModel userModel) {
        userModel.setLogin_time(DateUtil.getCurDate());
        // 登录成功不修改密码有效位
        userModel.setPass_val(null);
        if (userModel.getDel_flag() > 0) {
            // 用户被禁用
            return SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.PERMISSION.getValue(), "账号被禁用，无权登录");
        } else {
            userService.updateUser(userModel);
            userModel.setPassword("******");
            userModel.setLogin_id("******");
            userModel.setId("******");
            userModel.setPush_id("******");
            return SendAppJSONUtil.getNormalString(userModel);
        }
    }

    @RequestMapping("test")
    public void testLogin(HttpServletRequest req, HttpServletResponse res) {
        try {
            String phone = req.getParameter("phone");
            String code = req.getParameter("code");
            // 模拟登录
            UserNamePasswordToken token = new UserNamePasswordToken(phone, code, false);
            try {
                SecurityUtils.getSubject().login(token);
                logger.info("用户开始登录(测试接口) ：phone=" + phone + " code=" + code);
                res.getWriter().write("用户测试登录成功 phone=" + phone);
            } catch (Exception e) {
                e.printStackTrace();
                res.getWriter().write("用户测试登录失败 phone=" + phone);
                logger.info("用户开始登录(测试接口) ：phone=" + phone + " code=" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
