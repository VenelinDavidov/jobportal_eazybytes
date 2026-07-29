package com.eazybytes.jobportal.auth;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.dto.LoginRequestDto;

import com.eazybytes.jobportal.dto.LoginResponseDto;
import com.eazybytes.jobportal.dto.RegisterRequestDto;
import com.eazybytes.jobportal.dto.UserDto;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.entity.Role;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.RoleRepository;
import com.eazybytes.jobportal.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompromisedPasswordChecker compromisedPasswordChecker;


    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity <LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto) {

        try {
            var resultAuthenticated = authenticationManager.authenticate (new UsernamePasswordAuthenticationToken
                                      (loginRequestDto.username (), loginRequestDto.password ()));
            var userDto = new UserDto ();
            // Generate JWT token
            var jwtToken = jwtUtil.generateJwtToken (resultAuthenticated);

            return ResponseEntity
                                  .status (HttpStatus.OK)
                                  .body (new LoginResponseDto (HttpStatus.OK.getReasonPhrase (), userDto, jwtToken));

        } catch (BadCredentialsException ex) {
            return buildErrorResponseDto (HttpStatus.UNAUTHORIZED, "Invalid username or password");

        } catch (AuthenticationException ex) {
            return buildErrorResponseDto (HttpStatus.UNAUTHORIZED, "Authentication failed");

        } catch (Exception ex) {
            return buildErrorResponseDto (HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred!");
        }

    }

    @PostMapping(value = "/register/public", version = "1.0")
    public ResponseEntity <?> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {

        CompromisedPasswordDecision decision = compromisedPasswordChecker.check (registerRequestDto.password ());

        if (decision.isCompromised ()){
            return ResponseEntity
                    .status (HttpStatus.BAD_REQUEST)
                    .body (Map.of ( "password","Choose a stronger password!"));
        }

        Optional <JobPortalUser> existingUser =
                       userRepository.readUserByEmailOrMobileNumber (registerRequestDto.email (), registerRequestDto.mobileNumber ());

        if (existingUser.isPresent ()) {

           Map <String, String> errorResponse = new HashMap <> ();
           JobPortalUser jobPortalUser = existingUser.get ();

            if(jobPortalUser.getEmail ().equalsIgnoreCase (registerRequestDto.email ())) {
                errorResponse.put ("email", "Email already registered!");
            }

            if(jobPortalUser.getMobileNumber ().equals ( registerRequestDto.mobileNumber ())){
                errorResponse.put ("mobileNumber", "Mobile number already registered!");
            }
            return ResponseEntity
                             .status (HttpStatus.BAD_REQUEST)
                             .body (errorResponse);
        }


        JobPortalUser jobPortalUser = new JobPortalUser();
        BeanUtils.copyProperties (registerRequestDto, jobPortalUser);
        jobPortalUser.setPasswordHash (passwordEncoder.encode (registerRequestDto.password ()));

        Role role = roleRepository.findRoleByName (ApplicationConstants.ROLE_JOB_SEEKER)
                  .orElseThrow (() -> new IllegalStateException ("Role not found: " + ApplicationConstants.ROLE_JOB_SEEKER));

        jobPortalUser.setRole (role);
        userRepository.save (jobPortalUser);

        return ResponseEntity
                             .status (HttpStatus.CREATED)
                             .body ("User registered successfully!");
    }



    private ResponseEntity <LoginResponseDto> buildErrorResponseDto(HttpStatus status, String message) {

        return ResponseEntity
                             .status (status)
                             .body (new LoginResponseDto (message, null, null));
    }
}
