package com.study.contactapi.dto;

import com.study.contactapi.domain.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatedUserResponseDTO {
  private String first_name;
  private String last_name;
  private String email;

  public CreatedUserResponseDTO(User user) {
    this.first_name = user.getFirst_name();
    this.last_name = user.getLast_name();
    this.email = user.getLogin().getEmail();
  }
}
