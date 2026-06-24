package com.eazybytes.jobportal.contact.web.dto;

import java.io.Serializable;

public record ContactRequestDto(String email,
                                String message,
                                String name,
                                String subject,
                                String userType) implements Serializable {
}