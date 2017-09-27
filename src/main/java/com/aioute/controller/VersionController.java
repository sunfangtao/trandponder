package com.aioute.controller;

import com.aioute.model.VersionModel;
import com.aioute.service.VersionService;
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
    private VersionService versionService;

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
            VersionModel versionModel = versionService.getVersionInfo();
            if (versionModel == null) {
                resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "没有找到相应的版本");
            } else {
                resultJson = SendAppJSONUtil.getNormalString(versionModel);
            }
            logger.info("获取版本信息：" + resultJson);
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
