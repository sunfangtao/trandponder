package com.aioute.service;

import com.aioute.model.ActivityModel;

import java.util.List;

public interface ActivityService {

    public List<ActivityModel> queryList(int page, int pageSize);

    public List<ActivityModel> queryUserActivityList(String userId);
}
