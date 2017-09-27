package com.aioute.service.impl;

import com.aioute.dao.VersionDao;
import com.aioute.model.VersionModel;
import com.aioute.service.VersionService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class VersionServiceImpl implements VersionService {

    @Resource
    private VersionDao versionDao;

    public VersionModel getVersionInfo() {
        return versionDao.getVersionInfo();
    }
}
