package com.eazybytes.jobportal.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @GetMapping(version = "1.0")
    public ResponseEntity<String> getAllCompanies() {

        return ResponseEntity.status (HttpStatus.OK)
                             .body("Updated Companies List");
    }

}