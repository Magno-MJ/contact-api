package com.study.contactapi.dto;


import java.util.Date;

import com.study.contactapi.domain.user.Contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ContactResponseDTO {
  @Schema(description = "Contact id", example = "5f90d0da-83ad-468a-b7ec-0de8283789f1")
  private String id; 
  
  @Schema(description = "Contact user id", example = "9b230624-e432-49d5-b0a2-51643a60c499")
  private String contact_user_id; 
  
  @Schema(description = "Contact first name", example = "John")
  private String first_name;

  @Schema(description = "Contact last name", example = "Doe")
  private String last_name;

  @Schema(description = "Contact phone number", example = "99999999999") 
  private String phone_number;

  @Schema(description = "Contact creation date", example = "2024-08-26T00:54:51.937Z") 
  private Date created_at;

  @Schema(description = "Contact last update date", example = "2024-08-26T00:54:51.937Z")
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
