package com.storyhasyou.oauth2.client.controller;

import com.storyhasyou.kratos.result.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangxi created by 2021/1/3
 */
@RestController
public class ClientController {

    @GetMapping("/client1/hello")
    public Result<Object> hello() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails() ;
        return Result.ok(details);
    }

    @GetMapping("/hi")
    public Result<String> hi() {
        return Result.ok("hi");
    }

}
