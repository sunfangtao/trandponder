package com.aioute.controller;

import com.aioute.model.SellCarModel;
import com.aioute.service.CodeService;
import com.aioute.service.PicService;
import com.aioute.service.SellCarService;
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
import java.util.UUID;

@Controller
@RequestMapping("sell")
public class SellCarController {

    private Logger logger = Logger.getLogger(SellCarController.class);

    @Resource
    private SellCarService sellCarService;
    @Resource
    private PicService picService;
    @Resource
    private CodeService codeService;

    @RequestMapping("add")
    public void addSellCar(List<String> picList, SellCarModel sellCarModel, HttpServletRequest req, HttpServletResponse res) {
        try {
            String resultJson = null;
            // 验证手机验证码
            if ((resultJson = codeService.verifyCodeByPhone(sellCarModel.getMobile(), req.getParameter("code"))) != null) {
                res.getWriter().write(SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), resultJson));
                return;
            }
            sellCarModel.setId(UUID.randomUUID().toString());
            if (sellCarService.addSell(sellCarModel)) {
                picService.addPic(sellCarModel.getId(), picList);
                resultJson = SendAppJSONUtil.getNormalString("添加成功!");
            } else {
                resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "添加失败!");
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("query")
    public void queryList(SellCarModel sellCarModel, String priceRange, String sortType, HttpServletRequest req, HttpServletResponse res) {
        try {
            String resultJson = null;
            int page = PagingUtil.getPage(req);
            int pageSize = PagingUtil.getPageSize(req);
            List<SellCarModel> list = sellCarService.queryList(sortType, sellCarModel, priceRange, page, pageSize);
            if (list == null || list.size() == 0) {
                resultJson = SendAppJSONUtil.getNullResultObject();
            } else {
                resultJson = SendAppJSONUtil.getPageJsonString(page, pageSize, 0, list);
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取卖车详情
     *
     * @param id
     * @param res
     */
    @RequestMapping("detail")
    public void getDetail(String id, HttpServletResponse res) {
        try {
            String resultJson = null;
            if (StringUtils.hasText(id)) {
                SellCarModel sellCarModel = sellCarService.getDetail(id);
                if (sellCarModel == null) {
                    resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "ID不存在!");
                } else {
                    resultJson = SendAppJSONUtil.getNormalString(sellCarModel);
                }
            } else {
                resultJson = SendAppJSONUtil.getRequireParamsMissingObject("请上传ID!");
            }
            res.getWriter().write(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取卖车图片
     *
     * @param id
     * @param res
     */
    @RequestMapping("pics")
    public void getPics(String id, HttpServletResponse res) {
        try {
            String resultJson = null;
            if (StringUtils.hasText(id)) {
                List<String> picList = picService.getPics(id);
                if (picList == null) {
                    resultJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NODATA.getValue(), "ID不存在!");
                } else {
                    resultJson = SendAppJSONUtil.getNormalString(picList);
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
