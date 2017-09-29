/**
 * @Project:crm
 * @Title:PasswordShiroRealm.java
 * @Author:Riozenc
 * @Datetime:2016年10月16日 下午8:04:07
 */
package com.aioute.shiro.realm;

import com.aioute.model.AppUserModel;
import com.aioute.model.CodeModel;
import com.aioute.service.AppUserService;
import com.aioute.service.CodeService;
import com.aioute.shiro.UserNamePasswordToken;
import com.aioute.shiro.password.DefaultPasswordEncoder;
import com.aioute.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.UUID;

public class PasswordShiroRealm extends AuthorizingRealm {

    private Logger logger = Logger.getLogger(PasswordShiroRealm.class);

    @Autowired
    private SessionManager sessionManager;
    @Resource
    private AppUserService userService;
    @Resource
    private CodeService codeService;
    @SuppressWarnings("unused")
    @Autowired
    private DefaultPasswordEncoder defaultPasswordEncoder;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        UserNamePasswordToken token = (UserNamePasswordToken) authenticationToken;
        String username = token.getUsername();

        if (sessionManager instanceof DefaultWebSessionManager) {
            DefaultWebSessionManager defaultWebSessionManager = (DefaultWebSessionManager) sessionManager;

            SecurityUtils.getSubject().logout();
            SessionDAO sessionDAO = defaultWebSessionManager.getSessionDAO();
            Collection<Session> sessions = sessionDAO.getActiveSessions();

            for (Session session : sessions) {
                // 清除该用户以前登录时保存的session
                if (token.getPrincipal().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                    sessionDAO.delete(session);
                    logger.info("已在线的用户：" + token.getPrincipal() + "被踢出");
                }
            }
        }

        if (!StringUtils.hasText(username)) {
            return null;
        }
        AppUserModel user = userService.getUserInfoByPhone(username);
        if (user == null) {
            // 用户没有注册，自动完成注册
            // 先验证验证码(密码)
            CodeModel codeModel = codeService.getCodeByPhone(username);
            if (codeModel == null) {
                return null;
            }
            long range = (DateUtil.getTime(DateUtil.getCurDate()) - DateUtil.getTime(codeModel.getCreate_date())) / 60 / 1000;
            if (range > codeModel.getDuration()) {
                return null;
            }

            String password = new String(token.getPassword());
            if (password.equals(codeModel.getCode())) {
                user = new AppUserModel();
                user.setId(UUID.randomUUID().toString());
                user.setLogin_name(username);
                user.setPassword(defaultPasswordEncoder.encode(password));
                user.setCreate_time(DateUtil.getCurDate());
                userService.addUser(user);
            }

        }
        String password = user.getPassword();
        if (!StringUtils.hasText(password)) {
            return null;
        }

        SecurityUtils.getSubject().getSession().setAttribute("userId", user.getId());
        return new SimpleAuthenticationInfo(username, password, getName());
    }

}
