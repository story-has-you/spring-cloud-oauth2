package com.storyhasyou.oauth2.client.config;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author fangxi created by 2021/1/3
 */
@Component
public class CustomizeAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ? > map) {
        OAuth2Authentication oAuth2Authentication = super.extractAuthentication(map);
        // 将map放⼊认证对象 中，认证对象在controller中可以拿到
        oAuth2Authentication.setDetails(map);
        return oAuth2Authentication;
    }

}
