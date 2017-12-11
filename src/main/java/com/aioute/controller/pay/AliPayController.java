package com.aioute.controller.pay;

import com.aioute.controller.trans.TransController;
import com.aioute.model.Permission;
import com.aioute.service.PermissionService;
import com.sft.util.HttpClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
            res.getWriter().write(payProcess(req, res));
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
            res.getWriter().write(payProcess(req, res));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String payProcess(HttpServletRequest req, HttpServletResponse res) {
        String type = null;
        HttpClient client = new HttpClient(req, res, false);
        Map<String, String[]> map = req.getParameterMap();
        if (map == null || map.size() == 0) {
            // 微信支付
            String inputLine;
            String notityXml = "";
            try {
                while ((inputLine = req.getReader().readLine()) != null) {
                    notityXml += inputLine;
                }
                req.getReader().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            client.setParameter("notityXml", notityXml);
            type = "rentalOrderWXNotice";
        } else {
            // 阿里支付
            type = "rentalOrderAliNotice";
        }
        Permission permission = permissionService.getUrlByType(type);
        if (permission == null) {
            return "";
        }
        return client.sendByGet(permission.getAddress() + permission.getUrl(), null);
    }
}
