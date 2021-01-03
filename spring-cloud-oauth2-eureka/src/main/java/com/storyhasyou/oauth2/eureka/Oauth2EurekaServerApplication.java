package com.storyhasyou.oauth2.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author fangxi created by 2021/1/3
 */
@EnableEurekaServer
@SpringBootApplication
public class Oauth2EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2EurekaServerApplication.class, args);
    }

}