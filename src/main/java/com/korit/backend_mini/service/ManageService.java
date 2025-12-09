package com.korit.backend_mini.service;

import com.korit.backend_mini.dto.ApiRespDto;
import com.korit.backend_mini.repository.UserRepository;
import com.korit.backend_mini.secrity.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageService {

    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> getUserList(PrincipalUser principalUser) {
        if (principalUser.getUserRoles().stream().noneMatch(userRole -> userRole.getRoleId() == 1)) {
            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
        }
        return new ApiRespDto<>("success", "회원 정보 전체 조회 완료", userRepository.getUserList());
    }
}
