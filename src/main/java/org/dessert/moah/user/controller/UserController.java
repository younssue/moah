package org.dessert.moah.user.controller;


import lombok.RequiredArgsConstructor;
import org.dessert.moah.common.dto.CommonResponseDto;
import org.dessert.moah.common.dto.ResultDto;
import org.dessert.moah.user.dto.CustomUserDetails;
import org.dessert.moah.user.dto.SignupRequestDto;
import org.dessert.moah.user.dto.UpdateUserInfoRequestDto;
import org.dessert.moah.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResultDto<Void>> signup(@RequestBody SignupRequestDto signUpRequestDto) {
        CommonResponseDto<Object> commonResponseDto = userService.signup(signUpRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }



    @PutMapping
    public ResponseEntity<ResultDto<Void>> updateUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto) {

        CommonResponseDto<Object> commonResponseDto = userService.updateUserInfo(customUserDetails, updateUserInfoRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                             .body(resultDto);
    }
}
