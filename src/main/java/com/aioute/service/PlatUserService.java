/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.aioute.service;

import com.aioute.model.PlatUserModel;
import com.aioute.model.UserLevelModel;
import com.aioute.model.bean.PlatUserBean;

import java.util.List;

/**
 * 管理员表Service
 *
 * @author jxh
 * @version 2017-09-01
 */
public interface PlatUserService {

    /**
     * 获取详情
     *
     * @param userId
     * @return
     */
    public PlatUserModel getDetail(String userId);

    /**
     * 筛选用户
     *
     * @param sortType
     * @param userLevel
     * @param serverId
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