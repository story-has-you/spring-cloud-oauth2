package com.storyhasyou.oauth2.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * The type Resources server config.
 *
 * @author fangxi created by 2021/1/3
 */
@EnableResourceServer
@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {

    private static final String CHECK_TOKEN_URL = "http://localhost:9999/oauth/check_token";
    private static final String SING_KEY = "story-has-you";

    @Autowired
    private CustomizeAccessTokenConverter customizeAccessTokenConverter;



    /**
     * 该⽅法⽤于定义资源服务器向远程认证服务器发起请求，进⾏token校验 等事宜
     *
     * @param resources the resources
     * @throws Exception the exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 设置当前资源服务的资源id, 要与认证服务器上配置的一致
        /*
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        // 校验端点设置
        remoteTokenServices.setCheckTokenEndpointUrl(CHECK_TOKEN_URL);
        // 携带客户端id和客户端安全码
        remoteTokenServices.setClientId("client_lagou");
        remoteTokenServices.setClientSecret("123456");
        resources.tokenServices(remoteTokenServices);
        */
        // jwt改造
        resources.resourceId("resource_1").tokenStore(tokenStore()).stateless(true);

    }


    /**
     * 场景：⼀个服务中可能有很多资源（API接⼝）
     * 1. 某⼀些API接⼝，需要先认证，才能访问
     * 2. 某⼀些API接⼝，压根就不需要认证，本来就是对外开放的接⼝
     * 3. 我们就需要对不同特点的接⼝区分对待（在当前configure⽅法中 完成），设置是否需要经过认证
     *
     * @param http the http
     * @throws Exception the exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 设置session的创建策略（根据需要创建即可）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                // client1开头需要验证
                .antMatchers("/client1/**").authenticated()
                // 其他请求不验证
                .anyRequest().permitAll();
    }


    /**
     * 该⽅法⽤于创建tokenStore对象（令牌存储对象）
     * token以什么形式存储
     *
     * @return token store
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 返回jwt令牌转换器，生成jwt令牌
     * 在这⾥，我们可以把签名密钥传递进去给转换器对象
     *
     * @return the jwt access token converter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 签名密钥
        jwtAccessTokenConverter.setSigningKey(SING_KEY);
        // 验证时使⽤的密钥，和签名密钥保持⼀致, MacSigner: 对称加密
        jwtAccessTokenConverter.setVerifier(new MacSigner(SING_KEY));
        jwtAccessTokenConverter.setAccessTokenConverter(customizeAccessTokenConverter);
        return jwtAccessTokenConverter;
    }
}
