package com.aioute.controller;

import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("code")
public class CodeController {

    private static Logger logger = Logger.getLogger(CodeController.class);

    /**
     * 生成验证码
     *
     * @param res
     */
    @RequestMapping("create")
    public void createCode(HttpServletResponse res, String phone) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(phone)) {

            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入手机号!");
            }
            logger.info("生成验证码：" + returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
