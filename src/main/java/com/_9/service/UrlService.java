package com._9.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._9.entity.ShortUrl;

import com._9.model.UrlRequest;
import com._9.model.UrlResponse;
import com._9.entity.Click;
import java.time.LocalDateTime;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com._9.repository.UrlRepository;
import com._9.repository.ClickRepository;
import java.util.List;
import java.util.HashMap;

@Service
public class UrlService {
    @Autowired private UrlRepository urlRepo;
    @Autowired private ClickRepository clickRepo;

    public UrlResponse createShortUrl(UrlRequest req) {
        String shortcode = req.getShortcode() != null ? req.getShortcode() : generateRandomCode();
        if (urlRepo.existsById(shortcode)) throw new RuntimeException("Shortcode already exists");

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortcode(shortcode);
        shortUrl.setOriginalUrl(req.getUrl());
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setExpiry(LocalDateTime.now().plusMinutes(req.getValidity() != null ? req.getValidity() : 30));
        shortUrl.setClickCount(0);
        urlRepo.save(shortUrl);

        UrlResponse res = new UrlResponse();
        res.setShortLink("http://localhost:8080/" + shortcode);
        res.setExpiry(shortUrl.getExpiry().toString());
        return res;
    }

    public void redirect(String shortcode, HttpServletResponse response, HttpServletRequest request) throws IOException {
        ShortUrl shortUrl = urlRepo.findById(shortcode).orElseThrow();
        if (shortUrl.getExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Link expired");

        Click click = new Click();
        click.setShortcode(shortcode);
        click.setTimestamp(LocalDateTime.now());
        click.setSource(request.getHeader("Referer"));
        click.setLocation(request.getRemoteAddr());
        clickRepo.save(click);

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        urlRepo.save(shortUrl);

        response.sendRedirect(shortUrl.getOriginalUrl());
    }

    public Map<String, Object> getStats(String shortcode) {
        ShortUrl shortUrl = urlRepo.findById(shortcode).orElseThrow();
        List<Click> clicks = clickRepo.findByShortcode(shortcode);
        Map<String, Object> stats = new HashMap<>();
        stats.put("clicks", shortUrl.getClickCount());
        stats.put("originalUrl", shortUrl.getOriginalUrl());
        stats.put("createdAt", shortUrl.getCreatedAt());
        stats.put("expiry", shortUrl.getExpiry());
        stats.put("clickData", clicks);
        return stats;
    }

    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}

