package com.task.weaver.domain.authorization.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/oauth")
public class OAuthCallBack {

    @GetMapping("/kakao")
    public HttpEntity<?> login (@RequestParam final String code) {
        log.info(code);
        return ResponseEntity.ok().build();
    }
}
