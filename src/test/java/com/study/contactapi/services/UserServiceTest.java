package com.study.contactapi.services;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.study.contactapi.domain.user.AccountConfirmationToken;
import com.study.contactapi.domain.user.Login;
import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.CreateUserBodyDTO;
import com.study.contactapi.dto.CreatedUserResponseDTO;
import com.study.contactapi.dto.LoginBodyDTO;
import com.study.contactapi.dto.LoginResponseDTO;
import com.study.contactapi.http.exceptions.ContactNotFoundException;
import com.study.contactapi.http.exceptions.UserAlreadyExistsException;
import com.study.contactapi.infra.security.TokenService;
import com.study.contactapi.mail.EmailService;
import com.study.contactapi.repositories.AccountConfirmationTokenRepository;
import com.study.contactapi.repositories.LoginRepository;
import com.study.contactapi.repositories.UserRepository;

public class UserServiceTest {
  @Mock
  private LoginRepository loginRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AccountConfirmationTokenRepository accountConfirmationTokenRepository;

  @Mock
  private TokenService tokenService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private EmailService emailService;

  @Autowired
  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Should create user successfully")
  void createUserCase1() {
    CreateUserBodyDTO createUserDto = new CreateUserBodyDTO("fake@mail.com", "fake name", "fake name", "fake password");

    when(loginRepository.findByEmail(createUserDto.email())).thenReturn(Optional.empty());

    String confirmationToken = "fake-token";

    when(tokenService.generateConfirmationToken(any())).thenReturn(confirmationToken);

    String encryptedPassword = "fake-encrypted-password";

    when(passwordEncoder.encode(createUserDto.password())).thenReturn(encryptedPassword);

    Login createdLogin = new Login(createUserDto.email(), encryptedPassword);
    User createdUser = new User(createUserDto.first_name(), createUserDto.last_name(), createdLogin);

    AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(confirmationToken, createdLogin, true);

    CreatedUserResponseDTO createdUserResponseDTO = this.userService.createUser(createUserDto);

    verify(loginRepository, times(1)).findByEmail(createUserDto.email());
    verify(passwordEncoder, times(1)).encode(createUserDto.password());
    verify(userRepository, times(1)).save(createdUser);
    verify(tokenService, times(1)).generateConfirmationToken(createdLogin.getId());
    verify(accountConfirmationTokenRepository, times(1)).save(accountConfirmationToken);

    assertThat(createdUserResponseDTO).isEqualTo(new CreatedUserResponseDTO(createdUser));
  }

  @Test
  @DisplayName("Should throw if user already exists")
  void createUserCase2() {
    CreateUserBodyDTO createUserDto = new CreateUserBodyDTO("fake@mail.com", "fake name", "fake name", "fake password");

    Login login = new Login();

    when(loginRepository.findByEmail(createUserDto.email())).thenReturn(Optional.of(login));
    
    Exception exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> this.userService.createUser(createUserDto));
    
    Assertions.assertEquals("User already exists", exception.getMessage());
  }
}
