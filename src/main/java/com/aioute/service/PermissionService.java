package com.aioute.service;

import com.aioute.model.Permission;

public interface PermissionService {

    /**
     * 获取对应的url
     *
     * @param type
     * @return
     */
    public Permission getUrlByType(String type);
}
