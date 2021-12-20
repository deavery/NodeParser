package com.deavery.controller;

import com.deavery.service.Md5Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/md5/natural")
public class Md5Controller {

    private final Md5Service md5Service;

    @GetMapping
    public ResponseEntity<?> md5Parse (
            @RequestParam String hash,
            @RequestParam String operation
    ) {
        md5Service.md5Parse(hash, operation);
        return ResponseEntity.ok(200);
    }

    @GetMapping("/generate")
    public ResponseEntity<?> md5GenerateList (
        @RequestParam int count,
        @RequestParam int min,
        @RequestParam int max
    ) {
        return ResponseEntity.ok(md5Service.md5Generate(count, min, max));
    }
}
