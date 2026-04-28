package com.congpv.springboot_base_project.shared.dto.project;

import com.congpv.springboot_base_project.shared.dto.common.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProjectPageResponse extends PageResponse<ProjectResponseDto> {
    public ProjectPageResponse(List<ProjectResponseDto> content, int pageNo, int pageSize,
                               long totalElements, int totalPages, boolean last) {
        super(content, pageNo, pageSize, totalElements, totalPages, last);
    }

    public ProjectPageResponse() {
        super();
    }
}
