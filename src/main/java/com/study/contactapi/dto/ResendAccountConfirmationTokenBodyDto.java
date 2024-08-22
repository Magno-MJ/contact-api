package com.study.contactapi.dto;

import jakarta.validation.constraints.Email;

public record ResendAccountConfirmationTokenBodyDto (
  @Email(message = "email is invalid")
  String email
){}
