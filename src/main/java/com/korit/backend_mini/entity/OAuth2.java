package com.korit.backend_mini.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2 {
    private Integer oauth2UserId;
    private Integer userId;
    private String provider;
    private String providerUserId;
    private LocalDateTime createDt;
}
