package com.web.movie.service.implement;

import com.web.movie.entity.User;
import com.web.movie.repository.UserRepository;
import com.web.movie.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(()-> new UsernameNotFoundException("Not found user with email = " + email));
        return new CustomUserDetails(user);
    }
}
