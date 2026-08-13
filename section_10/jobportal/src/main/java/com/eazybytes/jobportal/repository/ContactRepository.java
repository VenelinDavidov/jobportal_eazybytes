package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List <Contact> findByStatus(String status);

    List<Contact> findContactsByStatusOrderByCreatedAtAsc(String status);

    List<Contact> findContactsByStatus(String newMessage, Sort sort);

    Page <Contact> findContactsByStatus(String newMessage, Pageable pageable);

}