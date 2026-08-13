package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.dto.ContactRequestDto;
import com.eazybytes.jobportal.dto.ContactResponseDto;
import com.eazybytes.jobportal.entity.Contact;
import com.eazybytes.jobportal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List <ContactResponseDto> fetchNewContactsMsgsWithSort(String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).descending ()
                : Sort.by(sortBy).ascending();

        List <Contact> contacts = contactRepository.findContactsByStatus (ApplicationConstants.NEW_MESSAGE, sort);

        List <ContactResponseDto> responseDtos = contacts
                .stream ()
                .map (this::transformToDto)
                .collect (Collectors.toList ());

        return responseDtos;
    }

    @Override
    public Page <ContactResponseDto> fetchNewContactsMsgsWithPaginationAndSort(int pageNumber, int pageSize, String sortBy, String sortDir) {

       // Create Sort object based on sortBy and sortDir parameters
      Sort sort = sortDir.equalsIgnoreCase ("desc")
                          ?Sort.by (sortBy).descending ()
                          :Sort.by (sortBy).ascending ();

        // Create Pageable object with page number, page size, and sorting
        Pageable pageable = PageRequest.of (pageNumber, pageSize, sort);

        // Fetch paginated and sorted contacts from repository
        Page <Contact> contactPage = contactRepository.findContactsByStatus (ApplicationConstants.NEW_MESSAGE, pageable);

        // Transform Contact entities to ContactResponseDto
        Page <ContactResponseDto> responseDtoPage = contactPage.map (this::transformToDto);

        return responseDtoPage;
    }

    @Override
    public boolean closeContactMsg(Long id, String status) {

        Contact contact = contactRepository.findById (id).orElse (null);

        if (contact == null) {
            return false;
        } else {
            contact.setStatus (status);
            contactRepository.save (contact);
        }
        return true;
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

        ContactResponseDto contactResponseDto = new ContactResponseDto(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getUserType(),
                contact.getMessage(),
                contact.getSubject(),
                contact.getStatus(),
                contact.getCreatedAt());

        return contactResponseDto;
    }
}
