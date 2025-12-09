package com.korit.backend_mini.dto.req;

import com.korit.backend_mini.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupReqDto {
    private String email;
    private String password;
    private String username;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .username(username)
                .build();
    }
}
