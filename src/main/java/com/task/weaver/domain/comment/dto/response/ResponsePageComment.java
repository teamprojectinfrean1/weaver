package com.task.weaver.domain.comment.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
@Data
public class ResponsePageComment<DTO,EN> {
    private List<DTO> dataList;
    private int totalPage;

    public ResponsePageComment(Page<EN> result, Function<EN, DTO> fn) {
        dataList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
    }
}