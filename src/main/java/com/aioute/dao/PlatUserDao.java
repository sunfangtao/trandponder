package com.aioute.dao;

import com.aioute.model.PlatUserModel;
import com.aioute.model.UserLevelModel;
import com.aioute.model.bean.PlatUserBean;

import java.util.List;

public interface PlatUserDao {

    public PlatUserModel getDetail(String userId);

    /**
     * 筛选用户
     *
     * @param sortType
     * @param userLevel
     * @return
     */
    public List<PlatUserBean> queryList(String sortType, String userLevel, String serverId, double latitude, double longtitude, int page, int pageSize);

    /**
     * 获取用户
     *
     * @return
     */
    public List<UserLevelModel> getLevels();
}
