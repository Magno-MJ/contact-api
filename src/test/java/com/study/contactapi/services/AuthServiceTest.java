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

import com.study.contactapi.domain.user.AccountConfirmationToken;
import com.study.contactapi.domain.user.Login;
import com.study.contactapi.dto.LoginBodyDTO;
import com.study.contactapi.dto.LoginResponseDTO;
import com.study.contactapi.dto.ResendAccountConfirmationTokenBodyDto;
import com.study.contactapi.http.exceptions.AccountNotActivatedException;
import com.study.contactapi.http.exceptions.ConfirmationTokenIsNotActiveException;
import com.study.contactapi.http.exceptions.ConfirmationTokenNotFoundException;
import com.study.contactapi.http.exceptions.InvalidTokenException;
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

  @Test
  @DisplayName("Should confirm account successfully")
  void confirmAccountCase1() {
    String confirmationToken = "fake-confirmation-token";
    String loginId = "fake-login-id";
    
    when(accountConfirmationTokenRepository.findByToken(confirmationToken)).thenReturn(new AccountConfirmationToken());
    when(tokenService.validateToken(confirmationToken)).thenReturn(loginId);

    this.authService.confirmAccount(confirmationToken);

    verify(accountConfirmationTokenRepository, times(1)).findByToken(confirmationToken);
    verify(tokenService, times(1)).validateToken(confirmationToken);
    verify(loginRepository, times(1)).activateUserLoginById(loginId);
    verify(accountConfirmationTokenRepository, times(1)).disableByToken(confirmationToken);
  }

  @Test
  @DisplayName("Should throw if the account confirmation token was not found")
  void confirmAccountCase2() {
    String confirmationToken = "fake-confirmation-token";
    
    when(accountConfirmationTokenRepository.findByToken(confirmationToken)).thenReturn(null);
    
    Exception exception = Assertions.assertThrows(ConfirmationTokenNotFoundException.class, () -> this.authService.confirmAccount(confirmationToken));
    
    Assertions.assertEquals("Confirmation token not found", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw if the account confirmation token is not active")
  void confirmAccountCase3() {
    String confirmationToken = "fake-confirmation-token";
    
    AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken();
    accountConfirmationToken.set_active(false);

    when(accountConfirmationTokenRepository.findByToken(confirmationToken)).thenReturn(accountConfirmationToken);
    
    Exception exception = Assertions.assertThrows(ConfirmationTokenIsNotActiveException.class, () -> this.authService.confirmAccount(confirmationToken));
    
    Assertions.assertEquals("Confirmation token is not active", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw if the login was not found")
  void confirmAccountCase4() {
    String confirmationToken = "fake-confirmation-token";

    when(accountConfirmationTokenRepository.findByToken(confirmationToken)).thenReturn(new AccountConfirmationToken());
    when(tokenService.validateToken(confirmationToken)).thenReturn(null);
    
    Exception exception = Assertions.assertThrows(InvalidTokenException.class, () -> this.authService.confirmAccount(confirmationToken));
    
    Assertions.assertEquals("Invalid token", exception.getMessage());

    verify(accountConfirmationTokenRepository, times(1)).disableByToken(confirmationToken);
  }


  @Test
  @DisplayName("Should resend confirmation token successfully")
  void resendAccountConfirmationTokenCase1() {
    Login login = new Login();
    ResendAccountConfirmationTokenBodyDto resendAccountConfirmationTokenBodyDto = new ResendAccountConfirmationTokenBodyDto("fake@mail.com");
    String confirmationToken = "fake-confirmation-token";
    String loginId = login.getId();

    when(loginRepository.findByEmail(resendAccountConfirmationTokenBodyDto.email())).thenReturn(Optional.of(login));
    when(tokenService.generateConfirmationToken(login.getId())).thenReturn(confirmationToken);


    this.authService.resendAccountConfirmationToken(resendAccountConfirmationTokenBodyDto);

    verify(loginRepository, times(1)).findByEmail(resendAccountConfirmationTokenBodyDto.email());
    verify(tokenService, times(1)).generateConfirmationToken(loginId);
    verify(accountConfirmationTokenRepository, times(1)).disableByLoginId(loginId);
    verify(accountConfirmationTokenRepository, times(1)).save(new AccountConfirmationToken(confirmationToken, login, true));
    verify(emailService, times(1)).sendAccountConfirmationMail(login.getEmail(), confirmationToken);
  }


  @Test
  @DisplayName("Should throw if the login was not found")
  void resendAccountConfirmationTokenCase2() {
    ResendAccountConfirmationTokenBodyDto resendAccountConfirmationTokenBodyDto = new ResendAccountConfirmationTokenBodyDto("fake@mail.com");

    when(loginRepository.findByEmail(resendAccountConfirmationTokenBodyDto.email())).thenReturn(Optional.empty());

    Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> this.authService.resendAccountConfirmationToken(resendAccountConfirmationTokenBodyDto));
    
    Assertions.assertEquals("User not found", exception.getMessage());
    ;
  }
}
