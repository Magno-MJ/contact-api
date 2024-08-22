package com.study.contactapi.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.contactapi.domain.user.Contact;
import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.CreateContactBodyDTO;
import com.study.contactapi.dto.UpdateContactBodyDTO;
import com.study.contactapi.http.exceptions.ContactNotFoundException;
import com.study.contactapi.dto.ContactResponseDTO;
import com.study.contactapi.repositories.ContactRepository;
import com.study.contactapi.utils.data.DataHandler;

@Service
public class ContactService {

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private DataHandler dataMerger;

  public ContactResponseDTO createContact(CreateContactBodyDTO createContactBodyDTO, User user) {
    Contact contact = new Contact(createContactBodyDTO.first_name(), createContactBodyDTO.last_name(), createContactBodyDTO.phone_number(), user);

    Contact createdContact = this.contactRepository.save(contact);
    
    return new ContactResponseDTO(createdContact);
  }

  public List<ContactResponseDTO> findAllContacts(String userId) {
    List<ContactResponseDTO> contactsFound = this.contactRepository.findAllByContactUserId(userId).stream().map((contact) -> new ContactResponseDTO(contact)).toList();

    return contactsFound;
  }

  public ContactResponseDTO findContactById(String contactId, String userId) {
    Contact contactFound = this.contactRepository.findByIdAndUserId(contactId, userId).orElseThrow(() -> new ContactNotFoundException());

    return new ContactResponseDTO(contactFound);
  }

  public ContactResponseDTO updateContactById(String contactId, String userId, UpdateContactBodyDTO updateContactBodyDTO) throws IllegalArgumentException, IOException {
    Contact contactFound = this.contactRepository.findByIdAndUserId(contactId, userId).orElseThrow(() -> new ContactNotFoundException());

    Contact updatedContact = this.contactRepository.save(this.dataMerger.mergeData(contactFound, updateContactBodyDTO));

    return new ContactResponseDTO(updatedContact);
  }

  public ContactResponseDTO deleteContactById(String contactId, String userId) {
    Contact contactFound = this.contactRepository.findByIdAndUserId(contactId, userId).orElseThrow(() -> new ContactNotFoundException());
    
    this.contactRepository.delete(contactFound);

    return new ContactResponseDTO(contactFound);
  }
}
