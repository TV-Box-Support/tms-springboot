package com.vnptt.tms.security.services;

import com.vnptt.tms.entity.UserEntity;
import com.vnptt.tms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Check if the user exists in the database?
    UserEntity user = userRepository.findByUsername(username);
    if (user == null){
      throw new RuntimeException("User Not Found with username: " + username);
    }

    return UserDetailsImpl.build(user);
  }

}
