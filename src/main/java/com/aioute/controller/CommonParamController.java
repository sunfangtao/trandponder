package com.aioute.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("params")
public class CommonParamController {

    /**
     * 获取排序方式
     *
     * @param res
     */
    @RequestMapping("sort")
    public void createCode(HttpServletResponse res, String phone) {
        try {
            String returnJson = null;
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
