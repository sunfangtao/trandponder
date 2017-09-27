package com.aioute.service;

import com.aioute.dao.PushDao;
import com.aioute.model.PushModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class PushServiceImpl implements PushService {

    @Resource
    private PushDao pushDao;

    public List<PushModel> getPushList(String userId, String pushType, int page, int pageSize) {
        return pushDao.getPushList(userId, pushType, page, pageSize);
    }
}
