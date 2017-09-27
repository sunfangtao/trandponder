package com.aioute.service;

import com.aioute.model.PushModel;

import java.util.List;

public interface PushService {

    public List<PushModel> getPushList(String userId, String pushType, int page, int pageSize);

}
