package org.dessert.moah.user.service;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.user.entity.Users;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       // Users userData = userRepository.findByName(username);
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(user);

    }
}
