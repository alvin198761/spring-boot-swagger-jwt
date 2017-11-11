package org.alvin.swagger.jwt.system.web.acl;

import org.alvin.swagger.jwt.system.bean.MenuBean;
import org.alvin.swagger.jwt.system.bean.UserBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * jwt 鉴权用户
 */
public class JWTUserSessionSubject extends User {

    private UserBean user;
    private List<MenuBean> menus;

    public JWTUserSessionSubject(String username, String password, Collection<? extends GrantedAuthority> authorities,
                                 UserBean user, List<MenuBean> menus) {
        super(username, password, authorities);
        this.user = user;
        this.menus = menus;
    }


    public UserBean getUserBean() {
        return user;
    }

    public List<MenuBean> getMenus() {
        return menus;
    }
}
