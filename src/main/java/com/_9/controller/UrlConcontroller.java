package com._9.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com._9.model.UrlRequest;
import com._9.model.UrlResponse;
import com._9.service.UrlService;

@RestController
@RequestMapping("/shorturls")
public class UrlConcontroller {
    @Autowired private UrlService service;

    @PostMapping
    public ResponseEntity<UrlResponse> create(@RequestBody UrlRequest req) {
        UrlResponse response = service.createShortUrl(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shortcode}")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable String shortcode) {
        return ResponseEntity.ok(service.getStats(shortcode));
    }

    @GetMapping("/r/{shortcode}")
    public void redirect(@PathVariable String shortcode, HttpServletResponse res, HttpServletRequest req) throws IOException {
        service.redirect(shortcode, res, req);
    }
}
