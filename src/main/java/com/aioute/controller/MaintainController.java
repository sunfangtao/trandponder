package com.aioute.controller;

import com.aioute.model.MaintainModel;
import com.aioute.model.SubMaintainModel;
import com.aioute.service.MaintainService;
import com.aioute.service.PlatUserService;
import com.aioute.util.CloudError;
import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("maintain")
public class MaintainController {

    private Logger logger = Logger.getLogger(MaintainController.class);

    @Resource
    private MaintainService maintainService;
    @Resource
    private PlatUserService platUserService;

    /**
     * 获取所有模块信息
     *
     * @param res
     */
    @RequestMapping("/all")
    public void all(HttpServletResponse res) {
        try {
            String returnJson = null;
            List<MaintainModel> maintainList = maintainService.getAll();
            if (maintainList == null || maintainList.size() == 0) {
                returnJson = SendAppJSONUtil.getNullResultObject();
            } else {
                returnJson = SendAppJSONUtil.getNormalString(maintainList);
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取模块子服务信息
     *
     * @param res
     */
    @RequestMapping("/subMaintain")
    public void subMaintain(String userId, String id, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(id)) {
                if (!StringUtils.hasText(userId)) {
                    returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传用户ID!");
                } else {
                    if (platUserService.getDetail(userId) != null) {
                        List<SubMaintainModel> subList = maintainService.getSubMaintain(userId, id);
                        returnJson = SendAppJSONUtil.getNormalString(subList);
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "用户ID不存在!");
                    }
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传模块ID!");
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取平台用户的服务模块(洗车，保养等)
     *
     * @param res
     */
    @RequestMapping("/serviceList")
    public void serviceList(String userId, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(userId)) {
                if (platUserService.getDetail(userId) != null) {
                    List<MaintainModel> maintainList = maintainService.getMaintain(userId);
                    returnJson = SendAppJSONUtil.getNormalString(maintainList);
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "用户ID不存在!");
                }

            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传用户ID!");
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
