package com.korit.backend_mini.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoveBoardReqDto {
    private Integer boardId;
    private Integer userId;
}
