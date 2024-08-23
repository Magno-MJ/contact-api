package com.study.contactapi.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.study.contactapi.domain.user.Login;
import com.study.contactapi.dto.LoginBodyDTO;
import com.study.contactapi.dto.LoginResponseDTO;
import com.study.contactapi.http.exceptions.AccountNotActivatedException;
import com.study.contactapi.http.exceptions.UserNotFoundException;
import com.study.contactapi.http.exceptions.WrongCredentialsException;
import com.study.contactapi.infra.security.TokenService;
import com.study.contactapi.mail.EmailService;
import com.study.contactapi.repositories.AccountConfirmationTokenRepository;
import com.study.contactapi.repositories.LoginRepository;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthServiceTest {
  @Mock
  AccountConfirmationTokenRepository accountConfirmationTokenRepository;

  @Mock
  private TokenService tokenService;

  @Mock
  private LoginRepository loginRepository;

  @Mock
  private EmailService emailService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Autowired
  @InjectMocks
  private AuthService authService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Should login successfully")
  void loginCase1() {
    String email = "fake@mail.com";
    String password = "fake-password";

    Login login = new Login(email, password);
    login.setAccount_activated_at(new Date());

    when(loginRepository.findByEmail(email)).thenReturn(Optional.of(login));

    when(passwordEncoder.matches(any(), any())).thenReturn(true);

    String token =  "fake-token";
    
    when(tokenService.generateToken(any())).thenReturn(token);

    LoginBodyDTO loginBodyDTO = new LoginBodyDTO(email, password);

    ResponseEntity<LoginResponseDTO> loginResponseDto = this.authService.login(loginBodyDTO);

    verify(loginRepository, times(1)).findByEmail(email);
    verify(passwordEncoder, times(1)).matches(password, password);
    verify(tokenService, times(1)).generateToken(login);

    assertThat(loginResponseDto.getBody()).isEqualTo(new LoginResponseDTO(token));
  }

  @Test
  @DisplayName("Should throw if the login was not found")
  void loginCase2() {
    when(loginRepository.findByEmail(any())).thenReturn(Optional.empty());

    LoginBodyDTO loginBodyDTO = new LoginBodyDTO("fake@mail.com", "fake-password");

    Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> this.authService.login(loginBodyDTO));
    
    Assertions.assertEquals("User not found", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw if the user account is not activated")
  void loginCase3() {
    String email = "fake@mail.com";
    String password = "fake-password";
    Login login = new Login(email, password);

    when(loginRepository.findByEmail(email)).thenReturn(Optional.of(login));

    LoginBodyDTO loginBodyDTO = new LoginBodyDTO(email, password);

    Exception exception = Assertions.assertThrows(AccountNotActivatedException.class, () -> this.authService.login(loginBodyDTO));
    
    Assertions.assertEquals("Account is not activated", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw if the credentials are wrong")
  void loginCase4() {
    String email = "fake@mail.com";
    String password = "fake-password";

    Login login = new Login(email, password);
    login.setAccount_activated_at(new Date());

    when(loginRepository.findByEmail(email)).thenReturn(Optional.of(login));

    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    String token =  "fake-token";
    
    when(tokenService.generateToken(any())).thenReturn(token);

    LoginBodyDTO loginBodyDTO = new LoginBodyDTO(email, password);

    Exception exception = Assertions.assertThrows(WrongCredentialsException.class, () -> this.authService.login(loginBodyDTO));
    
    Assertions.assertEquals("The credentials are wrong", exception.getMessage());
  }
}
