package com.aioute.service;

import com.aioute.model.CodeModel;

public interface CodeService {

    public boolean addCode(CodeModel codeModel,boolean isUpdate);

    public boolean updateCode(CodeModel codeModel);

    public CodeModel getCodeByPhone(String phone);
}

