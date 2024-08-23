package com.study.contactapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.study.contactapi.domain.user.Contact;
import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.ContactResponseDTO;
import com.study.contactapi.dto.CreateContactBodyDTO;
import com.study.contactapi.dto.UpdateContactBodyDTO;
import com.study.contactapi.http.exceptions.ContactNotFoundException;
import com.study.contactapi.repositories.ContactRepository;
import com.study.contactapi.utils.data.DataHandler;

public class ContactServiceTest {
  
  @Mock
  private ContactRepository contactRepository;

  @Mock
  private DataHandler dataMerger;

  @Autowired
  @InjectMocks
  private ContactService contactService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Should create a contact successfully")
  void createContact() {
    CreateContactBodyDTO createContactBodyDTO = new CreateContactBodyDTO("fake name", "fake name", "9999999999");
    User user = new User();

    Contact contact = new Contact(createContactBodyDTO.first_name(), createContactBodyDTO.last_name(), createContactBodyDTO.phone_number(), user);
    
    when(contactRepository.save(any())).thenReturn(contact);

    ContactResponseDTO createContactResponseDTO = this.contactService.createContact(createContactBodyDTO, user);

    verify(contactRepository, times(1)).save(contact);

    assertThat(createContactResponseDTO).isEqualTo(new ContactResponseDTO(contact));
  }


  @Test
  @DisplayName("Should create a contact successfully")
  void findAllContacts() {
    User user = new User();

    Contact contact = new Contact(user.getFirst_name(), user.getLast_name(), "9999999999", user);
    
    when(contactRepository.findAllByContactUserId(user.getId())).thenReturn(List.of(contact));

    List<ContactResponseDTO> findAllContactsResponseDto = this.contactService.findAllContacts(user.getId());

    verify(contactRepository, times(1)).findAllByContactUserId(user.getId());

    assertThat(findAllContactsResponseDto).isEqualTo(List.of(new ContactResponseDTO(contact)));
  }

  @Test
  @DisplayName("Should find contact by id successfully")
  void findContactByIdCase1() {
    User user = new User();

    Contact contact = new Contact();
    contact.setContact_user(user);

    when(contactRepository.findByIdAndUserId(contact.getId(), user.getId())).thenReturn(Optional.of(contact));

    ContactResponseDTO contactResponseDTO = this.contactService.findContactById(contact.getId(), user.getId());

    verify(contactRepository, times(1)).findByIdAndUserId(contact.getId(), user.getId());

    assertThat(contactResponseDTO).isEqualTo(new ContactResponseDTO(contact));
  }


  @Test
  @DisplayName("Should throw if the contact was not found")
  void findContactByIdCase2() {
    String userId = "fake-id";
    String contactId = "fake-id";

    when(contactRepository.findByIdAndUserId(contactId, userId)).thenReturn(Optional.empty());

 
    Exception exception = Assertions.assertThrows(ContactNotFoundException.class, () -> this.contactService.findContactById(contactId, userId));
    
    Assertions.assertEquals("Contact not found", exception.getMessage());
  }

  @Test
  @DisplayName("Should update contact by id successfully")
  void updateContactByIdCase1() {
    User user = new User();

    Contact contact = new Contact();
    contact.setContact_user(user);

    UpdateContactBodyDTO updateContactBodyDTO = new UpdateContactBodyDTO();

    when(contactRepository.findByIdAndUserId(contact.getId(), user.getId())).thenReturn(Optional.of(contact));

    when(dataMerger.mergeData(contact, updateContactBodyDTO)).thenReturn(contact);

    when(contactRepository.save(contact)).thenReturn(contact);

    ContactResponseDTO contactResponseDTO = this.contactService.updateContactById(contact.getId(), user.getId(), updateContactBodyDTO);

    verify(contactRepository, times(1)).findByIdAndUserId(contact.getId(), user.getId());
    verify(dataMerger, times(1)).mergeData(contact, updateContactBodyDTO);
    verify(contactRepository, times(1)).save(contact);

    assertThat(contactResponseDTO).isEqualTo(new ContactResponseDTO(contact));
  }


  @Test
  @DisplayName("Should throw if the contact was not found")
  void updateContactByIdCase2() {
    String userId = "fake-id";
    String contactId = "fake-id";

    when(contactRepository.findByIdAndUserId(contactId, userId)).thenReturn(Optional.empty());

    UpdateContactBodyDTO updateContactBodyDTO = new UpdateContactBodyDTO();

    Exception exception = Assertions.assertThrows(ContactNotFoundException.class, () -> this.contactService.updateContactById(contactId, userId, updateContactBodyDTO));
    
    Assertions.assertEquals("Contact not found", exception.getMessage());
  }


  @Test
  @DisplayName("Should delete contact by id successfully")
  void deleteContactByIdCase1() {
    User user = new User();

    Contact contact = new Contact();
    contact.setContact_user(user);

    when(contactRepository.findByIdAndUserId(contact.getId(), user.getId())).thenReturn(Optional.of(contact));

    ContactResponseDTO contactResponseDTO = this.contactService.deleteContactById(contact.getId(), user.getId());

    verify(contactRepository, times(1)).delete(contact);

    assertThat(contactResponseDTO).isEqualTo(new ContactResponseDTO(contact));
  }


  @Test
  @DisplayName("Should throw if the contact was not found")
  void deleteContactByIdCase2() {
    String userId = "fake-id";
    String contactId = "fake-id";

    when(contactRepository.findByIdAndUserId(contactId, userId)).thenReturn(Optional.empty());
 
    Exception exception = Assertions.assertThrows(ContactNotFoundException.class, () -> this.contactService.findContactById(contactId, userId));
    
    Assertions.assertEquals("Contact not found", exception.getMessage());
  }
}
