package org.dessert.moah.service.user;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.dto.CustomUserDetails;
import org.dessert.moah.entity.user.Users;
import org.dessert.moah.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users userData = userRepository.findByName(username);
        if(userData != null){
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
