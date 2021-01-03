package com.storyhasyou.oauth2.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author fangxi created by 2021/1/3
 */
@SpringBootTest
public class Oauth2ServerApplicationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        System.out.println(passwordEncoder.encode("123456"));
    }

}
