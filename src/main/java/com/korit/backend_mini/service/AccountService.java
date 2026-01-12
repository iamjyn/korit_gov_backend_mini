package com.korit.backend_mini.service;

import com.korit.backend_mini.dto.ApiRespDto;
import com.korit.backend_mini.dto.account.ChangePasswordReqDto;
import com.korit.backend_mini.dto.account.ChangeProfileImgReqDto;
import com.korit.backend_mini.dto.account.ChangeUsernameReqDto;
import com.korit.backend_mini.entity.User;
import com.korit.backend_mini.repository.UserRepository;
import com.korit.backend_mini.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiRespDto<?> changePassword(ChangePasswordReqDto changePasswordReqDto, PrincipalUser principalUser) {
        if (!changePasswordReqDto.getUserId().equals(principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByUserId(changePasswordReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 회원정보입니다.", null);
        }

        User user = foundUser.get();

        if (!bCryptPasswordEncoder.matches(changePasswordReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "현재 비밀번호가 일치하지 않습니다.", null);
        }

        if (bCryptPasswordEncoder.matches(changePasswordReqDto.getNewPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "기존 비밀번호와 일치합니다.", null);
        }

        user.setPassword(bCryptPasswordEncoder.encode(changePasswordReqDto.getNewPassword()));

        int result = userRepository.changePassword(user);
        if (result != 1) {
            return new ApiRespDto<>("failed", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.", null);
        }
        return new ApiRespDto<>("success", "비밀번호 변경 완료", null);
    }

    public ApiRespDto<?> changeUsername(ChangeUsernameReqDto changeUsernameReqDto, PrincipalUser principalUser) {
        if (!changeUsernameReqDto.getUserId().equals(principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> optionalUser = userRepository.getUserByUserId(changeUsernameReqDto.getUserId());
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 회원정보입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByUsername(changeUsernameReqDto.getUsername());
        if (foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 사용자 이름입니다.", null);
        }

        User user = optionalUser.get();
        user.setUsername(changeUsernameReqDto.getUsername());

        int result = userRepository.changeUsername(user);
        if (result != 1) {
            return new ApiRespDto<>("failed", "사용자 이름 변경에 실패했습니다. 다시 시도해주세요.", null);
        }
        return new ApiRespDto<>("success", "사용자 이름 변경 완료", null);
    }

    public ApiRespDto<?> changeProfileImg(ChangeProfileImgReqDto changeProfileImgReqDto, PrincipalUser principalUser) {
        if (!changeProfileImgReqDto.getUserId().equals(principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> optionalUser = userRepository.getUserByUserId(changeProfileImgReqDto.getUserId());
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 회원정보입니다.", null);
        }

        User user = optionalUser.get();
        user.setProfileImg(changeProfileImgReqDto.getProfileImg());

        int result = userRepository.changeProfileImg(user);
        if (result != 1) {
            return new ApiRespDto<>("failed", "사용자 프로필 이미지 변경에 실패했습니다. 다시 시도해주세요.", null);
        }
        return new ApiRespDto<>("success", "사용자 이미지 변경 완료", null);
    }

    public ApiRespDto<?> withdraw(PrincipalUser principalUser) {
        Optional<User> foundUSer = userRepository.getUserByUserId(principalUser.getUserId());
        if (foundUSer.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 회원정보입니다.", null);
        }

        User user = foundUSer.get();
        if (!user.isActive()) {
            return new ApiRespDto<>("failed", "이미 탈퇴 처리된 계정입니다.", null);
        }

        int result = userRepository.withdraw(user.getUserId());
        if (result != 1) {
            return new ApiRespDto<>("failed", "탈퇴 처리에 실팼습니다. 다시 시도해주세요.", null);
        }
        return new ApiRespDto<>("success", "탈퇴 처리 완료. 90일 이후 회원정보 삭제 예정", null);
    }
}
