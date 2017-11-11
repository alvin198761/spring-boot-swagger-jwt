package org.alvin.swagger.jwt.system.web.controllers;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.alvin.swagger.jwt.system.bean.SwaggerResponse;
import org.alvin.swagger.jwt.system.bean.UserBean;
import org.alvin.swagger.jwt.system.service.UserDaoService;
import org.alvin.swagger.jwt.system.web.acl.JWTUserSessionSubject;
import org.alvin.swagger.jwt.system.web.acl.JwtAuthService;
import org.alvin.swagger.jwt.system.web.acl.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private JwtAuthService jwtAuthService;
    @Autowired
    private UserDaoService userDaoService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "手机用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @ResponseStatus(HttpStatus.OK)
    public SwaggerResponse<String> login(@ApiParam(value = "name", example = "alvin001") String name,
                                         @ApiParam("password") String password) {
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(name, password);
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = this.userSecurityService.loadUserByUsername(name);
            final String token = jwtAuthService.generateToken((JWTUserSessionSubject) userDetails);
            return new SwaggerResponse(token);
        } catch (Exception e) {
            e.printStackTrace();
            return new SwaggerResponse(false, "查找出错");
        }
    }


    /**
     * 执行某个方法，来验证token 是否有效
     *
     * @param id
     * @return 该用户的信息
     */
    @RequestMapping(value = "findById/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "无密码登录", notes = "返回的是用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "手机号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "安全控制Token,在header 里带参数,格式：Bearer+(登录时产生的token,注意 ‘+’也是组成部分)", required = true, dataType = "string", paramType = "header")

    })
    @ResponseStatus(HttpStatus.OK)
    public SwaggerResponse findByPhone(@PathVariable("id") Long id) throws Exception {
        UserBean userBean = this.userDaoService.findById(id);
        if (userBean == null) {
            throw new Exception();
        }
        return new SwaggerResponse(userBean);
    }
}
