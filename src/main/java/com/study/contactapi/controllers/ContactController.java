package com.study.contactapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.CreateContactBodyDTO;
import com.study.contactapi.dto.UpdateContactBodyDTO;
import com.study.contactapi.dto.ContactResponseDTO;
import com.study.contactapi.services.ContactService;

@RestController
@RequestMapping("/contact")
public class ContactController {
  @Autowired
  private ContactService contactService;

  @PostMapping()
  public ResponseEntity<ContactResponseDTO> createContact(@Validated @RequestBody CreateContactBodyDTO createContactBodyDto, @AuthenticationPrincipal User user) {
    ContactResponseDTO createdContact =  contactService.createContact(createContactBodyDto, user);

    return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
  }

  @GetMapping()
  public ResponseEntity<List<ContactResponseDTO>> findAllContacts(@AuthenticationPrincipal User user) {
    List<ContactResponseDTO> contactsFound = contactService.findAllContacts(user.getId());

    return new ResponseEntity<>(contactsFound, HttpStatus.CREATED);
  }

  @GetMapping("/{contactId}")
  public ResponseEntity<ContactResponseDTO> findContactById(@PathVariable String contactId, @AuthenticationPrincipal User user) {
    ContactResponseDTO contactFound =  contactService.findContactById(contactId, user.getId());

    return new ResponseEntity<>(contactFound, HttpStatus.CREATED);
  }

  @PutMapping("/{contactId}")
  public ResponseEntity<ContactResponseDTO> updateContactById(
    @PathVariable String contactId, 
    @AuthenticationPrincipal User user, 
    @Validated @RequestBody UpdateContactBodyDTO updateContactBodyDto
  ) {
    ContactResponseDTO contactFound =  contactService.updateContactById(contactId, user.getId(), updateContactBodyDto);

    return new ResponseEntity<>(contactFound, HttpStatus.CREATED);
  }

  @DeleteMapping("/{contactId}")
  public ResponseEntity<ContactResponseDTO> deleteContactById(
    @PathVariable String contactId, 
    @AuthenticationPrincipal User user
  ) {
    ContactResponseDTO contactFound =  contactService.deleteContactById(contactId, user.getId());

    return ResponseEntity.ok(contactFound);
  }
}
