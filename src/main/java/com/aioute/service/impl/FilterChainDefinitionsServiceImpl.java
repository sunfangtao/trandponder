package com.aioute.service.impl;

import com.aioute.chain.ShiroPermissionFactory;
import com.aioute.model.Permission;
import com.aioute.service.FilterChainDefinitionsService;
import com.aioute.service.PermissionService;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FilterChainDefinitionsServiceImpl implements FilterChainDefinitionsService {

    @Autowired
    private ShiroPermissionFactory permissFactory;
    @Autowired
    private PermissionService permissionService;

    public void reloadFilterChains() {

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
    }
}
