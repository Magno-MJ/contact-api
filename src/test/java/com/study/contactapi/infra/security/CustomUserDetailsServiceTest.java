package com.study.contactapi.infra.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.study.contactapi.domain.user.Login;
import com.study.contactapi.repositories.LoginRepository;

public class CustomUserDetailsServiceTest {
  @Mock
  private LoginRepository loginRepository;

  @Autowired
  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Should load by username successfully")
  void loadByUserNameCase1() {
    String userName = "fake@mail.com";
    String password = "fake-password";

    Login login = new Login(userName, password);

    when(loginRepository.findByEmail(userName)).thenReturn(Optional.of(login));

    UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userName);

    verify(loginRepository, times(1)).findByEmail(userName);

    assertThat(userDetails).isEqualTo(new org.springframework.security.core.userdetails.User(login.getEmail(), login.getPassword(), new ArrayList<>()));
  }


  @Test
  @DisplayName("Should throw if the user was not found")
  void loadByUserNameCase2() {
    String userName = "fake@mail.com";

    when(loginRepository.findByEmail(userName)).thenReturn(Optional.empty());

    Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () ->this.customUserDetailsService.loadUserByUsername(userName));
    
    Assertions.assertEquals("User not found", exception.getMessage());
  }
}
