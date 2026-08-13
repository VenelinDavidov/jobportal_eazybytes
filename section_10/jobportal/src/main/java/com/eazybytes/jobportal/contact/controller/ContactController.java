package com.eazybytes.jobportal.contact.controller;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService contactService;

    @PostMapping(path = "/public", version = "1.0")
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactRequestDto) {

        boolean isSaved =  contactService.saveContact(contactRequestDto);

        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body("Request processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Request processing failed");
        }
    }


    @GetMapping(path = "/admin")
    public ResponseEntity<List <ContactResponseDto>> fetchOpenContactMsgs() {

       List<ContactResponseDto> contactServiceDto =  contactService.fetchNewOpenContactMsgs ();
          return ResponseEntity.status (HttpStatus.OK)
                              .body(contactServiceDto);
    }


    @GetMapping(path = "/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgsWithSort(
                                                      @RequestParam (defaultValue = "createdAt")String sortBy,
                                                      @RequestParam (defaultValue = "asc")String sortDir){

    List<ContactResponseDto> contactResponseDtos = contactService.fetchNewContactsMsgsWithSort (sortBy, sortDir);

    return ResponseEntity.status(HttpStatus.OK)
                         .body(contactResponseDtos);
    }

    @GetMapping("/page/admin")
    public ResponseEntity<Page<ContactResponseDto>> fetchOpenContactMsgsWithPaginationAndSort(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page <ContactResponseDto> contactResponseDtosPage =
                             contactService.fetchNewContactsMsgsWithPaginationAndSort(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.OK)
                               .body(contactResponseDtosPage);
    }

    @PatchMapping("/{id}/status/admin")
    public ResponseEntity<String> closeContactMsg(@PathVariable String id) {

        // Implement logic to close the contact message with the given ID
        // For example, you can call a service method to update the status of the contact message
        boolean isUpdated = contactService.closeContactMsg(Long.valueOf (id), ApplicationConstants.CLOSED_MESSAGE);

        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Contact message updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Failed to update contact message.");
        }
    }

}
