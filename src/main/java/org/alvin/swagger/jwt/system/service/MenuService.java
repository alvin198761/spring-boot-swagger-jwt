package org.alvin.swagger.jwt.system.service;

import org.alvin.swagger.jwt.system.bean.MenuBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    public List<MenuBean> findByMenu(Long id) {
        List<MenuBean> menus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MenuBean menuBean = new MenuBean();
            menuBean.setName("name" + i);
            menuBean.setIcon("icon" + i);
            menus.add(menuBean);
        }
        return menus;
    }
}
