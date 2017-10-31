package com.aioute.shiro.credentials;

import com.aioute.shiro.UserNamePasswordToken;
import com.sft.password.DefaultPasswordEncoder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户身份认证
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    private DefaultPasswordEncoder defaultPasswordEncoder;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        if (info instanceof SaltedAuthenticationInfo) {
            ByteSource salt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
            if (token instanceof UserNamePasswordToken) {
                UserNamePasswordToken token2 = (UserNamePasswordToken) token;
                if (token2.isThirdLogin()) {
                    return true;
                }
                String loginPassword = defaultPasswordEncoder.encode(new String(token2.getPassword()));
                if (loginPassword.equals(info.getCredentials())) {
                    return true;
                }
            }
        }
        return false;
    }
}
