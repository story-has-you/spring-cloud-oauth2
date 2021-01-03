package com.storyhasyou.oauth2.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 该配置类，主要处理⽤户名和密码的校验等事宜
 *
 * @author fangxi created by 2021/1/3
 */
@SpringBootConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService jdbcUserDetailsService;



    /**
     * 注册⼀个认证管理器对象到容器
     *
     * @return authentication manager
     * @throws Exception the exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 处理⽤户名和密码验证事宜
     * 1）客户端传递username和password参数到认证服务器
     * 2）⼀般来说，username和password会存储在数据库中的⽤户表中
     * 3）根据⽤户表中数据，验证当前传递过来的⽤户信息的合法性
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jdbcUserDetailsService).passwordEncoder(passwordEncoder());
    }
    /**
     * 配置密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 设置默认的加密方式
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
