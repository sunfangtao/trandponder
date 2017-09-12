package com.aioute.shiro.credentials;

import com.aioute.shiro.password.PasswordHelper;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户身份认证
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        if (info instanceof SaltedAuthenticationInfo) {
            ByteSource salt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
            if (token instanceof UsernamePasswordToken) {
                UsernamePasswordToken token2 = (UsernamePasswordToken) token;
                String loginPassword = passwordHelper.encryptPassword(new String(salt.getBytes()),
                        new String(token2.getPassword()));
                if (loginPassword.substring(32).equals(info.getCredentials())) {
                    return true;
                }
            }
        }

        return false;
    }
}
