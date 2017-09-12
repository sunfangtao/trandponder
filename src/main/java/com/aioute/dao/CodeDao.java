package com.aioute.dao;

import com.aioute.model.CodeModel;

public interface CodeDao {

    public CodeModel addCode(CodeModel codeModel);

    public boolean updateCode(CodeModel codeModel);

    public CodeModel getCodeByPhone(String phone);
}
