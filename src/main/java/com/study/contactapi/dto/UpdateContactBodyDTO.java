package com.study.contactapi.dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateContactBodyDTO {
  @Schema(description = "Contact first name", example = "John")
  @NotBlank(message = "first_name is required")
  Optional<String> first_name;

  @Schema(description = "Contact last name", example = "Doe")
  @NotBlank(message = "last_name is required")
  Optional<String> last_name;

  @Schema(description = "Contact phone number", example = "99999999999") 
  @NotBlank(message = "phone_number is required")
  @Size(min = 11, max = 11, message = "phone_number is invalid")
  Optional<String> phone_number;
}