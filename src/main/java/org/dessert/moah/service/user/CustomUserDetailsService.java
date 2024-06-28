package org.dessert.moah.service.user;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.type.ErrorCode;
import org.dessert.moah.dto.CustomUserDetails;
import org.dessert.moah.entity.user.Users;
import org.dessert.moah.exception.NotFoundException;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       // Users userData = userRepository.findByName(username);
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(user);

    }
}
