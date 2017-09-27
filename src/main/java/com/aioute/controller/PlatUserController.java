package com.aioute.controller;

import com.aioute.model.PlatUserModel;
import com.aioute.model.UserLevelModel;
import com.aioute.model.bean.PlatUserBean;
import com.aioute.service.PicService;
import com.aioute.service.PlatUserService;
import com.aioute.util.CloudError;
import com.aioute.util.PagingUtil;
import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("platUser")
public class PlatUserController {

    private Logger logger = Logger.getLogger(PlatUserController.class);

    @Resource
    private PlatUserService platUserService;
    @Resource
    private PicService picService;

    /**
     * 获取平台用户信息(店面详情)
     *
     * @param res
     */
    @RequestMapping("/detail")
    public void detail(String userId, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(userId)) {
                PlatUserModel user = platUserService.getDetail(userId);
                if (user == null) {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "用户ID不存在!");
                } else {
                    returnJson = SendAppJSONUtil.getNormalString(user);
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

    /**
     * 获取平台用户图片信息(店面详情)
     *
     * @param res
     */
    @RequestMapping("/pics")
    public void pics(String userId, HttpServletResponse res) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(userId)) {
                if (platUserService.getDetail(userId) != null) {
                    List<String> picList = picService.getPics(userId);
                    returnJson = SendAppJSONUtil.getNormalString(picList);
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

    /**
     * 获取平台用户列表信息(筛选)
     *
     * @param res
     */
    @RequestMapping("/queryList")
    public void queryList(String sortType, String userLevel, String serverId, double latitude, double longtitude, HttpServletRequest req, HttpServletResponse res) {
        try {
            int page = PagingUtil.getPage(req);
            int pageSize = PagingUtil.getPageSize(req);
            List<PlatUserBean> userList = platUserService.queryList(sortType, userLevel, serverId, latitude, longtitude, page, pageSize);
            String returnJson = null;

            if (userList == null || userList.size() == 0) {
                returnJson = SendAppJSONUtil.getNullResultObject();
            } else {
                returnJson = SendAppJSONUtil.getPageJsonString(page, pageSize, 0, userList);
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取平台用户等级
     *
     * @param res
     */
    @RequestMapping("/userLevel")
    public void userLevel(HttpServletRequest req, HttpServletResponse res) {
        try {
            List<UserLevelModel> levelList = platUserService.getLevels();
            String returnJson = null;

            if (levelList == null || levelList.size() == 0) {
                returnJson = SendAppJSONUtil.getNullResultObject();
            } else {
                returnJson = SendAppJSONUtil.getNormalString(levelList);
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
