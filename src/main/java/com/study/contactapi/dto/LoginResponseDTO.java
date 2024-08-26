package com.study.contactapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class LoginResponseDTO {
  @Schema(description = "Authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjb250YWN0LWFwaSIsInN1YiI6ImZha2VAZmFrZS5jb20iLCJleHAiOjE3MjQ2MTAzNzl9.-9lx8W1WDLc4woQFkTBCcgSH4Hn39ZvMzaG4crTgf0c")
  private String token;
}
