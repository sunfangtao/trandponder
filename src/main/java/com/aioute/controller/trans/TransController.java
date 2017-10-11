package com.aioute.controller.trans;

import com.aioute.model.Permission;
import com.aioute.service.FilterChainDefinitionsService;
import com.aioute.service.PermissionService;
import com.aioute.util.CloudError;
import com.sft.util.HttpClient;
import com.aioute.util.SecurityUtil;
import com.sft.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("trans")
public class TransController {

    private static Logger logger = Logger.getLogger(TransController.class);

    @Resource
    private FilterChainDefinitionsService filterChainDefinitionsService;
    @Resource
    private PermissionService permissionService;

    /**
     * app接口转发
     *
     * @param req
     * @param res
     */
    @RequestMapping("app")
    public void transPlat(HttpServletRequest req, HttpServletResponse res) {
        String returnJson = null;
        String type = req.getParameter("type");
        try {
            if (StringUtils.hasText(type)) {
                Permission permission = permissionService.getUrlByType(type);
                if (permission == null) {
                    returnJson = SendAppJSONUtil.getRequireParamsMissingObject("type错误!");
                } else {
                    if (permission.isIs_user() && SecurityUtils.getSubject().getPrincipal() == null) {
                        if (SecurityUtils.getSubject().getPrincipal() == null) {
                            // 用户没有登录
                            returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NOTLOGIN.getValue(), "请先登录!");
                        } else {
                            new HttpClient(req, res).sendByGet(permission.getAddress() + permission.getUrl(), SecurityUtil.getUserId());
                            return;
                        }
                    } else {
                        new HttpClient(req, res).sendByGet(permission.getAddress() + permission.getUrl(), null);
                        return;
                    }
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("没有type!");
            }

            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * app接口转发
     *
     * @param res
     */
    @RequestMapping("pic")
    public void transPict(HttpServletRequest req, HttpServletResponse res, @RequestParam("files") MultipartFile[] files) {
        String returnJson = null;
        try {
            if (files != null && files.length > 0) {
                List<String> picUrl = null;
                if ((picUrl = SecurityUtil.uploadPics(files)) != null) {
                    returnJson = SendAppJSONUtil.getNormalString(picUrl);
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "上传失败!");
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请添加文件!");
            }
            logger.info(returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 访问权限更新
     *
     * @param res
     */
    @RequestMapping("updatePermission")
    public void updatePermission(HttpServletResponse res) {
        try {
            filterChainDefinitionsService.reloadFilterChains();
            permissionService.update();
            String returnJson = SendAppJSONUtil.getNormalString("资源权限更新成功!");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}