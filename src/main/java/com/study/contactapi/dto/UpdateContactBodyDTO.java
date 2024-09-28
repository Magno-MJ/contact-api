package com.study.contactapi.dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateContactBodyDTO {
    @Schema(description = "Contact first name", example = "John")
    String first_name;

    @Schema(description = "Contact last name", example = "Doe")
    String last_name;

    @Schema(description = "Contact phone number", example = "99999999999")

    @Size(min = 11, max = 11, message = "phone_number is invalid")
    String phone_number;
}