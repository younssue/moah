package org.dessert.moah.controller.user;


import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.dto.ResultDto;
import org.dessert.moah.dto.CustomUserDetails;
import org.dessert.moah.dto.user.SignupRequestDto;
import org.dessert.moah.dto.user.UpdateUserInfoRequestDto;
import org.dessert.moah.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.apache.naming.ResourceRef.AUTH;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResultDto<Void>> signup(@RequestBody SignupRequestDto signUpRequestDto) {
        CommonResponseDto<Object> commonResponseDto = userService.signup(signUpRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }



    @PutMapping("/update")
    public ResponseEntity<ResultDto<Void>> updateUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto) {

        CommonResponseDto<Object> commonResponseDto = userService.updateUserInfo(customUserDetails, updateUserInfoRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }
}
