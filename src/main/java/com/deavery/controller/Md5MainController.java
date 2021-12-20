package com.deavery.controller;

import com.deavery.service.Md5MainService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(
        value ="/",
        produces="application/json"
)
public class Md5MainController {

    private final Md5MainService md5MainService;

    @GetMapping
    public ResponseEntity<?> done () {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/generate")
    public ResponseEntity<?> md5Generate(
            @RequestParam int count,
            @RequestParam int min,
            @RequestParam int max
    ) {
        return ResponseEntity.ok(md5MainService.md5Generate(count, min, max).body().string());
    }

    @PostMapping
    public ResponseEntity<?> md5Parse(
            @RequestBody List<String> parse
    ) {
        String operation = md5MainService.createOperation(parse.size());
        return ResponseEntity.ok(md5MainService.parseList(parse, operation, 9));
    }

}
