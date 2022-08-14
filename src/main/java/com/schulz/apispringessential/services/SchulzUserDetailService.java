package com.schulz.apispringessential.services;

import com.schulz.apispringessential.repositories.SchulzUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchulzUserDetailService implements UserDetailsService {

    private final SchulzUserRepository schulzUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(schulzUserRepository.findSchulzUserByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Schulz User not found!"));
    }
}
