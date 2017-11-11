package org.alvin.swagger.jwt.system.service;

import org.alvin.swagger.jwt.system.bean.UserBean;
import org.springframework.stereotype.Service;

/**
 * 模拟用户业务
 */
@Service
public class UserDaoService {

    public UserBean findByLoginName(String user) {
        UserBean userBean = new UserBean();
        userBean.setId(1L);
        userBean.setName(user);
        userBean.setPassword("1111111");
        userBean.setAuth(1);
        return userBean;
    }


    public UserBean findById(Long id) {
        return null;
    }
}
