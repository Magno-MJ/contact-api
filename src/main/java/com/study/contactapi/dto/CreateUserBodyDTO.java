package com.study.contactapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserBodyDTO (
  @Schema(description = "User email", example = "john@mail.com")
  @Email(message = "Invalid email")
  @NotEmpty(message = "email is required")
  String email,

  @Schema(description = "User first name", example = "John")
  @NotEmpty(message = "first_name is required")
  String first_name, 

  @Schema(description = "User last name", example = "Doe")
  @NotEmpty(message = "last_name is required")
  String last_name, 

  @Schema(description = "User password", example = "example#*")
  @NotEmpty(message = "password is required")
  String password
) {
}
