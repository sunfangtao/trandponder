package com.aioute.controller;

import com.aioute.model.NewCarModel;
import com.aioute.service.NewCarService;
import com.aioute.util.CloudError;
import com.sft.util.PagingUtil;
import com.sft.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("newCar")
public class NewCarController {

    private Logger logger = Logger.getLogger(NewCarController.class);

    @Resource
    private NewCarService newCarService;

    @RequestMapping("query")
    public void queryList(HttpServletRequest req, HttpServletResponse res) {
        try {
            String resultJson = null;
            int page = PagingUtil.getPage(req);
            int pageSize = PagingUtil.getPageSize(req);
            String userId = req.getParameter("userId");
            if (StringUtils.hasText(userId)) {
                List<NewCarModel> newCarList = newCarService.queryList(userId, page, pageSize);
                if (newCarList == null || newCarList.size() == 0) {
                    resultJson = SendAppJSONUtil.getNullResultObject();
                } else {
                    resultJson = SendAppJSONUtil.getPageJsonString(page, pageSize, 0, newCarList);
                }
            } else {
                resultJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传用户Id!");
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取新车详情
     *
     * @param id
     * @param res
     */
    @RequestMapping("detail")
    public void getDetail(String id, HttpServletResponse res) {
        try {
            String resultJson = null;
            if (StringUtils.hasText(id)) {
                NewCarModel newCarModel = newCarService.getDetail(id);
                if (newCarModel == null) {
                    resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "ID不存在!");
                } else {
                    resultJson = SendAppJSONUtil.getNormalString(newCarModel);
                }
            } else {
                resultJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传ID!");
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
