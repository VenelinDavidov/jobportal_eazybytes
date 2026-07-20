package com.eazybytes.jobportal.auth;

import com.eazybytes.jobportal.dto.LoginRequestDto;

import com.eazybytes.jobportal.dto.LoginResponseDto;
import com.eazybytes.jobportal.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/login/public")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto) {

    var userDto = new UserDto();

    return ResponseEntity
                .status (HttpStatus.OK)
                .body(new LoginResponseDto (HttpStatus.OK.getReasonPhrase (), userDto, null));
    }
}
