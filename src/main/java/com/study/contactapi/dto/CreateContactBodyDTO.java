package com.study.contactapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateContactBodyDTO (
  @NotEmpty(message = "first_name is required")
  String first_name,

  @NotEmpty(message = "last_name is required")
  String last_name,

  @NotEmpty(message = "phone_number is required")
  @Size(min = 11, max = 11, message = "phone_number is invalid")
  String phone_number
){}