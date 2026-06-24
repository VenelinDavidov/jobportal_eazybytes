package com.eazybytes.jobportal.contact.web;

import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.contact.web.dto.ContactRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;

    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContactMsg(@RequestBody ContactRequestDto contactRequestDto) {

       boolean isSaved = contactService.saveContactMsg (contactRequestDto);

         if (isSaved) {
              return ResponseEntity
                      .status (HttpStatus.CREATED)
                      .body("Contact message saved successfully.");
         } else {
              return ResponseEntity
                      .status(HttpStatus.INTERNAL_SERVER_ERROR).
                      body("Failed to save contact message.");
         }
   }
}
