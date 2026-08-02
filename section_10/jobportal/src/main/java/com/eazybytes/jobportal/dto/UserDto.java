package com.eazybytes.jobportal.dto;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@ToString
@Data
public class UserDto {

    private Long userId;
    private String name;
    private String email;
    private String mobileNumber;
    private String role;
    private String companyId;
    private String companyName;
    private Instant createdAt;

}
