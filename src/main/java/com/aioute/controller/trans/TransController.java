package com.aioute.controller.trans;

import com.aioute.chain.ShiroPermissionFactory;
import com.aioute.model.Permission;
import com.aioute.service.PermissionService;
import com.sft.util.*;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping("trans")
public class TransController {

    private static Logger logger = Logger.getLogger(TransController.class);

    @Resource
    private PermissionService permissionService;
    @Resource
    private ShiroPermissionFactory permissFactory;

    /**
     * app接口转发
     *
     * @param req
     * @param res
     */
    @RequestMapping("app")
    public void transPlat(HttpServletRequest req, HttpServletResponse res) {
        Lock lock = MtbhLock.getLock(SecurityUtil.getRemoteIP(req));
        lock.lock();
        try {
            String returnJson = null;
            String type = req.getParameter("type");
            StringBuffer sb = new StringBuffer();
            try {
                if (StringUtils.hasText(type)) {
                    Permission permission = permissionService.getUrlByType(type);
                    if (permission == null || !StringUtils.hasText(permission.getType())) {
                        returnJson = SendAppJSONUtil.getRequireParamsMissingObject("type错误!");
                    } else {
                        sb.append("ip=" + SecurityUtil.getRemoteIP(req) + " user=" + SecurityUtils.getSubject().getPrincipal() + " type=" + type);
                        Map<String, String[]> paramsMap = req.getParameterMap();
                        if (paramsMap != null) {
                            sb.append("\nURL: ").append(permission.getAddress() + permission.getUrl()).append("?");
                            StringBuffer sb2 = new StringBuffer();
                            for (String key : paramsMap.keySet()) {
                                sb2.append("&").append(key).append("=").append(paramsMap.get(key)[0]);
                            }
                            sb.append(sb2.substring(1));
                        }
                        logger.info(sb.toString());
                        if (permission.isIs_user()) {
                            if (SecurityUtils.getSubject().getPrincipal() == null) {
                                // 用户没有登录
                                returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.NOTLOGIN.getValue(), "请先登录!");
                            } else {
                                returnJson = new HttpClient(req, res, permission.isRedict()).sendByGet(permission.getAddress() + permission.getUrl(), SecurityUtil.getUserId(req));
                            }
                        } else {
                            returnJson = new HttpClient(req, res, permission.isRedict()).sendByGet(permission.getAddress() + permission.getUrl(), null);
                        }
                    }
                } else {
                    returnJson = SendAppJSONUtil.getRequireParamsMissingObject("没有type!");
                }

                if (returnJson == null) return;
                logger.info(returnJson);
                res.getWriter().write(returnJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
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
                if ((picUrl = FTPPicUtil.uploadPics(files)) != null) {
                    returnJson = SendAppJSONUtil.getNormalString(picUrl);
                } else {
                    returnJson = SendAppJSONUtil.getFailResultObject(Params.ReasonEnum.SQLEXCEPTION.getValue(), "上传失败!");
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
            synchronized (permissFactory) { // 强制同步，控制线程安全
                AbstractShiroFilter shiroFilter = null;
                try {
                    shiroFilter = (AbstractShiroFilter) permissFactory.getObject();
                    PathMatchingFilterChainResolver resolver = (PathMatchingFilterChainResolver) shiroFilter
                            .getFilterChainResolver();
                    // 过滤管理器
                    DefaultFilterChainManager manager = (DefaultFilterChainManager) resolver.getFilterChainManager();
                    // 清除权限配置
//                manager.getFilterChains().clear();
                    permissFactory.getFilterChainDefinitionMap().clear();
                    // 重新设置权限
                    permissFactory.setFilterChainDefinitions(ShiroPermissionFactory.definition);// 传入配置中的filterchains

                    Map<String, String> map = new HashMap<String, String>();
                    List<Permission> permissions = permissionService.getAllAppPermission();
                    // 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
                    // 里面的键就是链接URL,值就是存在什么条件才能访问该链接
                    if (permissions != null) {
                        for (Permission permission : permissions) {
                            // 如果不为空值添加到map中
                            if (StringUtils.hasText(permission.getUrl()) && StringUtils.hasText(permission.getUrl())) {
                                if (!permission.getUrl().startsWith("/")) {
                                    permission.setUrl("/" + permission.getUrl());
                                }
                                manager.createChain(permission.getUrl(), "user");
                                map.put(permission.getUrl(), "user");
                            }
                        }
                    }
                    map.put("/**", "anon");
                    permissFactory.setFilterChainDefinitionMap(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            permissionService.update();
            String returnJson = SendAppJSONUtil.getNormalString("资源权限更新成功!");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}