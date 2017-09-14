package com.aioute.controller;

import com.aioute.model.CodeModel;
import com.aioute.service.CodeService;
import com.aioute.util.CloudError;
import com.aioute.util.DateUtil;
import com.aioute.util.MessageSendUtil;
import com.aioute.util.SendAppJSONUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("code")
public class CodeController {

    private static Logger logger = Logger.getLogger(CodeController.class);
    @Resource
    private CodeService codeService;

    /**
     * 生成验证码
     *
     * @param res
     */
    @RequestMapping("create")
    public void createCode(HttpServletResponse res, String phone) {
        try {
            String returnJson = null;
            if (StringUtils.hasText(phone)) {
                String code = "" + (int) ((Math.random() * 9 + 1) * 100000);
                CodeModel codeModel = new CodeModel();
                codeModel.setCode(code);
                codeModel.setCreate_date(DateUtil.getCurDate());
                codeModel.setPhone(phone);

                CodeModel existModel = codeService.getCodeByPhone(phone);

                if (existModel != null
                        && (DateUtil.getTime(codeModel.getCreate_date()) - DateUtil.getTime(existModel.getCreate_date())) < existModel.getDuration() * 1000 * 60) {
                    returnJson = SendAppJSONUtil.getNormalString("验证码发送成功!");
                } else {
                    boolean isUpdate = false;
                    if (existModel != null) {
                        isUpdate = true;
                    }
                    if (codeService.addCode(codeModel, isUpdate)) {
                        // 发送验证码
                        if (MessageSendUtil.MessageSend(phone, code)) {
                            returnJson = SendAppJSONUtil.getNormalString("验证码发送成功!");
                        } else {
                            returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.IOException.getValue(), "发送失败!");
                        }
                    } else {
                        returnJson = SendAppJSONUtil.getFailResultObject(CloudError.ReasonEnum.SQLEXCEPTION.getValue(), "获取失败!");
                    }
                }
            } else {
                returnJson = SendAppJSONUtil.getRequireParamsMissingObject("请输入手机号!");
            }
            logger.info("生成验证码：" + returnJson);
            res.getWriter().write(returnJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
