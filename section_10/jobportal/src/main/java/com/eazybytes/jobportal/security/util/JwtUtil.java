package com.eazybytes.jobportal.security.util;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.entity.JobPortalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@PropertySource( value = "classpath:jwt.properties")
public class JwtUtil {

    private final Environment env;

    @Value("${jwt.issuer:Job Portal}")
    private String jwtIssuer;

    @Value("${jwt.subject:JWT Token}")
    private String jwtSubject;

    @Value("${jwt.experation.hours:1}")
    private int jwtExpirationHours;


    public String generateJwtToken(Authentication authentication) {

        String jwtToken;
        String ttlTime = env.getProperty ("cache.jobs.ttl-minutes", "5");
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        var fetchedUser = (JobPortalUser) authentication.getPrincipal();

        jwtToken = Jwts.builder().issuer(jwtIssuer).subject(jwtSubject)
                                 .claim("name", fetchedUser.getName ())
                                 .claim("email", fetchedUser.getEmail())
                                 .claim("mobileNumber", fetchedUser.getMobileNumber())
                                 .claim("roles", authentication.getAuthorities()
                                                                     .stream()
                                                                     .map(GrantedAuthority::getAuthority)
                                                                     .collect(Collectors.joining(",")
                                 ))
                .issuedAt(new Date ())
                .expiration(new Date((new Date()).getTime() + jwtExpirationHours * 60 * 60 * 1000))
                .signWith(secretKey).compact();

        return jwtToken;
    }
}
