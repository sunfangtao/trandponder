package com.aioute.dao.impl;

import com.aioute.dao.FavouriteDao;
import com.aioute.db.SqlConnectionFactory;
import com.aioute.model.FavouriteModel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class FavouriteDaoImpl implements FavouriteDao {

    @Resource
    private SqlConnectionFactory sqlConnectionFactory;

    public boolean addFavourite(FavouriteModel favouriteModel) {
        return false;
    }

    public List<FavouriteModel> getFavourite(String userId, String type, int page, int pageSize) {
        return null;
    }
}
