package com.storyhasyou.oauth2.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author fangxi created by 2021/1/3
 */
@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    private Long id;
    private String username;
    private String password;

}
