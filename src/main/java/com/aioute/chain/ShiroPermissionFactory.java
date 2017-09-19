package com.aioute.chain;

import com.aioute.model.Permission;
import com.aioute.service.PermissionService;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

public class ShiroPermissionFactory extends ShiroFilterFactoryBean {

    @Autowired
    private PermissionService permissionService;

    public static String definition;

    /**
     * 初始化设置过滤链
     */
    @Override
    public void setFilterChainDefinitions(String definitions) {
        definition = definitions;
        // 加载配置默认的过滤链
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }

        List<Permission> permissions = permissionService.getAllAppPermission();
        // 循环Resource的url,逐个添加到section中。section就是filterChainDefinitionMap,
        // 里面的键就是链接URL,值就是存在什么条件才能访问该链接
        if (permissions != null) {
            for (Iterator<Permission> it = permissions.iterator(); it.hasNext(); ) {
                Permission resource = it.next();
                // 如果不为空值添加到section中
                if (StringUtils.hasText(resource.getUrl()) && StringUtils.hasText(resource.getUrl())) {
                    if (!resource.getUrl().startsWith("/")) {
                        resource.setUrl("/" + resource.getUrl());
                    }
                    section.put(resource.getUrl(), "user");
                }
            }
        }
        section.put("/**", "anon");
        this.setFilterChainDefinitionMap(section);
    }

}  