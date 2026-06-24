package com.eazybytes.jobportal.company.web.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
@Builder
public record CompanyDTO(Long id, String name, String logo, String industry, String size, BigDecimal rating,
                         String locations, Integer founded, String description, Integer employees, String website,
                         Instant createdAt) {
}
