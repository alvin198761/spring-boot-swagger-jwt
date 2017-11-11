package org.alvin.swagger.jwt.system.web.acl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/8/22.
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private JwtAuthService jwtAuthService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UserAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 恢复回话
        if (authentication.getClass().isAssignableFrom(PreAuthenticatedAuthenticationToken.class) && authentication.getPrincipal() != null) {
            String tokenHeader = (String) authentication.getPrincipal();
            UserDetails userDetails = parseToken(tokenHeader);
            if (userDetails != null) {
                return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());  //授权
            }
            return null;
        }
        //从数据库找到的用户
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();
        UserDetails userDetails = null;
        if (username != null) {
            userDetails = this.userSecurityService.loadUserByUsername(username);
        }
        //数据库用户的密码
        String password = userDetails.getPassword();
        //与authentication里面的credentials相比较
        if (!password.equals(token.getCredentials())) {
            throw new BadCredentialsException("Invalid username/password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());  //授权
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //返回true后才会执行上面的authenticate方法,这步能确保authentication能正确转换类型
        return UsernamePasswordAuthenticationToken.class.equals(authentication) || authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class);
    }

    private UserDetails parseToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            String userAccount = jwtAuthService.getUserAccountFromToken(authToken);
            if (userAccount != null && SecurityContextHolder.getContext().getAuthentication() == null) {//token校验通过
                return this.userSecurityService.loadUserByUsername(userAccount);//根据account去数据库中查询user数据，足够信任token的情况下，可以省略这一步
            }
        }
        return null;
    }

}