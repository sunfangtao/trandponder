package com.aioute.controller;

import com.aioute.model.ActivityModel;
import com.aioute.service.ActivityService;
import com.sft.util.PagingUtil;
import com.sft.util.SendAppJSONUtil;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    /**
     * 查询活动
     *
     * @param req
     * @param res
     */
    @RequestMapping("query")
    public void query(HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            int page = PagingUtil.getPage(req);
            int pageSize = PagingUtil.getPageSize(req);
            List<ActivityModel> list = activityService.queryList(page, pageSize);
            if (list == null || list.size() == 0) {
                returnJson = SendAppJSONUtil.getNullResultObject();
            } else {
                returnJson = SendAppJSONUtil.getPageJsonString(page, pageSize, 0, list);
            }
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询店面活动
     *
     * @param req
     * @param res
     */
    @RequestMapping("userActivity")
    public void userActivity(String userId, HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (!StringUtils.hasText(userId)) {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传用户ID!");
            } else {
                List<ActivityModel> list = activityService.queryUserActivityList(userId);
                if (list == null || list.size() == 0) {
                    returnJson = SendAppJSONUtil.getNullResultObject();
                } else {
                    returnJson = SendAppJSONUtil.getNormalString(list);
                }
            }

            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
