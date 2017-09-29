package com.aioute.controller.trans;

import com.aioute.model.Permission;
import com.aioute.service.PermissionService;
import com.aioute.util.CloudError;
import com.aioute.util.HttpClient;
import com.aioute.util.SecurityUtil;
import com.aioute.util.SendAppJSONUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("trans")
public class TransController {

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
                    if (permission.isIs_user()) {
                        if (SecurityUtils.getSubject().getPrincipal() == null) {
                            // 用户没有登录
                            returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.NOTLOGIN.getValue(), "请先登录!");
                        } else {
                            returnJson = new HttpClient(req, res).sendByGet(permission.getAddress() + permission.getUrl(), SecurityUtil.getUserId());
                        }
                    } else {
                        returnJson = new HttpClient(req, res).sendByGet(permission.getAddress() + permission.getUrl(), null);
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
    public void transPict(MultipartFile[] files, HttpServletResponse res) {
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
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * app更新
     *
     * @param res
     */
    @RequestMapping("update")
    public void update(HttpServletResponse res) {
        try {
            permissionService.update();
            res.getWriter().write(SendAppJSONUtil.getNormalString("清除缓存成功!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}