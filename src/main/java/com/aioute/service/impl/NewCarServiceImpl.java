package com.aioute.service.impl;

import com.aioute.dao.NewCarDao;
import com.aioute.model.NewCarModel;
import com.aioute.service.NewCarService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class NewCarServiceImpl implements NewCarService {

    @Resource
    private NewCarDao newCarDao;

    public NewCarModel getDetail(String id) {
        return newCarDao.getDetail(id);
    }

    public List<NewCarModel> queryList(String userId, int page, int pageSize) {
        return newCarDao.queryList(userId, page, pageSize);
    }
}
