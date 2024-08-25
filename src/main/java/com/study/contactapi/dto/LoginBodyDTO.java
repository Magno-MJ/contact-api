package com.study.contactapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginBodyDTO (
  @Email(message = "email is invalid")
  @NotEmpty(message = "email is required")
  String email,
  

  @NotEmpty(message = "password is required")
  String password
){}
