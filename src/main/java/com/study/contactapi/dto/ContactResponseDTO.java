package com.study.contactapi.dto;


import java.util.Date;

import com.study.contactapi.domain.user.Contact;

import lombok.Getter;

@Getter
public class ContactResponseDTO {
  private String id; 
  private String contact_user_id; 
  private String first_name;
  private String last_name;
  private String phone_number;
  private Date created_at;
  private Date updated_at;

  public ContactResponseDTO(Contact contact) {
    this.id = contact.getId();
    this.contact_user_id = contact.getContact_user().getId();
    this.first_name = contact.getFirst_name();
    this.last_name = contact.getLast_name();
    this.phone_number = contact.getPhone_number();
    this.created_at = contact.getCreated_at();
    this.updated_at = contact.getUpdated_at();
  }
}
