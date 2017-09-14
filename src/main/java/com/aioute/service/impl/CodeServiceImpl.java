package com.aioute.service.impl;

import com.aioute.dao.CodeDao;
import com.aioute.model.CodeModel;
import com.aioute.service.CodeService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class CodeServiceImpl implements CodeService {

    @Resource
    private CodeDao codeDao;

    public boolean addCode(CodeModel codeModel,boolean isUpdate) {
        return codeDao.addCode(codeModel,isUpdate);
    }

    public boolean updateCode(CodeModel codeModel) {
        return codeDao.updateCode(codeModel);
    }

    public CodeModel getCodeByPhone(String phone) {
        return codeDao.getCodeByPhone(phone);
    }
}
