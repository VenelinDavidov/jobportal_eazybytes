package com.eazybytes.jobportal.contact.service;

import com.eazybytes.jobportal.contact.web.dto.ContactRequestDto;

public interface IContactService {

    boolean saveContactMsg(ContactRequestDto contactRequestDto);
}
