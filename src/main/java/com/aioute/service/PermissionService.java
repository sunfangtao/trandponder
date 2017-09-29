package com.aioute.service;

import com.aioute.model.Permission;

import java.util.List;

public interface PermissionService {

    /**
     * 获取对应的url
     *
     * @param type
     * @return
     */
    public Permission getUrlByType(String type);

    public List<Permission> getAllAppPermission();

    public void update();
}
