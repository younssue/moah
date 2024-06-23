package org.dessert.moah.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.service.CommonService;
import org.dessert.moah.base.type.SuccessCode;
import org.dessert.moah.dto.ExampleDto;
import org.dessert.moah.dto.LoginRequestDto;
import org.dessert.moah.dto.LoginResponseDto;
import org.dessert.moah.dto.SignupRequestDto;
import org.dessert.moah.entity.UserRoleEnum;
import org.dessert.moah.entity.Users;
import org.dessert.moah.jwt.JwtUtil;
import org.dessert.moah.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CommonService commonService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    // ADMIN_TOKEN
//    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public CommonResponseDto<Object> signup(SignupRequestDto requestDto) {
        String username = requestDto.getName();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<Users> checkUsername = userRepository.findByName(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<Users> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
//        if (requestDto.isAdmin()) {
//            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
//                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
//            }
//            role = UserRoleEnum.ADMIN;
//        }

        // 사용자 등록
        Users user = Users.builder()
                          .email(requestDto.getEmail())
                          .name(requestDto.getName())
                          .password(password)
                          .phoneNumber(requestDto.getPhoneNumber())
                          .role(role)
                          .build();
        userRepository.save(user);
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }


    // 반환값이 있을 때
    public CommonResponseDto<Object> login(LoginRequestDto requestDto, HttpServletResponse res) {


        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 사용자 확인
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        jwtUtil.addJwtToCookie(token, res);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .accessToken(token)
                                                            .build();

        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, loginResponseDto);
    }


//    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
//        String email = requestDto.getEmail();
//        String password = requestDto.getPassword();
//
//        // 사용자 확인
//        Users user = userRepository.findByEmail(email).orElseThrow(
//                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//
//        // 비밀번호 확인
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
//        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
//        jwtUtil.addJwtToCookie(token, res);
//
//        return token;  // JWT 토큰 반환
//    }
}
