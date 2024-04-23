package com.task.weaver.domain.comment.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.dto.response.ResponseCommentList;
import com.task.weaver.domain.comment.dto.response.ResponsePageComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Comment Controller", description = "코멘트 관련 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("api/v1/comment")
@RestController
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "코멘트 생성", description = "코멘트 생성")
    @PostMapping
    public ResponseEntity<DataResponse<ResponseComment>> addComment(@RequestBody RequestCreateComment comment) {
        ResponseComment responseComment = commentService.addComment(comment);
        return ResponseEntity.ok()
                .body(DataResponse.of(HttpStatus.OK, "Comment created successfully.", responseComment, true));
    }

    @Operation(summary = "코멘트 삭제" , description = "commentId로 코멘트 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body("Successfully deleted provided comment");
    }

    @Operation(summary = "코멘트 수정", description = "코멘트 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<DataResponse<ResponseComment>> putComment(@PathVariable UUID commentId,
                                                                    @RequestBody RequestUpdateComment comment) {
        ResponseComment responseComment = commentService.updateComment(commentId, comment);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "코멘트 수정 성공", responseComment, true), HttpStatus.OK);
    }

    @Operation(summary = "코멘트 조회", description = "코멘트 조회")
    @GetMapping()
    public ResponseEntity<DataResponse<ResponsePageComment<ResponseCommentList, Comment>>> getComments(
            @RequestParam int page, @RequestParam int size, @RequestParam UUID issueId) {
        ResponsePageComment<ResponseCommentList, Comment> responsePageComment = commentService.getComments(page, size,
                issueId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "이슈에 연결된 코멘트 조회 성공", responsePageComment, true),
                HttpStatus.OK);
    }
}