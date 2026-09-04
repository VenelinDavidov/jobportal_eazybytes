package com.eazybytes.jobportal.user.controller;



import com.eazybytes.jobportal.dto.ProfileDto;
import com.eazybytes.jobportal.dto.UserDto;
import com.eazybytes.jobportal.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/search/admin")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email) {

        Optional <UserDto> userOptional = userService.searchUserByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with email: " + email));
        }
        return ResponseEntity.ok(userOptional.get());
    }

    @PatchMapping("/{userId}/role/employer/admin")
    public ResponseEntity<?> elevateToEmployer(@PathVariable Long userId) {

        UserDto updatedUser = userService.elevateToEmployer(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/company/{companyId}/admin")
    public ResponseEntity<?> assignCompanyToEmployer(@PathVariable Long userId, @PathVariable Long companyId) {

        UserDto updatedUser = userService.assignCompanyToEmployer(userId, companyId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(value = "/profile/jobseeker", version = "1.0", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity <ProfileDto> createOrUpdateProfile(
                                                     @RequestPart(value = "profile") String profileJson,
                                                     @RequestPart(value = "profilePicture") MultipartFile profilePicture,
                                                     @RequestPart(value = "resume") MultipartFile resume,
                                                     Authentication authentication) throws JsonProcessingException {
        String userEmail = authentication.getName ();
        ProfileDto saveProfile = userService.createOrUpdateProfile(userEmail, profileJson, profilePicture, resume);
        return ResponseEntity.ok(saveProfile);
    }

}
