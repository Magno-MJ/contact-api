package com.study.contactapi.controllers;

import com.study.contactapi.dto.LoginBodyDTO;
import com.study.contactapi.dto.LoginResponseDTO;
import com.study.contactapi.dto.ResendAccountConfirmationTokenBodyDto;
import com.study.contactapi.services.AuthService;

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
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Validated @RequestBody LoginBodyDTO loginBodyDto){
    LoginResponseDTO loginResponseDto = this.authService.login(loginBodyDto);

    return ResponseEntity.ok(loginResponseDto);
  }

  @PostMapping("/confirm/{accountConfirmationToken}")
  public void confirmAccount(@PathVariable String accountConfirmationToken) {
    this.authService.confirmAccount(accountConfirmationToken);
  }

  @PostMapping("/resend-confirmation-token")
  public void resendAccountConfirmationToken(@Validated @RequestBody ResendAccountConfirmationTokenBodyDto resendAccountConfirmationTokenBodyDto) {
    this.authService.resendAccountConfirmationToken(resendAccountConfirmationTokenBodyDto);
  }
}
