package com.congpv.springboot_base_project.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequestDto {
    private String username; // tên đăng nhập của người sẽ được gán
    private String role; // giá trị: PROJECT_MANAGER, DEVELOPER, TESTER, VIEWER
}
