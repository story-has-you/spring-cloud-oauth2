package com.storyhasyou.oauth2.server.config;

import com.google.common.collect.Lists;
import com.storyhasyou.oauth2.server.dao.UserRepository;
import com.storyhasyou.oauth2.server.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author fangxi created by 2021/1/3
 */
@Service("jdbcUserDetailsService")
public class JdbcUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);
        return new User(users.getUsername(), users.getPassword(), Lists.newArrayList());
    }
}
