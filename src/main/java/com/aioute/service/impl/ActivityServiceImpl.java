package com.aioute.service.impl;

import com.aioute.dao.ActivityDao;
import com.aioute.model.ActivityModel;
import com.aioute.service.ActivityService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;

    public List<ActivityModel> queryList(int page, int pageSize) {
        return activityDao.queryList(page, pageSize);
    }

    public List<ActivityModel> queryUserActivityList(String userId) {
        return activityDao.queryUserActivityList(userId);
    }
}
