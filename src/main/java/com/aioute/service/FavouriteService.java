package com.aioute.service;

import com.aioute.model.FavouriteModel;

import java.util.List;

public interface FavouriteService {

    public boolean addFavourite(FavouriteModel favouriteModel);

    public List<FavouriteModel> getFavourite(String userId, String type, int page, int pageSize);
}
