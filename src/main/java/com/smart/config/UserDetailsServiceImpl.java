package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserReposetory;
import com.smart.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserReposetory userReposetory;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userReposetory.getUserByUserName(username);

    if (user == null) {
      throw new UsernameNotFoundException("Culd not found user !!");

    }
    CustomUserDetails customUserDetails = new CustomUserDetails(user);
    return customUserDetails;
  }

}
