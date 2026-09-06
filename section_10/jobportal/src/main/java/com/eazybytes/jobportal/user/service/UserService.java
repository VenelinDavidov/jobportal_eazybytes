package com.eazybytes.jobportal.user.service;


import com.eazybytes.jobportal.dto.ProfileDto;
import com.eazybytes.jobportal.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {


    UserDto assignCompanyToEmployer(Long userId, Long companyId);

    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);

    ProfileDto createOrUpdateProfile(String userEmail, String profileJson, MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException;

    ProfileDto getProfile(String userEmail);

    ProfileDto getProfilePicture(String userEmail);

    ProfileDto getResume(String userEmail);

}
