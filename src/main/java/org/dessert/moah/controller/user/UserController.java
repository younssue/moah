package org.dessert.moah.controller.user;


import lombok.RequiredArgsConstructor;
import org.dessert.moah.base.dto.CommonResponseDto;
import org.dessert.moah.base.dto.ResultDto;
import org.dessert.moah.dto.user.SignupRequestDto;
import org.dessert.moah.dto.user.UpdateUserInfoRequestDto;
import org.dessert.moah.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moah")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<ResultDto<Void>> signup(@RequestBody SignupRequestDto signUpRequestDto){
        CommonResponseDto<Object> commonResponseDto = userService.signup(signUpRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }

    @GetMapping("/user/admin")
    public String amdinP(){
        return "admin controller";
    }


    @PutMapping("/user/update/{userId}")
    public ResponseEntity<ResultDto<Void>> updateUserInfo(@PathVariable Long userId,@RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto){
        CommonResponseDto<Object> commonResponseDto = userService.updateUserInfo(userId,updateUserInfoRequestDto);
        ResultDto<Void> resultDto = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(resultDto);
    }

}
