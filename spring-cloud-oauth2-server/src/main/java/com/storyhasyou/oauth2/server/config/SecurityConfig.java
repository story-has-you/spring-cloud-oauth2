package com.storyhasyou.oauth2.server.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

/**
 * 该配置类，主要处理⽤户名和密码的校验等事宜
 *
 * @author fangxi created by 2021/1/3
 */
@SpringBootConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


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
        // 在这个⽅法中就可以去关联数据库了，当前我们先把⽤户信息配置在内存中
        // 实例化⼀个⽤户对象(相当于数据表中的⼀条⽤户记录)
        UserDetails user = new User("admin", "123456", new ArrayList<>());
        auth.inMemoryAuthentication()
                .withUser(user)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
