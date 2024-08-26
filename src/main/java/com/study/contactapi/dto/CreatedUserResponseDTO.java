package com.study.contactapi.dto;

import com.study.contactapi.domain.user.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CreatedUserResponseDTO {
  @Schema(description = "User first name", example = "John")
  private String first_name;

  @Schema(description = "User last name", example = "Doe")
  private String last_name;

  @Schema(description = "User email", example = "john@mail.com")
  private String email;

  public CreatedUserResponseDTO(User user) {
    this.first_name = user.getFirst_name();
    this.last_name = user.getLast_name();
    this.email = user.getLogin().getEmail();
  }
}
