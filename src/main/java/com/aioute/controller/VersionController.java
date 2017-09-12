package com.aioute.controller;

import com.aioute.dao.VersionDao;
import com.aioute.model.VersionModel;
import com.aioute.util.CloudError;
import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("version")
public class VersionController {
    private Logger logger = Logger.getLogger(VersionController.class);
    @Resource
    private VersionDao versionDao;

    /**
     * 获取版本信息
     *
     * @param req
     * @param res
     */
    @RequestMapping("curVersion")
    public void version(HttpServletRequest req, HttpServletResponse res) {
        try {
            String resultJson = null;
            String type = req.getParameter("type");
            if (type == null || type.length() == 0) {
                resultJson = SendAppJSONUtil.getRequireParamsMissingObject("type为必填参数");
            } else {
                VersionModel versionModel = versionDao.getVersionInfo(type);
                if (versionModel == null) {
                    resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "没有找到相应的版本");
                } else {
                    resultJson = SendAppJSONUtil.getNormalString(versionModel);
                }
            }
            logger.info("获取版本信息：" + resultJson);
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}