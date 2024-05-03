package com.task.weaver.domain.issue.util;

import com.task.weaver.common.model.Status;
import org.springframework.core.convert.converter.Converter;

public class IssueStatusTypeConverter implements Converter<String, Status> {
    @Override
    public Status convert(final String source) {
        return Status.fromName(source);
    }
}
