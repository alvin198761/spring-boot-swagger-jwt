package org.alvin.swagger.jwt.system.web.acl;

import com.alibaba.fastjson.JSONObject;
import org.alvin.swagger.jwt.system.bean.SwaggerResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * jwt 未授权
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if (authException instanceof BadCredentialsException) { /**身份认证未通过*/
            SwaggerResponse<?> resp = new SwaggerResponse<>(false,"用户名或密码错误，请重新输入！");
            response.getWriter().write(JSONObject.toJSONString(resp));
        } else {
            SwaggerResponse<?> resp = new SwaggerResponse<>(false,"无效的token！");
            response.getWriter().write(JSONObject.toJSONString(resp));
        }
    }
}