package com.example.springai.demo.dto;

public record Joke(
        String text,
        String category,
        Double laughScore,
        Boolean isNSFW,
        String language
) {
}
