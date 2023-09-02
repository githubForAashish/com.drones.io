package com.example.comdronesio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drone-control")
public class DronesController {

    @GetMapping("is-alive")
    public ResponseEntity<Boolean> isAlive() {
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
