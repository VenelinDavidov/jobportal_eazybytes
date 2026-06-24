package com.eazybytes.jobportal.contact.web.dto.mapper;

import com.eazybytes.jobportal.contact.web.dto.ContactRequestDto;
import com.eazybytes.jobportal.entity.Contact;


import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class DtoMapper {

    public Contact transferToEntity(ContactRequestDto contactRequestDto) {

        Contact contact = new Contact();

        contact.setName(contactRequestDto.name ());
        contact.setEmail(contactRequestDto.email ());
        contact.setSubject (contactRequestDto.subject ());
        contact.setMessage(contactRequestDto.message ());
        contact.setUserType (contactRequestDto.userType ());
        contact.setStatus ("NEW");

        contact.setCreatedAt (Instant.now ());
        contact.setCreatedBy ("System");
        return contact;




//       return Contact.builder()
//                .name(contactRequestDto.name())
//                .email(contactRequestDto.email())
//                .subject(contactRequestDto.subject())
//                .message(contactRequestDto.message())
//                .userType(contactRequestDto.userType())
//                .status("NEW")
//                .createdAt(Instant.now())
//                .createdBy("System")
//                .build();
    }
}
