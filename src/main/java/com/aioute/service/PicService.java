package com.aioute.service;

import java.util.List;

public interface PicService {

    public boolean addPic(String uid, List<String> picList);

    public List<String> getPics(String uid);
}
