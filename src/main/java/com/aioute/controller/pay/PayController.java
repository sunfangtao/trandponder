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
@RequestMapping("payNotice")
public class PayController {
    private static Logger logger = Logger.getLogger(TransController.class);

    @Resource
    private PermissionService permissionService;

    /**
     * 订单支付回调
     *
     * @param req
     * @param res
     */
    @RequestMapping("order")
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
            if (notityXml != null && notityXml.length() > 0) {
                client.setParameter("notityXml", notityXml);
                int startIndex = notityXml.indexOf("<attach><![CDATA[");
                int endIndex = notityXml.indexOf("]]></attach>");
                type = notityXml.substring(startIndex + "<attach><![CDATA[".length(), endIndex);
            }
        } else {
            // 阿里支付
            if (map.containsKey("passback_params")) {
                type = map.get("passback_params")[0];
            }
        }
        if (type != null) {
            Permission permission = permissionService.getUrlByType(type);
            if (permission == null) {
                return "";
            }
            String result = client.sendByGet(permission.getAddress() + permission.getUrl(), null);
            logger.info("result=" + result);
            return result;
        }
        return "";
    }
}
