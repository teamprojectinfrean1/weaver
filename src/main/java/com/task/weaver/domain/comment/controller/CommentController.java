package com.task.weaver.domain.comment.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment Controller", description = "코멘트 관련 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("api/v1/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "코멘트 생성", description = "코멘트 생성")
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody RequestCreateComment comment){
        commentService.addComment(comment);
        return ResponseEntity.ok().body("Successfully added provided comment");
    }
    @Operation(summary = "코멘트 삭제" , description = "commentId로 코멘트 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body("Successfully deleted provided comment");
    }
    @Operation(summary = "코멘트 수정" , description = "코멘트 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<DataResponse<ResponseComment>> putComment(@PathVariable Long commentId,
                                                                    @RequestBody RequestUpdateComment comment) {
        ResponseComment responseComment = commentService.updateComment(commentId, comment);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "코멘트 수정 성공",responseComment), HttpStatus.OK);
    }
}