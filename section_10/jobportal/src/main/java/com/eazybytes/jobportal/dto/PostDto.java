package com.eazybytes.jobportal.dto;

public record PostDto (
        Long id,
        Long userId,
        String title,
        String body){
}
