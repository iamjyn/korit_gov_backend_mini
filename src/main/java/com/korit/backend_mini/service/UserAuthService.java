package com.korit.backend_mini.service;

import com.korit.backend_mini.dto.resp.ApiRespDto;
import com.korit.backend_mini.dto.req.SigninReqDto;
import com.korit.backend_mini.dto.req.SignupReqDto;
import com.korit.backend_mini.entity.User;
import com.korit.backend_mini.entity.UserRole;
import com.korit.backend_mini.repository.UserRepository;
import com.korit.backend_mini.repository.UserRoleRepository;
import com.korit.backend_mini.secrity.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> signup(SignupReqDto signupReqDto) {
        Optional<User> foundUser = userRepository.getUserByUserEmail(signupReqDto.getEmail());
        if (foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용중인 email입니다.", null);
        }

        Optional<User> foundUsername = userRepository.getUserByUsername(signupReqDto.getUsername());

        if (foundUsername.isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용중인 사용자 이름입니다.", null);
        }

        Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("회원 추가에 실패했습니다.");
        }

        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();

        int result = userRoleRepository.addUserRole(userRole);
        if (result != 1) {
            throw new RuntimeException("회원 권한 추가에 실패했습니다.");
        }
        return new ApiRespDto<>("success", "회원가입 완료.", optionalUser.get());
    }

    public ApiRespDto<?> signin(SigninReqDto signinReqDto) {
        Optional<User> foundUser = userRepository.getUserByUserEmail(signinReqDto.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인해주세요", null);
        }

        if (!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인해주세요", null);
        }

        String accessToken = jwtUtils.generateAccessToken(foundUser.get().getUserId().toString());

        return new ApiRespDto<>("success", "로그인 완료.", accessToken);
    }
}
