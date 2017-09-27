package com.aioute.service.impl;

import com.aioute.dao.CodeDao;
import com.aioute.model.CodeModel;
import com.aioute.service.CodeService;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class CodeServiceImpl implements CodeService {

    @Resource
    private CodeDao codeDao;

    public boolean addCode(CodeModel codeModel, boolean isUpdate) {
        return codeDao.addCode(codeModel, isUpdate);
    }

    public boolean updateCode(CodeModel codeModel) {
        return codeDao.updateCode(codeModel);
    }

    public CodeModel getCodeByPhone(String phone) {
        return codeDao.getCodeByPhone(phone);
    }

    public String verifyCodeByPhone(String phone, String code) {
        if (!StringUtils.hasText(phone)) {
            return "请输入手机号!";
        }
        if (!StringUtils.hasText(code)) {
            return "请输入验证码!";
        }
        return codeDao.verifyCodeByPhone(phone, code);
    }
}
