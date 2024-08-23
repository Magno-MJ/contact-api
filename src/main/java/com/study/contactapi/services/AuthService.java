package com.study.contactapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

@Service
public class AuthService {
  @Autowired
  AccountConfirmationTokenRepository accountConfirmationTokenRepository;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private PasswordEncoder passwordEncoder;


  public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginBodyDTO body){
    Login login = this.loginRepository.findByEmail(body.email()).orElseThrow(() -> new UserNotFoundException());
    
    if (login.getAccount_activated_at() == null) {
      throw new AccountNotActivatedException();
    }

    if (passwordEncoder.matches(body.password(), login.getPassword())) {
      String token = this.tokenService.generateToken(login);

      return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    throw new WrongCredentialsException();
  }


  public void confirmAccount(String accountConfirmationToken) {
    AccountConfirmationToken confirmationToken = this.accountConfirmationTokenRepository.findByToken(accountConfirmationToken);

    if (confirmationToken == null) {
      throw new ConfirmationTokenNotFoundException();
    };

    if (!confirmationToken.is_active()) {
      throw new ConfirmationTokenIsNotActiveException();
    };

    String loginId = this.tokenService.validateToken(accountConfirmationToken);

    if (loginId == null) {
      this.accountConfirmationTokenRepository.disableByToken(accountConfirmationToken);
      
      throw new InvalidTokenException();
    }

    this.loginRepository.activateUserLoginById(loginId);

    this.accountConfirmationTokenRepository.disableByToken(accountConfirmationToken);
  }

  public void resendAccountConfirmationToken(ResendAccountConfirmationTokenBodyDto resendAccountConfirmationTokenBodyDto) {
    Login login = loginRepository.findByEmail(resendAccountConfirmationTokenBodyDto.email()).orElseThrow(() -> new UserNotFoundException());

    String loginId = login.getId();

    String confirmationToken = this.tokenService.generateConfirmationToken(loginId);

    this.accountConfirmationTokenRepository.disableByLoginId(loginId);

    AccountConfirmationToken newAccountActivationToken = new AccountConfirmationToken(confirmationToken, login, true);

    this.accountConfirmationTokenRepository.save(newAccountActivationToken);

    this.emailService.sendAccountConfirmationMail(login.getEmail(), confirmationToken);
  }
}
