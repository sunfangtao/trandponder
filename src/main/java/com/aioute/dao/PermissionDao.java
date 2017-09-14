package com.aioute.dao;

import com.aioute.model.Permission;

public interface PermissionDao {

    /**
     * 获取对应的url
     *
     * @param type
     * @return
     */
    public Permission getUrlByType(String type);
}
