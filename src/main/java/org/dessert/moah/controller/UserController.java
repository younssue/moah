package org.dessert.moah.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dessert.moah.dto.LoginRequestDto;
import org.dessert.moah.dto.SignupRequestDto;
import org.dessert.moah.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public String signup(@RequestBody SignupRequestDto signUpRequestDto){
        userService.signup(signUpRequestDto);
        return "success signup";
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res){
        try {
            String token = userService.login(loginRequestDto, res);
            Map<String, String> response = new HashMap<>();
            response.put("message", "success login");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
