package org.dessert.moah.service;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.service.CommonService;
import org.dessert.moah.base.type.SuccessCode;
import org.dessert.moah.dto.SignupRequestDto;
import org.dessert.moah.entity.type.UserRoleEnum;
import org.dessert.moah.entity.Users;
//import org.dessert.moah.jwt.JwtUtil;
import org.dessert.moah.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommonService commonService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    public CommonResponseDto<Object> signup(SignupRequestDto requestDto) {
        String username = requestDto.getName();
        String password = bCryptPasswordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Users checkUsername = userRepository.findByName(username);
        if (checkUsername != null) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<Users> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }


        // 사용자 등록
        Users user = Users.builder()
                          .email(requestDto.getEmail())
                          .name(requestDto.getName())
                          .password(password)
                          .phoneNumber(requestDto.getPhoneNumber())
                          .role(UserRoleEnum.USER)
                          .build();
        userRepository.save(user);
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }



}
