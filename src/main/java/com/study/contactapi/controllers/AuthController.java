package com.study.contactapi.controllers;

import com.study.contactapi.dto.LoginBodyDTO;
import com.study.contactapi.dto.LoginResponseDTO;
import com.study.contactapi.dto.ResendAccountConfirmationTokenBodyDto;
import com.study.contactapi.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Controller that manages the user account authentication")
public class AuthController {
  private final AuthService authService;

  @Operation(description = "Sign in", method = "POST")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200", 
      description = "Ok",
      content = @Content(
        mediaType = "application/json", 
        schema = @Schema(implementation = LoginResponseDTO.class)
      )
    ),
    @ApiResponse(responseCode = "400", description = "Account is not activated | The credentials are wrong", content = @Content(mediaType = "application/json")),
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json")),
  })
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Validated @RequestBody LoginBodyDTO loginBodyDto){
    LoginResponseDTO loginResponseDto = this.authService.login(loginBodyDto);

    return ResponseEntity.ok(loginResponseDto);
  }

  @Operation(description = "Confirm user account", method = "POST")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200", 
      description = "Ok", 
      content = @Content(
        mediaType = "application/json"
      )
    ),
    @ApiResponse(responseCode = "400", description = "Confirmation token is not active", content = @Content(mediaType = "application/json")),
    @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content(mediaType = "application/json")),
    @ApiResponse(responseCode = "404", description = "Confirmation token not found", content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/confirm/{accountConfirmationToken}")
  public void confirmAccount(@PathVariable String accountConfirmationToken) {
    this.authService.confirmAccount(accountConfirmationToken);
  }

  @Operation(description = "Resend account confirmation token", method = "POST")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200", 
      description = "Ok",
      content = @Content(
        mediaType = "application/json"
      )
    ),
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json")),
  })
  @PostMapping("/resend-confirmation-token")
  public void resendAccountConfirmationToken(@Validated @RequestBody ResendAccountConfirmationTokenBodyDto resendAccountConfirmationTokenBodyDto) {
    this.authService.resendAccountConfirmationToken(resendAccountConfirmationTokenBodyDto);
  }
}
