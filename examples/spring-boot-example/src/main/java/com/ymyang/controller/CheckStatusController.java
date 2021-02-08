package com.ymyang.controller;

import com.ymyang.framework.beans.ResponseEntity;
import com.ymyang.framework.web.annotations.Anonymous;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/")
public class CheckStatusController {

    @Anonymous
    @GetMapping
    public ResponseEntity<String> check() {
        return ResponseEntity.success("ok");
    }

}
