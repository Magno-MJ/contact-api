package com.study.contactapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateContactBodyDTO (
  @Schema(description = "Contact first name", example = "John")
  @NotEmpty(message = "first_name is required")
  String first_name,

  @Schema(description = "Contact last name", example = "Doe")
  @NotEmpty(message = "last_name is required")
  String last_name,

  @Schema(description = "Contact phone number", example = "99999999999") 
  @NotEmpty(message = "phone_number is required")
  @Size(min = 11, max = 11, message = "phone_number is invalid")
  String phone_number
){}