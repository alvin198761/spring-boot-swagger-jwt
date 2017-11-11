package org.alvin.swagger.jwt.system.bean;

/**
 * 模拟一个用户实体对象
 */
public class UserBean {

    private Long id;
    private String name;
    private String password;
    private Integer auth;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }
}
