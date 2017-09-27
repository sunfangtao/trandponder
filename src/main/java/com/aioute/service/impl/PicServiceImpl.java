package com.aioute.service.impl;

import com.aioute.dao.PicDao;
import com.aioute.service.PicService;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class PicServiceImpl implements PicService {

    @Resource
    private PicDao picDao;

    public boolean addPic(String uid, List<String> picList) {
        int length = picList.size();
        List<String> idList = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            idList.add(UUID.randomUUID().toString());
        }
        return picDao.addPic(uid, picList, idList);
    }

    public List<String> getPics(String uid) {
        if(!StringUtils.hasText(uid)){
            return null;
        }
        return picDao.getPics(uid);
    }
}
