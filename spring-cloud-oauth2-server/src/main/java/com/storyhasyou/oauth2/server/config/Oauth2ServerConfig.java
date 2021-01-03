package com.storyhasyou.oauth2.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * 当前类为Oauth2 server的配置类（需要继承特定的⽗类 AuthorizationServerConfigurerAdapter）
 *
 * @author fangxi created by 2021/1/3
 */
@SpringBootConfiguration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String SING_KEY = "story-has-you";


    /**
     * The Authentication manager.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    /**
     * 认证服务器最终是以api接⼝的⽅式对外提供服务（校验合法性并⽣成令牌、 校验令牌等）
     * 那么，以api接⼝⽅式对外的话，就涉及到接⼝的访问权限，我们需要在这⾥ 进⾏必要的配置.
     *
     * @param security the security
     * @throws Exception the exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 相当于打开endpoints 访问接⼝的开关，这样的话后期我们能够访问 该接⼝
        security
                // 允许表单形式提交进行认证
                .allowFormAuthenticationForClients()
                // 开启端⼝/oauth/token_key的访问权限（允许）
                .tokenKeyAccess("permitAll()")
                // 开启端⼝/oauth/check_token的访问权限（允许）
                .checkTokenAccess("permitAll()");
    }

    /**
     * 客户端详情配置. 比如client_id、secret
     * 当前这个服务就如同QQ平台，拉勾⽹作为客户端需要qq平台进⾏登录授权认 证等，提前需要到QQ平台注册，QQ平台会给拉勾⽹
     * 颁发client_id等必要参数，表明客户端是谁
     *
     * @param clients the clients
     * @throws Exception the exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 基于JDBC改造
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * 认证服务器是玩转token的，那么这⾥配置token令牌管理相关
     * (token此时 就是⼀个字符串，当下的token需要在服务器端存储，那么存储在哪⾥呢？都是在这⾥配置)
     *
     * @param endpoints the endpoints
     * @throws Exception the exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 指定token存储方式
        endpoints.tokenStore(tokenStore())
                // token服务的一个描述，可以认为是token生成细节的描述，比如有效时间等
                .tokenServices(authorizationServerTokenServices())
                // 指定一个认证管理器，随后注入一个到当前使用类即可
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET);

    }

    /**
     * 该⽅法⽤于创建tokenStore对象（令牌存储对象）
     * token以什么形式存储
     *
     * @return token store
     */
    @Bean
    public TokenStore tokenStore() {
        // return new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 返回jwt令牌转换器，生成jwt令牌
     * 在这⾥，我们可以把签名密钥传递进去给转换器对象
     * @return the jwt access token converter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 签名密钥
        jwtAccessTokenConverter.setSigningKey(SING_KEY);
        // 验证时使⽤的密钥，和签名密钥保持⼀致, MacSigner: 对称加密
        jwtAccessTokenConverter.setVerifier(new MacSigner(SING_KEY));
        return jwtAccessTokenConverter;
    }


    /**
     * 该⽅法⽤户获取⼀个token服务对象（该对象描述了token有效期等信息）
     *
     * @return the authorization server token services
     */
    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 是否开启令牌刷新
        defaultTokenServices.setSupportRefreshToken(true);
        // 指定token存储
        defaultTokenServices.setTokenStore(tokenStore());
        // 针对jwt令牌添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        // 设置令牌有效时间, 一般2个小时
        defaultTokenServices.setAccessTokenValiditySeconds(60);
        // 设置刷新令牌有效时间, 3天
        defaultTokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return defaultTokenServices;
    }

    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }



}
