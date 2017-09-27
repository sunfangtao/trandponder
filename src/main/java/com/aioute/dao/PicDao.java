package com.aioute.dao;

import java.util.List;

public interface PicDao {

    public boolean addPic(String uid, List<String> picList,List<String> idList);

    public List<String> getPics(String uid);
}
