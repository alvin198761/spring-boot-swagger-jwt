package org.alvin.swagger.jwt.config;

import org.alvin.swagger.jwt.system.web.acl.JwtAuthenticationEntryPoint;
import org.alvin.swagger.jwt.system.web.acl.JwtAuthenticationTokenFilter;
import org.alvin.swagger.jwt.system.web.acl.UserAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;


    /**
     * 用户名密码认证方法
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userDetailsService); // 设置UserDetailsService
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider);
    }

    /**
     * 装载BCrypt密码编码器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * token请求授权
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()//未授权处理
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/test/test/**").permitAll()
                // swagger
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/application/**").permitAll()
                .antMatchers("/swagger**/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                //endpoint
                .antMatchers("/restart").permitAll()
                .antMatchers("/auditevents").permitAll()
                .antMatchers("/refresh").permitAll()
                .antMatchers("/dump").permitAll()
                .antMatchers("/env").permitAll()
                .antMatchers("/configprops").permitAll()
                .antMatchers("/loggers").permitAll()
                .antMatchers("/pause").permitAll()
                .antMatchers("/resume").permitAll()
                .antMatchers("/heapdump").permitAll()
                .antMatchers("/features").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/autoconfig").permitAll()
                .antMatchers("/mappings").permitAll()
                .antMatchers("/archaius").permitAll()
                .antMatchers("/jolokia").permitAll()
                .antMatchers("/info").permitAll()
                .antMatchers("/metrics").permitAll()
                .antMatchers("/beans").permitAll()
                .antMatchers("/shutdown").permitAll()
                .antMatchers("/service").permitAll()
                .antMatchers("/trace").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        // 添加JWT filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);  //将token验证添加在密码验证前面
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

}