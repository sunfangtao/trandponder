package com.aioute.service.impl;

import com.aioute.dao.PlatUserDao;
import com.aioute.model.PlatUserModel;
import com.aioute.model.UserLevelModel;
import com.aioute.model.bean.PlatUserBean;
import com.aioute.service.PlatUserService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class PlatUserServiceImpl implements PlatUserService {

    @Resource
    private PlatUserDao platUserDao;

    public PlatUserModel getDetail(String userId) {
        return platUserDao.getDetail(userId);
    }

    public List<PlatUserBean> queryList(String sortType, String userLevel, String serverId, double latitude, double longtitude, int page, int pageSize) {
        return platUserDao.queryList(sortType, userLevel, serverId, latitude, longtitude, page, pageSize);
    }

    public List<UserLevelModel> getLevels() {
        return platUserDao.getLevels();
    }

}
