package com.aioute.service.impl;

import com.aioute.dao.SellCarDao;
import com.aioute.model.SellCarModel;
import com.aioute.service.SellCarService;
import com.sft.util.DateUtil;
import com.aioute.util.SecurityUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SellCarServiceImpl implements SellCarService {

    @Resource
    private SellCarDao sellCarDao;

    public boolean addSell(SellCarModel sellCarModel) {
        sellCarModel.setCreatedate(DateUtil.getCurDate());
        sellCarModel.setUserId(SecurityUtil.getUserId());
        sellCarModel.setUserType("0");
        return sellCarDao.addSell(sellCarModel);
    }

    public SellCarModel getDetail(String id) {
        return sellCarDao.getDetail(id);
    }

    public List<SellCarModel> queryList(String sortType, SellCarModel sellCarModel, String priceRange, int page, int pageSize) {
        return sellCarDao.queryList(sortType, sellCarModel, priceRange, page, pageSize);
    }
}
