package com.aioute.dao;

import com.aioute.model.NewCarModel;

import java.util.List;

public interface NewCarDao {

    /**
     * 车详情
     *
     * @param id
     * @return
     */
    public NewCarModel getDetail(String id);

    /**
     * 条件查询
     *
     * @return
     */
    public List<NewCarModel> queryList(String userId, int page, int pageSize);
}
