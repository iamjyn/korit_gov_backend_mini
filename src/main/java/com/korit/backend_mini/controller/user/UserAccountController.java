package com.korit.backend_mini.controller.user;

import com.korit.backend_mini.dto.ApiRespDto;
import com.korit.backend_mini.dto.account.ChangePasswordReqDto;
import com.korit.backend_mini.dto.account.ChangeProfileImgReqDto;
import com.korit.backend_mini.dto.account.ChangeUsernameReqDto;
import com.korit.backend_mini.security.model.PrincipalUser;
import com.korit.backend_mini.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/account")
public class UserAccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(new ApiRespDto<>("success", "회원조회완료", principalUser));
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordReqDto changePasswordReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(accountService.changePassword(changePasswordReqDto, principalUser));
    }

    @PostMapping("/change/username")
    public ResponseEntity<?> changeUsername(@RequestBody ChangeUsernameReqDto changeUsernameReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(accountService.changeUsername(changeUsernameReqDto, principalUser));
    }

    @PostMapping("/change/profileImg")
    public ResponseEntity<?> changeProfileImg(@RequestBody ChangeProfileImgReqDto changeProfileImgReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(accountService.changeProfileImg(changeProfileImgReqDto, principalUser));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(accountService.withdraw(principalUser));
    }
}
