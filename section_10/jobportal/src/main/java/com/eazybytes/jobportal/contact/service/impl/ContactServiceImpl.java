package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import com.eazybytes.jobportal.entity.Contact;
import com.eazybytes.jobportal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        boolean result = false;
        Contact contact = contactRepository.save(transformToEntity(contactRequestDto));
        
        if(contact != null && contact.getId() != null) {
            result = true;
        }
        return result;
    }

    @Override
    public List <ContactResponseDto> fetchNewOpenContactMsgs() {
        List <Contact> contacts = contactRepository.findByStatus (ApplicationConstants.NEW_MESSAGE);
        List <ContactResponseDto> responseDtos = contacts
                .stream ()
                .map (this::transformToDto)
                .toList ();
        return responseDtos;
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {

        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
//        contact.setCreatedAt(Instant.now());
//        contact.setCreatedBy("System");
        contact.setStatus(ApplicationConstants.NEW_MESSAGE);
        return contact;
    }


    private ContactResponseDto transformToDto(Contact contact) {

        ContactResponseDto contactResponseDto = new ContactResponseDto(contact.getId(),
                contact.getName(), contact.getEmail(), contact.getUserType(), contact.getMessage(),
                contact.getSubject(), contact.getStatus(), contact.getCreatedAt());
        return contactResponseDto;
    }
}
