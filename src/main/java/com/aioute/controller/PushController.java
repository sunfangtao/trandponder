package com.aioute.controller;

import com.aioute.model.PushModel;
import com.aioute.service.PushService;
import com.aioute.util.CloudError;
import com.sft.util.PagingUtil;
import com.aioute.util.SecurityUtil;
import com.sft.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("push")
public class PushController {

    private Logger logger = Logger.getLogger(PushController.class);

    @Resource
    private PushService pushService;

    /**
     * 获取推送列表
     *
     * @param res
     */
    @RequestMapping("list")
    public void getList(HttpServletRequest req, HttpServletResponse res) {
        try {
            String resultJson = null;
            int page = PagingUtil.getPage(req);
            int pageSize = PagingUtil.getPageSize(req);
            String pushType = req.getParameter("pushType");
            List<PushModel> pushList = pushService.getPushList(SecurityUtil.getUserId(), pushType, page, pageSize);
            if (pushList == null || pushList.size() == 0) {
                resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "没有消息!");
            } else {
                resultJson = SendAppJSONUtil.getPageJsonString(page, pageSize, 0, pushList);
            }
            logger.info(resultJson);
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
