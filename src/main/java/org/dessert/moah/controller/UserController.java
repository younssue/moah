package org.dessert.moah.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.dto.ResultDto;
import org.dessert.moah.dto.ExampleDto;
import org.dessert.moah.dto.LoginRequestDto;
import org.dessert.moah.dto.LoginResponseDto;
import org.dessert.moah.dto.SignupRequestDto;
import org.dessert.moah.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah")
public class UserController {

    private final UserService userService;

//    @PostMapping("/user/signup")
//    public String signup(@RequestBody SignupRequestDto signUpRequestDto){
//        userService.signup(signUpRequestDto);
//        return "success signup";
//    }

    @PostMapping("/user/signup")
    public ResponseEntity<ResultDto<Void>> signup(@RequestBody SignupRequestDto signUpRequestDto){
        CommonResponseDto<Object> commonResponseDto = userService.signup(signUpRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }
//
//    @PostMapping("/user/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res){
//        try {
//            String token = userService.login(loginRequestDto, res);
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "success login");
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }


    // 반환값이 있을 때
    @PostMapping("/user/login")
    public ResponseEntity<ResultDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res){
        CommonResponseDto<Object> commonResponseDto = userService.login(loginRequestDto, res);
        ResultDto<LoginResponseDto> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        resultDto.setData((LoginResponseDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }
}
