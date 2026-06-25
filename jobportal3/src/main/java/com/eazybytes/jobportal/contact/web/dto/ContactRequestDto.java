package com.eazybytes.jobportal.contact.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ContactRequestDto(
                                @NotBlank(message = "Email can not be empty")
                                @Email(message = "Email should be valid")
                                String email,

                                @NotBlank(message = "Message can not be empty")
                                @Size(min = 5, max = 500, message = "Message should be between 5 and 500 characters")
                                String message,

                                @NotBlank(message = "Name can not be empty")
                                @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
                                String name,

                                @NotBlank(message = "Subject can not be empty")
                                @Size(min = 5, max = 100, message = "Subject should be between 5 and 100 characters")
                                String subject,

                                @NotBlank(message = "UserType can not be empty")
                                @Pattern (regexp = "Job Seeker | Employer| Other", message = "UserType should be either 'Job Seeker', 'Employer' or 'Other'")
                                String userType)

        implements Serializable {
}