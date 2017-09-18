package com.aioute.controller;

import com.aioute.model.UserModel;
import com.aioute.service.UserService;
import com.aioute.util.CloudError;
import com.aioute.util.DateUtil;
import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
    private UserService userService;

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
                    resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NOTLOGIN.getValue(), "请先登录！");
                } else {
                    UserModel user = userService.getUserInfoByPhone(phone);
                    user.setLogin_time(DateUtil.getCurDate());

                    if (user.getDel_flag() > 0) {
                        // 用户被禁用
                        resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.PERMISSION.getValue(), "账号被禁用，无权登录");
                    } else {
                        userService.updateUser(user, true);
                        user.setPassword("******");
                        resultJson = SendAppJSONUtil.getNormalString(user);
                    }
                    logger.info("用户开始登录 ：" + resultJson);
                }
            } else {
                // 失败
                logger.info("用户登录失败 " + errorClassName);
                if (errorClassName.contains("IncorrectCredentialsException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject("", "密码错误！");
                } else if (errorClassName.contains("UnknownAccountException")) {
                    resultJson = SendAppJSONUtil.getFailResultObject("", "用户不存在！");
                } else {
                    resultJson = SendAppJSONUtil.getFailResultObject("", "登录失败！");
                }
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
