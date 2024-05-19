package com.task.weaver.common.aop.validator;

import com.task.weaver.common.aop.annotation.HasTasks;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.issue.UnableImportIssueException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.service.ProjectService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HasTasksValidator implements ConstraintValidator<HasTasks, UUID> {
    private final ProjectService projectService;

    @Override
    public boolean isValid(final UUID value, final ConstraintValidatorContext context) {
        Project project = projectService.getProjectById(value);
        if (project.getTaskList().isEmpty()){
            throw new UnableImportIssueException(ErrorCode.ISSUE_CANNOT_IMPORT, ErrorCode.ISSUE_CANNOT_IMPORT.getMessage());
        }
        return true;
    }
}
