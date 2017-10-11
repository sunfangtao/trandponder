package com.aioute.service.impl;

import com.aioute.dao.FavouriteDao;
import com.aioute.model.FavouriteModel;
import com.aioute.service.FavouriteService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class FavouriteServiceImpl implements FavouriteService {

    @Resource
    private FavouriteDao favouriteDao;

    public boolean addFavourite(FavouriteModel favouriteModel) {
        return favouriteDao.addFavourite(favouriteModel);
    }

    public List<FavouriteModel> getFavourite(String userId, String type,int page, int pageSize) {
        return favouriteDao.getFavourite(userId, type,page,pageSize);
    }
}