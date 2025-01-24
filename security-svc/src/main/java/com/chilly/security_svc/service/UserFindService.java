package com.chilly.security_svc.service;

import com.chilly.security_svc.model.User;
import com.chilly.security_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.chilly.common.exception.NoSuchEntityException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFindService implements UserDetailsService {

    private final UserRepository userRepository;

    public User loadByPhoneOrByEmail(String username) {
        return userRepository.findByPhoneNumber(username)
                .orElseGet(() -> loadByEmailOrException(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadByPhoneOrByEmail(username);
    }

    private User loadByEmailOrException(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchEntityException("no user with phone or email = " + username));
    }
}
