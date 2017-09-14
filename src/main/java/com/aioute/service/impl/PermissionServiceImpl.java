package com.aioute.service.impl;

import com.aioute.dao.PermissionDao;
import com.aioute.model.Permission;
import com.aioute.service.PermissionService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class PermissionServiceImpl implements PermissionService {

    public static Map<String, Permission> urlMap = new TreeMap<String, Permission>();

    @Resource
    private PermissionDao permissionDao;

    public Permission getUrlByType(String type) {
        Permission permission = urlMap.get(type);
        if (permission != null) {
            return permission;
        }
        return permissionDao.getUrlByType(type);
    }
}
