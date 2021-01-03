package com.storyhasyou.oauth2.server.dao;

import com.storyhasyou.oauth2.server.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author fangxi created by 2021/1/3
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

}
