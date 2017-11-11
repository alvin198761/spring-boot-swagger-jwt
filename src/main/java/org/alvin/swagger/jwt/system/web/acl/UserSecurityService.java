package org.alvin.swagger.jwt.system.web.acl;


import com.google.common.collect.Lists;
import org.alvin.swagger.jwt.system.bean.MenuBean;
import org.alvin.swagger.jwt.system.bean.UserBean;
import org.alvin.swagger.jwt.system.service.MenuService;
import org.alvin.swagger.jwt.system.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Component
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserDaoService userDaoService;
    @Autowired
    private MenuService menuService;

    /**
     * @param user
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        //正式环境权限认证 模拟数据库获取数据库
        UserBean userBean = this.userDaoService.findByLoginName(user);
        Assert.notNull(userBean, "userBean must not be null");
        List<MenuBean> menus = this.menuService.findByMenu(userBean.getId());
        Assert.notNull(menus ,"menus must not be null");

        //模拟权限
        switch (userBean.getAuth()) {
            case 1:
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
            case 2:
                grantedAuthorities.add(new SimpleGrantedAuthority("MANAGER"));
            case 3:
                grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            case 4:
                grantedAuthorities.add(new SimpleGrantedAuthority("GUEST"));
        }
        return new JWTUserSessionSubject(userBean.getName(), userBean.getPassword(), grantedAuthorities,   userBean, menus);
    }

}
