package com.task.weaver.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGetMainProjectList {
    private ResponseGetProjectList mainProject;
    @Builder.Default
    private List<ResponseGetProjectList> noMainProject = new ArrayList<>();
}
