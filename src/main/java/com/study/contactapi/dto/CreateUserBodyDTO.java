package com.study.contactapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserBodyDTO (
  @Email(message = "Invalid email")
  @NotEmpty(message = "email is required")
  String email,

  @NotEmpty(message = "first_name is required")
  String first_name, 

  @NotEmpty(message = "last_name is required")
  String last_name, 

  @NotEmpty(message = "password is required")
  String password
) {
}
