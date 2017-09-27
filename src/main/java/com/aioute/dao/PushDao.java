package com.aioute.dao;

import com.aioute.model.PushModel;

import java.util.List;

public interface PushDao {
    public List<PushModel> getPushList(String userId, String pushType, int page, int pageSize);
}
