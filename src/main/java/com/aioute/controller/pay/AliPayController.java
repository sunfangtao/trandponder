package com.aioute.controller.pay;

import com.aioute.controller.trans.TransController;
import com.aioute.model.Permission;
import com.aioute.service.PermissionService;
import com.sft.util.HttpClient;
import com.sft.util.SecurityUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("alipayNotice")
public class AliPayController {
    private static Logger logger = Logger.getLogger(TransController.class);

    @Resource
    private PermissionService permissionService;

    /**
     * 租车订单支付回调
     *
     * @param req
     * @param res
     */
    @RequestMapping("rentalOrder")
    public void rentalOrder(HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("rentalOrder ip=" + SecurityUtil.getRemoteIP(req));
            if ("success".equals(payProcess(req, res, "rentalOrderAlipayNotice"))) {
                res.getWriter().write("success");
            } else if("success".equals(payProcess(req, res, "rentalOrderWXNotice"))){
                res.getWriter().write("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 租车订单支付回调
     *
     * @param req
     * @param res
     */
    @RequestMapping("commonOrder")
    public void commonOrder(HttpServletRequest req, HttpServletResponse res) {
        try {
            logger.info("commonOrder ip=" + SecurityUtil.getRemoteIP(req));
            if ("success".equals(payProcess(req, res, "commonOrderAlipayNotice"))) {
                res.getWriter().write("success");
            }else if("success".equals(payProcess(req, res, "commonOrderWXNotice"))){
                res.getWriter().write("success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String payProcess(HttpServletRequest req, HttpServletResponse res, String type) {
        Permission permission = permissionService.getUrlByType(type);
        if (permission == null) {
            return "";
        }
        return new HttpClient(req, res, false).sendByGet(permission.getAddress() + permission.getUrl(), null);
    }
}
