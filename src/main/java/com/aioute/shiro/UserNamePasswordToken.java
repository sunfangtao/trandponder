package com.aioute.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserNamePasswordToken extends UsernamePasswordToken {

    private boolean isThirdLogin = false;

    public UserNamePasswordToken(String username, String password, boolean isThirdLogin) {
        super(username, password);
        this.isThirdLogin = isThirdLogin;
    }

    public boolean isThirdLogin() {
        return isThirdLogin;
    }

    public void setThirdLogin(boolean thirdLogin) {
        isThirdLogin = thirdLogin;
    }
}
