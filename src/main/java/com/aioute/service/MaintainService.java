/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.aioute.service;

import com.aioute.model.MaintainModel;
import com.aioute.model.SubMaintainModel;

import java.util.List;

/**
 * 门店服务Service
 *
 * @author jxh
 * @version 2017-08-31
 */
public interface MaintainService {

    /**
     * 查询所有服务模块
     *
     * @return
     */
    public List<MaintainModel> getAll();

    /**
     * 获取用户的服务模块
     *
     * @param userId
     * @return
     */
    public List<MaintainModel> getMaintain(String userId);

    /**
     * 获取服务模块的子服务列表
     *
     * @param id
     * @return
     */
    public List<SubMaintainModel> getSubMaintain(String userId,String id);

}