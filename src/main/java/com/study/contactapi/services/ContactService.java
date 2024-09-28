package com.study.contactapi.services;

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

        return this.contactRepository.findAllByContactUserId(userId).stream().map(ContactResponseDTO::new).toList();
    }

    public ContactResponseDTO findContactById(String contactId, String userId) {
        Contact contactFound = this.contactRepository.findByIdAndUserId(contactId, userId).orElseThrow(ContactNotFoundException::new);

        return new ContactResponseDTO(contactFound);
    }

    public ContactResponseDTO updateContactById(String contactId, String userId, UpdateContactBodyDTO updateContactBodyDTO) {
        Contact contactFound = this.contactRepository.findByIdAndUserId(contactId, userId).orElseThrow(ContactNotFoundException::new);

        Contact updatedContact = this.contactRepository.save(this.dataMerger.mergeData(contactFound, updateContactBodyDTO));

        return new ContactResponseDTO(updatedContact);
    }

    public ContactResponseDTO deleteContactById(String contactId, String userId) {
        Contact contactFound = this.contactRepository.findByIdAndUserId(contactId, userId).orElseThrow(ContactNotFoundException::new);

        this.contactRepository.delete(contactFound);

        return new ContactResponseDTO(contactFound);
    }
}
