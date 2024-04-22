package com.task.weaver.domain.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@Data
@Builder
public class RequestGetTaskPage {
    private int page;
    private int size;
    private UUID projectId;

    public RequestGetTaskPage() {
        this.page = 1;
        this.size = 10;
    }

    public RequestGetTaskPage(int page, int size, UUID projectId) {
        this.page = page;
        this.size = size;
        this.projectId = projectId;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
