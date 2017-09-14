package com.aioute.util;

import com.cloopen.rest.sdk.CCPRestSDK;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Set;

public class MessageSendUtil {

    private static Logger logger = Logger.getLogger(MessageSendUtil.class);

    public static boolean MessageSend(String phone, String code) {
        HashMap<String, Object> result = null;
        CCPRestSDK restAPI = new CCPRestSDK();

        restAPI.init("app.cloopen.com", "8883");
        restAPI.setAccount("8a48b5515350d1e201535f06420d182f", "1a12475a6a0045368fac6c86c962a478");
        restAPI.setAppId("aaf98f895376c19601537808b3e400b8");

        result = restAPI.sendTemplateSMS(phone, "73129", new String[]{code, "5"});
        logger.info("SDKTestGetSubAccounts result=" + result);
        if ("000000".equals(result.get("statusCode"))) {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {

            }
            return true;
        }
        return false;
    }
}
