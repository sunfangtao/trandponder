package com.aioute.dao;

import com.aioute.model.FavouriteModel;

import java.util.List;

public interface FavouriteDao {

    public boolean addFavourite(FavouriteModel favouriteModel);

    public List<FavouriteModel> getFavourite(String userId, String type,int page, int pageSize);
}