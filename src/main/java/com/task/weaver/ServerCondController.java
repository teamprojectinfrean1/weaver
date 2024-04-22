package com.task.weaver;

import com.task.weaver.common.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "서버 동작 체크", description = "Server Condition Check : 서버 동작 체크")
@RestController
public class ServerCondController {

    @Operation(summary = "서버 동작 체크")
    @GetMapping("/health")
    public String serverCheck() {
        return "hello dev";
    }

    /**
     * Data response에 감싸져있는 HTTP status는 응답 body에 노출
     */
    @Operation(summary = "서버 동작 체크")
    @GetMapping("/response")
    public ResponseEntity<DataResponse<String>> responseCheck() {
        return new ResponseEntity<>(DataResponse.of(
                HttpStatus.OK, "응답 형식 지정 성공", "ok", true), HttpStatus.OK);
    }
}
