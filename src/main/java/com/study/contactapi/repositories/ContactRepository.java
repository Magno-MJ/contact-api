package com.study.contactapi.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.study.contactapi.domain.user.Contact;

public interface ContactRepository extends JpaRepository<Contact, String> {
    @Query(value = "SELECT * FROM contact WHERE id = ?1 AND contact_user_id = ?2", nativeQuery = true)
    Optional<Contact> findByIdAndUserId(String contactId, String userId);

    @Query(value = "SELECT * FROM contact WHERE contact_user_id = ?1", nativeQuery = true)
    List<Contact> findAllByContactUserId(String userId);
}
