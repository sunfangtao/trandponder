package com.aioute.util;

import org.apache.shiro.SecurityUtils;

public class SecurityUtil {

    public static String getUserId() {
        return (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
    }
}
