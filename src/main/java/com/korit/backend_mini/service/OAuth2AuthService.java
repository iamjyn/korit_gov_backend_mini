package com.korit.backend_mini.service;

import com.korit.backend_mini.dto.ApiRespDto;
import com.korit.backend_mini.dto.OAuth2MergeReqDto;
import com.korit.backend_mini.dto.OAuth2SignupReqDto;
import com.korit.backend_mini.entity.User;
import com.korit.backend_mini.entity.UserRole;
import com.korit.backend_mini.repository.OAuth2UserRepository;
import com.korit.backend_mini.repository.UserRepository;
import com.korit.backend_mini.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OAuth2AuthService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> signup(OAuth2SignupReqDto oAuth2SignupReqDto) {
        Optional<User> foundUser = userRepository.getUserByUserEmail(oAuth2SignupReqDto.getEmail());
        if (foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용중인 email입니다.", null);
        }

        Optional<User> foundUsername = userRepository.getUserByUsername(oAuth2SignupReqDto.getUsername());

        if (foundUsername.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용중인 사용자 이름입니다.", null);
        }

        Optional<User> optionalUser = userRepository.addUser(oAuth2SignupReqDto.toUserEntity(bCryptPasswordEncoder));
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("회원 추가에 실패했습니다.");
        }

        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();

        int userRoleResult = userRoleRepository.addUserRole(userRole);
        if (userRoleResult != 1) {
            throw new RuntimeException("회원 권한 추가에 실패했습니다.");
        }

        int oauth2UserResult = oAuth2UserRepository.addOAuth2User(oAuth2SignupReqDto.toOAuth2UserEntity(optionalUser.get().getUserId()));
        if (oauth2UserResult != 1) {
            throw new RuntimeException("OAuth2 추가에 실패했습니다.");
        }

        return new ApiRespDto<>("success", "회원가입 완료", optionalUser.get());
    }

    public ApiRespDto<?> merge(OAuth2MergeReqDto oAuth2MergeReqDto) {
        Optional<User> foundUser = userRepository.getUserByUserEmail(oAuth2MergeReqDto.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인해주세요.", null);
        }

        if (!bCryptPasswordEncoder.matches(oAuth2MergeReqDto.getPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인해주세요.", null);
        }

        if (!foundUser.get().isActive()) {
            return new ApiRespDto<>("failed", "탈퇴 처리 된 계정입니다.", null);
        }

        int result = oAuth2UserRepository.addOAuth2User(oAuth2MergeReqDto.toEntity(foundUser.get().getUserId()));
        if (result != 1) {
            return new ApiRespDto<>("failed", "연동 실패했습니다. 나중에 다시 시도해주세요.", null);
        }

        return new ApiRespDto<>("success", "연동 완료", null);
    }
}
