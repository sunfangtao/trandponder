package com.aioute.controller;

import com.aioute.model.FavouriteModel;
import com.aioute.service.FavouriteService;
import com.aioute.util.CloudError;
import com.aioute.util.SecurityUtil;
import com.sft.util.PagingUtil;
import com.sft.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("favourites")
public class FavouriteController {

    private static Logger logger = Logger.getLogger(FavouriteController.class);
    @Resource
    private FavouriteService favouriteService;

    /**
     * 添加收藏
     *
     * @param res
     */
    @RequestMapping("add")
    public void addFavourite(FavouriteModel favouriteModel, HttpServletResponse res) {
        try {
            String returnJson = null;

            if (favouriteService.addFavourite(favouriteModel)) {
                returnJson = SendAppJSONUtil.getNormalString("收藏成功");
            } else {
                returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "收藏失败");
            }
            logger.info("添加收藏：" + returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取收藏
     *
     * @param res
     */
    @RequestMapping("get")
    public void getFavourite(String type, HttpServletRequest req, HttpServletResponse res) {
        try {
            String returnJson = null;
            int page = PagingUtil.getPage(req);
            int pageSize = PagingUtil.getPageSize(req);

            List<FavouriteModel> favouriteList = favouriteService.getFavourite(SecurityUtil.getUserId(), type, page, pageSize);
            if (favouriteList != null && favouriteList.size() > 0) {
                returnJson = SendAppJSONUtil.getNormalString(favouriteList);
            } else {
                returnJson = SendAppJSONUtil.getNullResultObject();
            }
            logger.info("获取收藏：" + returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
