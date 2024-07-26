package org.dessert.moah.user.service;

import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.service.CommonService;
import org.dessert.moah.common.type.ErrorCode;
import org.dessert.moah.common.type.SuccessCode;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.user.dto.SignupRequestDto;
import org.dessert.moah.user.dto.UpdateUserInfoRequestDto;
import org.dessert.moah.user.type.UserRoleEnum;
import org.dessert.moah.user.entity.Users;
//import org.dessert.moah.jwt.JwtUtil;
import org.dessert.moah.common.exception.NotFoundException;
import org.dessert.moah.common.jwt.JWTUtil;
import org.dessert.moah.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommonService commonService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;


    @Transactional
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
                          .address(requestDto.getAddress())
                          .role(UserRoleEnum.USER)
                          .build();
//
//        Users user = Users.signup(requestDto.getName(),requestDto.getPassword(),requestDto.getEmail(),requestDto.getPhoneNumber(),requestDto.getAddress(),UserRoleEnum.USER);

        userRepository.save(user);
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }


    @Transactional
    public CommonResponseDto<Object> updateUserInfo(CustomUserDetails customUserDetails, UpdateUserInfoRequestDto updateUserInfoRequestDto) {

        String email = customUserDetails.getEmail();
        Users user = userRepository.findByEmail(email)
                                   .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));


        user.UpdateUserInfo(updateUserInfoRequestDto.getPhoneNumber(), updateUserInfoRequestDto.getAddress());


        return commonService.successResponse(SuccessCode.USER_UPDATE_INFO.getDescription(), HttpStatus.OK, null);
    }

}