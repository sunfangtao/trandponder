package com.aioute.dao;

import com.aioute.model.ActivityModel;

import java.util.List;

public interface ActivityDao {

    public List<ActivityModel> queryList(int page, int pageSize);

    public List<ActivityModel> queryUserActivityList(String userId);
}
