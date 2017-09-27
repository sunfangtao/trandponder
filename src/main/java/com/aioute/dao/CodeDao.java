package com.aioute.dao;

import com.aioute.model.CodeModel;

public interface CodeDao {

    public boolean addCode(CodeModel codeModel,boolean isUpdate);

    public boolean updateCode(CodeModel codeModel);

    public CodeModel getCodeByPhone(String phone);

    public String verifyCodeByPhone(String phone, String code);
}
