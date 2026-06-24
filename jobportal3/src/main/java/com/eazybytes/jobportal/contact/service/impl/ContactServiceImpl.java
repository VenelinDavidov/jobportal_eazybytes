package com.eazybytes.jobportal.contact.service.impl;

import com.eazybytes.jobportal.contact.repository.ContactRepository;
import com.eazybytes.jobportal.contact.service.IContactService;
import com.eazybytes.jobportal.contact.web.dto.ContactRequestDto;
import com.eazybytes.jobportal.contact.web.dto.mapper.DtoMapper;
import com.eazybytes.jobportal.entity.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

//    public ContactServiceImpl(ContactRepository contactRepository) {
//        this.contactRepository = contactRepository;
//    }

    @Override
    public boolean saveContactMsg(ContactRequestDto contactRequestDto) {

        boolean result = false;
        Contact contact = contactRepository.save (DtoMapper.transferToEntity (contactRequestDto));

        if (contact != null && contact.getId () != null) {
            result = true;
        }
        return result;
    }

//    private Contact transformToEntity(ContactRequestDto contactRequestDto) {

//        Contact contact = new Contact();

//        BeanUtils.copyProperties(contactRequestDto, contact);
//        contact.setCreatedAt(Instant.now());
//        contact.setCreatedBy("System");
//        contact.setStatus("NEW");
//        return contact;
//    }
}
