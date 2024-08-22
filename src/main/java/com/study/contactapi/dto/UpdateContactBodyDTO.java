package com.study.contactapi.dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateContactBodyDTO {
  @NotBlank(message = "first_name is required")
  Optional<String> first_name;

  @NotBlank(message = "last_name is required")
  Optional<String> last_name;

  @NotBlank(message = "phone_number is required")
  @Size(min = 11, max = 11, message = "phone_number is invalid")
  Optional<String> phone_number;
}