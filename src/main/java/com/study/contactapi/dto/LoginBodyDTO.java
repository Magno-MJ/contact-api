package com.study.contactapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginBodyDTO (
  @Schema(description = "User email", example = "john@mail.com")
  @Email(message = "email is invalid")
  @NotEmpty(message = "email is required")
  String email,

  @Schema(description = "User password", example = "example#*")
  @NotEmpty(message = "password is required")
  String password
){}
